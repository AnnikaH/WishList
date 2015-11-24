package com.example.annika.wishlist;

public class RestSharingService {
    private static final String URL = "http://dotnet.cs.hioa.no/s198611/WishListAPI/api";
    private retrofit.RestAdapter restAdapter;
    private SharingService apiSharingService;

    public RestSharingService() {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiSharingService = restAdapter.create(SharingService.class);
    }

    public SharingService getService() {
        return apiSharingService;
    }
}