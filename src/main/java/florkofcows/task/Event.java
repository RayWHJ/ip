package florkofcows.task;

import java.time.LocalDate;

/**
 * Event task with a start (from) and end (to) date/time or free-text timeframe.
 */
public class Event extends Task {
    protected FlorkDateTime from;
    protected FlorkDateTime to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = FlorkDateTime.parse(from);
        this.to = FlorkDateTime.parse(to);
    }

    public String getFrom() {
        return from.toDisplayString();
    }

    public String getTo() {
        return to.toDisplayString();
    }

    @Override
    public boolean isOccurringOn(LocalDate queryDate) {
        LocalDate fromDate = from.getDateOrNull();
        LocalDate toDate = to.getDateOrNull();
        if (fromDate != null && toDate != null) {
            return !queryDate.isBefore(fromDate) && !queryDate.isAfter(toDate);
        } else if (fromDate != null) {
            return fromDate.equals(queryDate);
        } else if (toDate != null) {
            return toDate.equals(queryDate);
        } else {
            return false;
        }
    }

    @Override
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + from.toSaveFormat() + " | " + to.toSaveFormat();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.getFrom() + " to: " + this.getTo() + ")";
    }
}