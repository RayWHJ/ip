package florkofcows.task;

import java.time.LocalDate;

/**
 * Represents a generic task with a description and a completion status.
 * Serves as the base class for {@link Todo}, {@link Deadline}, and {@link Event}.
 */
public class Task {
    protected String description;
    protected boolean isDone;

    /**
     * Creates a task with the given description, initially marked as not done.
     *
     * @param description the text describing the task.
     */
    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    /**
     * Creates a task with the given description and completion status.
     *
     * @param description the text describing the task.
     * @param isDone whether the task is already marked as done.
     */
    public Task(String description, boolean isDone) {
        this.description = description;
        this.isDone = isDone;
    }

    /**
     * Returns the task's description.
     *
     * @return the description text.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Returns whether this task is marked as done.
     *
     * @return true if the task is done, false otherwise.
     */
    public boolean isDone() {
        return isDone;
    }

    /**
     * Marks the task as done.
     */
    public void markAsDone() {
        isDone = true;
    }

    /**
     * Marks the task as not done.
     */
    public void markAsNotDone() {
        isDone = false;
    }

    /**
     * Returns whether this task occurs on the given date.
     * The base implementation always returns false, since a plain task
     * has no associated date; subclasses with dates override this.
     *
     * @param queryDate the date to check against.
     * @return true if this task occurs on queryDate, false otherwise.
     */
    public boolean isOccurringOn(LocalDate queryDate) {
        return false; // Default implementation for tasks without specific dates
    }

    /**
     * Returns a string representation of this task suitable for saving to a file.
     *
     * @return a string in the format "isDone | description".
     */
    public String toSaveFormat() {
        return (isDone ? "1" : "0") + " | " + description;
    }

    @Override
    public String toString() {
        return "[" + (isDone ? "X" : " ") + "] " + description;
    }
}

