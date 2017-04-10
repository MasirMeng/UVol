package com.prepare;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SignUpCallback;
import com.example.uvol.MainActivity;
import com.example.uvol.R;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) public class Register extends Activity {

    private Button ackButton;
    private Button cancleButton;
    private EditText userName;
    private EditText userPass;
    private EditText userPass2;
    private EditText userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.register);

        ackButton=(Button)findViewById(R.id.zhuce);
        cancleButton=(Button)findViewById(R.id.cancle);
        userName=(EditText)findViewById(R.id.register_Name);
        userPass=(EditText)findViewById(R.id.register_pass);
        userPass2=(EditText)findViewById(R.id.register_PassAgain);
        userEmail=(EditText)findViewById(R.id.register_email);
        

        ackButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
            	attemptRegister();

            }
        });

        cancleButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View view){
                Intent intent = new Intent(Register.this,Login.class);
                startActivity(intent);
                Register.this.finish();
            }
        });
    }
    
    protected void showDialog(String errorMessage) {
		Dialog alertDialog = new AlertDialog.Builder(Register.this)
				.setTitle("提示")
				.setMessage(errorMessage)
				.setNegativeButton(android.R.string.ok,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog,
									int which) {
								dialog.dismiss();
							}
						}).show();
	}
    
    private void checkInfo(String name,String email,String pass,String pass2){
    	if(pass.length()>4){
    		if(!pass.equals(pass2)){
        		showDialog(Register.this
    					.getString(R.string.error_pass_unequal));
        	}else{       		
        		final AVUser user = new AVUser();
                user.setUsername(name);
                user.setPassword(pass);
                user.setEmail(email);
                user.signUpInBackground(new SignUpCallback() {
                  @Override
                  public void done(AVException e) {
                    if (e == null) {
                    	AVObject userHour=new AVObject("UserHour");
                    	userHour.put("userId", user.getObjectId());
                        userHour.saveInBackground();
                        startActivity(new Intent(Register.this, MainActivity.class));
                        Register.this.finish();
                    } else {
                    	switch (e.getCode()) {
    					case 202:
    						showDialog(Register.this
    								.getString(R.string.error_register_user_name_repeat));
    						break;
    					case 203:
    						showDialog(Register.this
    								.getString(R.string.error_register_email_repeat));
    						break;
    					case 125:
    						showDialog(Register.this
    								.getString(R.string.error_email_not_found));
    						break;		
    					default:
    						
    						Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
    						break;
    					}
                      
                    }
                  }
                });
        	}
    		
    	}else{
    		showDialog(Register.this
					.getString(R.string.error_invalid_password));
    	}
    	
    }
    
//    private void chooseType(){
//    	
//    	 final AlertDialog.Builder builder = new AlertDialog.Builder(Register.this);
//         builder.setTitle(R.string.choose_items);
//         final String[] items = {"卫生医疗", "爱心支教", "文化艺术", "生态环境","动物保护",
//        		 "关爱儿童","助学捐款","关爱老人"};
//         final StringBuffer sb = new StringBuffer(100);
//       
//        
//         builder.setMultiChoiceItems(items, null, new DialogInterface.OnMultiChoiceClickListener()
//         {
//             @Override
//             public void onClick(DialogInterface dialog, int which, boolean isChecked)
//             {
//                 if(isChecked)
//                 {
//                     sb.append(items[which] + ", ");
//                 }
//                 
//             }
//         });
//         builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
//         {
//             @Override
//             public void onClick(DialogInterface dialog, int which)
//             {
//            	 dialog.dismiss();
//            	 Toast.makeText(Register.this, "您选择了"+sb.toString(), Toast.LENGTH_SHORT).show();
//            	 startActivity(new Intent(Register.this, MainActivity.class));
//                 Register.this.finish();
//             }
//         });
//         
//         builder.create().show();
//    }
    
    private void attemptRegister() {

        String username = userName.getText().toString();
        String password = userPass.getText().toString();
        String password2=userPass2.getText().toString();
        String useremail=userEmail.getText().toString();
        
        if(!username.isEmpty()){
        	if(!useremail.isEmpty()){
        		if(!password.isEmpty()){
        			if(!password2.isEmpty()){
        				checkInfo(username,useremail,password,password2);
        				
        			}else{
        				showDialog(Register.this
            					.getString(R.string.error_pass2field_required));
        			}
        			
        		}else {
        			showDialog(Register.this
        					.getString(R.string.error_passfield_required));
        		}
        		
        	}else{
        		showDialog(Register.this
					.getString(R.string.error_emailfield_required));
        	}
        	
        }else{
        	showDialog(Register.this
					.getString(R.string.error_namefield_required));
        }
        

      }
}
