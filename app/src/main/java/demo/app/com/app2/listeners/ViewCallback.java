package demo.app.com.app2.listeners;

import java.util.List;

import demo.app.com.app2.models.ClientInfo;

/**
 * Created by root on 28/12/17.
 */

public interface ViewCallback {

    void editData(ClientInfo clientInfo);

    void deleteData(int position);

    void noData(int count);

}
