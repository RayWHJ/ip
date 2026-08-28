package florkofcows.task;

import java.time.LocalDate;

public class Deadline extends Task {
    protected FlorkDateTime by;

    /**
     * Represents a task that must be completed by a specific date or time.
     * The deadline may be a recognized date/datetime or free-form text,
     * depending on what was parsed by {@link FlorkDateTime}.
     */
    public Deadline(String description, String by) {
        super(description);
        this.by = FlorkDateTime.parse(by);
    }

    /**
     * Returns the deadline's due date/time as a display-friendly string.
     *
     * @return the formatted "by" value.
     */
    public String getBy() {
        return by.toDisplayString();
    }

    /**
     * Returns whether this deadline's due date falls on the given date.
     *
     * @param queryDate the date to check against.
     * @return true if the deadline's date equals queryDate, false otherwise
     *         (including when the deadline has no recognized date).
     */
    @Override
    public boolean isOccurringOn(LocalDate queryDate) {
        LocalDate byDate = by.getDateOrNull();
        return byDate != null && byDate.equals(queryDate);
    }

    @Override
    public String toSaveFormat() {
        return "D | " + super.toSaveFormat() + " | " + by.toSaveFormat();
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + this.getBy() + ")";
    }
}
