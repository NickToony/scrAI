/**
 *  Created by nick on 26/07/15.
 */
var Spawn = function() {};
stjs.extend(Spawn, null, [], function(constructor, prototype) {
    prototype.canCreateCreep = function(abilities, name) {
        return 0;
    };
    prototype.createCreep = function(abilities, name) {
        return 0;
    };
    prototype.spawning = null;
}, {spawning: "Creep"});
