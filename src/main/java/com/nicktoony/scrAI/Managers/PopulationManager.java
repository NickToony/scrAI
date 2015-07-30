package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Creeps.CreepCollector;
import com.nicktoony.scrAI.World.Creeps.CreepMiner;
import com.nicktoony.scrAI.World.Creeps.CreepWrapper;
import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.GlobalVariables;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Lodash = require('lodash');
 * var CreepMiner = require('CreepMiner');
 */
public class PopulationManager {
    private Array<CreepWrapper> allCreeps;
    private RoomController roomController;
    private Map<String, Array<CreepWrapper>> sortedCreeps;

    public PopulationManager(final RoomController roomController) {
        this.roomController = roomController;
        this.allCreeps = new Array<CreepWrapper>();

        Array<Creep> foundCreeps = (Array<Creep>) this.roomController.getRoom().find(GlobalVariables.FIND_MY_CREEPS, JSCollections.$map());
        this.sortedCreeps = JSCollections.$map();
        Lodash.forIn(foundCreeps, new LodashCallback1<Creep>() {
            @Override
            public boolean invoke(Creep creep) {
                String type = creep.name.substring(0, 1);
                CreepWrapper creepWrapper = getCreepWrapper(type, creep);
                if (creepWrapper != null) {
                    getSortedCreeps(type).push(creepWrapper);
                    allCreeps.push(creepWrapper);
                }
                return true;
            }
        }, this);
    }

    public Array<CreepWrapper> getSortedCreeps(String id) {
        if (this.sortedCreeps.$get(id) == null) {
            this.sortedCreeps.$put(id, new Array<CreepWrapper>());
        }
        return this.sortedCreeps.$get(id);
    }

    private CreepWrapper getCreepWrapper(String id, Creep creep) {
        if (id == Constants.CREEP_MINER_ID) {
            return new CreepMiner(roomController, creep);
        } else if (id == Constants.CREEP_COLLECTOR_ID) {
            return new CreepCollector(roomController, creep);
        }

        return null;
    }

    public Array<CreepWrapper> getAllCreeps() {
        return allCreeps;
    }
}
