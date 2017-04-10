package com.example.uvol;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class FragementMain extends Fragment {

    private View view;// ��Ҫ���صĲ���
    private Button chitang;//У�ڳ�����ť

    private ViewPager viewPager; // �������
    private List<ImageView> imageViews; // ͼƬ����


    private int[] imageResId; // ͼƬID
    private List<View> dots; // Բ��

    private int currentItem = 0; // ��ǰͼƬ������


    // An ExecutorService that can schedule commands to run after a given delay,
    // or to execute periodically.
    private ScheduledExecutorService scheduledExecutorService;

    @Override
    public void onAttach(Activity activity){
        super.onAttach(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (view == null) {// �Ż�View����View�Ĵ�������
            view = inflater.inflate(R.layout.shouye_layout, null);
            return view;
        }
        return view;
    }


    //�л���ǰ��ʾ��ͼƬ
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            viewPager.setCurrentItem(currentItem);
        };
    };

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        imageResId = new int[] { R.drawable.ph1, R.drawable.ph2, R.drawable.ph3, R.drawable.ph4, R.drawable.ph5 };

        imageViews = new ArrayList<ImageView>();

        // ��ʼ��ͼƬ��Դ
        for (int i = 0; i < imageResId.length; i++) {
            ImageView imageView = new ImageView(getActivity());
            imageView.setImageResource(imageResId[i]);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageViews.add(imageView);
        }

        dots = new ArrayList<View>();
        dots.add(getView().findViewById(R.id.v_dot0));
        dots.add(getView().findViewById(R.id.v_dot1));
        dots.add(getView().findViewById(R.id.v_dot2));
        dots.add(getView().findViewById(R.id.v_dot3));
        dots.add(getView().findViewById(R.id.v_dot4));

        viewPager = (ViewPager) getView().findViewById(R.id.vp);
        viewPager.setAdapter(new MyAdapter());// �������ViewPagerҳ���������
        // ����һ������������ViewPager�е�ҳ��ı�ʱ����
        viewPager.setOnPageChangeListener(new MyPageChangeListener());
    }


    @Override
    public void onStart() {
        scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
        // ��Activity��ʾ������ÿ�������л�һ��ͼƬ��ʾ
        scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 1,3, TimeUnit.SECONDS);
        super.onStart();
    }

    @Override
    public void onStop() {
        // ��Activity���ɼ���ʱ��ֹͣ�л�
        scheduledExecutorService.shutdown();
        super.onStop();
    }

    //ִ������
    private class ScrollTask implements Runnable {

        public void run() {
            synchronized (viewPager) {
                currentItem = (currentItem + 1) % imageViews.size();
                handler.obtainMessage().sendToTarget(); // ͨ��Handler�л�ͼƬ
            }
        }

    }

    //��ViewPager��ҳ���״̬�����ı�ʱ����
    private class MyPageChangeListener implements ViewPager.OnPageChangeListener {
        private int oldPosition = 0;

        /**
         * This method will be invoked when a new page becomes selected.
         * position: Position index of the new selected page.
         */
        public void onPageSelected(int position) {
            currentItem = position;
            dots.get(oldPosition).setBackgroundResource(R.drawable.dot_normal);
            dots.get(position).setBackgroundResource(R.drawable.dot_focused);
            oldPosition = position;
        }

        public void onPageScrollStateChanged(int arg0) {

        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }
    }

    //���ViewPagerҳ���������
    private class MyAdapter extends PagerAdapter {

        @Override
        public int getCount() {
            return imageResId.length;
        }

        @Override
        public Object instantiateItem(View arg0, int arg1) {
            ((ViewPager) arg0).addView(imageViews.get(arg1));
            return imageViews.get(arg1);
        }

        @Override
        public void destroyItem(View arg0, int arg1, Object arg2) {
            ((ViewPager) arg0).removeView((View) arg2);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
        public void restoreState(Parcelable arg0, ClassLoader arg1) {

        }

        @Override
        public Parcelable saveState() {
            return null;
        }


    }
    
    


}
