package com.example.uvol;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.uvol.EnterActivity.ActViewHolder;
import com.person.MyShoucang;
import com.squareup.picasso.Picasso;

public class SearchAct extends Activity{
	
	private Button backButton;
	private Button searchBtn;
	private Button clearBtn;
	private EditText search_et;
	private ListView act_list;
	private List<AVObject> acts;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.search_act);
        
        searchBtn=(Button)findViewById(R.id.search_btn);  
        clearBtn=(Button)findViewById(R.id.bt_clear);
        act_list=(ListView)findViewById(R.id.search_list);
        search_et=(EditText)findViewById(R.id.et_search);
        
        backButton=(Button)findViewById(R.id.iv_back);       
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
        
        initView();
        
        searchBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(search_et.getText()!=null){
					getSearch(search_et.getText().toString());
				}
				
			}
		});
        
        clearBtn.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				initView();
				search_et.setText(null);
				
			}
		});
        
	}
	
	private void getSearch(String content){
		AVQuery<AVObject> query1 = new AVQuery<AVObject>("Activity");
		query1.whereContains("name", content);
		
		AVQuery<AVObject> query2 = new AVQuery<AVObject>("Activity");
		query2.whereContains("type", content);
		
		AVQuery<AVObject> query3 = new AVQuery<AVObject>("Activity");
		query3.whereContains("content", content);
		
		 AVQuery<AVObject> query = AVQuery.or(Arrays.asList(query1, query2,query3));
	        query.findInBackground(new FindCallback<AVObject>() {
	            @Override
	            public void done(List<AVObject> list, AVException e) {
	                acts=list;
	                if(acts.size()>0){
	                	act_list.setAdapter(new SearchActAdapter(SearchAct.this,
	                    		R.layout.act_item,acts));
	                }else{
	                	new AlertDialog.Builder(SearchAct.this).setMessage("暂无搜索结果").setTitle("提示")
						.setPositiveButton("确定", null).show();
	                	
	                }
	            }
	        });

		
	}
	
	private void initView(){
		AVQuery<AVObject> query_all = new AVQuery<AVObject>("Activity");
		query_all.orderByDescending("createdAt");
		query_all.findInBackground(new FindCallback<AVObject>(){
			@Override
            public void done(List<AVObject> list, AVException e) {
                acts=list;
                act_list.setAdapter(new SearchActAdapter(SearchAct.this,
                		R.layout.act_item,acts));
            }
						
		});
	}
	
	class SearchActViewHolder{
    	ImageView act_pic;
        TextView tv_name;
        TextView tv_time;
        TextView tv_place;
        TextView tv_type;
        View v_enter_detail;
    }
	
	class SearchActAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public SearchActAdapter(Context context, int listResourceID, List<AVObject> objects)
	    {
	        super(context,listResourceID,objects);
	        resourceID=listResourceID;
	    }
		
		@Override
        public int getCount() {
            
            return acts.size();//返回数组的长度        
        }
		

		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent){
			final AVObject act=acts.get(position);
			View view;
			SearchActViewHolder viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new SearchActViewHolder();
				viewHolder.act_pic=(ImageView)view.findViewById(R.id.iv_act_pic);
				viewHolder.tv_name=(TextView)view.findViewById(R.id.tv_actName);
				viewHolder.tv_time=(TextView)view.findViewById(R.id.tv_time);
				viewHolder.tv_place=(TextView)view.findViewById(R.id.tv_place);
				viewHolder.tv_type=(TextView)view.findViewById(R.id.tv_type);
				viewHolder.v_enter_detail=(View)view.findViewById(R.id.v_act_detail);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(SearchActViewHolder)view.getTag();//重新获取viewHolder
	        }
			
			viewHolder.tv_name.setText(act.getString("name"));
			viewHolder.tv_time.setText(act.getString("time"));
			viewHolder.tv_place.setText(act.getString("place"));
			viewHolder.tv_type.setText(act.getString("type"));
			
			if(act.getAVFile("image")!=null){
				Picasso.with(SearchAct.this).load(act.getAVFile("image").getUrl()).into(viewHolder.act_pic);					
			}

			viewHolder.v_enter_detail.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

			       	 Intent intent = new Intent();
			         //Intent传递参数
			         intent.putExtra("act_Id", act.getObjectId());
			         intent.setClass(SearchAct.this, ActContent.class);
			         startActivity(intent);
					
				}
			});
			
			return view;
		
		}
		
	}

}
