package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.LodashCallback2;
import com.nicktoony.helpers.LodashSortCallback2;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.Task;
import com.nicktoony.scrAI.World.Tasks.TaskDepositSpawn;
import com.nicktoony.scrAI.World.Tasks.TaskPickupEnergy;
import com.nicktoony.scrAI.World.Tasks.TaskUpgradeController;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public class TaskManager {
    private Map<String, Task> tasks;
    private RoomController roomController;
    private Map<String, Object> memory;
    private Array<Task> sortedTasks;

    public TaskManager(final RoomController roomController, Map<String, Object> memory) {
        this.roomController = roomController;
        this.memory = memory;
        this.sortedTasks = null;

        tasks = new JSCollections().$map();
        Lodash.forIn(this.memory, new LodashCallback2<Object, String>() {

            @Override
            public boolean invoke(Object memory, String associatedId) {
                String taskType = (String) ((Map<String, Object>) memory).$get("taskType");
                Task task = null;
                if (taskType == "1") {
                    task =  new TaskPickupEnergy(roomController, associatedId, null);
                } else if (taskType == "2") {
                    task = new TaskDepositSpawn(roomController, associatedId, null);
                } else if (taskType == "3") {
                    task = new TaskUpgradeController(roomController, associatedId, null);
                }

                if (task != null) {
                    task.setMemory((Map<String, Object>) memory);
                    tasks.$put(associatedId, task);

                    task.prepare();
                } else {
                    Global.console.log("TaskManager -> Constructor -> You forgot to setup the task creation");
                }

                return true;
            }
        }, this);

        if (tasks.$get(roomController.getRoom().controller.id) == null) {
            addTask(new TaskUpgradeController(roomController, roomController.getRoom().controller.id, roomController.getRoom().controller));
        }
    }

    public void addTask(Task task) {
        task.setMemory(JSCollections.$map());
        task.getMemory().$put("taskType", task.getType());
        this.tasks.$put(task.getAssociatedId(), task);
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

    public Task getTask(String taskId) {
        return tasks.$get(taskId);
    }

    public Map<String, Task> getTasks() {
        return tasks;
    }

    public Array<Task> getSortedTasks() {
        if (sortedTasks == null) {
            sortedTasks = (Array<Task>) Lodash.sortBy(tasks, new LodashSortCallback2<Task, String>() {
                @Override
                public int invoke(Task variable1, String variable2) {
                    return -variable1.getPriority();
                }
            }, this);
        }

        return sortedTasks;
    }
}
