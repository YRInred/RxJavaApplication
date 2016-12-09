package com.inred.kuaimawork.net;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;
import com.google.gson.stream.JsonWriter;
import com.inred.kuaimawork.BuildConfig;
import com.inred.kuaimawork.MyApplication;
import com.inred.kuaimawork.R;
import com.inred.kuaimawork.entity.ResponseData;
import com.inred.kuaimawork.util.SystemUtils;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import okio.Buffer;
import okio.BufferedSource;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by inred on 2016/10/26.
 */
public class RestApi {

    private String API_URL = "";


    private Gson gson;

    private static RestApi instance;

    private Retrofit retrofit;

    private KuaiMaService kuaiMaService;

    private OkHttpClient okHttpClient;


    public static RestApi getInstance() {
        if (instance == null) {
            synchronized (RestApi.class) {
                if (instance == null) {
                    instance = new RestApi();
                }
            }

        }
        return instance;
    }

    private RestApi() {

        gson = new GsonBuilder().registerTypeAdapter(boolean.class, new TypeAdapter<Boolean>() {
            @Override
            public void write(JsonWriter out, Boolean value) throws IOException {
                if (value == null) {
                    out.nullValue();
                } else {
                    out.value(value);
                }
            }

            @Override
            public Boolean read(JsonReader in) throws IOException {
                JsonToken peek = in.peek();
                switch (peek) {
                    case BOOLEAN:
                        return in.nextBoolean();
                    case NULL:
                        in.nextNull();
                        return null;
                    case NUMBER:
                        return in.nextInt() != 0;
                    case STRING:
                        return Boolean.parseBoolean(in.nextString());
                    default:
                        throw new IllegalStateException("Expected BOOLEAN or NUMBER but was " + peek);
                }
            }
        }).create();

        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        if (!SystemUtils.verifyNetwork(MyApplication.getInstance())) {
                            MyApplication.getInstance().showToast(R.string.network_error);
                        }
                        return chain.proceed(chain.request());
                    }
                })
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response response = chain.proceed(chain.request());

                        ResponseBody responseBody = response.body();
                        BufferedSource source = responseBody.source();
                        source.request(Long.MAX_VALUE);
                        Buffer buffer = source.buffer();

                        Charset charset = Charset.forName("UTF-8");
                        MediaType contentType = responseBody.contentType();
                        if (contentType != null) {
                            charset = contentType.charset(charset);
                        }
                        ResponseData<Object> responseData = gson.fromJson(buffer.clone().readString(charset), new TypeToken<ResponseData<Object>>() {
                        }.getType());
                        return response;
                    }
                });
        if (BuildConfig.DEBUG) {
        builder.addInterceptor(new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
            @Override
            public void log(String message) {
                Log.i("okhttp", message);
            }
        }).setLevel(HttpLoggingInterceptor.Level.BODY));
        }
        okHttpClient = builder.build();

    }


    protected Retrofit getRetrofit() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(API_URL)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
        }
        return retrofit;
    }

    public KuaiMaService kuaiMaService() {
        if (kuaiMaService == null) {
            kuaiMaService = getRetrofit().create(KuaiMaService.class);
        }
        return kuaiMaService;
    }


    private static String bodyToString(RequestBody request) {
        try {
            Buffer buffer = new Buffer();
            request.writeTo(buffer);
            return buffer.readUtf8();
        } catch (IOException e) {
            return "";
        }
    }


}
