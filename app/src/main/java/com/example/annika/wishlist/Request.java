package com.example.annika.wishlist;

// Helper class to display request in a good way (with names instead of just ids)
public class Request {
    public int SharingID;
    public int WishListId;
    public String WishListName;
    public int UserId;
    public String UserName;
    public boolean Confirmed;

    @Override
    public String toString() {
        return UserName + ": " + WishListName;
    }
}