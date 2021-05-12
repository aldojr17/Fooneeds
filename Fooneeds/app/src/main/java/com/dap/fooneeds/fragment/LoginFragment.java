package com.dap.fooneeds.fragment;

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

import com.dap.fooneeds.R;
import com.dap.fooneeds.databinding.LoginFragmentBinding;
import com.dap.fooneeds.entity.User;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginFragment extends Fragment {

    private LoginFragmentBinding fragmentBinding;
    private List<User> users;
    private static LoginFragment loginFragment;

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
        try {
            users = fetchUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentBinding = LoginFragmentBinding.inflate(inflater, container, false);
        fragmentBinding.btnLogin.setOnClickListener(v -> {
            if(!fragmentBinding.etEmailLogin.getText().toString().trim().equals("") &&
                    !fragmentBinding.etPasswordLogin.getText().toString().trim().equals("")){
                if(checkUser(fragmentBinding.etEmailLogin.getText().toString().trim()
                        , fragmentBinding.etPasswordLogin.getText().toString().trim())){
//                    DetailFragment detailFragment = DetailFragment.newInstance();
//
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.loginContainer, detailFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentBinding.tvForgotPassword.setOnClickListener(v -> {
            ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.loginContainer, forgotPasswordFragment);
            transaction.addToBackStack(null);
            transaction.commit();
        });
        return fragmentBinding.getRoot();
    }

    private List<User> fetchUser() throws IOException {
        InputStream inputStream = getActivity().getAssets().open("user.json");
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        Gson gson = new Gson();
        return Arrays.asList(gson.fromJson(reader, User[].class));
    }

    private boolean checkUser(String email, String password){
        for(User u : users){
            if(u.getEmail().equals(email) && u.getPassword().equals(password)){
                return true;
            }
        }
        return false;
    }
}
