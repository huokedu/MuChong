package com.larno.util.okhttp.callback;


import okhttp3.Request;

public abstract class ResultCallback
{
    public ResultCallback()
    {
    }


    public void onBefore(Request request)
    {
    }

    public void onAfter()
    {
    }

    public void inProgress(float progress)
    {

    }

    public abstract void onError(Request request, Exception e);

    public abstract void onResponse(String response);


    public static final ResultCallback DEFAULT_RESULT_CALLBACK = new ResultCallback()
    {
        @Override
        public void onError(Request request, Exception e)
        {

        }

        @Override
        public void onResponse(String response)
        {

        }
    };
}