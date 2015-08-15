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
import com.nicktoony.screeps.global.GlobalVariables;
import com.nicktoony.screeps.Room;
import com.nicktoony.screeps.global.ResponseTypes;
import com.nicktoony.screeps.structures.Spawn;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class RoomController {

    // room globals
    public boolean hasPathFound = false;

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
    private Map<String, Object> timersMemory;
    private Map<String, Object> cpuMemory;
    private float cpu = 0;

    public RoomController(Room room) {
        this.room = room;
        this.alertStatus = Constants.ALERT_STATUS_LOW;

        // Check if memory is defined
        if (this.room.memory.$get("created") == null) {
            // Finally we're created
            this.room.memory.$put("created", true);
        }
        this.timersMemory = getMemory("timersMemory");
        this.cpuMemory = getMemory("cpuMemory");

        // Advisors
        this.economyAdvisor = new EconomyAdvisor(this);
        this.militaryAdvisor = new MilitaryAdvisor(this);
    }

    private Object getMemoryOrDefault(String key, Object value) {
        Object check = this.room.memory.$get(key);
        if (check == null) {
            check = value;
        }
        return check;
    }

    private void putMemory(String key, Object value) {
        this.room.memory.$put(key, value);
    }

    public void step() {

        if (handleManagerUpdates()) {
            return;
        }

        if (updateManager("Advisors", Constants.DELAY_ADVISORS)) {
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
                    if (spawn.canCreateCreep(request.getAbilities(), request.getName()) == ResponseTypes.OK) {
                        Global.console.log("BUILD: " + request.getName());
                        spawn.createCreep(request.getAbilities(), request.getName(), request.getMemory());
                    }
                }
            }

            updateTimer("Advisors");
            return;
        }

        Lodash.forIn(getPopulationManager().getAllCreeps(), new LodashCallback1<CreepWrapper>() {
            @Override
            public boolean invoke(CreepWrapper creepWrapper) {
                creepWrapper.step();
                return true;
            }
        }, this);
    }

    public void save() {
        // Tasks manager
        getTasksManager().save();
    }

    private boolean handleManagerUpdates() {


        if (updateManager("ConstructionManager", Constants.DELAY_CONSTRUCTION_MANAGER)) {
            getConstructionManager().update();
            updateTimer("ConstructionManager");
            return true;
        }

        if (updateManager("EnergyManager", Constants.DELAY_ENERGY_MANAGER)) {
            getEnergyManager().update();
            updateTimer("EnergyManager");
            return true;
        }

        if (updateManager("PathsManager", Constants.DELAY_PATH_MANAGER)) {
            getPathsManager().update();
            updateTimer("PathsManager");
            return true;
        }

        if (updateManager("PopulationManager", Constants.DELAY_POPULATION_MANAGER)) {
            getPopulationManager().update();
            updateTimer("PopulationManager");
            return true;
        }

        if (updateManager("SourcesManager", Constants.DELAY_SOURCES_MANAGER)) {
            getSourcesManager().update();
            updateTimer("SourcesManager");
            return true;
        }

        if (updateManager("SpawnsManager", Constants.DELAY_SPAWN_MANAGER)) {
            getSpawnsManager().update();
            updateTimer("SpawnsManager");
            return true;
        }

        if (updateManager("StructureManager", Constants.DELAY_STRUCTURE_MANAGER)) {
            getStructureManager().update();
            updateTimer("StructureManager");
            return true;
        }

        if (updateManager("TaskManager", Constants.DELAY_TASK_MANAGER)) {
            getTasksManager().update();
            updateTimer("TaskManager");
            return true;
        }

        return false;
    }

    private boolean updateManager( String name, int delay) {
        if (getTimer(name) + delay < Game.time) {
            return true;
        }
        return false;
    }

    private Map<String, Object> getMemory(String name) {
        Map<String, Object> memory = (Map<String, Object>) this.room.memory.$get(name);
        if (memory == null) {
            this.room.memory.$put(name, JSCollections.$map());
            return getMemory(name);
        }
        return memory;
    }

    public Room getRoom() {
        return room;
    }

    public void setAlertStatus(int alertStatus) {
        this.alertStatus = alertStatus;
    }

    public int getRoomTotalStorage() {
        return room.energyCapacityAvailable - Constants.OFFSET_ROOM_STORAGE;
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
        timersMemory.$put(manager, Game.time + (Math.random() * Constants.DELAY_RANDOM));
    }

    public PopulationManager getPopulationManager() {
        if (populationManager == null) {
            resetCPU();
            populationManager = new PopulationManager(this, getMemory("populationMemory"));
            saveCPU("PopulationManager");
        }
        return populationManager;
    }

    public SourcesManager getSourcesManager() {
        if (sourcesManager == null) {
            resetCPU();
            sourcesManager = new SourcesManager(this, getMemory("sourcesMemory"));
            saveCPU("SourcesManager");
        }
        return sourcesManager;
    }

    public SpawnsManager getSpawnsManager() {
        if (spawnsManager == null) {
            resetCPU();
            spawnsManager = new SpawnsManager(this, getMemory("spawnsMemory"));
            saveCPU("SpawnsManager");
        }
        return spawnsManager;
    }

    public EnergyManager getEnergyManager() {
        if (energyManager == null) {
            resetCPU();
            energyManager = new EnergyManager(this, getMemory("energyMemory"));
            saveCPU("EnergyManager");
        }
        return energyManager;
    }

    public TaskManager getTasksManager() {
        if (tasksManager == null) {
            resetCPU();
            tasksManager = new TaskManager(this, getMemory("taskMemory"));
            saveCPU("TaskManager");
        }
        return tasksManager;
    }

    public PathsManager getPathsManager() {
        if (pathsManager == null) {
            resetCPU();
            pathsManager = new PathsManager(this, getMemory("pathMemory"));
            saveCPU("PathsManager");
        }
        return pathsManager;
    }

    public ConstructionManager getConstructionManager() {
        if (constructionManager == null) {
            resetCPU();
            constructionManager = new ConstructionManager(this, getMemory("constructionMemory"));
            saveCPU("ConstructionManager");
        }
        return constructionManager;
    }

    public StructureManager getStructureManager() {
        if (structureManager == null) {
            resetCPU();
            structureManager = new StructureManager(this, getMemory("structureMemory"));
            saveCPU("StructureManager");
        }
        return structureManager;
    }

    private void resetCPU() {
        cpu = Game.getUsedCpu();
    }

    private void saveCPU(String name) {
        Object prevValue = cpuMemory.$get(name);
        float value = Game.getUsedCpu() - cpu;
        if (prevValue != null) {
            value += (Float) prevValue;
        }

        cpuMemory.$put(name, value);
    }
}
