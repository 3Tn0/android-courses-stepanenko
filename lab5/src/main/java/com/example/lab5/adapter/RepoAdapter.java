package com.example.lab5.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.lab5.Repo;

import java.util.ArrayList;
import java.util.List;


public class RepoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TYPE_REPO = 0;

    private List<Repo> repos = new ArrayList<>();

    @Override
    @NonNull
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_REPO:
                return new RepoHolder(parent);
        }
        throw new IllegalArgumentException("unknown viewType = " + viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_REPO:
                RepoHolder repoHolder = (RepoHolder) holder;
                Repo repo = repos.get(position);
                repoHolder.repo.setText(
                        repo.fullName
                );

                repoHolder.description.setText(
                        repo.description != "null" ? repo.description : ""
                );
                break;
        }
    }

    @Override
    public int getItemCount() {
        return repos.size();
    }

    @Override
    public int getItemViewType(int position) {
        return TYPE_REPO;
    }

    public void setRepos(List<Repo> repos) {
        this.repos = repos;
    }
}
