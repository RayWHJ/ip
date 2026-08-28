package florkofcows.parser;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;

import florkofcows.command.Command;
import florkofcows.command.CommandType;
import florkofcows.exception.FlorkingExceptions;

public class Parser {
    public static Command parse(String fullCommand) throws FlorkingExceptions {
        if (fullCommand == null || fullCommand.trim().isEmpty()) {
            throw new FlorkingExceptions("What you saying? I don't get sia.");
        }

        String trimmed = fullCommand.trim();
        String[] words = trimmed.split(" ", 2);
        String commandWord = words[0].toUpperCase();
        CommandType commandType;
        try {
            commandType = CommandType.valueOf(commandWord);
        } catch (IllegalArgumentException e) {
            throw new FlorkingExceptions("What you saying? I don't get sia.");
        }

        switch (commandType) {
        case BYE:
            return new Command.ExitCommand();
        case LIST:
            return new Command.ListCommand();
        case ON:
            String dateArg = words.length > 1 ? words[1].trim() : "";
            return new Command.OnDateCommand(parseDateArg(dateArg));
        case TODO:
            return new Command.AddTodoCommand(parseTodoDescription(trimmed));
        case DEADLINE:
            String[] deadlineParts = parseDeadlineParts(trimmed);
            return new Command.AddDeadlineCommand(deadlineParts[0], deadlineParts[1]);
        case EVENT:
            String[] eventParts = parseEventParts(trimmed);
            return new Command.AddEventCommand(eventParts[0], eventParts[1], eventParts[2]);
        case MARK:
            return new Command.MarkCommand(parseIndexArg(words, "mark"), true);
        case UNMARK:
            return new Command.MarkCommand(parseIndexArg(words, "unmark"), false);
        case DELETE:
            return new Command.DeleteCommand(parseIndexArg(words, "delete"));
        case FIND:
            return new Command.FindCommand(parseFindKeyword(trimmed));
        default:
            throw new FlorkingExceptions("What you saying? I don't get sia.");
        }
    }

    private static int parseIndexArg(String[] words, String actionName) throws FlorkingExceptions {
        String argument = words.length > 1 ? words[1].trim() : "";
        if (argument.isEmpty()) {
            throw new FlorkingExceptions("Say properly which task you want " + actionName + ".");
        }
        try {
            int index = Integer.parseInt(argument);
            return index;
        } catch (NumberFormatException e) {
            throw new FlorkingExceptions("Oi, '" + argument + "' isn't a valid task number eh.");
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

    public static String parseFindKeyword(String line) throws FlorkingExceptions {
        String keyword = line.length() > 4 ? line.substring(4).trim() : "";
        if (keyword.isEmpty()) {
            throw new FlorkingExceptions("What you want me find? Give me a keyword sia.");
        }
        return keyword;
    }
}
