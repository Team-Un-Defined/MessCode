package com.messcode.client.views.chat;

import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import javafx.scene.layout.Pane;
import org.controlsfx.control.ToggleSwitch;

import java.beans.PropertyChangeEvent;
import java.util.ResourceBundle;

public class ChatClientController {

    public TextArea textArea;
    public TextField textFieldAll;
    public TextField textFieldPM;
    public TextField textFieldGroup;
    public ListView<User> usersListFXML;
    public ListView<Label> messagesListAll;
    public ListView<Label> messagesListPM;
    public ListView<Label> messagesListGroup;
    public Label invitePmErrorLabel;
    public Label userDisplayedName;
    public ToggleSwitch toggleSwitch;
    public Button buttonAll;
    public Button buttonGroup;
    public Button buttonPrivate;
    public Button buttonProfile;
    public Pane paneAll;
    public Pane panePrivate;
    public Pane paneProfile;
    public Pane paneGroup;
    public Label otherUserNameLabel;
    public Label userNameLabel;
    public Label userEmailLabel;
    public Label userTypeLabel;

    private ChatClientViewModel chatVM;
    private ViewHandler vh;
    private User sender;
    private User receiver;
    private PrivateMessage usersPM;
    private ResourceBundle bundle;
    private String cssUsed;

    public void init(ChatClientViewModel chatVM, ViewHandler vh, ResourceBundle bundle) {
        this.chatVM = chatVM;
        this.vh = vh;
        this.bundle = bundle;

        // ONLINE LIST
        usersListFXML.setItems(chatVM.getUsersList());
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

        //CHAT MESSAGES
        StringProperty textChat = new SimpleStringProperty();
        textChat.bind(chatVM.messageProperty());

        textChat.addListener((observableValue, s, t1) -> {
            Platform.runLater(() -> {
                Label label = new Label(textChat.getValue());
                label.setMaxWidth(messagesListAll.getWidth() - 25);
                label.setWrapText(true);
                messagesListAll.getItems().add(label);
            });
        });

        StringProperty pmChat = new SimpleStringProperty();
        pmChat.bind(chatVM.PMProperty());

        pmChat.addListener((observableValue, s, t1) -> {
            Platform.runLater(() -> {
                Label label = new Label(pmChat.getValue());
                label.setMaxWidth(messagesListPM.getWidth() - 25);
                label.setWrapText(true);
                messagesListPM.getItems().add(label);
            });
        });
        //  chatVM.addListener("NewPM", this::openPrivateChat);

        sender = chatVM.getCurrentUser();
        userDisplayedName.setText("'" + sender.getSurname() + " " +sender.getName() + "'");
        userNameLabel.setText(sender.getSurname() + " " +sender.getName());
        userEmailLabel.setText(sender.getEmail());
        userTypeLabel.setText(sender.getType());

        Platform.runLater(() -> toggleSwitch.getScene().getStylesheets().add("lite.css"));

        toggleSwitch.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            Boolean value = observableValue.getValue();
            toggleSwitch.getScene().getStylesheets().clear();
            if (value) {
                cssUsed = "dark.css";
                toggleSwitch.getScene().getStylesheets().add(cssUsed);
            } else {
                cssUsed = "lite.css";
                toggleSwitch.getScene().getStylesheets().add(cssUsed);
            }
        });
    }

    private void openPrivateChat(PropertyChangeEvent propertyChangeEvent) {
        usersPM = ((PrivateMessage) propertyChangeEvent.getNewValue());
        this.receiver = usersPM.getReceiver();
        panePrivate.toFront();
        chatVM.sendListOfPmRoomUsers(usersPM);
    }

    public void sendButton() {
        System.out.println("*************************************");
        String message = textFieldAll.getText();
        chatVM.sendPublic(new PublicMessage(this.sender, message));
        textFieldAll.clear();
    }

    public void sendPM() {
        System.out.println("-------------------------------------");
        String message = textFieldPM.getText();
        chatVM.sendPM(new PrivateMessage(this.sender, this.receiver, message));
        textFieldPM.clear();
    }

    public void inviteToPmButton() {
        if (usersListFXML.getSelectionModel().getSelectedItems().isEmpty()) {
            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            User use = (User) usersListFXML.getSelectionModel().getSelectedItems().get(0);
            if (!use.getSurname().equals(this.sender.getSurname()) && !use.getName().equals(this.sender.getName())) {
                this.receiver = use;
                panePrivate.toFront();
                otherUserNameLabel.setText(use.getSurname() + " " + use.getName());
            } else {
                invitePmErrorLabel.setText(bundle.getString("talk_to_yourself"));
            }
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == buttonAll)
            paneAll.toFront();
        if (actionEvent.getSource() == buttonGroup)
            paneGroup.toFront();
        if (actionEvent.getSource() == buttonPrivate)
            panePrivate.toFront();
        if (actionEvent.getSource() == buttonProfile)
            paneProfile.toFront();
    }

    public void changePasswordClicked(ActionEvent actionEvent) {
        vh.openChangePassword(cssUsed);
    }

    public void newEmployeeClicked(ActionEvent actionEvent) {
        vh.openNewEmployee(cssUsed);
    }

    public void newGroupClicked(ActionEvent actionEvent) {
        vh.openNewGroup(cssUsed);
    }

    public String getCssUsed() {
        return cssUsed;
    }
}
