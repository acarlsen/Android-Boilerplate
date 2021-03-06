package uk.co.ribot.androidboilerplate;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.Collections;
import java.util.List;

import rx.Observable;
import uk.co.ribot.androidboilerplate.data.DataManager;
import uk.co.ribot.androidboilerplate.test.common.TestDataFactory;
import uk.co.ribot.androidboilerplate.ui.main.MainMvpView;
import uk.co.ribot.androidboilerplate.ui.main.MainPresenter;
import uk.co.ribot.androidboilerplate.util.RxSchedulersOverrideRule;

import static org.mockito.Matchers.anyListOf;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainPresenterTest {

    @Mock MainMvpView mMockMainMvpView;
    @Mock DataManager mMockDataManager;
    private MainPresenter mMainPresenter;

    @Rule
    public final RxSchedulersOverrideRule mOverrideSchedulersRule = new RxSchedulersOverrideRule();

    @Before
    public void setUp() {
        mMainPresenter = new MainPresenter(mMockDataManager);
        mMainPresenter.attachView(mMockMainMvpView);
    }

    @After
    public void tearDown() {
        mMainPresenter.detachView();
    }

    @Test
    public void loadRibotsReturnsRibots() {
        List<Ribot> ribots = TestDataFactory.makeListUsers(10);
        when(mMockDataManager.getGitHubUsers())
                .thenReturn(Observable.just(ribots));

        mMainPresenter.loadUsers();
        verify(mMockMainMvpView).showUsers(ribots);
        verify(mMockMainMvpView, never()).showUsersEmpty();
        verify(mMockMainMvpView, never()).showError();
    }

    @Test
    public void loadRibotsReturnsEmptyList() {
        when(mMockDataManager.getGitHubUsers())
                .thenReturn(Observable.just(Collections.<Ribot>emptyList()));

        mMainPresenter.loadUsers();
        verify(mMockMainMvpView).showUsersEmpty();
        verify(mMockMainMvpView, never()).showUsers(anyListOf(Ribot.class));
        verify(mMockMainMvpView, never()).showError();
    }

    @Test
    public void loadRibotsFails() {
        when(mMockDataManager.getGitHubUsers())
                .thenReturn(Observable.<List<Ribot>>error(new RuntimeException()));

        mMainPresenter.loadUsers();
        verify(mMockMainMvpView).showError();
        verify(mMockMainMvpView, never()).showUsersEmpty();
        verify(mMockMainMvpView, never()).showUsers(anyListOf(Ribot.class));
    }

}