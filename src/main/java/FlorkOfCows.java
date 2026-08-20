import java.util.Scanner;

public class FlorkOfCows {
    public static void main(String[] args) {
        String banner = "  ______ _            _     ____   __  _____                  \n"
                + " |  ____| |          | |   / __ \\ / _|/ ____|                 \n"
                + " | |__  | | ___  _ __| | _| |  | | |_| |     _____      _____ \n"
                + " |  __| | |/ _ \\| '__| |/ / |  | |  _| |    / _ \\ \\ /\\ / / __|\n"
                + " | |    | | (_) | |  |   <| |__| | | | |___| (_) \\ V  V /\\__ \\\n"
                + " |_|    |_|\\___/|_|  |_|\\_\\\\____/|_|  \\_____\\___/ \\_/\\_/ |___/\n";

        // Print banner and simple greeting interaction per requirement
        System.out.println("____________________________________________________________");
        System.out.println(banner);
        System.out.println("Greetings! I'm FlorkOfCows.");
        System.out.println("What do you need?");
        System.out.println("____________________________________________________________");

        try (java.util.Scanner scanner = new java.util.Scanner(System.in)) {
            while (true) {
                if (!scanner.hasNextLine()) {
                    // EOF reached — exit loop
                    break;
                }
                String line = scanner.nextLine().trim();

                System.out.println("____________________________________________________________");
                System.out.println(" " + line);
                System.out.println("____________________________________________________________");

                if ("bye".equals(line)) {
                    System.out.println("See ya!");
                    System.out.println("____________________________________________________________");
                    break;
                }
            }
        }
    }
}
