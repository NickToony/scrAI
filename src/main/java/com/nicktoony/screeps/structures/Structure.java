package com.nicktoony.screeps.structures;

import com.nicktoony.screeps.global.ScreepsObject;
import com.nicktoony.screeps.global.StructureTypes;
import com.nicktoony.screeps.helpers.OwnerProperties;

/**
 * Created by nick on 02/08/15.
 */
public abstract class Structure extends ScreepsObject {
    public StructureTypes structureType;
    public int hits = 0;
    public int hitsMax = 0;
    public boolean my = false;
    public OwnerProperties owner = null;

    public abstract int destroy();

    public abstract int notifyWhenAttacked(boolean enabled);
}
