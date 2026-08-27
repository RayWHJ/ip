import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Ui {
    private final Scanner scanner = new Scanner(System.in);

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showWelcome(String banner) {
        showLine();
        System.out.println(banner);
        System.out.println("Greetings! I'm FlorkOfCows.");
        System.out.println("What do you need?");
        showLine();
    }

    public boolean hasNextCommand() {
        return scanner.hasNextLine();
    }

    public String readCommand() {
        return scanner.nextLine().trim();
    }

    public void showBye() {
        showLine();
        System.out.println("See ya!");
        showLine();
    }

    public void showError(String message) {
        showLine();
        System.out.println(" Eh lock in!!! " + message);
        showLine();
    }

    public void showLoadingError(String message) {
        System.out.println(" Warning: couldn't load saved tasks (" + message + "). Starting fresh.");
    }

    public void showTaskList(TaskList tasks) {
        showLine();
        if (tasks.isEmpty()) {
            System.out.println(" Shiok eh! No tasks.");
        } else {
            System.out.println(" Shag sia.");
            for (int i = 0; i < tasks.size(); i++) {
                System.out.println(" " + (i + 1) + "." + tasks.get(i).toString());
            }
        }
        showLine();
    }

    public void showTaskAdded(Task newTask, int totalCount) {
        showLine();
        System.out.println(" Okayyy added!");
        System.out.println("   " + newTask.toString());
        System.out.println(" You now have " + totalCount + " tasks. Jiayous!");
        showLine();
    }

    public void showTaskDeleted(Task removedTask, int remainingCount) {
        showLine();
        System.out.println(" Cans. Deleted!");
        System.out.println("   " + removedTask.toString());
        System.out.println(" Shiok, you have " + remainingCount + " tasks left. Jiayous!");
        showLine();
    }

    public void showTaskMarked(String text) {
        showLine();
        System.out.println(" Marked it!");
        System.out.println("   " + text);
        showLine();
    }

    public void showTaskUnmarked(String text) {
        showLine();
        System.out.println(" Unmarked it!");
        System.out.println("   " + text);
        showLine();
    }

    public void showTasksOnDate(ArrayList<Task> matches, LocalDate queryDate) {
        showLine();
        if (matches.isEmpty()) {
            System.out.println(" Nothing happening on " + queryDate + ".");
        } else {
            System.out.println(" Here's what's on " + queryDate + ":");
            for (int i = 0; i < matches.size(); i++) {
                System.out.println(" " + (i + 1) + "." + matches.get(i).toString());
            }
        }
        showLine();
    }
}
