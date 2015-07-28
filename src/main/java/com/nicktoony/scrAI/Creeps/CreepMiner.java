package com.nicktoony.scrAI.Creeps;

import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Creep;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;
import static com.nicktoony.screeps.GlobalVariables.*;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var CreepWrapper = require("CreepWrapper");
 * var Constants = require("Constants");
 * var CreepDefinition = require("CreepDefinition");
 */
public class CreepMiner extends CreepWrapper {
    public CreepMiner(RoomController roomController, Creep creep) {
        super(roomController, creep);
    }

    @Override
    public void step() {

    }

    public static CreepDefinition define(RoomController roomController, int tier) {
        Array<String> abilities;

        if (tier == Constants.TIER_HIGH) {
            abilities = JSCollections.$array(WORK, CARRY, MOVE);
        } else if (tier == Constants.TIER_MEDIUM) {
            abilities = JSCollections.$array(WORK, CARRY, MOVE);
        } else {
            abilities = JSCollections.$array(WORK, CARRY, MOVE);
        }

        return new CreepDefinition(Constants.CREEP_MINER_ID, Constants.CREEP_MINER_NAME,
                abilities, roomController);
    }
}
