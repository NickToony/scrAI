package com.nicktoony.screeps;

import org.stjs.javascript.Array;

/**
 * Created by nick on 26/07/15.
 */
public class RoomPosition {
    public int x;
    public int y;
    public String roomName;

    public RoomPosition(int x, int y, String roomName) {
        this.x = x;
        this.y = y;
        this.roomName = roomName;
    }

    public Array<?> findInRange(String type, int range) {
        return null;
    }

    public boolean inRangeTo(RoomPosition pos, int dist) {
        return false;
    }

    public Array lookFor(String terrain) {
        return null;
    }
}
