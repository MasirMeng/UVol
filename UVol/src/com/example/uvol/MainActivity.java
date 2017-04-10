package com.example.uvol;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;

public class MainActivity extends FragmentActivity {
    // 布局管理器
    private FragmentManager fManager;

    private FragementMain fragment_home;
    private FragementFabu fragment_fabu;
    private FragementSelf fragment_self;

    // 首页
    private ImageView iv_menu_home;

    // 活动发布
    private ImageView iv_menu_fabu;

    // 个人中心
    private ImageView iv_menu_self;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        // 初始化组件
        initViews();
        // 默认先点中第一个“首页”
        clickMenu(findViewById(R.id.menu_home));
    }

    private void initViews() {
        // 布局管理器
        fManager = getSupportFragmentManager();

        iv_menu_home = (ImageView)findViewById(R.id.iv_menu_home);

        iv_menu_fabu = (ImageView)findViewById(R.id.iv_menu_fabu);

        iv_menu_self = (ImageView)findViewById(R.id.iv_menu_self);

    }

    /**
     * 点击底部菜单事件
     */
    public void clickMenu(View v){
        FragmentTransaction trans = fManager.beginTransaction();
        int vID = v.getId();
        // 设置menu样式
        setMenuStyle(vID);
        // 隐藏所有的fragment
        hideFrament(trans);
        // 设置Fragment
        setFragment(vID,trans);
        trans.commit();
    }

    /**
     * 隐藏所有的fragment(编程初始化状态)
     */
    private void hideFrament(FragmentTransaction trans) {
        if(fragment_home!=null){
            trans.hide(fragment_home);
        }
        if(fragment_fabu!=null){
            trans.hide(fragment_fabu);
        }
        if(fragment_self!=null){
            trans.hide(fragment_self);
        }
    }

    /**
     * 设置menu样式
     */
    private void setMenuStyle(int vID) {
        iv_menu_home.setImageDrawable(getResources().getDrawable(R.drawable.zhuye));
        iv_menu_fabu.setImageDrawable(getResources().getDrawable(R.drawable.fabu));
        iv_menu_self.setImageDrawable(getResources().getDrawable(R.drawable.self));

        // 首页
        if(vID==R.id.menu_home){
            iv_menu_home.setImageDrawable(getResources().getDrawable(R.drawable.zhuye_pressed));
        }else if(vID==R.id.menu_fabu){
            iv_menu_fabu.setImageDrawable(getResources().getDrawable(R.drawable.fabu_pressed));
        }else if(vID==R.id.menu_self){
            iv_menu_self.setImageDrawable(getResources().getDrawable(R.drawable.self_pressed));
        }
    }

    /**
     * 设置Fragment
     */
    private void setFragment(int vID,FragmentTransaction trans) {
        switch (vID) {
            case R.id.menu_home:
                if(fragment_home==null){
                    fragment_home = new FragementMain();
                    trans.add(R.id.content, fragment_home);
                }else{
                    trans.show(fragment_home);
                }
                break;
            case R.id.menu_fabu :
                if(fragment_fabu==null){
                    fragment_fabu = new FragementFabu();
                    trans.add(R.id.content, fragment_fabu);
                }else{
                    trans.show(fragment_fabu);
                }
                break;
            case R.id.menu_self:
            	fragment_self = new FragementSelf();
                trans.add(R.id.content, fragment_self);
                trans.show(fragment_self);
//                if(fragment_self==null){
//                    fragment_self = new FragementSelf();
//                    trans.add(R.id.content, fragment_self);
//                }else{
//                    trans.show(fragment_self);
//                }
                break;
            default:
                break;
        }
    }
    
    public void searchAct(View v){
    	int vId= v.getId();
    	if(vId==R.id.search_act){
    		Intent intent=new Intent(this,SearchAct.class);
        	startActivity(intent);
    	}
    	
    }
    
    public void searchZuzhi(View v){
    	int vId= v.getId();
    	if(vId==R.id.search_zuzhi){
    		Intent intent=new Intent(this,SearchZuzhi.class);
        	startActivity(intent);
    	}
    	
    }
    
    public void enter_tuijian(View v){
    	int vId= v.getId();
    	if(vId==R.id.xiyang){
    		Intent intent=new Intent(this,ActContent.class);
    		intent.putExtra("act_Id", "58d61d0061ff4b006cc6273e");
        	startActivity(intent);
    	}else if(vId==R.id.meihua){
    		Intent intent=new Intent(this,ActContent.class);
    		intent.putExtra("act_Id", "58d39878570c350058c3dc04");
        	startActivity(intent);
    	}
    }
    
    public void clickAct(View v){
    	int vID = v.getId();
    	int type=0;
    	if(vID==R.id.weisheng){
    		type=0;
    	}else if(vID==R.id.zhijiao){
    		type=1;
    	}else if(vID==R.id.wenhua){
    		type=2;
    	}else if(vID==R.id.shengtai){
    		type=3;
    	}else if(vID==R.id.dongwu){
    		type=4;
    	}else if(vID==R.id.retong){
    		type=5;
    	}else if(vID==R.id.juankuan){
    		type=6;
    	}else if(vID==R.id.laoren){
    		type=7;
    	}else if(vID==R.id.qita){
    		type=8;
    	}

       	 Intent intent = new Intent();
         //Intent传递参数
         intent.putExtra("type", type);
         intent.setClass(this, EnterActivity.class);
         startActivity(intent);

    	  	
    }


}
