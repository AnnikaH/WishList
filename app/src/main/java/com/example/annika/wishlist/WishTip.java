package com.example.annika.wishlist;

public class WishTip {
    public int ID;
    public int SenderId;   // foreign key from User
    public int ReceiverId; // foreign key from User
    public String Name;
    public String Spesification;
    public byte[] Image;
    public double Price;
    public String Where;
    public String Link;
/*
    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getSenderId() {
        return SenderId;
    }

    public void setSenderId(int senderId) {
        SenderId = senderId;
    }

    public int getReceiverId() {
        return ReceiverId;
    }

    public void setReceiverId(int receiverId) {
        ReceiverId = receiverId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getSpesification() {
        return Spesification;
    }

    public void setSpesification(String spesification) {
        Spesification = spesification;
    }

    public byte[] getImage() {
        return Image;
    }

    public void setImage(byte[] image) {
        Image = image;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getWhere() {
        return Where;
    }

    public void setWhere(String where) {
        Where = where;
    }

    public String getLink() {
        return Link;
    }

    public void setLink(String link) {
        Link = link;
    }*/
}