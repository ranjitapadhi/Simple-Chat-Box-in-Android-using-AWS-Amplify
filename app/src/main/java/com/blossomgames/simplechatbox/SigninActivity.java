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
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;

public class SigninActivity extends AppCompatActivity {

    private EditText UsernameView;
    private EditText PasssordView;

    private Button SignInView;
    private Button SignUpView;
    private Button ForgotPasswordView;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        UsernameView = findViewById(R.id.loginusername);
        PasssordView = findViewById(R.id.loginpassword);

        SignInView = findViewById(R.id.signinBtn);
        SignUpView = findViewById(R.id.signinactivitysignupBtn);

        SignInView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = UsernameView.getText().toString();
                String password = PasssordView.getText().toString();

                Amplify.Auth.signIn(
                        username,
                        password,
                        result -> {
                            Log.i("AuthQuickstart", result.isSignInComplete() ? "Sign in succeeded" : "Sign in not complete");

                            Amplify.DataStore.clear(() -> Log.i("MyAmplifyApp", "DataStore is Cleared"),
                                    failure -> Log.e("MyAmplifyApp", "Failed to Clear"));

                            if(result.isSignInComplete()){

                                Amplify.API.query(
                                        ModelQuery.list(User.class, User.NAME.contains(username)),
                                        response -> {
                                            int counter = 0;
                                            for (User user : response.getData()) {
                                                counter++;
                                            }

                                            if(counter == 0){
                                                User user = User.builder()
                                                        .name(username)
                                                        .build();

                                                Amplify.API.mutate(
                                                        ModelMutation.create(user),
                                                        response1 -> Log.i("MyAmplifyApp", "Create Todo with id: " + response1.getData().getId()),
                                                        error -> Log.e("MyAmplifyApp", "Create failed", error)
                                                );
                                            }
                                            Intent nextIntent = new Intent(getBaseContext(), ChatActivity.class);
                                            startActivity(nextIntent);
                                            finish();
                                        },
                                        error -> Log.e("MyAmplifyApp", "Query failure", error)
                                );
                                /*User user = User.builder().name(getIntent().getStringExtra("username").toString()).build();
                                Amplify.API.mutate(
                                        ModelMutation.create(user),
                                        response -> {
                                            Log.i("MyAmplifyApp", "Created User with id: " + response.getData().getId());
                                        },
                                        error -> {Log.e("MyAmplifyApp", "Creation failed", error);
                                        }
                                );*/


                                /*Intent nextIntent = new Intent(getBaseContext(), ChatActivity.class);
                                startActivity(nextIntent);
                                finish();*/
                            }

                        },
                        error -> {
                            Log.e("AuthQuickstart", error.toString());
                            if(error.getRecoverySuggestion().equals("Please confirm user first and then retry operation"))
                            {
                                Intent confirmSignupActivity = new Intent(getBaseContext(), ConfirmSignupActivity.class);
                                confirmSignupActivity.putExtra("username", username);
                                startActivityForResult(confirmSignupActivity, 11);
                            }
                        }
                );
            }
        });

        SignUpView.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getBaseContext(), SignupActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}
