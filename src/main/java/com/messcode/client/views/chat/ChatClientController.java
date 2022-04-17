package com.messcode.client.views.chat;

import com.messcode.transferobjects.messages.GroupMessages;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import org.controlsfx.control.ToggleSwitch;

import java.beans.PropertyChangeEvent;
import java.io.InputStream;
import java.util.ArrayList;
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
    public Pane groupListPane;
    public Pane userListPane;
    public Button sendPMButton;
    public Button sendGroupButton;
    public Button sendAllButton;
    public Button newGroupButton;
    public Button newEmployeeButton;
    public ImageView allButtonImage;
    public ImageView groupButtonImage;
    public ImageView PMButtonImage;

    private ChatClientViewModel chatVM;
    private ViewHandler vh;
    private PrivateMessage usersPM;
    private ResourceBundle bundle;
    private String cssUsed = "lite.css";

    public void init(ChatClientViewModel chatVM, ViewHandler vh, ResourceBundle bundle) {
        this.chatVM = chatVM;
        this.vh = vh;
        this.bundle = bundle;

        refreshPublic();
        // ONLINE LIST
        usersListFXML.setItems(chatVM.getUsersList());
        usersListFXML.setCellFactory(lv -> new ListCell<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {

                    String text = item.getName() + " " + item.getSurname() + item.getSalt(); // get text from item
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

        userDisplayedName.setText("'" + chatVM.getCurrentUser().getSurname() + " " + chatVM.getCurrentUser().getName() + "'");
        userNameLabel.setText(chatVM.getCurrentUser().getSurname() + " " + chatVM.getCurrentUser().getName());
        userEmailLabel.setText(chatVM.getCurrentUser().getEmail());
        userTypeLabel.setText(chatVM.getCurrentUser().getType());

        paneAll.toFront();
        userListPane.toFront();
        sendAllButton.setDefaultButton(true);

        Platform.runLater(() -> toggleSwitch.getScene().getStylesheets().add("lite.css"));

        toggleSwitch.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            System.out.println(observableValue.toString());
            System.out.println(aBoolean.toString());
            System.out.println(t1.toString());
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

        if (chatVM.getCurrentUser().isProjectLeader() || chatVM.getCurrentUser().isEmployee()) {
            newEmployeeButton.setVisible(false);
            newGroupButton.setVisible(false);
        }

        InputStream in = getClass().getResourceAsStream("/reddot.png");
//        groupButtonImage.setImage(new Image(in));
        for (PublicMessage msg : chatVM.getCurrentUser().getUnreadMessages()) {
            if (msg instanceof GroupMessages)
                groupButtonImage.setImage(new Image(in));
            else if (msg instanceof PrivateMessage)
                PMButtonImage.setImage(new Image(in));
            else allButtonImage.setImage(new Image(in));
        }
    }

    private void openPrivateChat(PropertyChangeEvent propertyChangeEvent) {
        usersPM = ((PrivateMessage) propertyChangeEvent.getNewValue());
        chatVM.setReceiver(usersPM.getReceiver());
        panePrivate.toFront();
        chatVM.sendListOfPmRoomUsers(usersPM);
    }

    public void sendButton() {
        System.out.println("*************************************");
        String message = textFieldAll.getText();
        chatVM.sendPublic(new PublicMessage(chatVM.getCurrentUser(), message));
        textFieldAll.clear();
    }

    public void sendPM() {
        if (chatVM.getReceiver() == null){
            System.out.println("FUCK YOU");
        }else {
        System.out.println("-------------------------------------");

        String message = textFieldPM.getText();

        chatVM.sendPM(new PrivateMessage(chatVM.getCurrentUser(), chatVM.getReceiver(), message));
        textFieldPM.clear();}
    }

    public void inviteToPmButton() {
        if (usersListFXML.getSelectionModel().getSelectedItems().isEmpty()) {
            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            User use = (User) usersListFXML.getSelectionModel().getSelectedItems().get(0);
            System.out.println(use.getEmail());
            if (!use.getSurname().equals(chatVM.getCurrentUser().getSurname()) && !use.getName().equals(chatVM.getCurrentUser().getName())) {
                System.out.println("WOTOTOFÖK");
                chatVM.setReceiver(use);
                messagesListPM.getItems().clear();
                ArrayList<PrivateMessage> priv = chatVM.loadPMs();
                for (PrivateMessage pm : priv) {
                    messagesListPM.getItems().add(new Label(pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg()));
                }

                panePrivate.toFront();
                otherUserNameLabel.setText(use.getSurname() + " " + use.getName());
            } else {
                invitePmErrorLabel.setText(bundle.getString("talk_to_yourself"));
            }
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == buttonAll) {
            refreshPublic();
            paneAll.toFront();
            userListPane.toFront();
            sendAllButton.setDefaultButton(true);
        }
        if (actionEvent.getSource() == buttonGroup) {
            paneGroup.toFront();
            groupListPane.toFront();
            sendGroupButton.setDefaultButton(true);
        }
        if (actionEvent.getSource() == buttonPrivate) {
            panePrivate.toFront();
            userListPane.toFront();
            sendPMButton.setDefaultButton(true);
        }
        if (actionEvent.getSource() == buttonProfile) {
            paneProfile.toFront();
            userListPane.toFront();
            sendGroupButton.setDefaultButton(false);
            sendAllButton.setDefaultButton(false);
            sendPMButton.setDefaultButton(false);
        }
    }

    public void changePasswordClicked() {
        vh.openChangePassword(cssUsed);
    }

    public void newEmployeeClicked() {
        vh.openNewEmployee(cssUsed);
    }

    public void newGroupClicked() {
        vh.openNewGroup(cssUsed);
    }

    public void refreshPublic() {
        messagesListAll.getItems().clear();
        ArrayList<PublicMessage> pub = chatVM.loadPublics();
        for (PublicMessage pb : pub) {
            messagesListAll.getItems().add(new Label(pb.getTime() + " " + pb.getUsername() + ": " + pb.getMsg()));
        }
    }
}
