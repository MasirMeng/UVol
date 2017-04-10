package com.person;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.avos.avoscloud.AVUser;
import com.example.uvol.R;
import com.prepare.Login;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;

public class SelfManage extends Activity{
	
	 private Button backButton;
	 private View personInfo;
	 private View offLine;
	 private ImageView userPhoto;
	 private TextView userName;

	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.account_manage);

	        
	        backButton=(Button)findViewById(R.id.iv_back);
	        personInfo=(View)findViewById(R.id.bianji_ziliao);
	        offLine=(View)findViewById(R.id.tuichu);
	        userPhoto=(ImageView)findViewById(R.id.ziliao_photo);
	        userName=(TextView)findViewById(R.id.ziliao_name);
	        
	        AVUser currentUser = AVUser.getCurrentUser();

			//Í·Ïñ
			if(currentUser.getAVFile("userPhoto") == null){
				Picasso.with(SelfManage.this).load(R.drawable.default_avatar).into(userPhoto);
			}else{
				Picasso.with(SelfManage.this).load(currentUser.getAVFile("userPhoto").getUrl()).into(userPhoto);
			}
			//Ãû×Ö
			userName.setText(currentUser.getString("username"));
	        
	      //·µ»Ø
	        backButton.setOnClickListener(new Button.OnClickListener(){
	            @Override
	            public  void onClick(View v){
	                finish();

	            }
	        });
	        
	        personInfo.setOnClickListener(new OnClickListener(){
	        	 @Override
		         public  void onClick(View v){
		                Intent intent = new Intent(SelfManage.this,ModifInfo.class);
		                startActivity(intent);
		                SelfManage.this.finish();

		         }
	        });
	        
	        offLine.setOnClickListener(new OnClickListener(){
	        	 @Override
		         public  void onClick(View v){
	        		final Dialog dialog = new Dialog(SelfManage.this);
	 				dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
	 				dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
	 				dialog.setContentView(R.layout.dialog_offline);
	 				dialog.findViewById(R.id.ok).setOnClickListener(new OnClickListener() {
	 					@Override
	 					public void onClick(View v) {
	 						dialog.dismiss();
	 						Logout();
	 					}

	 					private void Logout() {
	 						AVUser.logOut();
	 						Intent mainIntent = new Intent(SelfManage.this,
	 								Login.class);
	 						startActivity(mainIntent);
	 						SelfManage.this.finish();
	 					}
	 				});
	 				
	 				dialog.findViewById(R.id.cancle).setOnClickListener(new OnClickListener() {
						@Override
						public void onClick(View v) {
							dialog.dismiss();
						}
					});
					dialog.show();

		         }
	        });
	    
	    
	    
	    }

}
