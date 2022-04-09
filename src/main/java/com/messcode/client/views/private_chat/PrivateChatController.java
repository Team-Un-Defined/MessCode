package com.messcode.client.views.private_chat;

import com.messcode.transferobjects.messages.PrivateMessage;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import com.messcode.transferobjects.User;
import org.controlsfx.control.ToggleSwitch;

import java.util.ResourceBundle;

public class PrivateChatController {
    public ListView<User> usersListFXML;
    public Label invitePmErrorLabel;
    public TextArea textArea;
    public TextField textField;
    public Label userDisplayedName;

    private User user;
    private PrivateChatViewModel pmVM;
    private SimpleStringProperty textChat;
    private ResourceBundle bundle;
    @FXML
    private ToggleSwitch toggleSwitch;
    public ListView<Label> messagesListFXML;

    public void init(PrivateChatViewModel privateChatVM, User user, ResourceBundle bundle) {
        this.user = user;
        this.pmVM = privateChatVM;
        this.bundle = bundle;
        usersListFXML.setItems(pmVM.getUsers());
        textChat = new SimpleStringProperty();
        textChat.bind(pmVM.messageProperty());
        usersListFXML.setCellFactory(lv -> new ListCell<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = item.getUsername(); // get text from item
                    setText(text);
                }
            }
        });
        textChat.addListener((observableValue, s, t1) ->{
            Platform.runLater(()->{
                Label label = new Label(textChat.getValue());
                label.setMaxWidth(messagesListFXML.getWidth() - 25);
                label.setWrapText(true);
                messagesListFXML.getItems().add(label);
            });
        });
        userDisplayedName.setText(bundle.getString("your_nick") + user.getUsername() + "'");
        Platform.runLater(()->toggleSwitch.getScene().getStylesheets().add("lite.css"));

        toggleSwitch.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            Boolean value = observableValue.getValue();
            if (value){
                toggleSwitch.getScene().getStylesheets().add("dark.css");
            }else {
                toggleSwitch.getScene().getStylesheets().clear();
                toggleSwitch.getScene().getStylesheets().add("lite.css");
            }
        });
    }

    public void sendButton() {
        String message = textField.getText();
        pmVM.sendMessageInPM(new PrivateMessage(user,null, message));
        textField.clear();
    }
}
