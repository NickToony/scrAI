/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  Constants = require('Constants');/*
*/var  Lodash = require('lodash');/*
*/var  CreepMiner = require('CreepMiner');/*
 */
var PopulationManager = function(roomController) {
    this.roomController = roomController;
    this.allCreeps = new Array();
    var foundCreeps = this.roomController.getRoom().find(FIND_MY_CREEPS, {});
    this.sortedCreeps = {};
    Lodash.forIn(foundCreeps, function(creep) {
        var type = creep.name.substring(0, 1);
        var creepWrapper = this.getCreepWrapper(type, creep);
        if (creepWrapper != null) {
            this.getSortedCreeps(type).push(creepWrapper);
            this.allCreeps.push(creepWrapper);
        }
        return false;
    }, this);
};
stjs.extend(PopulationManager, null, [], function(constructor, prototype) {
    prototype.allCreeps = null;
    prototype.roomController = null;
    prototype.sortedCreeps = null;
    prototype.getSortedCreeps = function(id) {
        if (this.sortedCreeps[id] == null) {
            this.sortedCreeps[id] = new Array();
        }
        return this.sortedCreeps[id];
    };
    prototype.getCreepWrapper = function(id, creep) {
        if (id == Constants.CREEP_MINER_ID) {
            return new CreepMiner(this.roomController, creep);
        }
        return null;
    };
    prototype.getAllCreeps = function() {
        return this.allCreeps;
    };
}, {allCreeps: {name: "Array", arguments: ["CreepWrapper"]}, roomController: "RoomController", sortedCreeps: {name: "Map", arguments: [null, {name: "Array", arguments: ["CreepWrapper"]}]}});
(function() {
    module.exports = PopulationManager;
})();
