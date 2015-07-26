/**
 *  Created by nick on 26/07/15.
 * 
*/var  stjs = require("stjs");/*
*/var  HelloWorld = require("HelloWorld");/*
 */
var main = function() {};
stjs.extend(main, null, [], function(constructor, prototype) {
    constructor.main = function(args) {
        new HelloWorld("Yes", 2);
    };
}, {});
if (!stjs.mainCallDisabled) 
    main.main();
