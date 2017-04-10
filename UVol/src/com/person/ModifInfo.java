package com.person;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVUser;
import com.example.uvol.R;

import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.example.uvol.R;
import com.squareup.picasso.Picasso;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class ModifInfo extends Activity{
	
	    private Button backButton;
	    private RelativeLayout nicheng;
	    private RelativeLayout sex;
	    private RelativeLayout phone;
	    private RelativeLayout jianJie;
	    
		private RelativeLayout switchAvatar;
		private ImageView faceImage;
		private TextView userName;
		private TextView userSex;
		private TextView userPhone;
		private TextView userJianjie;
		private byte[] mImageBytes = null;



	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.modify_info);

	        backButton=(Button)findViewById(R.id.iv_back);
	        
	        switchAvatar = (RelativeLayout) findViewById(R.id.tx_xiugai);
	        nicheng=(RelativeLayout) findViewById(R.id.personal_name);
			faceImage = (ImageView) findViewById(R.id.ziliao_photo);
			sex=(RelativeLayout) findViewById(R.id.personal_sex);
			phone=(RelativeLayout) findViewById(R.id.phoneNumber);
			jianJie=(RelativeLayout) findViewById(R.id.jianjie);
			userName=(TextView)findViewById(R.id.personal_username);
			userSex=(TextView)findViewById(R.id.person_sex);
			userPhone=(TextView)findViewById(R.id.personal_phone);
			userJianjie=(TextView)findViewById(R.id.personal_jianjie);
			
			AVUser currentUser = AVUser.getCurrentUser();

			//头像
			if(currentUser.getAVFile("userPhoto") == null){
				Picasso.with(ModifInfo.this).load(R.drawable.default_avatar).into(faceImage);
			}else{
				Picasso.with(ModifInfo.this).load(currentUser.getAVFile("userPhoto").getUrl()).into(faceImage);
			}
			//名字
			userName.setText(currentUser.getString("username"));
			
			//性别
			Boolean sexNumber = currentUser.getBoolean("sex");			
			if(sexNumber == true){
				userSex.setText("男");
			}else if(sexNumber == false){
				userSex.setText("女");
			}
			
			//联系方式
			if(currentUser.getString("mobilePhoneNumber")!=null){
				userPhone.setText(currentUser.getString("mobilePhoneNumber"));				
			}else{
				userPhone.setText("");
			}
			
			//简介
			if(currentUser.getString("brief_introduce")!=null){
				userJianjie.setText(currentUser.getString("brief_introduce"));				
			}else{
				userJianjie.setText("");
			}
						
			// 设置事件监听
			switchAvatar.setOnClickListener(listener);
			
			nicheng.setOnClickListener(new Button.OnClickListener(){
				@Override
	            public  void onClick(View v){
					 final EditText inputnameServer = new EditText(ModifInfo.this);
				        AlertDialog.Builder builder = new AlertDialog.Builder(ModifInfo.this);
				        builder.setTitle("请输入昵称").setView(inputnameServer)
				                .setNegativeButton("取消", null)
				                .setPositiveButton("确认", new DialogInterface.OnClickListener() {

				            public void onClick(DialogInterface dialog, int which) {
				               String newname = inputnameServer.getText().toString();
				               userName.setText(newname);
				    
				               AVUser user = AVUser.getCurrentUser();
				               user.put("username", newname);
				               user.saveInBackground();
				             }
				        });
				        builder.show();
				}

	            });
			

			
			sex.setOnClickListener(new Button.OnClickListener(){
				@Override
	            public  void onClick(View v){
					AlertDialog.Builder builder = new AlertDialog.Builder(ModifInfo.this);
	                builder.setTitle("请选择性别");
	                final String[] sex = {"男", "女"};
	                
	                builder.setSingleChoiceItems(sex, 0, new DialogInterface.OnClickListener()
	                {
	                    @Override
	                    public void onClick(DialogInterface dialog, int which)
	                    {
	                        Toast.makeText(ModifInfo.this,"你选择的性别是：" + sex[which], Toast.LENGTH_SHORT).show();
	                        AVUser user = AVUser.getCurrentUser();
	                    	System.out.println("which:" + which);
	                    	if(which == 0){
	                    		System.out.println("which:" + which);
	                    		userSex.setText(sex[0]);
		                		user.put("sex",true);
		                		user.saveInBackground();
	                    	}
	                    	else if (which == 1){
	                    		System.out.println("which:" + which);
	                    		userSex.setText(sex[1]);
	                    		user.put("sex",false);
	                    		user.saveInBackground();
	                    	}
	                    	dialog.dismiss();
	                    }
	                });
	                builder.show();

	            }
			});
			
			phone.setOnClickListener(new Button.OnClickListener(){
				@Override
	            public  void onClick(View v){
					final EditText inputServer = new EditText(ModifInfo.this);
			        AlertDialog.Builder builder = new AlertDialog.Builder(ModifInfo.this);
			        builder.setTitle("请输入电话号码").setView(inputServer)
			                .setNegativeButton("取消", null);
			        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			            public void onClick(DialogInterface dialog, int which) {
			               String context = inputServer.getText().toString();
			               userPhone.setText(context);
			               
			               //保存到数据库
			               AVUser user = AVUser.getCurrentUser();
			               user.put("mobilePhoneNumber", context);
			               user.saveInBackground();
			             }
			        });
			        builder.show();

	            }
			});
			
			jianJie.setOnClickListener(new Button.OnClickListener(){
				@Override
	            public  void onClick(View v){
					final EditText inputServer = new EditText(ModifInfo.this);
			        AlertDialog.Builder builder = new AlertDialog.Builder(ModifInfo.this);
			        builder.setTitle("请输入内容").setView(inputServer)
			                .setNegativeButton("取消", null);
			        builder.setPositiveButton("确认", new DialogInterface.OnClickListener() {

			            public void onClick(DialogInterface dialog, int which) {
			               String context = inputServer.getText().toString();
			               userJianjie.setText(context);
			               
			               //保存到数据库
			               AVUser user = AVUser.getCurrentUser();
			               user.put("brief_introduce", context);
			               user.saveInBackground();
			             }
			        });
			        builder.show();
	            }
			});
			
			
	        backButton.setOnClickListener(new Button.OnClickListener(){
	            @Override
	            public  void onClick(View v){
	                finish();
	                Intent intent = new Intent(ModifInfo.this,SelfManage.class);
	                startActivity(intent);

	            }
	        });
	    }
	    
	    private View.OnClickListener listener = new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		        intent.setType("image/*");
		        startActivityForResult(intent, 42);
			}
		};
		
		
	    @Override
	    public void onActivityResult(int requestCode, int resultCode, Intent data) {
	      super.onActivityResult(requestCode, resultCode, data);
	      if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
	        try {
	        	faceImage.setImageBitmap(MediaStore.Images.Media.getBitmap(this.getContentResolver(), data.getData()));
	          mImageBytes = getBytes(this.getContentResolver().openInputStream(data.getData()));
	          AVUser user = AVUser.getCurrentUser();
              user.put("userPhoto", new AVFile("userPic", mImageBytes));
              user.saveInBackground();
	        } catch (IOException e) {
	          e.printStackTrace();
	        }
	      }
	    }
	    
	    public byte[] getBytes(InputStream inputStream) throws IOException {
	        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
	        int bufferSize = 1024;
	        byte[] buffer = new byte[bufferSize];
	        int len;
	        while ((len = inputStream.read(buffer)) != -1) {
	          byteArrayOutputStream.write(buffer, 0, len);
	        }
	        return byteArrayOutputStream.toByteArray();
	      }



}
