package com.example.annika.wishlist;

public class Wish {
    private int ID;
    private String Name;
    private String Spesification;
    private byte[] Image;
    private double Price;
    private String Where;
    private String Link;
    private int WishListId; // foreign key from WishList
}