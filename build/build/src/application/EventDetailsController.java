package application;

import application.model.LogEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class EventDetailsController {

    @FXML
    private Label filenameLabel;
    @FXML
    private Label levelLabel;
    @FXML
    private Label dateLabel;
    @FXML
    private Label sourceLabel;
    @FXML
    private Label idLabel;
    @FXML
    private Label categoryLabel;
    @FXML
    private TextArea descText;

    private Stage popupStage;

    @FXML
    private void initialize() {
    }

    public void setPopupStage(Stage popupStage) {
        this.popupStage = popupStage;
    }

    public void setEvent(LogEvent event) {
        filenameLabel.setText(event.getFileName());
        levelLabel.setText(event.getLevel());
        dateLabel.setText(event.getDisplayDate());
        sourceLabel.setText(event.getSource());
        idLabel.setText(event.getId());
        categoryLabel.setText(event.getLevel());
        descText.setText(event.getDesc());
    }


    @FXML
    private void handleClose() {
    	popupStage.close();
    }
    
}
