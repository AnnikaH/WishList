package com.example.annika.wishlist;

public class Sharing {
    private int ID;
    private int UserId;     // foreign key from User
    private int WishListId; // foreign key from WishList

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public int getWishListId() {
        return WishListId;
    }

    public void setWishListId(int wishListId) {
        WishListId = wishListId;
    }
}