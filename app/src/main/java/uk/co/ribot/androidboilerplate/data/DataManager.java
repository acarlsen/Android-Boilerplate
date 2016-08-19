package uk.co.ribot.androidboilerplate.data;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Provider;
import javax.inject.Singleton;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import rx.Observable;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.GitHubUser;
import uk.co.ribot.androidboilerplate.data.remote.GitHubService;

@Singleton
public class DataManager {

    private final GitHubService mGitHubService;
    private final Provider<Realm> realmProvider;
    private final PreferencesHelper mPreferencesHelper;

    @Inject
    public DataManager(GitHubService gitHubService, PreferencesHelper preferencesHelper, Provider<Realm> realmProvider) {
        this.mGitHubService = gitHubService;
        this.mPreferencesHelper = preferencesHelper;
        this.realmProvider = realmProvider;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public Observable<GitHubUser> syncGitHubUsers() {

        return getGitHubUsersInBackground()
                .flatMap(users -> {
                    // Emit each person individually
                    return Observable.from(users);
                })
                .flatMap(user -> {
                    // get GitHub statistics. Retrofit automatically does this on a separate thread.
                    return mGitHubService.user(user.getLogin());
                })
                .doOnNext(user -> {
                    Realm realm = realmProvider.get();
                    realm.beginTransaction();
                    realm.copyToRealmOrUpdate(user);
                    realm.commitTransaction();
                });


    }

    private Observable<List<GitHubUser>> getGitHubUsersInBackground() {
        return Observable.defer(() -> {
            try (Realm realm = realmProvider.get()) {
                List<GitHubUser> users = new ArrayList<>();
                for (GitHubUser result : realm.where(GitHubUser.class).findAllSorted("name")) {
                    users.add(realm.copyFromRealm(result));
                }
                return Observable.just(users);
            }
        });
    }

    public Observable<RealmResults<GitHubUser>> getGitHubUsers() {
        return realmProvider.get().where(GitHubUser.class).findAllSortedAsync("name", Sort.ASCENDING).asObservable();
    }
}
