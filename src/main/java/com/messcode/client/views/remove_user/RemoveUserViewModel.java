package com.messcode.client.views.remove_user;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.User;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RemoveUserViewModel {
    private MainModel mainModel;
    private ObservableList<User> usersList;
    public RemoveUserViewModel(MainModel mainModel) {
        this.usersList= FXCollections.observableArrayList() ;
        this.mainModel = mainModel;
    }

    public ObservableList<User> getUsersList() {
        return usersList;
    }
}
