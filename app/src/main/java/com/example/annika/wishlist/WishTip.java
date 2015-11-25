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

    @Override
    public String toString() {
        return Name;
    }
}