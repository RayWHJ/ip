package florkofcows.parser;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;

import florkofcows.command.Command;
import florkofcows.exception.FlorkingExceptions;
import org.junit.jupiter.api.Test;

public class ParserTest {
    @Test
    public void parseTodoDescription_validInput_returnsDescription() throws FlorkingExceptions {
        assertEquals("borrow book", Parser.parseTodoDescription("todo borrow book"));
    }

    @Test
    public void parseTodoDescription_emptyDescription_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseTodoDescription("todo"));
    }

    @Test
    public void parseTodoDescription_whitespaceOnlyDescription_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseTodoDescription("todo   "));
    }

    @Test
    public void parseDeadlineParts_validInput_returnsDescriptionAndBy() throws FlorkingExceptions {
        String[] parts = Parser.parseDeadlineParts("deadline return book /by Sunday");
        assertArrayEquals(new String[] {"return book", "Sunday"}, parts);
    }

    @Test
    public void parseDeadlineParts_missingByClause_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseDeadlineParts("deadline return book"));
    }

    @Test
    public void parseDeadlineParts_emptyByValue_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseDeadlineParts("deadline return book /by"));
    }

    @Test
    public void parseDeadlineParts_emptyDescription_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseDeadlineParts("deadline"));
    }

    @Test
    public void parseEventParts_validInput_returnsDescriptionFromAndTo() throws FlorkingExceptions {
        String[] parts = Parser.parseEventParts("event meeting /from Mon 2pm /to 4pm");
        assertArrayEquals(new String[] {"meeting", "Mon 2pm", "4pm"}, parts);
    }

    @Test
    public void parseEventParts_missingFromClause_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseEventParts("event meeting"));
    }

    @Test
    public void parseEventParts_missingToClause_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseEventParts("event meeting /from Mon 2pm"));
    }

    @Test
    public void parseDateArg_validDate_returnsLocalDate() throws FlorkingExceptions {
        assertEquals(LocalDate.of(2019, 12, 2), Parser.parseDateArg("2019-12-02"));
    }

    @Test
    public void parseDateArg_emptyInput_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseDateArg(""));
    }

    @Test
    public void parseDateArg_invalidFormat_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parseDateArg("2/12/2019"));
    }

    @Test
    public void parse_byeCommand_returnsExitCommand() throws FlorkingExceptions {
        Command command = Parser.parse("bye");
        assertTrue(command.isExit());
    }

    @Test
    public void parse_unknownCommand_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parse("blah"));
    }

    @Test
    public void parse_emptyInput_exceptionThrown() {
        assertThrows(FlorkingExceptions.class, () -> Parser.parse(""));
    }
}
