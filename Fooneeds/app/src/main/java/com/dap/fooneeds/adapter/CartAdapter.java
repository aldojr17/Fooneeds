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
import com.dap.fooneeds.databinding.CartItemBinding;
import com.dap.fooneeds.databinding.SavedItemBinding;
import com.dap.fooneeds.entity.CartItem;
import com.dap.fooneeds.entity.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.ref.WeakReference;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private List<CartItem> cartItems;
    private CartItemListener listener;

    public CartAdapter(List<CartItem> foods, CartItemListener listener) {
        this.cartItems = foods;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item, parent, false);
        return new CartViewHolder(view, listener);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.setCartData(cartItems.get(position));
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    static class CartViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private CartItemBinding binding;
        private WeakReference<CartItemListener> listenerRef;

        public CartViewHolder(@NonNull View itemView, CartItemListener listener) {
            super(itemView);
            binding = CartItemBinding.bind(itemView);

            listenerRef = new WeakReference<>(listener);
            binding.btnMin3.setOnClickListener(this);
            binding.btnAdd3.setOnClickListener(this);
        }

        public void setCartData(CartItem item){
            binding.txtFoodName2.setText(item.getName());
            binding.txtFoodType2.setText(item.getCategory());
            binding.txtFoodPrice2.setText("Rp."+String.valueOf(item.getOriPrice()));
            Glide.with(binding.getRoot())
                    .load(item.getCover())
                    .into(binding.imgFood3);
            binding.textQty.setText(String.valueOf(item.getQty()));
        }

        @Override
        public void onClick(View v) {
            if(v.getId() == binding.btnMin3.getId()) {
                listenerRef.get().savedDataMinClicked(getAdapterPosition());
            }else{
                listenerRef.get().savedDataPlusClicked(getAdapterPosition());
            }
        }
    }

    public interface CartItemListener{
        void savedDataMinClicked(int position);
        void savedDataPlusClicked(int position);
    }
}
