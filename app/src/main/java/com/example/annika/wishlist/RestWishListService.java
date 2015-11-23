package com.example.annika.wishlist;

public class RestWishListService {
    private static final String URL = "http://dotnet.cs.hioa.no/s198611/WishListAPI/api";
    private retrofit.RestAdapter restAdapter;
    private WishListService apiWishListService;

    public RestWishListService() {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiWishListService = restAdapter.create(WishListService.class);
    }

    public WishListService getService() {
        return apiWishListService;
    }
}