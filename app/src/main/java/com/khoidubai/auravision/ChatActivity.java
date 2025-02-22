package com.khoidubai.auravision;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import retrofit2.*;
import retrofit2.converter.gson.GsonConverterFactory;

public class ChatActivity extends AppCompatActivity {
    public RecyclerView chatRecyclerView;
    public ChatAdapter chatAdapter;
    public List<Message> messageList;
    public EditText messageInput;
    public Button sendButton;

    public DatabaseReference chatRef;
    public OpenAIService openAIService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_with_ai);

        chatRecyclerView = findViewById(R.id.chatRecyclerView);
        messageInput = findViewById(R.id.messageInput);
        sendButton = findViewById(R.id.sendButton);

        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList);
        chatRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        chatRecyclerView.setAdapter(chatAdapter);

        // Kết nối Firebase
        chatRef = FirebaseDatabase.getInstance().getReference("chats");

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://openrouter.ai/api/v1/")  // URL chuẩn của OpenRouter
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                .build();
        openAIService = retrofit.create(OpenAIService.class);


        // Tải tin nhắn từ Firebase
        loadChatHistory();

        // Xử lý sự kiện khi bấm gửi tin nhắn
        sendButton.setOnClickListener(view -> sendMessage());
    }

    private void loadChatHistory() {
        chatRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                messageList.clear();

                for (DataSnapshot data : snapshot.getChildren()) {
                    Message message = data.getValue(Message.class);
                    if (message != null) { // Kiểm tra nếu message không null
                        messageList.add(message);
                    }
                }

                chatAdapter.notifyDataSetChanged();

                // Kiểm tra messageList.size() trước khi cuộn RecyclerView
                if (messageList.size() > 0 && chatRecyclerView != null) {
                    chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ChatActivity.this, "Không thể tải tin nhắn!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendMessage() {
        String text = messageInput.getText().toString().trim();
        if (TextUtils.isEmpty(text)) return;

        // Hiển thị tin nhắn của người dùng trên giao diện trước
        Message userMessage = new Message("user", text);
        messageList.add(userMessage);
        chatAdapter.notifyDataSetChanged();
        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);

        messageInput.setText(""); // Xóa input sau khi gửi

        // Gọi AI để phản hồi
        getAIResponse(userMessage);
    }

    private void getAIResponse(Message userMessage) {
        List<Message> messages = new ArrayList<>();
        messages.add(new Message("system", "Bạn là một trợ lý ảo hữu ích."));
        messages.add(userMessage);

        // Đúng model OpenRouter hỗ trợ
        OpenAIRequest request = new OpenAIRequest("gpt-3.5-turbo", messages);

        openAIService.getChatResponse(request).enqueue(new Callback<OpenAIResponse>() {
            @Override
            public void onResponse(Call<OpenAIResponse> call, Response<OpenAIResponse> response) {
                try {
                    Log.d("API_RESPONSE", "Response Code: " + response.code());

                    if (response.isSuccessful() && response.body() != null) {
                        Log.d("API_RESPONSE", "Raw Response: " + new Gson().toJson(response.body()));

                        // Lấy nội dung phản hồi từ OpenRouter
                        String reply = response.body().getChoices().get(0).getMessage().getContent();
                        Message aiMessage = new Message("assistant", reply);

                        messageList.add(aiMessage);
                        chatAdapter.notifyDataSetChanged();
                        chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
                    } else {
                        if (response.errorBody() != null) {
                            Log.e("API_ERROR", "Error Body: " + response.errorBody().string());
                        }
                        Toast.makeText(ChatActivity.this, "Lỗi phản hồi từ AI!", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("API_ERROR", "Error parsing API response", e);
                }
            }

            @Override
            public void onFailure(Call<OpenAIResponse> call, Throwable t) {
                Log.e("API_FAILURE", "API Call Failed", t);
                Toast.makeText(ChatActivity.this, "Lỗi kết nối API!", Toast.LENGTH_SHORT).show();
            }
        });
    }

}
