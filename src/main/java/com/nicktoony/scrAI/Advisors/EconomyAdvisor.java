package com.nicktoony.scrAI.Advisors;

import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepWorker;
import com.nicktoony.scrAI.World.Creeps.CreepDefinition;
import com.nicktoony.scrAI.World.Creeps.CreepMiner;
import com.nicktoony.scrAI.World.SourceWrapper;

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

        roomController.getPathsManager().update();

        if (this.roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_MINER_ID).$length() >
                this.roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_WORKER_ID).$length()) {

            return CreepWorker.define(this.roomController);
        }

        // If there isn't enough creep miners
        if (this.roomController.getSourcesManager().getMaxMiners() >
                this.roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_MINER_ID).$length()) {

            SourceWrapper sourceWrapper = this.roomController.getSourcesManager().getFreeSource();
            int workParts = (int) Math.ceil((sourceWrapper.getOptimalWork() - sourceWrapper.getMiningRate()) / sourceWrapper.getAvailableSpots());

            // create a new miner
            return CreepMiner.define(this.roomController, workParts, sourceWrapper.getSource());
        }

        // Nothing we want..
        return null;
    }

}
