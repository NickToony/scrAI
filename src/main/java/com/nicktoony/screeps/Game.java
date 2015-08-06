package com.nicktoony.screeps;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class Game {
    public static Array<Room> rooms;
    public static int time;
    public static Array<Creep> creeps;
    public static Map<String, Spawn> spawns;

    public static int getUsedCpu() {
        return 0;
    }

    public static Object getObjectById(Object target) {
        return null;
    }
}
