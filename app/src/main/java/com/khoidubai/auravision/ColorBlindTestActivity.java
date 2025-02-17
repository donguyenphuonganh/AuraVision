package com.khoidubai.auravision;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class ColorBlindTestActivity extends AppCompatActivity {

    private List<IshiharaQuestion> questions;
    private Set<Integer> askedQuestions = new HashSet<>();

    private int currentQuestionIndex = 0;

    private ImageView questionImage;
    private RadioGroup answerGroup;
    private Button submitButton;
    private TextView resultTextView;

    private List<String> userAnswers = new ArrayList<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.color_blind_test);

        questionImage = findViewById(R.id.questionImage);
        answerGroup = findViewById(R.id.answerGroup);
        submitButton = findViewById(R.id.submitButton);
        resultTextView = findViewById(R.id.resultTextView);

        TextView tvBack = findViewById(R.id.tvBack);
        tvBack.setOnClickListener(view -> finish());

        questions = new ArrayList<>();
        initializeQuestions();

        loadQuestion();

        submitButton.setOnClickListener(v -> {
            int selectedOptionId = answerGroup.getCheckedRadioButtonId();
            if (selectedOptionId == -1) {
                Toast.makeText(ColorBlindTestActivity.this, "Hãy chọn một đáp án!", Toast.LENGTH_SHORT).show();
            } else {
                int selectedAnswerIndex = answerGroup.indexOfChild(findViewById(selectedOptionId));
                IshiharaQuestion currentQuestion = questions.get(currentQuestionIndex);
                String userAnswer = currentQuestion.getAnswers()[selectedAnswerIndex].trim().toLowerCase();
                String result = currentQuestion.getResult(userAnswer);

                userAnswers.add(userAnswer);
                resultTextView.setText("Kết luận: " + result);
                resultTextView.setTextSize(18);
                resultTextView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));
                resultTextView.setPadding(16, 16, 16, 16);

                if (askedQuestions.size() < questions.size()) {
                    int randomIndex;
                    do {
                        randomIndex = new Random().nextInt(questions.size());
                    } while (askedQuestions.contains(randomIndex));

                    askedQuestions.add(randomIndex);
                    currentQuestionIndex = randomIndex;
                    loadQuestion();
                } else {
                    Toast.makeText(ColorBlindTestActivity.this, "Bạn đã hoàn thành tất cả các câu hỏi!", Toast.LENGTH_SHORT).show();
                }
            }
        });
}

        private void initializeQuestions() {
        questions.add(new IshiharaQuestion(R.raw.ishihara8,
                new String[]{"8", "3", "Không nhìn thấy"},
                createResultMap(new String[]{"8", "3", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Mù màu đỏ", "Mù màu toàn bộ"})));

        questions.add(new IshiharaQuestion(R.raw.ishihara29,
                new String[]{"29", "70", "Không nhìn thấy"},
                createResultMap(new String[]{"29", "70", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Mù màu đỏ và xanh lục", "Mù màu toàn bộ"})));

        questions.add(new IshiharaQuestion(R.raw.ishihara3,
                new String[]{"3", "5", "Không nhìn thấy"},
                createResultMap(new String[]{"3", "5", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Mù màu đỏ và xanh lục", "Mù màu toàn bộ"})));

        questions.add(new IshiharaQuestion(R.raw.ishihara5,
                new String[]{"5", "2", "Không nhìn thấy"},
                createResultMap(new String[]{"5", "2", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Mù màu đỏ và xanh lục", "Mù màu toàn bộ"})));

        questions.add(new IshiharaQuestion(R.raw.ishihara15,
                new String[]{"15", "17", "Không nhìn thấy"},
                createResultMap(new String[]{"15", "17", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Mù màu đỏ và xanh lục", "Mù màu toàn bộ"})));

        questions.add(new IshiharaQuestion(R.raw.ishihara74,
                new String[]{"74", "21", "Không nhìn thấy"},
                createResultMap(new String[]{"74", "21", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Mù màu đỏ và xanh lục", "Mù màu toàn bộ"})));

        questions.add(new IshiharaQuestion(R.raw.ishihara6,
                new String[]{"6", "Không nhìn thấy"},
                createResultMap(new String[]{"6", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Phần lớn người mù màu không thể nhìn thấy"})));

        questions.add(new IshiharaQuestion(R.raw.ishihara45,
                new String[]{"45", "Không nhìn thấy"},
                createResultMap(new String[]{"45", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Phần lớn người mù màu không thể nhìn thấy"})));

        questions.add(new IshiharaQuestion(R.raw.i2shihara5,
                new String[]{"5", "Không nhìn thấy"},
                createResultMap(new String[]{"5", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Phần lớn người mù màu không thể nhìn thấy"})));

        questions.add(new IshiharaQuestion(R.raw.i2shihara7,
                new String[]{"7", "Không nhìn thấy"},
                createResultMap(new String[]{"7", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Phần lớn người mù màu không thể nhìn thấy"})));

        questions.add(new IshiharaQuestion(R.raw.i2shihara16,
                new String[]{"16", "Không nhìn thấy"},
                createResultMap(new String[]{"16", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Phần lớn người mù màu không thể nhìn thấy"})));

        questions.add(new IshiharaQuestion(R.raw.i2shihara73,
                new String[]{"73", "Không nhìn thấy"},
                createResultMap(new String[]{"73", "Không nhìn thấy"},
                        new String[]{"Thị giác màu bình thường", "Mù màu toàn bộ"})));

        questions.add(new IshiharaQuestion(R.raw.i3shihara45,
                new String[]{"5", "45", "Không nhìn thấy gì"},
                createResultMap(new String[]{"5", "45", "Không nhìn thấy gì"},
                        new String[]{"Mù màu đỏ và xanh lục", "Mù màu đỏ và xanh lục", "Thị giác màu bình thường"})));

        questions.add(new IshiharaQuestion(R.raw.i4shihara42,
                new String[]{"Nhìn thấy số 42", "Nhìn thấy số 2, số 4 mờ", "Nhìn thấy số 4, số 2 mờ"},
                createResultMap(new String[]{"Nhìn thấy số 42", "Nhìn thấy số 2, số 4 mờ", "Nhìn thấy số 4, số 2 mờ"},
                        new String[]{"Thị giác màu bình thường", "Người bị mù màu đỏ (protanopia) sẽ nhìn thấy số 4 còn người bị mù màu đỏ nhẹ (prontanomaly) có thể nhìn thấy số 4 mờ mờ.", "Người bị mù màu xanh lục (deuteranopia) sẽ nhìn thấy số 4, còn người bị mù màu xanh lục nhẹ (deuteranopia) có thể nhìn thấy số 2 mờ mờ."})));
    }

    private Map<String, String> createResultMap(String[] keys, String[] values) {
        Map<String, String> resultMap = new HashMap<>();
        for (int i = 0; i < keys.length; i++) {
            resultMap.put(keys[i].trim().toLowerCase(), values[i]);
        }
        return resultMap;
    }

    private void loadQuestion() {
        IshiharaQuestion currentQuestion = questions.get(currentQuestionIndex);
        questionImage.setImageResource(currentQuestion.getImageRes());
        answerGroup.removeAllViews();
        for (String answer : currentQuestion.getAnswers()) {
            RadioButton radioButton = new RadioButton(this);
            radioButton.setText(answer);
            answerGroup.addView(radioButton);
        }
        answerGroup.clearCheck();
        resultTextView.setText(""); // Reset kết luận mỗi khi tải câu hỏi mới
    }

    public static class IshiharaQuestion {
        private final int imageRes;
        private final String[] answers;
        private final Map<String, String> results;

        public IshiharaQuestion(int imageRes, String[] answers, Map<String, String> results) {
            this.imageRes = imageRes;
            this.answers = answers;
            this.results = results;
        }

        public int getImageRes() {
            return imageRes;
        }

        public String[] getAnswers() {
            return answers;
        }

        public String getResult(String answer) {
            answer = answer.trim().toLowerCase();
            for (String key : results.keySet()) {
                if (key.equals(answer)) {
                    return results.get(key);
                }
            }
            return "Không xác định";
        }
    }
}
