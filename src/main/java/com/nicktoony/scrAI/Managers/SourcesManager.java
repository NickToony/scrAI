package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.FilterCallback;
import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.TemporaryVariables;
import com.nicktoony.helpers.module;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.annotation.JavascriptFunction;
import org.stjs.javascript.functions.Callback;
import org.stjs.javascript.functions.Callback1;

/**
 * Created by nick on 26/07/15.
 * var stjs = require("stjs");
 * var Constants = require('Constants');
 * var Lodash = require('lodash');
 */
public class SourcesManager {
    private RoomController roomController;
    private Array<Source> sources;


    public SourcesManager(RoomController roomController) {
        this.roomController = roomController;

        FilterCallback callback = new FilterCallback<Source>() {
            @Override
            public boolean invoke(Source source) {
                return true;
            }
        };
        this.sources = (Array<Source>) this.roomController.getRoom().find(GlobalVariables.FIND_SOURCES,
                JSCollections.$map("filter", callback));
    }

    public Array<Source> getSources() {
        return sources;
    }

    public Source getFreeSource() {
        TemporaryVariables.tempSource = null;
        Lodash.forIn(sources, new Callback1<Source>() {
            @Override
            public void $invoke(Source source) {
                TemporaryVariables.tempSource = source;
            }
        }, this);

        return TemporaryVariables.tempSource;
    }

    static { module.exports = SourcesManager.class; }
}
