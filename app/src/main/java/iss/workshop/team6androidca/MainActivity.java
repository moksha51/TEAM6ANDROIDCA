package iss.workshop.team6androidca;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ListIterator;

public class MainActivity extends AppCompatActivity
        implements AdapterView.OnItemClickListener, View.OnClickListener {

    //this code is for for the test out the functionality without Heily's download part
    private final String[] fnames = {
            "hug", "laugh", "peep", "snore", "stop",
            "tired", "full", "what", "afraid", "no_way"
    };

    ImageView[] imageViews = new ImageView[20];
    ArrayList<Bitmap> imgBits = new ArrayList<Bitmap>();
    ProgressBar progressBar;
    Content content = null;
    int clickCount = 0;
    LinearLayout gallery;
    TextView textView;
    String url;
    Toast success, error;

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
    private final int REQUEST_EXTERNAL_STORAGE = 1;
    private String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE};

    public void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity,
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(activity, PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE);
        }
    }
    ActivityResultLauncher<Intent> rlSelectImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(MainActivity.this);
        success = Toast.makeText(this, "Download Completed!", Toast.LENGTH_SHORT);
        error = Toast.makeText(this, "Please use another URL", Toast.LENGTH_LONG);

//        MyCustomAdapter adapter = new MyCustomAdapter(this, 0);
//        adapter.setData(fnames);

//        gallery = findViewById(R.id.gallery);
//        textView = findViewById(R.id.progress_text);
//
//        progressBar=findViewById(R.id.progress_bar);
        progressBar.setMax(20);
        loadDefaultImageViews();

        Button fetchBtn = findViewById(R.id.fetchBtn);
        if(fetchBtn!=null){
            fetchBtn.setOnClickListener(new View.OnClickListener() {
                @RequiresApi(api = Build.VERSION_CODES.M)
                @Override
                public void onClick(View view) {
                    EditText newUrl = findViewById(R.id.urlBar);
                    if(newUrl!= null){
                        url=newUrl.getText().toString();
                    }
                    hideKeyboard(view);
                    revertToDefault();
                    if (content != null) {
                        content.cancel(true);
                    }
                    content = new Content();
                    content.execute();
                }
            });
        }
        /*GridView gridView = findViewById(R.id.gridView);
        if (gridView != null) {
            gridView.setAdapter(adapter);
            gridView.setOnItemClickListener(this);
        }*/

    }

    private class Content extends AsyncTask<Void, Integer, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(ProgressBar.VISIBLE);
            textView.setVisibility(textView.VISIBLE);
        }
        //downloadimage
        private void getWebImage(String url) {
            try {
                //get url
                URL httpUrl = new URL(url);
                //open connection
                HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                //disconnect over 5s
                //conn.setReadTimeout(5000);
                //Set ways to access data
                conn.setRequestMethod("GET");
                //Set up an input stream
                conn.setDoInput(true);
                //get input stream
                InputStream inputStream = conn.getInputStream();

                //System time
                String strDate = String.valueOf(System.currentTimeMillis());
                //file output stream object
                FileOutputStream out = null ;
                //Create subdirectories
                File downloadFile = null ;
                //if sd card exists
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    //SD card directory
                    File parentFile = Environment.getExternalStorageDirectory();
                    //创建子目录，这里如果不加“.JPG”同样能在ImageView中显示，但是不能再图库中显示。parentFile，是父级目录，必须存在，第二个参数可以自己随便写
                    //Create a subdirectory. If don't add ".JPG", it can also be displayed in ImageView,
                    // but it cannot be displayed in the gallery. parentFile, is the parent directory,
                    // must exist, the second parameter can be written or not
                    downloadFile = new File(parentFile,strDate+".jpg");

                    //Download pictures to local SD card
                    out = new FileOutputStream(downloadFile);
                }
                //字节流
                byte[] b = new byte[2*1024];
                int len ;
                if(out!=null){
                    //当长度为 -1 表示没有内容可读了
                    while ((len=inputStream.read(b))!=-1) {
                        //写入SD卡文件中
                        out.write(b, 0, len);
                    }
                }

                System.out.println("==========================="+downloadFile.getAbsolutePath());


            } catch (MalformedURLException | ProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }


        }

        public void delAllFile(String path) {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            if (!file.isDirectory()) {
                return;
            }
            String[] tempList = file.list();
            File temp = null;
            for (int i = 0; i < tempList.length; i++) {
                if (path.endsWith(File.separator)) {
                    temp = new File(path + tempList[i]);
                }
                else {
                    temp = new File(path + File.separator + tempList[i]);
                }
                if (temp.isFile()) {
                    temp.delete();
                }
                if (temp.isDirectory()) {
                    delAllFile(path+"/"+ tempList[i]);//Delete formerOne
                }
            }
        }
        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Document document = Jsoup.connect(url).get();
                //select all img elements
                Elements imgs = document.select("img[src~=(?i)\\.(png|jpe?g)]");
                //SD card file
                File parentFile = Environment.getExternalStorageDirectory();
                delAllFile(parentFile.toString());
                ListIterator<Element> elementIt = imgs.listIterator();

                for (int i = 0; i < 20; i++) {

                    if (elementIt.hasNext()) {

                        String imgSrc = elementIt.next().absUrl("src");
                        Log.i("data ",imgSrc);
                        getWebImage(imgSrc);
                        InputStream input = new java.net.URL(imgSrc).openStream();
                        Bitmap imgbit = BitmapFactory.decodeStream(input);

                        //crop the downloaded img to imageView ratio
                        float imgBitRatio = (float) imgbit.getHeight() / imgbit.getWidth();
                        float imgViewRatio = (float) imageViews[1].getMeasuredHeight() / imageViews[1].getMeasuredWidth();
                        if (imgViewRatio > imgBitRatio) {
                            int imgbitWidth = (int) (imgbit.getHeight() / imgViewRatio);
                            int startPosX = (int) (imgbit.getWidth() - (imgbit.getHeight() / imgViewRatio)) / 2;
                            imgbit = Bitmap.createBitmap(imgbit, startPosX, 0, imgbitWidth, imgbit.getHeight());
                        } else {
                            int imgbitHeight = (int) (imgbit.getWidth() * imgViewRatio);
                            int startPosY = (int) (imgbit.getHeight() - (imgbit.getWidth() * imgViewRatio)) / 2;
                            imgbit = Bitmap.createBitmap(imgbit, 0, startPosY, imgbit.getWidth(), imgbitHeight);
                        }

                        if (isCancelled()) {
                            return null;
                        }
                        imgBits.add(imgbit);
                        progressBar.incrementProgressBy(1);
                        publishProgress(i);
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            if (!isCancelled()) {
                imageViews[values[0]].setImageBitmap(imgBits.get(values[0]));
                textView.setText(values[0] + 1 + "/" + progressBar.getMax());
            }
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            progressBar.setVisibility(ProgressBar.INVISIBLE);

            if (imgBits.size() < 6) {
                textView.setVisibility(View.INVISIBLE);
                error.show();
            }
        }
    }

    private void hideKeyboard(View v) {
        InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(v.getApplicationWindowToken(), 0);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void revertToDefault() {
        imgBits.clear();
        clickCount = 0;
        progressBar.setProgress(0);
        textView.setText("0/" + progressBar.getMax());
        for (ImageView iv : imageViews) {
            iv.setImageResource(R.drawable.peep);
            iv.setForeground(null);
            iv.setClickable(false);
        }
        for (View v : imageViews) {
            v.setAlpha(1);
        }
    }

    private void loadDefaultImageViews() {
        LinearLayout.LayoutParams linearParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
        linearParams.setMargins(10, 0, 10, 0);
        LinearLayout.LayoutParams ivParams = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);
        ivParams.setMargins(10, 10, 10, 10);

        int column, row;
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            column = 4;
            row = 5;
        } else {
            column = 10;
            row = 2;
            ConstraintLayout.LayoutParams lp = (ConstraintLayout.LayoutParams) gallery.getLayoutParams();
            lp.height = 480;
            gallery.setLayoutParams(lp);
        }

        int count = 0;
        for (int i = 0; i < row; i++) {
            LinearLayout layout = new LinearLayout(this);
            layout.setWeightSum(column);
            layout.setLayoutParams(linearParams);

            for (int j = 0; j < column; j++) {
                ImageView iv = new ImageView(this);
                iv.setImageResource(R.drawable.peep);
                iv.setLayoutParams(ivParams);
                iv.setId(count);
                iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageViews[count] = iv;
                layout.addView(iv);
                count++;
            }
            gallery.addView(layout);
        }
    }

//    protected void registerForResult(){
//        rlSelectImage = registerForActivityResult(
//                new ActivityResultContracts.StartActivityForResult(),
//                result -> {
//                    if (result.getResultCode() == AppCompatActivity.RESULT_OK){
//                        Intent data = result.getData();
//                        if (data != null){
//                            String image = data.getStringExtra("");
//                            if (image != null){
//                                /* need to ask Cher Wah about this, supposed to log the files names
//                                into a manifest or something then reference so Halim can do his game code
//                                */
//                            }
//                        }
//                    }
//                }
//        );
//    }

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
                Intent intent = new Intent(this, StartGame.class);
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
