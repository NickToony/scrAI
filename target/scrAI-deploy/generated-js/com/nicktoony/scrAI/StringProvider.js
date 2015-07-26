/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
 */
var StringProvider = function() {};
stjs.extend(StringProvider, null, [], function(constructor, prototype) {
    prototype.string = "HELLO";
    prototype.getString = function() {
        return this.string;
    };
}, {});
(function() {
    module.exports = StringProvider;
})();
