/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
 */
var CreepWrapper = function(roomController, creep) {
    this.roomController = roomController;
    this.creep = creep;
};
stjs.extend(CreepWrapper, null, [], function(constructor, prototype) {
    prototype.roomController = null;
    prototype.creep = null;
    prototype.getCreep = function() {
        return this.creep;
    };
    prototype.getRoomController = function() {
        return this.roomController;
    };
}, {roomController: "RoomController", creep: "Creep"});
(function() {
    module.exports = CreepWrapper;
})();
