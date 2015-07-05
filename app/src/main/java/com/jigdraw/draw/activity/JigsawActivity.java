package com.jigdraw.draw.activity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import com.jigdraw.draw.R;
import com.jigdraw.draw.model.LongParceable;
import com.jigdraw.draw.tasks.JigsawLoader;
import com.jigdraw.draw.views.JigsawGridView;

/**
 * Represents the jigsaw puzzle solving activity.
 *
 * @author Jay Paulynice
 */
public class JigsawActivity extends BaseJigsawActivity {
    /** Class name for logging */
    private static final String TAG = "JigsawActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jigsaw);
        initGridView();
    }

    private void initGridView() {
        final JigsawGridView gridView = (JigsawGridView) findViewById(R.id
                .jigsaw_grid);
        
        JigsawLoader task = new JigsawLoader(getApplicationContext(), gridView);
        LongParceable longParceable = getIntent().getExtras().getParcelable(
                "originalId");
        task.execute(longParceable.getData());

        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view,
                                           int position, long id) {
                gridView.startEditMode(position);
                return true;
            }
        });

        gridView.setOnDropListener(new JigsawGridView.OnDropListener() {
            @Override
            public void onActionDrop() {
                Log.d(TAG, "dropped element");
                gridView.stopEditMode();
            }
        });

        gridView.setOnDragListener(new JigsawGridView.OnDragListener() {
            @Override
            public void onDragStarted(int position) {
                Log.d(TAG, "dragging starts...position: " + position);
            }

            @Override
            public void onDragPositionsChanged(int oldPosition,
                                               int newPosition) {
                Log.d(TAG, String.format("drag changed from %d to %d",
                        oldPosition, newPosition));
            }
        });
    }
}