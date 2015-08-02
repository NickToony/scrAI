package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.TaskBuild;
import com.nicktoony.scrAI.World.Tasks.TaskPickupEnergy;
import com.nicktoony.screeps.ConstructionSite;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;

/**
 * Created by nick on 26/07/15.
 */
public class ConstructionManager extends ManagerTimer {
    public ConstructionManager(final RoomController roomController) {
        super(roomController, "ConstructionManager", Constants.DELAY_CONSTRUCTION_MANAGER);

        if (!super.canRun()) {
            return;
        }
        super.hasRun();

        // Fetch ALL energy
        Array<ConstructionSite> foundSites = (Array<ConstructionSite>) this.roomController.getRoom().find(GlobalVariables.FIND_CONSTRUCTION_SITES,
                null);

        Lodash.forIn(foundSites, new LodashCallback1<ConstructionSite>() {
            @Override
            public boolean invoke(ConstructionSite constructionSite) {
                if (roomController.getTasksManager().getMemory().$get(constructionSite.id) == null) {
                    roomController.getTasksManager().addTask(new TaskBuild(roomController, constructionSite.id, constructionSite));
                }
                return true;
            }
        }, this);
    }
}
