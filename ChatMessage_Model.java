package com.basim.outfitters.modiles;

/**
 * Created by Basim on 18/07/2018.
 */

public class ChatMessage_Model {
    public String id ;
    public String image;
    public String text_message;


    public ChatMessage_Model() {
    }

    public ChatMessage_Model(String id, String image, String text_message) {
        this.id = id;
        this.image = image;
        this.text_message = text_message;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getText_message() {
        return text_message;
    }

    public void setText_message(String text_message) {
        this.text_message = text_message;
    }
}
