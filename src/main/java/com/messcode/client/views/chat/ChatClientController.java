package com.messcode.client.views.chat;

import com.messcode.client.core.SettingsConfig;
import com.messcode.client.core.ViewHandler;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.stage.WindowEvent;
import org.controlsfx.control.ToggleSwitch;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.beans.PropertyChangeEvent;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.ResourceBundle;

/**
 *
 */
public class ChatClientController {

    public TextField textFieldAll;
    public TextField textFieldPM;
    public TextField textFieldGroup;
    public ListView<User> usersListFXML;
    public ListView<Group> groupsList;
    public Button viewProfileButton;
    @FXML
    private ColorPicker colorPicker;
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

    /**
     * @param chatVM
     * @param vh
     * @param bundle
     */
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

        Platform.runLater(() -> sendAllButton.getScene().getWindow().setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent t) {
                System.out.println("nyeheheeee");
                // perhaps
                Platform.exit();
                System.exit(0);
            }
        }));

        editProjectLeaderButton.setVisible(false);
        editMemberButton.setVisible(false);
        resetPasswordButton.setVisible(false);
        viewProfileButton.setVisible(false);

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
            colorPicker.setValue(new Color(0, 0, 0, 1));
        } else {
            colorPicker.setValue(new Color(1, 1, 1, 1));
        }
        if (!SettingsConfig.getConfigOf("message_color_r").equals("n")) {
            colorPicker.setValue(new Color(
                    Double.parseDouble(SettingsConfig.getConfigOf("message_color_r")),
                    Double.parseDouble(SettingsConfig.getConfigOf("message_color_g")),
                    Double.parseDouble(SettingsConfig.getConfigOf("message_color_b")),
                    1));
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
                    inviteToPmButton();
                }
            }
        });

        groupsList.setOnMouseClicked(mouseEvent -> {
            if (mouseEvent.getButton().equals(MouseButton.PRIMARY)) {
                if (mouseEvent.getClickCount() == 2) {
                    openGroup();
                }
            }
        });

        paneAll.toFront();
        userListPane.toFront();
        sendAllButton.setDefaultButton(true);
    }

    /**
     *
     */
    private void updateGroupList() {
        groupsList.setItems(chatVM.getGroups());
        groupsList.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(Group item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else if (chatVM.getUnredGMs(item)) {

                    InputStream in = getClass().getResourceAsStream("/orangedotm.png");
                    ImageView imageView = new ImageView(new Image(in));
                    imageView.setFitHeight(10);
                    imageView.setPreserveRatio(true);
                    this.setGraphic(imageView);
                    String text = item.getName(); // get text from item
                    setText(text);

                } else {
                    String text = item.getName(); // get text from item
                    setText(text);
                }

            }

        });
    }

    /**
     *
     */
    private void updateUserList() {
        usersListFXML.setItems(chatVM.getUsersList());
        usersListFXML.setCellFactory(lv -> new ListCell<>() {
            @Override
            public void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if (empty) {
                    setText(null);
                } else {
                    if (chatVM.getUnredPMs(item)) {
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
                    } else {
                        if (item.getSalt().equals(" - online")) {
                            InputStream in = getClass().getResourceAsStream("/greendot.png");
                            ImageView imageView = new ImageView(new Image(in));
                            imageView.setFitHeight(10);
                            imageView.setPreserveRatio(true);
                            this.setGraphic(imageView);
                            String text = item.getName() + " " + item.getSurname(); // get text from item
                            setText(text);
                        } else if (item.getSalt().equals(" - deleted")) {
                            this.setGraphic(null);
                            String text = item.getName() + " " + item.getSurname(); // get text from item
                            setText(text);
                            this.setTextFill(Color.GRAY);
                        } else {
                            this.setGraphic(null);
                            String text = item.getName() + " " + item.getSurname(); // get text from item
                            setText(text);
                        }
                    }
                    String text = item.getName() + " " + item.getSurname(); // get text from item
                    setText(text);
                }
            }
        });
    }

    /**
     *
     */
    public void sendButton() {
        System.out.println("*************************************");
        String message = textFieldAll.getText();
        chatVM.sendPublic(new PublicMessage(chatVM.getCurrentUser(), message));
        textFieldAll.clear();
    }

    /**
     *
     */
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

    /**
     *
     */
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

    /**
     *
     */
    public void inviteToPmButton() {
        messagesListPM.setCellFactory(list -> {
            ListCell<Label> cell = new ListCell<>() {
                @Override
                protected void updateItem(Label item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setStyle(null);
                        setText(null);
                    } else {
                        String a = "-fx-background-color: " + SettingsConfig.getConfigOf("message_color") +
                                "; -fx-text-fill: " + SettingsConfig.getConfigOf("text_color");
                        setStyle(a);
                        setText(item.getText());
                    }
                }
            };
            return cell;
        });

        if (usersListFXML.getSelectionModel().getSelectedItems().isEmpty()) {
            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            if (chatVM.getCurrentUser().getType().equals("superuser")) {
                resetPasswordButton.setVisible(true);
            }
            if (usersListFXML.getSelectionModel().getSelectedItems().get(0).getSalt().equals(" - deleted")) {
                viewProfileButton.setVisible(false);
                sendPMButton.setDisable(true);
                resetPasswordButton.setDisable(true);
            } else {
                viewProfileButton.setVisible(true);
                sendPMButton.setDisable(false);
            }

            User use = usersListFXML.getSelectionModel().getSelectedItems().get(0);
            System.out.println(use.getEmail());
            if (!use.getEmail().equals(chatVM.getCurrentUser().getEmail()) && !use.getEmail().equals(chatVM.getCurrentUser().getEmail())) {
                updateUserList();
                chatVM.setReceiver(use);
                updateUserList();
                messagesListPM.getItems().clear();
                ArrayList<PrivateMessage> priv = chatVM.loadPMs();
                for (PrivateMessage pm : priv) {
                    Label label = new Label(pm.getTime() + " " + pm.getUsername() + ": " + pm.getMsg());
                    label.setMaxWidth(messagesListPM.getPrefWidth() - 30);
                    label.setWrapText(true);
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

    /**
     * @param str
     */
    private void copyMessage(String str) {
        StringSelection stringSelection = new StringSelection(str);
        Clipboard clpbrd = Toolkit.getDefaultToolkit().getSystemClipboard();
        clpbrd.setContents(stringSelection, null);
    }

    /**
     *
     */
    public void openGroup() {
        messagesListGroup.setCellFactory(list -> {
            ListCell<Label> cell = new ListCell<>() {
                @Override
                protected void updateItem(Label item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setStyle(null);
                        setText(null);
                    } else {
                        String a = "-fx-background-color: " + SettingsConfig.getConfigOf("message_color") +
                                "; -fx-text-fill: " + SettingsConfig.getConfigOf("text_color");
                        setStyle(a);
                        setText(item.getText());
                    }

                }
            };
            return cell;
        });


        if (groupsList.getSelectionModel().getSelectedItems().isEmpty()) {
        } else {
            if (chatVM.getCurrentUser().getType().equals("superuser") || chatVM.getCurrentUser().getType().equals("employer")) {
                editProjectLeaderButton.setVisible(true);
                editMemberButton.setVisible(true);
            }

            if (chatVM.getCurrentUser().getType().equals("project_leader")) {
                editMemberButton.setVisible(true);

            }


            Group group = groupsList.getSelectionModel().getSelectedItems().get(0);

            if (groupsList.getSelectionModel().getSelectedItems().get(0).getLeader() == null
                    || !group.isMember(chatVM.getCurrentUser())) {
                sendGroupButton.setDisable(true);
                editProjectLeaderButton.setDisable(true);
                editMemberButton.setDisable(true);
            } else {
                sendGroupButton.setDisable(false);
                editProjectLeaderButton.setDisable(false);
                editMemberButton.setDisable(false);
            }

            System.out.println(group.getName());
            chatVM.setReceiverGroup(group);
            updateGroupList();
            messagesListGroup.getItems().clear();
            ArrayList<GroupMessages> groupMess = chatVM.loadGroup();
            for (GroupMessages g : groupMess) {
                Label label = new Label(g.getTime() + " " + g.getUsername() + ": " + g.getMsg());
                label.setMaxWidth(messagesListGroup.getPrefWidth() - 30);
                label.setWrapText(true);
                label.setOnMouseClicked((evt) -> this.copyMessage(label.getText()));
                messagesListGroup.getItems().add(label);
            }
            messagesListGroup.scrollTo(messagesListGroup.getItems().size());
            groupLabel.setText(group.getName());
        }
    }

    /**
     * @param actionEvent
     */
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

    /**
     *
     */
    public void changePasswordClicked() {
        vh.openChangePassword(cssUsed);
    }

    /**
     *
     */
    public void newEmployeeClicked() {
        vh.openNewEmployee(cssUsed);
    }

    /**
     *
     */
    public void newGroupClicked() {
        vh.openNewGroup(cssUsed);
    }

    /**
     *
     */
    public void editMemberClicked() {
        vh.openEditMember(cssUsed);
    }

    /**
     *
     */
    public void refreshPublic() {
        messagesListAll.setCellFactory(list -> {
            ListCell<Label> cell = new ListCell<>() {
                @Override
                protected void updateItem(Label item, boolean empty) {
                    super.updateItem(item, empty);
                    if (empty || item == null) {
                        setGraphic(null);
                        setStyle(null);
                        setText(null);
                    } else {
                        String a = "-fx-background-color: " + SettingsConfig.getConfigOf("message_color") +
                                "; -fx-text-fill: " + SettingsConfig.getConfigOf("text_color");
                        setStyle(a);
                        setText(item.getText());
                    }
                }
            };
            return cell;
        });

        messagesListAll.getItems().clear();
        ArrayList<PublicMessage> pub = chatVM.loadPublics();
        for (PublicMessage pb : pub) {
            Label label = new Label(pb.getTime() + " " + pb.getUsername() + ": " + pb.getMsg());
            System.out.println(messagesListAll.getPrefWidth());
            label.setMaxWidth(messagesListAll.getPrefWidth() - 30);
            label.setWrapText(true);
            label.setOnMouseClicked((evt) -> this.copyMessage(label.getText()));
            messagesListAll.getItems().add(label);
        }
        messagesListAll.scrollTo(messagesListAll.getItems().size());
    }

    /**
     *
     */
    public void removeGroupClicked() {
        vh.openRemoveGroup(cssUsed);
    }

    /**
     *
     */
    public void removeUserClicked() {
        vh.openRemoveUser(cssUsed);
    }

    /**
     *
     */
    public void editProjectLeaderClicked() {
        vh.openEditProjectLeader(cssUsed);
    }

    /**
     * @param actionEvent
     */
    public void viewProfileClicked(ActionEvent actionEvent) {
        vh.openViewProfile(cssUsed);
    }

    /**
     * @param evt
     */
    private void displayPublic(PropertyChangeEvent evt) {
        String a = (String) evt.getNewValue();
        Platform.runLater(() -> {
            System.out.println("PUB    PUB     PUB       PUB     PUB   PUB   PUB    PUB");
            Label label = new Label(a);
            label.setMaxWidth(messagesListAll.getPrefWidth() - 30);
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

    /**
     * @param evt
     */
    private void displayPM(PropertyChangeEvent evt) {
        String a = (String) evt.getNewValue();
        System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
        Platform.runLater(() -> {
            System.out.println("AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA");
            Label label = new Label(a);
            label.setMaxWidth(messagesListGroup.getPrefWidth() - 30);
            label.setWrapText(true);
            label.setOnMouseClicked((event) -> this.copyMessage(label.getText()));
            messagesListPM.getItems().add(label);
            messagesListPM.scrollTo(messagesListPM.getItems().size());
        });
    }

    /**
     * @param evt
     */
    private void displayGroup(PropertyChangeEvent evt) {
        if (evt.getNewValue() instanceof String) {
            System.out.println("WOW?? ");
            String a = (String) evt.getNewValue();
            Platform.runLater(() -> {
                Label label = new Label(a);
                label.setMaxWidth(messagesListGroup.getWidth() - 25);
                label.setWrapText(true);
                label.setOnMouseClicked((event) -> this.copyMessage(label.getText()));
                messagesListGroup.getItems().add(label);
                messagesListGroup.scrollTo(messagesListGroup.getItems().size());
            });
        } else {
            System.out.println("WTHIS SHOULD BE RUNNING? ");
            InputStream reddot = getClass().getResourceAsStream("/reddot.png");

            groupButtonImage.setImage(new Image(reddot));

            updateGroupList();
        }
    }

    /**
     *
     */
    public void resetPassword() {
        if (usersListFXML.getSelectionModel().getSelectedItems().isEmpty()) {
            invitePmErrorLabel.setText(bundle.getString("select_user"));
        } else {
            User use = (User) usersListFXML.getSelectionModel().getSelectedItems().get(0);
            chatVM.resetPassword(use);
        }
    }

    /**
     * @param event
     */
    public void changeColor(ActionEvent event) {
        Color color = colorPicker.getValue();
        String webFormat = String.format("#%02x%02x%02x",
                (int) (255 * color.getRed()),
                (int) (255 * color.getGreen()),
                (int) (255 * color.getBlue()));
        SettingsConfig.setConfigOf("message_color_r", String.valueOf(color.getRed()));
        SettingsConfig.setConfigOf("message_color_g", String.valueOf(color.getGreen()));
        SettingsConfig.setConfigOf("message_color_b", String.valueOf(color.getBlue()));
        SettingsConfig.setConfigOf("message_color_a", "1");
        SettingsConfig.setConfigOf("message_color", webFormat);
        if ((int) (255 * color.getRed()) * 0.299 + (int) (255 * color.getGreen()) * 0.587 + (int) (255 * color.getBlue()) * 0.114 > 140) {
            SettingsConfig.setConfigOf("text_color", "black");
        } else {
            SettingsConfig.setConfigOf("text_color", "white");
        }
    }
}
