/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  Constants = require("Constants");/*
*/var  RoomController = require("RoomController");/*
*/var  Lodash = require('lodash');/*
 */
var GlobalController = function() {
    this.roomControllers = new Array();
    Lodash.forIn(Game.rooms, function(room, name) {
        this.roomControllers.push(new RoomController(room));
        return false;
    }, this);
    this.roomControllers.forEach(function(roomController) {
        if (roomController.getSpawnsManager().getSpawns().length == 0) {
            roomController.setAlertStatus(Constants.ALERT_STATUS_CRITICAL);
            console.log("CRITICAL STATUS: " + roomController.getRoom().name);
        } else {
            roomController.setAlertStatus(Constants.ALERT_STATUS_LOW);
        }
        roomController.step();
    });
};
stjs.extend(GlobalController, null, [], function(constructor, prototype) {
    prototype.roomControllers = null;
}, {roomControllers: {name: "Array", arguments: ["RoomController"]}});
(function() {
    module.exports = GlobalController;
})();
