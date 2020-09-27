package com.blossomgames.simplechatbox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.AuthUserAttribute;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;

import java.util.ArrayList;

public class SignupActivity extends AppCompatActivity {

    private static final int CONFIRM_SIGNUP_CODE = 10;

    private EditText UsernameView;
    private EditText PasssordView;
    private EditText EmailView;
    private EditText ContactView;

    private Button SignInView;
    private Button SignUpView;
    private Button ForgotPasswordView;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        UsernameView = findViewById(R.id.signupusername);
        PasssordView = findViewById(R.id.signuppassword);
        EmailView = findViewById(R.id.signupemail);
        ContactView = findViewById(R.id.signupcontact);


        SignInView = findViewById(R.id.signupactivitysigninBtn);
        SignUpView = findViewById(R.id.signupactivitysignupBtn);

        SignUpView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = UsernameView.getText().toString();
                String password = PasssordView.getText().toString();
                String email = EmailView.getText().toString();
                String contact = ContactView.getText().toString();

                ArrayList<AuthUserAttribute> attributes = new ArrayList<>();
                attributes.add(new AuthUserAttribute(AuthUserAttributeKey.email(), email));
                attributes.add(new AuthUserAttribute(AuthUserAttributeKey.phoneNumber(), contact));


                Amplify.Auth.signUp(
                        username,
                        password,
                        AuthSignUpOptions.builder().userAttributes(attributes).build(),
                        result ->{
                            Log.i("TAG", result.toString());
                            Intent confirmSignupActivity = new Intent(getBaseContext(), ConfirmSignupActivity.class);
                            confirmSignupActivity.putExtra("username", username);
                            startActivityForResult(confirmSignupActivity, CONFIRM_SIGNUP_CODE);
                        },
                        error -> Log.e("TAG", error.toString())
                );
            }
        });

        SignInView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), SigninActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
