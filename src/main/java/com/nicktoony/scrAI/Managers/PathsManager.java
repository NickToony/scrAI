package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.global.GlobalVariables;
import com.nicktoony.screeps.global.ResponseTypes;
import com.nicktoony.screeps.global.StructureTypes;
import com.nicktoony.screeps.structures.Structure;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public class PathsManager extends Manager {
    private Map<String, Array<Map<String, Object>>> paths;
    private Structure baseStructure;
    private int roadsCreated = 0;
    private int position = 0;
    private int currentPosition = -1;
    private int total;
    private boolean found;
    private Map<String, Integer> pathTimes;

    public PathsManager(RoomController roomController, Map<String, Object> memory) {
        super(roomController, memory);
    }

    @Override
    protected void init() {
        memory.$put("paths", JSCollections.$map());
        memory.$put("pathTimes", JSCollections.$map());
        memory.$put("position", 0);
    }

    public void update() {
        Global.console.log("PathsManager -> Update");

        // temporary
        if (roomController.getRoom().controller.level < 3) {
            return;
        }

        // Load from memory
        this.paths = (Map<String, Array<Map<String, Object>>>) this.memory.$get("paths");
        this.baseStructure = null;
        this.roadsCreated = 0;

        // For all structure ids
        position = (Integer) memory.$get("position");
        pathTimes = (Map<String, Integer>) memory.$get("pathTimes");
        currentPosition = -1;
        total = 0;
        found = false;
        if (roomController.getConstructionManager().getTotalConstructions() == 0) {
            Lodash.forIn(roomController.getStructureManager().getRoadableStructureIds(), new LodashCallback1<String>() {
                @Override
                public boolean invoke(String structureId) {

                    currentPosition++;
                    total ++;

                    if (currentPosition == 0) {
                        // Attempt to find the specified structure
                        Structure structure = (Structure) Game.getObjectById(structureId);
                        // If it exists
                        if (structure != null) {

                            // Do we have a base structure?
                            if (baseStructure == null) {
                                // Use as base structure, don't calculate path
                                baseStructure = structure;
                                return true;
                            }
                        }
                    } else if (currentPosition == position) {

                        // Attempt to find the specified structure
                        Structure structure = (Structure) Game.getObjectById(structureId);
                        // If it exists
                        if (structure != null) {
                            if (roadTo(structure, structureId, pathTimes.$get(structureId))) {
                                position ++;
                            }
                        } else {
                            // structure doesn't exist.. just skip to next
                            position ++;
                        }
                        found = true;
                        return false;
                    }

                    return true;
                }
            }, this);
        }

        if (!found) {
            position ++;
        }
        // Do the next position next time
        if (position > total) {
            position = 0;
        }

        memory.$put("position", position);
        memory.$put("pathTimes", pathTimes);
    }

    /**
     *
     * @param structure
     * @param structureId
     * @return true if all roads for path built
     */
    private boolean roadTo(Structure structure, String structureId, Integer pathTime) {
        // Check if it has a path
        Array<Map<String, Object>> path = paths.$get(structureId);

        // No path? Generate one
        if (path == null || pathTime == null || pathTime + Constants.SETTINGS_PATH_EXPIRE_TIME < Game.time) {

            Global.console.log("Recalculating Path");

            // Find the path
            Map<String, Object> parameters = JSCollections.$map();
            parameters.$put("ignoreCreeps", true);
            parameters.$put("ignoreDestructibleStructures", false);
            parameters.$put("heuristicWeight", 100);
            paths.$put(structureId, roomController.getRoom().findPath(baseStructure.pos, structure.pos, parameters));
            pathTimes.$put(structureId, Game.time);

            roomController.hasPathFound = true;

            // Don't do anymore paths, it's a lot of CPU
            return false;
        } else {

            Global.console.log("RUNNING PATH MANAGER: BUILTPATH");

            // For each step of the path
            roadsCreated = 0;
            Lodash.forIn(path, new LodashCallback1<Map<String, Object>>() {
                @Override
                public boolean invoke(Map<String, Object> pathStep) {

                    // Create a road for the path position
                    int x = (Integer) pathStep.$get("x");
                    int y = (Integer) pathStep.$get("y");
                    if (roomController.getRoom().createConstructionSite(x, y, StructureTypes.STRUCTURE_ROAD) == ResponseTypes.OK) {
                        roadsCreated++;
                        roomController.getConstructionManager().addConstruction();
                    }

                    // Keep going as long as not created too many roads
                    return (roadsCreated < Constants.SETTING_MAX_ROAD_CREATE);
                }
            }, this);

            return roadsCreated == 0;
        }
    }
}
