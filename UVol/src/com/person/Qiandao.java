package com.person;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.UIothers.CircleTransform;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.uvol.ActContent;
import com.example.uvol.ActJoinUser;
import com.example.uvol.R;
import com.squareup.picasso.Picasso;

public class Qiandao extends Activity{
	
	private String act_Id;
	private int shichang;
	private Button backButton;
	private ListView qiandao_list;
	private View quanxuan;
	private View ack;
	private AVObject act;
	private List<AVObject> AVjoins;
	private List<AVUser> AVusers;
	private List<CheckBox> checkBoxs;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.qiandao);
        
        Intent intent = getIntent();
        act_Id = intent.getStringExtra("act_Id");
        shichang=intent.getIntExtra("act_hour", 0);
        
        qiandao_list=(ListView)findViewById(R.id.list_canyuzhe); 
        quanxuan=(View)findViewById(R.id.btn_all);
        ack=(View)findViewById(R.id.btn_canjia);
        
        AVusers=new ArrayList<AVUser>();
        checkBoxs=new ArrayList<CheckBox>();
        
        backButton=(Button)findViewById(R.id.iv_back);       
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
        
        getAct();
        getJoins();
	}
	
	private void getJoins(){
		AVQuery<AVObject> join_query = new AVQuery<AVObject>("Join");
		join_query.whereEqualTo("actId", act_Id);
		join_query.orderByDescending("createdAt");
		join_query.findInBackground(new FindCallback<AVObject>(){
			@Override
            public void done(List<AVObject> list, AVException e) {
               AVjoins=list;
               qiandao_list.setAdapter(new QiandaoAdapter(Qiandao.this,R.layout.qiandao_item,AVjoins));
   			
       	}
       });			
  }
	

	
	private void getAct(){
		AVQuery<AVObject> act_query = new AVQuery<AVObject>("Activity");
		act_query.getInBackground(act_Id, new GetCallback<AVObject>() {
            @Override
            public void done(AVObject avObject, AVException e) {
                act=avObject;
            }
		});
	}
	
	class QiandaoViewHoler{
		ImageView act_pic;
        TextView tv_name;
        CheckBox checkBox;
        
	}
	
    class QiandaoAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public QiandaoAdapter(Context context, int listResourceID, List<AVObject> objects)
	    {
	        super(context,listResourceID,objects);
	        resourceID=listResourceID;
	    }
		
	
		
		@Override
        public int getCount() {
            
            return AVjoins.size();//返回数组的长度        
        }
		
		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent){
			final AVObject join=AVjoins.get(position);
			View view;
			final QiandaoViewHoler viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new QiandaoViewHoler();
				viewHolder.act_pic=(ImageView)view.findViewById(R.id.person_photo);
				viewHolder.tv_name=(TextView)view.findViewById(R.id.person_name);
				viewHolder.checkBox=(CheckBox)view.findViewById(R.id.check_box);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(QiandaoViewHoler)view.getTag();//重新获取viewHolder
	        }
			
			checkBoxs.add(viewHolder.checkBox);
			
			AVQuery<AVUser> userQuery = new AVQuery<AVUser>("_User");
			userQuery.getInBackground(join.getString("userId"), new GetCallback<AVUser>() {
	            @Override
	            public void done(AVUser avObject, AVException e) {
	            	AVusers.add(avObject);
	            	viewHolder.tv_name.setText(avObject.getString("username"));
	            	if(avObject.getAVFile("userPhoto")!=null){
	    				Picasso.with(Qiandao.this).load(avObject.getAVFile("userPhoto").getUrl())
	    				.transform(new CircleTransform()).into(viewHolder.act_pic);					
	    			}else{
	    				Picasso.with(Qiandao.this).load(R.drawable.default_avatar)
	    				.transform(new CircleTransform()).into(viewHolder.act_pic);
	    			}
	            	
	            	Log.i("USERS",AVusers.size()+" ");
	                if(AVusers.size()==AVjoins.size()){
	             	   quanxuan.setOnClickListener(new View.OnClickListener() {
	           				
	           				@Override
	           				public void onClick(View v) {
	           					if(checkBoxs.size()>0){
	           						for(int i=0;i<checkBoxs.size();i++){
	           							checkBoxs.get(i).setChecked(true);
	           						}
	           					}
	           					
	           				}
	           			});
	             	   
	             	   ack.setOnClickListener(new View.OnClickListener() {
	           				
	           				@Override
	           				public void onClick(View v) {     					
	           					if(checkBoxs.size()>0){
	           						int count=0;
	           						int i=0;
	           						for(;i<checkBoxs.size();i++){
	           							CheckBox check=checkBoxs.get(i);
	           							if(check.isChecked()){
	           								count=count+1;
	           								
	           								AVObject userqiandao=AVjoins.get(i);
	    										userqiandao.put("Ifqiandao",true);
	    										userqiandao.saveInBackground();
	    										
	           								AVQuery<AVObject> userQuery2 = new AVQuery<AVObject>("UserHour");
	           								userQuery2.whereEqualTo("userId", AVusers.get(i).getObjectId());
	           								userQuery2.findInBackground(new FindCallback<AVObject>(){

	        										@Override
	        										public void done(
	        												List<AVObject> arg0,
	        												AVException arg1) {
	        											AVObject userHour=arg0.get(0);
	        											userHour.increment("hour", shichang);
	        											userHour.saveInBackground();
	        											
	        											
	        											
	        										}
	           									
	           								});
	           							}
	           						}
	           						
	           						if(count==0){
	           							new AlertDialog.Builder(Qiandao.this).setMessage("您没有选中任何人！").setTitle("提示")
	        								.setPositiveButton("确定", null).show();
	           						}else{
	           							act.put("Ifend", true);         							
	               						act.saveInBackground();
	               						
	               						
	               						
	               						new AlertDialog.Builder(Qiandao.this).setMessage("签到完成！").setTitle("提示")
	        								.setPositiveButton("确定", new DialogInterface.OnClickListener() {
	        									
	        									@Override
	        									public void onClick(DialogInterface dialog, int which) {
	        										finish();
	        										
	        									}
	        								}).show();
	        								}
	           						}
	           						
	           						
	           					}
	           					
	           				});
	             	   
	             	   
	             	   
	                }

	            }
			});
			
			
			
            
			
			return view;
		
		}
    }
}


