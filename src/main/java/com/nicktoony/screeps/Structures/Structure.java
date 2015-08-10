package com.nicktoony.screeps.Structures;

import com.nicktoony.screeps.ScreepsObject;
import com.nicktoony.screeps.interfaces.Depositable;
import com.nicktoony.screeps.interfaces.Ownable;
import com.nicktoony.screeps.interfaces.Repairable;

/**
 * Created by nick on 02/08/15.
 */
public class Structure extends ScreepsObject implements Repairable, Ownable {
    public String structureType;

    public int destroy() {
        return 0;
    }

    public int notifyWhenAttacked(boolean enabled) {
        return 0;
    }
}
