package com.dap.fooneeds.adapter;

import android.app.Dialog;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.AddToCartPopupBinding;
import com.dap.fooneeds.databinding.FoodItemBinding;
import com.dap.fooneeds.databinding.SaveFragmentBinding;
import com.dap.fooneeds.databinding.SavedItemBinding;
import com.dap.fooneeds.entity.Article;
import com.dap.fooneeds.entity.CartItem;
import com.dap.fooneeds.entity.Food;
import com.dap.fooneeds.fragment.SavedFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.lang.ref.WeakReference;
import java.util.List;

public class SavedAdapter extends RecyclerView.Adapter<SavedAdapter.SavedViewHolder> {

    private List<Food> foods;
    private SavedItemListener listener;

    public SavedAdapter(List<Food> foods, SavedItemListener listener) {
        this.foods = foods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SavedViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.saved_item, parent, false);
        return new SavedViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedViewHolder holder, int position) {
        holder.setSavedData(foods.get(position));
    }

    @Override
    public int getItemCount() {
        return foods.size();
    }

    static class SavedViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private SavedItemBinding binding;
        private AddToCartPopupBinding popupBinding;
        private DatabaseReference reference;
        private final Dialog mBottomSheetDialog;
        private WeakReference<SavedItemListener> listenerRef;

        public SavedViewHolder(@NonNull View itemView, SavedItemListener listener) {
            super(itemView);
            binding = SavedItemBinding.bind(itemView);
            popupBinding = AddToCartPopupBinding.bind(LayoutInflater.from(itemView.getContext()).inflate(R.layout.add_to_cart_popup, null));

            listenerRef = new WeakReference<>(listener);
            mBottomSheetDialog = new Dialog(itemView.getContext(), R.style.MaterialDialogSheet);
            mBottomSheetDialog.setContentView(popupBinding.getRoot()); // your custom view.
            mBottomSheetDialog.setCancelable(true);
            mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

            binding.btnAdd2.setOnClickListener(v -> mBottomSheetDialog.show());
            reference = FirebaseDatabase.getInstance().getReference("users/" + FirebaseAuth.getInstance().getCurrentUser().getUid());
            binding.btnFav2.setOnClickListener(this);
        }

        public void setSavedData(Food food){
            binding.txtFoodName2.setText(food.getName());
            binding.txtFoodType2.setText(food.getCategory());
            binding.txtFoodPrice2.setText("Rp."+String.valueOf(food.getPrice()));
            Glide.with(binding.getRoot())
                    .load(food.getCover())
                    .into(binding.imgFood2);
            popupBinding.textTotal.setText(String.valueOf("Rp." + food.getPrice()));
            popupBinding.btnMinus.setOnClickListener(v -> {
                if(popupBinding.textQty.getText().toString().equals("1")){
                    Toast.makeText(itemView.getContext(), "Minimun Qty Is 1", Toast.LENGTH_SHORT).show();
                }else{
                    popupBinding.textQty.setText(String.valueOf(Integer.parseInt(popupBinding.textQty.getText().toString()) - 1));
                    popupBinding.textTotal.setText("Rp." + String.valueOf(food.getPrice() * Integer.parseInt(popupBinding.textQty.getText().toString())));
                }
            });
            popupBinding.btnPlus.setOnClickListener(v -> {
                popupBinding.textQty.setText(String.valueOf(Integer.parseInt(popupBinding.textQty.getText().toString()) + 1));
                popupBinding.textTotal.setText("Rp." + String.valueOf(food.getPrice() * Integer.parseInt(popupBinding.textQty.getText().toString())));
            });
            popupBinding.btnAddToCart.setOnClickListener(v -> {
                CartItem item = new CartItem();
                item.setName(food.getName());
                item.setCover(food.getCover());
                item.setCategory(food.getCategory());
                item.setOriPrice(food.getPrice());
                item.setPrice(Integer.parseInt(popupBinding.textTotal.getText().toString().substring(3)));
                item.setQty(Integer.parseInt(popupBinding.textQty.getText().toString()));

                reference.child("cart").push().setValue(item);

                mBottomSheetDialog.dismiss();
                Toast toast = new Toast(itemView.getContext());
                toast.setGravity(Gravity.BOTTOM, 0, 250);
                toast.setDuration(Toast.LENGTH_LONG);
                toast.setView(LayoutInflater.from(itemView.getContext()).inflate(R.layout.toast_add, null));
                toast.show();
            });
        }

        @Override
        public void onClick(View v) {
            listenerRef.get().savedDataClicked(getAdapterPosition());
        }
    }

    public interface SavedItemListener{
        void savedDataClicked(int position);
    }
}
