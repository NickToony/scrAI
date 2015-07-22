function SpawnsManager(roomController) {
    this.roomController = roomController;
    this.spawns = this.roomController.getRoom().find(
        FIND_MY_SPAWNS, {
            filter: function(spawn) {
                return true;
            }
        });
}

SpawnsManager.prototype.getSpawnsCount = function() {
    return this.spawns.length;
};
SpawnsManager.prototype.getAvailableSpawn = function() {
    var toReturn = false;
    this.spawns.forEach( function (spawn) {
        if (spawn.spawning === null) {
            toReturn = spawn;
        }
    });
    return toReturn;
};

module.exports = SpawnsManager;