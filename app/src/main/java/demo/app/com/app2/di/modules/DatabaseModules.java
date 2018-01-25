package demo.app.com.app2.di.modules;

import android.content.Context;
import dagger.Module;
import dagger.Provides;
import demo.app.com.app2.database.dataSource.ClientInfoDataSource;

/**
 * Created by root on 29/11/17.
 */

@Module
public class DatabaseModules {

@Provides
ClientInfoDataSource provideClientInfoDataSource(Context context){
    return new ClientInfoDataSource(context);
}


}
