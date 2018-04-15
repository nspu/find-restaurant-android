package com.nspu.findrestaurant.controllers;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import com.nspu.findrestaurant.controllers.interfaces.LoadingUserdataInterface;
import com.nspu.findrestaurant.models.UserData;

import static java.lang.Thread.sleep;

/**
 * I use AsyncTask because we don't need to download lot of data.
 * I use adapter pattern to pass the data or the error.
 * An other method would have been to use reactive(like javarx or MutableLiveData) but
 * it not useful here because you normally retrieve userdata once(i would have used mvvm).
 */
public class LoadingUserDataAsync extends AsyncTask<Void, Void, UserData> {
    private final LoadingUserdataInterface loadingUserdataInterface;
    private Exception mError = null;

    public LoadingUserDataAsync(@NonNull LoadingUserdataInterface loadingUserdataInterface) {
        this.loadingUserdataInterface = loadingUserdataInterface;
    }

    @Override
    protected UserData doInBackground(Void... voids) {
        //TODO: Call retrieve userdata
        UserData userData = null;
        try {
            sleep(3000);
            userData = new UserData("FistName LastName");
        } catch (Exception e) {
            mError = e;
            cancel(true);
        }
        return userData;
    }

    @Override
    protected void onPostExecute(UserData userData) {
        super.onPostExecute(userData);
        loadingUserdataInterface.onSuccessLoadingUserdata(userData);
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        loadingUserdataInterface.onErrorLoadingUserdata(mError);
    }
}

