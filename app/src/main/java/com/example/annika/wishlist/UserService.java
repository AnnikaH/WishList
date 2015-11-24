package com.example.annika.wishlist;

import java.util.List;
import retrofit.Callback;
import retrofit.client.Response;
import retrofit.http.Body;
import retrofit.http.DELETE;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Path;

public interface UserService {

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/User
    // Get
    @GET("/User")
    void getAllUsers(Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/User/5
    // Get
    @GET("/User/{id}")
    void getUserById(@Path("id") Integer id, Callback<Response> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/User
    // Register/post
    @POST("/User")
    void addUser(@Body User user, Callback<User> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/User/5
    // Update/put
    @PUT("/User/{id}")
    void updateUserById(@Path("id") Integer id, @Body User user, Callback<User> callback);

    // http://dotnet.cs.hioa.no/s198611/WishListAPI/api/User/5
    // Delete
    @DELETE("/User/{id}")
    void deleteUserById(@Path("id") Integer id, Callback<User> callback);
}