package de.timroes.palettedemo;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.io.IOException;


public class PaletteActivity extends Activity {

    private static final int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
    }

    public void selectImage(View view) {
        Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(in, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
			try {
				// TODO: Load in background and show spinner while loading
				Bitmap picture = MediaStore.Images.Media.getBitmap(getContentResolver(), data.getData());
				extractPalette(picture);
			} catch (IOException e) {
				// TODO: Error message
				e.printStackTrace();
			}
		}
    }

    private void extractPalette(Bitmap bitmap) {
		Palette.generateAsync(bitmap, new Palette.PaletteAsyncListener() {
			@Override
			public void onGenerated(Palette palette) {

				SwatchView vibrant = (SwatchView) findViewById(R.id.vibrant);
				vibrant.setSwatch(palette.getVibrantSwatch());

				SwatchView vibrantLight = (SwatchView) findViewById(R.id.vibrant_light);
				vibrantLight.setSwatch(palette.getLightVibrantSwatch());

				SwatchView vibrantDark = (SwatchView) findViewById(R.id.vibrant_dark);
				vibrantDark.setSwatch(palette.getDarkVibrantSwatch());

				SwatchView muted = (SwatchView) findViewById(R.id.muted);
				muted.setSwatch(palette.getMutedSwatch());

				SwatchView mutedLight = (SwatchView) findViewById(R.id.muted_light);
				mutedLight.setSwatch(palette.getLightMutedSwatch());

				SwatchView mutedDark = (SwatchView) findViewById(R.id.muted_dark);
				mutedDark.setSwatch(palette.getDarkMutedSwatch());
			}
		});
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_palette, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
