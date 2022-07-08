package iss.workshop.team6androidca;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
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

    private int imagecount = 0;
    ActivityResultLauncher<Intent> rlSelectImage;

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

    protected void registerForResult(){
        rlSelectImage = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == AppCompatActivity.RESULT_OK){
                        Intent data = result.getData();
                        if (data != null){
                            String image = data.getStringExtra("");
                            if (image != null){
                                /* need to ask Cher Wah about this, supposed to log the files names
                                into a manifest or something then reference so Halim can do his game code
                                */
                            }
                        }
                    }
                }
        );
    }

    //this part works when pressing the image, XJ needs to figure out how to select send images to TwelveImagePage
    @Override
    public void onItemClick(AdapterView<?> av, View v, int pos, long id) {
        //int selectedImage = v.getId();
        //if (selectedImage == R.id.grid_image){
            Toast.makeText(this, "This image will be loaded into the game", Toast.LENGTH_SHORT).show();
            if (imagecount >= 0 && imagecount < 6){
                Toast.makeText(this, imagecount + " image(s) has been loaded into the game", Toast.LENGTH_SHORT).show();
            }
            else if (imagecount == 6){
                Toast.makeText(this,"Game will commence shortly",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(this, TwelveImagePage.class);
                rlSelectImage.launch(intent);
            }
            else{
                //throw exception or something, see whether want to implement or not.
            }
        }

    //}

    //Heily's part on how to pass the URL link to the fetch button
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
