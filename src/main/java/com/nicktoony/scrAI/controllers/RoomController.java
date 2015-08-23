package com.nicktoony.scrAI.controllers;

import com.nicktoony.scrAI.planning.RoomPlanner;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Room;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 16/08/15.
 */
public class RoomController extends MemoryController {
    public Room room;

    public RoomController(Room room) {
        super(room.memory, null);
        this.room = room;
    }

    @Override
    public void init() {
        super.init();
    }

    public void step() {
        RoomPlanner roomPlanner = new RoomPlanner(getMemory("RoomPlanner"), this);
        while (Game.getUsedCpu() < 100) {
            roomPlanner.plan();
        }
    }

    private Map<String, Object> getMemory(String name) {
        if (memory.$get(name) == null) {
            memory.$put(name, JSCollections.$map());
        }
        return (Map<String, Object>) memory.$get(name);
    }
}
