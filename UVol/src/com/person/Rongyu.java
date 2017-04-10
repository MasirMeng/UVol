package com.person;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.UIothers.CircleTransform;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.example.uvol.ActContent;
import com.example.uvol.ActJoinUser;
import com.example.uvol.MyActivity;
import com.example.uvol.R;
import com.person.MyShoucang.ScViewHolder;
import com.squareup.picasso.Picasso;

public class Rongyu extends Activity{
	
	private View person_view;
	private View zuzhi_view;
	private Button backButton;
	private ListView rongyu_list;
	private List<AVObject> rongyu;
	private List<AVObject> userHours;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.rongyu_bang);

        rongyu_list=(ListView)findViewById(R.id.honor_list);
        person_view=(View)findViewById(R.id.v_person);
        zuzhi_view=(View)findViewById(R.id.v_zuzhi);
        
        rongyu=new ArrayList();
        userHours=new ArrayList();
  
        clickHonorType(findViewById(R.id.menu_person));
        
        backButton=(Button)findViewById(R.id.iv_back);       
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
	}
	
	//点击顶部菜单事件
    public void clickHonorType(View view)
    {
        int vID = view.getId();
        Boolean menu_type=true;
        person_view.setBackgroundColor(Color.WHITE);
        zuzhi_view.setBackgroundColor(Color.WHITE);
        
        if(vID==R.id.menu_person){
        	person_view.setBackgroundColor(Color.parseColor("#3399ff"));
        	menu_type=true;
        }else if(vID==R.id.menu_zuzhi){
        	zuzhi_view.setBackgroundColor(Color.parseColor("#3399ff"));
        	menu_type=false;
        }
        
        getHonorInfo(menu_type);
    }
    
    private void getHonorInfo(Boolean ifPerson){
    	if(ifPerson){
    		AVQuery<AVObject> hourQuery = new AVQuery<AVObject>("UserHour");
    		hourQuery.orderByDescending("hour");
    		hourQuery.findInBackground(new FindCallback<AVObject>(){
    			@Override
                public void done(List<AVObject> list, AVException e) {
                   userHours=list;
                   rongyu_list.setAdapter(new PersonRongyuAdapter(Rongyu.this,
                		   R.layout.honor_person_item,rongyu));
                }
    						
    		});
    		
    		
    	}else{
    		AVQuery<AVObject> zuzhiQuery = new AVQuery<AVObject>("Orginaze");
    		zuzhiQuery.orderByDescending("Hour");
    		zuzhiQuery.findInBackground(new FindCallback<AVObject>(){
    			@Override
                public void done(List<AVObject> list, AVException e) {
                   rongyu=list;
                   rongyu_list.setAdapter(new RongyuAdapter(Rongyu.this,
                		   R.layout.honor_person_item,rongyu));
                }
    						
    		});
    	}
    	
    }
    
    class RongyuViewHolder{
		TextView name;
		TextView jianjie;
		TextView paiming;
		TextView hour;
		ImageView pic;
	}
    
   class RongyuAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public RongyuAdapter(Context context, int listResourceID, List<AVObject> objects)
	    {
	        super(context,listResourceID,objects);
	        resourceID=listResourceID;
	    }
		
		@Override
        public int getCount() {
            
            return rongyu.size();//返回数组的长度        
        }
		

		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent){
			final AVObject zuzhi=rongyu.get(position);
			
			final int xuhao=position+1;
			View view;
			RongyuViewHolder viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new RongyuViewHolder();
				viewHolder.name=(TextView)view.findViewById(R.id.person_name);
				viewHolder.jianjie=(TextView)view.findViewById(R.id.person_jianjie);
				viewHolder.hour=(TextView)view.findViewById(R.id.zhiyuan_time);
				viewHolder.paiming=(TextView)view.findViewById(R.id.bianhao);
				viewHolder.pic=(ImageView)view.findViewById(R.id.person_photo);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(RongyuViewHolder)view.getTag();//重新获取viewHolder
	        }
			
			
			
			viewHolder.name.setText(zuzhi.getString("Originaze_Name"));
			viewHolder.jianjie.setText(zuzhi.getString("Intro"));
			viewHolder.hour.setText(zuzhi.getInt("Hour")+"");
			viewHolder.paiming.setText(xuhao+"");
			
			Picasso.with(Rongyu.this).load(zuzhi.getAVFile("Image").getUrl()).transform(new CircleTransform()).into(viewHolder.pic);
			
			
			
			return view;
		
		}
		
	}
   
   class PersonRongyuAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public PersonRongyuAdapter(Context context, int listResourceID, List<AVObject> objects)
	    {
	        super(context,listResourceID,objects);
	        resourceID=listResourceID;
	    }
		
		@Override
       public int getCount() {
           
           return userHours.size();//返回数组的长度        
       }
		

		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent){
			final AVObject userhour=userHours.get(position);
			final int xuhao=position+1;
			View view;
			final RongyuViewHolder viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new RongyuViewHolder();
				viewHolder.name=(TextView)view.findViewById(R.id.person_name);
				viewHolder.jianjie=(TextView)view.findViewById(R.id.person_jianjie);
				viewHolder.hour=(TextView)view.findViewById(R.id.zhiyuan_time);
				viewHolder.paiming=(TextView)view.findViewById(R.id.bianhao);
				viewHolder.pic=(ImageView)view.findViewById(R.id.person_photo);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(RongyuViewHolder)view.getTag();//重新获取viewHolder
	        }
			

			AVQuery<AVUser> userQuery = new AVQuery<AVUser>("_User");
        	userQuery.whereEqualTo("objectId", userhour.getString("userId"));
       		userQuery.findInBackground(new FindCallback<AVUser>(){
       			@Override
                   public void done(List<AVUser> list, AVException e) {
          			viewHolder.name.setText(list.get(0).getString("username"));
        			viewHolder.jianjie.setText(list.get(0).getString("brief_introduce"));
        			if(list.get(0).getAVFile("userPhoto") == null){ 

        				Picasso.with(Rongyu.this).load(R.drawable.default_avatar).transform(new CircleTransform()).into(viewHolder.pic);
        			}else{
        				Picasso.with(Rongyu.this).load(list.get(0).getAVFile("userPhoto").getUrl()).transform(new CircleTransform()).into(viewHolder.pic);
        			}
                   }
       						
       		});
			

			viewHolder.paiming.setText(xuhao+"");
			viewHolder.hour.setText(userhour.getNumber("hour")+"");
					
			return view;
		
		}
		
	}
    
    
}
