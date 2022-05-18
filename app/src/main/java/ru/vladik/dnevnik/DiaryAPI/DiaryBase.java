package ru.vladik.dnevnik.DiaryAPI;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import okhttp3.HttpUrl;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import ru.vladik.dnevnik.DiaryAPI.Util.HttpUtil;
import ru.vladik.dnevnik.DiaryAPI.exceptions.APIException;
import ru.vladik.dnevnik.DiaryAPI.exceptions.EmptyJsonStringException;
import ru.vladik.dnevnik.DiaryAPI.exceptions.JsonNotValidException;
import ru.vladik.dnevnik.DiaryAPI.exceptions.NoEthernetException;

public class DiaryBase {
    private final String token;
    private final String API_URL = "https://api.dnevnik.ru/v2/";
    private final String API_V6_MOBILE_URL = "https://api.dnevnik.ru/mobile/v6/";
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

    private void validateJson(String jsonString) throws APIException,
            EmptyJsonStringException, JsonNotValidException {
        if (jsonString == null || jsonString.isEmpty()) {
            throw new EmptyJsonStringException();
        }
        try {
            JsonElement parser = new JsonParser().parse(jsonString);
            if (parser.isJsonObject()) {
                JsonObject o = parser.getAsJsonObject();
                if (o != null && o.get("type") != null
                        && o.get("type").getAsString().toLowerCase(Locale.ROOT).contains("error")) {
                    throw new APIException(parser.getAsJsonObject().get("type").getAsString());
                }
                return;
            } else if (parser.isJsonArray() || parser.isJsonPrimitive() || parser.isJsonNull()) {
                return;
            }
        } catch (JsonIOException | JsonSyntaxException e) {
            throw new JsonNotValidException(e.getMessage());
        }
        throw new JsonNotValidException();
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
            if (response != null) {
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
            } else {
                throw new NoEthernetException();
            }
        } catch (ExecutionException | InterruptedException e) {
            throw new DiaryLoginException(e.getMessage());
        }
    }

    private String get(String url, String method, @Nullable Map<String, String> queryParams) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            Request request = new Request.Builder()
                    .url(HttpUtil.createLink(url + method, queryParams))
                    .addHeader("Access-Token", token)
                    .get()
                    .build();
            try {
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                if (body != null) {
                    String responseString = body.string();
                    response.close();
                    body.close();
                    return responseString;
                } else {
                    throw new NoEthernetException();
                }
            } catch (IOException | NullPointerException e) {
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
                        @Nullable Map<String, String> data) {
        CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> {
            RequestBody requestBody = HttpUtil.createFormBody(data);
            Request request = new Request.Builder()
                    .url(HttpUtil.createLink(url + method, queryParams))
                    .addHeader("Access-Token", token)
                    .post(requestBody)
                    .build();
            try {
                Response response = client.newCall(request).execute();
                ResponseBody body = response.body();
                if (body != null) {
                    String responseString = body.string();
                    response.close();
                    body.close();
                    return responseString;
                } else {
                    throw new NoEthernetException();
                }
            } catch (IOException | NullPointerException e) {
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

    public String getWithAPIv2(String method, @Nullable Map<String, String> queryParams, boolean checkJson)
    throws APIException, EmptyJsonStringException, JsonNotValidException {
        String response = get(API_URL, method, queryParams);
        if (checkJson) {
            validateJson(response);
        }
        return response;
    }

    public String getWithAPIv6Mobile(String method, @Nullable Map<String, String> queryParams, boolean checkJson) {
        String response = get(API_V6_MOBILE_URL, method, queryParams);
        if (checkJson) {
            validateJson(response);
        }
        return response;
    }

    public String getWithAPInoV(String method, @Nullable Map<String, String> queryParams,
                                boolean checkJson) {
        String response = get(API_URL_NO_V, method, queryParams);
        if (checkJson) {
            validateJson(response);
        }
        return response;
    }

    public String post(String method, @Nullable Map<String, String> queryParams,
                     @Nullable Map<String, String> data, boolean checkJson) {
        String response = post(API_URL, method, queryParams, data);
        if (checkJson) {
            validateJson(response);
        }
        return response;
    }

    public String postWithAPIv6Mobile(String method, @Nullable Map<String, String> queryParams,
                       @Nullable Map<String, String> data, boolean checkJson) {
        String response = post(API_V6_MOBILE_URL, method, queryParams, data);
        if (checkJson) {
            validateJson(response);
        }
        return response;
    }

    public String postAPInoV(String method, @Nullable Map<String, String> queryParams,
                       @Nullable Map<String, String> data, boolean checkJson) {
        String response = post(API_URL_NO_V, method, queryParams, data);
        if (checkJson) {
            validateJson(response);
        }
        return response;
    }
}
