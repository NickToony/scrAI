/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  Constants = require('Constants');/*
*/var  Lodash = require('lodash');/*
 */
var SpawnsManager = function(roomController) {
    this.roomController = roomController;
    this.spawns = this.roomController.getRoom().find(FIND_MY_SPAWNS, null);
};
stjs.extend(SpawnsManager, null, [], function(constructor, prototype) {
    prototype.roomController = null;
    prototype.spawns = null;
    prototype.getSpawns = function() {
        return this.spawns;
    };
    prototype.getAvailableSpawn = function() {
        var tempSpawn = null;
        Lodash.forIn(this.spawns, function(spawn) {
            if (spawn.spawning == null) {
                tempSpawn = spawn;
                return true;
            } else {
                return false;
            }
        }, this);
        return tempSpawn;
    };
}, {roomController: "RoomController", spawns: {name: "Array", arguments: ["Spawn"]}});
(function() {
    module.exports = SpawnsManager;
})();
