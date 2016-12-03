package edu.dhbw.andobjviewer.camera;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import edu.dhbw.andarmodelviewer.R;
import edu.dhbw.andobjviewer.util.AndroidComponentUtil;

/**
 * allows you to choose either one of the internal models
 * or choose an file on the sd card through another activity
 *
 * @author Tobias Domhan
 */
public class ModelChooser extends AppCompatActivity {
    private static final int REQUEST_CODE_CAMERA_PERMISSION = 1;

    private String currentModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_model_chooser);
        initToolbar();
        initListView();
    }

    @Override public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    @Override public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
            @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_CAMERA_PERMISSION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    showModelViewer();
                } else {
                    // Permission Denied
                    Toast.makeText(this, R.string.label_camera_deny, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
                break;
        }
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.label_camera);
        toolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(toolbar);
    }

    private void initListView() {
        ListView listView = (ListView) findViewById(R.id.listview);
        AssetManager am = getAssets();
        Vector<Item> models = new Vector<Item>();
        Item item = new Item();
        item.text = getResources().getString(R.string.choose_a_model);
        item.type = Item.TYPE_HEADER;
        models.add(item);

        try {
            String[] modelFiles = am.list("models");
            List<String> modelFilesList = Arrays.asList(modelFiles);
            for (int i = 0; i < modelFiles.length; i++) {
                String currFileName = modelFiles[i];
                if (currFileName.endsWith(".obj")) {
                    item = new Item();
                    String trimmedFileName = currFileName.substring(0, currFileName.lastIndexOf(".obj"));
                    item.text = trimmedFileName.toLowerCase(Locale.ENGLISH);
                    models.add(item);
                    if (modelFilesList.contains(trimmedFileName + ".jpg")) {
                        InputStream is = am.open("models/" + trimmedFileName + ".jpg");
                        item.icon = (BitmapFactory.decodeStream(is));
                    } else if (modelFilesList.contains(trimmedFileName + ".png")) {
                        InputStream is = am.open("models/" + trimmedFileName + ".png");
                        item.icon = (BitmapFactory.decodeStream(is));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        item = new Item();
        item.text = getResources().getString(R.string.help);
        item.type = Item.TYPE_HEADER;
        models.add(item);
        item = new Item();
        item.text = getResources().getString(R.string.instructions);
        item.icon = new Integer(R.drawable.help);
        models.add(item);

        listView.setAdapter(new ModelChooserListAdapter(models));
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override public void onItemClick(AdapterView<?> parent, View view, int position,
                    long id) {
                Item item = (Item) parent.getItemAtPosition(position);
                currentModel = item.text;
                if (currentModel.equals(getResources().getString(R.string.instructions))) {
                    //show the instructions activity
                    AndroidComponentUtil.showComingSoonDialog(ModelChooser.this);
                } else {
                    showModelViewerWrapper();
                }
            }
        });
    }

    private void showModelViewerWrapper() {
        int hasCameraPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA);
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA)) {
                AndroidComponentUtil.showMessageDialog(this,
                        R.string.label_camera_allow_access,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.parse("package:" + getPackageName()));
                                startActivity(intent);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.CAMERA},
                    REQUEST_CODE_CAMERA_PERMISSION);
            return;
        }
        showModelViewer();
    }

    private void showModelViewer() {
        if (TextUtils.isEmpty(currentModel)) {
            return;
        }
        //load the selected internal file
        Intent intent = new Intent(this, AugmentedModelViewerActivity.class);
        intent.putExtra("name", currentModel + ".obj");
        intent.putExtra("type", AugmentedModelViewerActivity.TYPE_INTERNAL);
        intent.setAction(Intent.ACTION_VIEW);
        startActivity(intent);
        finish();
    }

    class ModelChooserListAdapter extends BaseAdapter {

        private Vector<Item> items;

        public ModelChooserListAdapter(Vector<Item> items) {
            this.items = items;
        }


        @Override
        public int getCount() {
            return items.size();
        }

        @Override
        public Object getItem(int position) {
            return items.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public int getViewTypeCount() {
            //normal items, and the header
            return 2;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return false;
        }

        @Override
        public boolean isEnabled(int position) {
            return !(items.get(position).type == Item.TYPE_HEADER);
        }

        @Override
        public int getItemViewType(int position) {
            return items.get(position).type;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            Item item = items.get(position);
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                switch (item.type) {
                    case Item.TYPE_HEADER:
                        v = vi.inflate(R.layout.list_header, null);
                        break;
                    case Item.TYPE_ITEM:
                        v = vi.inflate(R.layout.choose_model_row, null);
                        break;
                }
            }
            if (item != null) {
                switch (item.type) {
                    case Item.TYPE_HEADER:
                        TextView headerText = (TextView) v.findViewById(R.id.list_header_title);
                        if (headerText != null) {
                            headerText.setText(item.text);
                        }
                        break;
                    case Item.TYPE_ITEM:
                        Object iconImage = item.icon;
                        ImageView icon = (ImageView) v.findViewById(R.id.choose_model_row_icon);
                        if (icon != null) {
                            if (iconImage instanceof Integer) {
                                icon.setImageResource(((Integer) iconImage).intValue());
                            } else if (iconImage instanceof Bitmap) {
                                icon.setImageBitmap((Bitmap) iconImage);
                            }
                        }
                        TextView text = (TextView) v.findViewById(R.id.choose_model_row_text);
                        if (text != null)
                            text.setText(item.text);
                        break;
                }
            }
            return v;
        }

    }

    class Item {
        public static final int TYPE_ITEM = 0;
        public static final int TYPE_HEADER = 1;
        public int type = TYPE_ITEM;
        public Object icon = new Integer(R.drawable.missingimage);
        public String text;
    }

}
