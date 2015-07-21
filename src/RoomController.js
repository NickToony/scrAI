var EconomyManager = require("EconomyManager");
var MilitaryManager = require("MilitaryManager");
var SourcesManager = require("SourcesManager");
var PopulationManager = require("PopulationManager");

RoomController.ALERT_STATUS_CRITICAL = 1; // 100% military, 0% economy
RoomController.ALERT_STATUS_HIGH = 2; //
RoomController.ALERT_STATUS_MEDIUM = 3; // 50% military, 50% ecconomy
RoomController.ALERT_STATUS_LOW = 4; //
RoomController.ALERT_STATUS_NONE = 5; // 0% military, 100% economy

function RoomController(room) {
    this.room = room;
    this.alertStatus = this.ALERT_STATUS_NONE;

    this.sourcesManager = new SourcesManager(this);
    this.populationManager = new PopulationManager(this);

    this.economyManager = new EconomyManager(this);
    this.militaryManager = new MilitaryManager(this);
}

RoomController.prototype.setAlertStatus = function(alertStatus) {
    this.alertStatus = alertStatus;
};

RoomController.prototype.step = function() {
    // Ask "advisors" what they want
    var economyRequest = this.economyManager.step();
    var militaryRequest = this.militaryManager.step();

    // Perform depending on ratio
    var doMilitary = false;
    if (this.alertStatus == this.ALERT_STATUS_CRITICAL) {
        doMilitary = true;
    } else if (this.alertStatus == this.ALERT_STATUS_NONE) {
        doMilitary = false;
    }

    if (doMilitary && militaryRequest != false) {
        // request the military thing
        console.log("MILITARY WANTS SOMETHING: " + militaryRequest);
    } else if (economyRequest != false) {
        // request the economy thing
        console.log("ECONOMY WANTS SOMETHING: " + economyRequest);
    } else if (militaryRequest != false) {
        // military is not priority, but economy didn't want anything
        console.log("ECONOMY DOESNT WANT, BUT MILITARY DOES: " + militaryRequest);
    } else {
        // no one wants anything
        console.log("NOONE WANTS ANYTHING?!");
    }
};

RoomController.prototype.getRoom = function() {
    return this.room;
}
RoomController.prototype.getSourcesManager = function() {
    return this.sourcesManager;
}
RoomController.prototype.getPopulationManager = function() {
    return this.populationManager;
}

module.exports = RoomController;