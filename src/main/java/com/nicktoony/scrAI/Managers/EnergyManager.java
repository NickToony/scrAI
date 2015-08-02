package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.TemporaryVariables;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepWorker;
import com.nicktoony.scrAI.World.Tasks.TaskPickupEnergy;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;

/**
 * Created by nick on 26/07/15.
 */
public class EnergyManager {
    private RoomController roomController;

    public EnergyManager(final RoomController roomController) {
        this.roomController = roomController;
        // Fetch ALL energy
        Array<Source> foundEnergy = (Array<Source>) this.roomController.getRoom().find(GlobalVariables.FIND_DROPPED_ENERGY,
                null);

        Lodash.forIn(foundEnergy, new LodashCallback1<Energy>() {
            @Override
            public boolean invoke(Energy energy) {
                if (roomController.getTasksManager().getMemory().$get(energy.id) == null) {
                    roomController.getTasksManager().addTask(new TaskPickupEnergy(roomController, energy.id, energy));
                }
                return true;
            }
        }, this);
    }
}
