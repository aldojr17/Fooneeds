package com.dap.fooneeds.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.FoodItemBinding;
import com.dap.fooneeds.databinding.SaveFragmentBinding;
import com.dap.fooneeds.databinding.SavedItemBinding;
import com.dap.fooneeds.entity.Food;

import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedViewHolder> {

    private List<Food> foods;

    public SavedAdapter(List<Food> foods) {
        this.foods = foods;
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_item, parent, false);
        return new SavedViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        holder.setSavedData(foods.get(position));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    static class SavedViewHolder extends RecyclerView.ViewHolder{

        private SavedItemBinding binding;

        public SavedViewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SavedItemBinding.bind(itemView);
        }

        public void setSavedData(Food food){
            binding.txtFoodName2.setText(food.getName());
            binding.txtFoodType2.setText(food.getCategory());
            binding.txtFoodPrice2.setText("Rp."+String.valueOf(food.getPrice()));
            Glide.with(binding.getRoot())
                    .load(food.getCover())
                    .into(binding.imgFood2);
        }
    }
}
