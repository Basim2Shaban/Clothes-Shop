package com.basim.outfitters.modiles;

/**
 * Created by Basim on 03/06/2018.
 */

public class Model_GetPosts {
    public String allPrice;
    public String colors;
    public String image_1;
    public String image_2;
    public String mobile;
    public String more;
    public String myEmail;
    public String myName;
    public String name;
    public String price;
    public String keyItem;
    public String user_id;

    public Model_GetPosts() {
    }

    public Model_GetPosts(String allPrice, String colors, String image_1, String image_2, String mobile, String more, String myEmail, String myName, String name, String price, String keyItem, String user_id) {
        this.allPrice = allPrice;
        this.colors = colors;
        this.image_1 = image_1;
        this.image_2 = image_2;
        this.mobile = mobile;
        this.more = more;
        this.myEmail = myEmail;
        this.myName = myName;
        this.name = name;
        this.price = price;
        this.keyItem = keyItem;
        this.user_id = user_id;
    }

    public String getAllPrice() {
        return allPrice;
    }

    public void setAllPrice(String allPrice) {
        this.allPrice = allPrice;
    }

    public String getColors() {
        return colors;
    }

    public void setColors(String colors) {
        this.colors = colors;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMore() {
        return more;
    }

    public void setMore(String more) {
        this.more = more;
    }

    public String getMyEmail() {
        return myEmail;
    }

    public void setMyEmail(String myEmail) {
        this.myEmail = myEmail;
    }

    public String getMyName() {
        return myName;
    }

    public void setMyName(String myName) {
        this.myName = myName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getKeyItem() {
        return keyItem;
    }

    public void setKeyItem(String keyItem) {
        this.keyItem = keyItem;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}