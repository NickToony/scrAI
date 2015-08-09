package com.nicktoony.scrAI.Advisors;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepDefinition;

/**
 * Created by nick on 26/07/15.
 * An advisor doesn't actually perform anything, it just takes data from managers
 * and makes a suggestion for the next action
 */
public abstract class Advisor {
    protected RoomController roomController;

    public Advisor(RoomController roomController) {
        this.roomController = roomController;
    }

    /**
     * Look at all the room's data, and determine what creep to create
     * @return a CreepDefiniton if decided, or null if nothing
     */
    public abstract CreepDefinition step();
}
