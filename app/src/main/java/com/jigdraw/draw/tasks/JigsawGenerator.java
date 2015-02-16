package com.jigdraw.draw.tasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;

import com.jigdraw.draw.activity.JigsawActivity;
import com.jigdraw.draw.model.Difficulty;
import com.jigdraw.draw.model.LongParceable;
import com.jigdraw.draw.service.JigsawService;
import com.jigdraw.draw.service.impl.JigsawServiceImpl;

/**
 * Class to generate jigsaw pieces and save the tiles in the sqlite database
 * asynchronously returning the id of the original image
 *
 * @author Jay Paulynice
 */
public class JigsawGenerator extends AsyncTask<Bitmap, Integer, Long> {
    private JigsawService service;
    private Difficulty level;
    private Context context;

    public JigsawGenerator(Context context, Difficulty level) {
        this.context = context;
        this.level = level;
        this.service = new JigsawServiceImpl(context);
    }

    @Override
    protected Long doInBackground(Bitmap... params) {
        return service.createJigsaw(params[0], level);
    }

    @Override
    protected void onPostExecute(Long id) {
        startJigsaw(id);
    }

    private void startJigsaw(long id) {
        Intent intent = new Intent(context,
                JigsawActivity.class).putExtra("originalId",
                new LongParceable(id));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }
}