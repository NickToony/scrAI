var RoomController = require('RoomController');

function GlobalController() {
    // Init rooms
    this.roomControllers = [];
    for(var room in Game.rooms) {
        this.roomControllers.push(new RoomController(room));
    };

    this.roomControllers.forEach( function (roomController)
    {
        roomController.setPriority(RoomController.PRIORITY_ECONOMY)
        roomController.step();
    });
}

module.exports = GlobalController;