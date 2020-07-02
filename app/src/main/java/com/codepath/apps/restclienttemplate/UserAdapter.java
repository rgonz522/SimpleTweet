
package com.codepath.apps.restclienttemplate;

import android.app.Activity;

import androidx.fragment.app.FragmentManager;

import android.content.Context;
import android.content.Intent;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;

import com.codepath.apps.restclienttemplate.models.User;

import org.parceler.Parcels;

import java.sql.Time;
import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    Context context;
    List<User> users;



    public static final int ROUNDED_RADIUs = 100;


    public UserAdapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;


    }


    @NonNull
    @Override // for each row we're going inflate layout
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new ViewHolder(view);
    }

    //bind values based on position of element
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // get data @ position
        User user = users.get(position);
        Log.i("Hi", "onBindViewHolder: \n "+ user.name);
        holder.bind(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    //clean elements of the recyclers
    public void clear() {
        users.clear();
        notifyDataSetChanged();
    }

    //update the the recycler
    public void addAll(List<User> userList) {
        users.addAll(userList);
        notifyDataSetChanged();
    }
    public void add(User user)
    {
        users.add(user);
    }


    //define a viewholder
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivProfileImage;
        TextView tvScreenName;
        TextView tvUserName;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ivProfileImage = itemView.findViewById(R.id.ivProfileImage);
            tvScreenName = itemView.findViewById(R.id.tvScreenName);
            tvUserName = itemView.findViewById(R.id.tvUserName);

        }

        public void bind(User user)
        {
            tvScreenName.setText(user.screen_name);
            tvUserName.setText(user.name);
            Glide.with(context).load(user.profile_img_url).transform(new RoundedCorners(ROUNDED_RADIUs)).into(ivProfileImage);


            ivProfileImage.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {

                    Intent intent = new Intent(context, UserProfileActivity.class);
                    intent.putExtra(User.class.getSimpleName(), Parcels.wrap(users.get(getAdapterPosition())));
                    context.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View view)
        {


        }


    }

    private void showComposeDialog(String replyScreenName) {

        FragmentManager fm = ((TimeLineActivity) context).getSupportFragmentManager();

        ComposeFragment composeFragment = ComposeFragment.newInstance(replyScreenName);
        Log.i("Composefragment", "showComposeDialog: " + "creating Fragment");
        composeFragment.show(fm, "fragment_edit_name");

    }


}
