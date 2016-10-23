package com.silatt.ziphttp_sample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.silatt.ziphttp.IZipResponseHandler;
import com.silatt.ziphttp.ZipRequest;

public class MainActivity extends AppCompatActivity implements IZipResponseHandler {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try
        {
            ZipRequest zipRequest = new ZipRequest("https://httpbin.org/post");
            zipRequest.add("filename.txt", "this is file content", "UTF-8");
            zipRequest.sendRequest(this);
            Log.d("ZipHttp", "Passed");
        } catch (Exception e) {
            Log.d("ZipHttp", "Exception: " + e.getMessage());
        }
    }

    @Override
    public void handleException(Exception e)
    {
        Log.d("ZipHttp", "Response Exception: " + e.getMessage());
    }

    @Override
    public void handleResponse(byte[] result)
    {
        Log.d("ZipHttp", "Response: " + new String(result));
    }
}
