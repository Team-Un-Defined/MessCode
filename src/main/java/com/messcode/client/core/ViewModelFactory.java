package com.messcode.client.core;

import com.messcode.client.views.change_password.ChangePasswordViewModel;
import com.messcode.client.views.chat.ChatClientViewModel;
import com.messcode.client.views.login.LoginViewModel;
import com.messcode.client.views.new_employee.NewEmployeeViewModel;
import com.messcode.client.views.new_group.NewGroupViewModel;

public class ViewModelFactory {

    private ModelFactory mf;
    private ChatClientViewModel chatVM;
    private LoginViewModel loginVM;
    private NewEmployeeViewModel newEmployeeVM;
    private NewGroupViewModel newGroupVM;
    private ChangePasswordViewModel changePasswordVM;

    public ViewModelFactory(ModelFactory mf) {
        this.mf = mf;
        chatVM = new ChatClientViewModel(mf.getMainModel());
        loginVM = new LoginViewModel(mf.getMainModel());
        newEmployeeVM = new NewEmployeeViewModel(mf.getMainModel());
        newGroupVM = new NewGroupViewModel(mf.getMainModel());
    }

    public ChatClientViewModel getChatVM() {
        return chatVM;
    }

    public LoginViewModel getLoginVM() {
        return loginVM;
    }

    public NewEmployeeViewModel getNewEmployeeVM() {
        return newEmployeeVM;
    }

    public NewGroupViewModel getNewGroupVM() {
        return newGroupVM;
    }

    public ChangePasswordViewModel getChangePasswordVM() {
        return changePasswordVM;
    }
}
