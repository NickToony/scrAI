package com.nicktoony.screeps;

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
    public abstract Array<?> find(String type, Map parameters);
}
