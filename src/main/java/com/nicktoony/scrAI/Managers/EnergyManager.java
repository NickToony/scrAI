package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.TemporaryVariables;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.scrAI.World.EnergyWrapper;
import com.nicktoony.scrAI.World.Tasks.TaskPickupEnergy;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Lodash = require('lodash');
 */
public class EnergyManager {
    private RoomController roomController;
    private Array<EnergyWrapper> availableEnergy;

    public EnergyManager(final RoomController roomController) {
        this.roomController = roomController;

        this.availableEnergy = new Array<EnergyWrapper>();
        // Fetch ALL energy
        Array<Source> foundEnergy = (Array<Source>) this.roomController.getRoom().find(GlobalVariables.FIND_DROPPED_ENERGY,
                null);

        Lodash.forIn(foundEnergy, new LodashCallback1<Energy>() {
            @Override
            public boolean invoke(Energy energy) {
                // Check for enemies near the source;
                EnergyWrapper energyWrapper = new EnergyWrapper(roomController, energy, roomController.getEnergyMemory(energy.id));
                if (energyWrapper.availableToClaim() > 0) {
                    availableEnergy.push(energyWrapper);
                }

                if (roomController.getTasksManager().getMemory().$get(energy.id) == null) {
                    roomController.getTasksManager().addTask(new TaskPickupEnergy(roomController, energy.id, energy));
                }

                return true;
            }
        }, this);
    }

    public Energy claimEnergy(CreepCollector creepCollector) {
        if (availableEnergy.$length() <= 0) {
            return null;
        }

        TemporaryVariables.tempEnergyWrapper = null;
        Lodash.forIn(availableEnergy, new LodashCallback1<EnergyWrapper>() {
            @Override
            public boolean invoke(EnergyWrapper energyWrapper) {

                // If it has energy left to be claimed
                if (energyWrapper.availableToClaim() > 0) {
                    // If no current highest
                    if (TemporaryVariables.tempEnergyWrapper == null) {
                        TemporaryVariables.tempEnergyWrapper = energyWrapper;
                    } else {
                        if (energyWrapper.availableToClaim() > TemporaryVariables.tempEnergyWrapper.availableToClaim()) {
                            TemporaryVariables.tempEnergyWrapper = energyWrapper;
                        }
                    }
                }

                return true;
            }
        }, this);

        if (TemporaryVariables.tempEnergyWrapper != null) {
            TemporaryVariables.tempEnergyWrapper.claim(creepCollector);

            return TemporaryVariables.tempEnergyWrapper.getEnergy();
        } else {
            return null;
        }
    }
}
