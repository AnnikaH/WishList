package com.example.annika.wishlist;

public class RestWishService {
    private static final String URL = "http://dotnet.cs.hioa.no/s198611/WishListAPI/api";
    private retrofit.RestAdapter restAdapter;
    private WishService apiWishService;

    public RestWishService() {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiWishService = restAdapter.create(WishService.class);
    }

    public WishService getService() {
        return apiWishService;
    }
}