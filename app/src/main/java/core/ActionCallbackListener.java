package core;

/**
 * Action的处理结果回调监听器
 *
 *
 */
public interface ActionCallbackListener<T> {
    /**
     * 成功时调用
     *
     * @param data 返回的数据
     */
    void onSuccess(T data);

    /**
     * 失败时调用
     *
     * @param errorEvent 错误码
     * @param message    错误信息
     */
    void onFailure(String errorEvent, String message);
}
