package com.dap.fooneeds.fragment;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dap.fooneeds.adapter.ArticleAdapter;
import com.dap.fooneeds.databinding.ArticleFragmentBinding;
import com.dap.fooneeds.entity.Article;
import com.dap.fooneeds.entity.ArticleResponse;
import com.dap.fooneeds.util.ArticleRequestInterface;

import java.util.ArrayList;
import java.util.Arrays;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ArticleFragment extends Fragment {

    private ArticleFragmentBinding fragmentBinding;
    private static ArticleFragment articleFragment;
    private ArrayList<Article> articles;
    private ArticleAdapter articleAdapter;

    private ArticleFragment() {

    }

    public static ArticleFragment newInstance() {
        if (articleFragment == null) {
            articleFragment = new ArticleFragment();
        }
        return articleFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        articles = new ArrayList<>();
        fetchArticleData();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = ArticleFragmentBinding.inflate(inflater, container, false);
        articleAdapter = new ArticleAdapter(articles, article -> {
            Uri uri = Uri.parse(article.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(intent);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        fragmentBinding.rvArticles.setLayoutManager(manager);
        fragmentBinding.rvArticles.setAdapter(articleAdapter);
        return fragmentBinding.getRoot();
    }

    private void fetchArticleData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        ArticleRequestInterface request = retrofit.create(ArticleRequestInterface.class);
        Call<ArticleResponse> call = request.getArticleResponse();

        call.enqueue(new Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, Response<ArticleResponse> response) {
                ArticleResponse articleResponse = response.body();
                articles = new ArrayList<>(Arrays.asList(articleResponse.getArticles()));
                refreshData(articles);
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "No Data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void refreshData(ArrayList<Article> articles) {
        articleAdapter = new ArticleAdapter(articles, article -> {
            Uri uri = Uri.parse(article.getUrl());
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            if(intent.resolveActivity(getActivity().getPackageManager()) != null){
                startActivity(intent);
            }
        });
        LinearLayoutManager manager = new LinearLayoutManager(getContext());
        fragmentBinding.rvArticles.setLayoutManager(manager);
        fragmentBinding.rvArticles.setAdapter(articleAdapter);
    }
}
