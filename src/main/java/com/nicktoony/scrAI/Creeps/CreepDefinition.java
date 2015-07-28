package com.nicktoony.scrAI.Creeps;

import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Controllers.RoomController;
import org.stjs.javascript.Array;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 */
public class CreepDefinition {

    private String name;
    private Array<String> abilities;

    public CreepDefinition(String creepId, String creepType, Array<String> abilities, RoomController roomController) {
        this.abilities = abilities;
        this.name = generateName(creepId, creepType, roomController);
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
}
