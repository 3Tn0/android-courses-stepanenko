package com.example.lab5;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.lab5.adapter.RepoAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Lab5Activity extends AppCompatActivity {

    private static final String TAG = Lab5Activity.class.getSimpleName();
    private final ReposCache reposCache = ReposCache.getInstance();

    Context main = this;

    private RecyclerView list;
    private RepoAdapter repoAdapter;

    private ProgressBar progressBar;
    private FloatingActionButton fab;
    private SwipeRefreshLayout swipeRefreshLayout;
    private LinearLayoutManager layoutManager;

    private boolean isLoading = false;
    private int pageNumber = 1;
    private String querryString = "";

    private static Executor threadExecutor = Executors.newCachedThreadPool();

    public static Intent newIntent(@NonNull Context context) {
        return new Intent(context, Lab5Activity.class);
    }

    private SearchTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle(getString(R.string.lab5_title, getClass().getSimpleName()));

        setContentView(R.layout.lab5_activity);

        list = findViewById(android.R.id.list);
        progressBar = findViewById(R.id.lab5_progressbar);
        fab = findViewById(R.id.lab5_floatingactionbutton);
        swipeRefreshLayout = findViewById(R.id.swiperefresh);

        fab.setOnClickListener(
                v -> {
                    fab.setVisibility(View.INVISIBLE);
                    if (querryString.length() > 2){
                        progressBar.setVisibility(View.VISIBLE);
                        if (isLoading){
                            task = new SearchTask(searchObserver, querryString, pageNumber);
                        }
                        else {
                            task = new SearchTask(searchObserver, querryString, 1);
                        }
                        threadExecutor.execute(task);
                    }
                }
        );

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (querryString.length() > 2){
                    task = new SearchTask(searchObserver, querryString, 1);
                    threadExecutor.execute(task);
                }
                else{
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        layoutManager = new LinearLayoutManager(this);

        RecyclerView.OnScrollListener scrollListener = new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();
                int firstVisibleItems = layoutManager.findFirstVisibleItemPosition();

                if (!isLoading) {
                    if ( (visibleItemCount+firstVisibleItems) >= totalItemCount) {
                        isLoading = true;
                        pageNumber++;
                        reposCache.setPageNumber(pageNumber);
                        task = new SearchTask(searchObserver, querryString, pageNumber);
                        progressBar.setVisibility(View.VISIBLE);
                        threadExecutor.execute(task);
                    }
                }
            }
        };

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(list.getContext(),
                layoutManager.getOrientation());
        list.setLayoutManager(layoutManager);
        list.addItemDecoration(dividerItemDecoration);
        list.addOnScrollListener(scrollListener);

        list.setAdapter(repoAdapter = new RepoAdapter());
        repoAdapter.setRepos(reposCache.getRepos());

        pageNumber = reposCache.getPageNumber();
        querryString = reposCache.getQuerry();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.lab5_search, menu);

        MenuItem searchItem = menu.findItem(R.id.search);

        SearchManager searchManager = (SearchManager) Lab5Activity.this.getSystemService(Context.SEARCH_SERVICE);

        Handler handler = new Handler();

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(Lab5Activity.this.getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                handler.removeCallbacksAndMessages(null);

                if (s.length() > 2){
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isLoading = false;
                            querryString = s;
                            reposCache.setQuerry(s);
                            progressBar.setVisibility(View.VISIBLE);
                            task = new SearchTask(searchObserver, s, 1);
                            threadExecutor.execute(task);
                        }
                    }, 500);
                }

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(task != null)
            task.unregisterObserver();
    }

    private Observer<List<Repo>> searchObserver = new Observer<List<Repo>>() {
        @Override
        public void onLoading(@NonNull Task<List<Repo>> task) {
            Log.d(TAG, "onLoading");
        }

        @Override
        public void onSuccess(@NonNull Task<List<Repo>> task, @Nullable List<Repo> data) {
            Log.d(TAG, "onSuccess");
            for (Repo repo : data) {
                Log.d(TAG, repo.toString());
            }

            if (isLoading){
                reposCache.addRepos(data);
                repoAdapter.setRepos(reposCache.getRepos());
                repoAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
                isLoading = false;
            }
            else {
                reposCache.setRepos(data);
                repoAdapter.setRepos(data);
                repoAdapter.notifyDataSetChanged();
                progressBar.setVisibility(View.INVISIBLE);
            }
            swipeRefreshLayout.setRefreshing(false);
        }

        @Override
        public void onError(@NonNull Task<List<Repo>> task, @NonNull Exception e) {
            Log.e(TAG, "onError", e);

            progressBar.setVisibility(View.INVISIBLE);
            swipeRefreshLayout.setRefreshing(false);
            Toast.makeText(main, R.string.lab5_fetching_error, Toast.LENGTH_LONG).show();
            fab.setVisibility(View.VISIBLE);
        }
    };
}
