package demo.app.com.app2;

import android.content.Context;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.text.TextUtils;

import demo.app.com.app2.di.DependencyInjector;
import demo.app.com.app2.di.components.AppComponent;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;


public class AppContext extends MultiDexApplication {

	private static final String TAG = "AppContext";
	private static AppContext instance;
	private static Context appContext = null;
	private static AppComponent appComponent;

	@Override
	public void onCreate() {
		super.onCreate();
		instance=this;

		initializeCalligraphy();
		initDependencies();

		MultiDex.install(this);

	}

	private void initializeCalligraphy() {

		CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
				.setDefaultFontPath("fonts/Lato-Regular.ttf")
				.setFontAttrId(R.attr.fontPath)
				.build());
	}

	private void initDependencies() {

		DependencyInjector.initialize(this);
		DependencyInjector.appComponent().inject(this);

	}

	public static AppContext getInstance() {
		return instance;
	}


	public static AppComponent getAppComponent(){
		// App Compent Object
		return appComponent;
	}

}
