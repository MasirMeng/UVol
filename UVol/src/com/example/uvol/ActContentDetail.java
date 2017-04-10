package com.example.uvol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ActContentDetail extends Activity{
	
	private Button backButton;
	private TextView content;
	private String act_content;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_content_detail);
        
        Intent intent = getIntent();
        act_content = intent.getStringExtra("act_content");
        
        content=(TextView)findViewById(R.id.act_content);
        content.setText(act_content);
        
        backButton=(Button)findViewById(R.id.iv_back);       
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
	}
}

