package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.TaskPickupEnergy;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.global.FindTypes;
import com.nicktoony.screeps.global.GlobalVariables;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class EnergyManager extends Manager {
    public EnergyManager(RoomController roomController, Map<String, Object> memory) {
        super(roomController, memory);
    }

    @Override
    protected void init() {

    }

    public void update() {
        Global.console.log("EnergyManager -> Update");

        // Fetch ALL energy
        Array<Source> foundEnergy = (Array<Source>) this.roomController.getRoom().find(FindTypes.FIND_CONSTRUCTION_SITES,
                null);

        Lodash.forIn(foundEnergy, new LodashCallback1<Energy>() {
            @Override
            public boolean invoke(Energy energy) {
                if (roomController.getTasksManager().getTaskMemory().$get(energy.id) == null) {
                    roomController.getTasksManager().addTask(new TaskPickupEnergy(roomController, energy.id, energy));

                }
                return true;
            }
        }, this);
    }
}
