package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.*;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.ScreepsArray;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Lodash = require('lodash');
 */
public class SourcesManager {
    private RoomController roomController;
    private Array<Source> sources;
    private Array<Source> safeSources;

    public SourcesManager(RoomController roomController) {
        this.roomController = roomController;

        // Fetch ALL sources
        this.sources = (Array<Source>) this.roomController.getRoom().find(GlobalVariables.FIND_SOURCES,
                null);

        // And fetch SAFE sources
        FilterCallback<Source> callback = new FilterCallback<Source>() {
            @Override
            public boolean invoke(Source source) {
                // Check for enemies near the source
                Array<Creep> targets = (Array<Creep>) source.pos.findInRange(GlobalVariables.FIND_HOSTILE_CREEPS, 3);

                if (targets.$length() == 0) {
                    return true;
                }

                return false;
            }
        };
        this.safeSources = (Array<Source>) this.roomController.getRoom().find(GlobalVariables.FIND_SOURCES,
                JSCollections.$map("filter", callback));
    }

    public Array<Source> getSources() {
        return sources;
    }

    public Array<Source> getSafeSources() {
        return safeSources;
    }

    public Source getFreeSource() {
        TemporaryVariables.tempSource = null;
        Lodash.forIn(sources, new LodashCallback1<Source>() {
            @Override
            public boolean invoke(Source source) {
                TemporaryVariables.tempSource = source;
                return true;
            }
        }, this);

        return TemporaryVariables.tempSource;
    }

    static { module.exports = SourcesManager.class; }
}
