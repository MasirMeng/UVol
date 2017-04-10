package com.example.uvol;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.avos.avoscloud.AVException;
import com.avos.avoscloud.AVFile;
import com.avos.avoscloud.AVObject;
import com.avos.avoscloud.AVUser;
import com.avos.avoscloud.SaveCallback;
import com.prepare.Login;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD) public class FragementFabu extends Fragment {
	
	private View view;
	private AVUser curUser;
	
	private Button ackBtn;
	private ImageButton findPic;
	private EditText actName;
	private EditText actContent;
	private EditText actPlace;
	private EditText actTime;
	private EditText needNumber;
	private EditText actFuzeren;
	private EditText telphone;
	private EditText actHours;
	private ImageView actImage;
	private CheckBox wsyl;
	private CheckBox axzj;
	private CheckBox whys;
	private CheckBox sthj;
	private CheckBox bhdw;
	private CheckBox gaet;
	private CheckBox zxjk;
	private CheckBox galr;
	private CheckBox qita;
	private List<CheckBox> checkBoxs=new ArrayList<CheckBox>();
	
	private byte[] mImageBytes = null;



    @Override
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fabu_main, container, false);
        
        curUser=AVUser.getCurrentUser();
        
        ackBtn=(Button)view.findViewById(R.id.btn_ack);
        findPic=(ImageButton)view.findViewById(R.id.get_photo);
        actName=(EditText)view.findViewById(R.id.huodong_name);
        actContent=(EditText)view.findViewById(R.id.huodong_neirong);
        actPlace=(EditText)view.findViewById(R.id.huodong_didian);
        actTime=(EditText)view.findViewById(R.id.huodong_shijian);
        needNumber=	(EditText)view.findViewById(R.id.huodong_renshu);
        actFuzeren=(EditText)view.findViewById(R.id.huodong_fuze);
        telphone=(EditText)view.findViewById(R.id.fuze_dianhua);
        actHours=(EditText)view.findViewById(R.id.huodong_shichang);
        actImage=(ImageView)view.findViewById(R.id.act_photo);
        wsyl=(CheckBox)view.findViewById(R.id.type_wsyl);
        axzj=(CheckBox)view.findViewById(R.id.type_axzj);
        whys=(CheckBox)view.findViewById(R.id.type_whys);
        sthj=(CheckBox)view.findViewById(R.id.type_sthj);
        bhdw=(CheckBox)view.findViewById(R.id.type_bhdw);
        gaet=(CheckBox)view.findViewById(R.id.type_gaet);
        zxjk=(CheckBox)view.findViewById(R.id.type_zxjk);
        galr=(CheckBox)view.findViewById(R.id.type_galr);
        qita=(CheckBox)view.findViewById(R.id.type_else);

        
        checkBoxs.add(wsyl);
        checkBoxs.add(axzj);
        checkBoxs.add(whys);
        checkBoxs.add(sthj);
        checkBoxs.add(bhdw);
        checkBoxs.add(gaet);
        checkBoxs.add(zxjk);
        checkBoxs.add(galr);
        checkBoxs.add(qita);
        
        ackBtn.setOnClickListener(new View.OnClickListener() {
        	@Override
            public void onClick(View view) {
        		getInfo();
            }
        });
        
        findPic.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
		        intent.setType("image/*");
		        startActivityForResult(intent, 42);
				
			}
		});
        
        return view;		
    }
    

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
      super.onActivityResult(requestCode, resultCode, data);
      if (requestCode == 42 && resultCode == Activity.RESULT_OK) {
        try {
          actImage.setImageBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData()));
          mImageBytes = getBytes(getActivity().getContentResolver().openInputStream(data.getData()));
        } catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
    
    private void initView(){
    	actName.setText(null);
    	actContent.setText(null);
    	actPlace.setText(null);
    	actTime.setText(null);
    	needNumber.setText(null);
    	actFuzeren.setText(null);
    	telphone.setText(null);
    	actHours.setText(null);
    	wsyl.setChecked(false);
    	axzj.setChecked(false);
    	whys.setChecked(false);
    	sthj.setChecked(false);
    	bhdw.setChecked(false);
    	gaet.setChecked(false);
    	zxjk.setChecked(false);
    	galr.setChecked(false);
    	qita.setChecked(false);
    	actImage.setImageResource(R.drawable.ph1);
    }

    public byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];
        int len;
        while ((len = inputStream.read(buffer)) != -1) {
          byteArrayOutputStream.write(buffer, 0, len);
        }
        return byteArrayOutputStream.toByteArray();
      }

	 private void getInfo(){
		final String name=actName.getText().toString();
		final String content=actContent.getText().toString();
		final String place=actPlace.getText().toString();
		final String time=actTime.getText().toString();
		final String number=needNumber.getText().toString();
		final String fuzeren=actFuzeren.getText().toString();
		final String tel=telphone.getText().toString();
		final String hours=actHours.getText().toString();
		String type="";

		
		if(!name.isEmpty()&&!content.isEmpty()&&!place.isEmpty()&&!time.isEmpty()
				&&!number.isEmpty()&&!fuzeren.isEmpty()&&!tel.isEmpty()&&!hours.isEmpty()){
			for(CheckBox cbx:checkBoxs){
				if(cbx.isChecked()){
					type+=cbx.getText()+" ";
				}
			}
			if("".equals(type)){
				new AlertDialog.Builder(getActivity()).setMessage("还没选择活动类型").setTitle("提示")
				.setPositiveButton("关闭", null).show();
				return;
			}
			
			try{
				final int actNum=Integer.parseInt(number);
				final int actHours=Integer.parseInt(hours);
				final String actLeixing=type;
				if(actNum>0&&actHours>0){
					final AVObject act = new AVObject("Activity");
					act.put("name", name);
					act.put("content", content);
					act.put("place", place);
					act.put("time", time);
					act.put("renshu", actNum);
					act.put("fuzeren", fuzeren);
					act.put("telphone", tel);
					act.put("shichang", actHours);
					act.put("type",type);
					act.put("fabuzhe",curUser.getObjectId() );
					act.put("fabuname", curUser.getString("username"));
					
					if(mImageBytes != null){
						act.put("image", new AVFile("actPic", mImageBytes));
					}

					
					act.saveInBackground(new SaveCallback() {
				          @Override
				          public void done(AVException e) {
				            if (e == null) {
				            	new AlertDialog.Builder(getActivity()).setMessage("活动成功发布！").setTitle("提示")
								.setPositiveButton("确定", null).show();
				            	initView();
				            	
								curUser.increment("Act_count", 1);
								curUser.saveInBackground();
				            } else {
				              
				              Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_SHORT).show();
				            }
				          }
				        });
				}
			}catch(Exception e){
				new AlertDialog.Builder(getActivity()).setMessage("输入有误").setTitle("提示")
				.setPositiveButton("关闭", null).show();
			}
			
		}else{
			new AlertDialog.Builder(getActivity()).setMessage(R.string.fabu_field_required).setTitle("提示")
			.setPositiveButton("关闭", null).show();
			
		}
	}




}
