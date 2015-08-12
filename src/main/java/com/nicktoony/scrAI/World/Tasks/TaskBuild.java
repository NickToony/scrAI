package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepWorker;
import com.nicktoony.screeps.ConstructionSite;
import com.nicktoony.screeps.Game;
import org.stjs.javascript.Global;

/**
 * Created by nick on 02/08/15.
 */
public class TaskBuild extends Task {
    protected ConstructionSite buildable;
    private float progress = 0;

    public TaskBuild(RoomController roomController, String associatedId, ConstructionSite buildable) {
        super(roomController, associatedId);
        this.buildable = buildable;
    }

    @Override
    public boolean canAssign(CreepWorker creepWorker) {
        return canAct(creepWorker)
                && (creeps.$length() <= 1);
    }

    @Override
    public boolean canAct(CreepWorker creepWorker) {
        return buildable != null
                && (creepWorker.getCreep().carry.energy > 0);
    }

    @Override
    public boolean act(CreepWorker creepWorker) {
        if (creepWorker.moveTo(buildable.pos)) {
            creepWorker.getCreep().build(buildable);
        }
        return true;
    }

    @Override
    public boolean save() {
        if (buildable == null) {
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
        buildable = (ConstructionSite) Game.getObjectById(associatedId);
        if (buildable != null) {
            progress = Math.max(0.25f, (float) buildable.progress / buildable.progressTotal);
        }
    }


    @Override
    public String getType() {
        return "4";
    }

    @Override
    public int getPriority() {
        return Math.round(Constants.PRIORITY_BUILD * progress);
    }
}
