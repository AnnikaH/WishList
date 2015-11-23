package com.example.annika.wishlist;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface WishListService {

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/WishList/5
    // Get
    // @GET("/WishList/{id}")
    // void getWishListById(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/WishList/5
    // Get
    @GET("/WishList/{id}")
    void getAllWishListsForUser(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/WishList
    // Register/post
    @POST("/WishList")
    void addWishList(@Body WishList wishList, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/WishList/5
    // Update/put
    @PUT("/WishList/{id}")
    void updateWishListById(@Path("id") Integer id, @Body WishList wishList, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/WishList/5
    // Delete
    @DELETE("/WishList/{id}")
    void deleteWishListById(@Path("id") Integer id, Callback<Response> callback);
}