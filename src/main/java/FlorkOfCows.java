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
        ui.showWelcome(banner);

        ArrayList<Task> tasks;
        try {
            tasks = Storage.load();
        } catch (IOException e) {
            tasks = new ArrayList<>();
            ui.showLoadingError(e.getMessage());
        }

        while (ui.hasNextCommand()) {
            String line = ui.readCommand();

            if (line.isEmpty()) {
                continue;
            }

            String commandWord = line.split(" ")[0].toUpperCase();
            Command command;

            try {
                command = Command.valueOf(commandWord);
            } catch (IllegalArgumentException e) {
                command = Command.UNKNOWN;
            }

            try {
                switch (command) {
                    case BYE:
                        ui.showBye();
                        return;
                    case LIST:
                        ui.showTaskList(tasks);
                        break;
                    case MARK:
                        handleMark(tasks, line, true, ui);
                        break;
                    case UNMARK:
                        handleMark(tasks, line, false, ui);
                        break;
                    case DELETE:
                        handleDelete(tasks, line, ui);
                        break;
                    case ON: {
                        String dateArg = line.length() > 2 ? line.substring(2).trim() : "";
                        if (dateArg.isEmpty()) {
                            throw new FlorkingExceptions("Which date? Try: on 2019-12-02");
                        }
                        LocalDate queryDate;
                        try {
                            queryDate = LocalDate.parse(dateArg);
                        } catch (DateTimeParseException e) {
                            throw new FlorkingExceptions(
                                    "'" + dateArg + "' isn't a valid date. Use yyyy-MM-dd, e.g. 2019-12-02.");
                        }
                        ArrayList<Task> matches = new ArrayList<>();
                        for (Task task : tasks) {
                            if (task.isOccurringOn(queryDate)) {
                                matches.add(task);
                            }
                        }
                        ui.showTasksOnDate(matches, queryDate);
                        break;
                    }
                    case TODO: {
                        String description = line.length() > 4 ? line.substring(4).trim() : "";
                        if (description.isEmpty()) {
                            throw new FlorkingExceptions("No todo description sia.");
                        }
                        addTask(tasks, new Todo(description), ui);
                        break;
                    }
                    case DEADLINE: {
                        String remainder = line.length() > 8 ? line.substring(8).trim() : "";
                        if (remainder.isEmpty()) {
                            throw new FlorkingExceptions("No deadline description sia.");
                        }
                        String[] parts = remainder.split(" /by ", 2);
                        String deadlineDescription = parts[0].trim();
                        if (deadlineDescription.isEmpty()) {
                            throw new FlorkingExceptions("No deadline description sia.");
                        }
                        if (parts.length < 2 || parts[1].trim().isEmpty()) {
                            throw new FlorkingExceptions(
                                    "Deadline needs a '/by' date/time one eh, like deadline return book /by Sunday.");
                        }
                        String by = parts[1].trim();
                        addTask(tasks, new Deadline(deadlineDescription, by), ui);
                        break;
                    }
                    case EVENT: {
                        String eventRemainder = line.length() > 5 ? line.substring(5).trim() : "";
                        if (eventRemainder.isEmpty()) {
                            throw new FlorkingExceptions("No event description sia.");
                        }
                        String[] fromParts = eventRemainder.split(" /from ", 2);
                        String eventDescription = fromParts[0].trim();
                        if (eventDescription.isEmpty()) {
                            throw new FlorkingExceptions("No event description sia.");
                        }
                        if (fromParts.length < 2 || fromParts[1].trim().isEmpty()) {
                            throw new FlorkingExceptions(
                                    "Event needs a '/from' time one eh, like event meeting /from Mon 2pm /to 4pm.");
                        }
                        String timeframe = fromParts[1].trim();
                        String[] toParts = timeframe.split(" /to ", 2);
                        String from = toParts[0].trim();
                        if (toParts.length < 2 || toParts[1].trim().isEmpty()) {
                            throw new FlorkingExceptions(
                                    "Event needs a '/to' time one eh, like event meeting /from Mon 2pm /to 4pm.");
                        }
                        String to = toParts[1].trim();
                        addTask(tasks, new Event(eventDescription, from, to), ui);
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
    private static void addTask(ArrayList<Task> tasks, Task newTask, Ui ui) throws FlorkingExceptions {
        tasks.add(newTask);
        saveOrThrow(tasks);
        ui.showTaskAdded(newTask, tasks.size());
    }

    private static void handleMark(ArrayList<Task> tasks, String line, boolean markingDone, Ui ui)
            throws FlorkingExceptions {
        int idx = parseTaskIndex(tasks, line, markingDone ? "mark" : "unmark");
        if (markingDone) {
            tasks.get(idx - 1).markAsDone();
        } else {
            tasks.get(idx - 1).markAsNotDone();
        }
        saveOrThrow(tasks);
        if (markingDone) {
            ui.showTaskMarked(tasks.get(idx - 1).toString());
        } else {
            ui.showTaskUnmarked(tasks.get(idx - 1).toString());
        }
    }

    private static void handleDelete(ArrayList<Task> tasks, String line, Ui ui) throws FlorkingExceptions {
        int idx = parseTaskIndex(tasks, line, "delete");
        Task removedTask = tasks.remove(idx - 1);
        saveOrThrow(tasks);
        ui.showTaskDeleted(removedTask, tasks.size());
    }

    private static int parseTaskIndex(ArrayList<Task> tasks, String line, String actionName)
            throws FlorkingExceptions {
        String[] parts = line.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new FlorkingExceptions("Say properly which task you want " + actionName + ".");
        }
        int idx;
        try {
            idx = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new FlorkingExceptions("Oi, '" + parts[1].trim() + "' isn't a valid task number eh.");
        }
        if (idx < 1 || idx > tasks.size()) {
            throw new FlorkingExceptions("You don't have task " + idx + " eh. You only got " + tasks.size() + " task(s).");
        }
        return idx;
    }

    private static void saveOrThrow(ArrayList<Task> tasks) throws FlorkingExceptions {
        try {
            Storage.save(tasks);
        } catch (IOException e) {
            throw new FlorkingExceptions("Cannot save to disk eh: " + e.getMessage());
        }
    }
}