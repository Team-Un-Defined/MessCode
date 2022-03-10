package com.messcode.client.core;

import com.messcode.client.views.chat.ChatClientController;
import com.messcode.client.views.login.LoginController;
import com.messcode.client.views.private_chat.PrivateChatController;
import com.messcode.transferobjects.User;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Locale;
import java.util.Optional;
import java.util.ResourceBundle;

public class ViewHandler {
    private ViewModelFactory vmf;
    private Stage stage;
    private Scene chat;
    private Scene login;
    private Scene chatPM;
    private Scene chatPM2;
    private ResourceBundle bundle;

    public ViewHandler(ViewModelFactory vmf) {
        stage = new Stage();
        this.vmf = vmf;
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
        dialog.showAndWait();
        openLogin();
    }

    public void openChatClientView() {
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);
        if (chat == null) {
            Parent root = getRootByPath("ChatClient.fxml", loader);
            //root.setStyle(".root{-fx-background-color: red;}");
            ChatClientController controller = loader.getController();
            controller.init(vmf.getChatVM(), this, bundle);
            chat = new Scene(root);

        }
        stage.setTitle("MessCode");
        stage.setScene(chat);
        stage.getIcons().add(new Image("icon.png"));
        stage.show();
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
    }

    public void openPrivateChat(User user) {
        Stage stage2 = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);
        if (chatPM == null) {
            Parent root = getRootByPath("PrivateChat.fxml",
                    loader);
            PrivateChatController controller = loader.getController();
            controller.init(vmf.getPrivateChatVM(), user, bundle);
            chatPM = new Scene(root);
        }
        stage2.setTitle("PM");
        stage2.setScene(chatPM);
        stage2.getIcons().add(new Image("icon.png"));
        stage2.show();
    }

    public void openPrivateChat2(User user) {
        Stage stage3 = new Stage();
        FXMLLoader loader = new FXMLLoader();
        loader.setResources(bundle);
        if (chatPM2 == null) {
            Parent root = getRootByPath("PrivateChat.fxml",
                    loader);
            PrivateChatController controller = loader.getController();
            controller.init(vmf.getPrivateChatVM(), user, bundle);
            chatPM2 = new Scene(root);
        }
        stage3.setTitle("PM");
        stage3.setScene(chatPM2);
        stage3.getIcons().add(new Image("icon.png"));
        stage3.show();
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
}
