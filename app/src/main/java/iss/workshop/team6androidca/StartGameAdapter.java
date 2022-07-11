package iss.workshop.team6androidca;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import androidx.annotation.NonNull;

public class StartGameAdapter extends BaseAdapter {
    private final Context context;
    protected String[] filenames;

    public StartGameAdapter(Context context, String[] filenames) {
        this.context = context;
        this.filenames = filenames;
    }

    @Override
    public int getCount() {
        return filenames.length;
    }

    @Override
    public Object getItem(int i) {
        return filenames[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @NonNull
    public View getView(int pos, View view, @NonNull ViewGroup parent) {
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.start_game_grid_item, parent, false);
        }

        ImageView imageView = view.findViewById(R.id.grid_image);
        int id = context.getResources().getIdentifier(filenames[pos], "drawable", context.getPackageName());
        imageView.setImageResource(id);

        return view;
    }
}