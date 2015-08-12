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

    public PathsManager(RoomController roomController, Map<String, Object> memory) {
        super(roomController, memory);
    }

    @Override
    protected void init() {
        memory.$put("paths", JSCollections.$map());
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
        if (roomController.getConstructionManager().getTotalConstructions() == 0) {
            Lodash.forIn(roomController.getStructureManager().getRoadableStructureIds(), new LodashCallback1<String>() {
                @Override
                public boolean invoke(String structureId) {

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

                        // Check if it has a path
                        Array<Map<String, Object>> path = paths.$get(structureId);

                        // No path? Generate one
                        if (path == null) {

                            // Find the path
                            Map<String, Object> parameters = JSCollections.$map();
                            parameters.$put("ignoreCreeps", true);
                            parameters.$put("ignoreDestructibleStructures", true);
                            parameters.$put("heuristicWeight", 100);
                            paths.$put(structureId, roomController.getRoom().findPath(baseStructure.pos, structure.pos, parameters));

                            roomController.hasPathFound = true;

                            // Don't do anymore paths, it's a lot of CPU
                            return false;
                        } else {

                            Global.console.log("RUNNING PATH MANAGER: BUILTPATH");

                            // For each step of the path
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

                            return true;
                        }
                    } else {
                        return true;
                    }
                }
            }, this);
        }
    }
}
