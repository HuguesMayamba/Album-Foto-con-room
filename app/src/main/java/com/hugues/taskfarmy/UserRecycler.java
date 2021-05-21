package com.hugues.taskfarmy;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hugues.taskfarmy.DataModel.DataConverter;
import com.hugues.taskfarmy.DataModel.User;
import com.hugues.taskfarmy.DataModel.UserDAO;
import com.hugues.taskfarmy.DataModel.UserDatabase;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class UserRecycler extends RecyclerView.Adapter<UserRecycler.UserViewHolder> {
    List<User> data;

    public UserRecycler(List<User> users) {
        data = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(
                R.layout.user_item_layout,
                viewGroup, false
        );
        return new UserViewHolder(view);
    }

    public void onBindViewHolder( @NotNull UserViewHolder userViewHolder, int i) {
        User user = data.get(i);
        userViewHolder.imageView.setImageBitmap(DataConverter.converByteArray2Image(user.getImage()));
        userViewHolder.name.setText(user.getFullName());
        userViewHolder.dob.setText(String.valueOf(user.getDob()));

    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    public void setData(List<User> data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public class UserViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        ImageView imageView;
        TextView name, dob;
        Button btnDelete;

        public UserViewHolder( @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cardName);
            dob = itemView.findViewById(R.id.cardDob);
            imageView = itemView.findViewById(R.id.cardImage);
            btnDelete = itemView.findViewById(R.id.btnDelete);
            btnDelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            data.clear();
            notifyDataSetChanged();
        }
    }
}
