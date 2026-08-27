import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class Parser {
    public static Command parseCommand(String line) {
        if (line == null || line.trim().isEmpty()) {
            return Command.UNKNOWN;
        }
        String commandWord = line.split(" ")[0].toUpperCase();
        try {
            return Command.valueOf(commandWord);
        } catch (IllegalArgumentException e) {
            return Command.UNKNOWN;
        }
    }

    public static LocalDate parseDateArg(String dateArg) throws FlorkingExceptions {
        if (dateArg == null || dateArg.trim().isEmpty()) {
            throw new FlorkingExceptions("Which date? Try: on 2019-12-02");
        }
        try {
            return LocalDate.parse(dateArg.trim());
        } catch (DateTimeParseException e) {
            throw new FlorkingExceptions(
                    "'" + dateArg.trim() + "' isn't a valid date. Use yyyy-MM-dd, e.g. 2019-12-02.");
        }
    }

    public static String parseTodoDescription(String line) throws FlorkingExceptions {
        String description = line.length() > 4 ? line.substring(4).trim() : "";
        if (description.isEmpty()) {
            throw new FlorkingExceptions("No todo description sia.");
        }
        return description;
    }

    public static String[] parseDeadlineParts(String line) throws FlorkingExceptions {
        String remainder = line.length() > 8 ? line.substring(8).trim() : "";
        if (remainder.isEmpty()) {
            throw new FlorkingExceptions("No deadline description sia.");
        }
        String[] parts = remainder.split(" /by ", 2);
        String deadlineDescription = parts[0].trim();
        if (deadlineDescription.isEmpty()) {
            throw new FlorkingExceptions("No deadline description sia.");
        }
        if (parts.length < 2 || parts[1].trim().isEmpty()) {
            throw new FlorkingExceptions(
                    "Deadline needs a '/by' date/time one eh, like deadline return book /by Sunday.");
        }
        return new String[] {deadlineDescription, parts[1].trim()};
    }

    public static String[] parseEventParts(String line) throws FlorkingExceptions {
        String eventRemainder = line.length() > 5 ? line.substring(5).trim() : "";
        if (eventRemainder.isEmpty()) {
            throw new FlorkingExceptions("No event description sia.");
        }
        String[] fromParts = eventRemainder.split(" /from ", 2);
        String eventDescription = fromParts[0].trim();
        if (eventDescription.isEmpty()) {
            throw new FlorkingExceptions("No event description sia.");
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
        return new String[] {eventDescription, from, to};
    }
}
