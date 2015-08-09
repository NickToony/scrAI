package com.nicktoony.scrAI.World.Creeps;

import com.nicktoony.helpers.Lodash;
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

    @Override
    public void save() {

    }

    private Source target;
    private boolean atSource;
    private int workParts;

    @Override
    public void init() {
        memory.$put("atSource", false);
        memory.$put("target", null);
        int workParts = 0;
        for (String part : this.creep.body) {
            if (part == "WORK") {
                workParts ++;
            }
        }
        memory.$put("workParts", workParts);
    }

    @Override
    public void create() {
        atSource = (Boolean) memory.$get("atSource");
        target = (Source) Game.getObjectById(memory.$get("target"));
        workParts = (Integer) memory.$get("workParts");
    }

    @Override
    public void step() {
        if (target != null) {
            if (atSource) {
                this.creep.harvest(target);
            } else {
                this.creep.moveTo(target, null);
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

    public static CreepDefinition define(RoomController roomController, int workParts, Source source) {
        Array<String> abilities = JSCollections.$array(MOVE);
        int totalWorkParts = Math.min((int) Math.floor((roomController.getRoomTotalStorage() - Constants.MOVE_COST) / Constants.WORK_COST), workParts);
        for (int i = 0; i < totalWorkParts; i++) {
            abilities.push(WORK);
        }

        return new CreepDefinition(Constants.CREEP_MINER_ID, Constants.CREEP_MINER_NAME,
                abilities, roomController, JSCollections.$map("target", (Object) source.id));
    }

    public Source getTarget() {
        return target;
    }

    public int getWorkParts() {
        return workParts;
    }
}
