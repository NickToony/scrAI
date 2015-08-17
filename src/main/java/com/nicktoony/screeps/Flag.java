package com.nicktoony.screeps;

import com.nicktoony.screeps.global.ColorTypes;
import com.nicktoony.screeps.global.ScreepsObject;
import org.stjs.javascript.Map;

/**
 * Created by nick on 17/08/15.
 */
public abstract class Flag extends ScreepsObject {
    public String name;
    public Map<String, Object> memory;
    public ColorTypes color;

    public abstract void remove();
}
