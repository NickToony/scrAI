package com.nicktoony.screeps.structures;

import com.nicktoony.screeps.helpers.Store;
import com.nicktoony.screeps.interfaces.DepositableStructure;

/**
 * Created by nick on 10/08/15.
 */
public abstract class Storage extends DepositableStructure {

    public Store store;
    public int storeCapacity;
}
