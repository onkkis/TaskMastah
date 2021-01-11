package com.example.taskmastah;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.auth.FirebaseUser;

public class UserViewModel extends AndroidViewModel {

    public MutableLiveData<FirebaseUser> user = new MutableLiveData<>();

    //User setter + getter
    public void setUser(FirebaseUser user){ this.user.setValue(user); }
    public LiveData<FirebaseUser> getUser(){ return user; }

    public UserViewModel(@NonNull Application application) { super(application); }
}
