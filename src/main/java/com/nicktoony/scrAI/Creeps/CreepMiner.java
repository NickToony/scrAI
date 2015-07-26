package com.nicktoony.scrAI.Creeps;

import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Creep;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var CreepWrapper = require("CreepWrapper");
 */
public class CreepMiner extends CreepWrapper {
    public CreepMiner(RoomController roomController, Creep creep) {
        super(roomController, creep);
    }

    static { module.exports = CreepMiner.class; }
}
