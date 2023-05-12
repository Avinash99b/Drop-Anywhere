package com.azstories.dropanywhere;

import android.content.Context;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.CountDownLatch;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class sendpost {

    String result;

    public String Response;

    public String posttext(String text) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder().add("text", text).build();

        Request request = new Request.Builder()
                .url("https://drophere.cloud/appdefault.php")
                .post(requestBody)
                .build();

        final CountDownLatch latch = new CountDownLatch(1); // create the countdown latch
        // Use the OkHttpClient instance to send the request asynchronously
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                // Handle errors here
                latch.countDown(); // count down the latch if an error occurs
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response here
                String responseBody = response.body().string();
                Log.d("TAG", responseBody);
                Response = responseBody;
                latch.countDown(); // count down the latch to signal that the response has arrived

                result = responseBody;
            }
        });
        try {
            latch.await(); // wait for the response to arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return result;
    }


    public String gettexxt(String id) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder().add("id", id).build();

        Request request = new Request.Builder()
                .url("https://drophere.cloud/receiveapp.php")
                .post(requestBody)
                .build();

        final CountDownLatch latch = new CountDownLatch(1); // create the countdown latch

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle errors here
                latch.countDown(); // count down the latch if an error occurs
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response here
                String responseBody = response.body().string();
                Log.d("TAG", responseBody);

                Response = responseBody;
                latch.countDown(); // count down the latch to signal that the response has arrived
            }
        });

        try {
            latch.await(); // wait for the response to arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Response;
    }

    public String gettexxt(String id , String url) {
        OkHttpClient client = new OkHttpClient();

        RequestBody requestBody = new FormBody.Builder().add("id", id).build();

        Request request = new Request.Builder()
                .url("https://drophere.cloud/receivefile.php")
                .post(requestBody)
                .build();

        final CountDownLatch latch = new CountDownLatch(1); // create the countdown latch

        client.newCall(request).enqueue(new Callback() {

            @Override
            public void onFailure(Call call, IOException e) {
                // Handle errors here
                latch.countDown(); // count down the latch if an error occurs
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                // Handle the response here
                String responseBody = response.body().string();
                Log.d("TAG", responseBody);

                Response = responseBody;
                latch.countDown(); // count down the latch to signal that the response has arrived
            }
        });

        try {
            latch.await(); // wait for the response to arrive
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return Response;
    }


}

