package com.dap.fooneeds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.ArticleItemBinding;
import com.dap.fooneeds.entity.Article;

import java.util.List;

public class ArticleAdapter extends RecyclerView.Adapter<ArticleAdapter.ArticleViewHolder> {

    private List<Article> articles;
    private ArticleItemListener listener;

    public ArticleAdapter(List<Article> articles, ArticleItemListener listener) {
        this.articles = articles;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ArticleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.article_item, parent, false);
        return new ArticleViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArticleViewHolder holder, int position) {
        holder.setArticleData(articles.get(position));
        holder.itemView.setOnClickListener(v -> listener.articleDataClicked(articles.get(position)));
    }

    @Override
    public int getItemCount() {
        return articles.size();
    }

    static class ArticleViewHolder extends RecyclerView.ViewHolder{

        private ArticleItemBinding binding;

        public ArticleViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ArticleItemBinding.bind(itemView);
        }

        public void setArticleData(Article article){
            binding.txtTitle.setText(article.getTitle());
            binding.txtDesc.setText(article.getDescription());
        }
    }

    public interface ArticleItemListener{
        void articleDataClicked(Article article);
    }
}
