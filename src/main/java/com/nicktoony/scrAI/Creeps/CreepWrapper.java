package com.nicktoony.scrAI.Creeps;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Creep;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 */
public class CreepWrapper {
    protected RoomController roomController;
    protected Creep creep;

    public CreepWrapper(RoomController roomController, Creep creep) {
        this.roomController = roomController;
        this.creep = creep;
    }

    public Creep getCreep() {
        return creep;
    }

    public RoomController getRoomController() {
        return roomController;
    }
}
