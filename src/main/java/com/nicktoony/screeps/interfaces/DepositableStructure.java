package com.nicktoony.screeps.interfaces;

import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.global.ResponseTypes;
import com.nicktoony.screeps.structures.Structure;

/**
 * Created by nick on 30/07/15.
 */
public abstract class DepositableStructure extends Structure {
    public int energy = 0;
    public int energyCapacity = 0;

    public abstract ResponseTypes transferEnergy(Creep creep, int amount);
}
