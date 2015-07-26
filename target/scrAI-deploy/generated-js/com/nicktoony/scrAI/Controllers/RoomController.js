/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  Constants = require("Constants");/*
*/var  Lodash = require('lodash');/*
 * 
*/var  PopulationManager = require("PopulationManager");/*
*/var  SourcesManager = require("SourcesManager");/*
*/var  SpawnsManager = require("SpawnsManager");/*
 * 
*/var  EconomyAdvisor = require("EconomyAdvisor");/*
*/var  MilitaryAdvisor = require("MilitaryAdvisor");/*
 */
var RoomController = function(room) {
    this.room = room;
    this.alertStatus = Constants.ALERT_STATUS_LOW;
    this.populationManager = new PopulationManager(this);
    this.sourcesManager = new SourcesManager(this);
    this.spawnsManager = new SpawnsManager(this);
    this.economyAdvisor = new EconomyAdvisor(this);
    this.militaryAdvisor = new MilitaryAdvisor(this);
};
stjs.extend(RoomController, null, [], function(constructor, prototype) {
    prototype.room = null;
    prototype.populationManager = null;
    prototype.sourcesManager = null;
    prototype.spawnsManager = null;
    prototype.economyAdvisor = null;
    prototype.militaryAdvisor = null;
    prototype.alertStatus = 0;
    prototype.step = function() {
        var spawn = this.spawnsManager.getAvailableSpawn();
        if (spawn != null) {
            var economyRequest = this.economyAdvisor.step();
            var militaryRequest = this.militaryAdvisor.step();
            var doMilitary = false;
            if (this.alertStatus == Constants.ALERT_STATUS_CRITICAL) {
                doMilitary = true;
            } else if (this.alertStatus == Constants.ALERT_STATUS_NONE) {
                doMilitary = false;
            }
            var request = null;
            if (doMilitary && militaryRequest != null) {
                console.log("MILITARY WANTS SOMETHING: " + militaryRequest);
                request = militaryRequest;
            } else if (economyRequest != null) {
                console.log("ECONOMY WANTS SOMETHING: " + economyRequest);
                request = economyRequest;
            } else if (militaryRequest != null) {
                console.log("ECONOMY DOESNT WANT, BUT MILITARY DOES: " + militaryRequest);
                request = militaryRequest;
            } else {
                console.log("NOONE WANTS ANYTHING?!");
            }
            if (request != null) {
                if (spawn.canCreateCreep(request.getAbilities(), request.getName()) == OK) {
                    console.log("BUILD: " + request.getName());
                    spawn.createCreep(request.getAbilities(), request.getName());
                }
            }
        }
        Lodash.forIn(this.getPopulationManager().getAllCreeps(), function(creepWrapper) {
            creepWrapper.step();
            return false;
        }, this);
    };
    prototype.getRoom = function() {
        return this.room;
    };
    prototype.getPopulationManager = function() {
        return this.populationManager;
    };
    prototype.getSourcesManager = function() {
        return this.sourcesManager;
    };
    prototype.getSpawnsManager = function() {
        return this.spawnsManager;
    };
    prototype.setAlertStatus = function(alertStatus) {
        this.alertStatus = alertStatus;
    };
}, {room: "Room", populationManager: "PopulationManager", sourcesManager: "SourcesManager", spawnsManager: "SpawnsManager", economyAdvisor: "EconomyAdvisor", militaryAdvisor: "MilitaryAdvisor"});
(function() {
    module.exports = RoomController;
})();
