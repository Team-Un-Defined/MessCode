package com.messcode.client.views.login;

import com.messcode.client.model.MainModel;
import com.messcode.transferobjects.User;

public class LoginViewModel
{
  private MainModel mainModel;

  public LoginViewModel(MainModel mainModel)
  {
    this.mainModel = mainModel;
  }

  public void addUser(User username)
  {
    mainModel.addUser(username);
  }
}
