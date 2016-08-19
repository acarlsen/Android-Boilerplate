package uk.co.ribot.androidboilerplate.injection.component;

import android.app.Application;
import android.content.Context;

import javax.inject.Singleton;

import dagger.Component;
import io.realm.Realm;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.SyncService;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.remote.GitHubService;
import uk.co.ribot.androidboilerplate.injection.ApplicationContext;
import uk.co.ribot.androidboilerplate.injection.module.ApplicationModule;
import uk.co.ribot.androidboilerplate.util.RxEventBus;

@Singleton
@Component(modules = ApplicationModule.class)
public interface ApplicationComponent {

    void inject(SyncService syncService);

    @ApplicationContext Context context();
    Application application();
    GitHubService ribotsService();
    Realm realm();
    PreferencesHelper preferencesHelper();
    DataManager dataManager();
    RxEventBus eventBus();

}
