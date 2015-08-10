package com.nicktoony.screeps.Structures;

import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.Helpers.Store;
import com.nicktoony.screeps.interfaces.Depositable;
import com.nicktoony.screeps.interfaces.Transferable;

/**
 * Created by nick on 10/08/15.
 */
public class Storage extends Structure implements Depositable, Transferable {

    public Store store;
    public int storeCapacity;

    @Override
    public int transferEnergy(Creep creep, int amount) {
        return 0;
    }
}
