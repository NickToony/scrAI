package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Spawn;

/**
 * Created by nick on 02/08/15.
 */
public class TaskDepositSpawn extends Task {
    protected Spawn spawn;

    public TaskDepositSpawn(RoomController roomController, String associatedId, Spawn spawn) {
        super(roomController, associatedId);
        this.spawn = spawn;
    }

    @Override
    public boolean canAct(CreepCollector creepCollector) {
        return spawn != null
                && (spawn.energy <= spawn.energyCapacity);
    }

    @Override
    public void act(CreepCollector creepCollector) {
        if (creepCollector.moveTo(spawn.pos)) {
            creepCollector.getCreep().transferEnergy(spawn);
        }
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
    }


    @Override
    public String getType() {
        return "2";
    }
}
