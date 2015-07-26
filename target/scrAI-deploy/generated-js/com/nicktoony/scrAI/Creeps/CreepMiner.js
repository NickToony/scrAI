/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  CreepWrapper = require("CreepWrapper");/*
 */
var CreepMiner = function(roomController, creep) {
    CreepWrapper.call(this, roomController, creep);
};
stjs.extend(CreepMiner, CreepWrapper, [], null, {roomController: "RoomController", creep: "Creep"});
(function() {
    module.exports = CreepMiner;
})();
