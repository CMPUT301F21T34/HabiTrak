package com.cmput301f21t34.habittrak;

import android.os.Bundle;
import android.text.Editable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.habi_login_fragment, container, false);
        TextInputLayout usernameLayout = view.findViewById(R.id.username_text_input);
        TextInputEditText usernameEditText = view.findViewById(R.id.username_edit_text);
        TextInputLayout passwordLayout = view.findViewById(R.id.password_text_input);
        TextInputEditText passwordEditText = view.findViewById(R.id.password_edit_text);
        MaterialButton loginButton = view.findViewById(R.id.login_button);

        // Password validator
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPasswordValid(passwordEditText.getText(), usernameEditText.getText())) {
                    passwordLayout.setError("Incorrect username and password");
                } else{
                    passwordLayout.setError(null);

                }



            }
        });


        return view;
    }

    public boolean isPasswordValid(@Nullable Editable password, @Nullable Editable username){
        Boolean passwordOk = false;
        String pass = password.toString();
        String user = username.toString();
        if (pass.equals("admin") && user.equals("admin")){
            passwordOk = true;
        }
        return passwordOk;
    }
}
