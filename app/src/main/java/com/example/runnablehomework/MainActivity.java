package com.example.runnablehomework;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    // Task 1
    EditText emailEditText;
    EditText passwordEditText;
    TextView resultTextView;
    Button button;

    // Task 2
    Button startCountingButton;
    TextView timeElapsedTextView;
    int currentSeconds = 0;
    boolean isCurrentlyCounting;
    Handler handler;

    // Task 3
    Button thirdTaskStartButton;
    TextView thirdTaskTextView;
    ProgressBar progressBar;

    boolean isLoginSuccessful;
    boolean isDownloadSuccessful;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // TASK 1
        emailEditText = findViewById(R.id.emailEditText);
        passwordEditText = findViewById(R.id.passwordEditText);
        resultTextView = findViewById(R.id.textView);
        button = findViewById(R.id.button);

        resultTextView.setVisibility(View.INVISIBLE);

        button.setOnClickListener((e) -> {
            resultTextView.setVisibility(View.INVISIBLE);
            new Thread(new LoginTask(new LoginTaskValidator())).start();
        });



        // TASK 2
        handler = new Handler();

        timeElapsedTextView = findViewById(R.id.timeElapsedTextView);
        timeElapsedTextView.setText("");
        startCountingButton = findViewById(R.id.startTimeButton);
        startCountingButton.setOnClickListener((e) -> {
                if (!isCurrentlyCounting) {
                    handler.postDelayed(countingRunnable, 1000);
                    startCountingButton.setText("Pause");
                    isCurrentlyCounting = true;
                } else {
                    handler.removeCallbacks(countingRunnable);
                    startCountingButton.setText("Start");
                    isCurrentlyCounting = false;
                }
        });



        // Task 3
        thirdTaskTextView = findViewById(R.id.thirdTaskTextview);
        thirdTaskStartButton = findViewById(R.id.thirdTaskButton);
        progressBar = findViewById(R.id.progressBar);

        thirdTaskTextView.setVisibility(View.INVISIBLE);
        progressBar.setVisibility(View.INVISIBLE);

        thirdTaskStartButton.setOnClickListener((l) -> {
            progressBar.setVisibility(View.VISIBLE);
            thirdTaskTextView.setVisibility(View.INVISIBLE);
            Thread thread1 = new Thread(new DownloadImageTask());
            Thread thread2 = new Thread(new ThirdLoginTask());

            thread1.start();
            thread2.start();

            new Thread(() -> {
                try {
                    thread1.join();
                    thread2.join();
                } catch (InterruptedException e) {
                    runOnUiThread(() -> thirdTaskTextView.setText("Error occurred. Please, try again!"));
                    return;
                }

                if (isDownloadSuccessful && isLoginSuccessful) {
                    runOnUiThread(() ->thirdTaskTextView.setText("Success!"));
                } else {
                    runOnUiThread(() ->thirdTaskTextView.setText("Failed."));
                }

                runOnUiThread(() -> {
                    thirdTaskTextView.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.INVISIBLE);
                });

            }).start();

        });

    }


    // Task 1
    public class LoginTask implements Runnable {

        private LoginTaskValidator validator;

        LoginTask(LoginTaskValidator validator) {
            this.validator = validator;
        }

        @Override
        public void run() {
            boolean isEmailValid = validator.checkEmail(emailEditText.getText().toString());
            boolean isPasswordValid = validator.checkPassword(passwordEditText.getText().toString());

            String result;
            if (isEmailValid && isPasswordValid) {
                result = "Successful login!";
            } else {
                result = "Unsuccessful login, please try again!";
            }

            runOnUiThread(() -> {
                resultTextView.setVisibility(View.VISIBLE);
                resultTextView.setText(result);
            });
        }
    }


    // Task 2
    private Runnable countingRunnable = new Runnable() {
        @Override
        public void run() {
            timeElapsedTextView.setText("Time elapsed : " + ++currentSeconds);
            handler.postDelayed(this, 1000);
        }
    };


    // Task 3
    class DownloadImageTask implements Runnable {
        @Override
        public void run() {
            Random random = new Random(System.currentTimeMillis());
            try {
                Thread.sleep((random.nextInt(4) + 2) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            isDownloadSuccessful = random.nextBoolean();
        }
    }

    class ThirdLoginTask implements Runnable {
        @Override
        public void run() {
            Random random = new Random(System.currentTimeMillis());
            try {
                Thread.sleep((random.nextInt(3) + 3) * 1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }


            isLoginSuccessful = random.nextBoolean();
        }
    }
}
