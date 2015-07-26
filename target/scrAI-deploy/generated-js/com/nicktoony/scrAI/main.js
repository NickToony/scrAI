/**
 *  Created by nick on 26/07/15.
 * 
*/var  stjs = require("stjs");/*
*/var  GlobalController = require("GlobalController");/*
 */
var main = function() {};
stjs.extend(main, null, [], function(constructor, prototype) {
    constructor.main = function(args) {
        new GlobalController();
    };
}, {});
if (!stjs.mainCallDisabled) 
    main.main();
