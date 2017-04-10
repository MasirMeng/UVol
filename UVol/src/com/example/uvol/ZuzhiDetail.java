package com.example.uvol;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import com.UIothers.CircleTransform;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.uvol.EnterActivity.ActAdapter;
import com.example.uvol.EnterActivity.ActViewHolder;
import com.squareup.picasso.Picasso;

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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ZuzhiDetail extends Activity{

	private String zuzhi_Id;
	private AVObject zuzhi;
	private AVUser curUser;

	private TextView zuzhi_name;
	private TextView zuzhi_jianjie;
	private TextView zuzhi_shichang;
	private TextView zuzhi_paiming;
	private TextView zuzhi_renshu;
	private TextView join;
	private ImageView zuzhi_pic;
	private ImageView back;
	
	private View join_zuzhi;
	
	private ListView act_list;
	private List<AVObject> acts;
	
	private Boolean ifJoin;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.zuzhi_detail_main);
        
        Intent intent = getIntent();
        zuzhi_Id = intent.getStringExtra("zuzhi_Id");
        
        curUser=AVUser.getCurrentUser();
        
        zuzhi_name=(TextView)findViewById(R.id.id_name);
        zuzhi_jianjie=(TextView)findViewById(R.id.id_jianjie);
        zuzhi_shichang=(TextView)findViewById(R.id.tv_shichang);
        zuzhi_paiming=(TextView)findViewById(R.id.tv_paiming);
        zuzhi_renshu=(TextView)findViewById(R.id.tv_renshu);
        zuzhi_pic=(ImageView)findViewById(R.id.tx_zuzhi);
        back=(ImageView)findViewById(R.id.back);
        join_zuzhi=(View)findViewById(R.id.btn_canjia);
        act_list=(ListView)findViewById(R.id.list_zuzhiAct);
        join=(TextView)findViewById(R.id.woyaocanjia);
        
        initView();
        
        back.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
        
	}
	
	private void initView(){
		
		AVQuery<AVObject> query = new AVQuery<AVObject>("Orginaze");
		query.getInBackground(zuzhi_Id, new GetCallback<AVObject>() {
	            @Override
	            public void done(AVObject avObject, AVException e) {
	            	
	            	zuzhi=avObject;
	            	 if(zuzhi.getAVFile("Image")!=null){
		                	Picasso.with(ZuzhiDetail.this).load(zuzhi.getAVFile("Image").getUrl()).
		                	transform(new CircleTransform()).into(zuzhi_pic);
		             }
	            	 
	            	 zuzhi_name.setText(zuzhi.getString("Originaze_Name"));
	            	 zuzhi_jianjie.setText(zuzhi.getString("Intro"));
	            	 zuzhi_shichang.setText(zuzhi.getNumber("Hour")+"");
	            	 zuzhi_paiming.setText(zuzhi.getNumber("Paiming")+"");
	            	 zuzhi_renshu.setText(zuzhi.getNumber("Renshu")+"");
	            	 
	            	 int act_count=(Integer) zuzhi.getNumber("Activity_count");
	            	 if(act_count==0){
	            		 act_list.setVisibility(View.GONE);
	            	 }else{
	            		 AVQuery<AVObject> query2 = new AVQuery<AVObject>("Activity");
	            		 query2.whereEqualTo("fabuzhe", zuzhi.getObjectId());
	            		 query2.findInBackground(new FindCallback<AVObject>() {
	            	            @Override
	            	            public void done(List<AVObject> list, AVException e) {
	            	                acts=list;
	            	                act_list.setAdapter(
	       		            			 new ZuzhiActAdapter(ZuzhiDetail.this,R.layout.zuzhi_act_item,acts));
	            	            }
	            	        });
	            		 
	            		 

	            	 }
	            	 
	            	 //检查是否已加入组织
		                final AVQuery<AVObject> zuzhi_query1 = new AVQuery<AVObject>("JoinZuzhi");
		                zuzhi_query1.whereEqualTo("zuzhiId", zuzhi.getObjectId());
						
						final AVQuery<AVObject> zuzhi_query2 = new AVQuery<AVObject>("JoinZuzhi");
						zuzhi_query2.whereEqualTo("userId", AVUser.getCurrentUser().getObjectId());
						
						final AVQuery<AVObject> zuzhi_query = AVQuery.and(
								Arrays.asList(zuzhi_query1, zuzhi_query2));
						zuzhi_query.countInBackground(new CountCallback() {
				            @Override
				            public void done(int i, AVException e) {
				                if (e == null) {
				                    if(i==0){
				                    	join.setText("加入组织");			         
				                    	ifJoin=false;
				                    }else{
				                    	join.setText("退出组织");
				                    	ifJoin=true;
				                    }
				                } else {
				                    // 查询失败
				                }
				            }
				        });
	            	 
	            	 join_zuzhi.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(ifJoin){
								final AVQuery<AVObject> query3 = AVQuery.and(
										Arrays.asList(zuzhi_query1, zuzhi_query2));
								
							query3.findInBackground(new FindCallback<AVObject>() {
						            @Override
						            public void done(List<AVObject> list, AVException e) {
						            	AVObject delact=list.get(0);
                                        delact.deleteInBackground();
                                        
                                        zuzhi.increment("Renshu",-1);
                                        zuzhi.saveInBackground();
                                        
                                        curUser.increment("zuzhi",-1);
                                        curUser.saveInBackground();
          
                                        zuzhi_renshu.setText(zuzhi.getInt("Renshu")+"");
                                        join.setText("加入组织");			         
				                    	ifJoin=false;
						            }
						        });
								
							}else{
								
								AVObject zz = new AVObject("JoinZuzhi");
								zz.put("userId", AVUser.getCurrentUser().getObjectId());
								zz.put("zuzhiId", zuzhi.getObjectId());
								zz.saveInBackground(new SaveCallback() {
							          @Override
							          public void done(AVException e) {
							            if (e == null) {
							            	new AlertDialog.Builder(ZuzhiDetail.this).setMessage("成功加入组织！").setTitle("提示")
											.setPositiveButton("确定", null).show();
							            	
							            	zuzhi.increment("Renshu",1);
							            	zuzhi.saveInBackground();
							            	
							            	curUser.increment("zuzhi",1);
							            	curUser.saveInBackground();
	                                        
							            	zuzhi_renshu.setText(zuzhi.getInt("Renshu")+"");
							            	join.setText("退出组织");
					                    	ifJoin=true;
							            } else {
							              
							              Toast.makeText(ZuzhiDetail.this, e.getMessage(), Toast.LENGTH_SHORT).show();
							            }
							          }
							        });
								
							}
							
						}
					});


		            	 
		            	 
	           
	            	 
	            	 
	            	 
	            }
		
	     });
	}

    class ZuzhiActHolder{
        TextView tv_name;
        View enter_act;
    }
    
    class ZuzhiActAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public ZuzhiActAdapter(Context context, int listResourceID, List<AVObject> objects)
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
			final int index=position;
			final String name=acts.get(position).getString("name");
			View view;
			ZuzhiActHolder viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new ZuzhiActHolder();
				viewHolder.tv_name=(TextView)view.findViewById(R.id.act_name);
				viewHolder.enter_act=(View)view.findViewById(R.id.enter_act);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(ZuzhiActHolder)view.getTag();//重新获取viewHolder
	        }
			
			viewHolder.tv_name.setText(name);
			
			
			viewHolder.enter_act.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

			       	 Intent intent = new Intent();
			         //Intent传递参数
			         intent.putExtra("act_Id", acts.get(index).getObjectId());
			         intent.setClass(ZuzhiDetail.this, ActContent.class);
			         startActivity(intent);
					
				}
			});
			
			return view;
		
		}
		
	}

}
