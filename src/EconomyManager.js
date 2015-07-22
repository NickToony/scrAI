var CreepMiner = require('CreepMiner');
var Constants = require('Constants');

function EconomyManager(roomController) {
    this.roomController = roomController;
    this.tier = Constants.TIER_LOW;
}

EconomyManager.prototype.step = function() {
    if (this.roomController.getSourcesManager().getSafeSourcesCount() > this.roomController.getPopulationManager().getSortedCreeps(CreepMiner.CREEP_ID).length) {
        return CreepMiner.getAbilities(this.tier);
    }

    return false;
};

module.exports = EconomyManager;