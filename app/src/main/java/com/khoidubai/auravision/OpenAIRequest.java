package com.khoidubai.auravision;

import java.util.List;

public class OpenAIRequest {
    private String model;
    private List<Message> messages;

    public OpenAIRequest(String model, List<Message> messages) {
        this.model = model;
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public List<Message> getMessages() {
        return messages;
    }
}
