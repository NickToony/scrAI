package com.nicktoony.scrAI.World.Creeps;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.MemoryWrapper;
import com.nicktoony.screeps.Creep;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public abstract class CreepWrapper extends MemoryWrapper {
    protected Creep creep;

    public CreepWrapper(RoomController roomController, Creep creep) {
        super(roomController, creep.memory);
        this.creep = creep;
        super.prepare();
    }

    public abstract void save();

    public Creep getCreep() {
        return creep;
    }
}
