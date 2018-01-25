package demo.app.com.app2;


import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import butterknife.ButterKnife;
import demo.app.com.app2.helper.HelperInterface;

public abstract class BaseActivity<T extends BasePresenter> extends AppCompatActivity implements BaseView, HelperInterface {
    public T mPresenter;

	@Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        ButterKnife.bind(this);
        mPresenter = getPresenter();

        if(mPresenter!=null) {
            mPresenter.start();
        }
    }

    protected abstract T getPresenter();

    @Override
    public void showError(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void showMessage(String message) {
        try {
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}