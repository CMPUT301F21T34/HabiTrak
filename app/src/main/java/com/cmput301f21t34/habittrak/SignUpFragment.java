package com.cmput301f21t34.habittrak;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

/**
 * SignUpFragment
 */
public class SignUpFragment extends Fragment {



    public SignUpFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habi_signup_fragment, container, false);

        TextInputLayout emailLayout = view.findViewById(R.id.signup_email_text_input);
        TextInputEditText emailEditText = view.findViewById(R.id.signup_email_edit_text);
        TextInputLayout usernameLayout = view.findViewById(R.id.signup_username_text_input);
        TextInputEditText usernameEditText = view.findViewById(R.id.signup_username_edit_text);
        TextInputLayout passwordLayout = view.findViewById(R.id.signup_password_text_input);
        TextInputEditText passwordEditText = view.findViewById(R.id.signup_password_edit_text);

        return view;
    }
}