function CreepMiner() {

}

CreepMiner.prototype.createAbilities = function(level) {
    var abilities;

    if(level <= 1) {
        abilities = [WORK, CARRY, MOVE];
    } else
    if(level <= 2) {
        abilities = [WORK, WORK, CARRY, MOVE];
    } else
    if(level <= 3) {
        abilities = [WORK, WORK, CARRY, MOVE, MOVE];
    } else
    if(level <= 4) {
        abilities = [WORK, WORK, WORK, CARRY, MOVE, MOVE];
    } else
    if(level <= 5) {
        abilities = [WORK, WORK, WORK, CARRY, MOVE, MOVE, MOVE];
    } else
    if(level <= 6) {
        abilities = [WORK, WORK, WORK, WORK, CARRY, MOVE, MOVE];
    } else
    if(level <= 7) {
        abilities = [WORK, WORK, WORK, WORK, CARRY, CARRY, MOVE, MOVE];
    } else
    if(level <= 8) {
        abilities = [WORK, WORK, WORK, WORK, WORK, CARRY, CARRY, MOVE, MOVE];
    } else
    if(level <= 9) {
        abilities = [WORK, WORK, WORK, WORK, WORK, CARRY, CARRY, MOVE, MOVE, MOVE];
    } else
    if(level >= 10) {
        abilities = [WORK, WORK, WORK, WORK, WORK, WORK, CARRY, CARRY, MOVE, MOVE, MOVE];
    }
    return abilities;
}

module.exports = CreepMiner;