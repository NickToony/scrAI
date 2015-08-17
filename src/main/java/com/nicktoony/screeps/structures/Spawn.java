package com.nicktoony.screeps.structures;

import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.global.PartTypes;
import com.nicktoony.screeps.global.ResponseTypes;
import com.nicktoony.screeps.helpers.SpawningCreep;
import com.nicktoony.screeps.interfaces.DepositableStructure;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public abstract class Spawn extends DepositableStructure {

    public SpawningCreep spawning;
    public String name;
    public Map<String, Object> memory;

    public abstract ResponseTypes canCreateCreep(Array<PartTypes> abilities, String name);

    public abstract ResponseTypes createCreep(Array<PartTypes> abilities, String name, Map<String, Object> memory);

    @Override
    public abstract ResponseTypes transferEnergy(Creep creep, int amount);
}
