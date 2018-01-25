package demo.app.com.app2.di.components;


import javax.inject.Singleton;

import dagger.Component;
import demo.app.com.app2.AppContext;
import demo.app.com.app2.detailsFragment.DetailFragment;
import demo.app.com.app2.detailsFragment.DetailFragmentPresenter;
import demo.app.com.app2.di.modules.AppModule;
import demo.app.com.app2.di.modules.DatabaseModules;
import demo.app.com.app2.homeFragment.HomeFragment;
import demo.app.com.app2.mainActivity.MainActivity;
import demo.app.com.app2.mainActivity.MainActivityPresenter;
import demo.app.com.app2.homeFragment.HomeFragmentPresenter;

/**
 * Created by root on 27/11/17.
 */



@Singleton
@Component(modules = {AppModule.class, DatabaseModules.class})
public interface AppComponent {

    void inject(AppContext appContext);

    void inject(MainActivity activity);

    void inject(MainActivityPresenter presenter);

    void inject(HomeFragmentPresenter presenter);

    void inject(DetailFragmentPresenter presenter);

    void inject(DetailFragment fragment);

    void inject(HomeFragment fragment );





}
