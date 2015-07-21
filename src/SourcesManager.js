function SourcesManager(roomController) {
    this.roomController = roomController;
    this.deposits = this.roomController.getRoom().find(
        FIND_SOURCES, {
            filter: function(src) {
                return true;
            }
        });
}

SourcesManager.prototype.getSourcesCount = function() {
    return this.deposits.length;
}

module.exports = SourcesManager;