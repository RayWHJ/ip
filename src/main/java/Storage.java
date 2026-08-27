import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final Path DATA_FOLDER = Path.of("data");
    private static final Path DATA_FILE = DATA_FOLDER.resolve("florkofcows.txt");

    public static void save(ArrayList<Task> tasks) throws IOException {
        Files.createDirectories(DATA_FOLDER);
        try (BufferedWriter writer = Files.newBufferedWriter(DATA_FILE)) {
            for (Task task : tasks) {
                writer.write(task.toSaveFormat());
                writer.newLine();
            }
        }
    }

    public static ArrayList<Task> load() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        List<String> lines = Files.readAllLines(DATA_FILE);
        for (String line : lines) {
            String[] parts = line.split(" \\| ");
            String type = parts[0];
            boolean isDone = parts[1].equals("1");
            String description = parts[2];
            Task task;
            switch (type) {
                case "T":
                    task = new Todo(description);
                    break;
                case "D":
                    String by = parts[3];
                    task = new Deadline(description, by);
                    break;
                case "E":
                    String from = parts[3];
                    String to = parts[4];
                    task = new Event(description, from, to);
                    break;
                default:
                    throw new IOException("Unknown task type: " + type);
            }
            if (isDone) {
                task.markAsDone();
            }
            tasks.add(task);
        }
        return tasks;
    }
}