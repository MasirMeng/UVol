package com.example.uvol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.person.MyShoucang;

public class MyActivity extends Activity {

    private Button backButton;
    private View lineAll;
    private View linePast;
    private View lineFuture;
    private List<AVObject> act_list;
    private ListView listView;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_activity);

        //返回
        backButton=(Button)findViewById(R.id.iv_back);
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });

        lineAll=(View)findViewById(R.id.v_all);
        linePast=(View)findViewById(R.id.v_past);
        lineFuture=(View)findViewById(R.id.v_future);
        listView=(ListView)findViewById(R.id.myact_list);
        
        act_list=new ArrayList();


        clickMyType(findViewById(R.id.menu_all));



    }

    //点击顶部菜单事件
    public void clickMyType(View view)
    {
        int vID = view.getId();


        lineAll.setBackgroundColor(Color.WHITE);
        linePast.setBackgroundColor(Color.WHITE);
        lineFuture.setBackgroundColor(Color.WHITE);

        if(vID==R.id.menu_all){
            lineAll.setBackgroundColor(Color.parseColor("#3399ff"));
            getActs(0);
        }else if(vID==R.id.menu_past){
            linePast.setBackgroundColor(Color.parseColor("#3399ff"));
            getActs(1);
        }else if(vID==R.id.menu_future){
            lineFuture.setBackgroundColor(Color.parseColor("#3399ff"));
            getActs(2);
        }



    }

    private void getActs(int choose){
    			
    	if(choose==0){
    		AVQuery<AVObject> query0 = new AVQuery<AVObject>("Activity");
    		query0.whereEqualTo("fabuzhe", AVUser.getCurrentUser().getObjectId());
    		query0.orderByDescending("createdAt");
    		query0.findInBackground(new FindCallback<AVObject>(){
    			@Override
                public void done(List<AVObject> list, AVException e) {
    				act_list=list;
    				listView.setAdapter(new MyFabuAdapter(MyActivity.this,R.layout.myact_listitem,act_list));
                }
    			
    			
    		});
    		
    	}else if(choose==1){
    		AVQuery<AVObject> query1 = new AVQuery<AVObject>("Join");
    		query1.whereEqualTo("userId", AVUser.getCurrentUser().getObjectId());
    		query1.orderByDescending("createdAt");
    		query1.findInBackground(new FindCallback<AVObject>(){
    			@Override
                public void done(List<AVObject> list, AVException e) {
    				act_list=list;
    				listView.setAdapter(new MyactAdapter(MyActivity.this,R.layout.myact_listitem,act_list));
                    //shoucang_list.setAdapter(new ShoucangAdapter(MyShoucang.this,R.layout.myact_listitem,shoucangs));
                }
    			
    			
    		});
    		
    	}else if(choose==2){
    		AVQuery<AVObject> query2 = new AVQuery<AVObject>("Join");
    		query2.whereEqualTo("userId", AVUser.getCurrentUser().getObjectId());
    		
    		AVQuery<AVObject> query3=new AVQuery<AVObject>("Join");
    		query3.whereEqualTo("Ifqiandao", true);
    		
    		AVQuery query4=AVQuery.and(Arrays.asList(query2, query3));
    		query4.orderByDescending("createdAt");
    		query4.findInBackground(new FindCallback<AVObject>(){
    			@Override
                public void done(List<AVObject> list, AVException e) {
    				act_list=list;
    				listView.setAdapter(new MyactAdapter(MyActivity.this,R.layout.myact_listitem,act_list));
                }
    			
    			
    		});
    	}
    	
    	
    	
    }

    
    
    class MyactAdapter extends ArrayAdapter<AVObject> {

        private int resourceID;

        public MyactAdapter(Context context, int listResourceID, List<AVObject> objects)
        {
            super(context,listResourceID,objects);
            resourceID=listResourceID;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final AVObject act=getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView==null){
                view= LayoutInflater.from(getContext()).inflate(resourceID,null);

                //优化ListView性能
                viewHolder=new ViewHolder();
                viewHolder.tv_name=(TextView)view.findViewById(R.id.tv_listName);
                viewHolder.tv_time=(TextView)view.findViewById(R.id.tv_listTime);
                viewHolder.tv_place=(TextView)view.findViewById(R.id.tv_listPlace);
                viewHolder.enter_detail=(View)view.findViewById(R.id.list_view);
                view.setTag(viewHolder);//将viewHolder存储在view中
            }else{
                view=convertView;
                viewHolder=(ViewHolder)view.getTag();//重新获取viewHolder
            }

            viewHolder.tv_name.setText(act.getString("actName"));
    		viewHolder.tv_time.setText(act.getString("actTime"));
    		viewHolder.tv_place.setText(act.getString("actPlace"));
    		
    		viewHolder.enter_detail.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {

    		       	 Intent intent = new Intent();
    		         //Intent传递参数
    		         intent.putExtra("act_Id", act.getString("actId"));
    		         intent.setClass(MyActivity.this, ActContent.class);
    		         startActivity(intent);
    				
    			}
    		});

            return view;
        }
    }
    
    class MyFabuAdapter extends ArrayAdapter<AVObject> {

        private int resourceID;

        public MyFabuAdapter(Context context, int listResourceID, List<AVObject> objects)
        {
            super(context,listResourceID,objects);
            resourceID=listResourceID;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            final AVObject act=getItem(position);
            View view;
            ViewHolder viewHolder;

            if(convertView==null){
                view= LayoutInflater.from(getContext()).inflate(resourceID,null);

                //优化ListView性能
                viewHolder=new ViewHolder();
                viewHolder.tv_name=(TextView)view.findViewById(R.id.tv_listName);
                viewHolder.tv_time=(TextView)view.findViewById(R.id.tv_listTime);
                viewHolder.tv_place=(TextView)view.findViewById(R.id.tv_listPlace);
                viewHolder.enter_detail=(View)view.findViewById(R.id.list_view);
                view.setTag(viewHolder);//将viewHolder存储在view中
            }else{
                view=convertView;
                viewHolder=(ViewHolder)view.getTag();//重新获取viewHolder
            }

            viewHolder.tv_name.setText(act.getString("name"));
    		viewHolder.tv_time.setText(act.getString("time"));
    		viewHolder.tv_place.setText(act.getString("place"));
    		
    		viewHolder.enter_detail.setOnClickListener(new View.OnClickListener() {
    			
    			@Override
    			public void onClick(View v) {

    		       	 Intent intent = new Intent();
    		         //Intent传递参数
    		         intent.putExtra("act_Id", act.getObjectId());
    		         intent.setClass(MyActivity.this, ActContent.class);
    		         startActivity(intent);
    				
    			}
    		});

            return view;
        }
    }


        class ViewHolder{
            TextView tv_name;
            TextView tv_time;
            TextView tv_place;
            View enter_detail;
        }


}