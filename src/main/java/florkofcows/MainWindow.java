package florkofcows;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
/**
 * Controller for the main GUI.
 */
public class MainWindow extends AnchorPane {
    @FXML
    private ScrollPane scrollPane;
    @FXML
    private VBox dialogContainer;
    @FXML
    private TextField userInput;
    @FXML
    private Button sendButton;

    private FlorkOfCows florkOfCows;

    private Image userImage = new Image(this.getClass().getResourceAsStream("/images/florktheuser.jpg"));
    private Image florkOfCowsImage = new Image(this.getClass().getResourceAsStream("/images/florkofcowsChat.png"));

    /**
     * Initializes the controller class. This method is automatically called after the FXML file has been loaded.
     * It sets up the scroll pane to always scroll to the bottom when new messages are added.
     * It also displays a welcome message.
     */
    @FXML
    public void initialize() {
        scrollPane.vvalueProperty().bind(dialogContainer.heightProperty());
        String welcomeMessage = String.join(System.lineSeparator(),
                "Wassup! Got something on your mind today?",
                "",
                "Here are some useful commands!",
                "list - View tasks",
                "todo TASK",
                "deadline TASK /by DATE",
                "event TASK /from DATE /at DATE",
                "mark/unmark TASK",
                "delete INDEX",
                "find KEYWORD",
                "on DATE",
                "tag INDEX TAGNAME",
                "bye",
                "date format - yyyy-MM-dd");
        dialogContainer.getChildren().add(DialogBox.getFlorkOfCowsDialog(welcomeMessage, florkOfCowsImage));
    }

    /** Injects the FlorkOfCows instance */
    public void setFlorkOfCows(FlorkOfCows foc) {
        florkOfCows = foc;
    }

    /**
     * Creates two dialog boxes, one echoing user input and the other containing FlorkOfCows's reply and then appends
     * them to the dialog container. Clears the user input after processing.
     */
    @FXML
    private void handleUserInput() {
        String input = userInput.getText();
        String response = florkOfCows.getResponse(input);
        dialogContainer.getChildren().add(DialogBox.getUserDialog(input, userImage));

        DialogBox reply = DialogBox.getFlorkOfCowsDialog(response, florkOfCowsImage);
        if (florkOfCows.isLastCommandError()) {
            reply.setErrorBubble();
        }
        dialogContainer.getChildren().add(reply);
        userInput.clear();
    }
}
