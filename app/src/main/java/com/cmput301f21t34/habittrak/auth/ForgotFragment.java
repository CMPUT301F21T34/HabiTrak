package com.cmput301f21t34.habittrak.auth;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.cmput301f21t34.habittrak.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;

/**
 * ForgotFragment
 *
 * @author Dakota
 * @version 1.0
 * <p>
 * Forgot Fragment for the app
 */
public class ForgotFragment extends Fragment {

    private TextInputEditText emailEditText;
    private MaterialButton sendButton;
    Auth mAuth;

    // empty fragment constructor
    public ForgotFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.habi_forgot_fragment, container, false);

        emailEditText = view.findViewById(R.id.forgot_email_edit_text);
        sendButton = view.findViewById(R.id.forgot_send_button);
        mAuth = new Auth(getActivity());

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        sendButton.setOnClickListener(view1 -> {

            // null check
            if (emailEditText.getText() == null) {
                emailEditText.setText("");
            }

            String email = emailEditText.getText().toString();

            if (email.equals("")) {
                emailEditText.setError("Cannot be empty");
            } else {
                emailEditText.setError(null);
                FirebaseAuth fAuth = mAuth.getAuth();
                try {
                    fAuth.sendPasswordResetEmail(email);
                    emailEditText.setError("Email Sent");
                } catch (Exception e) {
                    emailEditText.setError(e.toString());
                }

            }

        });

    }

}
