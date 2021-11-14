package com.cmput301f21t34.habittrak.auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cmput301f21t34.habittrak.BaseActivity;
import com.cmput301f21t34.habittrak.DatabaseManager;
import com.cmput301f21t34.habittrak.R;
import com.cmput301f21t34.habittrak.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


/**
 * LoginFragment
 *
 * @author Pranav
 * @author Henry
 *
 * Login Fragment for the app
 *
 * TODO: update the password validation: current username: <any> password: <any>
 */
public class LoginFragment extends Fragment {

    private User mainUser;
    private DatabaseManager db;
    private Auth mAuth;  // This is our Auth Helper Class
    private FirebaseUser authUser;

    private View view;

    TextInputLayout usernameLayout;
    TextInputEditText usernameEditText;
    TextInputLayout passwordLayout;
    TextInputEditText passwordEditText;
    MaterialButton loginButton;
    MaterialButton signupButton;
    MaterialButton forgotButton;



    public LoginFragment(User mainUser){
        this.mainUser = mainUser; // Passes User object

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.habi_login_fragment, container, false);

        // Variables of the UI elements

        // UI
        usernameLayout = view.findViewById(R.id.username_text_input);
        usernameEditText = view.findViewById(R.id.username_edit_text);
        passwordLayout = view.findViewById(R.id.password_text_input);
        passwordEditText = view.findViewById(R.id.password_edit_text);
        loginButton = view.findViewById(R.id.login_button);
        signupButton = view.findViewById(R.id.signup_button);
        forgotButton = view.findViewById(R.id.forgot_button);

        db = new DatabaseManager();

        mAuth = new Auth(getActivity(), db);
        

        return view;

    }

    @Override
    public void onStart(){
        super.onStart();

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toSignUp();
            }
        });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { toForgot(); }
        });



        // Password validator
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordLayout.setError(null);
                usernameLayout.setError(null);

                String email = usernameEditText.getText().toString();
                String password = passwordEditText.getText().toString();

                try {
                    runLogin(email, password);
                } catch (Exception e) {

                    if (e instanceof IllegalArgumentException) {
                        // Invalid entry
                        usernameLayout.setError("Invalid Entry");
                        passwordLayout.setError("Invalid Entry");
                    }

                    else {
                        // Displays any other exceptions
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG);
                    }


                }


            }



        });

    }


    ///


    private void runLogin(String email, String password){
        // This is the firebase auth
        // Must be used as they don't get put on top of the stack but are called later
        FirebaseAuth fAuth = mAuth.getAuth();


        fAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){

                            authUser = fAuth.getCurrentUser();

                            if (authUser.isEmailVerified()){


                                mainUser = db.getUser(authUser.getEmail());
                                startHomePage(null, mainUser);

                            } else {

                                usernameLayout.setError("Email not Verified");
                                passwordLayout.setError(null);

                                mAuth.alertNotVerified(authUser).show();

                            }


                        } else {

                            usernameLayout.setError(null);
                            passwordLayout.setError("Incorrect Password");


                        }
                    }
                });
    }





    /**
     * startHomePage
     * Start the base activity after logging in
     * @param view
     */
    public void startHomePage(View view, User currentUser){

        Log.d("MERGE", "startHomePage");

        Intent intent = new Intent(getActivity(), BaseActivity.class);

        intent.putExtra("mainUser", currentUser);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        startActivity(intent);
        getActivity().finish();
    }

    private void toSignUp() {
        SignUpFragment signUpFragment = new SignUpFragment(mAuth);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, signUpFragment, "signupFrag")
                .addToBackStack(null)
                .commit();
    }

    private void toForgot() {
        ForgotFragment forgotFragment = new ForgotFragment(null);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.login_fragment_container, forgotFragment, "forgotFrag")
                .addToBackStack(null)
                .commit();

    }

}
