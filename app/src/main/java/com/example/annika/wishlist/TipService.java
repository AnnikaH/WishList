package com.example.annika.wishlist;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface TipService {

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/TipHelper/5
    // Get
    @GET("/TipHelper/{id}")
    void getAllTipsForReceiver(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Tip
    // Get
    @GET("/Tip")
    void getAllTips(Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Tip/5
    // Get
    @GET("/Tip/{id}")
    void getTipById(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Tip
    // Register/post
    @POST("/Tip")
    void addTip(@Body WishTip wishTip, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Tip/5
    // Update/put
    @PUT("/Tip/{id}")
    void updateTipById(@Path("id") Integer id, @Body WishTip wishTip, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Tip/5
    // Delete
    @DELETE("/Tip/{id}")
    void deleteTipById(@Path("id") Integer id, Callback<Response> callback);
}