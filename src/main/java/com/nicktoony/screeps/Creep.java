package com.nicktoony.screeps;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public abstract class Creep {
    public String name;
    public RoomPosition pos;
    public Map<String, Object> memory;
    public Array<String> body;

    public void harvest(Source target) {

    }

    public abstract int moveTo(Source target);
}
