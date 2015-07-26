/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  PopulationManager = require("PopulationManager");/*
 */
var RoomController = function(room) {
    this.room = room;
    this.populationManager = new PopulationManager(this);
    this.sourcesManager = new SourcesManager(this);
};
stjs.extend(RoomController, null, [], function(constructor, prototype) {
    prototype.room = null;
    prototype.populationManager = null;
    prototype.sourcesManager = null;
    prototype.step = function() {};
    prototype.getRoom = function() {
        return this.room;
    };
}, {room: "Room", populationManager: "PopulationManager", sourcesManager: "SourcesManager"});
(function() {
    module.exports = RoomController;
})();
