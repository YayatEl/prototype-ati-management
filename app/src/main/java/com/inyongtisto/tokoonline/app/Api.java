package com.inyongtisto.tokoonline.app;

import com.inyongtisto.tokoonline.data.UsersList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Eyehunt Team on 12/06/18.
 */

public interface Api {

    @GET("users?q=rokano")
    Call<UsersList> getUsers();
}
