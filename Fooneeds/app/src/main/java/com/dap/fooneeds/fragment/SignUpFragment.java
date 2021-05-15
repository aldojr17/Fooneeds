package com.dap.fooneeds.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dap.fooneeds.databinding.ForgotpasswordFragmentBinding;
import com.dap.fooneeds.databinding.SignupFragmentBinding;
import com.dap.fooneeds.entity.User;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

public class SignUpFragment extends Fragment {

    private SignupFragmentBinding signupFragmentBinding;
    private static SignUpFragment signUpFragment;
    private List<User> users;

    private SignUpFragment() {

    }

    public static SignUpFragment newInstance() {
        if (signUpFragment == null) {
            signUpFragment = new SignUpFragment();
        }
        return signUpFragment;
    }

    @Override
    public void onStart() {
        super.onStart();
        try {
            users = fetchUser();
        } catch (IOException e) {
            e.printStackTrace();
        }
        String name = signupFragmentBinding.etNameSignUp.getText().toString().trim();
        String email = signupFragmentBinding.etEmailSignUp.getText().toString().trim();
        String password = signupFragmentBinding.etPasswordSignUp.getText().toString().trim();
        String confirmPassword = signupFragmentBinding.etConfirmPasswordSignUp.getText().toString().trim();
        signupFragmentBinding.btnSignUp.setOnClickListener(v -> {
            if(!name.equals("") && !email.equals("") && !password.equals("") && !confirmPassword.equals("")){
                if(!password.equals(confirmPassword)){
                    Toast.makeText(getContext(), "Password not equal!", Toast.LENGTH_SHORT).show();
                }else{
                    User user = new User();
                    user.setId(users.size());
                    user.setName(name);
                    user.setEmail(email);
                    user.setPassword(password);
                    users.add(user);
                    Toast.makeText(getContext(), "Success", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        signupFragmentBinding = SignupFragmentBinding.inflate(inflater, container, false);
        return signupFragmentBinding.getRoot();
    }

    private List<User> fetchUser() throws IOException {
        InputStream inputStream = getActivity().getAssets().open("user.json");
        JsonReader reader = new JsonReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
        Gson gson = new Gson();
        return Arrays.asList(gson.fromJson(reader, User[].class));
    }

    private boolean checkUser(String email){
        for(User u : users){
            if(u.getEmail().equals(email)){
                return true;
            }
        }
        return false;
    }
}
