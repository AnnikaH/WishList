package com.example.annika.wishlist;

public class RestUserService {
    //private static final String URL = "http://localhost:60861/api";
    private static final String URL = "http://dotnet.cs.hioa.no/s198611/WishListAPI/api";
    private retrofit.RestAdapter restAdapter;
    private UserService apiUserService;

    public RestUserService() {
        restAdapter = new retrofit.RestAdapter.Builder()
                .setEndpoint(URL)
                .setLogLevel(retrofit.RestAdapter.LogLevel.FULL)
                .build();

        apiUserService = restAdapter.create(UserService.class);
    }

    public UserService getService() {
        return apiUserService;
    }
}