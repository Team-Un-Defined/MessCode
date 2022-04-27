package com.messcode.client.core;

import com.messcode.client.views.change_password.ChangePasswordViewModel;
import com.messcode.client.views.chat.ChatClientViewModel;
import com.messcode.client.views.edit_member.EditMemberController;
import com.messcode.client.views.edit_member.EditMemberViewModel;
import com.messcode.client.views.edit_project_leader.EditProjectLeaderViewModel;
import com.messcode.client.views.login.LoginViewModel;
import com.messcode.client.views.new_employee.NewEmployeeViewModel;
import com.messcode.client.views.new_group.NewGroupViewModel;
import com.messcode.client.views.remove_group.RemoveGroupViewModel;
import com.messcode.client.views.remove_user.RemoveUserViewModel;
import com.messcode.client.views.view_group.ViewGroupViewModel;
import com.messcode.client.views.view_profile.ViewProfileViewModel;

/**
 *This class is responsible for initializing and returning the instance of different viewmodel
 */
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
    private EditProjectLeaderViewModel editProjectLeaderVM;
    private ViewProfileViewModel viewProfileVM;
    private ViewGroupViewModel viewGroupVM;

    /**
     * Assigning the MainModelManager to all viewmodels and initializing them.
     * @param mf ModelFactory object
     */
    public ViewModelFactory(ModelFactory mf) {
        this.mf = mf;
        chatVM = new ChatClientViewModel(mf.getMainModel());
        loginVM = new LoginViewModel(mf.getMainModel());
        newEmployeeVM = new NewEmployeeViewModel(mf.getMainModel());
        newGroupVM = new NewGroupViewModel(mf.getMainModel());
        changePasswordVM = new ChangePasswordViewModel(mf.getMainModel());
        editMemberVM = new EditMemberViewModel(mf.getMainModel());
        removeGroupVM = new RemoveGroupViewModel(mf.getMainModel());
        removeUserVM = new RemoveUserViewModel(mf.getMainModel());
        editProjectLeaderVM = new EditProjectLeaderViewModel(mf.getMainModel());
        viewProfileVM = new ViewProfileViewModel(mf.getMainModel());
        viewGroupVM = new ViewGroupViewModel(mf.getMainModel());
    }

    /**
     * Get method
     * @return ChatClientViewModel
     */
    public ChatClientViewModel getChatVM() {
        return chatVM;
    }

    /**
     * Get method
     * @return LoginViewModel
     */
    public LoginViewModel getLoginVM() {
        return loginVM;
    }

    /**
     * Get method
     * @return NewEmployeeViewModel
     */
    public NewEmployeeViewModel getNewEmployeeVM() {
        return newEmployeeVM;
    }

    /**
     * get method
     * @return NewGroupViewModel
     */
    public NewGroupViewModel getNewGroupVM() {
        return newGroupVM;
    }

    /**
     * get method
     * @return ChangePasswordViewModel
     */
    public ChangePasswordViewModel getChangePasswordVM() {
        return changePasswordVM;
    }

    /**
     * get method
     * @return EditMemberViewModel
     */
    public EditMemberViewModel getEditMemberVM() {
        return editMemberVM;
    }

    /**
     * get method
     * @return RemoveGroupViewModel
     */
    public RemoveGroupViewModel getRemoveGroupVM() {
        return removeGroupVM;
    }

    /**
     * get method
     * @return RemoveUserViewModel
     */
    public RemoveUserViewModel getRemoveUserVM() {
        return removeUserVM;
    }

    /**
     * get method
     * @return EditProjectLeaderViewModel
     */
    public EditProjectLeaderViewModel getEditProjectLeaderVM() {
        return editProjectLeaderVM;
    }

    /**
     * get method
     * @return ViewProfileViewModel
     */
    public ViewProfileViewModel getViewProfileVM() {
        return viewProfileVM;
    }

    /**
     * get method
     * @return ViewGroupViewModel
     */
    public ViewGroupViewModel getViewGroupVM() {
        return viewGroupVM;
    }
}
