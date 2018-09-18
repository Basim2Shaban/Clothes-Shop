package com.basim.outfitters.modiles;

/**
 * Created by Basim on 28/07/2018.
 */

public class ModelGetViewPagData {
    private String name;
    private String price;
    private String image_1;
    private String key;


    public ModelGetViewPagData() {
    }


    public ModelGetViewPagData(String name, String price, String image_1, String key) {
        this.name = name;
        this.price = price;
        this.image_1 = image_1;
        this.key = key;
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

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}

