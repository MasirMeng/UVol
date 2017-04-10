package com.person;

import java.util.ArrayList;
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
import com.avos.avoscloud.GetCallback;
import com.example.uvol.ActContent;
import com.example.uvol.ActJoinUser;
import com.example.uvol.EnterActivity;
import com.example.uvol.R;
import com.example.uvol.ZuzhiDetail;
import com.person.MyShoucang.ScViewHolder;
import com.person.MyShoucang.ShoucangAdapter;
import com.squareup.picasso.Picasso;

public class MyZuzhi extends Activity{
	
	private Button backButton;
	private ListView zuzhi_list;
	private List<AVObject> joins;
	private List<AVObject> zuzhis;

	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_zuzhi);

        
        zuzhi_list=(ListView)findViewById(R.id.list_myzuzhi);
        zuzhis=new ArrayList<AVObject>();
        getZuzhi();
        
        backButton=(Button)findViewById(R.id.iv_back);       
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
                finish();

            }
        });
	}
	
	private void getZuzhi(){
		AVQuery<AVObject> query = new AVQuery<AVObject>("JoinZuzhi");
		query.orderByDescending("createdAt");
		query.whereEqualTo("userId", AVUser.getCurrentUser().getObjectId());		
		query.findInBackground(new FindCallback<AVObject>(){
			@Override
            public void done(List<AVObject> list, AVException e) {
				joins=list;
				getInfo();
                //shoucang_list.setAdapter(new ShoucangAdapter(MyShoucang.this,R.layout.myact_listitem,shoucangs));
            }
			
			
		});
		
	}
	
	private void getInfo(){
		for(AVObject zuzhi:joins){
			AVQuery<AVObject> zuzhiQuery = new AVQuery<AVObject>("Orginaze");
			zuzhiQuery.getInBackground(zuzhi.getString("zuzhiId"), new GetCallback<AVObject>() {
	            @Override
	            public void done(AVObject avObject, AVException e) {
	            	zuzhis.add(avObject);
	            	if(zuzhis.size()==joins.size()){
	            		zuzhi_list.setAdapter(new MyZuzhiAdapter(MyZuzhi.this,R.layout.my_zuzhi_item,zuzhis));
	            	}
	            }
			});		
		}
	}
	
	class MyZuzhiViewHolder{
		TextView zuzhi_name;
		TextView zuzhi_jianjie;
		ImageView zuzhi_tx;
		View enter_detail;
	}
	
class MyZuzhiAdapter extends ArrayAdapter<AVObject>{
		
		private int resourceID;
		
		public MyZuzhiAdapter(Context context, int listResourceID, List<AVObject> objects)
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
			final AVObject act=zuzhis.get(position);
			View view;
			MyZuzhiViewHolder viewHolder;
			
			if(convertView==null){
				view=LayoutInflater.from(getContext()).inflate(resourceID,null);
				
				viewHolder=new MyZuzhiViewHolder();
				viewHolder.zuzhi_name=(TextView)view.findViewById(R.id.person_name);
				viewHolder.zuzhi_jianjie=(TextView)view.findViewById(R.id.person_jianjie);
				viewHolder.zuzhi_tx=(ImageView)view.findViewById(R.id.tx_zuzhi);
				viewHolder.enter_detail=(View)view.findViewById(R.id.enter_detail);

				view.setTag(viewHolder);
			}else{
	            view=convertView;
	            viewHolder=(MyZuzhiViewHolder)view.getTag();//重新获取viewHolder
	        }
			
			viewHolder.zuzhi_name.setText(act.getString("Originaze_Name"));
			viewHolder.zuzhi_jianjie.setText(act.getString("Intro"));
			if(act.getAVFile("Image")!=null){
				Picasso.with(MyZuzhi.this).load(act.getAVFile("Image").getUrl()).into(viewHolder.zuzhi_tx);					
			}

			viewHolder.enter_detail.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {

			       	 Intent intent = new Intent();
			         //Intent传递参数
			         intent.putExtra("zuzhi_Id", act.getObjectId());
			         intent.setClass(MyZuzhi.this, ZuzhiDetail.class);
			         startActivity(intent);
					
				}
			});
			
			return view;
		
		}
		
	}
}
