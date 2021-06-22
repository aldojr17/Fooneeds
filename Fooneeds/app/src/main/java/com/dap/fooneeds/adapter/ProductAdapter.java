package com.dap.fooneeds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.ArticleItemBinding;
import com.dap.fooneeds.databinding.ProductItemBinding;
import com.dap.fooneeds.entity.Article;
import com.dap.fooneeds.entity.Product;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private List<Product> products;
    private ProductItemListener listener;

    public ProductAdapter(List<Product> products, ProductItemListener listener) {
        this.products = products;
        this.listener = listener;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        holder.setProductData(products.get(position));
        holder.binding.btnProduct.setOnClickListener(v -> listener.productDataClicked(products.get(position)));
    }

    @Override
    public int getItemCount() {
        return products.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder{

        private ProductItemBinding binding;

        public ProductViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = ProductItemBinding.bind(itemView);
        }

        public void setProductData(Product product){
            binding.btnProduct.setText(product.getName());
        }
    }

    public interface ProductItemListener{
        void productDataClicked(Product product);
    }
}
