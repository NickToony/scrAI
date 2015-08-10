package com.nicktoony.screeps.Structures;

import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.Helpers.SpawningCreep;
import com.nicktoony.screeps.interfaces.*;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class Spawn extends Structure implements Depositable, ConvenientMemory, Nameable, Transferable {

    public SpawningCreep spawning;

    public int canCreateCreep(Array<String> abilities, String name) {
        return 0;
    }

    public int createCreep(Array<String> abilities, String name, Map<String, Object> memory) {
        return 0;
    }

    @Override
    public int transferEnergy(Creep creep, int amount) {
        return 0;
    }
}
