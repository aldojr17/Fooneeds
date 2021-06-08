package com.dap.fooneeds;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.dap.fooneeds.databinding.ActivityDetailBinding;
import com.dap.fooneeds.databinding.AddToCartPopupBinding;
import com.dap.fooneeds.databinding.ToastAddBinding;
import com.dap.fooneeds.entity.CartItem;
import com.dap.fooneeds.entity.Food;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class DetailActivity extends AppCompatActivity {

    private ActivityDetailBinding binding;
    private AddToCartPopupBinding popupBinding;
    private ToastAddBinding addBinding;
    private DatabaseReference reference;
    private DatabaseReference foodReference;
    private DatabaseReference detailFoodReference;

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
            binding.tvFoodType.setText(bundle.getString("category"));
            binding.tvDesc.setText(bundle.getString("desc"));
            binding.tvStock.setText("Stock : " + bundle.getString("stock") + " pcs");
            binding.tvPrice.setText("Rp." + bundle.getString("price"));
            popupBinding.textTotal.setText("Rp." + bundle.getString("price"));
        }

        reference = FirebaseDatabase.getInstance().getReference("users/" + bundle.getString("id"));
        reference.child("fav").addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("UseCompatLoadingForDrawables")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    if(dataSnapshot.child("name").getValue(String.class).equals(bundle.getString("name"))){
                        binding.btnFav.setImageDrawable(getResources().getDrawable(R.drawable.favourites));
                        break;
                    }else{
                        binding.btnFav.setImageDrawable(getResources().getDrawable(R.drawable.loveplain));
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        foodReference = FirebaseDatabase.getInstance().getReference("foods");
        foodReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot ds : snapshot.getChildren()){
                    if(ds.child("category").getValue().equals(bundle.getString("category"))){
                        DatabaseReference catReference = FirebaseDatabase.getInstance().getReference("foods/" + ds.getKey() + "/products");
                        catReference.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                for(DataSnapshot childSnap : snapshot.getChildren()){
                                    if(((Long) childSnap.child("id").getValue()) == Long.parseLong(bundle.getString("foodId"))){
                                        detailFoodReference = FirebaseDatabase.getInstance().getReference("foods/" + ds.getKey() + "/products/" + childSnap.getKey());
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

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
            item.setCategory(bundle.getString("category"));
            item.setPrice(Integer.parseInt(popupBinding.textTotal.getText().toString().substring(3)));
            item.setQty(Integer.parseInt(popupBinding.textQty.getText().toString()));

            reference.child("cart").push().setValue(item);

            detailFoodReference.child("stock").setValue(Integer.parseInt(bundle.getString("stock")) - Integer.parseInt(popupBinding.textQty.getText().toString()));
            binding.tvStock.setText("Stock : " + String.valueOf(Integer.parseInt(bundle.getString("stock")) - Integer.parseInt(popupBinding.textQty.getText().toString())) + " pcs");
            mBottomSheetDialog.hide();
            Toast toast = new Toast(getApplicationContext());
            toast.setGravity(Gravity.BOTTOM, 0, 150);
            toast.setDuration(Toast.LENGTH_LONG);
            toast.setView(addBinding.getRoot());
            toast.show();
        });

        binding.btnFav.setOnClickListener(v -> {
            Food item = new Food();
            item.setName(bundle.getString("name"));
            item.setCover(bundle.getString("cover"));
            item.setCategory(bundle.getString("category"));
            item.setType(bundle.getString("type"));
            item.setAge(bundle.getString("age"));
            item.setId(Integer.parseInt(bundle.getString("foodId")));
            item.setPrice(Integer.parseInt(binding.tvPrice.getText().toString().substring(3)));
            if(binding.btnFav.getDrawable().getConstantState() == getResources().getDrawable(R.drawable.favourites).getConstantState()){
                binding.btnFav.setImageDrawable(getResources().getDrawable(R.drawable.loveplain));
                Toast.makeText(this, "Item Removed!", Toast.LENGTH_SHORT).show();
                reference.child("fav").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("name").getValue().equals(item.getName())){
                                snapshot.child(dataSnapshot.getKey()).getRef().removeValue();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else{
                binding.btnFav.setImageDrawable(getResources().getDrawable(R.drawable.favourites));
                Toast.makeText(this, "Item Saved!", Toast.LENGTH_SHORT).show();
                reference.child("fav").push().setValue(item);
            }
        });
    }
}