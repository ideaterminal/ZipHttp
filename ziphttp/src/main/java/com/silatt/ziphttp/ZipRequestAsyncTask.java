package com.silatt.ziphttp;

import android.os.AsyncTask;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;

/**
 * Created by Si Latt on 10/22/2016.
 */
public class ZipRequestAsyncTask extends AsyncTask<Object[], String, byte[]>
{
    private Exception e;
    private HttpURLConnection httpURLConnection;
    private byte[] contentByteArray;
    private IZipResponseHandler zipResponseHandler;

    public ZipRequestAsyncTask(HttpURLConnection httpUrlConnection, byte[] contentByteArray, IZipResponseHandler zipResponseHandler)
    {
        this.httpURLConnection = httpUrlConnection;
        this.contentByteArray = contentByteArray;
        this.zipResponseHandler = zipResponseHandler;
    }

    @Override
    protected byte[] doInBackground(Object[]... objects)
    {
        try
        {
            OutputStream os = httpURLConnection.getOutputStream();
            os.write(contentByteArray);
            os.flush();

            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            InputStream inputStream = httpURLConnection.getInputStream();
            byte[] buffer = new byte[1024];
            int readCount = 0;
            while((readCount = inputStream.read(buffer)) != -1)
            {
                byteArrayOutputStream.write(buffer, 0, readCount);
            }
            byteArrayOutputStream.flush();
            byteArrayOutputStream.close();
            inputStream.close();
            return byteArrayOutputStream.toByteArray();
        }
        catch (Exception e)
        {
            this.e = e;
            return null;
        }
    }

    @Override
    protected void onPostExecute(byte[] responseByteArray)
    {
        if (e != null)
        {
            zipResponseHandler.handleException(e);
        }
        else
        {
            zipResponseHandler.handleResponse(responseByteArray);
        }
    }
}
