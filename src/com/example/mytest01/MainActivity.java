package com.example.mytest01;

import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.onekeyshare.ShareContentCustomizeCallback;

import java.io.File;
import java.util.HashMap;

import com.tencent.mm.opensdk.openapi.IWXAPI;
import com.tencent.mm.opensdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


public class MainActivity extends Activity {
	private int a;
	private Button bt;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initWX();
		ShareSDK.initSDK(this);
		bt=(Button) findViewById(R.id.bt);
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
				showShare();
			}
		});
		a=3;
		System.out.println("a的值"+a);
		System.out.println("����onSaveInStanceState����");
		if(null!=savedInstanceState){
			a=savedInstanceState.getInt("a");
			
		}
		System.out.println("a��ֵ��"+a);
	}
	private IWXAPI api;
	private void initWX() {
		// TODO Auto-generated method stub
		Log.v("tag", "init wx");
		final String APP_ID="wx7b36d07fbccce004";
		api=WXAPIFactory.createWXAPI(this, APP_ID, true);
		api.registerApp(APP_ID);
	}
//	3B:1F:10:B8:3E:89:AD:8D:7D:00:F7:FA:B3:57:98:AC数字签名在打完包MD5
//	3b1f10b83e89ad8d7d00f7fab35798ac
	private void showShare() {
		String url=getExternalCacheDir().getAbsolutePath()+"/Photoes/20100909418.jpg";
		File file=new File(url);
		
		// TODO Auto-generated method stub
		OnekeyShare oneShare=new OnekeyShare();
		oneShare.disableSSOWhenAuthorize();
		oneShare.setTitle("title");
		oneShare.setAddress("111111111");
		oneShare.setComment("comment");
		oneShare.setImageUrl("http://img.appgo.cn/imgs/sharesdk/content/2013/07/25/1374723172663.jpg");
		oneShare.setLatitude(100.0f);
		oneShare.setLongitude(200f);
		if(file!= null){
		oneShare.setFilePath(url);
		oneShare.setImagePath(url);
		} else {
			System.out.println("url"+url);
		}
		
		oneShare.setText("text");
		oneShare.setSite("site");
		oneShare.setSiteUrl("siteurl");
		oneShare.setShareToTencentWeiboWhenPerformingQQOrQZoneSharing();
		oneShare.setShareContentCustomizeCallback(new ShareContentCustomizeCallback() {
			
			@Override
			public void onShare(Platform platform, ShareParams paramsToShare) {
				// TODO Auto-generated method stub
				
			}
		});
		oneShare.setCallback(new PlatformActionListener() {
			
			@Override
			public void onError(Platform arg0, int arg1, Throwable arg2) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2) {
				// TODO Auto-generated method stub
				switch ("") {
				case "Wechat":
					//微信好友分享
					break;

				default:
					break;
				}
			}
			
			@Override
			public void onCancel(Platform arg0, int arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		oneShare.show(getApplicationContext());
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		// TODO Auto-generated method stub
		outState.putInt("a", 2);
		super.onSaveInstanceState(outState);
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		if(null!=api){
			api.unregisterApp();
		}
	}

}
