package demo.app.com.app2.di.modules;

import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import demo.app.com.app2.AppContext;
import demo.app.com.app2.helper.ApplicationHelper;
import demo.app.com.app2.helper.HelperInterface;

/**
 * Created by root on 27/11/17.
 */

@Module
public class AppModule implements HelperInterface {

    private final AppContext application;

    public AppModule(AppContext application) {
        this.application = application;
    }

    @Provides
    @Singleton
    Context provideContext() {
        return application;
    }

    @Provides
    AppContext provideApplication() {
        return application;
    }

    @Override
    public ApplicationHelper getHelper() {
        return ApplicationHelper.getInstance();
    }
}
