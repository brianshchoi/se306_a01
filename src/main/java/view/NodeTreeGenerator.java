package view;

import scheduleModel.IProcessor;
import scheduleModel.ISchedule;
import taskModel.Task;
import taskModel.TaskModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class takes in a TaskModel and generates a graphical representation of the dependencies
 */
public class NodeTreeGenerator {

    private TaskModel _taskModel;
    private ISchedule _schedule;

    NodeTreeGenerator(TaskModel taskModel) {
        _taskModel = taskModel;
    }

    public void render(){
        for (Task task : _taskModel.getTasks()){
            // create a Node
            Node node = new Node(task.getName(), task.getWeight(), "blue");


        }
    }



    public void a() {

        for (Task task : _taskModel.getTasks()) {
            int startTime = _schedule.getFinishTimeOf(task) - task.getWeight();
            IProcessor processor = _schedule.getProcessorOf(task);

            // Add all task
//            output.append(DotRenderer.addNode(task, startTime, processor));

            // Add dependencies and their weight
            if (task.getParents().size() > 0) {
                List<Task> parentsInOrderByName = new ArrayList<>(task.getParents());
                Collections.sort(parentsInOrderByName);
                for (Task parent : parentsInOrderByName) {
//                    output.append(DotRenderer.addDependency(parent, task));
                }
            }
        }
    }
}

