package com.nicktoony.scrAI.Managers;

import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Game;

/**
 * Created by nick on 02/08/15.
 */
public class ManagerTimer {
    protected RoomController roomController;
    private String name;
    private int offset;

    public ManagerTimer(RoomController roomController, String name, int offset) {
        this.roomController = roomController;
        this.name = name;
        this.offset = offset;
    }

    public boolean canRun() {
        return (roomController.getTimer(name) + offset < Game.time);
    }

    public void hasRun() {
        roomController.updateTimer(name);
    }


}
