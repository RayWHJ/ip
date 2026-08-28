package florkofcows.task;

import java.time.LocalDate;

/**
 * Deadline task with a single by-date/time or free-text value.
 */
public class Deadline extends Task {
    protected FlorkDateTime by;

    public Deadline(String description, String by) {
        super(description);
        this.by = FlorkDateTime.parse(by);
    }

    public String getBy() {
        return by.toDisplayString();
    }

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
