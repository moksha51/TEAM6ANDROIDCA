package iss.workshop.team6androidca;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {

    private final String[] fnames = {
            "hug", "laugh", "peep", "snore", "stop",
            "tired", "full", "what", "afraid", "no_way"
    };

    private int imagecount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MyCustomAdapter adapter = new MyCustomAdapter(this, 0);
        adapter.setData(fnames);

        Button fetchBtn = findViewById(R.id.fetchBtn);
        if(fetchBtn!=null){
            fetchBtn.setOnClickListener(this);
        }

        GridView gridView = findViewById(R.id.gridView);
        if (gridView != null) {
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(this);
        }

    }

    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        Toast.makeText(this, "This image will be loaded into the game", Toast.LENGTH_SHORT).show();
        if (imagecount >= 0 && imagecount < 6){
            Toast.makeText(this, imagecount + "image(s) has been loaded into the game", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"Game will commence shortly",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View view){
        EditText newUrl = findViewById(R.id.urlBar);
        if(newUrl!= null){
            Intent intent = new Intent();
            intent.putExtra("newURL", newUrl.getText().toString());
            Uri uri = Uri.parse(String.valueOf(newUrl));
            setResult(RESULT_OK, intent);
            finish();
        }
    }
}
