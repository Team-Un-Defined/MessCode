package com.messcode.client.views.chat;

import com.messcode.transferobjects.UsersPM;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.Message;
import javafx.scene.layout.Pane;
import org.controlsfx.control.ToggleSwitch;

import javax.swing.*;
import java.beans.PropertyChangeEvent;
import java.util.ResourceBundle;

public class ChatClientController {
    public TextArea textArea;
    public TextField textField;
    public ListView<User> usersListFXML;
    public ListView<Label> messagesListFXML;
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

    private ChatClientViewModel chatVM;
    private ViewHandler vh;
    private User user;
    private UsersPM usersPM;
    private ResourceBundle bundle;

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

        textChat.addListener((observableValue, s, t1) ->{
                    Platform.runLater(()->{
                        Label label = new Label(textChat.getValue());
                        label.setMaxWidth(messagesListFXML.getWidth() - 25);
                        label.setWrapText(true);
                        messagesListFXML.getItems().add(label);
                    });
                });
        chatVM.addListener("SendInvite", this::openPrivateChat);

        user = chatVM.getCurrentUser();
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

    private void openPrivateChat(PropertyChangeEvent propertyChangeEvent) {
        usersPM = ((UsersPM) propertyChangeEvent.getNewValue());
        JPanel panel = new JPanel();

        Object[] options = {bundle.getString("yes"), bundle.getString("no")};

        int selected = JOptionPane.showOptionDialog(panel,
                bundle.getString("hey") + " " + user + ", " + bundle.getString("user") +
                        usersPM.getSender() + bundle.getString("invites_you"), bundle.getString("invite"),
                JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null,
                options, options[0]);

        if (selected == JOptionPane.NO_OPTION) {
        } else if (selected == JOptionPane.YES_OPTION) {

            openPrivateChatForSender(usersPM.getSender());
            Platform.runLater(() -> vh.openPrivateChat(usersPM.getReceiver()));
            chatVM.sendListOfPmRoomUsers(usersPM);
        }
    }

    private void openPrivateChatForSender(User user) {
        Platform.runLater(() -> vh.openPrivateChat2(user));
    }

    public void sendButton() {
        String message = textField.getText();
        chatVM.sendMessageToEveryone(new Message(message));
        textField.clear();
    }

    public void inviteToPmButton() {
        if (usersListFXML.getSelectionModel().getSelectedItems().isEmpty()) {
            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            User user = (User) usersListFXML.getSelectionModel().getSelectedItems().get(0);
            if (!user.getUsername().equals(this.user.getUsername())) {
                chatVM.sentInviteToPM(user);
            } else {
                invitePmErrorLabel.setText(bundle.getString("talk_to_yourself"));
            }
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if(actionEvent.getSource() == buttonAll)
            paneAll.toFront();
        if(actionEvent.getSource() == buttonGroup)
            paneGroup.toFront();
        if(actionEvent.getSource() == buttonPrivate)
            panePrivate.toFront();
        if(actionEvent.getSource() == buttonProfile)
            paneProfile.toFront();
    }
}
