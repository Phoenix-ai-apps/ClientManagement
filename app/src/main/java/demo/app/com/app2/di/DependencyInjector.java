package demo.app.com.app2.di;


import demo.app.com.app2.AppContext;
import demo.app.com.app2.di.components.AppComponent;
import demo.app.com.app2.di.components.DaggerAppComponent;
import demo.app.com.app2.di.modules.AppModule;
import demo.app.com.app2.di.modules.DatabaseModules;

public class DependencyInjector {

    private static AppComponent appComponent;

    private DependencyInjector() {
    }

    public static void initialize(AppContext appContext) {
        appComponent = DaggerAppComponent.builder()
                .appModule(new AppModule(appContext))
                .databaseModules(new DatabaseModules())
                .build();
    }

    public static AppComponent appComponent() {
        return appComponent;
    }
}
