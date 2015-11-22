package com.example.annika.wishlist;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.POST;

public interface LoginService {

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Login
    @POST("/Login")
    void logIn(@Body LoginUser loginUser, Callback<String> callback);
}