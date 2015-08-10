package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.*;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.SourceWrapper;
import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.global.FindTypes;
import com.nicktoony.screeps.global.GlobalVariables;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class SourcesManager extends Manager {
    private Array<SourceWrapper> sourceWrappers;
    private Array<SourceWrapper> safeSourceWrappers;
    private Array<String> sourceIds;
    private Array<String> safeSourceIds;
    private int maxMiners;

    public SourcesManager(RoomController roomControllerParam, Map<String, Object> memory) {
        super(roomControllerParam, memory);


        this.sourceIds = (Array<String>) memory.$get("sourceIds");
        this.safeSourceIds = (Array<String>) memory.$get("safeSourceIds");
        this.maxMiners = (Integer) memory.$get("maxMiners");
    }

    @Override
    protected void init() {
        memory.$put("sourceIds", new Array<String>());
        memory.$put("safeSourceIds", new Array<String>());
        memory.$put("maxMiners", 0);
    }

    @Override
    public void update() {
        Global.console.log("ConstructionManager -> Update");

        // Clear old array
        sourceIds = new Array<String>();
        safeSourceIds = new Array<String>();
        sourceWrappers = new Array<SourceWrapper>();
        safeSourceWrappers = new Array<SourceWrapper>();
        maxMiners = 0;

        // Fetch ALL sourceWrappers
        Array<Source> foundSources = (Array<Source>) this.roomController.getRoom().find(FindTypes.FIND_SOURCES,
                null);

        Lodash.forIn(foundSources, new LodashCallback1<Source>() {
            @Override
            public boolean invoke(Source source) {
                // Check for enemies near the source;
                Array<Creep> targets = (Array<Creep>) source.pos.findInRange(FindTypes.FIND_HOSTILE_CREEPS, 3, null);

                boolean safe = targets.$length() == 0;
                SourceWrapper sourceWrapper = addSourceWrapper(source, safe);
                sourceIds.push(source.id);
                if (safe) {
                    safeSourceIds.push(source.id);
                    maxMiners += sourceWrapper.getAvailableSpots();
                }

                return true;
            }
        }, this);

        memory.$put("sourceIds", sourceIds);
        memory.$put("safeSourceIds", safeSourceIds);
        memory.$put("maxMiners", maxMiners);
    }

    private SourceWrapper addSourceWrapper(Source source, boolean safe) {
        SourceWrapper sourceWrapper = new SourceWrapper(roomController, source, getSourcesMemory(source.id));
        sourceWrappers.push(sourceWrapper);
        if (safe) {
            safeSourceWrappers.push(sourceWrapper);
        }
        return sourceWrapper;
    }


    public Map<String, Object> getSourcesMemory(String id) {
        if (memory.$get(id) == null) {
            memory.$put(id, JSCollections.$map());
        }
        return (Map<String, Object>) memory.$get(id);
    }

    public Array<SourceWrapper> getSourceWrappers() {

        if (sourceWrappers == null) {
            loadSourceWrappers();
        }

        return sourceWrappers;
    }

    public Array<SourceWrapper> getSafeSourceWrappers() {

        if (safeSourceWrappers == null) {
            loadSourceWrappers();
        }

        return safeSourceWrappers;
    }

    private void loadSourceWrappers() {
        sourceWrappers = new Array<SourceWrapper>();
        safeSourceWrappers = new Array<SourceWrapper>();

        Global.console.log(sourceIds.$length());

        Lodash.forIn(sourceIds, new LodashCallback1<String>() {
            @Override
            public boolean invoke(String source) {
                // Check for enemies near the source;
                addSourceWrapper((Source) Game.getObjectById(source), safeSourceIds.indexOf(source) >= 0);
                return true;
            }
        }, this);
    }

    public SourceWrapper getFreeSource() {
        TemporaryVariables.tempSource = null;
        Lodash.forIn(getSafeSourceWrappers(), new LodashCallback1<SourceWrapper>() {
            @Override
            public boolean invoke(SourceWrapper sourceWrapper) {

                if (sourceWrapper.getTakenSpots() < sourceWrapper.getAvailableSpots()) {
                    TemporaryVariables.tempSource = sourceWrapper;
                    return false;
                } else {
                    return true;
                }
            }
        }, this);

        return TemporaryVariables.tempSource;
    }

    public int getMaxMiners() {
        return maxMiners;
    }
}
