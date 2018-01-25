package demo.app.com.app2.helper;

import demo.app.com.app2.constants.AppConstants;
import demo.app.com.app2.constants.DatabaseConstants;

/**
 * Created by root on 23/12/17.
 */

public interface HelperInterface extends AppConstants,DatabaseConstants {

    ApplicationHelper getHelper();
}
