package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.LodashCallback2;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.Task;
import com.nicktoony.scrAI.World.Tasks.TaskPickupEnergy;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public class TaskManager {
    private Array<Task> tasks;
    private RoomController roomController;
    private Map<String, Object> memory;

    public TaskManager(final RoomController roomController, Map<String, Object> memory) {
        this.roomController = roomController;
        this.memory = memory;

        tasks = new Array<Task>();
        Lodash.forIn(this.memory, new LodashCallback2<Object, String>() {

            @Override
            public boolean invoke(Object memory, String associatedId) {
                String taskType = (String) ((Map<String, Object>) memory).$get("taskType");
                if (taskType == "1") {
                    Task task =  new TaskPickupEnergy(roomController, associatedId, null);
                    task.setMemory((Map<String, Object>) memory);
                    tasks.push(task);

                    task.prepare();
                }

                return true;
            }
        }, this);

    }

    public void addTask(Task task) {
        task.setMemory(JSCollections.$map());
        task.getMemory().$put("taskType", task.getType());
        this.tasks.push(task);
    }

    public void save() {
        Lodash.forIn(tasks, new LodashCallback1<Task>() {
            @Override
            public boolean invoke(Task task) {
                boolean success = task.save();
                if (success) {
                    memory.$put(task.getAssociatedId(), task.getMemory());
                } else {
                    memory.$delete(task.getAssociatedId());
                }
                return true;
            }
        }, this);
    }

    public Map<String, Object> getMemory() {
        return memory;
    }

    public Array<Task> getTasks() {
        return tasks;
    }
}
