import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FlorkOfCows {
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }

    private static void printMarked(String text) {
        printLine();
        System.out.println(" Marked it!");
        System.out.println("   " + text);
        printLine();
    }

    private static void printUnmarked(String text) {
        printLine();
        System.out.println(" Unmarked it!");
        System.out.println("   " + text);
        printLine();
    }

    private static void printBye() {
        printLine();
        System.out.println("See ya!");
        printLine();
    }

    private static void printError(String message) {
        printLine();
        System.out.println(" Eh lock in!!! " + message);
        printLine();
    }

    private static void printList(ArrayList<Task> tasks) {
        printLine();
        if (tasks.size() == 0) {
            System.out.println(" Shiok eh! No tasks.");
        } else {
            System.out.println(" Shag sia.");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i).toString());
            }
        }
        printLine();
    }

    private static void addTask(ArrayList<Task> tasks, Task newTask) throws FlorkingExceptions {
        tasks.add(newTask);
        saveOrThrow(tasks);
        printLine();
        System.out.println(" Okayyy added!");
        System.out.println("   " + newTask.toString());
        System.out.println(" You now have " + tasks.size() + " tasks. Jiayous!");
        printLine();
    }

    public static void main(String[] args) throws IOException {
        String banner = "  ______ _            _     ____   __  _____                  \n"
                + " |  ____| |          | |   / __ \\ / _|/ ____|                 \n"
                + " | |__  | | ___  _ __| | _| |  | | |_| |     _____      _____ \n"
                + " |  __| | |/ _ \\| '__| |/ / |  | |  _| |    / _ \\ \\ /\\ / / __|\n"
                + " | |    | | (_) | |  |   <| |__| | | | |___| (_) \\ V  V /\\__ \\\n"
                + " |_|    |_|\\___/|_|  |_|\\_\\\\____/|_|  \\_____\\___/ \\_/\\_/ |___/\n";

        System.out.println("____________________________________________________________");
        System.out.println(banner);
        System.out.println("Greetings! I'm FlorkOfCows.");
        System.out.println("What do you need?");
        System.out.println("____________________________________________________________");

        ArrayList<Task> tasks = Storage.load();

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (!scanner.hasNextLine()) {
                    break;
                }
                String line = scanner.nextLine().trim();

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
                            printBye();
                            return;
                        case LIST:
                            printList(tasks);
                            break;
                        case MARK:
                            handleMark(tasks, line, true);
                            break;
                        case UNMARK:
                            handleMark(tasks, line, false);
                            break;
                        case DELETE:
                            handleDelete(tasks, line);
                            break;
                        case TODO: {
                            String description = line.length() > 4 ? line.substring(4).trim() : "";
                            if (description.isEmpty()) {
                                throw new FlorkingExceptions("No todo description sia.");
                            }
                            addTask(tasks, new Todo(description));
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
                            addTask(tasks, new Deadline(deadlineDescription, by));
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
                            addTask(tasks, new Event(eventDescription, from, to));
                            break;
                        }
                        case UNKNOWN:
                        default:
                            throw new FlorkingExceptions("What you saying? I don't get sia.");
                    }
                } catch (FlorkingExceptions e) {
                    printError(e.getMessage());
                }
            }
        }
    }

    private static void handleMark(ArrayList<Task> tasks, String line, boolean markingDone)
            throws FlorkingExceptions {
        int idx = parseTaskIndex(tasks, line, markingDone ? "mark" : "unmark");
        if (markingDone) {
            tasks.get(idx - 1).markAsDone();
        } else {
            tasks.get(idx - 1).markAsNotDone();
        }

        saveOrThrow(tasks);
        if (markingDone) {
            printMarked(tasks.get(idx - 1).toString());
        } else {
            printUnmarked(tasks.get(idx - 1).toString());
        }
    }

    private static void handleDelete(ArrayList<Task> tasks, String line) throws FlorkingExceptions {
        int idx = parseTaskIndex(tasks, line, "delete");
        Task removedTask = tasks.remove(idx - 1);
        saveOrThrow(tasks);
        printLine();
        System.out.println(" Cans. Deleted!");
        System.out.println("   " + removedTask.toString());
        System.out.println(" Shiok, you have " + tasks.size() + " tasks left. Jiayous!");
        printLine();
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
        } catch (Exception e) {
            throw new FlorkingExceptions("Cannot save tasks to file. " + e.getMessage());
        }
    }
}