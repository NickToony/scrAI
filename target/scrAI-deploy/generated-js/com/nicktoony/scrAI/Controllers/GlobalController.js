/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  RoomController = require("RoomController");/*
*/var  Lodash = require('lodash');/*
 */
var GlobalController = function() {
    this.roomControllers = new Array();
    Lodash.forIn(Game.rooms, function(room, name) {
        this.roomControllers.push(new RoomController(room));
    }, this);
    this.roomControllers.forEach(function(roomController) {
        roomController.step();
    });
};
stjs.extend(GlobalController, null, [], function(constructor, prototype) {
    prototype.roomControllers = null;
}, {roomControllers: {name: "Array", arguments: ["RoomController"]}});
(function() {
    module.exports = GlobalController;
})();
