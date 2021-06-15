package com.dap.fooneeds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.dap.fooneeds.adapter.CartAdapter;
import com.dap.fooneeds.databinding.CheckoutFragmentBinding;
import com.dap.fooneeds.entity.CartItem;
import com.dap.fooneeds.entity.Food;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CartCheckoutFragment extends Fragment {

    private CheckoutFragmentBinding fragmentBinding;
    private static CartCheckoutFragment cartCheckoutFragment;
    private ArrayList<CartItem> cartItems;
    private FirebaseAuth mAuth;
    private FirebaseUser user;
    private DatabaseReference reference;
    private CartAdapter cartAdapter;

    private CartCheckoutFragment(){}

    public static CartCheckoutFragment newInstance(){
        if(cartCheckoutFragment == null){
            cartCheckoutFragment = new CartCheckoutFragment();
        }
        return cartCheckoutFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        cartItems = new ArrayList<>();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = CheckoutFragmentBinding.inflate(inflater, container, false);
        cartAdapter = new CartAdapter(cartItems, new CartAdapter.CartItemListener() {
            @Override
            public void savedDataMinClicked(int position) {
                reference.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("name").getValue().equals(cartItems.get(position).getName())){
                                if((Long) dataSnapshot.child("qty").getValue() == 1){
                                    Toast.makeText(getContext(), "Item Removed!", Toast.LENGTH_SHORT).show();
                                    snapshot.child(dataSnapshot.getKey()).getRef().removeValue();
                                    cartItems.remove(position);
                                    cartAdapter.notifyDataSetChanged();
                                }else{
                                    reference.child("cart").child(dataSnapshot.getKey()).child("qty").setValue(dataSnapshot.child("qty").getValue(Long.class) - 1);
                                    cartItems.get(position).setQty(dataSnapshot.child("qty").getValue(Integer.class) - 1);
                                    cartAdapter.notifyDataSetChanged();
                                }
                                setTotal();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }

            @Override
            public void savedDataPlusClicked(int position) {
                reference.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("name").getValue().equals(cartItems.get(position).getName())){
                                reference.child("cart").child(dataSnapshot.getKey()).child("qty").setValue(dataSnapshot.child("qty").getValue(Long.class) + 1);
                                cartItems.get(position).setQty(dataSnapshot.child("qty").getValue(Integer.class) + 1);
                                cartAdapter.notifyDataSetChanged();
                                setTotal();
                                break;
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }
        });
        fragmentBinding.rvItem.setLayoutManager(new LinearLayoutManager(getContext()));
        fragmentBinding.rvItem.setAdapter(cartAdapter);
        fragmentBinding.tvSubtotal.setText("Rp." + sumSubtotal());
        fragmentBinding.tvShipping.setText("Rp.10.000");
        fragmentBinding.tvTotal.setText("Rp." + String.valueOf(sumSubtotal() + 10000));
        return fragmentBinding.getRoot();
    }

    @Override
    public void onStart() {
        super.onStart();
        user = mAuth.getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("users/" + user.getUid());
        fetchCartItem();
    }

    private void fetchCartItem() {
        reference.child("cart").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                    cartItems.add(dataSnapshot.getValue(CartItem.class));
                }
                cartAdapter.notifyDataSetChanged();
                setTotal();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void setTotal(){
        fragmentBinding.tvSubtotal.setText("Rp." + sumSubtotal());
        fragmentBinding.tvShipping.setText("Rp.10.000");
        fragmentBinding.tvTotal.setText("Rp." + String.valueOf(sumSubtotal() + 10000));
    }

    private int sumSubtotal(){
        int subtotal = 0;
        for(CartItem cartItem : cartItems){
            subtotal += (cartItem.getQty() * cartItem.getPrice());
        }
        return subtotal;
    }
}
