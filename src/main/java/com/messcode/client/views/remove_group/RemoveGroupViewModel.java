package com.messcode.client.views.remove_group;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.Group;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class RemoveGroupViewModel {
    private MainModel mainModel;
    private ObservableList<Group> groupsList;
    public RemoveGroupViewModel(MainModel mainModel) {
        this.groupsList= FXCollections.observableArrayList() ;
        this.mainModel = mainModel;
    }

    public ObservableList<Group> getGroupsList() {
        return groupsList;
    }
}
