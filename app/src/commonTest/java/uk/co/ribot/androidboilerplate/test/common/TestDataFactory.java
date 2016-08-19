package uk.co.ribot.androidboilerplate.test.common;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import uk.co.ribot.androidboilerplate.data.model.GitHubUser;

/**
 * Factory class that makes instances of data models with random field values.
 * The aim of this class is to help setting up test fixtures.
 */
public class TestDataFactory {

    public static String randomUuid() {
        return UUID.randomUUID().toString();
    }

    public static List<GitHubUser> makeListUsers(int number) {
        List<GitHubUser> users = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            users.add(makeUser(String.valueOf(i)));
        }
        return users;
    }

    public static GitHubUser makeUser(String uniqueSuffix) {
        GitHubUser user = new GitHubUser();
        user.setName("Name-" + uniqueSuffix);
        user.setLogin("Login-" + uniqueSuffix);
        user.setPublic_gists(5);
        return user;
    }
}