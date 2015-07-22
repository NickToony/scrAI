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
}

SourcesManager.prototype.getSafeSourcesCount = function() {
    return this.safeSources.length;
}

module.exports = SourcesManager;