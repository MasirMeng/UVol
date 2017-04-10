package com.prepare;

import android.app.Application;
import com.avos.avoscloud.AVOSCloud;
import com.avos.avoscloud.AVAnalytics;

public class ConnectLeancloud extends Application {

	  @Override
	  public void onCreate() {
	    super.onCreate();
	    AVOSCloud.initialize(this,"pAKLuX8clX1tYnWdo68CJ6Vl-gzGzoHsz",
	    		"gdQuGQEeQeLXyEnqkSUprg8f");
	    AVOSCloud.setDebugLogEnabled(true);
	    AVAnalytics.enableCrashReport(this, true);
	  }

}

