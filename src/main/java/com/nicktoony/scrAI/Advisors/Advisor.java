package com.nicktoony.scrAI.Advisors;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepDefinition;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 */
public abstract class Advisor {
    protected RoomController roomController;

    public Advisor(RoomController roomController) {
        this.roomController = roomController;
    }

    public abstract CreepDefinition step();
}
