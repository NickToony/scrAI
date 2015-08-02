package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.Game;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public class TaskPickupEnergy extends Task {
    protected Energy energy;

    public TaskPickupEnergy(RoomController roomController, String associatedId, Energy energy) {
        super(roomController, associatedId);
        this.energy = energy;
    }

    @Override
    public boolean canAct(CreepCollector creepCollector) {
        return energy != null
                && (creepCollector.getCreep().carry.energy <= (creepCollector.getCarryCapacity()/2));
    }

    @Override
    public void act(CreepCollector creepCollector) {
        if (creepCollector.moveTo(energy.pos)) {
            creepCollector.getCreep().pickup(energy);
        }
    }

    @Override
    public boolean save() {
        if (energy == null) {
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
        energy = (Energy) Game.getObjectById(associatedId);
    }


    @Override
    public String getType() {
        return "1";
    }
}
