var Constants = require('Constants');

CreepMiner.CREEP_ID = 1;

function CreepMiner() {

}

CreepMiner.getAbilities = function(tier) {
    switch (tier) {

        case Constants.TIER_HIGH:
            return [WORK, WORK, WORK, WORK, WORK, WORK, CARRY, CARRY, MOVE, MOVE, MOVE];

        case Constants.TIER_MEDIUM:
            return [WORK, WORK, WORK, CARRY, MOVE, MOVE, MOVE];

        case Constants.TIER_LOW:
        default:
            return [WORK, CARRY, MOVE];

    }
};

module.exports = CreepMiner;