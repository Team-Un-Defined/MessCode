package com.messcode.client.views.edit_project_leader;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.util.Subject;
import java.beans.PropertyChangeEvent;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.sql.Statement;
import java.util.ArrayList;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class EditProjectLeaderViewModel implements Subject {
    private Group selectedGroup;
    private ObservableList<User> usersList;
    private PropertyChangeSupport support;
    private MainModel mainModel;

    public EditProjectLeaderViewModel(MainModel mainModel) {
        support = new PropertyChangeSupport(this);
        usersList= FXCollections.observableArrayList();
        this.mainModel = mainModel;
        
        mainModel.addListener("changeSelectedGroup",this::updateGroup);
        mainModel.addListener("AddOfflineUsers", this::addOfflineUsers);
        
    }
    
    
    private void addOfflineUsers(PropertyChangeEvent propertyChangeEvent) {
        ArrayList<User> users = (ArrayList<User>) propertyChangeEvent.getNewValue();

        Platform.runLater(() -> {
            for (User u : users) {
                if (u.getType().equals("superuser") || u.getType().equals("employer")) {
                    continue;
                }
                usersList.add(u);
            }
            System.out.println(usersList);
        });
    }
     private void updateGroup(PropertyChangeEvent propertyChangeEvent) {
        selectedGroup = (Group)propertyChangeEvent.getNewValue();
  
    }
    
    
    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    public ObservableList<User> getUsers() {
        ObservableList<User> use = FXCollections.observableArrayList();
        use.addAll(usersList);
        use.removeIf( i ->i.getEmail().equals(selectedGroup.getLeader().getEmail()));
        return use;
    }
    
   public User getLeader(){
   
  return selectedGroup.getLeader();
          
   }

    void changeLeader(User u) {
        if(u.getEmail().equals(selectedGroup.getLeader().getEmail())){
        return;
        }
        Group g = new Group(selectedGroup.getName(),selectedGroup.getDescription(),u);
        mainModel.changeLeader(g);
        
    }
    
    
}
