package florkofcows.task;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Small immutable value object encapsulating parsed date/time or free-text.
 *
 * Offers parsing helpers and display/save formatting used by Task subclasses.
 */
public class FlorkDateTime {
    private static final DateTimeFormatter INPUT_DATETIME = DateTimeFormatter.ofPattern("yyyy-MM-dd HHmm");
    private static final DateTimeFormatter INPUT_DATE = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DISPLAY_DATETIME = DateTimeFormatter.ofPattern("MMM d yyyy, h:mma");
    private static final DateTimeFormatter DISPLAY_DATE = DateTimeFormatter.ofPattern("MMM d yyyy");

    private final LocalDateTime dateTime;
    private final LocalDate date;
    private final String text;

    /**
     * Represents a date/time value that may be a recognized {@link LocalDateTime},
     * a recognized {@link LocalDate}, or free-form text, depending on what the
     * user typed. Falls back to storing the raw text so unrecognized input
     * (e.g. "no idea :-p") is preserved rather than rejected.
     */
    private FlorkDateTime(LocalDateTime dateTime, LocalDate date, String text) {
        this.dateTime = dateTime;
        this.date = date;
        this.text = text;
    }

    /**
     * Parses the given input into a FlorkDateTime, trying a full date/time
     * format first, then a date-only format, and finally falling back to
     * storing the input as plain text if neither matches.
     *
     * @param input the raw text to parse.
     * @return a FlorkDateTime representing the parsed value or the original text.
     */
    public static FlorkDateTime parse(String input) {
        try {
            LocalDateTime dateTime = LocalDateTime.parse(input, INPUT_DATETIME);
            return new FlorkDateTime(dateTime, null, null);
        } catch (DateTimeParseException e) {
            // Not a datetime — fall through and try parsing as a date instead.
        }

        try {
            return new FlorkDateTime(null, LocalDate.parse(input, INPUT_DATE), null);
        } catch (DateTimeParseException e) {
            // Not a date either — fall through and treat input as plain text.
        }
        return new FlorkDateTime(null, null, input);
    }

    /**
     * Returns a display-friendly string for this value: a formatted date/time
     * or date if one was recognized, or the original text otherwise.
     *
     * @return the display string.
     */
    public String toDisplayString() {
        if (dateTime != null) {
            return dateTime.format(DISPLAY_DATETIME);
        } else if (date != null) {
            return date.format(DISPLAY_DATE);
        } else {
            return text;
        }
    }

    /**
     * Returns a representation of this value suitable for saving to disk,
     * re-emitted in the same format it was originally parsed from so it can
     * be parsed identically again on load.
     *
     * @return the save-format string.
     */
    public String toSaveFormat() {
        if (dateTime != null) {
            return dateTime.format(INPUT_DATETIME);
        } else if (date != null) {
            return date.format(INPUT_DATE);
        } else {
            return text;
        }
    }

    /**
     * Returns the underlying date if this value was recognized as a date or
     * datetime, or null if it is free-form text with no associated date.
     *
     * @return the underlying LocalDate, or null.
     */
    public LocalDate getDateOrNull() {
        if (dateTime != null) {
            return dateTime.toLocalDate();
        } else if (date != null) {
            return date;
        } else {
            return null;
        }
    }
}
