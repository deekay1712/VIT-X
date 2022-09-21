package com.example.vit_x.ui.home;

public class PostData {
    String author, authorImageUrl, description, imageUrl, pid, postedAt, uid;

    public PostData() {
    }

    public PostData(String author, String authorImageUrl, String description, String imageUrl, String pid, String postedAt, String uid) {
        this.author = author;
        this.authorImageUrl = authorImageUrl;
        this.description = description;
        this.imageUrl = imageUrl;
        this.pid = pid;
        this.postedAt = postedAt;
        this.uid = uid;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getAuthorImageUrl() {
        return authorImageUrl;
    }

    public void setAuthorImageUrl(String authorImageUrl) {
        this.authorImageUrl = authorImageUrl;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getPostedAt() {
        return postedAt;
    }

    public void setPostedAt(String postedAt) {
        this.postedAt = postedAt;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
