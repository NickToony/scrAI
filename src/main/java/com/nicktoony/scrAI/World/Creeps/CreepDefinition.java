package com.nicktoony.scrAI.World.Creeps;

import com.nicktoony.scrAI.Controllers.RoomController;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 */
public class CreepDefinition {

    private Map<String, Object> memory;
    private String name;
    private Array<String> abilities;

    public CreepDefinition(String creepId, String creepType, Array<String> abilities, RoomController roomController, Map<String, Object> memory) {
        this.abilities = abilities;
        this.name = generateName(creepId, creepType, roomController);
        this.memory = memory;
    }

    private String generateName(String creepId, String creepType, RoomController roomController) {
        long random = Math.round(Math.random()*10000);
        return name = creepId + "_" + creepType + "_" + roomController.getRoom().name + "_" + random;
    }

    public String getName() {
        return name;
    }

    public Array<String> getAbilities() {
        return abilities;
    }

    public Map<String, Object> getMemory() {
        return memory;
    }
}
