var RoomController = require('RoomController');

function GlobalController() {
    // Init rooms
    this.roomControllers = [];
    for(var i in Game.rooms) {
        this.roomControllers.push(new RoomController(Game.rooms[i]));
    };

    this.roomControllers.forEach( function (roomController)
    {
        roomController.setAlertStatus(RoomController.ALERT_STATUS_CRITICAL);
        roomController.step();
    });
}

module.exports = GlobalController;