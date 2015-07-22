Constants.TIER_LOW = 1;
Constants.TIER_MEDIUM = 2;
Constants.TIER_HIGH = 3;

Constants.ALERT_STATUS_CRITICAL = 1; // 100% military, 0% economy
Constants.ALERT_STATUS_HIGH = 2; //
Constants.ALERT_STATUS_MEDIUM = 3; // 50% military, 50% ecconomy
Constants.ALERT_STATUS_LOW = 4; //
Constants.ALERT_STATUS_NONE = 5; // 0% military, 100% economy

// Creep ids
Constants.CREEP_MINER = 1;

// Creep settings (miner)
Constants.CREEP_MINER_PER_SPAWN = 2;

function Constants() {

}

Constants.getCreepName = function(id, roomId) {
    var type = "Creep";
    switch (id) {
        case this.CREEP_MINER:
            type = "CreepMiner";
            break;
    }

    var random = Math.round(Math.random()*10000);
    return id + "_" + type + "_" + roomId + "_" + random;
};

module.exports = Constants;