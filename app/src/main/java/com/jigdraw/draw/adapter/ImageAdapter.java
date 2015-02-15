package com.jigdraw.draw.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.jigdraw.draw.model.ImageEntity;
import com.jigdraw.draw.service.ImageService;
import com.jigdraw.draw.service.impl.ImageServiceImpl;

import java.util.List;

public class ImageAdapter extends BaseAdapter {
    private ImageService imgserv;
    private Context mContext;
    private Bitmap[] mThumbIds;
    private int numColumns;

    public ImageAdapter(Context c, long id) {
        mContext = c;
        imgserv = new ImageServiceImpl(c);
        List<ImageEntity> entities = imgserv.findTiles(id);

        numColumns = (int) Math.sqrt(entities.size());

        mThumbIds = new Bitmap[entities.size()];

        int n = 0;
        for (ImageEntity e : entities) {
            Bitmap d = e.getImage();
            mThumbIds[n] = d;
            n++;
        }
    }

    public int getNumColumns() {
        return numColumns;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return mThumbIds[position];
    }

    public long getItemId(int position) {
        return 0;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        Bitmap d = mThumbIds[position];
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(new GridView.LayoutParams(d
                    .getWidth(),
                    d.getHeight()));
            imageView.setPadding(1, 1, 1, 1);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageDrawable(new BitmapDrawable(d));
        return imageView;
    }
}
