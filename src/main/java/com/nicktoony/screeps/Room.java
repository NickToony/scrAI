package com.nicktoony.screeps;

import com.nicktoony.screeps.global.*;
import com.nicktoony.screeps.helpers.SurvivalInfo;
import com.nicktoony.screeps.helpers.Targetable;
import com.nicktoony.screeps.structures.Controller;
import com.nicktoony.screeps.structures.Storage;
import com.sun.istack.internal.Nullable;
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
    public ModeTypes mode;
    private Storage storage;
    private SurvivalInfo survivalInfo;

    public abstract ResponseTypes createConstructionSite(int x, int y, StructureTypes type);

    public abstract ResponseTypes createConstructionSite(RoomPosition position, StructureTypes type);

    public abstract ResponseTypes createFlag(int x, int y, @Nullable String name, @Nullable ColorTypes color);

    public abstract ResponseTypes createFlag(RoomPosition position, @Nullable String name, @Nullable ColorTypes color);

    public abstract Array<? extends ScreepsObject> find(FindTypes type, Map<String, Object> options);

    public abstract RoomDirectionTypes findExitTo(Room room);

    public abstract RoomDirectionTypes findExitTo(String roomName);

    public abstract Array<Map<String, Object>>  findPath(RoomPosition fromPos, RoomPosition toPos, @Nullable Map<String, Object> options);

    public abstract RoomPosition getPositionAt(int x, int y);

    public abstract Array<Map<String, Object>> lookAt(int x, int y);

    public abstract Array<Map<String, Object>> lookAt(Targetable targetable);

    public abstract Array<Map<String, Object>> lookAtArea(int top, int left, int bottom, int right);

    public abstract Array<? extends ScreepsObject> lookForAt(LookTypes type, int x, int y);

    public abstract Array<? extends ScreepsObject> lookForAt(LookTypes type, Targetable targetable);

    public abstract Map<String, Map<String, Object>> lookForAtArea(LookTypes type, int top, int left, int bottom, int right);
}
