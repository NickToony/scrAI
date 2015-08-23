package com.nicktoony.scrAI.planning;

import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.controllers.MemoryController;
import com.nicktoony.scrAI.controllers.RoomController;
import com.nicktoony.screeps.Flag;
import com.nicktoony.screeps.RoomPosition;
import com.nicktoony.screeps.Source;
import com.nicktoony.screeps.callbacks.Lodash;
import com.nicktoony.screeps.callbacks.LodashCallback1;
import com.nicktoony.screeps.callbacks.LodashSortCallback1;
import com.nicktoony.screeps.global.ColorTypes;
import com.nicktoony.screeps.global.FindTypes;
import com.nicktoony.screeps.helpers.Path;
import com.nicktoony.screeps.structures.Controller;
import com.nicktoony.screeps.structures.Spawn;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;
import org.stjs.javascript.functions.Callback1;

/**
 * Created by nick on 16/08/15.
 */
public class RoomPlanner extends MemoryController {

    private Array<Map<String, Object>> minerLocations;
    private Array<Map<String, Object>> extensionLocations;
    private Array<Map<String, Object>> upgraderLocations;
    private Map<String, Path> paths;
    private Map<String, Object> tempMemory;
    private RoomPosition tempPosition;
    private int tempDistance = 0;

    public RoomPlanner(Map<String, Object> memory, RoomController roomController) {
        super(memory, roomController);
    }

    @Override
    public void init() {
        super.init();

        // stage
        memory.$put("stage", 0);

        // Clear all stored memory
        memory.$put("minerLocations", new Array<Map<String, Object>>());
        memory.$put("extensionLocations", new Array<Map<String, Object>>());
        memory.$put("upgraderLocations", new Array<Map<String, Object>>());
        memory.$put("paths", JSCollections.$map());

        // clear temp memory
        memory.$put("tempMemory", JSCollections.$map());

        // Clear all current flags, we're planning again.
        Array<Flag> flags = (Array<Flag>) roomController.room.find(FindTypes.FIND_FLAGS, null);
        flags.forEach(new Callback1<Flag>() {
            @Override
            public void $invoke(Flag flag) {
                flag.remove();
            }
        });
    }

    public void plan() {
        int stage = (Integer) memory.$get("stage");
        int initialStage = stage;
        tempMemory = (Map<String, Object>) memory.$get("tempMemory");

        // miner locations memory
        this.minerLocations = (Array<Map<String, Object>>) memory.$get("minerLocations");
        // extension locations memory
        this.extensionLocations = (Array<Map<String, Object>>) memory.$get("extensionLocations");
        // upgrader locations memory
        this.upgraderLocations = (Array<Map<String, Object>>) memory.$get("upgraderLocations");
        // path storage
        this.paths = (Map<String, Path>) memory.$get("paths");

        if (stage == 0) {
            Global.console.log("PLANNING -> INIT");

            // reset everything
            init();
            // next stage
            stage ++;
        } else if (stage == 1) {
            Global.console.log("PLANNING -> MINING LOCATIONS");

            // Plan mining locations
            if (planMiners()) {
                // next stage
                stage++;
            }
        } else if (stage == 2) {
            Global.console.log("PLANNING -> UPGRADER LOCATIONS");

            // Plan mining locations
            if (planUpgraders()) {
                // next stage
                stage++;
            }
        } else if (stage == 3) {
            Global.console.log("PLANNING -> EXTENSION LOCATIONS");

            PlanStructureCallback callback = new PlanStructureCallback(this) {
                @Override
                public boolean callback(RoomPosition roomPosition) {
                    roomPosition.createFlag(null, ColorTypes.COLOR_RED);
                    Map<String, Object> map = JSCollections.$map();
                    map.$put("x", roomPosition.x);
                    map.$put("y", roomPosition.y);
                    self.extensionLocations.push(map);
                    return true;
                }
            };
//
//            if (planStructures(2, 50, 2, 10, callback)) {
////                stage ++;
//                stage = 20;
//            }

            if (planExtensions(true, 50, callback)) {
                stage ++;
            }
        } else if (stage == 4) {
            Global.console.log("PLANNING -> PLANNING WALLS");

            PlanStructureCallback callback = new PlanStructureCallback(this) {
                @Override
                public boolean callback(RoomPosition roomPosition) {
                    roomPosition.createFlag(null, ColorTypes.COLOR_BROWN);
                    return true;
                }
            };
            if (planStructures(1, -1, 13, 13, callback)) {
                stage ++;
            }
        } else if (stage == 5) {
            Global.console.log("PLANNING -> PLANNING PATHS");

            if (planPaths()) {
                stage ++;
            }
        }

        if (stage != initialStage) {
            memory.$put("tempMemory", JSCollections.$map());
        }
        memory.$put("stage", stage);
    }

    /**
     * for all sources in the room, plan the amount of miners for each
     * @return
     */
    private boolean planMiners() {
        // get base
        final Spawn spawn = (Spawn) roomController.room.find(FindTypes.FIND_MY_SPAWNS, null).$get(0);
        if (spawn == null) return false;

        // Get all sources
        Array<Source> sources = (Array<Source>) roomController.room.find(FindTypes.FIND_SOURCES, null);

        // For each source
        Lodash.forIn(sources, new LodashCallback1<Source>() {
            @Override
            public boolean invoke(Source source) {
                // Plan how many miners for that source
                planSource(source, spawn.pos);
                return true;
            }
        }, this);

        return true;
    }

    /**
     * Plan the positions of upgraders
     * @return
     */
    private boolean planUpgraders() {
        // get base
        final Spawn spawn = (Spawn) roomController.room.find(FindTypes.FIND_MY_SPAWNS, null).$get(0);
        if (spawn == null) return false;

        // Get all sources
        Controller controller = roomController.room.controller;
        if (controller == null) return false;

        // For all spaces immediately around the controller
        for (int x = -1; x <= 1; x ++) {
            for (int y = -1; y <= 1; y ++) {
                // If the position is clear
                RoomPosition position = new RoomPosition(controller.pos.x + x, controller.pos.y + y, controller.pos.roomName);
                Array objects = position.lookFor("terrain");
                if (objects.$length() > 0 && isTerrainClear(objects.$get(0)) && (x != 0 || y != 0)) {
                    // create a flag
                    position.createFlag(null, ColorTypes.COLOR_BLUE);
                    // Store the position
                    Map<String, Object> map = JSCollections.$map();
                    map.$put("x", position.x);
                    map.$put("y", position.y);
                    upgraderLocations.push(map);
                }
            }
        }

        return true;
    }

    /**
     * Plans the number of miners for the given source
     * @param source
     */
    private void planSource(final Source source, final RoomPosition base) {
        Array<RoomPosition> roomPositions = new Array<RoomPosition>();
        // For all spaces immediately around the source
        for (int x = -1; x <= 1; x ++) {
            for (int y = -1; y <= 1; y ++) {
                // If the position is clear
                RoomPosition position = new RoomPosition(source.pos.x + x, source.pos.y + y, source.pos.roomName);
                Array objects = position.lookFor("terrain");
                // if is free
                if (objects.$length() > 0 && isTerrainClear(objects.$get(0)) && (x != 0 || y != 0)) {
                    roomPositions.push(position);
                }
            }
        }

        // Sort by range from spawn
        Lodash.sortBy(roomPositions, new LodashSortCallback1<RoomPosition>() {
            @Override
            public int invoke(RoomPosition roomPosition) {
                return roomPosition.getRangeTo(base);
            }
        }, this);

        // Calculate total available spots
        int totalSpots = Math.min(Constants.SETTING_MINER_PER_SPAWN, roomPositions.$length());
        // Calculate optimal work parts for that source
        float optimalWork = (source.energyCapacity / Constants.SOURCE_REGEN) / Constants.HARVEST_PER_WORK;
        int totalCovered = 0;
        // for each spot
        for (int i = 0; i < totalSpots; i ++) {
            // Calculate the creeps ideal work parts
            double value = Math.floor(optimalWork / totalSpots);
            // If it's the final spot
            if (i + 1 == totalSpots) {
                // just give this creep the remaining work parts
                value = optimalWork - totalCovered;
            }
            totalCovered += value;

            // get the room position
            RoomPosition position = roomPositions.$get(i);
            // create a flag
            position.createFlag(null, ColorTypes.COLOR_BLUE);
            // Store the position
            Map<String, Object> map = JSCollections.$map();
            map.$put("x", position.x);
            map.$put("y", position.y);
            map.$put("value", value);
            minerLocations.push(map);
        }

    }

    /**
     * Returns whether the terrain is free to move/build upon
     * @param terrain
     * @return
     */
    private boolean isTerrainClear(Object terrain) {
        return (terrain == "plain" || terrain == "swamp");
    }

    /**
     * A callback class. Binds to the RoomPlanner
     */
    public abstract class PlanStructureCallback {
        protected RoomPlanner self;
        public PlanStructureCallback(RoomPlanner roomPlanner) {
            this.self = roomPlanner;
        }
        public abstract boolean callback(RoomPosition roomPosition);
    }

    /**
     * Plan the position of structures of given type
     * @param spacing
     * @param maxBuild
     * @param layerFrom
     * @param layerTo
     * @param planStructureCallback
     * @return
     */
    private boolean planStructures(int spacing, int maxBuild, int layerFrom, int layerTo, PlanStructureCallback planStructureCallback) {
        Spawn spawn = (Spawn) roomController.room.find(FindTypes.FIND_MY_SPAWNS, null).$get(0);
        if (spawn == null) return false;

        Integer layer = (Integer) tempMemory.$get("layer");
        Integer state = (Integer) tempMemory.$get("state");
        Integer count = (Integer) tempMemory.$get("count");
        if (layer == null) {
            state = 0;
            layer = layerFrom;
            count = 0;
        }

        int topLeftX = spawn.pos.x-layer;
        int topLeftY = spawn.pos.y-layer;

        if (state == 0) {
            // Left side
            count += buildStructures(topLeftX, topLeftY, topLeftX, topLeftY + layer * 2, spacing, planStructureCallback);
        } else if (state == 1 ) {
            // Right side
            count += buildStructures(topLeftX + layer * 2, topLeftY, topLeftX + layer * 2, topLeftY + layer * 2, spacing, planStructureCallback);
        } else if (state == 2) {
            // Top side
            count += buildStructures(topLeftX + spacing, topLeftY, topLeftX + layer * 2 - spacing, topLeftY, spacing, planStructureCallback);
        } else if (state == 3 ) {
            // Bottom side
            count += buildStructures(topLeftX + spacing, topLeftY + layer * 2, topLeftX + layer * 2 - spacing, topLeftY + layer * 2, spacing, planStructureCallback);
        }

        state ++;
        // If layer is complete
        if (state >= 4) {
            // go to next layer
            state = 0;
            layer ++;
        }
        // If gone beyond 10, that's not good
        if (layer > layerTo || (count > maxBuild && maxBuild != -1)) {
            return true;
        }
        tempMemory.$put("layer", layer);
        tempMemory.$put("state", state);
        tempMemory.$put("count", count);
        return false;
    }

    private boolean planExtensions(boolean spacing, int maxBuild,  PlanStructureCallback planStructureCallback) {
        Spawn spawn = (Spawn) roomController.room.find(FindTypes.FIND_MY_SPAWNS, null).$get(0);
        if (spawn == null) return false;

        Map<String, Object> spacesDone;
        Map<String, Object> builtSpaces;
        Array<Integer> spacesNextX;
        Array<Integer> spacesNextY;
        int innerX;
        int innerY;
        boolean terrainClear;
        int clearSpaces;

        if (tempMemory.$get("init") == null) {
            tempMemory.$put("init", true);
            tempMemory.$put("built", 0);

            spacesDone = JSCollections.$map();
            spacesNextX = new Array<Integer>();
            spacesNextY = new Array<Integer>();
            for (int x = -2; x <= 2; x ++) {
                for (int y = -2; y <= 2; y ++) {
                    innerX = spawn.pos.x + x;
                    innerY = spawn.pos.y + y;

                    if (Math.abs(x) != 2 && Math.abs(y) != 2) {

                    } else {
                        if (isTerrainClear(new RoomPosition(innerX, innerY, spawn.pos.roomName).lookFor("terrain"))) {
                            spacesNextX.push(innerX);
                            spacesNextY.push(innerY);
                        }
                    }
                    spacesDone.$put(innerX + "," + innerY, true);
                }
            }
            tempMemory.$put("spacesDone", spacesDone);
            tempMemory.$put("builtSpaces", JSCollections.$map());
            tempMemory.$put("spacesNextX", spacesNextX);
            tempMemory.$put("spacesNextY", spacesNextY);
        }

        int built = (Integer) tempMemory.$get("built");
        spacesDone = (Map<String, Object>) tempMemory.$get("spacesDone");
        builtSpaces = (Map<String, Object>) tempMemory.$get("builtSpaces");
        spacesNextX = (Array<Integer>) tempMemory.$get("spacesNextX");
        spacesNextY = (Array<Integer>) tempMemory.$get("spacesNextY");

        int currentX = spacesNextX.shift();
        int currentY = spacesNextY.shift();

        spacesDone.$put(currentX + "," + currentY, true);

        clearSpaces = 0;
        for (int x = -1; x <= 1; x ++) {
            for (int y = -1; y <= 1; y++) {
                innerX = currentX + x;
                innerY = currentY + y;
                terrainClear = isTerrainClear(roomController.room.lookForAt("terrain", innerX, innerY));

                if (spacesDone.$get(innerX + "," + innerY) == null
                        && terrainClear) {
                    spacesNextX.push(innerX);
                    spacesNextY.push(innerY);
                    spacesDone.$put(innerX + "," + innerY, true);
                }

                if (terrainClear && (x != 0 && y != 0)) {
                    clearSpaces ++;
                }
            }
        }

        boolean clear = true;
        if (builtSpaces.$get((currentX+1) + "," + (currentY)) != null) {
            clearSpaces -= 1;
            clear = false;
        }
        if (builtSpaces.$get((currentX-1) + "," + (currentY)) != null) {
            clearSpaces -= 1;
            clear = false;
        }
        if (builtSpaces.$get((currentX) + "," + (currentY+1)) != null) {
            clearSpaces -= 1;
            clear = false;
        }
        if (builtSpaces.$get((currentX) + "," + (currentY-1)) != null) {
            clearSpaces -= 1;
            clear = false;
        }
        if (clear || clearSpaces < 2)
        {
            if (planStructureCallback.callback(new RoomPosition(currentX, currentY, spawn.pos.roomName))) {
                builtSpaces.$put(currentX + "," + currentY, true);
                built += 1;
            }
        }

        tempMemory.$put("built", built);
        if (built > maxBuild) {
            return true;
        }

        return false;
    }

    /**
     * Build flags
     * @param fromX
     * @param fromY
     * @param toX
     * @param toY
     * @param spacing
     * @param planStructureCallback
     * @return
     */
    private int buildStructures(int fromX, int fromY, int toX, int toY, int spacing, PlanStructureCallback planStructureCallback) {
        int count = 0;
        for (int x = fromX; x <= toX; x += spacing) {
            for (int y = fromY; y <= toY; y+= spacing) {
                RoomPosition position = new RoomPosition(x, y, roomController.room.name);
                Array objects = position.lookFor("terrain");
                if (objects.$length() > 0 && isTerrainClear(objects.$get(0))) {
                    if (planStructureCallback.callback(position)) {
                        count++;
                    }
                }
            }
        }

        return count;
    }

    /**
     * Calculates the avoid area
     * @param miners
     * @param extensions
     * @return
     */
    private Array<RoomPosition> calculateAvoidAreas(boolean miners, boolean extensions, boolean upgraders) {
        final Array<RoomPosition> positions = new Array<RoomPosition>();
        if (miners) {
            Lodash.forIn(minerLocations, new LodashCallback1<Map<String, Object>>() {
                @Override
                public boolean invoke(Map<String, Object> variable) {
                    RoomPosition roomPosition = new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), roomController.room.name);
                    positions.push(roomPosition);
                    return true;
                }
            }, this);
        }
        if (extensions) {
            Lodash.forIn(extensionLocations, new LodashCallback1<Map<String, Object>>() {
                @Override
                public boolean invoke(Map<String, Object> variable) {
                    RoomPosition roomPosition = new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), roomController.room.name);
                    positions.push(roomPosition);
                    return true;
                }
            }, this);
        }
        if (upgraders) {
            Lodash.forIn(upgraderLocations, new LodashCallback1<Map<String, Object>>() {
                @Override
                public boolean invoke(Map<String, Object> variable) {
                    RoomPosition roomPosition = new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), roomController.room.name);
                    positions.push(roomPosition);
                    return true;
                }
            }, this);
        }
        return positions;
    }

    /**
     * Plan the creep paths
     * @return
     */
    private boolean planPaths() {
        final Array<RoomPosition> avoidPositions = calculateAvoidAreas(true, true, true);

        Spawn spawn = (Spawn) roomController.room.find(FindTypes.FIND_MY_SPAWNS, null).$get(0);
        if (spawn == null) {
            return false;
        }
        final RoomPosition startPosition = new RoomPosition(spawn.pos.x + 1, spawn.pos.y, spawn.pos.roomName);

        // find paths to all miner locations (combine: yes)
        Lodash.forIn(minerLocations, new LodashCallback1<Map<String, Object>>() {
            @Override
            public boolean invoke(Map<String, Object> variable) {
                createPath(startPosition,
                        new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), startPosition.roomName),
                        avoidPositions,
                        true);
                return true;
            }
        }, this);

        // Find paths to all extension locations (combine: false)
        Lodash.forIn(extensionLocations, new LodashCallback1<Map<String, Object>>() {
            @Override
            public boolean invoke(Map<String, Object> variable) {
                createPath(startPosition,
                        new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), startPosition.roomName),
                        avoidPositions,
                        false);
                return true;
            }
        }, this);

        // Find paths to all upgrader locations (combine: true)
        Lodash.forIn(upgraderLocations, new LodashCallback1<Map<String, Object>>() {
            @Override
            public boolean invoke(Map<String, Object> variable) {
                createPath(startPosition,
                        new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), startPosition.roomName),
                        avoidPositions,
                        true);
                return true;
            }
        }, this);

        RoomPosition exits;
        exits = (RoomPosition) startPosition.findClosest(FindTypes.FIND_EXIT_BOTTOM, null);
        Global.console.log(exits);
        if (exits != null) {
            createPath(startPosition, exits, avoidPositions, true);
            Global.console.log("FOUND");
        }
        exits = (RoomPosition) startPosition.findClosest(FindTypes.FIND_EXIT_TOP, null);
        if (exits != null) {
            createPath(startPosition, exits, avoidPositions, true);
            Global.console.log("FOUND");
        }
        exits = (RoomPosition) startPosition.findClosest(FindTypes.FIND_EXIT_LEFT, null);
        if (exits != null) {
            createPath(startPosition, exits, avoidPositions, true);
            Global.console.log("FOUND");
        }
        exits = (RoomPosition) startPosition.findClosest(FindTypes.FIND_EXIT_RIGHT, null);
        if (exits != null) {
            createPath(startPosition, exits, avoidPositions, true);
            Global.console.log("FOUND");
        }

        return true;
    }

    /**
     * Create a path from one position to another
     * @param from
     * @param to
     * @param avoids
     * @param reuse
     */
    private void createPath(final RoomPosition from, final RoomPosition to, Array<RoomPosition> avoids, boolean reuse) {
        Map<String, Object> options = JSCollections.$map();
        options.$put("avoid", avoids);
        options.$put("heuristicWeight", 100);

        RoomPosition intermediateObject = null;
        if (reuse) {
            reuse = false;
            intermediateObject = findClosest(to, avoids);
            // if another nearby object is found, and it's within 8, and it has a path
            if (intermediateObject != null && intermediateObject.getRangeTo(to) < 5) {
                Path path = loadPath(from, intermediateObject);
                if (path != null) {
                    Map<String, Object> pos = path.$get(path.$length() - 2);
                    intermediateObject = new RoomPosition((Integer) pos.$get("x"), (Integer) pos.$get("y"), intermediateObject.roomName);
                    reuse = true;
                }
            }
        }


        Array<Map<String, Object>> path;
        if (reuse) {
            path = roomController.room.findPath(intermediateObject, to, options);
            Lodash.forIn(path, new LodashCallback1<Map<String, Object>>() {
                @Override
                public boolean invoke(Map<String, Object> variable) {
                    RoomPosition position = new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), from.roomName);
//                if (position.x != from.x && position.y != from.y)
                    if (position.x != to.x || position.y != to.y) {
                        position.createFlag(null, ColorTypes.COLOR_YELLOW);
                    }
                    return true;
                }
            }, this);
        } else {
            path = roomController.room.findPath(from, to, options);
            Lodash.forIn(path, new LodashCallback1<Map<String, Object>>() {
                @Override
                public boolean invoke(Map<String, Object> variable) {
                    RoomPosition position = new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), from.roomName);
//                if (position.x != from.x && position.y != from.y)
                    if (position.x != to.x || position.y != to.y) {
                        position.createFlag(null, ColorTypes.COLOR_GREEN);
                    }
                    return true;
                }
            }, this);
        }

        savePath(from, to, (Path) path);
    }

    /**
     * Store the path in memory
     * @param from
     * @param to
     * @param path
     */
    private void savePath(RoomPosition from, RoomPosition to, Path path) {
        this.paths.$put(from.x + "," + from.y + "->" + to.x + "," + to.y, path);
    }

    /**
     * Get a path from memory
     * @param from
     * @param to
     * @return
     */
    private Path loadPath(RoomPosition from, RoomPosition to) {
        return this.paths.$get(from.x + "," + from.y + "->" + to.x + "," + to.y);
    }

    /**
     * Find the closest point in an array of roomPositions
     * @param position
     * @param avoids
     * @return
     */
    private RoomPosition findClosest(final RoomPosition position, Array<RoomPosition> avoids) {
        tempPosition = null;
        tempDistance = 0;
        Lodash.forIn(avoids, new LodashCallback1<RoomPosition>() {
            @Override
            public boolean invoke(RoomPosition variable) {
                if (variable.x != position.x || variable.y != position.y) {
                    if (tempPosition == null || position.getRangeTo(variable) < tempDistance) {
                        tempPosition = variable;
                        tempDistance = position.getRangeTo(variable);
                    }
                }
                return true;
            }
        }, this);
        return tempPosition;
    }
}
