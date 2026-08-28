package florkofcows.command;

import java.io.IOException;
import java.time.LocalDate;

import florkofcows.exception.FlorkingExceptions;
import florkofcows.storage.Storage;
import florkofcows.task.Deadline;
import florkofcows.task.Event;
import florkofcows.task.Task;
import florkofcows.task.TaskList;
import florkofcows.task.Todo;
import florkofcows.ui.Ui;

/**
 * Base command type. Concrete command implementations perform actions on the
 * task list and may persist changes via Storage.
 */
public abstract class Command {
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws FlorkingExceptions;

    public boolean isExit() {
        return false;
    }

    protected void save(TaskList tasks, Storage storage) throws FlorkingExceptions {
        try {
            storage.save(tasks.getAll());
        } catch (IOException e) {
            throw new FlorkingExceptions("Couldn't save to disk: " + e.getMessage());
        }
    }

    public static class ExitCommand extends Command {
        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            ui.showBye();
        }

        @Override
        public boolean isExit() {
            return true;
        }
    }

    public static class ListCommand extends Command {
        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            ui.showTaskList(tasks);
        }
    }

    public static class OnDateCommand extends Command {
        private final LocalDate queryDate;

        public OnDateCommand(LocalDate queryDate) {
            this.queryDate = queryDate;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) {
            ui.showTasksOnDate(tasks.getTasksOn(queryDate), queryDate);
        }
    }

    public static class AddTodoCommand extends Command {
        private final String description;

        public AddTodoCommand(String description) {
            this.description = description;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) throws FlorkingExceptions {
            Task task = new Todo(description);
            tasks.add(task);
            save(tasks, storage);
            ui.showTaskAdded(task, tasks.size());
        }
    }

    public static class AddDeadlineCommand extends Command {
        private final String description;
        private final String by;

        public AddDeadlineCommand(String description, String by) {
            this.description = description;
            this.by = by;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) throws FlorkingExceptions {
            Task task = new Deadline(description, by);
            tasks.add(task);
            save(tasks, storage);
            ui.showTaskAdded(task, tasks.size());
        }
    }

    public static class AddEventCommand extends Command {
        private final String description;
        private final String from;
        private final String to;

        public AddEventCommand(String description, String from, String to) {
            this.description = description;
            this.from = from;
            this.to = to;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) throws FlorkingExceptions {
            Task task = new Event(description, from, to);
            tasks.add(task);
            save(tasks, storage);
            ui.showTaskAdded(task, tasks.size());
        }
    }

    public static class MarkCommand extends Command {
        private final int index;
        private final boolean markAsDone;

        public MarkCommand(int index, boolean markAsDone) {
            this.index = index;
            this.markAsDone = markAsDone;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) throws FlorkingExceptions {
            Task task = markAsDone ? tasks.mark(index) : tasks.unmark(index);
            save(tasks, storage);
            if (markAsDone) {
                ui.showTaskMarked(task.toString());
            } else {
                ui.showTaskUnmarked(task.toString());
            }
        }
    }

    public static class DeleteCommand extends Command {
        private final int index;

        public DeleteCommand(int index) {
            this.index = index;
        }

        @Override
        public void execute(TaskList tasks, Ui ui, Storage storage) throws FlorkingExceptions {
            Task removed = tasks.delete(index);
            save(tasks, storage);
            ui.showTaskDeleted(removed, tasks.size());
        }
    }
}
