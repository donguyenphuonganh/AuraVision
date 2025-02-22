package com.khoidubai.auravision;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface OpenAIService {
    @Headers({
            "Content-Type: application/json",
            "Authorization: Bearer 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"
    })
    @POST("chat/completions")
    Call<OpenAIResponse> getChatResponse(@Body OpenAIRequest request);
}
