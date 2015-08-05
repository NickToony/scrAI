package com.nicktoony.scrAI.Advisors;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.Managers.ManagerTimer;
import com.nicktoony.scrAI.World.Creeps.CreepWorker;
import com.nicktoony.scrAI.World.Creeps.CreepDefinition;
import com.nicktoony.scrAI.World.Creeps.CreepMiner;
import com.nicktoony.scrAI.World.SourceWrapper;
import org.stjs.javascript.Global;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Advisor = require("Advisor");
 * var CreepMiner = require("CreepMiner");
 */
public class EconomyAdvisor extends ManagerTimer {

    private int currentWorkers = 0;

    public EconomyAdvisor(RoomController roomController) {
        super(roomController, "EconomyAdvisor", Constants.DELAY_ECONOMY_DECISION);
    }

    public CreepDefinition step() {

        if (!super.canRun()) {
            return null;
        }
        super.hasRun();

        // Calculate how many workers we need
        int totalWorkers = Math.max((int) Math.ceil(this.roomController.getTasksManager().getTaskCount() / 2f),
                this.roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_MINER_ID).$length());
        // Calculate how many we have
        Lodash.forIn(this.roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_WORKER_ID), new LodashCallback1<CreepWorker>() {
            @Override
            public boolean invoke(CreepWorker variable) {

                currentWorkers += variable.getCarryCapacity()/50;

                return true;
            }
        }, this);

        // Create more workers if needed
        if (totalWorkers > currentWorkers) {
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
