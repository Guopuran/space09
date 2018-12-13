package guopuran.bwie.com.space09.util;



import android.os.Handler;
import android.os.Looper;

import com.google.gson.Gson;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;

public class OkUtil {
    private static OkUtil instance;
    private OkHttpClient mClient;
    private Handler handler=new Handler(Looper.getMainLooper());

    //单例
    public static OkUtil getInstance(){
        if (instance==null){
            synchronized (OkUtil.class){
                instance=new OkUtil();
            }
        }
        return instance;
    }
    private OkUtil(){
        HttpLoggingInterceptor interceptor= new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        mClient=new OkHttpClient.Builder()
                .readTimeout(10,TimeUnit.SECONDS)
                .connectTimeout(10,TimeUnit.SECONDS)
                .writeTimeout(10,TimeUnit.SECONDS)
                .addInterceptor(interceptor)
                .build();
    }
    public void getenqueue(String url, final Class clazz, final ICallBack callBack){
        Request request=new Request.Builder()
                .get()
                .url(url)
                .build();
        Call call=mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final Object o = new Gson().fromJson(result, clazz);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.success(o);
                    }
                });
            }
        });
    }
    public void postenqueue(String url, Map<String,String> params, final Class clazz, final ICallBack callBack){
        FormBody.Builder builder=new FormBody.Builder();
        for (Map.Entry<String,String> entry:params.entrySet()){
            builder.add(entry.getKey(),entry.getValue());
        }
        RequestBody body=builder.build();
        Request request=new Request.Builder()
                .post(body)
                .url(url)
                .build();
        Call call=mClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                callBack.failed(e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                final Object o = new Gson().fromJson(result, clazz);
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.success(o);
                    }
                });
            }
        });
    }
}
