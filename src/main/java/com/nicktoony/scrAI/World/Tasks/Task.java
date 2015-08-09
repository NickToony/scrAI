package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepWorker;
import com.nicktoony.scrAI.World.Creeps.CreepWrapper;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public abstract class Task {

    protected String associatedId;
    protected RoomController roomController;
    protected Map<String, Object> memory;
    protected Array<CreepWrapper> creeps;

    public Task(RoomController roomController, String associatedId) {
        this.roomController = roomController;
        this.associatedId = associatedId;
        this.creeps = new Array<CreepWrapper>();
    }

    public void assignCreep(CreepWrapper creepWrapper) {
        this.creeps.push(creepWrapper);
    }

    public void setMemory(Map<String, Object> memory) {
        this.memory = memory;
    }

    /**
     * Can this task be carried out with the creep selected
     * @param creepWorker
     * @return
     */
    public abstract boolean canAct(CreepWorker creepWorker);

    public abstract boolean canAssign(CreepWorker creepWorker);

    /**
     * Act out this task with the current creep
     * @param creepWorker
     */
    public abstract boolean act(CreepWorker creepWorker);

    /**
     * Save the task. If return false, this task should be removed from memory.
     * @return
     */
    public boolean save() {
        return true;
    }

    public void init() {

    }

    public abstract void create();

    public void prepare() {
        if (memory.$get("init") == null) {
            // Trigger create event
            init();
            // Don't trigger it again later
            memory.$put("init", true);
        }

        create();
    }

    public String getAssociatedId() {
        return associatedId;
    }

    public Map<String, Object> getMemory() {
        return memory;
    }

    public abstract String getType();

    public abstract int getPriority();
}

