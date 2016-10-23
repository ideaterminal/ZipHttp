package com.silatt.ziphttp;

/**
 * Created by Si Latt on 10/22/2016.
 */
class Zippable {
    private String filename = "";
    private byte[] bytes;

    public Zippable(String filename, byte[] bytes)
    {
        this.filename = filename;
        this.bytes = bytes;
    }

    public String getFilename()
    {
        return this.filename;
    }

    public byte[] getContent()
    {
        return bytes;
    }
}
