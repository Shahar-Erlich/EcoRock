package com.ecorock.game.MainPage;

import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.badlogic.gdx.Game;
import com.ecorock.game.GameScreen;
import com.ecorock.game.R;
import com.ecorock.game.databinding.FragmentGuitarBinding;

import java.util.List;

public class MyGuitarRecyclerViewAdapter extends RecyclerView.Adapter<MyGuitarRecyclerViewAdapter.ViewHolder>{

    private final List<GuitarTemplate> mValues;

    public MyGuitarRecyclerViewAdapter(List<GuitarTemplate> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        return new ViewHolder(FragmentGuitarBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));

    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.imgB.setImageResource(holder.mItem.getResource());
        holder.tv.setText(holder.mItem.getName());
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public ImageButton imgB;
        public TextView tv;
        public GuitarTemplate mItem;

        public ViewHolder(FragmentGuitarBinding binding) {
            super(binding.getRoot());
            imgB = binding.GuitarTemplate;
            imgB.setOnClickListener(this);
            tv = binding.TemplateTV;
        }

        @Override
        public void onClick(View v) {
            if(v==imgB){
                GameScreen.changeBGT(mItem.getFilePath());
            }
        }
    }
}