package com.nicktoony.scrAI.World.Creeps;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.TemporaryVariables;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.EnergyWrapper;
import com.nicktoony.screeps.*;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;

import static com.nicktoony.screeps.GlobalVariables.MOVE;
import static com.nicktoony.screeps.GlobalVariables.CARRY;

/**
 * Created by nick on 26/07/15.
 */
public class CreepCollector extends CreepWrapper {
    public CreepCollector(RoomController roomController, Creep creep) {
        super(roomController, creep);
    }

    private static int STATE_FETCHING = 0;
    private static int STATE_DEPOSITING_SPAWN = 1;
    private static int STATE_DEPOSITING_CONTROLLER = 2;

    private String target;
    private int state;

    @Override
    public void init() {
        memory.$put("target", null);
        memory.$put("state", STATE_FETCHING);
    }

    @Override
    public void create() {
        target = (String) memory.$get("target");
        state = (Integer) memory.$get("state");
    }

    @Override
    public void step() {
        if (state == STATE_FETCHING) {
            if (target == null) {
                Energy energy = roomController.getEnergyManager().claimEnergy(this);
                if (energy != null) {
                    target = energy.id;
                }
            } else {
                // We have a target
                Energy energy = (Energy) Game.getObjectById(target);

                if (energy == null) {
                    target = null;
                    return;
                }

                // If in range
                if (this.creep.pos.inRangeTo(energy.pos, 1)) {
                    // transfer and change target
                    this.creep.pickup(energy);
                    target = null;
                    state = STATE_DEPOSITING_SPAWN;
                } else {
                    // Move to the target
                    this.creep.moveTo(energy);
                }
            }

        } else if (state == STATE_DEPOSITING_SPAWN) {
            if (this.creep.carry.energy <= 0) {
                state = STATE_FETCHING;
                target = null;
                return;
            }

            // No target? find one
            if (target == null) {
                // Look for a spawn with space
                Lodash.forIn(roomController.getSpawnsManager().getSpawns(), new LodashCallback1<Spawn>() {
                    @Override
                    public boolean invoke(Spawn spawn) {
                        if (spawn.energy < spawn.energyCapacity) {
                            target = spawn.id;
                            return false;
                        }
                        return true;
                    }
                }, this);

                // Just use the roomcontroller to deposit
                if (target == null) {
                    target = roomController.getRoom().controller.id;
                }
            } else {
                // We have a target
                Spawn spawn = (Spawn) Game.getObjectById(target);

                // If in range
                if (this.creep.pos.inRangeTo(spawn.pos, 1)) {
                    // transfer and change target
                    this.creep.transferEnergy(spawn);
                    target = null;
                } else {
                    // Move to the target
                    this.creep.moveTo(spawn);
                }
            }
        }
    }

    @Override
    public void save() {
        memory.$put("target", target);
        memory.$put("state", state);
    }

    public static CreepDefinition define(RoomController roomController) {
        Array<String> abilities;

        abilities = JSCollections.$array(MOVE, MOVE, CARRY, CARRY);

        return new CreepDefinition(Constants.CREEP_COLLECTOR_ID, Constants.CREEP_COLLECTOR_NAME,
                abilities, roomController, null);
    }

    public String getTarget() {
        return target;
    }

    public int getCarryCapacity() {
        return this.creep.carryCapacity;
    }
}
