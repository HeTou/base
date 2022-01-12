package com.example.https;

import io.reactivex.rxjava3.core.Observable;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface AppService {


    @POST("/EPM-CORE-SERVICE/api/pc/project/page")
    Observable<Object> page(@Query("current") int current, @Query("size") int size, @Query("type") int type, @Query("employeeId") String employeeId);
}
