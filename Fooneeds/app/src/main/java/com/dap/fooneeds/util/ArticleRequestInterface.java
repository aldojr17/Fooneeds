package com.dap.fooneeds.util;

import com.dap.fooneeds.entity.ArticleResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ArticleRequestInterface {
    @GET("v2/everything?q=pet&apiKey=20d26ea0a6464b0ca8e8e7a2772e5c44")
    Call<ArticleResponse> getArticleResponse();
}
