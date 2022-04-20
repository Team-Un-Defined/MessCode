package com.messcode.client.views.chat;

import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.messages.GroupMessages;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.*;
import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
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
    public ListView<Group> groupsList;
    public ListView<Label> messagesListAll;
    public ListView<Label> messagesListPM;
    public ListView<Label> messagesListGroup;
    public Label invitePmErrorLabel;
    public Label userDisplayedName1;
    public Label userDisplayedName2;
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
    public Label groupLabel;
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
    private String paneInFront = "all";

    public void init(ChatClientViewModel chatVM, ViewHandler vh, ResourceBundle bundle) {
        this.chatVM = chatVM;
        this.vh = vh;
        this.bundle = bundle;

        refreshPublic();
        updateUserList();
        updateGroupList();

        //CHAT MESSAGES
        StringProperty textChat = new SimpleStringProperty();
        textChat.bind(chatVM.messageProperty());

        textChat.addListener((observableValue, s, t1) -> {
            Platform.runLater(() -> {
                Label label = new Label(textChat.getValue());
                label.setMaxWidth(messagesListAll.getWidth() - 25);
                label.setWrapText(true);
                messagesListAll.getItems().add(label);

                if (!paneInFront.equals("all")) {
                    InputStream reddot = getClass().getResourceAsStream("/reddot.png");
                    allButtonImage.setImage(new Image(reddot));
                }
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

//                if(!paneInFront.equals("pm")) {
//                    InputStream reddot = getClass().getResourceAsStream("/reddot.png");
//                    PMButtonImage.setImage(new Image(reddot));
//                }
            });
        });
        //  chatVM.addListener("NewPM", this::openPrivateChat);

        userDisplayedName1.setText(chatVM.getCurrentUser().getSurname() + " " + chatVM.getCurrentUser().getName());
        userDisplayedName2.setText(chatVM.getCurrentUser().getSurname() + " " + chatVM.getCurrentUser().getName());
        userNameLabel.setText(chatVM.getCurrentUser().getSurname() + " " + chatVM.getCurrentUser().getName());
        userEmailLabel.setText(chatVM.getCurrentUser().getEmail());
        userTypeLabel.setText(chatVM.getCurrentUser().getType());

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

        for (PublicMessage msg : chatVM.getCurrentUser().getUnreadMessages()) {
            InputStream reddot = getClass().getResourceAsStream("/reddot.png");
            if (msg instanceof GroupMessages)
                groupButtonImage.setImage(new Image(reddot));
            else if (msg instanceof PrivateMessage)
                PMButtonImage.setImage(new Image(reddot));
            else allButtonImage.setImage(new Image(reddot));
        }

        usersListFXML.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        inviteToPmButton();
                    }
                }
            }
        });

        groupsList.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                    if (mouseEvent.getClickCount() == 2) {
                        openGroup();
                    }
                }
            }
        });

        paneAll.toFront();
        userListPane.toFront();
        sendAllButton.setDefaultButton(true);
    }

    private void updateGroupList() {
        groupsList.setItems(chatVM.getGroups());
        groupsList.setCellFactory(lv -> new ListCell<Group>() {
            @Override
            public void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    String text = item.getName(); // get text from item
                    setText(text);
                }
            }
        });
    }

    private void updateUserList() {
        usersListFXML.setItems(chatVM.getUsersList());
        usersListFXML.setCellFactory(lv -> new ListCell<User>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    if (item.getSalt().equals(" - online")) {
                        InputStream in = getClass().getResourceAsStream("/greendot.png");
                        ImageView imageView = new ImageView(new Image(in));
                        imageView.setFitHeight(10);
                        imageView.setPreserveRatio(true);
                        this.setGraphic(imageView);
                    } else {
                        this.setGraphic(null);
                    }
                    String text = item.getName() + " " + item.getSurname(); // get text from item
                    setText(text);
                }
            }
        });
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
        if (chatVM.getReceiver() == null) {
            System.out.println("FUCK YOU");
        } else {
            System.out.println("-------------------------------------");

            String message = textFieldPM.getText();

            chatVM.sendPM(new PrivateMessage(chatVM.getCurrentUser(), chatVM.getReceiver(), message));
            textFieldPM.clear();
        }
    }

    public void sendGroup(ActionEvent actionEvent) {
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

    public void openGroup() {
        if (groupsList.getSelectionModel().getSelectedItems().isEmpty()) {
//            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            Group group = (Group) groupsList.getSelectionModel().getSelectedItems().get(0);
            chatVM.setReceiverGroup(group);
            groupLabel.setText(group.getName());
            //hele
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

    public void editMemberButton(ActionEvent actionEvent) {
        vh.openEditMember(cssUsed);
    }

    public void refreshPublic() {
        messagesListAll.getItems().clear();
        ArrayList<PublicMessage> pub = chatVM.loadPublics();
        for (PublicMessage pb : pub) {
            messagesListAll.getItems().add(new Label(pb.getTime() + " " + pb.getUsername() + ": " + pb.getMsg()));
        }
    }
}
