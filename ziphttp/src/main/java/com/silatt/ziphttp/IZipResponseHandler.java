package com.silatt.ziphttp;

/**
 * Created by Si Latt on 10/22/2016.
 */
public interface IZipResponseHandler
{
    void handleException(Exception e);
    void handleResponse(byte[] result);
}
