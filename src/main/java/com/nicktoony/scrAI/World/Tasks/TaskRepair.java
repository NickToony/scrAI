package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepWorker;
import com.nicktoony.screeps.ConstructionSite;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Structure;
import org.stjs.javascript.Global;

/**
 * Created by nick on 02/08/15.
 */
public class TaskRepair extends Task {
    protected Structure buildable;
    private float progress = 0;

    public TaskRepair(RoomController roomController, String associatedId, Structure buildable) {
        super(roomController, associatedId);
        this.buildable = buildable;
    }

    @Override
    public boolean canAssign(CreepWorker creepWorker) {
        return canAct(creepWorker)
                && (creeps.$length() == 0);
    }

    @Override
    public boolean canAct(CreepWorker creepWorker) {
        return buildable != null
                && (creepWorker.getCreep().carry.energy > 0)
                && buildable.hits < buildable.hitsMax;
    }

    @Override
    public boolean act(CreepWorker creepWorker) {
        if (creepWorker.moveTo(buildable.pos)) {
            creepWorker.getCreep().repair(buildable);
        }
        return true;
    }

    @Override
    public boolean save() {
        if (buildable == null || buildable.hits == buildable.hitsMax) {
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
        buildable = (Structure) Game.getObjectById(associatedId);
        if (buildable!=null) {
            progress = 1 - ((float) buildable.hits / buildable.hitsMax);
        }
    }


    @Override
    public String getType() {
        return "5";
    }

    @Override
    public int getPriority() {
        return Math.round(Constants.PRIORITY_REPAIR * progress);
    }
}
