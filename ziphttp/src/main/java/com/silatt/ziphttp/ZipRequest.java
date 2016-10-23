package com.silatt.ziphttp;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.Keep;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by Si Latt on 10/22/2016.
 */
@Keep
public class ZipRequest {

    private String url;
    private HttpURLConnection urlConnection;
    private List<Zippable> entries;

    public ZipRequest(String url)
    {
        this.url = url;
        entries = new ArrayList<>();
    }

    public ZipRequest(URLConnection urlConnection)
    {
        this.urlConnection = (HttpURLConnection) urlConnection;
    }

    public void sendRequest() throws IOException
    {
        sendRequest(null);
    }

    public void sendRequest(IZipResponseHandler responseHandler) throws IOException
    {
        if (urlConnection == null)
        {
            URL u = new URL(url);
            this.urlConnection = (HttpURLConnection) u.openConnection();
            this.urlConnection.setDoInput(true);
            this.urlConnection.setDoOutput(responseHandler != null ? true : false);
            this.urlConnection.setRequestMethod("POST");
            this.urlConnection.setRequestProperty("Content-Type", "application/zip");
        }
        ZipRequestAsyncTask task = new ZipRequestAsyncTask(urlConnection, getZippedByteArray(), responseHandler);
        task.execute();
    }

    public byte[] getZippedByteArray() throws IOException
    {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ZipOutputStream zipOutputStream = new ZipOutputStream(byteArrayOutputStream);
        for (Zippable zippable : entries)
        {
            ZipEntry entry = new ZipEntry(zippable.getFilename());
            zipOutputStream.putNextEntry(entry);
            zipOutputStream.write(zippable.getContent());
            zipOutputStream.closeEntry();
            zipOutputStream.flush();
        }
        zipOutputStream.close();
        byteArrayOutputStream.close();
        return byteArrayOutputStream.toByteArray();
    }

    public void add(String filename, byte[] fileContent)
    {
        entries.add(new Zippable(filename, fileContent));
    }

    public void add(String filename, String fileContent, String encoding) throws UnsupportedEncodingException
    {
        add(filename, fileContent.getBytes(encoding));
    }

    public void add(String filename, File file) throws IOException
    {
        BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(file));
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int bytesRead;
        while((bytesRead = bufferedInputStream.read()) != -1)
        {
            byteArrayOutputStream.write(buffer, 0, bytesRead);
        }
        byteArrayOutputStream.flush();
        byteArrayOutputStream.close();
        bufferedInputStream.close();

        add(filename, byteArrayOutputStream.toByteArray());
    }

    public void add(String filename, Drawable fileContent)
    {
        Bitmap bitmap = ((BitmapDrawable)fileContent).getBitmap();
        add(filename, bitmap);
    }

    public void add(String filename, Bitmap bitmap)
    {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream); // format doesn't matter
        byte[] bitmapData = stream.toByteArray();

        add(filename, bitmapData);
    }

}
