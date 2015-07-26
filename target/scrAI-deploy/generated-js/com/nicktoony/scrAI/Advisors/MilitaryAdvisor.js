/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
*/var  Constants = require('Constants');/*
*/var  Advisor = require("Advisor");/*
 */
var MilitaryAdvisor = function(roomController) {
    Advisor.call(this, roomController);
};
stjs.extend(MilitaryAdvisor, Advisor, [], function(constructor, prototype) {
    prototype.tier = 0;
    prototype.step = function() {
        return null;
    };
}, {roomController: "RoomController"});
(function() {
    module.exports = MilitaryAdvisor;
})();
