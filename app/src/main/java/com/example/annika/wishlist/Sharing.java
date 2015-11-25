package com.example.annika.wishlist;

public class Sharing {
    public int ID;
    public int UserId;     // foreign key from User
    public int WishListId; // foreign key from WishList
    public boolean Confirmed;

    @Override
    public String toString() {
        return "UserID: " + UserId + ", WishListID: " + WishListId;
    }
}