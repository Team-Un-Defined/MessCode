package com.messcode.client.views.change_password;

import com.messcode.client.model.MainModel;

public class ChangePasswordViewModel {

    private MainModel mainModel;

    public ChangePasswordViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }

    public void changePassword(String password, String passwordConfirmed) {

        mainModel.changePassword(password,passwordConfirmed);
    }
}
