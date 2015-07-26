/**
 *  Created by nick on 26/07/15.
 * 
*/var  stjs = require("stjs");/*
*/var  StringProvider =  require("StringProvider");/*
 */
var HelloWorld = function(hello, world) {
    this.hello = hello;
    this.world = world;
    this.helloWorld = hello + world;
    this.getting = new StringProvider().getString();
    var int1 = 10;
    var int2 = 10;
    if (int1 == int2) {
        console.log(hello);
    }
};
stjs.extend(HelloWorld, null, [], function(constructor, prototype) {
    prototype.hello = null;
    prototype.world = 0;
    prototype.helloWorld = null;
    prototype.getting = null;
    prototype.getHello = function() {
        return this.hello;
    };
    prototype.setHello = function(hello) {
        this.hello = hello;
    };
    prototype.getWorld = function() {
        return this.world;
    };
    prototype.setWorld = function(world) {
        this.world = world;
    };
}, {});
(function() {
    module.exports = HelloWorld;
})();
