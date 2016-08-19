package uk.co.ribot.androidboilerplate.ui.main;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import uk.co.ribot.androidboilerplate.R;
import uk.co.ribot.androidboilerplate.data.model.GitHubUser;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.GitHubUserViewHolder> {

    private List<GitHubUser> mUsers;

    @Inject
    public UserAdapter() {
        mUsers = new ArrayList<>();
    }

    public void setUsers(List<GitHubUser> ribots) {
        mUsers = ribots;
    }

    @Override
    public GitHubUserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_ribot, parent, false);
        return new GitHubUserViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GitHubUserViewHolder holder, int position) {
        GitHubUser user = mUsers.get(position);
        holder.hexColorView.setBackgroundColor(position % 2 == 0 ? 0x009688 : 0x004D40);
        holder.nameTextView.setText(String.format("%s (%s)",
                user.name, user.login));
        holder.emailTextView.setText("Public repos: " + user.getPublic_repos());
    }

    @Override
    public int getItemCount() {
        return mUsers.size();
    }

    class GitHubUserViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.view_hex_color) View hexColorView;
        @BindView(R.id.text_name) TextView nameTextView;
        @BindView(R.id.text_email) TextView emailTextView;

        public GitHubUserViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
