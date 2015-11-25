package com.example.annika.wishlist;

import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface SharingService {
    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/SharingHelper/5
    // Get
    @GET("/SharingHelper/{id}")
    void getAllSharingsForUser(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Sharing
    // Get
    @GET("/Sharing")
    void getAllSharings(Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Sharing/5
    // Get
    @GET("/Sharing/{id}")
    void getSharingById(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Sharing
    // Register/post
    @POST("/Sharing")
    void addSharing(@Body Sharing sharing, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Sharing/5
    // Update/put
    @PUT("/Sharing/{id}")
    void updateSharingById(@Path("id") Integer id, @Body Sharing sharing, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/Sharing/5
    // Delete
    @DELETE("/Sharing/{id}")
    void deleteSharingById(@Path("id") Integer id, Callback<Response> callback);
}