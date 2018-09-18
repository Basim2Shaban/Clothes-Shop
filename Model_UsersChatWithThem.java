package com.basim.outfitters.modiles;

/**
 * Created by Basim on 20/07/2018.
 */

public class Model_UsersChatWithThem {
    private String userimage ;
    private String username ;
    private String userid ;


    public Model_UsersChatWithThem() {
    }

    public Model_UsersChatWithThem(String userimage, String username, String userid) {
        this.userimage = userimage;
        this.username = username;
        this.userid = userid;
    }

    public String getUserimage() {
        return userimage;
    }

    public void setUserimage(String userimage) {
        this.userimage = userimage;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }
}
