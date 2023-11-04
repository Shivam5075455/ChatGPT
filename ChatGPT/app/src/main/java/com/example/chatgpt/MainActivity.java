package com.example.chatgpt;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;


import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {


    EditText etMessage;
    ImageView imgSend;
    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    List<MessageModel> messageModelList;

    public static final MediaType JSON = MediaType.get("applicaton/json; charset=utf-8");
    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .readTimeout(120, TimeUnit.SECONDS)
            .build();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);


        etMessage = findViewById(R.id.etMessage);
        imgSend = findViewById(R.id.imgSend);

        messageModelList = new ArrayList<>();

        messageAdapter = new MessageAdapter(messageModelList);
        recyclerView.setAdapter(messageAdapter);

        imgSend.setOnClickListener(view->{
            String message = etMessage.getText().toString();
            if(!message.isEmpty()){
                messageModelList.add(new MessageModel(message,MessageModel.SEND_BY_USER));
                messageAdapter.notifyDataSetChanged();
                etMessage.setText("");
                callChatGPTApi();
            }
        });
    }//onCreate

    public void callChatGPTApi(){
        messageModelList.add(new MessageModel("Thinking...", MessageModel.SEND_BY_BOT));
        JSONObject jsonObject = new JSONObject();

        try{
            jsonObject.put("model", "gpt-3.5-turbo");
            JSONArray jsonArray = new JSONArray();
            JSONObject jsonObject1 = new JSONObject();
            jsonObject1.put("role","system");
            jsonObject1.put("content","You are a helpful assistant.");
            jsonObject1.put("role", "user");
            jsonObject1.put("content", messageModelList);
            jsonArray.put(jsonObject1);
//            send user message to openAi in the form of array
            jsonObject.put("messages", jsonArray);
        }catch (Exception e){
            e.printStackTrace();
        }
//        Send Request to openAi
        RequestBody requestBody = RequestBody.create(jsonObject.toString(), JSON);
        Request request = new Request.Builder()
                .url("https://api.openai.com/v1/chat/completions")
                .addHeader("Authorization", "Bearer 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")
                .post(requestBody)
                .build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                addToChat("Something went wrong! "+e.getMessage());
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
//                after getting this response we need to send it on the main thread
                if(response.isSuccessful()){
                    String textResponse = response.toString();
                    try {
                        JSONObject jsonObject2 = new JSONObject(textResponse);
//                        get actual value i.e., response from bot
                        String botResponse = jsonObject2.getJSONArray("choices").getJSONObject(0).getJSONObject("message").getString("content");
//                        set response on UI thread
                        addToChat(botResponse.trim());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }else{
                    addToChat("Something went wrong! "+response.message());
                }
            }
        });
    }

//    set this response on the UI thread
    @SuppressLint("NotifyDataSetChanged")
    private void addToChat(String msg) {
        messageModelList.remove(messageModelList.size()-1);
        runOnUiThread(()->{
            messageModelList.add(new MessageModel(msg,MessageModel.SEND_BY_BOT));
            messageAdapter.notifyDataSetChanged();
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount());
        });

    }

}