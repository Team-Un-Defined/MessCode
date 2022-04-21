package com.messcode.client.core;

import com.messcode.client.views.change_password.ChangePasswordController;
import com.messcode.client.views.chat.ChatClientController;
import com.messcode.client.views.edit_member.EditMemberController;
import com.messcode.client.views.login.LoginController;
import com.messcode.client.views.new_employee.NewEmployeeController;
import com.messcode.client.views.new_group.NewGroupController;
import com.messcode.client.views.remove_group.RemoveGroupController;
import com.messcode.client.views.remove_group.RemoveGroupViewModel;
import com.messcode.client.views.remove_user.RemoveUserController;
import com.messcode.transferobjects.User;
import com.messcode.client.core.SettingsConfig;
import com.messcode.transferobjects.UserList;
import javafx.application.Platform;
import javafx.event.EventHandler;
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
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.util.Locale;
import java.util.Objects;
import java.util.ResourceBundle;

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
    private ResourceBundle bundle;

    private User myUser = null;

    public ViewHandler(ViewModelFactory vmf) {
        stage = new Stage();
        this.vmf = vmf;
    }

    public void start() {
        SettingsConfig.readConfig();
        showConfirmation();
    }

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
            stage.setOnCloseRequest(windowEvent -> stage.close());
            dialog.showAndWait();
        }
        if (bundle != null)
            openLogin();
    }


    public void changeLanguage(String language){
        if(language.equals("SK")){
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
        stage.setX(x);
        stage.setY(y);
    }

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
        stage.show();
        stage.setOnCloseRequest(windowEvent -> stage.close());
        return controller;
    }

    private void openLogin() {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        if (login == null) {
            Parent root = getRootByPath("login.fxml", loader);
            LoginController controller = loader.getController();
            controller.init(vmf.getLoginVM(), this, bundle);
            login = new Scene(root);
        }

        stage.setTitle(bundle.getString("login.up"));
        stage.setScene(login);
        stage.getIcons().add(new Image("icon.png"));
        stage.getScene().getStylesheets().add(getCssStyle());
        stage.show();
        stage.setOnCloseRequest(windowEvent -> stage.close());
    }

    public void openNewEmployee(String css) {
        Stage newEmployeeStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        if (newEmployee == null) {
            Parent root = getRootByPath("NewEmployee.fxml", loader);
            NewEmployeeController controller = loader.getController();
            controller.init(vmf.getNewEmployeeVM(), this, bundle);
            newEmployee = new Scene(root);
        }

        newEmployeeStage.setTitle(bundle.getString("new_employee.up"));
        newEmployeeStage.setScene(newEmployee);
        newEmployeeStage.getIcons().add(new Image("icon.png"));
        newEmployeeStage.getScene().getStylesheets().add(css);
        newEmployeeStage.show();
    }

    public void openNewGroup(String css) {
        Stage newGroupStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        if (newGroup == null) {
            Parent root = getRootByPath("NewGroup.fxml", loader);
            NewGroupController controller = loader.getController();
            controller.init(vmf.getNewGroupVM(), this, bundle);
            newGroup = new Scene(root);
        }

        newGroupStage.setTitle(bundle.getString("new_group.up"));
        newGroupStage.setScene(newGroup);
        newGroupStage.getIcons().add(new Image("icon.png"));
        newGroupStage.getScene().getStylesheets().add(css);
        newGroupStage.show();
    }

    public void openChangePassword(String css) {
        Stage changePasswordStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        if(changePassword == null) {
            Parent root = getRootByPath("ChangePassword.fxml", loader);
            ChangePasswordController controller = loader.getController();
            controller.init(vmf.getChangePasswordVM(), this, bundle);
            changePassword = new Scene(root);
        }

        changePasswordStage.setTitle(bundle.getString("change_pass.up"));
        changePasswordStage.setScene(changePassword);
        changePasswordStage.getIcons().add(new Image("icon.png"));
        changePasswordStage.getScene().getStylesheets().add(css);
        changePasswordStage.show();
    }

    public void openEditMember(String css) {
        Stage editMemberStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        if (editMember == null) {
            Parent root = getRootByPath("EditMember.fxml", loader);
            EditMemberController controller = loader.getController();
            controller.init(vmf.getEditMemberVM(), this, bundle);
            editMember = new Scene(root);
        }

        editMemberStage.setTitle(bundle.getString("edit_member.up"));
        editMemberStage.setScene(editMember);
        editMemberStage.getIcons().add(new Image("icon.png"));
        editMemberStage.getScene().getStylesheets().add(css);
        editMemberStage.show();
    }

    public void openRemoveUser(String css) {
        Stage removeUserStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        if (removeUser == null) {
            Parent root = getRootByPath("RemoveUser.fxml", loader);
            RemoveUserController controller = loader.getController();
            controller.init(vmf.getRemoveUserVM(), this, bundle);
            removeUser = new Scene(root);
        }

        removeUserStage.setTitle(bundle.getString("remove_user.up"));
        removeUserStage.setScene(removeUser);
        removeUserStage.getIcons().add(new Image("icon.png"));
        removeUserStage.getScene().getStylesheets().add(css);
        removeUserStage.show();
    }

    public void openRemoveGroup(String css) {
        Stage removeGroupStage = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        if (removeGroup == null) {
            Parent root = getRootByPath("RemoveGroup.fxml", loader);
            RemoveGroupController controller = loader.getController();
            controller.init(vmf.getRemoveGroupVM(), this, bundle);
            removeGroup = new Scene(root);
        }

        removeGroupStage.setTitle(bundle.getString("remove_group.up"));
        removeGroupStage.setScene(removeGroup);
        removeGroupStage.getIcons().add(new Image("icon.png"));
        removeGroupStage.getScene().getStylesheets().add(css);
        removeGroupStage.show();
    }

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


    public String getCssStyle(){
        if(Objects.equals(SettingsConfig.getConfigOf("dark_theme"), "0")){
            return "lite.css";
        }
        return  "dark.css";
    }


    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }
}
