package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.TemporaryVariables;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.TaskDeposit;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.Spawn;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Lodash = require('lodash');
 */
public class SpawnsManager extends Manager {
    private Array<Spawn> spawns;

    public SpawnsManager(RoomController roomControllerParam, Map<String, Object> memory) {
        super(roomControllerParam, memory);

        this.spawns = (Array<Spawn>) this.roomController.getRoom().find(GlobalVariables.FIND_MY_SPAWNS, null);
        Lodash.forIn(spawns, new LodashCallback1<Spawn>() {
            @Override
            public boolean invoke(Spawn spawn) {
                if (roomController.getTasksManager().getMemory().$get(spawn.id) == null) {
                    roomController.getTasksManager().addTask(new TaskDeposit(roomController, spawn.id, spawn));
                }
                return true;
            }
        }, this);
    }

    @Override
    protected void init() {

    }

    public Array<Spawn> getSpawns() {
        return spawns;
    }

    public Spawn getAvailableSpawn() {
        TemporaryVariables.tempSpawn = null;
        Lodash.forIn(spawns, new LodashCallback1<Spawn>() {
            @Override
            public boolean invoke(Spawn spawn) {
                if (spawn.spawning == null) {
                    TemporaryVariables.tempSpawn = spawn;
                    return false;
                } else {
                    return true;
                }
            }
        }, this);

        return TemporaryVariables.tempSpawn;
    }

    @Override
    public void update() {

    }
}
