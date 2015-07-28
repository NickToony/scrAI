package com.nicktoony.scrAI.Creeps;

import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Creep;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 */
public abstract class CreepWrapper {
    protected RoomController roomController;
    protected Creep creep;

    public CreepWrapper(RoomController roomController, Creep creep) {
        this.roomController = roomController;
        this.creep = creep;

        if (getMemory().$get("created") == null) {
            // Trigger create event
            create();
            // Don't trigger it again later
            getMemory().$put("created", true);
        }
    }

    public abstract void create();

    public abstract void step();

    protected Map<String, Object> getMemory() {
        return this.creep.memory;
    }

    public Creep getCreep() {
        return creep;
    }

    public RoomController getRoomController() {
        return roomController;
    }
}
