package com.cmput301f21t34.habittrak.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cmput301f21t34.habittrak.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotFragment extends Fragment {

    TextInputLayout emailLayout;
    TextInputEditText emailEditText;
    MaterialButton sendButton;
    Auth mAuth;

    public ForgotFragment(Auth auth) {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habi_forgot_fragment, container, false);

        emailLayout = view.findViewById(R.id.forgot_email_text_input);
        emailEditText = view.findViewById(R.id.forgot_email_edit_text);
        sendButton = view.findViewById(R.id.forgot_send_button);
        mAuth = new Auth(getActivity(), null); // no need for db here, potential crash cause though





        // if everything correct then start base activity

        return view;
    }

    @Override
    public void onStart(){
        super.onStart();

        sendButton.setOnClickListener(view1 -> {

            String email = emailEditText.getText().toString();

            if (email == ""){
                emailEditText.setError("Cannot be empty");
            } else {
                emailEditText.setError(null);
                FirebaseAuth fAuth = mAuth.getAuth();
                try {
                    fAuth.sendPasswordResetEmail(email);
                } catch (Exception e){
                    emailEditText.setError(e.toString());
                }

            }

        });

    }

}
