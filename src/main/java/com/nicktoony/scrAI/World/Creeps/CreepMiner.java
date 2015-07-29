package com.nicktoony.scrAI.World.Creeps;

import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.SourceWrapper;
import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import static com.nicktoony.screeps.GlobalVariables.*;

/**
 * Created by nick on 26/07/15.
 */
public class CreepMiner extends CreepWrapper {
    public CreepMiner(RoomController roomController, Creep creep) {
        super(roomController, creep);
    }

    private Source target;
    private boolean atSource;

    @Override
    public void init() {
        memory.$put("atSource", false);
        memory.$put("target", null);
    }

    @Override
    public void create() {
        atSource = (Boolean) memory.$get("atSource");
        target = (Source) Game.getObjectById(memory.$get("target"));
    }

    @Override
    public void step() {
        if (target != null) {
            if (atSource) {
                this.creep.harvest(target);
            } else {
                this.creep.moveTo(target);
                if (this.creep.pos.inRangeTo(target.pos, 1)) {
                    memory.$put("atSource", true);
                }
            }
        } else {
            SourceWrapper sourceWrapper = roomController.getSourcesManager().getFreeSource();
            target = sourceWrapper.getSource();
            memory.$put("target", target.id);
        }
    }

    public static CreepDefinition define(RoomController roomController, int tier) {
        Array<String> abilities;

        if (tier == Constants.TIER_HIGH) {
            abilities = JSCollections.$array(WORK, CARRY, MOVE);
        } else if (tier == Constants.TIER_MEDIUM) {
            abilities = JSCollections.$array(WORK, CARRY, MOVE);
        } else {
            abilities = JSCollections.$array(WORK, CARRY, MOVE);
        }

        return new CreepDefinition(Constants.CREEP_MINER_ID, Constants.CREEP_MINER_NAME,
                abilities, roomController);
    }

    public Source getTarget() {
        return target;
    }
}
