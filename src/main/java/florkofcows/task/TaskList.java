package florkofcows.task;

import java.time.LocalDate;
import java.util.ArrayList;

import florkofcows.exception.FlorkingExceptions;

/**
 * Mutable list wrapper for Task objects with basic operations.
 *
 * Provides add/mark/unmark/delete operations and simple index parsing/validation
 * used by command implementations.
 */
public class TaskList {
    private final ArrayList<Task> tasks;

    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    public TaskList() {
        this(new ArrayList<>());
    }

    public void add(Task task) {
        tasks.add(task);
    }

    public Task mark(int idx) throws FlorkingExceptions {
        validateIndex(idx, "mark");
        Task task = tasks.get(idx - 1);
        task.markAsDone();
        return task;
    }

    public Task unmark(int idx) throws FlorkingExceptions {
        validateIndex(idx, "unmark");
        Task task = tasks.get(idx - 1);
        task.markAsNotDone();
        return task;
    }

    public Task delete(int idx) throws FlorkingExceptions {
        validateIndex(idx, "delete");
        return tasks.remove(idx - 1);
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    public Task get(int index) {
        return tasks.get(index);
    }

    public ArrayList<Task> getAll() {
        return tasks;
    }

    public ArrayList<Task> getTasksOn(LocalDate queryDate) {
        ArrayList<Task> matches = new ArrayList<>();
        for (Task task : tasks) {
            if (task.isOccurringOn(queryDate)) {
                matches.add(task);
            }
        }
        return matches;
    }

    public int parseIndex(String argument, String actionName) throws FlorkingExceptions {
        if (argument == null || argument.trim().isEmpty()) {
            throw new FlorkingExceptions("Say properly which task you want " + actionName + ".");
        }
        int idx;
        try {
            idx = Integer.parseInt(argument.trim());
        } catch (NumberFormatException e) {
            throw new FlorkingExceptions("Oi, '" + argument.trim() + "' isn't a valid task number eh.");
        }
        validateIndex(idx, actionName);
        return idx;
    }

    private void validateIndex(int idx, String actionName) throws FlorkingExceptions {
        if (idx < 1 || idx > tasks.size()) {
            throw new FlorkingExceptions(
                    "You don't have task " + idx + " eh. You only got " + tasks.size() + " task(s).");
        }
    }
}
