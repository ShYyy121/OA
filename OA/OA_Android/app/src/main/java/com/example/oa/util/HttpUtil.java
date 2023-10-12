package com.example.oa.util;


import static com.example.oa.util.Api.BASE_URL;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.oa.App;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class HttpUtil {
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final Gson gson = new Gson();
    private static final String TAG = "HttpUtil";

    public static String toJSON(Object object) {
        return gson.toJson(object);
    }


    public interface OnNetModelListener<T> {
        void success(T data, String message);

        void error(String message);
    }

    public interface OnNetListListener<T> {
        void success(List<T> data, String message);

        void error(String message);
    }

    public interface OnNetStringListener {
        void success(String data, String message);

        void error(String message);
    }

    public static void post(String url, HashMap<String, Object> params, OnNetStringListener listener) {
        post(url, gson.toJson(params), listener);
    }

    public static void post(String url, String json, OnNetStringListener listener) {
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(PersistentCookieJar.six.persistentCookieJar).build();
        RequestBody requestBody = RequestBody.create(json, MediaType.parse("application/json"));
        String fullUrl = BASE_URL + url;
        Request request = new Request.Builder().url(fullUrl).post(requestBody).build();
        Log.d(TAG, "INFO -> [" + fullUrl + "];param -> " + json);
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "ERROR->[" + fullUrl + "]" + e.getLocalizedMessage());
                handler.post(() -> listener.error(e.getLocalizedMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                Log.d(TAG, "SUCCESS->[" + fullUrl + "]" + string);
                handler.post(() -> {
                    try {
                        JSONObject object = new JSONObject(string);
                        int code = object.getInt("code");
                        if (code == 200) {
                            if (object.isNull("data")) {
                                listener.success("", object.getString("msg"));
                            } else {
                                Object data = object.get("data");
                                listener.success(data.toString(), object.getString("msg"));
                            }
                        } else {
                            listener.error(object.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.error("数据解析失败");
                    }
                });
            }
        });
    }

    public static void get(String url, HashMap<String, Object> params, OnNetStringListener listener) {
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(PersistentCookieJar.six.persistentCookieJar).build();
        String fullUrl = BASE_URL + url;
        HttpUrl.Builder builder = HttpUrl.parse(fullUrl).newBuilder();
        Log.d(TAG, "INFO -> [" + fullUrl + "]");
        for (Map.Entry<String, Object> param : params.entrySet()) {
            builder.addQueryParameter(param.getKey(), param.getValue().toString());
            Log.d(TAG, "param -> [" + param.getKey() + ":" + param.getValue() + "]");
        }
        Request request = new Request.Builder().url(builder.build()).get().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "ERROR->[" + fullUrl + "]" + e.getLocalizedMessage());
                if (listener != null) {
                    handler.post(() -> listener.error(e.getLocalizedMessage()));
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                Log.d(TAG, "SUCCESS->[" + fullUrl + "]" + string);
                handler.post(() -> {
                    try {
                        JSONObject object = new JSONObject(string);
                        int code = object.getInt("code");
                        if (code == 200) {
                            if (object.isNull("data")) {
                                listener.success("", object.getString("msg"));
                            } else {
                                Object data = object.get("data");
                                listener.success(data.toString(), object.getString("msg"));
                            }
                        } else {
                            listener.error(object.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.error("数据解析失败");
                    }
                });
            }
        });
    }

    public static void delete(String url, HashMap<String, Object> params, OnNetStringListener listener) {
        OkHttpClient client = new OkHttpClient.Builder().cookieJar(PersistentCookieJar.six.persistentCookieJar).build();
        String fullUrl = BASE_URL + url;
        HttpUrl.Builder builder = HttpUrl.parse(fullUrl).newBuilder();
        Log.d(TAG, "INFO -> [" + fullUrl + "]");
        for (Map.Entry<String, Object> param : params.entrySet()) {
            builder.addQueryParameter(param.getKey(), param.getValue().toString());
            Log.d(TAG, "param -> [" + param.getKey() + ":" + param.getValue() + "]");
        }

        Request request = new Request.Builder().url(builder.build()).tag(fullUrl).delete().build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "ERROR->[" + fullUrl + "]" + e.getLocalizedMessage());
                if (listener != null) {
                    handler.post(() -> listener.error(e.getLocalizedMessage()));
                }
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                Log.d(TAG, "SUCCESS->[" + fullUrl + "]" + string);
                handler.post(() -> {
                    try {
                        JSONObject object = new JSONObject(string);
                        int code = object.getInt("code");
                        if (code == 200) {
                            if (object.isNull("data")) {
                                listener.success("", object.getString("msg"));
                            } else {
                                Object data = object.get("data");
                                listener.success(data.toString(), object.getString("msg"));
                            }
                        } else {
                            listener.error(object.getString("msg"));
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.error("数据解析失败");
                    }
                });
            }
        });
    }

    public static <T> void getModel(String url, HashMap<String, Object> params, Class<T> modelClass, OnNetModelListener<T> listener) {
        OnNetStringListener onNetStringListener = new OnNetStringListener() {
            @Override
            public void success(String data, String message) {
                listener.success(gson.fromJson(data, modelClass), message);
            }

            @Override
            public void error(String error) {
                listener.error(error);
            }
        };
        get(url, params, onNetStringListener);
    }

    public static <T> void getList(String url, HashMap<String, Object> params, Class<T> modelClass, OnNetListListener<T> listener) {
        OnNetStringListener onNetStringListener = new OnNetStringListener() {
            @Override
            public void success(String data, String message) {
                try {
                    JSONArray array = new JSONArray(data);
                    List<T> result = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        result.add(gson.fromJson(array.get(i).toString(), modelClass));
                    }
                    listener.success(result, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.error("数据解析异常");
                }

            }

            @Override
            public void error(String error) {
                listener.error(error);
            }
        };
        get(url, params, onNetStringListener);
    }


    public static <T> void postModel(String url, HashMap<String, Object> params, Class<T> modelClass, OnNetModelListener<T> listener) {
        OnNetStringListener onNetStringListener = new OnNetStringListener() {
            @Override
            public void success(String data, String message) {
                listener.success(gson.fromJson(data, modelClass), message);
            }

            @Override
            public void error(String error) {
                listener.error(error);
            }
        };
        post(url, params, onNetStringListener);
    }
    public static <T> void postModel(String url,String params, Class<T> modelClass, OnNetModelListener<T> listener) {
        OnNetStringListener onNetStringListener = new OnNetStringListener() {
            @Override
            public void success(String data, String message) {
                listener.success(gson.fromJson(data, modelClass), message);
            }

            @Override
            public void error(String error) {
                listener.error(error);
            }
        };
        post(url, params, onNetStringListener);
    }
    public static <T> void postList(String url, HashMap<String, Object> params, Class<T> modelClass, OnNetListListener<T> listener) {
        OnNetStringListener onNetStringListener = new OnNetStringListener() {
            @Override
            public void success(String data, String message) {
                try {
                    JSONArray array = new JSONArray(data);
                    List<T> result = new ArrayList<>();
                    for (int i = 0; i < array.length(); i++) {
                        result.add(gson.fromJson(array.get(i).toString(), modelClass));
                    }
                    listener.success(result, message);
                } catch (JSONException e) {
                    e.printStackTrace();
                    listener.error("数据解析异常");
                }

            }

            @Override
            public void error(String error) {
                listener.error(error);
            }
        };
        post(url, params, onNetStringListener);
    }

    public static void upload(String url, File file, OnNetStringListener listener) {
        OkHttpClient client = new OkHttpClient();
        RequestBody fileBody = RequestBody.create(MediaType.parse("image/*"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", file.getName(), fileBody)
                .build();
        String fullUrl = BASE_URL + url;
        Request request = new Request.Builder().url(fullUrl).post(requestBody).build();
        Log.d(TAG, "INFO -> [" + fullUrl + "];");
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(@NonNull Call call, @NonNull IOException e) {
                Log.d(TAG, "ERROR->[" + fullUrl + "]" + e.getLocalizedMessage());
                handler.post(() -> listener.error(e.getLocalizedMessage()));
            }

            @Override
            public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                String string = response.body().string();
                int statusCode = response.code();
                Log.d(TAG, String.valueOf(statusCode));
                Log.d(TAG, "SUCCESS->[" + fullUrl + "]" + string);
                handler.post(() -> {
                    try {
                        Log.d(TAG, string);
                        JSONObject object = new JSONObject(string);
                        int code = object.getInt("code");
                        if (code == 200) {
                            if (object.isNull("data")) {
                                listener.success("", object.getString("msg"));
                            } else {
                                String data = object.getString("data");
                                listener.success(data.toString(), object.getString("msg"));
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        listener.error("数据解析失败");
                    }
                });
            }
        });
    }

}
