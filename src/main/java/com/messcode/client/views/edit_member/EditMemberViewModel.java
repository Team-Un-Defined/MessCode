package com.messcode.client.views.edit_member;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.User;

import java.beans.PropertyChangeSupport;

public class EditMemberViewModel {

    private PropertyChangeSupport support; // idk if needed?
    private MainModel mainModel;

    public EditMemberViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }
}
