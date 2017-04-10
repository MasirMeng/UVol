package com.prepare;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.RequestPasswordResetCallback;
import com.example.uvol.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class FindPass extends Activity{
	
	private Button ackButton;
	private Button cancleBut;
	private EditText userEmail;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.find_password);
        
        ackButton=(Button) findViewById(R.id.ack_email);
        cancleBut=(Button) findViewById(R.id.iv_back);
        userEmail=(EditText) findViewById(R.id.find_email);
        
        ackButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
            	if (userEmail.getText().toString() != null) {
    				AVUser.requestPasswordResetInBackground(userEmail.getText()
    						.toString(), new RequestPasswordResetCallback() {
    					public void done(AVException e) {
    						if (e == null) {
    							Toast.makeText(FindPass.this,
    									"发送成功",
    									Toast.LENGTH_LONG).show();
    							Intent mainIntent = new Intent(FindPass.this,
    									Login.class);
    							startActivity(mainIntent);
    							FindPass.this.finish();
    						} else {
    							if(e.getCode()==205){
    								Dialog alertDialog = new AlertDialog.Builder(FindPass.this)
        			  				.setTitle("提示")
        			  				.setMessage(R.string.error_email_not_found)
        			  				.setNegativeButton(android.R.string.ok,
        			  						new DialogInterface.OnClickListener() {
        			  							public void onClick(DialogInterface dialog,
        			  									int which) {
        			  								dialog.dismiss();
        			  							}
        			  						}).show();
    							}else{
    								Toast.makeText(FindPass.this,
        									"发送失败"+e.getMessage(),
        									Toast.LENGTH_LONG).show();
    							}
    							
    						}
    					}
    				});
    			} else {
    				userEmail.setError(getString(R.string.error_field_required));
    			}
            	
            }
            
            	
        });
        
        
        cancleBut.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
            	Intent intent = new Intent(FindPass.this,Login.class);
                startActivity(intent);
                FindPass.this.finish();
            }
        
	});

    }
}
