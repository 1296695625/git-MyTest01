package com.example.mytest01;

import org.apache.http.HttpEntity;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.HttpClientParams;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpParams;

public class HttpClientUrl {
		public HttpClientUrl(){}
		public void sendGet(){
			HttpClient client=new DefaultHttpClient();
			HttpPost post=new HttpPost("");
			HttpParams params=new MyHttpParams();
			params.setParameter("", "");
			
//			HttpEntity entity=
		}
		class MyHttpParams implements HttpParams{

			@Override
			public HttpParams copy() {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean getBooleanParameter(String name, boolean defaultValue) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public double getDoubleParameter(String name, double defaultValue) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public int getIntParameter(String name, int defaultValue) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public long getLongParameter(String name, long defaultValue) {
				// TODO Auto-generated method stub
				return 0;
			}

			@Override
			public Object getParameter(String name) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public boolean isParameterFalse(String name) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean isParameterTrue(String name) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public boolean removeParameter(String name) {
				// TODO Auto-generated method stub
				return false;
			}

			@Override
			public HttpParams setBooleanParameter(String name, boolean value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public HttpParams setDoubleParameter(String name, double value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public HttpParams setIntParameter(String name, int value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public HttpParams setLongParameter(String name, long value) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public HttpParams setParameter(String name, Object value) {
				// TODO Auto-generated method stub
				return null;
			}}
}
