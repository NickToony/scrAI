package com.nicktoony.scrAI.Controllers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Advisors.EconomyAdvisor;
import com.nicktoony.scrAI.Advisors.MilitaryAdvisor;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Managers.*;
import com.nicktoony.scrAI.World.Creeps.CreepDefinition;
import com.nicktoony.scrAI.World.Creeps.CreepWrapper;
import com.nicktoony.screeps.Game;
import com.nicktoony.screeps.GlobalVariables;
import com.nicktoony.screeps.Room;
import com.nicktoony.screeps.Spawn;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

import java.nio.file.PathMatcher;

/**
 * Created by nick on 26/07/15.
 */
public class RoomController {

    private Room room;
    private PopulationManager populationManager;
    private SourcesManager sourcesManager;
    private SpawnsManager spawnsManager;
    private EnergyManager energyManager;
    private TaskManager tasksManager;
    private PathsManager pathsManager;
    private ConstructionManager constructionManager;
    private StructureManager structureManager;
    private EconomyAdvisor economyAdvisor;
    private MilitaryAdvisor militaryAdvisor;
    private int alertStatus;
    private Map<String, Object> sourcesMemory;
    private Map<String, Object> timersMemory;
    private int roomTotalStorage = 300;

    public RoomController(Room room) {
        this.room = room;
        this.alertStatus = Constants.ALERT_STATUS_LOW;

        // Check if memory is defined
        if (this.room.memory.$get("created") == null) {
            this.room.memory.$put("sourcesMemory", JSCollections.$map());
            this.room.memory.$put("tasksMemory", JSCollections.$map());
            this.room.memory.$put("pathsMemory", JSCollections.$map());
            this.room.memory.$put("timersMemory", JSCollections.$map());

            // Finally we're created
            this.room.memory.$put("created", true);
        }
        this.sourcesMemory = (Map<String, Object>) this.room.memory.$get("sourcesMemory");
        this.timersMemory = (Map<String, Object>) this.room.memory.$get("timersMemory");

        // Managers
        this.tasksManager = new TaskManager(this, (Map<String, Object>) this.room.memory.$get("tasksMemory"));
        this.populationManager = new PopulationManager(this);
        this.sourcesManager = new SourcesManager(this);
        this.spawnsManager = new SpawnsManager(this);
        this.energyManager = new EnergyManager(this);
        this.pathsManager = new PathsManager(this, (Map<String, Object>) this.room.memory.$get("pathsMemory"));
        this.constructionManager = new ConstructionManager(this);
        this.structureManager = new StructureManager(this);

        // Advisors
        this.economyAdvisor = new EconomyAdvisor(this);
        this.militaryAdvisor = new MilitaryAdvisor(this);
    }

    public void step() {
        Spawn spawn = this.spawnsManager.getAvailableSpawn();
        if (spawn != null) {
            // Ask "advisors" what they want
            CreepDefinition economyRequest = this.economyAdvisor.step();
            CreepDefinition militaryRequest = this.militaryAdvisor.step();

            // Perform depending on ratio
            boolean doMilitary = false;
            if (this.alertStatus == Constants.ALERT_STATUS_CRITICAL) {
                doMilitary = true;
            } else if (this.alertStatus == Constants.ALERT_STATUS_NONE) {
                doMilitary = false;
            }

            CreepDefinition request = null;
            if (doMilitary && militaryRequest != null) {
                // request the military thing
                Global.console.log("MILITARY WANTS SOMETHING: " + militaryRequest);
                request = militaryRequest;
            } else if (economyRequest != null) {
                // request the economy thing
                Global.console.log("ECONOMY WANTS SOMETHING: " + economyRequest);
                request = economyRequest;
            } else if (militaryRequest != null) {
                // military is not priority, but economy didn't want anything
                Global.console.log("ECONOMY DOESNT WANT, BUT MILITARY DOES: " + militaryRequest);
                request = militaryRequest;
            } else {
                // no one wants anything
                Global.console.log("NOONE WANTS ANYTHING?!");
            }

            if (request != null) {
                if (spawn.canCreateCreep(request.getAbilities(), request.getName()) == GlobalVariables.OK) {
                    Global.console.log("BUILD: " + request.getName());
                    spawn.createCreep(request.getAbilities(), request.getName(), request.getMemory());
                }
            }
        }

        Lodash.forIn(getPopulationManager().getAllCreeps(), new LodashCallback1<CreepWrapper>() {
            @Override
            public boolean invoke(CreepWrapper creepWrapper) {
                creepWrapper.step();
                creepWrapper.save();
                return true;
            }
        }, this);

        // Save source memory
        this.room.memory.$put("sourcesMemory", sourcesMemory);
        this.room.memory.$put("timersMemory", timersMemory);

        // Tasks manager
        tasksManager.save();
        this.room.memory.$put("tasksMemory", tasksManager.getMemory());
        this.room.memory.$put("pathsMemory", pathsManager.getMemory());
    }

    public Room getRoom() {
        return room;
    }

    public PopulationManager getPopulationManager() {
        return populationManager;
    }

    public SourcesManager getSourcesManager() {
        return sourcesManager;
    }

    public SpawnsManager getSpawnsManager() {
        return spawnsManager;
    }

    public EnergyManager getEnergyManager() {
        return energyManager;
    }

    public void setAlertStatus(int alertStatus) {
        this.alertStatus = alertStatus;
    }

    public Map<String, Object> getSourcesMemory(String id) {
        if (sourcesMemory.$get(id) == null) {
            sourcesMemory.$put(id, JSCollections.$map());
        }
        return (Map<String, Object>) sourcesMemory.$get(id);
    }

    public int getRoomTotalStorage() {
        return roomTotalStorage - Constants.OFFSET_ROOM_STORAGE;
    }

    public TaskManager getTasksManager() {
        return tasksManager;
    }

    public PathsManager getPathsManager() {
        return pathsManager;
    }

    public int getTimer(String manager) {
        Integer time = (Integer) timersMemory.$get(manager);
        if (time == null) {
            timersMemory.$put(manager, 0);
            time = 0;
        }
        return time;
    }

    public void updateTimer(String manager) {
        timersMemory.$put(manager, Game.time);
    }
}
