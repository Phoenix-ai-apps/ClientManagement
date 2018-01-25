package demo.app.com.app2.homeFragment;

import demo.app.com.app2.di.DependencyInjector;

/**
 * Created by root on 23/12/17.
 */

public class HomeFragmentPresenter implements HomeFragmentContract.Presenter {

    private HomeFragmentContract.View mView;


    public <T extends HomeFragment & HomeFragmentContract.View> HomeFragmentPresenter(T view) {
        this.mView = view;

        DependencyInjector.appComponent().inject(this);

    }


    @Override
    public void start() {

    }
}
