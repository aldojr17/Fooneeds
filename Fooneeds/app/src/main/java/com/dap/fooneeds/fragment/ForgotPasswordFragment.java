package com.dap.fooneeds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.ForgotpasswordFragmentBinding;
import com.dap.fooneeds.entity.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class ForgotPasswordFragment extends Fragment {

    private ForgotpasswordFragmentBinding forgotpasswordFragmentBinding;
    private static ForgotPasswordFragment forgotPasswordFragment;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private ForgotPasswordFragment() {

    }

    public static ForgotPasswordFragment newInstance() {
        if (forgotPasswordFragment == null) {
            forgotPasswordFragment = new ForgotPasswordFragment();
        }
        return forgotPasswordFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
        forgotpasswordFragmentBinding.btnResetLink.setOnClickListener(v -> {
            if(!forgotpasswordFragmentBinding.etEmailForgotPassword.getText().toString().trim().equals("")){
                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for(DataSnapshot dataSnapshot : snapshot.getChildren()){
                            if(dataSnapshot.child("email").getValue(String.class).equals(forgotpasswordFragmentBinding.etEmailForgotPassword.getText().toString())){
                                Bundle bundle = new Bundle();
                                bundle.putString(User.USER_EMAIL, forgotpasswordFragmentBinding.etEmailForgotPassword.getText().toString().trim());
                                CheckYourEmailFragment checkYourEmailFragment = CheckYourEmailFragment.newInstance();
                                checkYourEmailFragment.setArguments(bundle);

                                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                                transaction.replace(R.id.loginContainer, checkYourEmailFragment);
                                transaction.commit();
                                break;
                            }else{
                                Toast.makeText(getContext(), "Email not found", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
            }else{
                Toast.makeText(getContext(), "Please fill all field", Toast.LENGTH_SHORT).show();
            }
        });

        forgotpasswordFragmentBinding.backButton.setOnClickListener(v -> {
            getActivity().getSupportFragmentManager().popBackStackImmediate();
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        forgotpasswordFragmentBinding = ForgotpasswordFragmentBinding.inflate(inflater, container, false);
        return forgotpasswordFragmentBinding.getRoot();
    }
}
