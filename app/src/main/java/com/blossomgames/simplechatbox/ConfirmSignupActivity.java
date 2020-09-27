package com.blossomgames.simplechatbox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amazonaws.mobileconnectors.cognitoauth.Auth;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.User;

public class ConfirmSignupActivity extends AppCompatActivity {

    private EditText AuthCodeView;
    private EditText Username;
    private Button ConfirmSignupBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmsignup);

        AuthCodeView = findViewById(R.id.authcode);
        Username = findViewById(R.id.confirmusername);

        Username.setText(getIntent().getStringExtra("username"));
        ConfirmSignupBtn = findViewById(R.id.ConfirmSignupBtn);

        String authCode = AuthCodeView.getText().toString();

        ConfirmSignupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Amplify.Auth.confirmSignUp(
                        getIntent().getStringExtra("username").toString(),
                        AuthCodeView.getText().toString(),
                        result -> {
                            Log.i("AuthQuickstart", result.isSignUpComplete() ? "Confirm signUp succeeded" : "Confirm sign up not complete");
                            Intent backIntent = new Intent();
                            backIntent.putExtra("msg", "Done");
                            setResult(10, backIntent);
                            finish();
                        },
                        error -> Log.e("AuthQuickstart", error.toString())
                );


            }
        });
    }
}
