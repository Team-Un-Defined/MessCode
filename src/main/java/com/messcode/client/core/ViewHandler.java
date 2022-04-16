package com.messcode.client.core;

import com.messcode.client.views.change_password.ChangePasswordController;
import com.messcode.client.views.chat.ChatClientController;
import com.messcode.client.views.login.LoginController;
import com.messcode.client.views.new_employee.NewEmployeeController;
import com.messcode.client.views.new_group.NewGroupController;
import com.messcode.transferobjects.User;
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
import java.util.ResourceBundle;

public class ViewHandler {

    private ViewModelFactory vmf;
    private Stage stage;
    private Scene chat;
    private Scene login;
//    private Scene chatPM;
//    private Scene chatPM2;
    private Scene newEmployee;
    private Scene newGroup;
    private Scene changePassword;
    private ResourceBundle bundle;

    private UserList userList = new UserList();
    private User myUser = null;

    public ViewHandler(ViewModelFactory vmf) {
        stage = new Stage();
        this.vmf = vmf;

        User employee1 = new User("jozef", "hruska", "jozef.hruska@gmail.com", "Password123");
        User employee2 = new User("zachary", "solomon", "zachary.solomon@gmail.com", "Password123");
        User employee3 = new User("adam", "velky", "adam.velky@gmail.com", "Password123");

        userList.addUser(employee1);
        userList.addUser(employee2);
        userList.addUser(employee3);
    }

    public void start() {
        showConfirmation();
    }

    private void showConfirmation() {
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
            grid.getScene().getWindow().hide();
        });

        skButton.setOnAction((event) -> {
            this.bundle = ResourceBundle.getBundle("bundle", new Locale("sk", "SK"));
            grid.getScene().getWindow().hide();
        });

        dialog.initModality(Modality.APPLICATION_MODAL);

        stage = (Stage) dialog.getDialogPane().getScene().getWindow();
        stage.getIcons().add(new Image("icon.png"));
        stage.setTitle("MessCode");
        stage.setOnCloseRequest(windowEvent -> stage.close());
        dialog.showAndWait();
        if (bundle != null)
            openLogin();
    }

    public void openChatClientView() {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);

        if (chat == null) {
            Parent root = getRootByPath("ChatClient.fxml", loader);
            ChatClientController controller = loader.getController();
            controller.init(vmf.getChatVM(), this, bundle);
            chat = new Scene(root);
        }

        stage.setTitle("MessCode");
        stage.setScene(chat);
        stage.getIcons().add(new Image("icon.png"));
        stage.show();
        stage.setOnCloseRequest(windowEvent -> stage.close());
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

        stage.setTitle(bundle.getString("username"));
        stage.setScene(login);
        stage.getIcons().add(new Image("icon.png"));
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

        newEmployeeStage.setTitle("Add a new employee");
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

        newGroupStage.setTitle("Add a new project");
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

        changePasswordStage.setTitle("Change your password");
        changePasswordStage.setScene(changePassword);
        changePasswordStage.getIcons().add(new Image("icon.png"));
        changePasswordStage.getScene().getStylesheets().add(css);
        changePasswordStage.show();
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

    public UserList getUserList() {
        return userList;
    }

    public User getMyUser() {
        return myUser;
    }

    public void setMyUser(User myUser) {
        this.myUser = myUser;
    }
}
