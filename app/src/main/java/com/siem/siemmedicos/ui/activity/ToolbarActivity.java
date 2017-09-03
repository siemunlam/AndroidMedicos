package com.siem.siemmedicos.ui.activity;

import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;

import com.siem.siemmedicos.R;
import com.siem.siemmedicos.ui.custom.CustomToolbar;
import com.siem.siemmedicos.utils.Utils;

/**
 * Created by Lucas on 3/9/17.
 */

public class ToolbarActivity extends AppCompatActivity {

    private CustomToolbar mToolbar;

    protected void setToolbar(boolean isHomeButtonVisible) {
        mToolbar = (CustomToolbar)findViewById(R.id.appBarLayout);
        setSupportActionBar(mToolbar.getToolbar());
        if(getSupportActionBar() != null) {
            getSupportActionBar().setTitle("");
            if(isHomeButtonVisible){
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }else
            mToolbar.setVisibility(View.GONE);
        mToolbar.setText(getString(R.string.name));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed(){
        supportFinishAfterTransition();
        Utils.addFinishTransitionAnimation(this);
    }

}
