package uk.co.ribot.androidboilerplate.data.model;

/**
 * Copyright @ Tacit Dynamics ApS 2016
 * <p/>
 * Created by Anders on 19-08-2016.
 */

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class GitHubUser extends RealmObject {

    // All fields are by default persisted.
    @PrimaryKey
    public String login;
    public String name;
    public int public_repos;
    public int public_gists;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPublic_repos() {
        return public_repos;
    }

    public void setPublic_repos(int public_repos) {
        this.public_repos = public_repos;
    }

    public int getPublic_gists() {
        return public_gists;
    }

    public void setPublic_gists(int public_gists) {
        this.public_gists = public_gists;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

}