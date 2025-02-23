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
