package edu.dhbw.andobjviewer;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import edu.dhbw.andarmodelviewer.R;
import edu.dhbw.andobjviewer.util.AndroidComponentUtil;

/**
 * Created by Edwin on 24/11/2016.
 */

public class ProfileActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button mbtnUpdate;
    private ImageView mImgProfile;

    @Override protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(R.string.label_users);
        mToolbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
        setSupportActionBar(mToolbar);

        mbtnUpdate = (Button) findViewById(R.id.update_profile);
        mImgProfile = (ImageView) findViewById(R.id.img_profile);

        mbtnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                AndroidComponentUtil.showComingSoonDialog(ProfileActivity.this);
            }
        });

        mImgProfile.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View view) {
                Toast.makeText(ProfileActivity.this, "Change profile picture is coming soon.",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }
}
