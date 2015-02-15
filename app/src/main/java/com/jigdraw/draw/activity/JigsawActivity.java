package com.jigdraw.draw.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;

import com.jigdraw.draw.R;
import com.jigdraw.draw.adapter.ImageAdapter;
import com.jigdraw.draw.model.LongParceable;

public class JigsawActivity extends Activity implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);
        initGridView();
    }

    private void initGridView() {
        LongParceable p = getIntent().getExtras().getParcelable("originalId");

        GridView gridview = (GridView) findViewById(R.id.gridview);
        ImageAdapter adapter = new ImageAdapter(this, p.getData());
        gridview.setAdapter(adapter);
        gridview.setNumColumns(adapter.getNumColumns());

        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
                //TODO: drag and drop to solve puzzle
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View v) {
        //TODO: do work
    }
}