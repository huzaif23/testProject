package com.example.mangotech2.a123ngo;

import android.app.Application;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.example.mangotech2.a123ngo.ExceptionHandler.ExceptionHandler;

public class MyApplication extends Application {

	private RequestQueue mRequestQueue;
	private static MyApplication mInstance;

	@Override
	public void onCreate() {
		Thread.setDefaultUncaughtExceptionHandler(new ExceptionHandler(this));
		super.onCreate();
		mInstance = this;
	}

	public static synchronized MyApplication getInstance() {
		return mInstance;
	}

	public RequestQueue getReqQueue() {
		if (mRequestQueue == null) {
			mRequestQueue = Volley.newRequestQueue(getApplicationContext());
		}

		return mRequestQueue;
	}

	public <T> void addToReqQueue(Request<T> req, String tag) {

		getReqQueue().add(req);
	}

	public <T> void addToReqQueue(Request<T> req) {

		getReqQueue().add(req);
	}

	public void cancelPendingReq(Object tag) {
		if (mRequestQueue != null) {
			mRequestQueue.cancelAll(tag);
		}
	}
}