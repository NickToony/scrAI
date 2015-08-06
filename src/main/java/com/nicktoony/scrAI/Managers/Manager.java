package com.nicktoony.scrAI.Managers;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Game;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public abstract class Manager {
    protected RoomController roomController;
    protected Map<String, Object> memory;

    public Manager(RoomController roomController, Map<String, Object> memory) {
        this.roomController = roomController;
        this.memory = memory;

        if (memory.$get("init") == null) {
            init();
            memory.$put("init", true);
        }
    }

    protected abstract void init();

    public abstract void update();

}
