/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  Constants = require('Constants');/*
*/var  Advisor = require("Advisor");/*
*/var  CreepMiner = require("CreepMiner");/*
 */
var EconomyAdvisor = function(roomController) {
    Advisor.call(this, roomController);
    this.tier = Math.max(this.roomController.getSpawnsManager().getSpawns().length, 1);
};
stjs.extend(EconomyAdvisor, Advisor, [], function(constructor, prototype) {
    prototype.tier = 0;
    prototype.step = function() {
        if (this.roomController.getSourcesManager().getSafeSources().length * Constants.SETTING_MINER_PER_SPAWN > this.roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_MINER_ID).length) {
            return CreepMiner.define(this.roomController, this.tier);
        }
        return null;
    };
}, {roomController: "RoomController"});
(function() {
    module.exports = EconomyAdvisor;
})();
