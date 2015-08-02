package com.nicktoony.screeps;

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

    public abstract int moveTo(Object target);

    public abstract int transferEnergy(Depositable target);

    public abstract void pickup(Energy energy);

    public abstract void upgradeController(Controller controller);

    public abstract void build(Buildable buildable);
}
