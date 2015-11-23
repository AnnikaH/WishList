package com.example.annika.wishlist;

public class WishList {
    public int ID;
    public String Name;
    public int OwnerId;    // foreign key from User

    // When added a WishList item to ArrayAdapter<WishList> the toString is called:
    @Override
    public String toString() {
        return Name;
    }
}