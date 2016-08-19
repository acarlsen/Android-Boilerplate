package uk.co.ribot.androidboilerplate.data.remote;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import uk.co.ribot.androidboilerplate.data.model.GitHubUser;

public interface GitHubService {

    String ENDPOINT = "https://api.github.com/";

    @GET("/users/{user}")
    Observable<GitHubUser> user(@Path("user") String user);

    /******** Helper class that sets up a new services *******/
    class Creator {

        public static GitHubService newGitHubService() {
            Gson gson = new GsonBuilder()
                    .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'")
                    .create();
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(GitHubService.ENDPOINT)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .build();

            return retrofit.create(GitHubService.class);
        }
    }

}
