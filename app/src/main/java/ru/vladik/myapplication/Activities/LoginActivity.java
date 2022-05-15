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
import ru.vladik.myapplication.Utils.AsyncUtil;
import ru.vladik.myapplication.Utils.DiarySingleton;
import ru.vladik.myapplication.Utils.SharedPreferencesManager;
import ru.vladik.myapplication.Utils.StaticRecourses;
import ru.vladik.myapplication.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding binding;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        FrameLayout loggedCaseFrame = binding.loginLoadFrame;
        RelativeLayout notLoggedCaseRelativeLayout = binding.loginMainRelative;
        notLoggedCaseRelativeLayout.setVisibility(View.INVISIBLE);
        String[] loginInfo = SharedPreferencesManager.getAccountLoginInfo(this);
        if (loginInfo == null) {
            notLoggedCaseRelativeLayout.setVisibility(View.VISIBLE);
            loggedCaseFrame.setVisibility(View.INVISIBLE);
            setLogin();
        } else {
            AsyncUtil.startAsyncTask(() -> {
                try {
                    String login = loginInfo[0];
                    String password = loginInfo[1];
                    DiaryAPI diaryAPI = new DiaryAPI(login, password);
                    DiarySingleton.init(diaryAPI);
                    StaticRecourses.UserContext = diaryAPI.getContext();
                    StaticRecourses.classmatePersonsList = diaryAPI.getGroupPersons(
                            StaticRecourses.UserContext.getGroupIds().get(0)
                    );
                    StaticRecourses.timeTable = diaryAPI.getGroupTimeTable(
                            StaticRecourses.UserContext.getGroupIds().get(0)
                    );
                    startActivity(new Intent(this, MainActivity.class));
                    finish();
                } catch (DiaryLoginException e) {
                    AsyncUtil.executeInMain(() -> {
                        loggedCaseFrame.setVisibility(View.GONE);
                        notLoggedCaseRelativeLayout.setVisibility(View.VISIBLE);
                        setLogin();
                    });
                }
            });
        }
    }

    private void setLogin() {
        TextInputLayout passwordInputLayout = binding.passwordTextInput;
        TextInputEditText loginView = binding.loginEditText;
        TextInputEditText passwordView = binding.passwordEditText;
        MaterialButton loginButton = binding.loginButton;
        loginButton.setOnClickListener(view -> AsyncUtil.startAsyncTask(() -> {
            try {
                String login = loginView.getText() != null ?
                        loginView.getText().toString() : "";
                String password = passwordView.getText() != null ?
                        passwordView.getText().toString() : "";
                if (!login.isEmpty() && !password.isEmpty()) {
                    DiaryAPI diaryAPI = new DiaryAPI(login, password);
                    DiarySingleton.init(diaryAPI);
                    StaticRecourses.UserContext = diaryAPI.getContext();
                    StaticRecourses.classmatePersonsList = diaryAPI.getGroupPersons(
                            StaticRecourses.UserContext.getGroupIds().get(0)
                    );
                    StaticRecourses.timeTable = diaryAPI.getGroupTimeTable(
                            StaticRecourses.UserContext.getGroupIds().get(0)
                    );
                    SharedPreferencesManager.saveAccountLoginInfo(this, login, password);
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    AsyncUtil.executeInMain(() -> passwordInputLayout.setError("Поля не должны быть пустыми"));
                }
            } catch (DiaryLoginException e) {
                AsyncUtil.executeInMain(() -> passwordInputLayout.setError("Ошибка авторизации"));
            }
        }));
    }
}
