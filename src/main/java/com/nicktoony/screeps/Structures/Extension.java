package com.nicktoony.screeps.Structures;

import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.Structures.Structure;
import com.nicktoony.screeps.interfaces.Depositable;
import com.nicktoony.screeps.interfaces.Transferable;

/**
 * Created by nick on 10/08/15.
 */
public class Extension extends Structure implements Depositable, Transferable {
    @Override
    public int transferEnergy(Creep creep, int amount) {
        return 0;
    }
}
