package com.larno.util;

import android.content.Context;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;

/**
 * Created by sks on 2016/5/13.
 */
public class CacheUtil {
    private final File cacheFileDir;

    public CacheUtil(Context context) {
        cacheFileDir = context.getExternalCacheDir().getAbsoluteFile();
        if (cacheFileDir.exists()) {
            cacheFileDir.mkdirs();
        }
    }

    /**
     * 缓存 指定url，指定请求参数request，的响应数据（文本形式）response
     * @param url
     * @param request
     * @param response
     */
    public void put(String url, String request, String response) {
        String filename = EncryptUtil.makeMD5(url + request);
        File file = new File(cacheFileDir, filename);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file, false));
            writer.write(response);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 根据 指定url，指定请求参数request，获取缓存（文本形式）response
     * @param url
     * @param request
     * @return
     */
    public String get(String url, String request){
        String filename = EncryptUtil.makeMD5(url + request);
        File file = new File(cacheFileDir, filename);
        if (!file.exists()) {
            return null;
        }
        Reader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            char[] chars = new char[1024];
            int len = 0;
            StringBuilder builder = new StringBuilder();
            while ((len = reader.read(chars))!=-1){
                builder.append(chars,0,len);
            }
            return builder.toString();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

}
