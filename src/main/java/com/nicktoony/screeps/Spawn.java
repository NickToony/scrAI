package com.nicktoony.screeps;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class Spawn implements Depositable {
    public RoomPosition pos;
    public String id;

    public int canCreateCreep(Array<String> abilities, String name) {
        return 0;
    }

    public int createCreep(Array<String> abilities, String name, Map<String, Object> memory) {
        return 0;
    }

    public Creep spawning;

    public int energy;
    public int energyCapacity;
}
