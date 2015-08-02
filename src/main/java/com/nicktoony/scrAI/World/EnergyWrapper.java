package com.nicktoony.scrAI.World;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.scrAI.World.Creeps.CreepMiner;
import com.nicktoony.scrAI.World.Creeps.CreepWrapper;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.RoomPosition;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 29/07/15.
 */
public class EnergyWrapper extends MemoryWrapper {
    private RoomController roomController;
    private Energy energy;
    private int claimed = 0;

    public EnergyWrapper(RoomController roomController, Energy energy, Map<String, Object> energyMemory) {
        super(roomController, energyMemory);
        this.energy = energy;
        super.prepare();
    }

    @Override
    public void init() {
        ;
    }

    @Override
    public void create() {
        Lodash.forIn(roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_COLLECTOR_ID), new LodashCallback1<CreepCollector>() {
            @Override
            public boolean invoke(CreepCollector creepCollector) {
                if (creepCollector.getTarget() == energy.id) {
                    claimed += creepCollector.getCarryCapacity();
                }
                return true;
            }
        }, this);
    }

    @Override
    public void step() {

    }

    public int availableToClaim() {
        return energy.energy - claimed;
    }


    public Energy getEnergy() {
        return energy;
    }

    public void claim(CreepCollector creepWrapper) {
        claimed += creepWrapper.getCarryCapacity();
    }
}
