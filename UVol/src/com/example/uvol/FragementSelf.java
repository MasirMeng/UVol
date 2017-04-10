package com.example.uvol;

import java.util.List;

import com.UIothers.CircleTransform;
import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.FindCallback;
import com.person.ModifInfo;
import com.person.MyShoucang;
import com.person.MyZuzhi;
import com.person.Rongyu;
import com.person.SelfManage;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class FragementSelf extends Fragment {

    private View view;
    private View info;
    private View activity;
    private View shoucang;
    private View myZuzhi;
    private View honor;
    private ImageView faceImage;
	private TextView userName;
	private TextView userJianjie;
	private TextView shoucang_count;
	private TextView shichang;


    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {// 优化View减少View的创建次数
            view = inflater.inflate(R.layout.self_main, null);
            
            
            faceImage = (ImageView) view.findViewById(R.id.icon);
            userName=(TextView)view.findViewById(R.id.id_name);
			userJianjie=(TextView)view.findViewById(R.id.id_jianjie);
			shoucang_count=(TextView)view.findViewById(R.id.shoucang_count);
			shichang=(TextView)view.findViewById(R.id.service_time);
			
			final AVUser currentUser = AVUser.getCurrentUser();

			//头像
			if(currentUser.getAVFile("userPhoto") == null){ 

				Picasso.with(getActivity()).load(R.drawable.default_avatar).transform(new CircleTransform()).into(faceImage);
			}else{
				Picasso.with(getActivity()).load(currentUser.getAVFile("userPhoto").getUrl()).transform(new CircleTransform()).into(faceImage);
			}
			//名字
			userName.setText("ID:"+currentUser.getString("username"));
			//收藏
			shoucang_count.setText(currentUser.getNumber("shoucang")+"");
			//时长
			AVQuery<AVObject> hour_query = new AVQuery<AVObject>("UserHour");
			hour_query.whereEqualTo("userId", currentUser.getObjectId());
			hour_query.findInBackground(new FindCallback<AVObject>(){
				@Override
	            public void done(List<AVObject> list, AVException e) {

					shichang.setText(list.get(0).getNumber("hour")+"");
	            }
							
			});
			//简介
			if(currentUser.getString("brief_introduce")!=null){
				userJianjie.setText("简介："+currentUser.getString("brief_introduce"));				
			}else{
				userJianjie.setText("");
			}
			

			shoucang=(View)view.findViewById(R.id.my_shoucang);
			if(currentUser.getInt("shoucang")>0){
				shoucang.setOnClickListener(new View.OnClickListener() {
					
					@Override
					public void onClick(View v) {
						Intent intent=new Intent(getActivity(),MyShoucang.class);
						startActivity(intent);
						
					}
				});
			}
			
			myZuzhi=(View)view.findViewById(R.id.alter_chitang);
			myZuzhi.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(currentUser.getInt("zuzhi")>0){
						Intent intent=new Intent(getActivity(),MyZuzhi.class);
						startActivity(intent);
					}else{
						
						new AlertDialog.Builder(getActivity()).setMessage("您还没加入任何组织").setTitle("提示")
						.setPositiveButton("确定", null).show();
						
					}
					
					
				}
			});
			
			honor=(View)view.findViewById(R.id.alter_rongyu);
			honor.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent=new Intent(getActivity(),Rongyu.class);
					startActivity(intent);
					
				}
			});
					
            return view;
        }
        return view;
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        info = getView().findViewById(R.id.alter_info);
        info.setOnClickListener(new AlterInfoListener());
        activity = getView().findViewById(R.id.alter_activity);
        activity.setOnClickListener(new AlterActListener());

    }

    private class AlterInfoListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),SelfManage.class);
            startActivity(intent);
        }
    }

    private class AlterActListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(getActivity(),MyActivity.class);
            startActivity(intent);
        }
    }



//    @Override
//    public void onActivityCreated(Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//    }


}