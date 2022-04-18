package com.messcode.client.views.new_employee;

import com.messcode.client.model.MainModel;

public class NewEmployeeViewModel {

    private MainModel mainModel;

    public NewEmployeeViewModel(MainModel mainModel) {
        this.mainModel = mainModel;
    }
    public void register(String firstName,String lastName,String email,String password,String type){
    mainModel.register(firstName, lastName, email, password,type);
    }
    
    
}
