package com.example.lab5;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class ReposCache {
    private static ReposCache instance;

    public static ReposCache getInstance() {
        if (instance == null) {
            synchronized (ReposCache.class) {
                if (instance == null) {
                    instance = new ReposCache();
                }
            }
        }
        return instance;
    }

    private Set<Repo> repos = new LinkedHashSet<>();

    private int pageNumber = 1;

    private String querry = "";

    private ReposCache() {
    }

    public void setRepos(List<Repo> repos){
        this.repos.clear();
        this.repos.addAll(repos);
    }

    public void setPageNumber(int pageNumber){
        this.pageNumber = pageNumber;
    }
    public void setQuerry(String querry){
        this.querry = querry;
    }

    public void addRepos(List<Repo> repos){
        this.repos.addAll(repos);
    }

    @NonNull
    public List<Repo> getRepos() {
        return new ArrayList<>(repos);
    }

    public int getPageNumber() { return pageNumber; }

    public  String getQuerry() { return  querry; }
}
