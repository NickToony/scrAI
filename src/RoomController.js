var EconomyManager = require("EconomyManager");
var MilitaryManager = require("MilitaryManager");
var DepositsManager = require("DepositsManager");

RoomController.prototype.PRIORITY_MILITARY = 1;
RoomController.prototype.PRIORITY_ECONOMY = 2;

function RoomController(room) {
    this.room = room;
    this.priority = this.PRIORITY_ECONOMY;

    this.depositsManager = new DepositsManager(room);

    this.economyManager = new EconomyManager(this.depositsManager);
    this.militaryManager = new MilitaryManager(this.depositsManager);
}

RoomController.prototype.setPriority = function(priority) {
    this.priority = priority;
}

RoomController.prototype.step = function() {

    if (this.priority == this.PRIORITY_ECONOMY) {
        this.economyManager.step();
    }

    this.militaryManager.step();

    if (this.priority != this.PRIORITY_ECONOMY) {
        this.economyManager.step();
    }

}

module.exports = RoomController;