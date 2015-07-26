

function MilitaryManager(roomController) {
    this.roomController = roomController;
};

MilitaryManager.prototype.step = function() {
    return false;
};

module.exports = MilitaryManager;