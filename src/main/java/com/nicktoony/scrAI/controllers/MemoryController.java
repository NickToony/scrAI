package com.nicktoony.scrAI.controllers;

import org.stjs.javascript.Map;

/**
 * Created by nick on 16/08/15.
 */
public abstract class MemoryController {
    protected Map<String, Object> memory;
    protected RoomController roomController;

    public MemoryController(Map<String, Object> memory, RoomController roomController) {
        this.memory = memory;
        this.roomController = roomController;

        if (memory.$get("init") == null) {
            init();
        }
    }

    public void init() {
        memory.$put("init", true);
    }


}
