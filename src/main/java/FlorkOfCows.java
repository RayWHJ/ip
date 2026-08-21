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

    private static void printList(Task[] tasks, int count) {
        printLine();
        if (count == 0) {
            System.out.println(" No tasks.");
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
            System.out.println(" Task list is full. Cannot add more tasks.");
            printLine();
            return count;
        } else {
            tasks[count] = newTask;
            printLine();
            System.out.println(" Okayyy added:");
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

                if ("bye".equals(line)) {
                    printBye();
                    break;
                } else if ("list".equals(line)) {
                    printList(tasks, task_count);
                } else if (line.startsWith("mark ")) {
                    String[] parts = line.split(" ", 2);
                    try {
                        int idx = Integer.parseInt(parts[1]);
                        if (idx < 1 || idx > task_count) {
                            printLine();
                            System.out.println(" Invalid task number.");
                            printLine();
                        } else {
                            tasks[idx - 1].markAsDone();
                            printMarked(tasks[idx - 1].toString());
                        }
                    } catch (Exception e) {
                        printLine();
                        System.out.println(" Please specify a valid task number to mark.");
                        printLine();
                    }
                } else if (line.startsWith("unmark ")) {
                    String[] parts = line.split(" ", 2);
                    try {
                        int idx = Integer.parseInt(parts[1]);
                        if (idx < 1 || idx > task_count) {
                            printLine();
                            System.out.println(" Invalid task number.");
                            printLine();
                        } else {
                            tasks[idx - 1].markAsNotDone();
                            printUnmarked(tasks[idx - 1].toString());
                        }
                    } catch (Exception e) {
                        printLine();
                        System.out.println(" Please specify a valid task number to mark.");
                        printLine();
                    }
                } else if (line.startsWith("todo ")) {
                    String description = line.substring("todo ".length()).trim();
                    task_count = addTask(tasks, task_count, new Todo(description));
                } else if (line.startsWith("deadline ")) {
                    String remainder = line.substring("deadline ".length()).trim();

                    String[] parts = remainder.split(" /by ", 2);
                    String description = parts[0].trim();
                    String by = parts.length > 1 ? parts[1].trim() : "";

                    task_count = addTask(tasks, task_count, new Deadline(description, by));
                } else if (line.startsWith("event ")) {
                    String remainder = line.substring("event ".length()).trim();

                    String[] fromParts = remainder.split(" /from ", 2);;
                    String description = fromParts[0].trim();
                    String timeframe = fromParts.length > 1 ? fromParts[1].trim() : "";

                    String[] toParts = timeframe.split(" /to ", 2);
                    String from = toParts[0].trim();
                    String to = toParts.length > 1 ? toParts[1].trim() : "";

                    task_count = addTask(tasks, task_count, new Event(description, from, to));
                } else {
                    task_count = addTask(tasks, task_count, new Task(line, false));
                }
            }
        }
    }
}