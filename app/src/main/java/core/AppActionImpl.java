package core;

import android.content.Context;

import com.larno.util.CacheUtil;

import api.Api;
import api.ApiImpl;


/**
 * AppAction接口的实现类
 *
 */
public class AppActionImpl implements AppAction {
    private Context context;
    private Api api;
    private CacheUtil cacheUtil;

    public AppActionImpl(Context context) {
        this.context = context;
        this.api = new ApiImpl();
        this.cacheUtil = new CacheUtil(context);
    }

}
