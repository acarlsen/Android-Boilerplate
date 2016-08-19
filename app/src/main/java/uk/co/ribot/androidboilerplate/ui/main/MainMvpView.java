package uk.co.ribot.androidboilerplate.ui.main;

import java.util.List;

import uk.co.ribot.androidboilerplate.data.model.GitHubUser;
import uk.co.ribot.androidboilerplate.ui.base.MvpView;

public interface MainMvpView extends MvpView {

    void showUsers(List<GitHubUser> users);

    void showUsersEmpty();

    void showError();

}
