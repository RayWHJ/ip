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

    private static void printList(String tasks[], boolean[] done, int count) {
        printLine();
        if (count == 0) {
            System.out.println(" No tasks.");
        } else {
            for (int i = 0; i < count; i++) {
                String status = done[i] ? "X" : " ";
                System.out.println(" " + (i + 1) + ".[" + status + "] " + tasks[i]);
            }
        }
        printLine();
    }

    private static int addTask(String[] tasks, boolean[] done, int count, String line) {
        if (count >= tasks.length) {
            printLine();
            System.out.println(" Task list is full. Cannot add more tasks.");
            printLine();
            return count;
        } else {
            tasks[count] = line;
            done[count] = false;
            printAdded(line);
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

        String[] tasks = new String[100];
        int task_count = 0;

        boolean[] done = new boolean[100];

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
                    printList(tasks, done, task_count);
                } else if (line.startsWith("mark ")) {
                    String[] parts = line.split(" ", 2);
                    try {
                        int idx = Integer.parseInt(parts[1]);
                        if (idx < 1 || idx > task_count) {
                            printLine();
                            System.out.println(" Invalid task number.");
                            printLine();
                        } else {
                            done[idx - 1] = true;
                            String entry = "[X] " + tasks[idx - 1];
                            printMarked(entry);
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
                            done[idx - 1] = false;
                            String entry = "[ ] " + tasks[idx - 1];
                            printMarked(entry);
                        }
                    } catch (Exception e) {
                        printLine();
                        System.out.println(" Please specify a valid task number to mark.");
                        printLine();
                    }
                } else {
                    task_count = addTask(tasks, done, task_count, line);
                }
            }
        }
    }
}