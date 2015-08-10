package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.TaskBuild;
import com.nicktoony.screeps.ConstructionSite;
import com.nicktoony.screeps.global.FindTypes;
import com.nicktoony.screeps.global.GlobalVariables;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class ConstructionManager extends Manager {
    public ConstructionManager(RoomController roomController, Map<String, Object> memory) {
        super(roomController, memory);
    }

    @Override
    protected void init() {

    }

    public void update() {
        Global.console.log("ConstructionManager -> Update");

        // Fetch ALL energy
        Array<ConstructionSite> foundSites = (Array<ConstructionSite>) this.roomController.getRoom().find(FindTypes.FIND_CONSTRUCTION_SITES,
                null);

        Lodash.forIn(foundSites, new LodashCallback1<ConstructionSite>() {
            @Override
            public boolean invoke(ConstructionSite constructionSite) {
                if (roomController.getTasksManager().getTaskMemory().$get(constructionSite.id) == null) {
                    roomController.getTasksManager().addTask(new TaskBuild(roomController, constructionSite.id, constructionSite));
                }
                return true;
            }
        }, this);
    }
}
