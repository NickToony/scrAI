package com.nicktoony.screeps;

import com.nicktoony.screeps.global.*;
import com.nicktoony.screeps.helpers.Targetable;
import com.sun.istack.internal.Nullable;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class RoomPosition extends Targetable {
    public int x;
    public int y;
    public String roomName;

    /**
     * You can create new RoomPosition object using its constructor.
     *
     * @param x x position in room
     * @param y y position in room
     * @param roomName room name
     */
    public RoomPosition(int x, int y, String roomName) {
        this.x = x;
        this.y = y;
        this.roomName = roomName;
    }

    /**
     * Create new @ConstructionSite at the specified location.
     *
     * @param structureType the structure type
     * @return result
     */
    public ResponseTypes createConstructionSite(StructureTypes structureType) {
        return ResponseTypes.OK;
    }

    /**
     * Create new Flag at the specified location.
     *
     * @param name The name of a new flag. It should be unique, i.e. the Game.flags object
     *             should not contain another flag with the same name (hash key).
     *             If not defined, a random name will be generated.
     * @param color The color of a new flag. The default value is COLOR_WHITE.
     * @return result
     */
    public ResponseTypes createFlag(@Nullable String name, @Nullable ColorTypes color) {
        return ResponseTypes.OK;
    }

    /**
     * Find an object with the shortest path from the given position. Uses A* search algorithm and Dijkstra's algorithm.
     *
     * @param type object type
     * @param options parameters for the search
     * @return response
     */
    public ResponseTypes findClosest(FindTypes type, @Nullable Map<String, Object> options) {
        return ResponseTypes.OK;
    }

    public ResponseTypes findClosest(Array<Targetable> objects, @Nullable Map<String, Object> options) {
        return ResponseTypes.OK;
    }

    public Array<? extends ScreepsObject> findInRange(FindTypes type, int range, @Nullable Map<String, Object> options) {
        return null;
    }

    public Array<? extends ScreepsObject> findInRange(Array<Targetable> objects, int range, @Nullable Map<String, Object> options) {
        return null;
    }

    public Array<Map<String, Object>> findPathTo(int x, int y, @Nullable Map<String, Object> options) {
        return null;
    }

    public Array<Map<String, Object>> findPathTo(Targetable target, @Nullable Map<String, Object> options) {
        return null;
    }

    public DirectionType getDirectionTo(int x, int y) {
        return null;
    }

    public DirectionType getDirectionTo(Targetable target) {
        return null;
    }

    public int getRangeTo(int x, int y) {
        return 0;
    }

    public int getRangeTo(Targetable target) {
        return 0;
    }

    public boolean inRangeTo(RoomPosition roomPosition, int range) {
        return true;
    }

    public boolean isEqualTo(int x, int y) {
        return true;
    }

    public boolean isEqualTo(Targetable targetable) {
        return true;
    }

    public Array<Map<String, Object>> look() {
        return null;
    }

    public Array<Map<String, Object>> lookFor(LookTypes type) {
        return null;
    }
}
