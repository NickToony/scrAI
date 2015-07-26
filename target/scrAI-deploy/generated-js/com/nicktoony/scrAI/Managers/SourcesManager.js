/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  Constants = require('Constants');/*
*/var  Lodash = require('lodash');/*
 */
var SourcesManager = function(roomController) {
    this.roomController = roomController;
    var callback = function(source) {
        return true;
    };
    this.sources = this.roomController.getRoom().find(FIND_SOURCES, {"filter": callback});
};
stjs.extend(SourcesManager, null, [], function(constructor, prototype) {
    prototype.roomController = null;
    prototype.sources = null;
    prototype.getSources = function() {
        return this.sources;
    };
    prototype.getFreeSource = function() {
        tempSource = null;
        Lodash.forIn(this.sources, function(source) {
            tempSource = source;
        }, this);
        return tempSource;
    };
}, {roomController: "RoomController", sources: {name: "Array", arguments: ["Source"]}});
(function() {
    module.exports = SourcesManager;
})();
