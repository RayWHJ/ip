import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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
}