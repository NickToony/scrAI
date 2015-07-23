var Constants = require('Constants');

CreepMiner.CREEP_ID = Constants.CREEP_MINER;

function CreepMiner(roomController, creep) {
    this.roomController = roomController;
    this.creep = creep;
}

CreepMiner.prototype.step = function() {
    
};


CreepMiner.getCreep = function(tier) {

    var abilities;
    switch (tier) {

        case Constants.TIER_HIGH:
            abilities = [WORK, WORK, WORK, WORK, WORK, WORK, CARRY, CARRY, MOVE, MOVE, MOVE];

        case Constants.TIER_MEDIUM:
            abilities = [WORK, WORK, WORK, CARRY, MOVE, MOVE, MOVE];

        case Constants.TIER_LOW:
        default:
            abilities = [WORK, CARRY, MOVE];

    }

    return {
        "abilities": abilities,
        "id": this.CREEP_ID
    };
};

module.exports = CreepMiner;