package com.nicktoony.scrAI.Managers;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Game;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 *
 * A manager is a helper object that collects data from the world, associates memory with it, and
 * provides the methods to use/manipulate that data.
 */
public abstract class Manager {
    protected RoomController roomController;
    protected Map<String, Object> memory;

    /**
     * Default constructor assigns memory and roomcontroller
     *
     * @param roomController
     * @param memory
     */
    public Manager(RoomController roomController, Map<String, Object> memory) {
        this.roomController = roomController;
        this.memory = memory;

        // If not initiated
        if (memory.$get("init") == null) {
            init();
            memory.$put("init", true);
        }
    }

    /**
     * When the manager is first used, it initiates its memory
     */
    protected abstract void init();

    /**
     * When its timer is called, update occurs.
     */
    public abstract void update();

}
