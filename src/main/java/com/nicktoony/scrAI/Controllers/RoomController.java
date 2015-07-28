package com.nicktoony.scrAI.Controllers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Advisors.EconomyAdvisor;
import com.nicktoony.scrAI.Advisors.MilitaryAdvisor;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Creeps.CreepDefinition;
import com.nicktoony.scrAI.Creeps.CreepWrapper;
import com.nicktoony.scrAI.Managers.PopulationManager;
import com.nicktoony.scrAI.Managers.SourcesManager;
import com.nicktoony.scrAI.Managers.SpawnsManager;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.Room;
import com.nicktoony.screeps.Spawn;
import org.stjs.javascript.Global;

/**
 * Created by nick on 26/07/15.
 *  var stjs = require("stjs");
 *  var Constants = require("Constants");
 *  var Lodash = require('lodash');
 *
 *  var PopulationManager = require("PopulationManager");
 *  var SourcesManager = require("SourcesManager");
 *  var SpawnsManager = require("SpawnsManager");
 *
 *  var EconomyAdvisor = require("EconomyAdvisor");
 *  var MilitaryAdvisor = require("MilitaryAdvisor");
 */
public class RoomController {

    private Room room;
    private PopulationManager populationManager;
    private SourcesManager sourcesManager;
    private SpawnsManager spawnsManager;
    private EconomyAdvisor economyAdvisor;
    private MilitaryAdvisor militaryAdvisor;
    private int alertStatus;

    public RoomController(Room room) {
        this.room = room;
        this.alertStatus = Constants.ALERT_STATUS_LOW;

        // Managers
        this.populationManager = new PopulationManager(this);
        this.sourcesManager = new SourcesManager(this);
        this.spawnsManager = new SpawnsManager(this);

        // Advisors
        this.economyAdvisor = new EconomyAdvisor(this);
        this.militaryAdvisor = new MilitaryAdvisor(this);
    }

    public void step() {
        Spawn spawn = this.spawnsManager.getAvailableSpawn();
        if (spawn != null) {
            // Ask "advisors" what they want
            CreepDefinition economyRequest = this.economyAdvisor.step();
            CreepDefinition militaryRequest = this.militaryAdvisor.step();

            // Perform depending on ratio
            boolean doMilitary = false;
            if (this.alertStatus == Constants.ALERT_STATUS_CRITICAL) {
                doMilitary = true;
            } else if (this.alertStatus == Constants.ALERT_STATUS_NONE) {
                doMilitary = false;
            }

            CreepDefinition request = null;
            if (doMilitary && militaryRequest != null) {
                // request the military thing
                Global.console.log("MILITARY WANTS SOMETHING: " + militaryRequest);
                request = militaryRequest;
            } else if (economyRequest != null) {
                // request the economy thing
                Global.console.log("ECONOMY WANTS SOMETHING: " + economyRequest);
                request = economyRequest;
            } else if (militaryRequest != null) {
                // military is not priority, but economy didn't want anything
                Global.console.log("ECONOMY DOESNT WANT, BUT MILITARY DOES: " + militaryRequest);
                request = militaryRequest;
            } else {
                // no one wants anything
                Global.console.log("NOONE WANTS ANYTHING?!");
            }

            if (request != null) {
                if (spawn.canCreateCreep(request.getAbilities(), request.getName()) == GlobalVariables.OK) {
                    Global.console.log("BUILD: " + request.getName());
                    spawn.createCreep(request.getAbilities(), request.getName());
                }
            }
        }

        Lodash.forIn(getPopulationManager().getAllCreeps(), new LodashCallback1<CreepWrapper>() {
            @Override
            public boolean invoke(CreepWrapper creepWrapper) {
                creepWrapper.step();
                return false;
            }
        }, this);
    }

    public Room getRoom() {
        return room;
    }

    public PopulationManager getPopulationManager() {
        return populationManager;
    }

    public SourcesManager getSourcesManager() {
        return sourcesManager;
    }

    public SpawnsManager getSpawnsManager() {
        return spawnsManager;
    }

    public void setAlertStatus(int alertStatus) {
        this.alertStatus = alertStatus;
    }
}
