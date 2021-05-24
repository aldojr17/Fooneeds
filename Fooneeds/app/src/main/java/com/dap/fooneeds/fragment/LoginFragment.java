package com.dap.fooneeds.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dap.fooneeds.MainActivity;
import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.LoginFragmentBinding;
import com.dap.fooneeds.entity.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class LoginFragment extends Fragment {

    private LoginFragmentBinding fragmentBinding;
    private static LoginFragment loginFragment;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference databaseReference;

    private LoginFragment() {

    }

    public static LoginFragment newInstance() {
        if (loginFragment == null) {
            loginFragment = new LoginFragment();
        }
        return loginFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("users");
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = LoginFragmentBinding.inflate(inflater, container, false);
        fragmentBinding.btnLogin.setOnClickListener(v -> {
            fragmentBinding.btnLogin.setVisibility(View.GONE);
            fragmentBinding.loadingBar.setVisibility(View.VISIBLE);
            String email = fragmentBinding.etEmailLogin.getText().toString();
            String password = fragmentBinding.etPasswordLogin.getText().toString();
            if(email.trim().isEmpty() || password.trim().isEmpty()){
                fragmentBinding.btnLogin.setVisibility(View.GONE);
                fragmentBinding.loadingBar.setVisibility(View.VISIBLE);
                Toast.makeText(getContext(), getResources().getString(R.string.error_login), Toast.LENGTH_SHORT).show();
            }else{
                mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(command -> {
                    if(command.isSuccessful()){
                        FirebaseUser firebaseUser = mAuth.getCurrentUser();
                        Query query = databaseReference.orderByChild("id").equalTo(firebaseUser.getUid());
//                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        query.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                if(snapshot.exists()){
                                    System.out.println(snapshot.child(firebaseUser.getUid()).child("name").getValue(String.class));
                                }
//                                collectPhoneNumbers((Map<String,Object>) snapshot.getValue());
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {

                            }
                        });
                        fragmentBinding.btnLogin.setVisibility(View.GONE);
                        fragmentBinding.loadingBar.setVisibility(View.VISIBLE);
                        Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                        System.out.println(firebaseUser);
                        updateUI(firebaseUser);
                    }else{
                        fragmentBinding.btnLogin.setVisibility(View.VISIBLE);
                        fragmentBinding.loadingBar.setVisibility(View.GONE);
                        Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        fragmentBinding.tvForgotPassword.setOnClickListener(v -> {
            ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.loginContainer, forgotPasswordFragment);
            transaction.commit();
        });

        fragmentBinding.tvSignUp.setOnClickListener(v -> {
            SignUpFragment signUpFragment = SignUpFragment.newInstance();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.loginContainer, signUpFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return fragmentBinding.getRoot();
    }

    private void updateUI(FirebaseUser firebaseUser) {
        if(firebaseUser != null){
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseUser user = mAuth.getCurrentUser();
        if(user != null){
            updateUI(user);
        }
    }
}
