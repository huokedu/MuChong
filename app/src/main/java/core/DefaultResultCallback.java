package core;

import com.larno.util.okhttp.callback.ResultCallback;

import okhttp3.Request;

/**
 * Created by sks on 2016/5/30.
 */
public abstract class DefaultResultCallback extends ResultCallback {
    protected ActionCallbackListener listener;

    public DefaultResultCallback(ActionCallbackListener listener) {
        this.listener = listener;
    }

    @Override
    public void onError(Request request, Exception e) {
        listener.onFailure(ErrorEvent.SEVER_ERROR,ErrorEvent.SEVER_ERROR_MSG);
    }

}
