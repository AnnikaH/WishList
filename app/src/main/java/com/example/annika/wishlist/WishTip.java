package com.example.annika.wishlist;

public class WishTip {
    private int ID;
    private int SenderId;   // foreign key from User
    private int ReceiverId; // foreign key from User
    private String Name;
    private String Spesification;
    private byte[] Image;
    private double Price;
    private String Where;
    private String Link;
}