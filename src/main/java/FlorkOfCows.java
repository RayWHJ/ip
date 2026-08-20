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

    private static void printBye() {
        printLine();
        System.out.println("See ya!");
        printLine();
    }

    private static void printList(String tasks[], int count) {
        printLine();
        if (count == 0) {
            System.out.println(" No tasks.");
        } else {
            for (int i = 0; i < count; i++) {
                System.out.println(" " + (i + 1) + ". " + tasks[i]);
            }
        }
        printLine();
    }

    private static int addTask(String[] tasks, int count, String line) {
        if (count >= tasks.length) {
            printLine();
            System.out.println(" Task list is full. Cannot add more tasks.");
            printLine();
            return count;
        } else {
            tasks[count] = line;
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
                } else {
                    task_count = addTask(tasks, task_count, line);
                }
            }
        }
    }
}