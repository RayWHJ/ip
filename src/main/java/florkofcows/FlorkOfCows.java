package florkofcows;

import florkofcows.command.Command;
import florkofcows.exception.FlorkingExceptions;
import florkofcows.parser.Parser;
import florkofcows.storage.Storage;
import florkofcows.task.TaskList;
import florkofcows.ui.Ui;
import java.io.IOException;

public class FlorkOfCows {
    private final Ui ui;
    private final Storage storage;
    private TaskList tasks;

    public FlorkOfCows() {
        this.ui = new Ui();
        this.storage = new Storage();
    }

    public static void main(String[] args) {
        new FlorkOfCows().run();
    }

    public void run() {
        String banner = "  ______ _            _     ____   __  _____                  \n"
                + " |  ____| |          | |   / __ \\ / _|/ ____|                 \n"
                + " | |__  | | ___  _ __| | _| |  | | |_| |     _____      _____ \n"
                + " |  __| | |/ _ \\| '__| |/ / |  | |  _| |    / _ \\ \\ /\\ / / __|\n"
                + " | |    | | (_) | |  |   <| |__| | | | |___| (_) \\ V  V /\\__ \\\n"
                + " |_|    |_|\\___/|_|  |_|\\_\\____/|_|  \\_____\\___/ \\_/\\_/ |___/\n";

        ui.showWelcome(banner);

        try {
            tasks = new TaskList(storage.load());
        } catch (IOException e) {
            tasks = new TaskList();
            ui.showLoadingError(e.getMessage());
        }

        boolean isExit = false;
        while (!isExit && ui.hasNextCommand()) {
            try {
                String fullCommand = ui.readCommand();
                if (fullCommand.isEmpty()) {
                    continue;
                }
                Command command = Parser.parse(fullCommand);
                command.execute(tasks, ui, storage);
                isExit = command.isExit();
            } catch (FlorkingExceptions e) {
                ui.showError(e.getMessage());
            }
        }
    }
}