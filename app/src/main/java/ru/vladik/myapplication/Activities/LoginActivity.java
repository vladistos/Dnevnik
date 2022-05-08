package ru.vladik.myapplication.Activities;

import android.app.Activity;
import android.app.NativeActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ru.vladik.myapplication.DiaryAPI.DiaryAPI;
import ru.vladik.myapplication.DiaryAPI.DiaryLoginException;
import ru.vladik.myapplication.R;
import ru.vladik.myapplication.Utils.SharedPreferencesManager;
import ru.vladik.myapplication.Utils.StaticRecourses;

public class LoginActivity extends AppCompatActivity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        FrameLayout loggedCaseFrame = findViewById(R.id.login_load_frame);
        RelativeLayout notLoggedCaseRelative = findViewById(R.id.login_main_relative);
        notLoggedCaseRelative.setVisibility(View.INVISIBLE);
        String[] loginInfo = SharedPreferencesManager.getAccountLoginInfo(this);
        if (loginInfo == null) {
            notLoggedCaseRelative.setVisibility(View.VISIBLE);
            loggedCaseFrame.setVisibility(View.INVISIBLE);
            setLogin();
        } else {
            DiaryAPI.startAsyncTask(() -> {
                try {
                    String login = loginInfo[0];
                    String password = loginInfo[1];
                    StaticRecourses.diaryAPI = new DiaryAPI(login, password);
                    StaticRecourses.UserContext = StaticRecourses.diaryAPI.getContext();
                    StaticRecourses.classmatePersonsList = StaticRecourses.diaryAPI.getGroupPersons(
                            StaticRecourses.UserContext.getGroupIds().get(0)
                    );
                    StaticRecourses.timeTable = StaticRecourses.diaryAPI.getGroupTimeTable(
                            StaticRecourses.UserContext.getGroupIds().get(0)
                    );
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } catch (DiaryLoginException e) {
                    runOnUiThread(() -> {
                        loggedCaseFrame.setVisibility(View.GONE);
                        notLoggedCaseRelative.setVisibility(View.VISIBLE);
                        setLogin();
                    });
                }
            });
        }
    }

    private void setLogin() {
        TextInputLayout passwordInputLayout = findViewById(R.id.password_text_input);
        TextInputEditText loginView = findViewById(R.id.login_edit_text);
        TextInputEditText passwordView = findViewById(R.id.password_edit_text);
        MaterialButton loginButton = findViewById(R.id.login_button);
        loginButton.setOnClickListener((view -> DiaryAPI.startAsyncTask(() -> {
            try {
                String login = loginView.getText() != null ?
                        loginView.getText().toString() : "";
                String password = passwordView.getText() != null ?
                        passwordView.getText().toString() : "";
                if (!login.isEmpty() && !password.isEmpty()) {
                    StaticRecourses.diaryAPI = new DiaryAPI(login, password);
                    StaticRecourses.UserContext = StaticRecourses.diaryAPI.getContext();
                    StaticRecourses.timeTable = StaticRecourses.diaryAPI.getGroupTimeTable(
                            StaticRecourses.UserContext.getGroupIds().get(0)
                    );
                    SharedPreferencesManager.saveAccountLoginInfo(this, login, password);
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } else {
                    runOnUiThread(() -> passwordInputLayout.setError("Поля не должны быть пустыми"));
                }
            } catch (DiaryLoginException e) {
                runOnUiThread(() -> passwordInputLayout.setError("Ошибка авторизации"));
            }
        })));
    }
}
