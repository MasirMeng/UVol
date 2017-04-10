package com.example.uvol;

import java.util.ArrayList;
import java.util.List;

import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.AVException;
import com.person.ModifInfo;
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

public class EnterActivity extends Activity{
	
	private TextView title;
	private ListView act_list;
	private Button backButton;
	private List<String> types;
	private int value;
	private List<AVObject> AVacts;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_list);
        
        Intent intent = getIntent();
        value = intent.getIntExtra("type",0);
        
        types=new ArrayList();
        types.add("卫生医疗");
        types.add("爱心支教");
        types.add("文化艺术");
        types.add("生态环境");
        types.add("保护动物");
        types.add("关爱儿童");
        types.add("助学捐款");
        types.add("关爱老人");
        types.add("全部活动");
        
        title=(TextView)findViewById(R.id.tv_title);
        title.setText(types.get(value));
        
        act_list=(ListView)findViewById(R.id.act_type_list);
        
        AVacts=new ArrayList();
        getActInfo(value);
        
        
        
      //返回
        backButton=(Button)findViewById(R.id.iv_back);
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
	}
	
	private void getActInfo(int typeId){
		AVQuery<AVObject> query = new AVQuery<AVObject>("Activity");
		query.orderByDescending("createdAt");
		if(value==8){
			query.findInBackground(new FindCallback<AVObject>(){
				@Override
	            public void done(List<AVObject> list, AVException e) {
	                AVacts=list;
	                act_list.setAdapter(new ActAdapter(EnterActivity.this,R.layout.act_item,AVacts));
	                Log.i("ZHUYI",AVacts.size()+"");
	            }
				
				
			});
			
		}else{
			query.whereContains("type", types.get(value).toString());
			query.orderByDescending("createdAt");
			query.findInBackground(new FindCallback<AVObject>(){
				@Override
	            public void done(List<AVObject> list, AVException e) {
	                AVacts=list;
	                act_list.setAdapter(new ActAdapter(EnterActivity.this,R.layout.act_item,AVacts));
	                Log.i("TIXING", AVacts.size()+"");
	            }
				
				
			});
			
		}


		
	}
	
    class ActViewHolder{
    	ImageView act_pic;
        TextView tv_name;
        TextView tv_time;
        TextView tv_place;
        TextView tv_type;
        View v_enter_detail;
    }
	
	class ActAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public ActAdapter(Context context, int listResourceID, List<AVObject> objects)
	    {
	        super(context,listResourceID,objects);
	        resourceID=listResourceID;
	    }
		
		@Override
        public int getCount() {
            
            return AVacts.size();//返回数组的长度        
        }
		

		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent){
			final AVObject act=AVacts.get(position);
			View view;
			ActViewHolder viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new ActViewHolder();
				viewHolder.act_pic=(ImageView)view.findViewById(R.id.iv_act_pic);
				viewHolder.tv_name=(TextView)view.findViewById(R.id.tv_actName);
				viewHolder.tv_time=(TextView)view.findViewById(R.id.tv_time);
				viewHolder.tv_place=(TextView)view.findViewById(R.id.tv_place);
				viewHolder.tv_type=(TextView)view.findViewById(R.id.tv_type);
				viewHolder.v_enter_detail=(View)view.findViewById(R.id.v_act_detail);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(ActViewHolder)view.getTag();//重新获取viewHolder
	        }
			
			viewHolder.tv_name.setText(act.getString("name"));
			viewHolder.tv_time.setText(act.getString("time"));
			viewHolder.tv_place.setText(act.getString("place"));
			viewHolder.tv_type.setText(act.getString("type"));
			
			if(act.getAVFile("image")!=null){
				Picasso.with(EnterActivity.this).load(act.getAVFile("image").getUrl()).into(viewHolder.act_pic);					
			}

			viewHolder.v_enter_detail.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

			       	 Intent intent = new Intent();
			         //Intent传递参数
			         intent.putExtra("act_Id", act.getObjectId());
			         intent.setClass(EnterActivity.this, ActContent.class);
			         startActivity(intent);
					
				}
			});
			
			return view;
		
		}
		
	}

}
