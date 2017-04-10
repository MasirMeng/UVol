package com.example.uvol;

import java.util.Arrays;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVQuery;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.CountCallback;
import com.avos.avoscloud.FindCallback;
import com.avos.avoscloud.GetCallback;
import com.avos.avoscloud.SaveCallback;
import com.example.uvol.EnterActivity.ActAdapter;
import com.person.Qiandao;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class ActContent extends Activity{
	
    private AVUser curUser;
	private String act_Id;
	private AVObject act;
	private ImageView act_pic;
	private TextView act_title;
	private TextView act_fabuzhe;
	private TextView act_time;
	private TextView act_place;
	private TextView act_type;
	private TextView act_hour;
	private TextView act_fuzeren;
	private TextView act_phone;
	private TextView act_content;
	private TextView zong_renshu;
	private TextView yicanjia;
	private ImageView backButton;
	private ImageView enterZuzhi;
	private View enterZuzhiDetail;
	private View enterActContent;
	private View enterYicanjia;
	
	private TextView shoucang;
	private View join;
	private TextView tv_join;
	
	private Boolean ifSC;
	private Boolean ifJoin;
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.act_detail);
        
        Intent intent = getIntent();
        act_Id = intent.getStringExtra("act_Id");
        
        curUser=AVUser.getCurrentUser();
        
        act_pic=(ImageView)findViewById(R.id.v_background);
        act_title=(TextView)findViewById(R.id.act_name);
        act_fabuzhe=(TextView)findViewById(R.id.tv_fabuzhe);
        act_time=(TextView)findViewById(R.id.tv_actTime);
        act_place=(TextView)findViewById(R.id.tv_place);
        act_type=(TextView)findViewById(R.id.tv_leixing);
        act_hour=(TextView)findViewById(R.id.tv_hour);
        act_fuzeren=(TextView)findViewById(R.id.tv_fuzeren);
        act_phone=(TextView)findViewById(R.id.tv_dianhua);
        act_content=(TextView)findViewById(R.id.act_content);
        zong_renshu=(TextView)findViewById(R.id.act_weicanjia);
        yicanjia=(TextView)findViewById(R.id.act_yicanjia);
        
        shoucang=(TextView)findViewById(R.id.shoucang);
        join=(View)findViewById(R.id.btn_canjia);
        tv_join=(TextView)findViewById(R.id.woyaocanjia);
        
        enterZuzhiDetail=(View)findViewById(R.id.enter_zuzhi_detail);
        enterYicanjia=(View)findViewById(R.id.enter_yicanjia);
        
        enterZuzhi=(ImageView)findViewById(R.id.enter);
        enterZuzhi.setVisibility(View.INVISIBLE);
        
        enterActContent=(View)findViewById(R.id.enter_content_detail);

        
        backButton=(ImageView)findViewById(R.id.back);       
        backButton.setOnClickListener(new Button.OnClickListener(){
            @Override
            public  void onClick(View v){
            	finish();

            }
        });
        
        getDetail();
        
        
	}
	
	private void getDetail(){
		AVQuery<AVObject> query = new AVQuery<AVObject>("Activity");
		query.getInBackground(act_Id, new GetCallback<AVObject>() {
	            @Override
	            public void done(AVObject avObject, AVException e) {
	                act=avObject;
	                if(act.getAVFile("image")!=null){
	                	Picasso.with(ActContent.this).load(act.getAVFile("image").getUrl()).into(act_pic);
	                }else{
	                	Picasso.with(ActContent.this).load(R.drawable.ph1).into(act_pic);
	                }
	                act_title.setText(act.getString("name"));
	                act_fabuzhe.setText(act.getString("fabuname"));
	                act_time.setText(act.getString("time"));
	                act_place.setText(act.getString("place"));
	                act_type.setText(act.getString("type"));
	                act_hour.setText(act.getNumber("shichang")+"h");
	                act_fuzeren.setText(act.getString("fuzeren"));
	                act_phone.setText(act.getString("telphone"));
	                act_content.setText(act.getString("content"));
	                zong_renshu.setText(act.getNumber("renshu")+"");
	                yicanjia.setText(act.getNumber("yicanjia")+"");
	                
	                //检查是否已收藏
	                final AVQuery<AVObject> shoucang_query1 = new AVQuery<AVObject>("Shoucang");
					shoucang_query1.whereEqualTo("actId", act.getObjectId());
					
					final AVQuery<AVObject> shoucang_query2 = new AVQuery<AVObject>("Shoucang");
					shoucang_query2.whereEqualTo("userId", curUser.getObjectId());
					
					final AVQuery<AVObject> shoucang_query = AVQuery.and(
							Arrays.asList(shoucang_query1, shoucang_query2));
					shoucang_query.countInBackground(new CountCallback() {
			            @Override
			            public void done(int i, AVException e) {
			                if (e == null) {
			                    if(i==0){
			                    	shoucang.setText("收藏");			         
			                    	ifSC=false;
			                    }else{
			                    	shoucang.setText("取消收藏");
			                    	ifSC=true;
			                    }
			                } else {
			                    // 查询失败
			                }
			            }
			        });
					//点击收藏
	                shoucang.setOnClickListener(new View.OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(ifSC){
								final AVQuery<AVObject> shoucang_query3 = AVQuery.and(
										Arrays.asList(shoucang_query1, shoucang_query2));
								
								shoucang_query3.findInBackground(new FindCallback<AVObject>() {
						            @Override
						            public void done(List<AVObject> list, AVException e) {
						            	AVObject delact=list.get(0);
                                        delact.deleteInBackground();
                                        
                                        curUser.increment("shoucang", -1);
										curUser.saveInBackground();
										
                                        shoucang.setText("收藏");			         
    			                    	ifSC=false;
						            }
						        });


							}else{
							
		                    	AVObject sc = new AVObject("Shoucang");
								sc.put("userId", curUser.getObjectId());
								sc.put("actId", act.getObjectId());
								sc.put("actName",act.getString("name"));
								sc.put("actTime", act.getString("time"));
								sc.put("actPlace", act.getString("place"));
								sc.saveInBackground(new SaveCallback() {
							          @Override
							          public void done(AVException e) {
							            if (e == null) {
							            	new AlertDialog.Builder(ActContent.this).setMessage("收藏成功！").setTitle("提示")
											.setPositiveButton("确定", null).show();
							            	
							            	curUser.increment("shoucang", 1);
											curUser.saveInBackground();
											
							            	shoucang.setText("取消收藏");
					                    	ifSC=true;
							            } else {
							              
							              Toast.makeText(ActContent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
							            }
							          }
							        });
								
								
								
							}
						
							
							
						}
					});
	                
	                if(act.getBoolean("Ifend")){
	                	tv_join.setText("活动已结束");
	                	join.setEnabled(false);
	                }else{
	                	join.setEnabled(true);
	                	if((act.getString("fabuzhe")).equals(curUser.getObjectId())){
	                		
	                		tv_join.setText("活动签到");
                             join.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									Intent intent=new Intent(ActContent.this,Qiandao.class);
									intent.putExtra("act_Id", act.getObjectId());
									intent.putExtra("act_hour",act.getInt("shichang"));
									startActivity(intent);
								}
                             });
	                		
	                	}else{
	                		
	                		//检查是否已参加
			                final AVQuery<AVObject> join_query1 = new AVQuery<AVObject>("Join");
			                join_query1.whereEqualTo("actId", act.getObjectId());
							
							final AVQuery<AVObject> join_query2 = new AVQuery<AVObject>("Join");
							join_query2.whereEqualTo("userId", curUser.getObjectId());
							
							final AVQuery<AVObject> join_query = AVQuery.and(
									Arrays.asList(join_query1, join_query2));
							join_query.countInBackground(new CountCallback() {
					            @Override
					            public void done(int i, AVException e) {
					                if (e == null) {
					                    if(i==0){
					                    	tv_join.setText("我要参加");			         
					                    	ifJoin=false;
					                    }else{
					                    	tv_join.setText("退出活动");
					                    	ifJoin=true;
					                    }
					                } else {
					                    // 查询失败
					                }
					            }
					        });
							//点击参加
			                join.setOnClickListener(new View.OnClickListener() {
								
								@Override
								public void onClick(View v) {
									if(ifJoin){
										final AVQuery<AVObject> join_query3 = AVQuery.and(
												Arrays.asList(join_query1, join_query2));
										
										join_query3.findInBackground(new FindCallback<AVObject>() {
								            @Override
								            public void done(List<AVObject> list, AVException e) {
								            	AVObject delact=list.get(0);
		                                        delact.deleteInBackground();
		                      
		                                        act.increment("yicanjia", -1);
								            	act.saveInBackground();
								            	
		                                        tv_join.setText("我要参加");			         
		    			                    	ifJoin=false;
		    			                    	
		    			                    	yicanjia.setText(act.getNumber("yicanjia")+"");
		    			                    	
								            }
								        });


									}else{
										
										if( act.getInt("yicanjia")<act.getInt("renshu")){
											AVObject sc = new AVObject("Join");
											sc.put("userId", curUser.getObjectId());
											sc.put("userName",curUser.getString("username"));
											sc.put("actId", act.getObjectId());
											sc.put("actName",act.getString("name"));
											sc.put("actTime", act.getString("time"));
											sc.put("actPlace", act.getString("place"));
											sc.saveInBackground(new SaveCallback() {
										          @Override
										          public void done(AVException e) {
										            if (e == null) {
										            	new AlertDialog.Builder(ActContent.this).setMessage("成功加入！").setTitle("提示")
														.setPositiveButton("确定", null).show();

										            	
										            	act.increment("yicanjia", 1);
										            	act.saveInBackground();
										            	

										            	
										            	tv_join.setText("退出活动");
								                    	ifJoin=true;
								                    	
								                    	yicanjia.setText(act.getNumber("yicanjia")+"");
										            } else {
										              
										              Toast.makeText(ActContent.this, e.getMessage(), Toast.LENGTH_SHORT).show();
										            }
										          }
										        });
											
										}else{
											new AlertDialog.Builder(ActContent.this).setMessage("对不起，活动人数已满！").setTitle("提示")
											.setPositiveButton("确定", null).show();
										}
									
									
										
									}
									
								}
							});
		                	
	                		
	                	}
	                	
	                	
	                }
	                
	                
	                if(act.getInt("yicanjia")>0){
		                enterYicanjia.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent intent = new Intent(ActContent.this,ActJoinUser.class);
								intent.putExtra("act_Id", act.getObjectId());
								startActivity(intent);
							}
						});
	                }
                
	                enterActContent.setOnClickListener(new Button.OnClickListener(){
	                    @Override
	                    public  void onClick(View v){
	                    	Intent intent = new Intent();
					         //Intent传递参数
					         intent.putExtra("act_content", act.getString("content"));
					         intent.setClass(ActContent.this, ActContentDetail.class);
					         startActivity(intent);

	                    }
	                });
	                
	                if(act.getBoolean("ifZuzhi")){
	                	enterZuzhi.setVisibility(View.VISIBLE);
	                	enterZuzhiDetail.setOnClickListener(new View.OnClickListener() {
							
							@Override
							public void onClick(View v) {
								Intent intent = new Intent();
						         //Intent传递参数
						         intent.putExtra("zuzhi_Id", act.getString("fabuzhe"));
						         intent.setClass(ActContent.this, ZuzhiDetail.class);
						         startActivity(intent);
								
							}
						});
	                	
	                }else{
	                	enterZuzhi.setVisibility(View.INVISIBLE);
	                }
	            }
	        });
		
	}

}
