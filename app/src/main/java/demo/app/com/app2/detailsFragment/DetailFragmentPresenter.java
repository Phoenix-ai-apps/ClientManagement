package demo.app.com.app2.detailsFragment;

import demo.app.com.app2.di.DependencyInjector;

/**
 * Created by root on 23/12/17.
 */

public class DetailFragmentPresenter implements DetailFragmentContract.Presenter {

    private DetailFragmentContract.View mView;


    public <T extends DetailFragment & DetailFragmentContract.View> DetailFragmentPresenter(T view) {
        this.mView = view;

        DependencyInjector.appComponent().inject(this);

    }


    @Override
    public void start() {

    }
}
