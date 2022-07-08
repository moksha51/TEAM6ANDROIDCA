package iss.workshop.team6androidca;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

public class TwelveImagePage extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener  {

    private final int imagecount = 0;
    ActivityResultLauncher<Intent> rlSelectImage;

    //this code is for for the test out the functionality without Heily's download part
    private final String[] fnames = {
            "hug", "laugh", "peep", "snore", "stop",
            "tired", "full", "what", "afraid", "no_way"
    };

    /*
    this code is to be used for actual submission
    Heily to rename the downloaded image files to "1", "2", "3" etc
    up to 20 for the downloaded files
    private final String[] fnames = {
            "1", "2", "3", "4", "5","6","7","8","9","10",
            "11", "12", "13", "14", "15","16","17","18","19","20"
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_twelve_image_page);

        MyCustomAdapter adapter = new MyCustomAdapter(this, 0);
        adapter.setData(fnames);

        GridView gridView = findViewById(R.id.gridView);
        if (gridView != null) {
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(this);
        }
    }

    protected void registerForResult(){
        rlSelectImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null){
                            String image = data.getStringExtra("");
                            if (image != null){
                                //ask Cher Wah about this
                            }
                        }
                    }
                }
        );
    }

    //Game logic for Halim to figure out how to match the images
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        int selectedImage = v.getId();

        if (id == R.id.grid_image){
            Intent intent = new Intent(this, TwelveImagePage.class);
            rlSelectImage.launch(intent);
            Toast.makeText(this, "This image will be loaded into the game", Toast.LENGTH_SHORT).show();
            if (imagecount >= 0 && imagecount < 6){
                Toast.makeText(this, imagecount + " image(s) has been loaded into the game", Toast.LENGTH_SHORT).show();
            }
            else{
                Toast.makeText(this,"Game will commence shortly",Toast.LENGTH_SHORT).show();
            }
        }

    }

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