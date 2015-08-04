package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Constants;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.SourceWrapper;
import com.nicktoony.scrAI.World.Tasks.TaskDeposit;
import com.nicktoony.scrAI.World.Tasks.TaskRepair;
import com.nicktoony.screeps.*;
import org.stjs.javascript.Array;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class StructureManager extends ManagerTimer {
    private Map<String, Object> memory;
    private int totalStorageCalculation = 0;
    private Array<String> roadableStructureIds;

    public StructureManager(final RoomController roomController, Map<String, Object> memory) {
        super(roomController, "StructureManager", Constants.DELAY_STRUCTURE_MANAGER);
        this.memory = memory;

        if (memory.$get("init") == null) {
            memory.$put("roadableStructureIds", new Array<String>());
            memory.$put("init", true);
        }

        if (!super.canRun()) {
            return;
        }
        super.hasRun();

        // Fetch ALL structures
        Array<Structure> foundSites = (Array<Structure>) this.roomController.getRoom().find(GlobalVariables.FIND_MY_STRUCTURES,
                null);
        // An array of structures to road to
        roadableStructureIds = new Array<String>();

        // If controller exists
        if (roomController.getRoom().controller != null) {
            // Road to the controller
            roadableStructureIds.push(roomController.getRoom().controller.id);
        }
        // For all sources
        Lodash.forIn(roomController.getSourcesManager().getSafeSources(), new LodashCallback1<SourceWrapper>() {
            @Override
            public boolean invoke(SourceWrapper variable) {
                // Road to the sources
                roadableStructureIds.push(variable.getSource().id);
                return true;
            }
        }, this);

        // For all structures
        Lodash.forIn(foundSites, new LodashCallback1<Structure>() {
            @Override
            public boolean invoke(Structure structure) {
                // If structure is damaged, and no task exists for it
                if (roomController.getTasksManager().getMemory().$get(structure.id) == null && structure.hits < structure.hitsMax) {
                    roomController.getTasksManager().addTask(new TaskRepair(roomController, structure.id, structure));
                }

                // If it's an extension
                if (structure.structureType == GlobalVariables.STRUCTURE_EXTENSION) {
                    // If it has no task, and energy requirements
                    if (roomController.getTasksManager().getMemory().$get(structure.id) == null && structure.energy < structure.energyCapacity) {
                        // Give it a deposit task
                        roomController.getTasksManager().addTask(new TaskDeposit(roomController, structure.id, structure));
                    }

                    // Add to total storage
                    totalStorageCalculation += structure.energyCapacity;
                    // Road to it
                    roadableStructureIds.push(structure.id);
                } else if (structure.structureType == GlobalVariables.STRUCTURE_SPAWN) {
                    // Spawn? add its storage capacity
                    totalStorageCalculation += structure.energyCapacity;
                    // Force to start of road!
                    roadableStructureIds.unshift(structure.id);
                }

                return true;
            }
        }, this);

        // Update room controllers total storage
        roomController.setRoomTotalStorage(totalStorageCalculation);

        // Save the roadable structures to memory
        memory.$put("roadableStructureIds", roadableStructureIds);
    }

    public Array<String> getRoadableStructureIds() {
        return (Array<String>) memory.$get("roadableStructureIds");
    }

    public Map<String, Object> getMemory() {
        return memory;
    }
}
