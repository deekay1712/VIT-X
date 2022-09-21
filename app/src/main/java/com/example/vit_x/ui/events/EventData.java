package com.example.vit_x.ui.events;

public class EventData {
    String description, link, image, date, time, key;
    public EventData(){

    }

    public EventData(String description, String link, String image, String date, String time, String key) {
        this.description = description;
        this.link = link;
        this.image = image;
        this.date = date;
        this.time = time;
        this.key = key;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
