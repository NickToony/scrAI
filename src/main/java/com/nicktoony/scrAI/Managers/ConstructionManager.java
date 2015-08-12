package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.TaskBuild;
import com.nicktoony.screeps.ConstructionSite;
import com.nicktoony.screeps.RoomPosition;
import com.nicktoony.screeps.global.FindTypes;
import com.nicktoony.screeps.global.GlobalVariables;
import com.nicktoony.screeps.global.ResponseTypes;
import com.nicktoony.screeps.global.StructureTypes;
import com.nicktoony.screeps.structures.Spawn;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class ConstructionManager extends Manager {
    public ConstructionManager(RoomController roomController, Map<String, Object> memory) {
        super(roomController, memory);
    }

    @Override
    protected void init() {
        memory.$put("layer", 3);
        memory.$put("state", 0);
    }

    public void update() {
        Global.console.log("ConstructionManager -> Update");

        // Fetch ALL energy
        Array<ConstructionSite> foundSites = (Array<ConstructionSite>) this.roomController.getRoom().find(FindTypes.FIND_CONSTRUCTION_SITES,
                null);

        Lodash.forIn(foundSites, new LodashCallback1<ConstructionSite>() {
            @Override
            public boolean invoke(ConstructionSite constructionSite) {
                if (roomController.getTasksManager().getTaskMemory().$get(constructionSite.id) == null) {
                    roomController.getTasksManager().addTask(new TaskBuild(roomController, constructionSite.id, constructionSite));
                }
                return true;
            }
        }, this);


        // no build sites?
        if (foundSites.$length() == 0) {
            Spawn spawn = roomController.getSpawnsManager().getSpawns().$get(0);
            if (spawn == null) return;

            int layer = (Integer) memory.$get("layer");
            int state = (Integer) memory.$get("state");
            Global.console.log("LAYER: " + layer);

            int topLeftX = spawn.pos.x-layer;
            int topLeftY = spawn.pos.y-layer;

            // Left side
            if (state == 0 && tryToBuildSpawn(topLeftX, topLeftY,topLeftX, topLeftY + layer * 2)) {
                memory.$put("state", state);
                return;
            } else if (state == 0) {
                state = 1;
            }
            // Right side
            if (state == 1 && tryToBuildSpawn(topLeftX + layer * 2, topLeftY,topLeftX + layer * 2, topLeftY + layer * 2)) {
                memory.$put("state", state);
                return;
            } else if (state == 1) {
                state = 2;
            }
            // Top side
            if (state == 2 && tryToBuildSpawn(topLeftX, topLeftY,topLeftX + layer * 2, topLeftY)) {
                memory.$put("state", state);
                return;
            } else if (state == 2) {
                state = 3;
            }
            // Bottom side
            if (state == 3 && tryToBuildSpawn(topLeftX, topLeftY + layer * 2,topLeftX + layer * 2, topLeftY + layer * 2)) {
                memory.$put("state", state);
                return;
            } else if (state == 3) {
                state = 4;
            }

            layer += 2;
            state = 0;
            // If gone beyond 10, that's not good
            if (layer > 10) layer = 3;
            memory.$put("layer", layer);
            memory.$put("state", state);
        }
    }

    private boolean tryToBuildSpawn(int fromX, int fromY, int toX, int toY) {
        for (int x = fromX; x <= toX; x += 2) {
            for (int y = fromY; y <= toY; y+= 2) {
                RoomPosition position = new RoomPosition(x, y, roomController.getRoom().name);
                Array objects = position.lookFor("terrain");
                if (objects.$length() > 0 && objects.$get(0) == "plain") {
                    ResponseTypes created = position.createConstructionSite(StructureTypes.STRUCTURE_EXTENSION);
                    if (created == ResponseTypes.OK) {
                        return true;
                    } else if (created == ResponseTypes.ERR_RCL_NOT_ENOUGH) {
                        return true;
                    } else {
                        // continue and try again
                    }
                }
            }
        }

        return false;
    }
}
