package com.nicktoony.screeps;

import com.nicktoony.screeps.Structures.Controller;
import com.nicktoony.screeps.Structures.Structure;
import com.nicktoony.screeps.interfaces.Buildable;
import com.nicktoony.screeps.interfaces.Depositable;
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
    public Carry carry;
    public int carryCapacity;

    public void harvest(Source target) {

    }

    public abstract int moveTo(Object target, Map<String, ?> opts);

    public abstract int transferEnergy(Depositable target);

    public abstract void pickup(Energy energy);

    public abstract void upgradeController(Controller controller);

    public abstract void build(Buildable buildable);

    public abstract void repair(Structure buildable);
}
