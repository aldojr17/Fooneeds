package com.dap.fooneeds;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dap.fooneeds.databinding.ActivityDetailBinding;
import com.dap.fooneeds.databinding.AddToCartPopupBinding;
import com.dap.fooneeds.databinding.ToastAddBinding;
import com.dap.fooneeds.entity.CartItem;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private AddToCartPopupBinding popupBinding;
    private ToastAddBinding addBinding;
    private DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailBinding.inflate(getLayoutInflater());
        popupBinding = AddToCartPopupBinding.inflate(getLayoutInflater());
        addBinding = ToastAddBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        if(bundle != null && !bundle.isEmpty()){
            Glide.with(binding.getRoot()).load(bundle.getString("cover")).into(binding.imgFood);
            binding.tvFoodName.setText(bundle.getString("name"));
            binding.tvFoodType.setText(bundle.getString("type"));
            binding.tvDesc.setText(bundle.getString("desc"));
            binding.tvStock.setText("Stock : " + bundle.getString("stock") + " pcs");
            binding.tvPrice.setText("Rp." + bundle.getString("price"));
            popupBinding.textTotal.setText("Rp." + bundle.getString("price"));
        }

        reference = FirebaseDatabase.getInstance().getReference("users/" + bundle.getString("id") + "/cart");

        final Dialog mBottomSheetDialog = new Dialog(this, R.style.MaterialDialogSheet);
        mBottomSheetDialog.setContentView(popupBinding.getRoot()); // your custom view.
        mBottomSheetDialog.setCancelable(true);
        mBottomSheetDialog.getWindow().setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mBottomSheetDialog.getWindow().setGravity(Gravity.BOTTOM);

        binding.btnAddToCart.setOnClickListener(v -> mBottomSheetDialog.show());

        popupBinding.btnMinus.setOnClickListener(v -> {
            if(popupBinding.textQty.getText().toString().equals("1")){
                Toast.makeText(this, "Minimun Qty Is 1", Toast.LENGTH_SHORT).show();
            }else{
                popupBinding.textQty.setText(String.valueOf(Integer.parseInt(popupBinding.textQty.getText().toString()) - 1));
                popupBinding.textTotal.setText("Rp." + String.valueOf(Integer.parseInt(bundle.getString("price")) * Integer.parseInt(popupBinding.textQty.getText().toString())));
            }
        });

        popupBinding.btnPlus.setOnClickListener(v -> {
            popupBinding.textQty.setText(String.valueOf(Integer.parseInt(popupBinding.textQty.getText().toString()) + 1));
            popupBinding.textTotal.setText("Rp." + String.valueOf(Integer.parseInt(bundle.getString("price")) * Integer.parseInt(popupBinding.textQty.getText().toString())));
        });

        popupBinding.btnAddToCart.setOnClickListener(v -> {
            CartItem item = new CartItem();
            item.setName(bundle.getString("name"));
            item.setCover(bundle.getString("cover"));
            item.setCategory(bundle.getString("type"));
            item.setPrice(Integer.parseInt(popupBinding.textTotal.getText().toString().substring(3)));
            item.setQty(Integer.parseInt(popupBinding.textQty.getText().toString()));

            reference.push().setValue(item);
            mBottomSheetDialog.hide();
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(addBinding.getRoot());
            toast.show();
        });
    }
}