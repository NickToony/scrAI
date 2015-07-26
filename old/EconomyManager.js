var CreepMiner = require('CreepMiner');
var Constants = require('Constants');

function EconomyManager(roomController) {
    this.roomController = roomController;
    // The economies tier is based on spawn count
    this.tier = Math.max(this.roomController.getSpawnsManager().getSpawnsCount(), 1);
}

EconomyManager.prototype.step = function() {
    if (this.roomController.getSourcesManager().getSafeSourcesCount() * Constants.CREEP_MINER_PER_SPAWN >
        this.roomController.getPopulationManager().getSortedCreeps(CreepMiner.CREEP_ID).length) {
        return CreepMiner.getCreep(this.tier);
    }

    return false;
};

module.exports = EconomyManager;