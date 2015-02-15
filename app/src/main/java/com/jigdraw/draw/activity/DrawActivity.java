package com.jigdraw.draw.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.jigdraw.draw.R;
import com.jigdraw.draw.model.Difficulty;
import com.jigdraw.draw.model.LongParceable;
import com.jigdraw.draw.service.JigsawService;
import com.jigdraw.draw.service.impl.JigsawServiceImpl;
import com.jigdraw.draw.views.DrawingView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Main activity class represents all the activities that a user starts with
 * such as draw, choose to create a new drawing, save the current drawing,
 * choose eraser and brush sizes etc.
 *
 * @author Jay Paulynice
 */
public class DrawActivity extends Activity implements OnClickListener {
    /** activity name for logging */
    private static final String TAG = "DrawActivity";

    /** custom drawing view */
    private DrawingView drawView;

    /** buttons for drawing */
    private ImageButton currPaint, eraseBtn, newBtn, saveBtn;

    /** brush sizes */
    private float smallBrush, mediumBrush, largeBrush, largestBrush;

    /** list of brushes */
    private List<ImageButton> brushes = new ArrayList<>();

    /** image data access */
    private JigsawService service;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        service = new JigsawServiceImpl(getApplicationContext());
        super.onCreate(savedInstanceState);
        init();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public void onClick(View view) {
        Log.d(TAG, "view id clicked: " + view.getId());
        if (view.getId() == R.id.erase_btn) {
            handleEraseButton();
        } else if (view.getId() == R.id.new_btn) {
            handleNewButton();
        } else if (view.getId() == R.id.save_btn) {
            handleSaveButton();
        }
    }

    /**
     * Handle the change of the brush size
     *
     * @param view the current brush view
     */
    public void handleBrushSize(View view) {
        //default to medium brush
        float bSize = mediumBrush;
        if (view.getId() == R.id.small_brush) {
            Log.d(TAG, "small brush clicked.");
            bSize = smallBrush;
        } else if (view.getId() == R.id.large_brush) {
            Log.d(TAG, "large brush clicked.");
            bSize = largeBrush;
        } else if (view.getId() == R.id.largest_brush) {
            Log.d(TAG, "largest brush clicked.");
            bSize = largestBrush;
        }
        drawView.setErase(false);
        drawView.setBrushSize(bSize);
        drawView.setLastBrushSize(bSize);
    }

    /**
     * Handles the change of color chosen
     *
     * @param view the view for the color chosen
     */
    public void paintClicked(View view) {
        drawView.setErase(false);
        drawView.setBrushSize(drawView.getLastBrushSize());

        if (view != currPaint) {
            String color = changeColor(view);
            setBrushColor(Color.parseColor(color));
        }
    }

    /**
     * Change to selected color
     *
     * @param view the image button clicked
     * @return the color to set
     */
    private String changeColor(View view) {
        ImageButton imgView = (ImageButton) view;
        String color = view.getTag().toString();
        drawView.setColor(color);

        updateUI(imgView, view);

        return color;
    }

    /**
     * Update UI with new selected color
     *
     * @param imgView the image view
     * @param view    the view
     */
    private void updateUI(ImageButton imgView, View view) {
        imgView.setImageDrawable(getResources().getDrawable(R.drawable
                .paint_pressed));
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable
                .paint));
        currPaint = (ImageButton) view;
    }

    /**
     * Set the brushes to the color chosen
     *
     * @param color the chosen color
     */
    public void setBrushColor(int color) {
        initBrushList();
        for (ImageButton im : brushes) {
            GradientDrawable d = (GradientDrawable) im.getDrawable();
            d.setColor(color);
        }
    }

    /**
     * Make a list of the brushes
     */
    private void initBrushList() {
        if (brushes.isEmpty()) {
            brushes.addAll(Arrays.asList(
                    (ImageButton) findViewById(R.id.small_brush),
                    (ImageButton) findViewById(R.id.medium_brush),
                    (ImageButton) findViewById(R.id.large_brush),
                    (ImageButton) findViewById(R.id.largest_brush)));
        }
    }

    /**
     * Initialize all the ui components
     */
    private void init() {
        setContentView(R.layout.activity_main);

        initBrushes();
        initView();
        initLayout();
        initButtons();
        setBrushColor(drawView.getPaintColor());
    }

    /**
     * Initialize the layout and set current color to first one
     */
    private void initLayout() {
        LinearLayout paintLayout = (LinearLayout) findViewById(R.id
                .paint_colors);
        currPaint = (ImageButton) paintLayout.getChildAt(0);
        currPaint.setImageDrawable(getResources().getDrawable(R.drawable
                .paint_pressed));
    }

    /**
     * Initialize the drawing view
     */
    private void initView() {
        drawView = (DrawingView) findViewById(R.id.drawing);
        drawView.setBrushSize(mediumBrush);
    }

    /**
     * Initialize the brushes
     */
    private void initBrushes() {
        smallBrush = getResources().getInteger(R.integer.small_size);
        mediumBrush = getResources().getInteger(R.integer.medium_size);
        largeBrush = getResources().getInteger(R.integer.large_size);
        largestBrush = getResources().getInteger(R.integer.largest_size);
    }

    /**
     * Initialize the buttons
     */
    private void initButtons() {
        //erase button
        eraseBtn = (ImageButton) findViewById(R.id.erase_btn);
        eraseBtn.setOnClickListener(this);

        //new button
        newBtn = (ImageButton) findViewById(R.id.new_btn);
        newBtn.setOnClickListener(this);

        //save button
        saveBtn = (ImageButton) findViewById(R.id.save_btn);
        saveBtn.setOnClickListener(this);
    }

    /**
     * Set erase to true on eraser click
     */
    private void handleEraseButton() {
        drawView.setErase(true);
    }

    /**
     * Handle the new button click
     */
    private void handleNewButton() {
        //new button
        AlertDialog.Builder newDialog = new AlertDialog.Builder(this);
        newDialog.setTitle("New drawing");
        newDialog.setMessage("Start new drawing (you will lose the current " +
                "drawing)?");
        newDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                drawView.startNew();
                dialog.dismiss();
            }
        });
        newDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        newDialog.show();
    }

    /**
     * Handle the save button click
     */
    private void handleSaveButton() {
        //save drawing
        AlertDialog.Builder saveDialog = new AlertDialog.Builder(this);
        saveDialog.setTitle("Create Jigsaw From Image");
        saveDialog.setMessage("Do you want to create a jigsaw with the " +
                "current Image?");
        saveDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                createJigsaw();
            }
        });
        saveDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        saveDialog.show();
    }

    /**
     * Create jigsaw and give user feedback
     */
    private void createJigsaw() {
        drawView.setDrawingCacheEnabled(true);
        Bitmap bitmap = drawView.getDrawingCache();

        long createdId = service.createJigsaw(bitmap, Difficulty.MEDIUM);

        toast(createdId > 0);
        drawView.destroyDrawingCache();

        startJigsaw(createdId);
    }

    private void startJigsaw(long id) {
        Intent myIntent = new Intent(getApplicationContext(),
                JigsawActivity.class).putExtra("originalId",
                new LongParceable(id));
        startActivity(myIntent);
        finish();
    }

    /**
     * Get feedback if jigsaw is created or not
     *
     * @param saved whether the image was saved
     */
    private void toast(boolean saved) {
        String feedback = saved ? "Jigsaw successfully created." :
                "Oops! Unable to create jigsaw.";
        Toast toast = Toast.makeText(getApplicationContext(),
                feedback, Toast.LENGTH_SHORT);
        toast.show();
    }
}