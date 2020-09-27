package com.blossomgames.simplechatbox;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Loadatbackground(this).execute();
    }

    @SuppressLint("StaticFieldLeak")
    class Loadatbackground extends AsyncTask<Object, Void, String> {

        Context context;
        String res;

        Loadatbackground(Context context){
            this.context = context;
        }

        @Override
        protected String doInBackground(Object... objects) {
            try{

                Amplify.Auth.fetchAuthSession(
                        result -> {
                            AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                            switch(cognitoAuthSession.getIdentityId().getType()) {
                                case SUCCESS:
                                    res ="done";
                                    Log.i("AuthQuickStart", "IdentityId: " + cognitoAuthSession.getIdentityId().getValue());
                                    break;
                                case FAILURE:
                                    res = "none";
                                    Log.i("AuthQuickStart", "IdentityId not present because: " + cognitoAuthSession.getIdentityId().getError().toString());
                            }
                        },
                        error -> Log.e("AuthQuickStart", error.toString())
                );
                Thread.sleep(6000);

            }catch(Exception e){
                e.printStackTrace();
            }

            return res;
        }

        @Override
        protected void onPostExecute(String result){
            super.onPostExecute(result);

            Intent nextIntent = null;

            if(result == "done" && Amplify.Auth.getCurrentUser() != null){
                nextIntent = new Intent(context, ChatActivity.class);
            }
            else{
                nextIntent = new Intent(context, SigninActivity.class);
            }
            startActivity(nextIntent);
            finish();
        }
    }

}