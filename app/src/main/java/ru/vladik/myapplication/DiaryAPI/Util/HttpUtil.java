package ru.vladik.myapplication.DiaryAPI.Util;

import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

import okhttp3.FormBody;
import okhttp3.HttpUrl;

public class HttpUtil {
    public static HttpUrl createLink(String url, Map<String, String> params) {
        HttpUrl.Builder builder = HttpUrl.parse(url).newBuilder();
        if (params != null) {
            params.forEach(builder::addQueryParameter);
        }
        return builder.build();
    }

    public static FormBody createFormBody(@Nullable Map<String, String> params) {
        FormBody.Builder builder = new FormBody.Builder();
        if (params != null) {
            params.forEach(builder::add);
        }
        return builder.build();
    }

    public static class MapWith_p<K, V> extends HashMap<K, V> {
        public MapWith_p<K, V> p(K k, V v) {
            this.put(k, v);
            return this;
        }
    }
}
