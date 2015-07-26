/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  Constants = require('Constants');/*
*/var  Lodash = require('lodash');/*
 */
var SourcesManager = function(roomController) {
    this.roomController = roomController;
    this.sources = this.roomController.getRoom().find(FIND_SOURCES, null);
    var callback = function(source) {
        var targets = source.pos.findInRange(FIND_HOSTILE_CREEPS, 3);
        if (targets.length == 0) {
            return true;
        }
        return false;
    };
    this.safeSources = this.roomController.getRoom().find(FIND_SOURCES, {"filter": callback});
};
stjs.extend(SourcesManager, null, [], function(constructor, prototype) {
    prototype.roomController = null;
    prototype.sources = null;
    prototype.safeSources = null;
    prototype.getSources = function() {
        return this.sources;
    };
    prototype.getSafeSources = function() {
        return this.safeSources;
    };
    prototype.getFreeSource = function() {
        var tempSource = null;
        Lodash.forIn(this.sources, function(source) {
            tempSource = source;
            return true;
        }, this);
        return tempSource;
    };
}, {roomController: "RoomController", sources: {name: "Array", arguments: ["Source"]}, safeSources: {name: "Array", arguments: ["Source"]}});
(function() {
    module.exports = SourcesManager;
})();
