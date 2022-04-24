package com.messcode.client.views.chat;

import com.messcode.client.core.SettingsConfig;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.messages.GroupMessages;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import org.controlsfx.control.ToggleSwitch;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeEvent;
import java.io.InputStream;

import static java.lang.Thread.sleep;

import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ChatClientController {

    public TextField textFieldAll;
    public TextField textFieldPM;
    public TextField textFieldGroup;
    public ListView<User> usersListFXML;
    public ListView<Group> groupsList;
    public ListView<Label> messagesListAll;
    public ListView<Label> messagesListPM;
    public ListView<Label> messagesListGroup;
    public Label invitePmErrorLabel;
    public Label userDisplayedName1;
    public Label userDisplayedName2;
    public ToggleSwitch toggleSwitch;
    public ToggleSwitch toggleSwitchLoc;
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
    public Label groupLabel;
    public Label newPasswordLabel;
    public Pane groupListPane;
    public Pane userListPane;
    public Button sendPMButton;
    public Button sendGroupButton;
    public Button sendAllButton;
    public Button newGroupButton;
    public Button newEmployeeButton;
    public Button editMemberButton;
    public Button removeGroupButton;
    public Button removeUserButton;
    public Button editProjectLeaderButton;
    public Button resetPasswordButton;

    public ImageView allButtonImage;
    public ImageView groupButtonImage;
    public ImageView PMButtonImage;
    private ChatClientViewModel chatVM;
    private ViewHandler vh;
    private PrivateMessage usersPM;
    private ResourceBundle bundle;
    private String cssUsed;
    private String paneInFront = "all";

    public void init(ChatClientViewModel chatVM, ViewHandler vh, ResourceBundle bundle) {
        this.chatVM = chatVM;
        this.vh = vh;
        this.bundle = bundle;
        cssUsed = vh.getCssStyle();

        refreshPublic();
        updateUserList();
        updateGroupList();

        chatVM.addListener("MessageForEveryone", this::displayPublic);
        chatVM.addListener("newPM", this::displayPM);
        chatVM.addListener("newGroupMessage", this::displayGroup);

        editProjectLeaderButton.setVisible(false);
        editMemberButton.setVisible(false);
        resetPasswordButton.setVisible(false);

        userDisplayedName1.setText(chatVM.getCurrentUser().getSurname() + " " + chatVM.getCurrentUser().getName());
        userDisplayedName2.setText(chatVM.getCurrentUser().getSurname() + " " + chatVM.getCurrentUser().getName());
        userNameLabel.setText(chatVM.getCurrentUser().getSurname() + " " + chatVM.getCurrentUser().getName());
        userEmailLabel.setText(chatVM.getCurrentUser().getEmail());
        userTypeLabel.setText(chatVM.getCurrentUser().getType());

        if (SettingsConfig.getConfigOf("language").equals("SK")) {
            toggleSwitchLoc.setSelected(true);
        }

        if (SettingsConfig.getConfigOf("dark_theme").equals("1")) {
            toggleSwitch.setSelected(true);
        }

        toggleSwitch.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            Boolean value = observableValue.getValue();
            toggleSwitch.getScene().getStylesheets().clear();
            if (value) {
                cssUsed = "dark.css";
                toggleSwitch.getScene().getStylesheets().add(cssUsed);
                SettingsConfig.setConfigOf("dark_theme", "1");
            } else {
                cssUsed = "lite.css";
                toggleSwitch.getScene().getStylesheets().add(cssUsed);
                SettingsConfig.setConfigOf("dark_theme", "0");
            }
        });

        toggleSwitchLoc.selectedProperty().addListener((observableValue, aBoolean, t1) -> {
            Boolean value = observableValue.getValue();
            if (value) {
                System.out.println("Language SK");
                SettingsConfig.setConfigOf("language", "SK");
                this.vh.changeLanguage("SK");
            } else {
                System.out.println("Language ENG");
                SettingsConfig.setConfigOf("language", "ENG");
                this.vh.changeLanguage("ENG");
            }
        });

        if (chatVM.getCurrentUser().isEmployee()) {
            newEmployeeButton.setVisible(false);
            newGroupButton.setVisible(false);
            removeUserButton.setVisible(false);
            removeGroupButton.setVisible(false);
            editMemberButton.setVisible(false);
            resetPasswordButton.setVisible(false);
            editProjectLeaderButton.setVisible(false);
        } else if (chatVM.getCurrentUser().isProjectLeader()) {
            newEmployeeButton.setVisible(false);
            newGroupButton.setVisible(false);
            removeUserButton.setVisible(false);
            removeGroupButton.setVisible(false);
            resetPasswordButton.setVisible(false);
            editProjectLeaderButton.setVisible(false);
        } else if (chatVM.getCurrentUser().isEmployer()) {
            sendGroupButton.setVisible(false);
            removeUserButton.setVisible(false);
            resetPasswordButton.setVisible(false);
        }

        for (PublicMessage msg : chatVM.getCurrentUser().getUnreadMessages()) {
            InputStream reddot = getClass().getResourceAsStream("/reddot.png");
            if (msg instanceof GroupMessages)
                groupButtonImage.setImage(new Image(reddot));
            else if (msg instanceof PrivateMessage)
                PMButtonImage.setImage(new Image(reddot));
            else allButtonImage.setImage(new Image(reddot));
        }

        usersListFXML.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    if (chatVM.getCurrentUser().getType().equals("superuser")) {
                        resetPasswordButton.setVisible(true);
                    }

                    inviteToPmButton();
                }
            }
        });

        groupsList.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    if (chatVM.getCurrentUser().getType().equals("superuser") || chatVM.getCurrentUser().getType().equals("employer")) {
                        editProjectLeaderButton.setVisible(true);
                        editMemberButton.setVisible(true);
                    }
                    if (chatVM.getCurrentUser().getType().equals("project_leader")) {
                        editMemberButton.setVisible(true);
                    }


                    openGroup();
                }
            }
        });

        paneAll.toFront();
        userListPane.toFront();
        sendAllButton.setDefaultButton(true);
    }

    private void updateGroupList() {
        groupsList.setItems(chatVM.getGroups());
        groupsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (item.getLeader() == null) {

                    String text = item.getName();
                    setText(text);
                    this.setTextFill(Color.RED);
                } else {
                    String text = item.getName(); // get text from item
                    setText(text);
                }
            }
        });
    }

    private void updateUserList() {
        usersListFXML.setItems(chatVM.getUsersList());
        usersListFXML.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    if (chatVM.getUnredPMs(item)){
                    if (item.getSalt().equals(" - online")) {
                        InputStream in = getClass().getResourceAsStream("/orange-green.png");
                        ImageView imageView = new ImageView(new Image(in));
                        imageView.setFitHeight(10);
                        imageView.setPreserveRatio(true);
                        this.setGraphic(imageView);
                    } else {
                     InputStream in = getClass().getResourceAsStream("/orangedotm.png");
                        ImageView imageView = new ImageView(new Image(in));
                        imageView.setFitHeight(10);
                        imageView.setPreserveRatio(true);
                        this.setGraphic(imageView);
                   
                    }
                    
                    }
                    else{
                    if (item.getSalt().equals(" - online")) {
                        InputStream in = getClass().getResourceAsStream("/greendot.png");
                        ImageView imageView = new ImageView(new Image(in));
                        imageView.setFitHeight(10);
                        imageView.setPreserveRatio(true);
                        this.setGraphic(imageView);
                    } else if (item.getSalt().equals(" - deleted")) {
                        this.setGraphic(null);
                        this.setTextFill(Color.GRAY);
                    } else {
                        this.setGraphic(null);
                    }
                    }
                    String text = item.getName() + " " + item.getSurname(); // get text from item
                    setText(text);
                }
            }
        });
    }

//    // TODO as in do we still need this? i dont think so
//    private void openPrivateChat(PropertyChangeEvent propertyChangeEvent) {
//        usersPM = ((PrivateMessage) propertyChangeEvent.getNewValue());
//        chatVM.setReceiver(usersPM.getReceiver());
//        panePrivate.toFront();
//        chatVM.sendListOfPmRoomUsers(usersPM);
//    }

    public void sendButton() {
        System.out.println("*************************************");
        String message = textFieldAll.getText();
        chatVM.sendPublic(new PublicMessage(chatVM.getCurrentUser(), message));
        textFieldAll.clear();
    }

    public void sendPM() {
        if (chatVM.getReceiver() == null) {
            System.out.println("FUCK YOU");
        } else {
            System.out.println("-------------------------------------");

            String message = textFieldPM.getText();

            chatVM.sendPM(new PrivateMessage(chatVM.getCurrentUser(), chatVM.getReceiver(), message));
            textFieldPM.clear();
        }
    }

    public void sendGroup() {
        if (chatVM.getReceiverGroup() == null) {
            System.out.println("EF YOU 2.0");
        } else {
            System.out.println("^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^");
            String message = textFieldGroup.getText();
            chatVM.sendGroup(new GroupMessages(chatVM.getCurrentUser(), message, chatVM.getReceiverGroup()));
            textFieldGroup.clear();
        }
    }

    public void inviteToPmButton() {
        if (usersListFXML.getSelectionModel().getSelectedItems().isEmpty()) {
            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            User use = (User) usersListFXML.getSelectionModel().getSelectedItems().get(0);
            System.out.println(use.getEmail());
            if (!use.getEmail().equals(chatVM.getCurrentUser().getEmail()) && !use.getEmail().equals(chatVM.getCurrentUser().getEmail())) {
               
                chatVM.setReceiver(use);
                messagesListPM.getItems().clear();
                ArrayList<PrivateMessage> priv = chatVM.loadPMs();
                for (PrivateMessage pm : priv) {
                    Label label = new Label(pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
                    label.setOnMouseClicked((evt) -> this.copyMessage(label.getText()));
                    messagesListPM.getItems().add(label);
                }
                messagesListPM.scrollTo(messagesListPM.getItems().size());

                paneInFront = "pm";
                panePrivate.toFront();
                userListPane.toFront();
                sendGroupButton.setDefaultButton(false);
                sendAllButton.setDefaultButton(false);
                sendPMButton.setDefaultButton(true);
                otherUserNameLabel.setText(use.getSurname() + " " + use.getName());
            } else {
                invitePmErrorLabel.setText(bundle.getString("talk_to_yourself"));
            }
        }
    }

    private void copyMessage(String str) {
        StringSelection stringSelection = new StringSelection(str);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    public void openGroup() {
        if (groupsList.getSelectionModel().getSelectedItems().isEmpty()) {
//            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            Group group = groupsList.getSelectionModel().getSelectedItems().get(0);
            System.out.println(group.getName());
            chatVM.setReceiverGroup(group);
            messagesListGroup.getItems().clear();
            ArrayList<GroupMessages> groupMess = chatVM.loadGroup();
            for (GroupMessages g : groupMess) {
                Label label = new Label(g.getTime() + " " + g.getUsername() + ": " + g.getMsg());
                label.setOnMouseClicked((evt) -> this.copyMessage(label.getText()));
                messagesListGroup.getItems().add(label);
            }
            messagesListGroup.scrollTo(messagesListGroup.getItems().size());
            groupLabel.setText(group.getName());
        }
    }

    public void handleClicks(ActionEvent actionEvent) {
        if (actionEvent.getSource() == buttonAll) {
            paneInFront = "all";
            refreshPublic();
            paneAll.toFront();
            userListPane.toFront();
            sendPMButton.setDefaultButton(false);
            sendGroupButton.setDefaultButton(false);
            sendAllButton.setDefaultButton(true);
            allButtonImage.setImage(null);
        }
        if (actionEvent.getSource() == buttonGroup) {
            paneInFront = "group";
            paneGroup.toFront();
            groupListPane.toFront();
            sendAllButton.setDefaultButton(false);
            sendPMButton.setDefaultButton(false);
            sendGroupButton.setDefaultButton(true);
            groupButtonImage.setImage(null);
        }
        if (actionEvent.getSource() == buttonPrivate) {
            paneInFront = "pm";
            panePrivate.toFront();
            userListPane.toFront();
            sendGroupButton.setDefaultButton(false);
            sendAllButton.setDefaultButton(false);
            sendPMButton.setDefaultButton(true);
            PMButtonImage.setImage(null);
        }
        if (actionEvent.getSource() == buttonProfile) {
            paneInFront = "profile";
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

    public void editMemberClicked() {
        vh.openEditMember(cssUsed);
    }

    public void refreshPublic() {
        messagesListAll.getItems().clear();
        ArrayList<PublicMessage> pub = chatVM.loadPublics();
        for (PublicMessage pb : pub) {
            Label label = new Label(pb.getTime() + " " + pb.getUsername() + ": " + pb.getMsg());
            label.setOnMouseClicked((evt) -> this.copyMessage(label.getText()));
            messagesListAll.getItems().add(label);
        }
        messagesListAll.scrollTo(messagesListAll.getItems().size());
    }

    public void removeGroupClicked() {
        vh.openRemoveGroup(cssUsed);
    }

    public void removeUserClicked() {
        vh.openRemoveUser(cssUsed);
    }

    public void editProjectLeaderClicked() {
        vh.openEditProjectLeader(cssUsed);
    }

    private void displayPublic(PropertyChangeEvent evt) {
        String a = (String) evt.getNewValue();
        Platform.runLater(() -> {
            System.out.println("PUB    PUB     PUB       PUB     PUB   PUB   PUB    PUB");
            Label label = new Label(a);
            label.setMaxWidth(messagesListAll.getWidth() - 25);
            label.setWrapText(true);
            label.setOnMouseClicked((event) -> this.copyMessage(label.getText()));
            messagesListAll.getItems().add(label);
            messagesListAll.scrollTo(messagesListAll.getItems().size());

            if (!paneInFront.equals("all")) {
                InputStream reddot = getClass().getResourceAsStream("/reddot.png");
                allButtonImage.setImage(new Image(reddot));
            }
        });
    }

    private void displayPM(PropertyChangeEvent evt) {
        String a = (String) evt.getNewValue();
        Platform.runLater(() -> {
            Label label = new Label(a);
            label.setMaxWidth(messagesListGroup.getWidth() - 25);
            label.setWrapText(true);
            label.setOnMouseClicked((event) -> this.copyMessage(label.getText()));
            messagesListPM.getItems().add(label);
            messagesListPM.scrollTo(messagesListPM.getItems().size());
        });
    }

    private void displayGroup(PropertyChangeEvent evt) {
        String a = (String) evt.getNewValue();
        Platform.runLater(() -> {
            Label label = new Label(a);
            label.setMaxWidth(messagesListGroup.getWidth() - 25);
            label.setWrapText(true);
            label.setOnMouseClicked((event) -> this.copyMessage(label.getText()));
            messagesListGroup.getItems().add(label);
            messagesListGroup.scrollTo(messagesListGroup.getItems().size());
        });
    }

    public void resetPassword() {
        if (usersListFXML.getSelectionModel().getSelectedItems().isEmpty()) {
            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            User use = (User) usersListFXML.getSelectionModel().getSelectedItems().get(0);
            chatVM.resetPassword(use);
        }
    }
}
