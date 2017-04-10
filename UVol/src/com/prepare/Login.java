package com.prepare;


import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.LogInCallback;
import com.example.uvol.MainActivity;
import com.example.uvol.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
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

public class Login extends Activity {

	private EditText userName;
	private EditText userPass;
    private Button logButton;
    private Button registerButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.login);
        
        if (AVUser.getCurrentUser() != null) {
            startActivity(new Intent(Login.this, MainActivity.class));
            Login.this.finish();
          }

        userName=(EditText)findViewById(R.id.accountEt);
        userPass=(EditText)findViewById(R.id.pwdEt);
        logButton=(Button) findViewById(R.id.subBtn);
        registerButton=(Button)findViewById(R.id.regBtn);
        
        //回车登陆
        userPass.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
              if (id == EditorInfo.IME_NULL && keyEvent.getAction() == KeyEvent.ACTION_UP) {
                attemptLogin();
                return true;
              }
              return false;
            }
          });
        

        logButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
            	attemptLogin();

            }

        });

        registerButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
            	Intent intent = new Intent(Login.this,Register.class);
				startActivity(intent);
				Login.this.finish();

            }

        });


    }
    
    private void attemptLogin() {
        userName.setError(null);
        userPass.setError(null);

        final String username = userName.getText().toString();
        final String password = userPass.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if (!TextUtils.isEmpty(password) && !isPasswordValid(password)) {
          userPass.setError(getString(R.string.error_invalid_password));
          focusView = userPass;
          cancel = true;
        }

        if (TextUtils.isEmpty(username)) {
          userName.setError(getString(R.string.error_field_required));
          focusView = userName;
          cancel = true;
        }
        
        if(TextUtils.isEmpty(password)){
        	userPass.setError(getString(R.string.error_field_required));
            focusView = userPass;
            cancel = true;
        }

        if (cancel) {
          focusView.requestFocus();
        } else {

          AVUser.logInInBackground(username, password, new LogInCallback<AVUser>() {
            @Override
            public void done(AVUser avUser, AVException e) {
              if (e == null) { 
            	  
                startActivity(new Intent(Login.this, MainActivity.class));
                Login.this.finish();
              } else {
            	  
            	  Dialog alertDialog = new AlertDialog.Builder(Login.this)
  				.setTitle("提示")
  				.setMessage(R.string.login_error)
  				.setNegativeButton(android.R.string.cancel,
  						new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog,
								int which) {
							dialog.dismiss();
						}
					})
				
  				.setPositiveButton(android.R.string.ok,
  						new DialogInterface.OnClickListener() {
  							public void onClick(DialogInterface dialog,
  									int which) {
  								dialog.dismiss();
  								startActivity(new Intent(Login.this, FindPass.class));
  								Login.this.finish();
  								
  							}
  						}).show();
  	}
              }
            }
        );}
        
      }
    
    private boolean isPasswordValid(String password) {
        //判断密码长度是否大于4
        return password.length() > 4;
      }
}
