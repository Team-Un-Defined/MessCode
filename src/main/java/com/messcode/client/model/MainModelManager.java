package com.messcode.client.model;

import com.messcode.client.networking.Client;
import com.messcode.transferobjects.AccountManager;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import com.messcode.transferobjects.User;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.IOException;
import java.util.ArrayList;

public class MainModelManager implements MainModel {

    private Client client;
    private PropertyChangeSupport support;
    private User user;
    private PrivateMessage usersPM;
    private ArrayList<PublicMessage> allMessage;



    public MainModelManager(Client client) {
        support = new PropertyChangeSupport(this);
        allMessage = new ArrayList<>();

        this.client = client;
        try {
            client.start();
            client.addListener("AddNewUser", this::addToUsersList);
            client.addListener("MessageForEveryone", this::receivePublic);
            client.addListener("newPM", this::receivePM);
            client.addListener("RemoveUser", this::removeFromUsersList);
            client.addListener("LoginResponse", this::loginResponse);
            client.addListener("LoginData", this::loginData);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loginData(PropertyChangeEvent propertyChangeEvent) {
        Container packet = ((Container) propertyChangeEvent.getNewValue());
        ArrayList<Object> objs = (ArrayList<Object>) packet.getObject();
        ArrayList<PublicMessage> allPublicMessages = (ArrayList<PublicMessage>) objs.get(0);
        ArrayList<PublicMessage> lastSeen = (ArrayList<PublicMessage>) objs.get(1);
        user = (User) objs.get(2);
        System.out.println("Everything has been casted");
        allMessage.addAll(allPublicMessages);
        // user.getLastSeen.add(lastSeen);


        this.allMessage=allPublicMessages;
        // user.getLastSeen.add(lastSeen);


        for (int i = 0; i< this.allMessage.size();i++){
            System.out.println(this.allMessage.get(i).getMsg());
            System.out.println(this.allMessage.get(i).getClass());
            System.out.println(this.allMessage.get(i) instanceof PrivateMessage);
            System.out.println("++++++++++++++++++++++++++++++NYO NYO+++++++++++++++++++++");
            if (this.allMessage.get(i) instanceof PrivateMessage){

                System.out.println("++++++++++++++++++++++++++++++EGY MEG EGY+++++++++++++++++++++");

            }
        }
        support.firePropertyChange("LoginData", null, allMessage);  // probably lot more stuff should happen here and vm, but rn this is okay.
        System.out.println(user.getEmail() + " "+ user.getName());
        support.firePropertyChange("SetUsernameInChat", null, user);
        support.firePropertyChange("LoginData", null, allMessage);  // probably lot more stuff should happen here and vm, but rn this is okay.
    }

    private void loginResponse(PropertyChangeEvent propertyChangeEvent) {
        boolean answer = (boolean) propertyChangeEvent.getNewValue();
        System.out.println("in model: " + answer);
        support.firePropertyChange("LoginResponseToVM", null, answer);

    }

    private void removeFromUsersList(PropertyChangeEvent propertyChangeEvent) {
        support.firePropertyChange(propertyChangeEvent);
    }

    //  GLOBAL CHAT
    @Override
    public void sendListOfPmRoomUsers(PrivateMessage usersPM) {
        this.usersPM = usersPM;
        support.firePropertyChange("UsersOnlineInPM", null, usersPM);
    }


    public void receivePublic(PropertyChangeEvent propertyChangeEvent) {
        PublicMessage publicMessage = (PublicMessage) propertyChangeEvent.getNewValue();
        System.out.println("got to model");
        support.firePropertyChange("MessageForEveryone", null, publicMessage);
    }


    public void receivePM(PropertyChangeEvent propertyChangeEvent) {
        PrivateMessage pm = (PrivateMessage) propertyChangeEvent.getNewValue();
        System.out.println("//////////////////////////PMPM//////////////////////////////");
        support.firePropertyChange("newPM", null, pm);
    }



    public void addToUsersList(PropertyChangeEvent propertyChangeEvent) {
        User user = (User) propertyChangeEvent.getNewValue();
        support.firePropertyChange("AddNewUser", null, user);
    }

    @Override
    public void addUser(String email, String pwd) {
        client.addUser(new User(email,pwd));

    }

    @Override
    public void addListener(String eventName, PropertyChangeListener listener) {
        support.addPropertyChangeListener(eventName, listener);
    }

    @Override
    public void removeListener(String eventName, PropertyChangeListener listener) {
        support.removePropertyChangeListener(eventName, listener);
    }

    @Override
    public void sendPublic(PublicMessage mess) {
        client.sendPublic(mess);
    }

    @Override
    public void sendPM(PrivateMessage message) {
        client.sendPM(message);
    }
    public ArrayList<PublicMessage> getAllMessage() {
        return allMessage;
    }

    public void setAllMessage(ArrayList<PublicMessage> allMessage) {
        this.allMessage = allMessage;
    }

    @Override
    public void register(String firstName, String lastName, String email, String password) {
        User newUser = new User(firstName,lastName,email,password);
        client.register(newUser);
    }

}
