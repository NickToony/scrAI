function PopulationManager(roomController) {
    this.roomController = roomController;

    this.allCreeps = this.roomController.getRoom().find(FIND_MY_CREEPS);
    this.sortedCreeps = {};
    for (var creep in this.allCreeps) {
        this.getSortedCreeps(creep.name.charAt(0)).push(creep);
    }
}

PopulationManager.prototype.getSortedCreeps = function(id) {
    if (this.sortedCreeps[id] == null) {
        this.sortedCreeps[id] = [];
    }
    return this.sortedCreeps[id];
}

module.exports = PopulationManager;