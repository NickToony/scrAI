package com.nicktoony.scrAI.World.Creeps;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.Task;
import com.nicktoony.screeps.*;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

import static com.nicktoony.screeps.GlobalVariables.MOVE;
import static com.nicktoony.screeps.GlobalVariables.WORK;
import static com.nicktoony.screeps.GlobalVariables.CARRY;

/**
 * Created by nick on 26/07/15.
 */
public class CreepWorker extends CreepWrapper {
    public CreepWorker(RoomController roomController, Creep creep) {
        super(roomController, creep);
    }

    private String taskId;
    private Task task;
    private CreepWorker myself;

    @Override
    public void init() {
        memory.$put("taskId", taskId);
    }

    @Override
    public void create() {
        taskId = (String) memory.$get("taskId");
        task = roomController.getTasksManager().getTask(taskId);
        myself = this;
    }

    @Override
    public void step() {
        if (task == null) {

            Lodash.forIn(roomController.getTasksManager().getSortedTasks(), new LodashCallback1<Task>() {
                @Override
                public boolean invoke(Task possibleTask) {
                    if (possibleTask.canAct(myself)) {
                        task = possibleTask;
                        taskId = possibleTask.getAssociatedId();
                        return false;
                    }
                    return true;
                }
            }, this);
        } else {
            if (!task.act(this)) {
                taskId = null;
                task = null;
            }
        }
    }

    public boolean moveTo(RoomPosition position) {
        // if reached target
        if (this.creep.pos.inRangeTo(position, 1)) {
            return true;
        } else {
            // move to target
            this.creep.moveTo(position);
            return false;
        }
    }

    @Override
    public void save() {
        memory.$put("taskId", taskId);
    }

    public static CreepDefinition define(RoomController roomController) {

        Array<String> abilities = new Array<String>();

        int comboCost = Constants.WORK_COST + Constants.CARRY_COST + Constants.MOVE_COST;
        int totalWorkParts = Math.max(1, (int) Math.floor(roomController.getRoomTotalStorage() / comboCost));
        for (int i = 0; i < totalWorkParts; i++) {
            abilities.push(WORK, MOVE, CARRY);
        }

        return new CreepDefinition(Constants.CREEP_WORKER_ID, Constants.CREEP_WORKER_NAME,
                abilities, roomController, null);
    }

    public int getCarryCapacity() {
        return this.creep.carryCapacity;
    }

    public String getTaskId() {
        return taskId;
    }
}
