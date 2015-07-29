package com.nicktoony.scrAI.World;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepMiner;
import com.nicktoony.screeps.RoomPosition;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 29/07/15.
 */
public class SourceWrapper extends MemoryWrapper {
    private RoomController roomController;
    private Source source;
    private int availableSpots;
    private int takenSpots;

    public SourceWrapper(RoomController roomController, Source source, Map<String, Object> sourceMemory) {
        super(roomController, sourceMemory);
        this.source = source;
        super.prepare();
    }

    @Override
    public void init() {
        int count = 0;
        for (int x = -1; x <= 1; x ++) {
            for (int y = -1; y <= 1; y ++) {
                RoomPosition position = new RoomPosition(this.source.pos.x + x, this.source.pos.y + y, this.source.pos.roomName);
                Array objects = position.lookFor("terrain");
                if (objects.$length() > 0 && objects.$get(0) == "plain" && (x != 0 || y != 0)) {
                    count ++;
                }
            }
        }

        memory.$put("availableSpots", count);
    }

    @Override
    public void create() {
        availableSpots = (Integer) memory.$get("availableSpots");
        Lodash.forIn(roomController.getPopulationManager().getSortedCreeps(Constants.CREEP_MINER_ID), new LodashCallback1<CreepMiner>() {
            @Override
            public boolean invoke(CreepMiner creepMiner) {
                if (creepMiner.getTarget() == source) {
                    takenSpots ++;
                }
                return true;
            }
        }, this);
    }

    @Override
    public void step() {

    }

    public Source getSource() {
        return source;
    }

    public int getAvailableSpots() {
        return Math.min(availableSpots, Constants.SETTING_MINER_PER_SPAWN);
    }

    public int getTakenSpots() {
        return takenSpots;
    }

    public void setTakenSpots(int takenSpots) {
        this.takenSpots = takenSpots;
    }
}
