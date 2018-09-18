package com.basim.outfitters.single_classes;

import com.basim.outfitters.firebase_use.FireBaseVarConnect;

import java.util.Calendar;

/**
 * Created by Basim on 22/07/2018.
 */

public class ClassMakeUserOnAndOff {
  private FireBaseVarConnect varConnect = new FireBaseVarConnect();

    private String mId = varConnect.bCurrentUser.getUid();

    public long timeNowMilliSecond (){
        Calendar rightNow = Calendar.getInstance();
        long milliSec = rightNow.getTimeInMillis();
        return milliSec ;
    }

    public void makeUserOn(){
        varConnect.bUserStateConn.child(mId).child("state").setValue(-1);
    }

}
