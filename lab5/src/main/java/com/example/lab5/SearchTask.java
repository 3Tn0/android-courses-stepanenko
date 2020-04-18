package com.example.lab5;

import androidx.annotation.Nullable;
import androidx.annotation.WorkerThread;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;


public class SearchTask extends Task<List<Repo>> {

    private static OkHttpClient httpClient;

    private String text = "";
    private int pageNumber = 1;

    public static OkHttpClient getHttpClient() {
        if (httpClient == null) {
            synchronized (SearchTask.class) {
                if (httpClient == null) {
                    HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor()
                            .setLevel(HttpLoggingInterceptor.Level.BASIC);
                    httpClient = new OkHttpClient.Builder()
                            .addInterceptor(loggingInterceptor)
                            .build();
                }
            }
        }
        return httpClient;
    }

    public SearchTask(@Nullable Observer<List<Repo>> observer, String text, int pageNumber) {
        super(observer);
        this.text = text;
        this.pageNumber = pageNumber;
    }

    @Override
    @WorkerThread
    protected List<Repo> executeInBackground() throws Exception {
        String response = search();
        return parseSearch(response);
    }

    private String search() throws Exception {
        Request request = new Request.Builder()
                .url("https://api.github.com/search/repositories?q=" + text + "&page=" + pageNumber + "&per_page=20")
                .build();
        Response response = getHttpClient().newCall(request).execute();
        if (response.code() != 200) {
            throw new Exception("api returned unexpected http code: " + response.code());
        }

        return response.body().string();
    }

    private List<Repo> parseSearch(String response) throws JSONException {
        JSONObject responseJson = new JSONObject(response);
        List<Repo> repos = new ArrayList<>();
        JSONArray items = responseJson.getJSONArray("items");
        for (int i = 0; i < items.length(); i++) {
            JSONObject repoJson = items.getJSONObject(i);
            Repo repo = new Repo(repoJson.getString("full_name"), repoJson.getString("description"), repoJson.getString("html_url"));
            repos.add(repo);
        }
        return repos;
    }
}
