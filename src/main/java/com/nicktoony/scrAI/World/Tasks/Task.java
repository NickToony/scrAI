package com.nicktoony.scrAI.World.Tasks;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.scrAI.World.MemoryWrapper;
import com.nicktoony.screeps.Energy;
import com.nicktoony.screeps.Game;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public abstract class Task {

    protected String associatedId;
    protected RoomController roomController;
    protected Map<String, Object> memory;

    public Task(RoomController roomController, String associatedId) {
        this.roomController = roomController;
        this.associatedId = associatedId;
    }

    public void setMemory(Map<String, Object> memory) {
        this.memory = memory;
    }

    /**
     * Can this task be carried out with the creep selected
     * @param creepCollector
     * @return
     */
    public abstract boolean canAct(CreepCollector creepCollector);

    /**
     * Act out this task with the current creep
     * @param creepCollector
     */
    public abstract void act(CreepCollector creepCollector);

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
}

