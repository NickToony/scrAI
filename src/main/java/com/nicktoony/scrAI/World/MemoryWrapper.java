package com.nicktoony.scrAI.World;

import com.nicktoony.scrAI.Controllers.RoomController;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 29/07/15.
 */
public abstract class MemoryWrapper {
    protected RoomController roomController;
    protected Map<String, Object> memory;

    public MemoryWrapper(RoomController roomController, Map<String, Object> memory) {
        this.roomController = roomController;
        this.memory = memory;
    }

    public void prepare() {
        if (memory.$get("init") == null) {
            // Trigger create event
            init();
            // Don't trigger it again later
            memory.$put("init", true);
        }

        create();
    }

    public abstract void init();

    public abstract void create();

    public abstract void step();
}
