package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.scrAI.World.Creeps.CreepWrapper;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Spawn;
import org.stjs.javascript.Global;

/**
 * Created by nick on 02/08/15.
 */
public class TaskDepositSpawn extends Task {
    protected Spawn spawn;
    private int storageAvailable = 0;

    public TaskDepositSpawn(RoomController roomController, String associatedId, Spawn spawn) {
        super(roomController, associatedId);
        this.spawn = spawn;
    }

    @Override
    public boolean canAct(CreepCollector creepCollector) {
        return spawn != null
                && (storageAvailable > 0)
                && (creepCollector.getCreep().carry.energy > 0);
    }

    @Override
    public boolean act(CreepCollector creepCollector) {
        if (creepCollector.moveTo(spawn.pos)) {
            creepCollector.getCreep().transferEnergy(spawn);
            return false;
        }
        return true;
    }

    @Override
    public boolean save() {
        if (spawn == null) {
            return false;
        }
        return super.save();
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void create() {
        spawn = (Spawn) Game.getObjectById(associatedId);
        if (spawn != null) {
            storageAvailable = spawn.energyCapacity - spawn.energy;
        }
    }

    @Override
    public void assignCreep(CreepWrapper creepWrapper) {
        super.assignCreep(creepWrapper);

        storageAvailable -= creepWrapper.getCreep().carry.energy;
    }

    @Override
    public String getType() {
        return "2";
    }

    @Override
    public int getPriority() {
        return 1;
    }
}
