package com.example.plogginglovers.Client;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class RetrofitClient {

    private static Retrofit retrofit;

    //Define the base URL//

    //private static final String BASE_URL = "http://my-json-server.typicode.com/iMikeZz/PLmobileApp/"; //todo change
    private static final String BASE_URL = "http://46.101.15.61/api/"; //todo change
    //private static final String BASE_URL = "https://jsonplaceholder.typicode.com";
    //Create the Retrofit instance//

    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {
            retrofit = new retrofit2.Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    //Add the converter//
                    .addConverterFactory(GsonConverterFactory.create())
                    //Build the Retrofit instance//
                    .build();
        }
        return retrofit;
    }
}
