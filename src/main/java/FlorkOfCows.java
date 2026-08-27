import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class FlorkOfCows {
    public static void main(String[] args) {
        String banner = "  ______ _            _     ____   __  _____                  \n"
                + " |  ____| |          | |   / __ \\ / _|/ ____|                 \n"
                + " | |__  | | ___  _ __| | _| |  | | |_| |     _____      _____ \n"
                + " |  __| | |/ _ \\| '__| |/ / |  | |  _| |    / _ \\ \\ /\\ / / __|\n"
                + " | |    | | (_) | |  |   <| |__| | | | |___| (_) \\ V  V /\\__ \\\n"
                + " |_|    |_|\\___/|_|  |_|\\_\\\\____/|_|  \\_____\\___/ \\_/\\_/ |___/\n";

        Ui ui = new Ui();
        Storage storage = new Storage();
        ui.showWelcome(banner);

        TaskList tasks;
        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            tasks = new TaskList();
            ui.showLoadingError(e.getMessage());
        }

        while (ui.hasNextCommand()) {
            String line = ui.readCommand();

            if (line.isEmpty()) {
                continue;
            }

            Command command = Parser.parseCommand(line);

            try {
                switch (command) {
                    case BYE:
                        ui.showBye();
                        return;
                    case LIST:
                        ui.showTaskList(tasks);
                        break;
                    case MARK:
                        handleMark(tasks, line, true, storage, ui);
                        break;
                    case UNMARK:
                        handleMark(tasks, line, false, storage, ui);
                        break;
                    case DELETE:
                        handleDelete(tasks, line, storage, ui);
                        break;
                    case ON: {
                        String dateArg = line.length() > 2 ? line.substring(2).trim() : "";
                        LocalDate queryDate = Parser.parseDateArg(dateArg);
                        ui.showTasksOnDate(tasks.getTasksOn(queryDate), queryDate);
                        break;
                    }
                    case TODO: {
                        String description = Parser.parseTodoDescription(line);
                        addTask(tasks, new Todo(description), storage, ui);
                        break;
                    }
                    case DEADLINE: {
                        String[] parts = Parser.parseDeadlineParts(line);
                        String deadlineDescription = parts[0];
                        String by = parts[1];
                        addTask(tasks, new Deadline(deadlineDescription, by), storage, ui);
                        break;
                    }
                    case EVENT: {
                        String[] parts = Parser.parseEventParts(line);
                        String eventDescription = parts[0];
                        String from = parts[1];
                        String to = parts[2];
                        addTask(tasks, new Event(eventDescription, from, to), storage, ui);
                        break;
                    }
                    case UNKNOWN:
                    default:
                        throw new FlorkingExceptions("What you saying? I don't get sia.");
                }
            } catch (FlorkingExceptions e) {
                ui.showError(e.getMessage());
            }
        }
    }

    private static void addTask(TaskList tasks, Task newTask, Storage storage, Ui ui) throws FlorkingExceptions {
        tasks.add(newTask);
        saveOrThrow(tasks, storage);
        ui.showTaskAdded(newTask, tasks.size());
    }

    private static void handleMark(TaskList tasks, String line, boolean markingDone, Storage storage, Ui ui)
            throws FlorkingExceptions {
        String[] parts = line.split(" ", 2);
        String argument = parts.length > 1 ? parts[1] : "";
        int idx = tasks.parseIndex(argument, markingDone ? "mark" : "unmark");
        Task task = markingDone ? tasks.mark(idx) : tasks.unmark(idx);
        saveOrThrow(tasks, storage);
        if (markingDone) {
            ui.showTaskMarked(task.toString());
        } else {
            ui.showTaskUnmarked(task.toString());
        }
    }

    private static void handleDelete(TaskList tasks, String line, Storage storage, Ui ui) throws FlorkingExceptions {
        String[] parts = line.split(" ", 2);
        String argument = parts.length > 1 ? parts[1] : "";
        int idx = tasks.parseIndex(argument, "delete");
        Task removed = tasks.delete(idx);
        saveOrThrow(tasks, storage);
        ui.showTaskDeleted(removed, tasks.size());
    }

    private static void saveOrThrow(TaskList tasks, Storage storage) throws FlorkingExceptions {
        try {
            storage.save(tasks.getAll());
        } catch (IOException e) {
            throw new FlorkingExceptions("Couldn't save to disk: " + e.getMessage());
        }
    }
}