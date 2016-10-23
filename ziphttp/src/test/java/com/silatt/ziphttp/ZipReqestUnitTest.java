package com.silatt.ziphttp;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

/**
 * Created by ronga on 10/22/2016.
 */
public class ZipReqestUnitTest
{
    public List<FileEntry> getUnZippedEntries(byte[] byteArray, String encoding) {
        List<FileEntry> contents = new ArrayList<>();
        try
        {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
            ZipInputStream zipInputStream = new ZipInputStream(byteArrayInputStream);

            ZipEntry zipEntry;
            while ((zipEntry = zipInputStream.getNextEntry()) != null)
            {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                for (int c = zipInputStream.read(); c != -1; c = zipInputStream.read()) {
                    byteArrayOutputStream.write(c);
                }
                byteArrayOutputStream.flush();
                byteArrayOutputStream.close();
                zipInputStream.closeEntry();
                byte[] result = byteArrayOutputStream.toByteArray();
                contents.add(new FileEntry(zipEntry.getName(), new String(result, encoding)));
            }
            zipInputStream.close();
        }
        catch (Exception e)
        {
            fail("Should not throw exception: " + e.getMessage());
        }
        return contents;
    }

    @Test
    public void TestStringToZip()
    {
        ZipRequest zipRequest = new ZipRequest("sample url");
        try
        {
            String encoding = "UTF-8";
            String filename1 = "filename.txt";
            String content1 = "some content Привіт";

            String filename2 = "[content].txt";
            String content2 = "simple content 123...";

            zipRequest.add(filename1, content1, encoding);
            zipRequest.add(filename2, content2, encoding);
            byte[] byteArray = zipRequest.getZippedByteArray();

            List<FileEntry> entries = getUnZippedEntries(byteArray, encoding);
            assertEquals("Different filename before and after zip.", filename1, entries.get(0).filename);
            assertEquals("Different file content before and after zip.", content1, entries.get(0).content);
            assertEquals("Different filename before and after zip.", filename2, entries.get(1).filename);
            assertEquals("Different file content before and after zip.", content2, entries.get(1).content);
        }
        catch (Exception e)
        {
        }
    }

    private class FileEntry {
        public String filename;
        public String content;
        public FileEntry(String filename, String content) {
            this.filename = filename;
            this.content = content;
        }
    }

}
