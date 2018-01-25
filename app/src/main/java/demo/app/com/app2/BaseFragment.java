package demo.app.com.app2;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import demo.app.com.app2.helper.HelperInterface;


public abstract class BaseFragment <T extends BasePresenter> extends Fragment implements BaseView,HelperInterface {

    protected T mPresenter;
    private Unbinder unbinder;
    private final Handler handler = new Handler();
    protected BaseActivity mActivity;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(getLayout(), container, false);
        ButterKnife.setDebug(true);

        unbinder = ButterKnife.bind(this,rootView);
        mPresenter = getPresenter();
        if(mPresenter!=null) {
            mPresenter.start();
        }
        return rootView;
    }

    @Override
    public void showError(String message) {
        hideProgress();
        Toast.makeText(mActivity,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showMessage(String message) {
        hideProgress();
        Toast.makeText(mActivity,message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showProgress() {
        if(mActivity!=null) {
            mActivity.showProgress();
        }
    }

    @Override
    public void hideProgress() {
        if(mActivity!=null) {
            mActivity.hideProgress();
        }
    }


    @Override
    public void finish() {
        mActivity.finish();
    }

    protected abstract T getPresenter();
    protected abstract int getLayout();

    @Override
    public void onDestroyView() {
        if(unbinder!=null) {
            unbinder.unbind();
        }
        super.onDestroyView();

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        mActivity= (BaseActivity) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mActivity=null;
    }

}
