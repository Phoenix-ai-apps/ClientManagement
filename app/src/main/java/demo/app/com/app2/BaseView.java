package demo.app.com.app2;

public interface  BaseView {


	void showProgress();
	void hideProgress();
	void showError(String message);
	void showMessage(String message);
	void finish();
}
