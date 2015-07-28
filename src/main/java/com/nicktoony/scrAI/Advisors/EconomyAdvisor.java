package com.nicktoony.scrAI.Advisors;

import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.Creeps.CreepDefinition;
import com.nicktoony.scrAI.Creeps.CreepMiner;
import org.stjs.javascript.Global;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Advisor = require("Advisor");
 * var CreepMiner = require("CreepMiner");
 */
public class EconomyAdvisor extends Advisor {
    private int tier;

    public EconomyAdvisor(RoomController roomController) {
        super(roomController);
        this.tier = Math.max(this.roomController.getSpawnsManager().getSpawns().$length(),1);
    }

    @Override
    public CreepDefinition step() {
        // If there isn't enough creep miners
        if (this.roomController.getSourcesManager().getSafeSources().$length() * Constants.SETTING_MINER_PER_SPAWN >
                this.roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_MINER_ID).$length()) {
            // create a new miner
            return CreepMiner.define(this.roomController, tier);
        }

        // Nothing we want..
        return null;
    }

}
