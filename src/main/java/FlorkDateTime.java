import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class FlorkDateTime {
    private static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_DATETIME = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDateTime dateTime;
    private final LocalDate date;
    private final String text;

    private FlorkDateTime(LocalDateTime dateTime, LocalDate date, String text) {
        this.dateTime = dateTime;
        this.date = date;
        this.text = text;
    }

    public static FlorkDateTime parse(String input) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(input, INPUT_DATETIME);
            return new FlorkDateTime(dateTime, null, null);
        } catch (DateTimeParseException e) {

        }

        try {
            return new FlorkDateTime(null, LocalDate.parse(input, INPUT_DATE), null);
        } catch (DateTimeParseException e) {

        }
        return new FlorkDateTime(null, null, input);
    }

    public String toDisplayString() {
        if (dateTime != null) {
            return dateTime.format(DISPLAY_DATETIME);
        } else if (date != null) {
            return date.format(DISPLAY_DATE);
        } else {
            return text;
        }
    }

    public String toSaveFormat() {
        if (dateTime != null) {
            return dateTime.format(INPUT_DATETIME);
        } else if (date != null) {
            return date.format(INPUT_DATE);
        } else {
            return text;
        }
    }

    public LocalDate getDateorNull() {
        if (dateTime != null) {
            return dateTime.toLocalDate();
        } else if (date != null) {
            return date;
        } else {
            return null;
        }
    }
}
