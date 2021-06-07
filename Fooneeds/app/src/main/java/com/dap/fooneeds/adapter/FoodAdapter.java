package com.dap.fooneeds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.FoodItemBinding;
import com.dap.fooneeds.entity.Food;

import java.util.List;

public class FoodAdapter extends RecyclerView.Adapter<FoodAdapter.FoodViewHolder> {

    private List<Food> foods;
    private FoodItemListener listener;

    public FoodAdapter(List<Food> foods, FoodItemListener listener) {
        this.foods = foods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.food_item, parent, false);
        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int position) {
        holder.setFoodData(foods.get(position));
        holder.itemView.setOnClickListener(v -> listener.articleDataClicked(foods.get(position)));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    static class FoodViewHolder extends RecyclerView.ViewHolder{

        private FoodItemBinding binding;

        public FoodViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = FoodItemBinding.bind(itemView);
        }

        public void setFoodData(Food food){
            binding.txtFoodName.setText(food.getName());
            binding.txtFoodType.setText(food.getCategory());
            binding.txtFoodPrice.setText("Rp."+String.valueOf(food.getPrice()));
            Glide.with(binding.getRoot())
                    .load(food.getCover())
                    .into(binding.imgFood);
        }
    }

    public interface FoodItemListener{
        void articleDataClicked(Food food);
    }
}
