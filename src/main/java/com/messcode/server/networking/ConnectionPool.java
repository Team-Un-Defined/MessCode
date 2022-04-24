package com.messcode.server.networking;

import JDBC.ExportData;
import com.messcode.transferobjects.ClassName;
import com.messcode.transferobjects.Container;
import com.messcode.transferobjects.Group;
import com.messcode.transferobjects.User;
import com.messcode.transferobjects.messages.GroupMessages;
import com.messcode.transferobjects.messages.PrivateMessage;
import com.messcode.transferobjects.messages.PublicMessage;
import java.sql.SQLException;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConnectionPool {

    private List<ServerSocketHandler> connections = new ArrayList<>();
    private List<User> users = new ArrayList<>();

    public void broadcastMessage(PublicMessage publicMessage) {
        for (ServerSocketHandler handler : connections) {
            handler.sendMessage(publicMessage);
        }
    }

    public void userJoin(User user) {
        for (ServerSocketHandler handler : connections) {
            if (handler.getUser() != null && !handler.getUser().equals(user)) {
                handler.joinChat(user);
            }
        }
        users.add(user);
    }

    public void sendMessageInPM(PrivateMessage pm) {
        for (ServerSocketHandler handler : connections) {

            if (handler.getUser().getEmail().equals(pm.getSender().getEmail()) || handler.getUser().getEmail().equals(pm.getReceiver().getEmail()))
            { handler.sendMessageInPM(pm);System.out.println("I got the user");}

        }
    }

    public List<User> getUsers() {
        return users;
    }

    public synchronized void addHandler(ServerSocketHandler handler) {
        connections.add(handler);
    }

    public void removeHandler(ServerSocketHandler handler) {
        connections.remove(handler);
        userLeft(handler.getUser());
    }

    private void userLeft(User user) {
        users.remove(user);
        for (ServerSocketHandler handler : connections) {
            if (handler.getUser() != null && !handler.getUser().equals(user)) {
                handler.userLeft(user);
            }
        }
    }

    public boolean userCheck(User us) {
        boolean ans=false;
        for (ServerSocketHandler handler : connections) {

            System.out.println("kur a bigi "+us.getEmail());
            System.out.println(us);
            System.out.println(handler.getUser());
            System.out.println(handler.getUser().getEmail());
            if (handler.getUser().equals(us) || handler.getUser().getEmail().equals(us.getEmail()) ) {
                ans = true;
                break;
            }
        }
        return ans;
    }
    public void updateGroup(ExportData dbe){
     
     for (ServerSocketHandler handler : connections) {
         try {
             handler.sendGroups(new Container(dbe.updateGroups(handler.getUser()),ClassName.GROUP_UPDATE));

         } catch (SQLException ex) {
             Logger.getLogger(ConnectionPool.class.getName()).log(Level.SEVERE, null, ex);
         }
        }
    
    
    }


    public void sendGroupMessages(GroupMessages message) {
        for (ServerSocketHandler handler : connections) {
            for(int i=0;i<message.getGroup().getMembers().size();i++)
            {
                if(message.getGroup().getMembers().get(i).getEmail().equals(handler.getUser().getEmail()))
                {
                    handler.sendGroupMessage(message);break;
                }
            }


        }
    }

    public void kickUser(User u) {

        for (ServerSocketHandler handler : connections) {
            if (handler.getUser().getEmail().equals(u.getEmail())) {
                handler.removeUser();
                removeHandler(handler);
                break;
            }
        }
    }

    public void sendAllGroupMessages(ArrayList<PublicMessage> groupMessages) {
        if(groupMessages!=null)
        { Group g = ((GroupMessages)groupMessages.get(0)).getGroup();
            for (ServerSocketHandler handler : connections) {
                for(int i=0;i<g.getMembers().size();i++)
                {
                    if(g.getMembers().get(i).getEmail().equals(handler.getUser().getEmail()))
                    {
                        handler.sendAllGroupMessage(groupMessages);break;
                    }
                }


            }
        }
    }
}
