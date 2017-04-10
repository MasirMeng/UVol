package com.example.uvol;

import java.util.ArrayList;
import java.util.List;

import com.UIothers.CircleTransform;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.example.uvol.EnterActivity.ActAdapter;
import com.example.uvol.EnterActivity.ActViewHolder;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class ActJoinUser extends Activity{
	
	private String act_Id;
	private Button backButton;
	private ListView user_list;
	private List<AVObject> AVjoins;
	private List<AVUser> AVusers;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_canyu_list);
        
        Intent intent = getIntent();
        act_Id = intent.getStringExtra("act_Id");
        
        user_list=(ListView)findViewById(R.id.list_canyuzhe);   
        AVusers=new ArrayList<AVUser>();
        
        backButton=(Button)findViewById(R.id.iv_back);       
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
        
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
               getUsers();
            }
						
		});
	}
	
	private void getUsers(){
		for(AVObject join:AVjoins){
		
			AVQuery<AVUser> userQuery = new AVQuery<AVUser>("_User");

            Log.i("JOIN",join.getString("userId"));
			userQuery.getInBackground(join.getString("userId"), new GetCallback<AVUser>() {
	            @Override
	            public void done(AVUser avObject, AVException e) {
	            	Log.i("USERNAME",avObject.getString("username"));
	            	AVusers.add(avObject);
	            	if(AVusers.size()==AVjoins.size()){
	            		Log.i("USERS",AVusers.size()+"");
	            		user_list.setAdapter(new ActJoinAdapter(ActJoinUser.this,R.layout.act_canyu_item,AVusers));
	            		
	            	}
	            }
			});			
		}
		
		
	}
	
	class ActJoinViewHoler{
		ImageView act_pic;
        TextView tv_name;
	}
	
    class ActJoinAdapter extends ArrayAdapter<AVUser>{
		
		private int resourceID;
		
		public ActJoinAdapter(Context context, int listResourceID, List<AVUser> objects)
	    {
	        super(context,listResourceID,objects);
	        resourceID=listResourceID;
	    }
		
		@Override
        public int getCount() {
            
            return AVusers.size();//返回数组的长度        
        }
		
		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent){
			final AVUser joinuser=AVusers.get(position);
			View view;
			ActJoinViewHoler viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new ActJoinViewHoler();
				viewHolder.act_pic=(ImageView)view.findViewById(R.id.person_photo);
				viewHolder.tv_name=(TextView)view.findViewById(R.id.person_name);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(ActJoinViewHoler)view.getTag();//重新获取viewHolder
	        }
			
			viewHolder.tv_name.setText(joinuser.getString("username"));
			
			if(joinuser.getAVFile("userPhoto")!=null){
				Picasso.with(ActJoinUser.this).load(joinuser.getAVFile("userPhoto").getUrl())
				.transform(new CircleTransform()).into(viewHolder.act_pic);					
			}else{
				Picasso.with(ActJoinUser.this).load(R.drawable.default_avatar)
				.transform(new CircleTransform()).into(viewHolder.act_pic);
			}

			

			
			return view;
		
		}
    }
}