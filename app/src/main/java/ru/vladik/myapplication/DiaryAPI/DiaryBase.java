package ru.vladik.myapplication.DiaryAPI;

import android.util.Log;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Nullable;
import org.json.JSONObject;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import ru.vladik.myapplication.DiaryAPI.Util.HttpUtil;

public class DiaryBase {
    private final String token;
    private final String API_URL = "https://api.dnevnik.ru/v2/";
    private final String API_URL_NO_V = "https://dnevnik.ru/api/";
    private final String login, password;
    private final OkHttpClient client;

    public DiaryBase(String login, String password) throws DiaryLoginException {
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        this.login = login;
        this.password = password;
        client = new OkHttpClient.Builder().cookieJar(new JavaNetCookieJar(cookieManager))
                .followRedirects(true)
                .build();
        token = get_token(login, password);
    }

    private boolean jsonIsValid(@NonNull String jsonString) {
        return  (jsonString.startsWith("[")&&jsonString.endsWith("]"))||
                (jsonString.startsWith("{")&&jsonString.endsWith("}"));
    }

    private String get_token(String login, String password) throws DiaryLoginException {
        CompletableFuture<Response> future = CompletableFuture.supplyAsync(() -> {
            HttpUrl url = HttpUtil.createLink("https://login.dnevnik.ru/login/",
                    new HttpUtil.MapWith_p<String, String>()
                            .p("ReturnUrl", "https://login.dnevnik.ru/oauth2?response_type=" +
                                    "token&client_id=bb97b3e445a340b9b9cab4b9ea0dbd6f&scope=" +
                                    "CommonInfo,ContactInfo,FriendsAndRelatives,EducationalInfo")
                            .p("login", login)
                            .p("password", password));
            Request request = new Request.Builder()
                    .url(url.toString())
                    .post(HttpUtil.createFormBody(null))
                    .build();
            try {
                return client.newCall(request).execute();
            } catch (IOException exception) {
                exception.printStackTrace();
            }
            return null;
        });
        try {
            Response response = future.get();
            String responseURL = response.request().url().toString();
            if (responseURL.contains("#access_token=")) {
                String token = responseURL.substring(
                        responseURL.lastIndexOf("#access_token="),
                        responseURL.indexOf("&state=")
                ).replace("#access_token=", "");
                response.close();
                Log.d("main", "Получен токен");
                return token;
            } else {
                throw new DiaryLoginException("Ошибка авторизации");
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new DiaryLoginException(e.getMessage());
        }
    }

    private String get(String url, String method, @Nullable Map<String, String> queryParams,
                       boolean checkJson, boolean refreshCookieOnError) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(HttpUtil.createLink(url + method, queryParams))
                    .addHeader("Access-Token", token)
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                response.close();
                if (checkJson) {
                    if (jsonIsValid(responseString)) {
                        return responseString;
                    } else if (!refreshCookieOnError) {
                        return new JSONObject().toString();
                    } else {
                        try {
                            get_token(login, password);
                            return get(url, method, queryParams, true, false);
                        } catch (DiaryLoginException e) {
                            return new JSONObject().toString();
                        }
                    }
                } else {
                    return responseString;
                }
            } catch (IOException e) {
                Log.d("main", e.getMessage());
                e.printStackTrace();
                return null;
            }
        });
        String resp = null;
        try {
            resp = future.get();
        } catch (ExecutionException | InterruptedException e) {
            Log.d("main", e.getMessage());
        }
        return resp;
    }

    private String post(String url, String method, @Nullable Map<String, String> queryParams,
                        @Nullable Map<String, String> data, boolean checkJson, boolean refreshCookieOnError) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            RequestBody requestBody = HttpUtil.createFormBody(data);
            Request request = new Request.Builder()
                    .url(HttpUtil.createLink(url + method, queryParams))
                    .addHeader("Access-Token", token)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                String responseString = response.body().string();
                response.close();
                if (checkJson) {
                    if (jsonIsValid(responseString)) {
                        return responseString;
                    } else if (!refreshCookieOnError) {
                        return new JSONObject().toString();
                    } else {
                        try {
                            get_token(login, password);
                            Log.d("main", login);
                            return post(url, method, queryParams, data, true, false);
                        } catch (DiaryLoginException e) {
                            return new JSONObject().toString();
                        }
                    }
                } else {
                    return responseString;
                }
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        });
        String resp = null;
        try {
            resp = future.get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return resp;
    }

    public String getWithAPIv2(String method, @Nullable Map<String, String> queryParams, boolean checkJson) {
        return get(API_URL, method, queryParams, checkJson, false);
    }

    public String getWithAPInoV(String method, @Nullable Map<String, String> queryParams,
                                boolean checkJson, boolean refreshCookieOnError) {
        return get(API_URL_NO_V, method, queryParams, checkJson, refreshCookieOnError);
    }

    public String post(String method, @Nullable Map<String, String> queryParams,
                     @Nullable Map<String, String> data, boolean checkJson) {
        return post(API_URL, method, queryParams, data, checkJson, false);
    }

    public String postAPInoV(String method, @Nullable Map<String, String> queryParams,
                       @Nullable Map<String, String> data, boolean checkJson, boolean refreshCookieOnError) {
        return post(API_URL_NO_V, method, queryParams, data, checkJson, refreshCookieOnError);
    }
}
