package demo.app.com.app2.mainActivity;

import java.util.List;

import javax.inject.Inject;

import demo.app.com.app2.database.dataSource.ClientInfoDataSource;
import demo.app.com.app2.di.DependencyInjector;
import demo.app.com.app2.models.ClientInfo;

/**
 * Created by root on 23/12/17.
 */

public class MainActivityPresenter implements MainActivityContract.Presenter {

    private MainActivityContract.View mView;

    @Inject
    ClientInfoDataSource clientInfoDataSource;


    public <T extends MainActivity & MainActivityContract.View> MainActivityPresenter(T view) {
        this.mView = view;

        DependencyInjector.appComponent().inject(this);

    }


    @Override
    public void start() {

    }



}
