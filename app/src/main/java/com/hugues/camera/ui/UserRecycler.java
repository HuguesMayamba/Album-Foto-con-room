package com.hugues.camera.ui;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.hugues.camera.DataModel.DataConverter;
import com.hugues.camera.DataModel.User;
import com.hugues.camera.R;

import org.jetbrains.annotations.NotNull;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class UserRecycler extends RecyclerView.Adapter<UserRecycler.UserViewHolder> {
    List<User> data;


    public UserRecycler(List<User> users) {
        data = users;
    }

    @Override
    public @NotNull UserViewHolder onCreateViewHolder(@NotNull ViewGroup viewGroup, int i) {
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

        SimpleDateFormat formatoOriginal = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy", Locale.ENGLISH);
        SimpleDateFormat formatoDeseado = new SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH);

        String fechaFormateadaMin = "";

        try{
           Date fechaMin = formatoOriginal.parse(String.valueOf(user.getDob()));
            assert fechaMin != null;
            fechaFormateadaMin = formatoDeseado.format(fechaMin);
            Log.d ("TAG", "DATE"+ fechaFormateadaMin);

        }catch (Exception e){ e.printStackTrace(); }

        userViewHolder.dob.setText(fechaFormateadaMin);

    }

    @Override
    public int getItemCount() {
        return data.size();

    }

    public static class UserViewHolder extends RecyclerView.ViewHolder{

        ImageView imageView;
        TextView name, dob;

        public UserViewHolder( @NotNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.cardName);
            dob = itemView.findViewById(R.id.cardDob);
            imageView = itemView.findViewById(R.id.cardImage);
        }

    }
}
