/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  CreepWrapper = require("CreepWrapper");/*
*/var  Constants = require("Constants");/*
*/var  CreepDefinition = require("CreepDefinition");/*
 */
var CreepMiner = function(roomController, creep) {
    CreepWrapper.call(this, roomController, creep);
};
stjs.extend(CreepMiner, CreepWrapper, [], function(constructor, prototype) {
    prototype.step = function() {};
    constructor.define = function(roomController, tier) {
        var abilities;
        if (tier == Constants.TIER_HIGH) {
            abilities = [WORK, CARRY, MOVE];
        } else if (tier == Constants.TIER_MEDIUM) {
            abilities = [WORK, CARRY, MOVE];
        } else {
            abilities = [WORK, CARRY, MOVE];
        }
        return new CreepDefinition(Constants.CREEP_MINER_ID, Constants.CREEP_MINER_NAME, abilities, roomController);
    };
}, {roomController: "RoomController", creep: "Creep"});
(function() {
    module.exports = CreepMiner;
})();
