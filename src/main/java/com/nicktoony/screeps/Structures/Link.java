package com.nicktoony.screeps.Structures;

import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.interfaces.Depositable;
import com.nicktoony.screeps.interfaces.Transferable;

/**
 * Created by nick on 10/08/15.
 */
public class Link extends Structure implements Depositable, Transferable {
    public int cooldown;

    @Override
    public int transferEnergy(Creep creep, int amount) {
        return 0;
    }
}
