package com.example.annika.wishlist;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface WishService {

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/WishHelper/5
    // Get
    @GET("/WishHelper/{id}")
    void getAllWishesForWishList(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Wish/5
    // Get
    @GET("/Wish/{id}")
    void getWishById(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Wish
    // Register/post
    @POST("/Wish")
    void addWish(@Body Wish wish, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Wish/5
    // Update/put
    @PUT("/Wish/{id}")
    void updateWishById(@Path("id") Integer id, @Body Wish wish, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Wish/5
    // Delete
    @DELETE("/Wish/{id}")
    void deleteWishById(@Path("id") Integer id, Callback<Response> callback);
}