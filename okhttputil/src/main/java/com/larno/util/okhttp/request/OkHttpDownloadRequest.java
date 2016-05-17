package com.larno.util.okhttp.request;

import com.larno.util.okhttp.Log;
import com.larno.util.okhttp.OkHttpClientManager;
import com.larno.util.okhttp.callback.ResultCallback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by zhy on 15/11/6.
 */
public class OkHttpDownloadRequest extends OkHttpGetRequest {
    private String destFileDir;
    private String destFileName;


    protected OkHttpDownloadRequest(
            String url, Object tag, Map<String,
            String> params, Map<String, String> headers,
            String destFileName, String destFileDir) {
        super(url, tag, params, headers,null);
        this.destFileName = destFileName;
        this.destFileDir = destFileDir;
    }


    @Override
    public void invokeAsyn(final ResultCallback callback) {
        prepareInvoked(callback);

        final Call call = mOkHttpClient.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mOkHttpClientManager.sendFailResultCallback(request, e, callback);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    String filePath = saveFile(response, callback);
                    OkHttpClientManager.getInstance().sendSuccessResultCallback(filePath, callback);
                } catch (IOException e) {
                    e.printStackTrace();
                    OkHttpClientManager.getInstance().sendFailResultCallback(response.request(), e, callback);
                }
            }
        });

    }

    private String getFileName(String path) {
        int separatorIndex = path.lastIndexOf("/");
        return (separatorIndex < 0) ? path : path.substring(separatorIndex + 1, path.length());
    }

    @Override
    public String invoke() throws IOException {
        final Call call = mOkHttpClient.newCall(request);
        Response response = call.execute();
        return saveFile(response, null);
    }

    public String saveFile(Response response, final ResultCallback callback) throws IOException {
        InputStream is = null;
        byte[] buf = new byte[2048];
        int len = 0;
        FileOutputStream fos = null;
        try {
            is = response.body().byteStream();
            final long total = response.body().contentLength();
            long sum = 0;

            Log.e(total + "");

            File dir = new File(destFileDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File file = new File(dir, destFileName);
            fos = new FileOutputStream(file);
            while ((len = is.read(buf)) != -1) {
                sum += len;
                fos.write(buf, 0, len);

                if (callback != null) {
                    final long finalSum = sum;
                    mOkHttpClientManager.getDelivery().post(new Runnable() {
                        @Override
                        public void run() {
                            callback.inProgress(finalSum * 1.0f / total);
                        }
                    });
                }
            }
            fos.flush();

            return file.getAbsolutePath();

        } finally {
            try {
                if (is != null) is.close();
            } catch (IOException e) {
            }
            try {
                if (fos != null) fos.close();
            } catch (IOException e) {
            }

        }
    }


}
