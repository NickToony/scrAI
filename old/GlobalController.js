var RoomController = require('RoomController');
var Constants = require('Constants');

function GlobalController() {
    // Init rooms
    this.roomControllers = [];
    for(var i in Game.rooms) {
        this.roomControllers.push(new RoomController(Game.rooms[i]));
    };

    this.roomControllers.forEach( function (roomController)
    {
        // If we have no spawns in this room
        if (roomController.getSpawnsManager().getSpawnsCount()) {
            // Immediately jump to alert
            roomController.setAlertStatus(Constants.ALERT_STATUS_CRITICAL);
        } else {
            roomController.setAlertStatus(Constants.ALERT_STATUS_LOW);
        }
        roomController.step();
    });
}

module.exports = GlobalController;