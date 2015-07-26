package com.nicktoony.screeps;

import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public abstract class Position {
    public abstract Array<?> findInRange(String type, int range);
}
