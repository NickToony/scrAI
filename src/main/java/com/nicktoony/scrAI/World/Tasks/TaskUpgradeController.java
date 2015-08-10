package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepWorker;
import com.nicktoony.screeps.Structures.Controller;
import com.nicktoony.screeps.Game;

/**
 * Created by nick on 02/08/15.
 */
public class TaskUpgradeController extends Task {
    protected Controller controller;

    public TaskUpgradeController(RoomController roomController, String associatedId, Controller controller) {
        super(roomController, associatedId);
        this.controller = controller;
    }

    @Override
    public boolean canAct(CreepWorker creepWorker) {
        return controller != null
                && (creepWorker.getCreep().carry.energy > 0);
    }

    @Override
    public boolean canAssign(CreepWorker creepWorker) {
        return canAct(creepWorker);
    }

    @Override
    public boolean act(CreepWorker creepWorker) {
        if (creepWorker.moveTo(controller.pos)) {
            creepWorker.getCreep().upgradeController(controller);
        }
        return true;
    }

    @Override
    public boolean save() {
        if (controller == null) {
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
        controller = (Controller) Game.getObjectById(associatedId);
    }


    @Override
    public String getType() {
        return "3";
    }

    @Override
    public int getPriority() {
        return Constants.PRIORITY_UPGRADE;
    }
}
