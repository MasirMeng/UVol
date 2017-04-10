package com.example.uvol;

import java.util.Arrays;
import java.util.List;

import android.app.Activity;
import android.app.AlertDialog;
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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.FindCallback;
import com.example.uvol.SearchAct.SearchActAdapter;
import com.example.uvol.SearchAct.SearchActViewHolder;
import com.squareup.picasso.Picasso;

public class SearchZuzhi extends Activity{
	
	private Button backButton;
	private Button searchBtn;
	private Button clearBtn;
	private EditText search_et;
	private ListView act_list;
	private List<AVObject> zuzhis;

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
		AVQuery<AVObject> query1 = new AVQuery<AVObject>("Orginaze");
		query1.whereContains("Originaze_Name", content);
		
		AVQuery<AVObject> query2 = new AVQuery<AVObject>("Orginaze");
		query2.whereContains("Intro", content);
		

		
		 AVQuery<AVObject> query = AVQuery.or(Arrays.asList(query1, query2));
	        query.findInBackground(new FindCallback<AVObject>() {
	            @Override
	            public void done(List<AVObject> list, AVException e) {
	                zuzhis=list;
	                if(zuzhis.size()>0){
	                	act_list.setAdapter(new SearchZuzhiAdapter(SearchZuzhi.this,
	                    		R.layout.zuzhi_item,zuzhis));
	                }else{
	                	new AlertDialog.Builder(SearchZuzhi.this).setMessage("暂无搜索结果").setTitle("提示")
						.setPositiveButton("确定", null).show();
	                	
	                }
	            }
	        });

		
	}
	
	private void initView(){
		AVQuery<AVObject> query_all = new AVQuery<AVObject>("Orginaze");
		query_all.orderByDescending("createdAt");
		query_all.findInBackground(new FindCallback<AVObject>(){
			@Override
            public void done(List<AVObject> list, AVException e) {
                zuzhis=list;
                Log.i("ZUZHI",zuzhis.size()+"");
                act_list.setAdapter(new SearchZuzhiAdapter(SearchZuzhi.this,
                		R.layout.zuzhi_item,zuzhis));
            }
						
		});
	}
	
	class SearchZuzhiViewHolder{
    	ImageView zuzhi_pic;
        TextView tv_name;
        TextView tv_jianjie;
        TextView tv_shichang;
        TextView tv_renshu;
        View v_enter_detail;
    }
	
class SearchZuzhiAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public SearchZuzhiAdapter(Context context, int listResourceID, List<AVObject> objects)
	    {
	        super(context,listResourceID,objects);
	        resourceID=listResourceID;
	    }
		
		@Override
        public int getCount() {
            
            return zuzhis.size();//返回数组的长度        
        }
		

		
		@Override
	    public View getView(int position, View convertView, ViewGroup parent){
			final AVObject zuzhi=zuzhis.get(position);
			View view;
			SearchZuzhiViewHolder viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new SearchZuzhiViewHolder();
				viewHolder.zuzhi_pic=(ImageView)view.findViewById(R.id.tx_zuzhi);
				viewHolder.tv_name=(TextView)view.findViewById(R.id.person_name);
				viewHolder.tv_jianjie=(TextView)view.findViewById(R.id.person_jianjie);
				viewHolder.tv_shichang=(TextView)view.findViewById(R.id.shichang);
				viewHolder.tv_renshu=(TextView)view.findViewById(R.id.renshu);
				viewHolder.v_enter_detail=(View)view.findViewById(R.id.enter_detail);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(SearchZuzhiViewHolder)view.getTag();//重新获取viewHolder
	        }
			
			viewHolder.tv_name.setText(zuzhi.getString("Originaze_Name"));
			viewHolder.tv_jianjie.setText(zuzhi.getString("Intro"));
			viewHolder.tv_shichang.setText(zuzhi.getInt("Hour")+"");
			viewHolder.tv_renshu.setText(zuzhi.getInt("Renshu")+"");
			
			if(zuzhi.getAVFile("Image")!=null){
				Picasso.with(SearchZuzhi.this).load(zuzhi.getAVFile("Image").getUrl()).into(viewHolder.zuzhi_pic);					
			}

			viewHolder.v_enter_detail.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

			       	 Intent intent = new Intent();
			         //Intent传递参数
			         intent.putExtra("zuzhi_Id", zuzhi.getObjectId());
			         intent.setClass(SearchZuzhi.this, ZuzhiDetail.class);
			         startActivity(intent);
					
				}
			});
			
			return view;
		
		}
		
	}

	

}
