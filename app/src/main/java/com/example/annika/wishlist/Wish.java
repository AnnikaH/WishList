package com.example.annika.wishlist;

public class Wish {
    public int ID;
    public String Name;
    public String Spesification;
    public byte[] Image;
    public double Price;
    public String Where;
    public String Link;
    public int WishListId; // foreign key from WishList
}