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

        // Fetch ALL energy
        Array<Structure> foundSites = (Array<Structure>) this.roomController.getRoom().find(GlobalVariables.FIND_MY_STRUCTURES,
                null);
        roadableStructureIds = new Array<String>();

        Lodash.forIn(foundSites, new LodashCallback1<Structure>() {
            @Override
            public boolean invoke(Structure structure) {
                if (roomController.getTasksManager().getMemory().$get(structure.id) == null && structure.hits < structure.hitsMax) {
                    roomController.getTasksManager().addTask(new TaskRepair(roomController, structure.id, structure));
                }

                if (structure.structureType == GlobalVariables.STRUCTURE_EXTENSION) {
                    if (roomController.getTasksManager().getMemory().$get(structure.id) == null) {
                        roomController.getTasksManager().addTask(new TaskDeposit(roomController, structure.id, structure));
                    }

                    totalStorageCalculation += structure.energyCapacity;
                    roadableStructureIds.push(structure.id);
                } else if (structure.structureType == GlobalVariables.STRUCTURE_SPAWN) {
                    totalStorageCalculation += structure.energyCapacity;
                    roadableStructureIds.push(structure.id);
                }

                return true;
            }
        }, this);


        roomController.setRoomTotalStorage(totalStorageCalculation);
        if (roomController.getRoom().controller != null) {
            roadableStructureIds.push(roomController.getRoom().controller.id);
        }
        Lodash.forIn(roomController.getSourcesManager().getSafeSources(), new LodashCallback1<SourceWrapper>() {
            @Override
            public boolean invoke(SourceWrapper variable) {
                roadableStructureIds.push(variable.getSource().id);
                return true;
            }
        }, this);

        memory.$put("roadableStructureIds", roadableStructureIds);
    }

    public Array<String> getRoadableStructureIds() {
        return (Array<String>) memory.$get("roadableStructureIds");
    }

    public Map<String, Object> getMemory() {
        return memory;
    }
}
