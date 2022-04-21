package com.messcode.client.core;

import com.messcode.client.views.change_password.ChangePasswordViewModel;
import com.messcode.client.views.chat.ChatClientViewModel;
import com.messcode.client.views.edit_member.EditMemberController;
import com.messcode.client.views.edit_member.EditMemberViewModel;
import com.messcode.client.views.login.LoginViewModel;
import com.messcode.client.views.new_employee.NewEmployeeViewModel;
import com.messcode.client.views.new_group.NewGroupViewModel;
import com.messcode.client.views.remove_group.RemoveGroupViewModel;
import com.messcode.client.views.remove_user.RemoveUserViewModel;

public class ViewModelFactory {

    private ModelFactory mf;
    private ChatClientViewModel chatVM;
    private LoginViewModel loginVM;
    private NewEmployeeViewModel newEmployeeVM;
    private NewGroupViewModel newGroupVM;
    private ChangePasswordViewModel changePasswordVM;
    private EditMemberViewModel editMemberVM;
    private RemoveGroupViewModel removeGroupVM;
    private RemoveUserViewModel removeUserVM;

    public ViewModelFactory(ModelFactory mf) {
        this.mf = mf;
        chatVM = new ChatClientViewModel(mf.getMainModel());
        loginVM = new LoginViewModel(mf.getMainModel());
        newEmployeeVM = new NewEmployeeViewModel(mf.getMainModel());
        newGroupVM = new NewGroupViewModel(mf.getMainModel());
        editMemberVM = new EditMemberViewModel(mf.getMainModel());
        removeGroupVM = new RemoveGroupViewModel(mf.getMainModel());
        removeUserVM = new RemoveUserViewModel(mf.getMainModel());
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

    public EditMemberViewModel getEditMemberVM() {
        return editMemberVM;
    }

    public RemoveGroupViewModel getRemoveGroupVM() {
        return removeGroupVM;
    }

    public RemoveUserViewModel getRemoveUserVM() {
        return removeUserVM;
    }
}
