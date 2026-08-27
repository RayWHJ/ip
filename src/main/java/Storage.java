import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private final Path filePath;

    public Storage() {
        this(Path.of("data", "florkofcows.txt"));
    }

    public Storage(String filePath) {
        this(Path.of(filePath));
    }

    public Storage(Path filePath) {
        this.filePath = filePath;
    }

    public void save(ArrayList<Task> tasks) throws IOException {
        Path parent = filePath.getParent();
        if (parent != null) {
            Files.createDirectories(parent);
        }
        try (BufferedWriter writer = Files.newBufferedWriter(filePath)) {
            for (Task task : tasks) {
                writer.write(task.toSaveFormat());
                writer.newLine();
            }
        }
    }

    public ArrayList<Task> load() throws IOException {
        List<String> lines;
        try {
            lines = Files.readAllLines(filePath);
        } catch (NoSuchFileException e) {
            return new ArrayList<>();
        }

        ArrayList<Task> tasks = new ArrayList<>();
        int skippedCount = 0;
        for (String line : lines) {
            try {
                tasks.add(parseLine(line));
            } catch (IllegalArgumentException e) {
                skippedCount++;
            }
        }
        if (skippedCount > 0) {
            System.out.println("FLORKALERT! Careful ah, got " + skippedCount
                    + " corrupted line(s) in save file.");
        }
        return tasks;
    }

    private Task parseLine(String line) {
        String[] parts = line.split(" \\| ");
        if (parts.length < 3) {
            throw new IllegalArgumentException("Not enough fields: " + line);
        }
        String type = parts[0];
        boolean isDone = parts[1].equals("1");
        String description = parts[2];
        Task task;
        switch (type) {
            case "T":
                task = new Todo(description);
                break;
            case "D":
                if (parts.length < 4) {
                    throw new IllegalArgumentException("Deadline missing '/by' field: " + line);
                }
                String by = parts[3];
                task = new Deadline(description, by);
                break;
            case "E":
                if (parts.length < 5) {
                    throw new IllegalArgumentException("Event missing '/from' or '/to' field: " + line);
                }
                String from = parts[3];
                String to = parts[4];
                task = new Event(description, from, to);
                break;
            default:
                throw new IllegalArgumentException("Unknown task type: " + type);
        }

        if (isDone) {
            task.markAsDone();
        }

        return task;
    }
}