var EconomyManager = require("EconomyManager");
var MilitaryManager = require("MilitaryManager");
var SourcesManager = require("SourcesManager");
var PopulationManager = require("PopulationManager");
var SpawnsManager = require("SpawnsManager");
var Constants = require("Constants");

function RoomController(room) {
    this.room = room;
    this.alertStatus = Constants.ALERT_STATUS_NONE;

    this.sourcesManager = new SourcesManager(this);
    this.populationManager = new PopulationManager(this);
    this.spawnsManager = new SpawnsManager(this);

    this.economyManager = new EconomyManager(this);
    this.militaryManager = new MilitaryManager(this);
}

RoomController.prototype.setAlertStatus = function(alertStatus) {
    this.alertStatus = alertStatus;
};

RoomController.prototype.step = function() {

    var spawn = this.spawnsManager.getAvailableSpawn();
    if (spawn != false) {
        // Ask "advisors" what they want
        var economyRequest = this.economyManager.step();
        var militaryRequest = this.militaryManager.step();

        // Perform depending on ratio
        var doMilitary = false;
        if (this.alertStatus == Constants.ALERT_STATUS_CRITICAL) {
            doMilitary = true;
        } else if (this.alertStatus == Constants.ALERT_STATUS_NONE) {
            doMilitary = false;
        }

        var request = false;
        if (doMilitary && militaryRequest != false) {
            // request the military thing
            console.log("MILITARY WANTS SOMETHING: " + militaryRequest);
            request = militaryRequest;
        } else if (economyRequest != false) {
            // request the economy thing
            console.log("ECONOMY WANTS SOMETHING: " + economyRequest);
            request = economyRequest;
        } else if (militaryRequest != false) {
            // military is not priority, but economy didn't want anything
            console.log("ECONOMY DOESNT WANT, BUT MILITARY DOES: " + militaryRequest);
            request = militaryRequest;
        } else {
            // no one wants anything
            console.log("NOONE WANTS ANYTHING?!");
        }

        if (request != false) {
            var name = Constants.getCreepName(request["id"], this.room.name);
            if (spawn.canCreateCreep(request['abilities'], name) == OK) {
                console.log("BUILD: " + name);
                spawn.createCreep(request['abilities'], name);
            }
        }
    }

    this.populationManager.actAllCreeps();

};

RoomController.prototype.getRoom = function() {
    return this.room;
};
RoomController.prototype.getSourcesManager = function() {
    return this.sourcesManager;
};
RoomController.prototype.getPopulationManager = function() {
    return this.populationManager;
};
RoomController.prototype.getSpawnsManager = function() {
    return this.spawnsManager;
}

module.exports = RoomController;