package uk.co.ribot.androidboilerplate;

import android.app.Application;
import android.content.Context;

import java.util.Map;
import java.util.TreeMap;

import io.realm.Realm;
import timber.log.Timber;
import uk.co.ribot.androidboilerplate.data.model.GitHubUser;
import uk.co.ribot.androidboilerplate.injection.component.ApplicationComponent;
import uk.co.ribot.androidboilerplate.injection.component.DaggerApplicationComponent;
import uk.co.ribot.androidboilerplate.injection.module.ApplicationModule;

public class BoilerplateApplication extends Application  {

    ApplicationComponent mApplicationComponent;

    private static final TreeMap<String, String> testPersons = new TreeMap<>();
    static {
        testPersons.put("Anders", "acarlsen");
        testPersons.put("Christian", "cmelchior");
        testPersons.put("Emanuele", "emanuelez");
        testPersons.put("Donn", "donnfelker");
        testPersons.put("Nabil", "nhachicha");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
            //Fabric.with(this, new Crashlytics());
        }

        // Create test data
        createTestData();
    }

    // Create test data
    private void createTestData() {
        Realm realm = getComponent().realm();
        realm.executeTransaction(realm1 -> {
            for (Map.Entry<String, String> entry : testPersons.entrySet()) {
                GitHubUser p = new GitHubUser();
                p.setLogin(entry.getValue());
                p.setName(entry.getKey());
                realm1.copyToRealmOrUpdate(p);
            }
        });
        realm.close();
    }

    public static BoilerplateApplication get(Context context) {
        return (BoilerplateApplication) context.getApplicationContext();
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }
}
