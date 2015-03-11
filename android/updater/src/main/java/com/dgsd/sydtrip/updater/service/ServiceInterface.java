package com.dgsd.sydtrip.updater.service;

import com.dgsd.sydtrip.updater.model.Manifest;

import retrofit.client.Response;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.Path;
import rx.Observable;

public interface ServiceInterface {

    @GET("/manifest.json")
    public Observable<Manifest> getManifest(@Header("If-Modified-Since") String ifModifiedSince);

    @GET("/{file_url}")
    public Observable<Response> getFile(@Path(value = "file_url", encode = false) String fileUrl);
}
