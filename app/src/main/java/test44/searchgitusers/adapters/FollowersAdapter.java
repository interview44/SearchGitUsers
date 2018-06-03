package test44.searchgitusers.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import test44.searchgitusers.R;
import test44.searchgitusers.modals.FollowersModal;

public class FollowersAdapter extends RecyclerView.Adapter<FollowersAdapter.ViewHolder> {
    private ArrayList<FollowersModal> followersList;
    private Activity mContext;

    public FollowersAdapter(ArrayList<FollowersModal> list, Activity activity) {
        this.followersList = list;
        this.mContext = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follower, parent, false);
        return new FollowersAdapter.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        FollowersModal userResponse = followersList.get(position);
        holder.followerName.setText(userResponse.getFollowerName());
        Picasso.with(mContext).load(userResponse.getFollowerAvatar()).into(holder.followerAvatar);
    }

    @Override
    public int getItemCount() {
        return followersList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView followerName;
        private ImageView followerAvatar;

        public ViewHolder(View itemView) {
            super(itemView);

            followerAvatar = (ImageView) itemView.findViewById(R.id.follower_avatar);
            followerName = (TextView) itemView.findViewById(R.id.follower_name);
        }
    }

}
