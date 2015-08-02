package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.screeps.Controller;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Spawn;

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
    public boolean canAct(CreepCollector creepCollector) {
        return controller != null
                && (creepCollector.getCreep().carry.energy > 0);
    }

    @Override
    public boolean act(CreepCollector creepCollector) {
        if (creepCollector.moveTo(controller.pos)) {
            creepCollector.getCreep().upgradeController(controller);
            return false;
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
        return 0;
    }
}
