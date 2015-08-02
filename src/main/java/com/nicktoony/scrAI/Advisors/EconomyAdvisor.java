package com.nicktoony.scrAI.Advisors;

import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.Managers.ManagerTimer;
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
public class EconomyAdvisor extends ManagerTimer {

    public EconomyAdvisor(RoomController roomController) {
        super(roomController, "EconomyAdvisor", Constants.DELAY_ECONOMY_DECISION);
    }

    public CreepDefinition step() {

        if (!super.canRun()) {
            return null;
        }
        super.hasRun();

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

        roomController.getPathsManager().update();

        // Nothing we want..
        return null;
    }

}
