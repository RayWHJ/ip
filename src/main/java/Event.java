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
    public String toSaveFormat() {
        return "E | " + super.toSaveFormat() + " | " + from.toSaveFormat() + " | " + to.toSaveFormat();
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + this.getFrom() + " to: " + this.getTo() + ")";
    }
}