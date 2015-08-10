package com.nicktoony.screeps;

import com.nicktoony.screeps.global.FindTypes;
import com.nicktoony.screeps.global.GlobalVariables;
import com.nicktoony.screeps.global.ResponseTypes;
import com.nicktoony.screeps.global.StructureTypes;
import com.nicktoony.screeps.structures.Controller;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;
import org.stjs.javascript.annotation.STJSBridge;

/**
 * Created by nick on 26/07/15.
 */
@STJSBridge
public abstract class Room {
    public String name;
    public Map<String, Object> memory;
    public Controller controller;
    public int energyAvailable;
    public int energyCapacityAvailable;

    public abstract Array<?> find(FindTypes type, Map parameters);

    public abstract Array<Map<String, Object>>  findPath(RoomPosition pos, RoomPosition pos1, Map<String, Object> strings);

    public abstract ResponseTypes createConstructionSite(int x, int y, StructureTypes structureRoad);
}
