/**
 *  Created by nick on 26/07/15.
 */
var TemporaryVariables = function() {};
stjs.extend(TemporaryVariables, null, [], function(constructor, prototype) {
    constructor.tempSource = null;
    constructor.tempSpawn = null;
}, {tempSource: "Source", tempSpawn: "Spawn"});
