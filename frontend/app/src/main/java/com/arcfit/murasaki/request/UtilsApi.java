package com.arcfit.murasaki.request;

public class UtilsApi {
    public static final String BASE_URL_API = "http://localhost:7000";
    public static BaseApiService getApiService() {
        return
                RetrofitClient.getClient(BASE_URL_API).create(BaseApiService.class);
    }
}