package com.nspu.findrestaurant.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.nspu.findrestaurant.R;
import com.nspu.findrestaurant.controllers.LoadingUserDataAsync;
import com.nspu.findrestaurant.controllers.interfaces.LoadingUserdataInterface;
import com.nspu.findrestaurant.models.UserData;

public class LoadingActivity extends AppCompatActivity implements LoadingUserdataInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        LoadingUserDataAsync mLoadingUserDataAsync = new LoadingUserDataAsync(this);
        mLoadingUserDataAsync.execute();
    }

    @Override
    public void onSuccessLoadingUserdata(UserData userData) {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        startActivity(mapIntent);
        finish();
    }

    @Override
    public void onErrorLoadingUserdata(Exception error) {
        //TODO: implement error
    }
}
