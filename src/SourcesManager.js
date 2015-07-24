var Constants = require('Constants');

function SourcesManager(roomController) {
    this.roomController = roomController;
    this.sources = this.roomController.getRoom().find(
        FIND_SOURCES, {
            filter: function(src) {
                return true;
            }
        });
    this.safeSources = this.roomController.getRoom().find(
        FIND_SOURCES, {
            filter: function(src) {
                var targets = src.pos.findInRange(FIND_HOSTILE_CREEPS, 3);
                if(targets.length == 0) {
                    return true;
                }

                return false;
            }
        });
}

SourcesManager.prototype.getSourcesCount = function() {
    return this.sources.length;
};

SourcesManager.prototype.getSafeSourcesCount = function() {
    return this.safeSources.length;
};

SourcesManager.prototype.getFreeSource = function() {
    for (var i in this.safeSources) {
        var source = this.safeSources[i];
        if (source.memory['creep_count'] < Constants.CREEP_MINER_PER_SPAWN) {
            return source;
        }
    }

    return false;
};

SourcesManager.prototype.addCreepToSpawn = function(creep, spawn) {
    spawn.memory['creep_count'] ++;
    creep.memory['source_target'] = spawn;
};
SourcesManager.prototype.removeCreepFromSpawn = function(creep) {
    if (creep.memory["source_target"] != null) {
        creep.memory["source_target"].memory['creep_count'] ++;
    }
};


module.exports = SourcesManager;