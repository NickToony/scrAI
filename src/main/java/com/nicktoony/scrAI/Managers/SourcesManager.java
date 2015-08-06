package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.*;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.SourceWrapper;
import com.nicktoony.screeps.Creep;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.ScreepsArray;
import com.nicktoony.screeps.Source;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class SourcesManager extends Manager {
    private Array<SourceWrapper> sources;
    private Array<SourceWrapper> safeSources;
    private int maxMiners;

    public SourcesManager(RoomController roomControllerParam, Map<String, Object> memory) {
        super(roomControllerParam, memory);

        this.sources = new Array<SourceWrapper>();
        this.safeSources = new Array<SourceWrapper>();
        // Fetch ALL sources
        Array<Source> foundSources = (Array<Source>) this.roomController.getRoom().find(GlobalVariables.FIND_SOURCES,
                null);

        Lodash.forIn(foundSources, new LodashCallback1<Source>() {
            @Override
            public boolean invoke(Source source) {
                // Check for enemies near the source;
                Array<Creep> targets = (Array<Creep>) source.pos.findInRange(GlobalVariables.FIND_HOSTILE_CREEPS, 3);

                SourceWrapper sourceWrapper = new SourceWrapper(roomController, source, getSourcesMemory(source.id));
                sources.push(sourceWrapper);
                if (targets.$length() == 0) {
                    safeSources.push(sourceWrapper);
                    maxMiners += sourceWrapper.getAvailableSpots();
                }

                return true;
            }
        }, this);
    }

    @Override
    protected void init() {

    }

    @Override
    public void update() {

    }


    public Map<String, Object> getSourcesMemory(String id) {
        if (memory.$get(id) == null) {
            memory.$put(id, JSCollections.$map());
        }
        return (Map<String, Object>) memory.$get(id);
    }

    public Array<SourceWrapper> getSources() {
        return sources;
    }

    public Array<SourceWrapper> getSafeSources() {
        return safeSources;
    }

    public SourceWrapper getFreeSource() {
        TemporaryVariables.tempSource = null;
        Lodash.forIn(safeSources, new LodashCallback1<SourceWrapper>() {
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
