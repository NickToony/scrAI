/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
 */
var CreepDefinition = function(creepId, creepType, abilities, roomController) {
    this.abilities = abilities;
    this.name = this.generateName(creepId, creepType, roomController);
};
stjs.extend(CreepDefinition, null, [], function(constructor, prototype) {
    prototype.name = null;
    prototype.abilities = null;
    prototype.generateName = function(creepId, creepType, roomController) {
        var random = Math.round(Math.random() * 10000);
        return this.name = creepId + "_" + creepType + "_" + roomController.getRoom().name + "_" + random;
    };
    prototype.getName = function() {
        return this.name;
    };
    prototype.getAbilities = function() {
        return this.abilities;
    };
}, {abilities: {name: "Array", arguments: [null]}});
(function() {
    module.exports = CreepDefinition;
})();
