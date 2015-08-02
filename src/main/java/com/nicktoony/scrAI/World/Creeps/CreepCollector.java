package com.nicktoony.scrAI.World.Creeps;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.*;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

import static com.nicktoony.screeps.GlobalVariables.MOVE;
import static com.nicktoony.screeps.GlobalVariables.WORK;
import static com.nicktoony.screeps.GlobalVariables.CARRY;

/**
 * Created by nick on 26/07/15.
 */
public class CreepCollector extends CreepWrapper {
    public CreepCollector(RoomController roomController, Creep creep) {
        super(roomController, creep);
    }

    private static final int STATE_FETCHING = 0;
    private static final int STATE_DEPOSITING = 1;

    // Deposit types
    private static final int DEPOSIT_TRANSFER = 0;
    private static final int DEPOSIT_UPGRADE = 1;

    private String target;
    private int state;
    private int depositType;

    @Override
    public void init() {
        memory.$put("target", null);
        memory.$put("state", STATE_FETCHING);
        memory.$put("depositType", DEPOSIT_TRANSFER);
    }

    @Override
    public void create() {
        target = (String) memory.$get("target");
        state = (Integer) memory.$get("state");
        depositType = (Integer) memory.$get("depositType");
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
                    state = STATE_DEPOSITING;
                } else {
                    // Move to the target
                    this.creep.moveTo(energy);
                }
            }

        } else if (state == STATE_DEPOSITING) {
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
                    depositType = DEPOSIT_UPGRADE;
                } else {
                    depositType = DEPOSIT_TRANSFER;
                }
            } else {

                if (depositType == DEPOSIT_TRANSFER) {
                    // We have a target, it's a spawn
                    Spawn spawn = (Spawn) Game.getObjectById(target);

                    // If in range
                    if (moveTo(spawn.pos)) {
                        // transfer energy and change target
                        this.creep.transferEnergy(spawn);
                        target = null;
                    }
                } else if (depositType == DEPOSIT_UPGRADE) {
                    // We have a target, it's a controller
                    Controller controller = (Controller) Game.getObjectById(target);

                    // If in range
                    if (moveTo(controller.pos)) {
                        // transfer energy and change target
                        this.creep.upgradeController(controller);
                        target = null;
                    }
                }
            }
        }
    }

    public boolean moveTo(RoomPosition position) {
        // if reached target
        if (this.creep.pos.inRangeTo(position, 1)) {
            return true;
        } else {
            // move to target
            this.creep.moveTo(position);
            return false;
        }
    }

    @Override
    public void save() {
        memory.$put("target", target);
        memory.$put("state", state);
        memory.$put("depositType", depositType);
    }

    public static CreepDefinition define(RoomController roomController) {
        Array<String> abilities;

        abilities = JSCollections.$array(WORK, MOVE, MOVE, CARRY, CARRY);

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
