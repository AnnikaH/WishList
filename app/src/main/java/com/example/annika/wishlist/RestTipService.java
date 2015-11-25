package com.example.annika.wishlist;

public class RestTipService {
    private static final String URL = "http://dotnet.cs.hioa.no/s198611/WishListAPI/api";
    private retrofit.RestAdapter restAdapter;
    private TipService apiTipService;

    public RestTipService() {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiTipService = restAdapter.create(TipService.class);
    }

    public TipService getService() {
        return apiTipService;
    }
}