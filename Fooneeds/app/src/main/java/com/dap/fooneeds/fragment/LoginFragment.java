package com.dap.fooneeds.fragment;

import android.content.Context;
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
            if(!fragmentBinding.etEmailLogin.getText().toString().trim().equals("") && !fragmentBinding.etPasswordLogin.getText().toString().trim().equals("")){
                if(checkUser(fragmentBinding.etEmailLogin.getText().toString().trim(), fragmentBinding.etPasswordLogin.getText().toString().trim())){
//                    DetailFragment detailFragment = DetailFragment.newInstance();
//
//                    FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
//                    transaction.replace(R.id.loginContainer, detailFragment);
//                    transaction.addToBackStack(null);
//                    transaction.commit();

//                    String jsonFileString = getJsonFromAssets(getContext(), "user.json");
//                    System.out.println(jsonFileString);
//
//                    Gson gson = new Gson();
//                    List<User> users = gson.fromJson(jsonFileString, new TypeToken<List<User>>() {}.getType());

//                    User user = new User();
//                    // set properties
//                    user.setId(3);
//                    user.setName("test");
//                    user.setEmail("test@gmail.com");
//                    user.setPassword("qwe");
//                    users.add(user);
//
//                    String json = gson.toJson(users);
//                    System.out.println(json);


                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                } else{
                    Toast.makeText(getContext(), "Failed", Toast.LENGTH_SHORT).show();
                }
            } else{
                Toast.makeText(getContext(), "Please fill the field!", Toast.LENGTH_SHORT).show();
            }
        });

        fragmentBinding.tvForgotPassword.setOnClickListener(v -> {
            ForgotPasswordFragment forgotPasswordFragment = ForgotPasswordFragment.newInstance();

            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.loginContainer, forgotPasswordFragment);
            transaction.addToBackStack(null);
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

//    public String getJsonFromAssets(Context context, String fileName) {
//        String jsonString;
//        try {
//            InputStream is = context.getAssets().open(fileName);
//
//            int size = is.available();
//            byte[] buffer = new byte[size];
//            is.read(buffer);
//            is.close();
//
//            jsonString = new String(buffer, "UTF-8");
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//
//        return jsonString;
//    }
}
