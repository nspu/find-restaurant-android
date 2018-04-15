package com.nspu.findrestaurant.controllers.interfaces;

import com.nspu.findrestaurant.models.UserData;

public interface LoadingUserdataInterface {
    void onSuccessLoadingUserdata(UserData userData);

    void onErrorLoadingUserdata(Exception error);
}