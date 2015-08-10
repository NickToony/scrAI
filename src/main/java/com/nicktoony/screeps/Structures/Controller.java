package com.nicktoony.screeps.Structures;

import com.nicktoony.screeps.RoomPosition;
import com.nicktoony.screeps.interfaces.Upgradeable;

/**
 * Created by nick on 30/07/15.
 */
public class Controller extends Structure implements Upgradeable {
    public int level;
    public int progress;
    public int progressTotal;
    public int ticksToDowngrade;
}
