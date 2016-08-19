package uk.co.ribot.androidboilerplate;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.List;

import javax.inject.Provider;

import io.realm.Realm;
import rx.Observable;
import rx.observers.TestSubscriber;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.data.local.PreferencesHelper;
import uk.co.ribot.androidboilerplate.data.model.GitHubUser;
import uk.co.ribot.androidboilerplate.data.remote.GitHubService;
import uk.co.ribot.androidboilerplate.test.common.TestDataFactory;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * This test class performs local unit tests without dependencies on the Android framework
 * For testing methods in the DataManager follow this approach:
 * 1. Stub mock helper classes that your method relies on. e.g. RetrofitServices or DatabaseHelper
 * 2. Test the Observable using TestSubscriber
 * 3. Optionally write a SEPARATE test that verifies that your method is calling the right helper
 * using Mockito.verify()
 */
@RunWith(MockitoJUnitRunner.class)
public class DataManagerTest {

    @Mock Provider<Realm> mMockRealmProvider;
    @Mock PreferencesHelper mMockPreferencesHelper;
    @Mock GitHubService mMockGitHubService;
    private DataManager mDataManager;

    @Before
    public void setUp() {
        mDataManager = new DataManager(mMockGitHubService, mMockPreferencesHelper,
                mMockRealmProvider);
    }

    @Test
    public void syncGitHubUsersEmitsValues() {
        List<GitHubUser> GitHubUsers = Arrays.asList(TestDataFactory.makeUser("r1"),
                TestDataFactory.makeUser("r2"));
        stubSyncGitHubUsersHelperCalls(GitHubUsers);

        TestSubscriber<GitHubUser> result = new TestSubscriber<>();
        mDataManager.syncGitHubUsers().subscribe(result);
        result.assertNoErrors();
        result.assertReceivedOnNext(GitHubUsers);
    }

    @Test
    public void syncGitHubUsersCallsApiAndDatabase() {
        List<GitHubUser> GitHubUsers = Arrays.asList(TestDataFactory.makeUser("r1"),
                TestDataFactory.makeUser("r2"));
        stubSyncGitHubUsersHelperCalls(GitHubUsers);

        mDataManager.syncGitHubUsers().subscribe();
        // Verify right calls to helper methods
        verify(mMockGitHubService).getGitHubUsers();
        verify(mMockRealmProvider).setGitHubUsers(GitHubUsers);
    }

    @Test
    public void syncGitHubUsersDoesNotCallDatabaseWhenApiFails() {
        when(mMockGitHubService.getGitHubUsers())
                .thenReturn(Observable.<List<GitHubUser>>error(new RuntimeException()));

        mDataManager.syncGitHubUsers().subscribe(new TestSubscriber<GitHubUser>());
        // Verify right calls to helper methods
        verify(mMockGitHubService).getGitHubUsers();
        verify(mMockRealmProvider, never()).setGitHubUsers(anyListOf(GitHubUser.class));
    }

    private void stubSyncGitHubUsersHelperCalls(List<GitHubUser> GitHubUsers) {
        // Stub calls to the GitHubUser service and database helper.
        when(mMockGitHubService.getGitHubUsers())
                .thenReturn(Observable.just(GitHubUsers));
        when(mMockRealmProvider.setGitHubUsers(GitHubUsers))
                .thenReturn(Observable.from(GitHubUsers));
    }

}
