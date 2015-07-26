/**
 *  Created by nick on 26/07/15.
*/var  stjs = require("stjs");/*
 */
var Constants = function() {};
stjs.extend(Constants, null, [], function(constructor, prototype) {
    constructor.TIER_LOW = 1;
    constructor.TIER_MEDIUM = 2;
    constructor.TIER_HIGH = 3;
    constructor.ALERT_STATUS_CRITICAL = 1;
    constructor.ALERT_STATUS_HIGH = 2;
    constructor.ALERT_STATUS_MEDIUM = 3;
    constructor.ALERT_STATUS_LOW = 4;
    constructor.ALERT_STATUS_NONE = 5;
    constructor.CREEP_MINER = "1";
    constructor.SETTING_MINER_PER_SPAWN = 2;
}, {});
