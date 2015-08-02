package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.scrAI.World.Creeps.CreepWrapper;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.Game;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public class TaskPickupEnergy extends Task {
    protected Energy energy;
    private int energyAvailable = 0;

    public TaskPickupEnergy(RoomController roomController, String associatedId, Energy energy) {
        super(roomController, associatedId);
        this.energy = energy;
    }

    @Override
    public boolean canAct(CreepCollector creepCollector) {
        return energy != null
                && (creepCollector.getCreep().carry.energy <= (creepCollector.getCarryCapacity()/2))
                && energyAvailable > 0;
    }

    @Override
    public boolean act(CreepCollector creepCollector) {
        if (creepCollector.moveTo(energy.pos)) {
            creepCollector.getCreep().pickup(energy);
            return false;
        }

        return true;
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
        if (energy != null) {
            energyAvailable = energy.energy;
        }
    }

    @Override
    public void assignCreep(CreepWrapper creepWrapper) {
        super.assignCreep(creepWrapper);

        energyAvailable -= (creepWrapper.getCreep().carryCapacity - creepWrapper.getCreep().carry.energy);
    }

    @Override
    public String getType() {
        return "1";
    }

    @Override
    public int getPriority() {
        return energyAvailable;
    }
}
