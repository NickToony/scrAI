package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.SourceWrapper;
import com.nicktoony.scrAI.World.Tasks.TaskDeposit;
import com.nicktoony.scrAI.World.Tasks.TaskRepair;
import com.nicktoony.screeps.*;
import com.nicktoony.screeps.Structures.Extension;
import com.nicktoony.screeps.Structures.Structure;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.Map;

/**
 * Created by nick on 26/07/15.
 */
public class StructureManager extends Manager {
    private int totalStorageCalculation = 0;
    private Array<String> roadableStructureIds;

    public StructureManager(RoomController roomController, Map<String, Object> memory) {
        super(roomController, memory);
    }

    @Override
    protected void init() {
        memory.$put("roadableStructureIds", new Array<String>());
    }

    @Override
    public void update() {
        Global.console.log("StructureManager -> Update");

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
        Lodash.forIn(roomController.getSourcesManager().getSafeSourceWrappers(), new LodashCallback1<SourceWrapper>() {
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
                if (roomController.getTasksManager().getTaskMemory().$get(structure.id) == null && structure.hits < structure.hitsMax) {
                    roomController.getTasksManager().addTask(new TaskRepair(roomController, structure.id, structure));
                }

                // If it's an extension
                if (structure.structureType == GlobalVariables.STRUCTURE_EXTENSION) {
                    // If it has no task, and energy requirements
                    if (roomController.getTasksManager().getTaskMemory().$get(structure.id) == null
                            && ((Extension) structure).energy < ((Extension) structure).energyCapacity) {
                        // Give it a deposit task
                        roomController.getTasksManager().addTask(new TaskDeposit(roomController, structure.id, (Extension) structure));
                    }

                    // Add to total storage
                    totalStorageCalculation += ((Extension) structure).energyCapacity;
                    // Road to it
                    roadableStructureIds.push(structure.id);
                } else if (structure.structureType == GlobalVariables.STRUCTURE_SPAWN) {
                    // Spawn? add its storage capacity
                    totalStorageCalculation += ((Extension) structure).energyCapacity;
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
