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

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
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
    }

    public int getWishListId() {
        return WishListId;
    }

    public void setWishListId(int wishListId) {
        WishListId = wishListId;
    }
}