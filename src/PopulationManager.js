var Constants = require('Constants');
var CreepMiner = require('CreepMiner');

function PopulationManager(roomController) {
    this.roomController = roomController;

    this.allCreeps = this.roomController.getRoom().find(FIND_MY_CREEPS);
    this.sortedCreeps = {};
    for (var i in this.allCreeps) {
        var creep = this.allCreeps[i];
        this.getSortedCreeps(creep.name.charAt(0)).push(creep);
    }
}

PopulationManager.prototype.getSortedCreeps = function(id) {
    if (this.sortedCreeps[id] == null) {
        this.sortedCreeps[id] = [];
    }
    return this.sortedCreeps[id];
};

PopulationManager.prototype.actAllCreeps = function() {
    for (var i in this.allCreeps) {
        var creep = this.allCreeps[i];
        var creepClass = this.getCreepClass(creep);
        if (creepClass != false) {
            creepClass.step();
        }
    }
};

PopulationManager.prototype.getCreepClass = function(creep) {
    switch (creep.name.charAt(0)) {
        case Constants.CREEP_MINER:
            return new CreepMiner(this.roomController, creep);
    }

    return false;
};

module.exports = PopulationManager;