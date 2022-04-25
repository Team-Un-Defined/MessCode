package com.messcode.client.core;

import com.messcode.client.views.change_password.ChangePasswordController;
import com.messcode.client.views.chat.ChatClientController;
import com.messcode.client.views.edit_member.EditMemberController;
import com.messcode.client.views.edit_project_leader.EditProjectLeaderController;
import com.messcode.client.views.login.LoginController;
import com.messcode.client.views.new_employee.NewEmployeeController;
import com.messcode.client.views.new_group.NewGroupController;
import com.messcode.client.views.remove_group.RemoveGroupController;
import com.messcode.client.views.remove_user.RemoveUserController;
import com.messcode.client.views.view_profile.ViewProfileController;
import com.messcode.transferobjects.User;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * This class is used to manage all main windows and popup windows of the application.
 */
public class ViewHandler {

    private ViewModelFactory vmf;
    private Stage stage;
    private Scene chat;
    private Scene login;
    private Scene newEmployee;
    private Scene newGroup;
    private Scene changePassword;
    private Scene editMember;
    private Scene removeUser;
    private Scene removeGroup;
    private Scene editProjectLeader;
    private Scene viewProfile;
    private ResourceBundle bundle;

    private User myUser = null;

    /**
     * Creates new ViewHandler instance
     * @param vmf active instance of ViewModelFactory
     */
    public ViewHandler(ViewModelFactory vmf) {
        stage = new Stage();
        this.vmf = vmf;
    }

    /**
     * The method calls the first start window after reading the actual config
     */
    public void start() {
        SettingsConfig.readConfig();
        showConfirmation();
    }

    /**
     * The method checks if the user has selected the application interface language;
     * if the user starts the application for the first time,
     * method displays a dialog box with the localization language selection.
     * After selecting a language, an open login window handler is called.
     */
    private void showConfirmation() {
        String languageConfig = SettingsConfig.getConfigOf("language");
        if (languageConfig.equals("ENG")) {
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("en", "EN"));
        } else if (languageConfig.equals("SK")) {
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("sk", "SK"));
        } else {
            Dialog dialog = new Dialog();
            ImageView enView = new ImageView("en.gif");
            Button enButton = new Button();
            enButton.setGraphic(enView);

            ImageView skView = new ImageView("sk.gif");
            Button skButton = new Button();
            skButton.setGraphic(skView);

            GridPane grid = new GridPane();
            grid.add(skButton, 0, 0);
            grid.add(enButton, 1, 0);

            dialog.getDialogPane().setContent(grid);
            Platform.runLater(skButton::requestFocus);

            enButton.setOnAction((event) -> {
                this.bundle = ResourceBundle.getBundle("bundle", new Locale("en", "EN"));
                SettingsConfig.setConfigOf("language", "ENG");
                grid.getScene().getWindow().hide();
            });

            skButton.setOnAction((event) -> {
                this.bundle = ResourceBundle.getBundle("bundle", new Locale("sk", "SK"));
                SettingsConfig.setConfigOf("language", "SK");
                grid.getScene().getWindow().hide();
            });
            dialog.initModality(Modality.APPLICATION_MODAL);
            stage = (Stage) dialog.getDialogPane().getScene().getWindow();
            stage.getIcons().add(new Image("icon.png"));
            stage.setTitle("MessCode");
            stage.setResizable(false);
            stage.setOnCloseRequest(windowEvent -> stage.close());
            dialog.showAndWait();
        }
        if (bundle != null)
            openLogin();
    }

    /**
     * This method dynamically changes the language within the application.
     * @param language Language localize app to
     */
    public void changeLanguage(String language) {
        if (language.equals("SK")) {
            System.out.println("Bundle SK");
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("sk", "SK"));
        } else {
            System.out.println("Bundle ENG");
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("en", "EN"));
        }
        double x = stage.getX();
        double y = stage.getY();
        stage.close();
        openChatClientView().paneProfile.toFront();
        stage.setResizable(false);
        stage.setX(x);
        stage.setY(y);
    }

    /**
     * This method opens the main window with chats.
     * @return Returns client window controller
     */
    public ChatClientController openChatClientView() {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(this.bundle);

        Parent root = getRootByPath("ChatClient.fxml", loader);
        ChatClientController controller = loader.getController();
        controller.init(vmf.getChatVM(), this, bundle);
        chat = new Scene(root);

        stage.setTitle("MessCode");
        stage.setScene(chat);
        stage.getIcons().add(new Image("icon.png"));
        stage.getScene().getStylesheets().add(getCssStyle());
        stage.setOnCloseRequest(windowEvent -> stage.close());
        stage.setResizable(false);
        stage.show();
        return controller;
    }

    /**
     * This method opens login window
     */
    private void openLogin() {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("login.fxml", loader);
        LoginController controller = loader.getController();
        controller.init(vmf.getLoginVM(), this, bundle);
        login = new Scene(root);

        stage.setTitle(bundle.getString("login.up"));
        stage.setScene(login);
        stage.getIcons().add(new Image("icon.png"));
        stage.getScene().getStylesheets().add(getCssStyle());
        stage.setOnCloseRequest(windowEvent -> stage.close());
        stage.setResizable(false);
        stage.show();
    }

    /**
     *  This method opens window popup with functionality of adding a new employee
     * @param css Current interface theme
     */
    public void openNewEmployee(String css) {
        Stage newEmployeeStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("NewEmployee.fxml", loader);
        NewEmployeeController controller = loader.getController();
        controller.init(vmf.getNewEmployeeVM(), this, bundle);
        newEmployee = new Scene(root);

        newEmployeeStage.setTitle(bundle.getString("new_employee.up"));
        newEmployeeStage.setScene(newEmployee);
        newEmployeeStage.getIcons().add(new Image("icon.png"));
        newEmployeeStage.getScene().getStylesheets().add(css);
        newEmployeeStage.setResizable(false);
        newEmployeeStage.show();
    }

    /**
     *  This method opens window popup with functionality of adding a new group
     * @param css Current interface theme
     */
    public void openNewGroup(String css) {
        Stage newGroupStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("NewGroup.fxml", loader);
        NewGroupController controller = loader.getController();
        controller.init(vmf.getNewGroupVM(), this, bundle);
        newGroup = new Scene(root);

        newGroupStage.setTitle(bundle.getString("new_group.up"));
        newGroupStage.setScene(newGroup);
        newGroupStage.getIcons().add(new Image("icon.png"));
        newGroupStage.getScene().getStylesheets().add(css);
        newGroupStage.setResizable(false);
        newGroupStage.show();
    }

    /**
     *  This method opens window popup with functionality of changing user password
     * @param css Current interface theme
     */
    public void openChangePassword(String css) {
        Stage changePasswordStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("ChangePassword.fxml", loader);
        ChangePasswordController controller = loader.getController();
        controller.init(vmf.getChangePasswordVM(), this, bundle);
        changePassword = new Scene(root);

        changePasswordStage.setTitle(bundle.getString("change_pass.up"));
        changePasswordStage.setScene(changePassword);
        changePasswordStage.getIcons().add(new Image("icon.png"));
        changePasswordStage.getScene().getStylesheets().add(css);
        changePasswordStage.setResizable(false);
        changePasswordStage.show();
    }

    /**
     *  This method opens window popup with functionality of editing members to the group chat
     * @param css Current interface theme
     */
    public void openEditMember(String css) {
        Stage editMemberStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("EditMember.fxml", loader);
        EditMemberController controller = loader.getController();
        controller.init(vmf.getEditMemberVM(), this, bundle);
        editMember = new Scene(root);

        editMemberStage.setTitle(bundle.getString("edit_member.up"));
        editMemberStage.setScene(editMember);
        editMemberStage.getIcons().add(new Image("icon.png"));
        editMemberStage.getScene().getStylesheets().add(css);
        editMemberStage.setResizable(false);
        editMemberStage.show();
    }

    /**
     *  This method opens window popup with functionality of changing project leader of the group chat
     * @param css Current interface theme
     */
    public void openEditProjectLeader(String css) {
        Stage editProjectLeaderStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("EditProjectLeader.fxml", loader);
        EditProjectLeaderController controller = loader.getController();
        controller.init(vmf.getEditProjectLeaderVM(), this, bundle);
        editProjectLeader = new Scene(root);

        editProjectLeaderStage.setTitle(bundle.getString("edit_project_leader.up"));
        editProjectLeaderStage.setScene(editProjectLeader);
        editProjectLeaderStage.getIcons().add(new Image("icon.png"));
        editProjectLeaderStage.getScene().getStylesheets().add(css);
        editProjectLeaderStage.setResizable(false);
        editProjectLeaderStage.show();
    }

    /**
     *  This method opens window popup with functionality of removing user
     * @param css Current interface theme
     */
    public void openRemoveUser(String css) {
        Stage removeUserStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("RemoveUser.fxml", loader);
        RemoveUserController controller = loader.getController();
        controller.init(vmf.getRemoveUserVM(), this, bundle);
        removeUser = new Scene(root);

        removeUserStage.setTitle(bundle.getString("remove_user.up"));
        removeUserStage.setScene(removeUser);
        removeUserStage.getIcons().add(new Image("icon.png"));
        removeUserStage.getScene().getStylesheets().add(css);
        removeUserStage.setResizable(false);
        removeUserStage.show();
    }

    /**
     *  This method opens window popup with functionality of removing group chat
     * @param css Current interface theme
     */
    public void openRemoveGroup(String css) {
        Stage removeGroupStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("RemoveGroup.fxml", loader);
        RemoveGroupController controller = loader.getController();
        controller.init(vmf.getRemoveGroupVM(), this, bundle);
        removeGroup = new Scene(root);

        removeGroupStage.setTitle(bundle.getString("remove_group.up"));
        removeGroupStage.setScene(removeGroup);
        removeGroupStage.getIcons().add(new Image("icon.png"));
        removeGroupStage.getScene().getStylesheets().add(css);
        removeGroupStage.setResizable(false);
        removeGroupStage.show();
    }

    /**
     *  This method opens window popup with functionality of viewing other user's profile information
     * @param css Current interface theme
     */
    public void openViewProfile(String css) {
        Stage viewProfileStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        Parent root = getRootByPath("ViewProfile.fxml", loader);
        ViewProfileController controller = loader.getController();
        controller.init(vmf.getViewProfileVM(), this, bundle);
        viewProfile = new Scene(root);

        viewProfileStage.setTitle(bundle.getString("view_profile.up"));
        viewProfileStage.setScene(viewProfile);
        viewProfileStage.getIcons().add(new Image("icon.png"));
        viewProfileStage.getScene().getStylesheets().add(css);
        viewProfileStage.setResizable(false);
        viewProfileStage.show();
    }

    /**
     * This method finds the FXML for a window in the application files
     * @param path FXML file name
     * @param loader FXMLLoader instance
     * @return loaded file
     */
    private Parent getRootByPath(String path, FXMLLoader loader) {
        loader.setLocation(getClass().getResource(path));
        Parent root = null;
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return root;
    }

    /**
     * This method gets currently used interface style from app config
     * @return current used interface style
     */
    public String getCssStyle() {
        if (Objects.equals(SettingsConfig.getConfigOf("dark_theme"), "0")) {
            return "lite.css";
        }
        return "dark.css";
    }

    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }
}
