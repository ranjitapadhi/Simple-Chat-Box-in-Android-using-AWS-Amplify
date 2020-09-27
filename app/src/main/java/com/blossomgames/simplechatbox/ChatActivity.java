package com.blossomgames.simplechatbox;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.ApiOperation;
import com.amplifyframework.api.aws.GsonVariablesSerializer;
import com.amplifyframework.api.graphql.GraphQLRequest;
import com.amplifyframework.api.graphql.SimpleGraphQLRequest;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelPagination;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.api.graphql.model.ModelSubscription;
import com.amplifyframework.auth.options.AuthSignOutOptions;
import com.amplifyframework.core.Action;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.Where;
import com.amplifyframework.datastore.generated.model.Message;
import com.amplifyframework.datastore.generated.model.User;


import java.util.ArrayList;
import java.util.Collections;

public class ChatActivity extends AppCompatActivity {

    private RecyclerView mMessageRecycler;
    private MessageListAdapter mMessageAdapter;
    private ArrayList<RecyclerData> messageList = new ArrayList<>();

    private Button sendButton;
    private EditText ChatBoxInputField;


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat);

        sendButton = findViewById(R.id.button_chatbox_send);
        ChatBoxInputField = findViewById(R.id.edittext_chatbox);

        mMessageRecycler = (RecyclerView) findViewById(R.id.reyclerview_message_list);
        mMessageAdapter = new MessageListAdapter(this, messageList);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mMessageRecycler.setLayoutManager(layoutManager);
    }

    @Override
    protected void onStart(){
        super.onStart();

        messageList.clear();

        //FetchMessages();
        ObserveMessages();

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                /*RecyclerData rd = new RecyclerData();
                rd.setSender(Amplify.Auth.getCurrentUser().getUsername());
                rd.setMessage(ChatBoxInputField.getText().toString());
                messageList.add(rd);*/

                Message post = Message.builder()
                        .user(Amplify.Auth.getCurrentUser().getUsername())
                        .text(ChatBoxInputField.getText().toString())
                        .build();

                Amplify.DataStore.save(post,
                        saved -> Log.i("MyAmplifyApp", "Saved a post."),
                        failure -> Log.e("MyAmplifyApp", "Save failed.", failure)
                );

                new Thread() {
                    public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        //mMessageAdapter.AddList(messageList);
                                        //mMessageAdapter.notifyData(messageList);
                                        //mMessageRecycler.setAdapter(mMessageAdapter);
                                    }
                                });
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }

                    }
                }.start();
                ChatBoxInputField.setText("");
            }
        });
    }

    private void ObserveMessages(){
        /*Amplify.DataStore.observe(Message.class,
                cancelable -> Log.i("MyAmplifyApp", "Observation began."),
                postChanged -> {
                    Message msg = postChanged.item();
                    Log.i("MyAmplifyApp", "Subscription Messages: " + msg);
                    RecyclerData rd = new RecyclerData();
                    rd.setSender(msg.getUser().toString());
                    rd.setMessage(msg.getText().toString());


                    new Thread() {
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messageList.add(rd);
                                        //mMessageAdapter.AddList(messageList);
                                        mMessageAdapter.notifyData(messageList);
                                        mMessageRecycler.setAdapter(mMessageAdapter);
                                        //FetchMessages();
                                    }
                                });
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();
                },
                failure -> Log.e("MyAmplifyApp", "Observation failed.", failure),
                () -> Log.i("MyAmplifyApp", "Observation complete.")
        );*/


        ApiOperation subscription = Amplify.API.subscribe(
                ModelSubscription.onCreate(Message.class),
                onEstablished -> Log.i("ApiQuickStart", "Subscription established"),
                onCreated ->{
                    Log.i("ApiQuickStart", "Todo create subscription received: " + ((Message) onCreated.getData()).getText());

                    RecyclerData rd = new RecyclerData();
                    rd.setSender(onCreated.getData().getUser().toString());
                    rd.setMessage(onCreated.getData().getText().toString());


                    new Thread() {
                        public void run() {
                            try {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        messageList.add(rd);
                                        //mMessageAdapter.AddList(messageList);
                                        mMessageAdapter.notifyData(messageList);
                                        mMessageRecycler.setAdapter(mMessageAdapter);
                                        //FetchMessages();
                                    }
                                });
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                    },
                onFailure -> Log.e("ApiQuickStart", "Subscription failed", onFailure),
                () -> Log.i("ApiQuickStart", "Subscription completed")
        );


    }

    private void FetchMessages(){

        Amplify.API.query(
                ModelQuery.list(Message.class, ModelPagination.limit(1_50)),
                response -> {
                    for (Message todo : response.getData()) {
                        Log.i("MyAmplifyApp", todo.getText());


                        RecyclerData rd = new RecyclerData();
                        rd.setSender(todo.getUser().toString());
                        rd.setMessage(todo.getText().toString());


                        new Thread() {
                            public void run() {
                                try {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            messageList.add(rd);
                                            //mMessageAdapter.AddList(messageList);
                                            mMessageAdapter.notifyData(messageList);
                                            mMessageRecycler.setAdapter(mMessageAdapter);
                                            //FetchMessages();
                                        }
                                    });
                                    Thread.sleep(1000);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        }.start();
                    }
                },
                error -> Log.e("MyAmplifyApp", "Query failure", error)
        );

        /*Amplify.DataStore.query(Message.class, Where.sorted(Message.TEXT.ascending()),
                allMessages -> {
                        while(allMessages.hasNext()){
                        Message msg = allMessages.next();
                        Log.e("MyAmplifyApp", "Query Fetched : "+ msg.getText().toString());

                            RecyclerData rd = new RecyclerData();
                            rd.setSender(msg.getUser().toString());
                            rd.setMessage(msg.getText().toString());

                            new Thread() {
                                public void run() {
                                    try {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                messageList.add(rd);
                                                //mMessageAdapter.AddList(messageList);
                                                mMessageAdapter.notifyData(messageList);
                                                mMessageRecycler.setAdapter(mMessageAdapter);
                                            }
                                        });
                                        Thread.sleep(1000);
                                    } catch (InterruptedException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }.start();
                    }
                },
                failure -> Log.e("MyAmplifyApp", "Query Failed", failure));*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.sign_out) {
            Amplify.Auth.signOut(
                    AuthSignOutOptions.builder().globalSignOut(true).build(),
                    () -> {
                        Amplify.DataStore.clear(() -> Log.i("MyAmplifyApp", "DataStore is Cleared"),
                                failure -> Log.e("MyAmplifyApp", "Failed to Clear"));
                        Log.i("AuthQuickstart", "Signed out globally");
                        Intent signinActivity = new Intent(getBaseContext(), SigninActivity.class);
                        startActivity(signinActivity);
                        finish();
                    },
                    error -> Log.e("AuthQuickstart", error.toString())
            );
        }

        return super.onOptionsItemSelected(item);
    }
}
