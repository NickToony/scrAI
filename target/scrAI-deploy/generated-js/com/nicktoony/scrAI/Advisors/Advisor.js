/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
 */
var Advisor = function(roomController) {
    this.roomController = roomController;
};
stjs.extend(Advisor, null, [], function(constructor, prototype) {
    prototype.roomController = null;
}, {roomController: "RoomController"});
(function() {
    module.exports = Advisor;
})();
