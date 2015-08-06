package com.nicktoony.scrAI.Managers;

import com.nicktoony.helpers.Lodash;
import com.nicktoony.helpers.LodashCallback1;
import com.nicktoony.helpers.LodashCallback2;
import com.nicktoony.helpers.LodashSortCallback2;
import com.nicktoony.scrAI.Controllers.RoomController;
import com.nicktoony.scrAI.World.Tasks.*;
import org.stjs.javascript.Array;
import org.stjs.javascript.Global;
import org.stjs.javascript.JSCollections;
import org.stjs.javascript.Map;

/**
 * Created by nick on 02/08/15.
 */
public class TaskManager extends Manager{
    private Map<String, Task> tasks;
    private Array<Task> sortedTasks;
    private int taskCount = 0;
    private Map<String, Object> taskMemory;

    public TaskManager(RoomController roomControllerParam, Map<String, Object> memory) {
        super(roomControllerParam, memory);
        this.sortedTasks = null;
        this.taskMemory = (Map<String, Object>) memory.$get("taskMemory");

        tasks = new JSCollections().$map();
        Lodash.forIn(taskMemory, new LodashCallback2<Map<String, Object>, String>() {

            @Override
            public boolean invoke(Map<String, Object> innerMemory, String associatedId) {
                String taskType = (String) ( innerMemory).$get("taskType");
                Task task = null;
                if (taskType == "1") {
                    task =  new TaskPickupEnergy(roomController, associatedId, null);
                } else if (taskType == "2") {
                    task = new TaskDeposit(roomController, associatedId, null);
                } else if (taskType == "3") {
                    task = new TaskUpgradeController(roomController, associatedId, null);
                } else if (taskType == "4") {
                    task = new TaskBuild(roomController, associatedId, null);
                } else if (taskType == "5") {
                    task = new TaskRepair(roomController, associatedId, null);
                }

                if (task != null) {
                    task.setMemory(innerMemory);
                    tasks.$put(associatedId, task);

                    task.prepare();

                    taskCount ++;
                } else {
                    Global.console.log("TaskManager -> Constructor -> You forgot to setup the task creation -> " + taskType);
                }

                return true;
            }
        }, this);

        if (tasks.$get(roomController.getRoom().controller.id) == null) {
            addTask(new TaskUpgradeController(roomController, roomController.getRoom().controller.id, roomController.getRoom().controller));
        }
    }

    @Override
    protected void init() {
        memory.$put("taskMemory", JSCollections.$map());
    }

    @Override
    public void update() {

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
                    taskMemory.$put(task.getAssociatedId(), task.getMemory());
                } else {
                    taskMemory.$delete(task.getAssociatedId());
                }
                return true;
            }
        }, this);
    }

    public Map<String, Object> getTaskMemory() {
        return taskMemory;
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

    public int getTaskCount() {
        return taskCount;
    }
}
