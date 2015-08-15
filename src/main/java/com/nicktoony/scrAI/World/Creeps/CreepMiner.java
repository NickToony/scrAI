package com.nicktoony.scrAI.World.Creeps;

import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.SourceWrapper;
import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.Source;
import com.nicktoony.screeps.global.PartTypes;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

import static com.nicktoony.screeps.global.GlobalVariables.*;

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
                Map<String, Object> parameters = JSCollections.$map();
//                parameters.$put("heuristicWeight", 100);
                parameters.$put("reusePath", Constants.SETTINGS_PATH_REUSE); // reuse the path for a long time
                parameters.$put("noPathFinding", roomController.hasPathFound); // if have already done some pathfinding.. delay it.
                this.creep.moveTo(target, parameters);

                // If reached destination
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
        Array<PartTypes> abilities = JSCollections.$array(PartTypes.MOVE);
        int totalWorkParts = Math.min((int) Math.floor((roomController.getRoomTotalStorage() - Constants.MOVE_COST) / Constants.WORK_COST), workParts);
        for (int i = 0; i < totalWorkParts; i++) {
            abilities.push(PartTypes.WORK);
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
