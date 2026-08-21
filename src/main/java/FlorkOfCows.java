import java.util.Scanner;

public class FlorkOfCows {
    private static void printLine() {
        System.out.println("____________________________________________________________");
    }

    private static void printAdded(String text) {
        printLine();
        System.out.println(" added: " + text);
        printLine();
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

    private static void printList(Task[] tasks, int count) {
        printLine();
        if (count == 0) {
            System.out.println(" Shiok eh! No tasks.");
        } else {
            for (int i = 0; i < count; i++) {
                System.out.println((i + 1) + "." + tasks[i].toString());
            }
        }
        printLine();
    }

    private static int addTask(Task[] tasks, int count, Task newTask) {
        if (count >= tasks.length) {
            printLine();
            System.out.println(" Wah shag, you already have 100 tasks.");
            printLine();
            return count;
        } else {
            tasks[count] = newTask;
            printLine();
            System.out.println(" Okayyy added!");
            System.out.println("   " + newTask.toString());
            System.out.println(" You now have " + (count + 1) + " tasks. Jiayous!");
            printLine();
            return count + 1;
        }
    }

    public static void main(String[] args) {
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

        Task[] tasks = new Task[100];
        int task_count = 0;

        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                if (!scanner.hasNextLine()) {
                    break;
                }
                String line = scanner.nextLine().trim();

                try {
                    if ("bye".equals(line)) {
                        printBye();
                        break;
                    } else if ("list".equals(line)) {
                        printList(tasks, task_count);
                    } else if (line.equals("mark") || line.startsWith("mark ")) {
                        task_count = handleMark(tasks, task_count, line, true);
                    } else if (line.equals("unmark") || line.startsWith("unmark ")) {
                        task_count = handleMark(tasks, task_count, line, false);
                    } else if (line.equals("todo") || line.startsWith("todo ")) {
                        String description = line.length() > 4 ? line.substring(4).trim() : "";
                        if (description.isEmpty()) {
                            throw new FlorkingExceptions("No todo description sia.");
                        }
                        task_count = addTask(tasks, task_count, new Todo(description));
                    } else if (line.equals("deadline") || line.startsWith("deadline ")) {
                        String remainder = line.length() > 8 ? line.substring(8).trim() : "";
                        if (remainder.isEmpty()) {
                            throw new FlorkingExceptions("No deadline description sia.");
                        }
                        String[] parts = remainder.split(" /by ", 2);
                        String description = parts[0].trim();
                        if (description.isEmpty()) {
                            throw new FlorkingExceptions("No deadline description sia.");
                        }
                        if (parts.length < 2 || parts[1].trim().isEmpty()) {
                            throw new FlorkingExceptions(
                                    "Deadline needs a '/by' date/time one eh, like deadline return book /by Sunday.");
                        }
                        String by = parts[1].trim();
                        task_count = addTask(tasks, task_count, new Deadline(description, by));
                    } else if (line.equals("event") || line.startsWith("event ")) {
                        String remainder = line.length() > 5 ? line.substring(5).trim() : "";
                        if (remainder.isEmpty()) {
                            throw new FlorkingExceptions("No event description sia.");
                        }
                        String[] fromParts = remainder.split(" /from ", 2);
                        String description = fromParts[0].trim();
                        if (description.isEmpty()) {
                            throw new FlorkingExceptions    ("No event description sia.");
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
                        task_count = addTask(tasks, task_count, new Event(description, from, to));
                    } else {
                        throw new FlorkingExceptions("What you saying? I don't get sia.");
                    }
                } catch (FlorkingExceptions e) {
                    printError(e.getMessage());
                }
            }
        }
    }
    private static int handleMark(Task[] tasks, int taskCount, String line, boolean markingDone)
            throws FlorkingExceptions {
        String[] parts = line.split(" ", 2);
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new FlorkingExceptions("Say properly which task you want " + (markingDone ? "mark" : "unmark") + ".");
        }
        int idx;
        try {
            idx = Integer.parseInt(parts[1].trim());
        } catch (NumberFormatException e) {
            throw new FlorkingExceptions("Oi, '" + parts[1].trim() + "' isn't a valid task number eh.");
        }
        if (idx < 1 || idx > taskCount) {
            throw new FlorkingExceptions("You don't have task " + idx + " oh. You only got " + taskCount + " task(s).");
        }
        if (markingDone) {
            tasks[idx - 1].markAsDone();
            printMarked(tasks[idx - 1].toString());
        } else {
            tasks[idx - 1].markAsNotDone();
            printUnmarked(tasks[idx - 1].toString());
        }
        return taskCount;
    }
}