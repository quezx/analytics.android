package com.quezx.analytics.listener;

@SuppressWarnings("unchecked")
public interface FunctionCallBack<T> {
	void onFunctionCall(T... object);
}
