package com.example.annika.wishlist;

public class RestLoginService {
    private static final String URL = "http://dotnet.cs.hioa.no/s198611/WishListAPI/api";
    private retrofit.RestAdapter restAdapter;
    private LoginService apiLoginService;

    public RestLoginService() {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiLoginService = restAdapter.create(LoginService.class);
    }

    public LoginService getService() {
        return apiLoginService;
    }
}