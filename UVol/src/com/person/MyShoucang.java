package com.person;

import java.util.List;

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

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.uvol.ActContent;
import com.example.uvol.EnterActivity;
import com.example.uvol.R;
import com.squareup.picasso.Picasso;

public class MyShoucang extends Activity{
	
	private Button backButton;
	private ListView shoucang_list;
	private List<AVObject> shoucangs;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_shoucang);

        
        shoucang_list=(ListView)findViewById(R.id.list_shoucang);
        
        getShoucang();
        
        backButton=(Button)findViewById(R.id.iv_back);       
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
	}
	
	private void getShoucang(){
		AVQuery<AVObject> query = new AVQuery<AVObject>("Shoucang");
		query.whereEqualTo("userId", AVUser.getCurrentUser().getObjectId());
		query.orderByDescending("createdAt");
		query.findInBackground(new FindCallback<AVObject>(){
			@Override
            public void done(List<AVObject> list, AVException e) {
                shoucangs=list;
                shoucang_list.setAdapter(new ShoucangAdapter(MyShoucang.this,R.layout.myact_listitem,shoucangs));
            }
			
			
		});
		
	}
	
	class ScViewHolder{
		TextView act_name;
		TextView act_time;
		TextView act_place;
		View enter_detail;
	}
	
class ShoucangAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public ShoucangAdapter(Context context, int listResourceID, List<AVObject> objects)
	    {
	        super(context,listResourceID,objects);
	        resourceID=listResourceID;
	    }
		
		@Override
        public int getCount() {
            
            return shoucangs.size();//返回数组的长度        
        }
		

		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent){
			final AVObject act=shoucangs.get(position);
			View view;
			ScViewHolder viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new ScViewHolder();
				viewHolder.act_name=(TextView)view.findViewById(R.id.tv_listName);
				viewHolder.act_time=(TextView)view.findViewById(R.id.tv_listTime);
				viewHolder.act_place=(TextView)view.findViewById(R.id.tv_listPlace);
				viewHolder.enter_detail=(View)view.findViewById(R.id.list_view);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(ScViewHolder)view.getTag();//重新获取viewHolder
	        }
			
			viewHolder.act_name.setText(act.getString("actName"));
			viewHolder.act_time.setText(act.getString("actTime"));
			viewHolder.act_place.setText(act.getString("actPlace"));

			viewHolder.enter_detail.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

			       	 Intent intent = new Intent();
			         //Intent传递参数
			         intent.putExtra("act_Id", act.getString("actId"));
			         intent.setClass(MyShoucang.this, ActContent.class);
			         startActivity(intent);
					
				}
			});
			
			return view;
		
		}
		
	}
}
