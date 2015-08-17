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
import com.nicktoony.screeps.global.ScreepsObject;
import com.nicktoony.screeps.structures.Spawn;
import com.nicktoony.screeps.structures.Structure;
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
    private Map<String, Object> tempMemory;

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

        if (stage == 0) {
            Global.console.log("PLANNING -> INIT");

            // reset everything
            init();
            // next stage
            stage ++;
        } else if (stage == 1) {
            Global.console.log("PLANNING -> MINING LOCATIONS");

            // Plan mining locations
            planMiners();

            // next stage
            stage ++;
        } else if (stage == 2) {
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

            if (planStructures(2, 50, 2, 10, callback)) {
                stage ++;
            }
        } else if (stage == 3) {
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
        } else if (stage == 4) {
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

    private void planMiners() {
        Array<Source> sources = (Array<Source>) roomController.room.find(FindTypes.FIND_SOURCES, null);

        Lodash.forIn(sources, new LodashCallback1<Source>() {
            @Override
            public boolean invoke(Source source) {
                planSource(source);
                return true;
            }
        }, this);

    }

    private void planSource(final Source source) {
        Array<RoomPosition> roomPositions = new Array<RoomPosition>();
        for (int x = -1; x <= 1; x ++) {
            for (int y = -1; y <= 1; y ++) {
                RoomPosition position = new RoomPosition(source.pos.x + x, source.pos.y + y, source.pos.roomName);
                Array objects = position.lookFor("terrain");
                if (objects.$length() > 0 && isTerrainClear(objects.$get(0)) && (x != 0 || y != 0)) {
                    roomPositions.push(position);
                }
            }
        }

        Lodash.sortBy(roomPositions, new LodashSortCallback1<RoomPosition>() {
            @Override
            public int invoke(RoomPosition roomPosition) {
                return roomPosition.getRangeTo(source.pos);
            }
        }, this);

        int totalSpots = Math.min(Constants.SETTING_MINER_PER_SPAWN, roomPositions.$length());
        float optimalWork = (source.energyCapacity / Constants.SOURCE_REGEN) / Constants.HARVEST_PER_WORK;
        int totalCovered = 0;
        for (int i = 0; i < totalSpots; i ++) {
            double value = Math.floor(optimalWork / totalSpots);
            if (i + 1 == totalSpots) {
                value = optimalWork - totalCovered;
            }
            totalCovered += value;

            RoomPosition position = roomPositions.$get(i);
            position.createFlag(null, ColorTypes.COLOR_BLUE);
            Map<String, Object> map = JSCollections.$map();
            map.$put("x", position.x);
            map.$put("y", position.y);
            map.$put("value", value);
            minerLocations.push(map);
        }

    }

    private boolean isTerrainClear(Object terrain) {
        return (terrain == "plain" || terrain == "swamp");
    }

    public abstract class PlanStructureCallback {
        protected RoomPlanner self;
        public PlanStructureCallback(RoomPlanner roomPlanner) {
            this.self = roomPlanner;
        }
        public abstract boolean callback(RoomPosition roomPosition);
    }

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
            count += buildFlags(topLeftX, topLeftY, topLeftX, topLeftY + layer * 2, spacing, planStructureCallback);
        } else if (state == 1 ) {
            // Right side
            count += buildFlags(topLeftX + layer * 2, topLeftY, topLeftX + layer * 2, topLeftY + layer * 2, spacing, planStructureCallback);
        } else if (state == 2) {
            // Top side
            count += buildFlags(topLeftX + spacing, topLeftY, topLeftX + layer * 2 - spacing, topLeftY, spacing, planStructureCallback);
        } else if (state == 3 ) {
            // Bottom side
            count += buildFlags(topLeftX + spacing, topLeftY + layer * 2, topLeftX + layer * 2 - spacing, topLeftY + layer * 2, spacing, planStructureCallback);
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

    private int buildFlags(int fromX, int fromY, int toX, int toY, int spacing, PlanStructureCallback planStructureCallback) {
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

    private Array<RoomPosition> calculateAvoidAreas(boolean miners, boolean extensions) {
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
        return positions;
    }

    private boolean planPaths() {
        final Array<RoomPosition> avoidPositions = calculateAvoidAreas(true, true);

        Spawn spawn = (Spawn) roomController.room.find(FindTypes.FIND_MY_SPAWNS, null).$get(0);
        if (spawn == null) {
            return false;
        }
        final RoomPosition startPosition = new RoomPosition(spawn.pos.x + 1, spawn.pos.y, spawn.pos.roomName);

        Lodash.forIn(minerLocations, new LodashCallback1<Map<String, Object>>() {
            @Override
            public boolean invoke(Map<String, Object> variable) {
                createPath(startPosition,
                        new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), startPosition.roomName),
                        avoidPositions);
                return true;
            }
        }, this);

        Lodash.forIn(extensionLocations, new LodashCallback1<Map<String, Object>>() {
            @Override
            public boolean invoke(Map<String, Object> variable) {
                createPath(startPosition,
                        new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), startPosition.roomName),
                        avoidPositions);
                return true;
            }
        }, this);

        RoomPosition exits;
        exits = (RoomPosition) startPosition.findClosest(FindTypes.FIND_EXIT_BOTTOM, null);
        Global.console.log(exits);
        if (exits != null) {
            createPath(startPosition, exits, avoidPositions);
            Global.console.log("FOUND");
        }
        exits = (RoomPosition) startPosition.findClosest(FindTypes.FIND_EXIT_TOP, null);
        if (exits != null) {
            createPath(startPosition, exits, avoidPositions);
            Global.console.log("FOUND");
        }
        exits = (RoomPosition) startPosition.findClosest(FindTypes.FIND_EXIT_LEFT, null);
        if (exits != null) {
            createPath(startPosition, exits, avoidPositions);
            Global.console.log("FOUND");
        }
        exits = (RoomPosition) startPosition.findClosest(FindTypes.FIND_EXIT_RIGHT, null);
        if (exits != null) {
            createPath(startPosition, exits, avoidPositions);
            Global.console.log("FOUND");
        }

        return true;
    }

    private void createPath(final RoomPosition from, final RoomPosition to, Array<RoomPosition> avoids) {
        Map<String, Object> options = JSCollections.$map();
        options.$put("avoid", avoids);
        options.$put("heuristicWeight", 100);
        Array<Map<String, Object>> path = roomController.room.findPath(from, to, options);
        Lodash.forIn(path, new LodashCallback1<Map<String, Object>>() {
            @Override
            public boolean invoke(Map<String, Object> variable) {
                RoomPosition position = new RoomPosition((Integer) variable.$get("x"), (Integer) variable.$get("y"), from.roomName);
//                if (position.x != from.x && position.y != from.y)
                    if (position.x != to.x && position.y != to.y) {
                        position.createFlag(null, ColorTypes.COLOR_GREEN);
                    }
                return true;
            }
        }, this);
    }
}
