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
        roomController.setAlertStatus(Constants.ALERT_STATUS_CRITICAL);
        roomController.step();
    });
}

module.exports = GlobalController;