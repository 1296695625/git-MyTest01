
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.http.util.EncodingUtils;

import com.ht.weidiaocha.R;
import com.ht.weidiaocha.application.MyApplication;
import com.ht.weidiaocha.listener.OnTitleBarClickListener;
import com.ht.weidiaocha.task.UpdateApkService;
import com.ht.weidiaocha.utils.AppUtils;
import com.ht.weidiaocha.utils.LogUtils;
import com.ht.weidiaocha.utils.Utils;
import com.ht.weidiaocha.view.CustomTitleBar;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.media.ImageReader;
import android.net.NetworkInfo.DetailedState;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.text.style.UpdateAppearance;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class UpLoadActivity extends Activity implements OnClickListener {
	private CustomTitleBar tileBar;
	private ImageView imagView1,imagView2,imagView3;
	private Button bt;
	private String data ,url,qid="",tid="",uid="",rid="",cmd;
	private int tag=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.up_load_activity);
		MyApplication.getInstance().addActivity(this);
		initActionBar();
		WindowManager manager=(WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		DisplayMetrics metrics=new DisplayMetrics();
		manager.getDefaultDisplay().getMetrics(metrics);;
		imagView1=(ImageView) findViewById(R.id.imagview1);
		imagView2=(ImageView) findViewById(R.id.imagview2);
		imagView3=(ImageView) findViewById(R.id.imagview3);
		imagView1.setAdjustViewBounds(true);
		imagView2.setAdjustViewBounds(true);
		imagView3.setAdjustViewBounds(true);
		imagView1.setMaxHeight(metrics.widthPixels/3);
//		imagView1.setMaxWidth(metrics.widthPixels/3);
		imagView2.setMaxHeight(metrics.widthPixels/3);
		imagView3.setMaxHeight(metrics.widthPixels/3);
		LogUtils.v("details", "width"+metrics.widthPixels);
		list.add("");
		list.add("");
		list.add("");
		list.add("");
		imagView1.setOnClickListener(this);
		imagView2.setOnClickListener(this);
		imagView3.setOnClickListener(this);
		bt=(Button) findViewById(R.id.bt);
		bt.setOnClickListener(this);
		cmd="upload";
		Intent intent=getIntent();
		data=intent.getStringExtra("data");
		url=intent.getStringExtra("url");
		if(null!=data){
			qid=url.substring(url.lastIndexOf("qid=")+4);
			tid=url.substring(url.lastIndexOf("tid=")+4, url.indexOf("&rid"));
			uid=data.substring(data.indexOf("&id=")+4);
			rid=url.substring(url.lastIndexOf("rid=")+4, url.indexOf("&qid"));
		}
	}

	private void initActionBar() {
		// TODO Auto-generated method stub
		tileBar=(CustomTitleBar) findViewById(R.id.upload_titlebar);
		tileBar.setOnLeftClickListener(new OnTitleBarClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				goBack();
			}
		});
	}

	protected void goBack() {
		// TODO Auto-generated method stub
		MyApplication.getInstance().removeActivity(this);
		finish();
		this.overridePendingTransition(R.anim.slide_in_from_left, R.anim.slide_out_to_right);
	}
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if(keyCode==KeyEvent.KEYCODE_BACK){
		goBack();}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.imagview1:
			tag=1;
			createDialog();
			break;
		case R.id.imagview2:
			tag=2;
			createDialog();
			break;
		case R.id.imagview3:	
			tag=3;
			createDialog();
//			new MyDialog(this).getDialog().show();
			break;
		case R.id.bt:
			dialog=new ProgressDialog(UpLoadActivity.this);
			dialog.setTitle("正在上传图片");
			dialog.setCancelable(false);
			dialog.setProgressStyle(DEFAULT_KEYS_DISABLE);
			dialog.setCanceledOnTouchOutside(false);
			dialog.show();
			Toast.makeText(this, "开始上传", Toast.LENGTH_SHORT).show();
				new Thread(new Runnable() {
					@Override
					public void run() {

						// TODO Auto-generated method stub
						sendPost();
	
					}
				}).start();
			
		default:
			break;
		}
	}
	private void createDialog() {
		// TODO Auto-generated method stub
		final CustomDialog dialog=new CustomDialog(this);
		ViewGroup.LayoutParams params=new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		View view=LayoutInflater.from(this).inflate(R.layout.my_dialog, null);
		TextView pai=(TextView) view.findViewById(R.id.pai);
		TextView tuku=(TextView) view.findViewById(R.id.tuku);
		TextView bt=(TextView) view.findViewById(R.id.cancel);
		pai.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				startActivityForResult(intent, 1);
				dialog.dismiss();
			}
		});
		tuku.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent1=new Intent();
				intent1.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
				intent1.setType("image/png");
				intent1.setAction(Intent.ACTION_PICK);
				startActivityForResult(intent1, 2);
				dialog.dismiss();
			}
		});
		bt.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				quXiao(tag);
				dialog.dismiss();
			}
		});
		dialog.setContentView(view);
		dialog.setCanceledOnTouchOutside(false);
		dialog.show();
	}
	protected void quXiao(int tag2) {
		// TODO Auto-generated method stub
		switch (tag2) {
		case 1:
			imagView1.setImageResource(R.drawable.ic_head_default);
			list.set(tag, "");
			break;
		case 2:
			imagView2.setImageResource(R.drawable.ic_head_default);
			list.set(tag, "");
			break;
		case 3:
			imagView3.setImageResource(R.drawable.ic_head_default);
			list.set(tag, "");
			break;
		default:
			break;
		}
	}
	private Handler handler=new Handler(){
			public void handleMessage(android.os.Message msg) {
				switch (msg.what){
				case 1:
					dialog.setCancelable(true);
					dialog.cancel();

//					Toast.makeText(UpLoadActivity.this, "返回信息是ok",Toast.LENGTH_SHORT).show();
					break;
				case 2:
					Toast.makeText(UpLoadActivity.this, "图片转换失败", Toast.LENGTH_SHORT).show();
				default:
					break;
				}
			};
	};
	class CustomDialog extends Dialog{
		public CustomDialog getInstance(){
			return null;}
		public CustomDialog(Context context, int theme) {
			super(context, theme);
			// TODO Auto-generated constructor stub
		}
		public void positiveBt(){
			
		}
		public CustomDialog(Context context) {
			super(context);
			// TODO Auto-generated constructor stub
		}

		protected CustomDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
			super(context, cancelable, cancelListener);
			// TODO Auto-generated constructor stub
		}

		
	}
	private void sendPictures(OutputStream writer){
		StringBuilder b=new StringBuilder();
		String contentType="";
//		LogUtils.v("details", "srcpath.length"+list.size());
		for(int i=1;i<list.size();i++){
			LogUtils.v("details", "for="+list.get(i));
			if(new File(list.get(i)).exists()){
				b.append("--"+boundary+"\r\n");
				b.append("Content-Disposition:form-data;name=\"imgFile"+i+"\";filename=\""+"img_"+i+".png\""+"\r\n");
				
                if (list.get(i).endsWith(".png")||list.get(i).endsWith(".PNG")) {  
                    contentType = "image/png";  
                }else if(list.get(i).endsWith(".jpg")||list.get(i).endsWith(".JPG")){
                	contentType = "image/png";  
                }
                else {  
                    contentType = "application/octet-stream";  
                }  
				b.append("Content-Type:"+contentType+"\r\n\r\n");
				LogUtils.v("details", "图片写入开始前"+b.toString());
				Bitmap bitmap=BitmapFactory.decodeFile(list.get(i));
				Bitmap bit2;
				int width=bitmap.getWidth();
				int height=bitmap.getHeight();
				if(width<=400){
				LogUtils.v("details", "小于400");
					bit2=bitmap;
				}else{
					
					LogUtils.v("details", "缩放比例"+(double)(400.0/width)+"--"+(float)(width/400.0));
					Matrix matrix=new Matrix();
					float bi=(float)(400.0/width);
					if(bi<0.1){
						bi=(float) 0.1;
					}
					matrix.postScale(bi, bi); 
					LogUtils.v("details", "大于400+"+width+height);
					bit2=Bitmap.createBitmap(bitmap, 0, 0, width,height, matrix, false);
					LogUtils.v("details", "已创建bit2");
				}
//				DataInputStream input;
//				String transUrl="";
				try {
					writer.write(b.toString().getBytes());
				    b.append(parseToString(list.get(i)));
//				    if(list.get(i).endsWith(".jpg")){
//				    FileOutputStream wr=new  FileOutputStream(list.get(i).replace(".jpg", "1.png"));
//				    Bitmap bit=BitmapFactory.decodeFile(list.get(i));
//				    bit.compress(Bitmap.CompressFormat.PNG, 50, wr);
//				    wr.flush();wr.close();
					ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
					if(bit2.compress(Bitmap.CompressFormat.PNG, 0, baos)){
						baos.flush();
						baos.close();
					};
					LogUtils.v("details", "baos大小"+baos.size());
					byte[] appicon = baos.toByteArray();
//				    if(new File(list.get(i).replace(".jpg", "1.png")).exists()){
//				    	LogUtils.v("details", "照片是否存在："+new File(list.get(i).replace(".jpg", "1.png")));
//				    	transUrl=list.get(i).replace(".jpg", "1.png");
//				    }else{
//				    	handler.sendEmptyMessage(2);
//				    	LogUtils.v("details", "并没有保存");
//				    	return;
//				    	}
				    writer.write(appicon);
				    if(!bitmap.isRecycled()){
				    	bitmap.recycle();
				    }
				    if(!bit2.isRecycled()){
				    	bit2.recycle();
				    }
//				    }else{
//				    	transUrl=list.get(i);
//				    	input=new DataInputStream(new FileInputStream( new File(transUrl)));
//				    	int len=0;
//				    	byte[] by=new byte[1024];
//				    	while((len=input.read(by))!=-1){
//				    		writer.write(by, 0, len);
//				    	}
//				    	input.close();
//				    }
					writer.write(new String(("\r\n").getBytes(),"utf-8").getBytes());
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		LogUtils.v("details", "sendpictures"+b.toString());
		try {
			writer.write(new String(("--"+boundary+"--\r\n").getBytes(),"utf-8").getBytes());
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	private Bitmap transformImage(String string) {
		// TODO Auto-generated method stub
		Bitmap bitmap=BitmapFactory.decodeFile(string);
		int width=bitmap.getWidth();
		int height=bitmap.getHeight();
		float bi=width/400;
		if(width<=400){
			if(!bitmap.isRecycled()){
			bitmap.recycle();}
			return bitmap;
		}else{
			Matrix matrix=new Matrix();
			Bitmap bitmap2=Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, false);
			if(!bitmap2.isRecycled()){
				bitmap2.recycle();
			}
			return bitmap2;
		}
		
	}

	private String parseToString(String fileUrl){
		ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
		Bitmap bitmap=BitmapFactory.decodeFile(fileUrl);
		bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
	    byte[] appicon = baos.toByteArray();// 转为byte数组
	    bitmap.recycle();
		return Base64.encodeToString(appicon, Base64.DEFAULT);
	}
//	ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
//	Bitmap bitmap=BitmapFactory.decodeFile(list.get(i));
//	bitmap.compress(Bitmap.CompressFormat.JPEG, 0, baos);
//    byte[] appicon = baos.toByteArray();// 转为byte数组
//    b.append(Base64.encodeToString(appicon, Base64.DEFAULT));
	String boundary="====14737809831466499882746641449";
	private void sendPost()  {
		// TODO Auto-generated method stub
		String str="";
		URL url;
		try {
			url = new URL("");
		LogUtils.v("details", "uri+:"+url);
		HttpURLConnection connection= (HttpURLConnection) url.openConnection();
		if(null!=connection){
			LogUtils.v("details", "connection不为空");
		}
		connection.setReadTimeout(10*1000);
		connection.setConnectTimeout(10*1000);
		connection.setDoInput(true);
		connection.setDoOutput(true);
		connection.setDefaultUseCaches(false);
		connection.setRequestMethod("POST");
		connection.setRequestProperty("Charset", "UTF-8");
		connection.setRequestProperty("connection", "keep-alive");
		connection.setRequestProperty("Content-type", "multipart/form-data"+";boundary="+boundary);
		LogUtils.v("details", "type");
//		connection.connect();
		OutputStream writer=new DataOutputStream(connection.getOutputStream());
		StringBuilder sb=new StringBuilder();
		sb.append("\r\n");
		sb.append("--"+boundary+"\r\n");
		sb.append("Content-Disposition: form-data; name=\"cmd\"\r\n\r\n"+cmd+"\r\n");
		sb.append("--"+boundary+"\r\n");
		sb.append("Content-Disposition: form-data; name=\"tid\"\r\n\r\n"+tid+"\r\n");
		sb.append("--"+boundary+"\r\n");
		sb.append("Content-Disposition: form-data; name=\"rid\"\r\n\r\n"+rid+"\r\n");
		sb.append("--"+boundary+"\r\n");
		sb.append("Content-Disposition: form-data; name=\"qid\"\r\n\r\n"+qid+"\r\n");
		sb.append("--"+boundary+"\r\n");
		sb.append("Content-Disposition: form-data; name=\"u_id\"\r\n\r\n"+uid+"\r\n");
		LogUtils.v("details", "发出包信息：。。"+sb.toString().trim());
		writer.write(sb.toString().getBytes());
		sendPictures(writer);
		writer.flush();
		writer.close();
		LogUtils.v("details", "返回信息："+connection.getResponseMessage());
		if(connection.getResponseMessage().trim().contains("OK")){
			handler.sendEmptyMessage(1);
			Intent data=new Intent();
			data.putExtra("qid", qid);
			data.putExtra("tid", tid);
			setResult(DetailActivity.LOADPHOTO,data);
			LogUtils.v("details", "发送成功");
		}else {
			
			LogUtils.v("details", "上传失败");
		}
		connection.disconnect();
		LogUtils.v("details", "上传完成");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			UpLoadActivity.this.finish();
		}
	}
private ProgressDialog dialog;
private String srcPath="";
private List<String> list=new ArrayList<String>(4);
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		if(resultCode==Activity.RESULT_OK){
		switch (requestCode) {
		case 1:
			Bundle bundle=data.getExtras();
			Bitmap bitmap=(Bitmap) bundle.get("data");
			if(tag==1) imagView1.setImageBitmap(bitmap);		saveImage(bitmap,tag);
			if(tag==2) imagView2.setImageBitmap(bitmap);		saveImage(bitmap,tag);
			if(tag==3) imagView3.setImageBitmap(bitmap);		saveImage(bitmap,tag);
			
			Toast.makeText(this, "paizhao", Toast.LENGTH_SHORT).show();
			break;
		case 2:
			Uri uri=data.getData();
			LogUtils.v("details","uri"+uri.toString() );
			ContentResolver resolver=this.getContentResolver();
			Cursor c=resolver.query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
			c.moveToFirst();
			srcPath=c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
			BitmapFactory.Options opts=new BitmapFactory.Options();
			opts.inSampleSize=4;
			Bitmap bitmap2=BitmapFactory.decodeFile(c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)), opts);
			if(tag==1) {imagView1.setImageBitmap(bitmap2);		list.set(tag, c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)));}
			if(tag==2) {imagView2.setImageBitmap(bitmap2);		list.set(tag, c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)));}
			if(tag==3)	{imagView3.setImageBitmap(bitmap2);	list.set(tag, c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)));}
			Toast.makeText(this, "tuku"+srcPath, Toast.LENGTH_SHORT).show();
			c.close();
		default:
			break;
		}
	}
	}

	private void getpath(Uri uri, int tag2) {
		// TODO Auto-generated method stub
		ContentResolver resolver=this.getContentResolver();
		Cursor c=resolver.query(uri, new String[]{MediaStore.Images.Media.DATA}, null, null, null);
		c.moveToFirst();
		srcPath=c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA));
		list.set(tag2, c.getString(c.getColumnIndex(MediaStore.Images.Media.DATA)));
		Toast.makeText(this, "tuku"+srcPath, Toast.LENGTH_SHORT).show();
		c.close();
	}

	private void saveImage(Bitmap bitmap,int tag2) {
		// TODO Auto-generated method stub
		String fileName=Environment.getExternalStorageDirectory().getAbsolutePath()+"/photos/"+tag+".png";
//		srcPath=fileName;
//		list.set(tag2, fileName);
		File imagFile=new File(fileName);
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			if(!imagFile.getParentFile().exists()){
				imagFile.getParentFile().mkdirs();
			}
			srcPath=imagFile.getPath();
			list.set(tag2, fileName);
			try {
				BufferedOutputStream out=new BufferedOutputStream(new FileOutputStream(imagFile));
				bitmap.compress(CompressFormat.JPEG, 30, out);
				out.flush();
				out.close();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}else{
			Toast.makeText(this, "sd卡出问题", Toast.LENGTH_SHORT).show();
		}
	}
	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}
}
