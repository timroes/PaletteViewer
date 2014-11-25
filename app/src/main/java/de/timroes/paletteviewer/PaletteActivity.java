package de.timroes.paletteviewer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.io.IOException;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class PaletteActivity extends Activity {

    private static final int PICK_IMAGE = 1;

	@InjectView(R.id.image) ImageView imageThumbnail;
	@InjectView(R.id.label)	TextView instructionLabel;
	@InjectView(R.id.wrapper_layout) LinearLayout wrapperLayout;
	@InjectView(R.id.loading_progress) ProgressBar progressIndicator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_palette);
		ButterKnife.inject(this);
    }

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		super.onConfigurationChanged(newConfig);
		if(newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {
			wrapperLayout.setOrientation(LinearLayout.VERTICAL);
		} else {
			wrapperLayout.setOrientation(LinearLayout.HORIZONTAL);
		}
	}

	public void selectImage(View view) {
        Intent in = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
        startActivityForResult(in, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
			new LoadImageAsync().execute(data.getData());
		}
    }

	private class LoadImageAsync extends AsyncTask<Uri, Void, Palette> {

		private Uri loadingUri;

		@Override
		protected void onPreExecute() {
			imageThumbnail.setVisibility(View.GONE);
			instructionLabel.setVisibility(View.GONE);
			progressIndicator.setVisibility(View.VISIBLE);
		}

		@Override
		protected Palette doInBackground(Uri... uris) {
			try {
				loadingUri = uris[0];
				Bitmap picture = MediaStore.Images.Media.getBitmap(getContentResolver(), loadingUri);
				return Palette.generate(picture);
			} catch (IOException e) {
				// TODO: Error message
				e.printStackTrace();
			}

			cancel(false);
			return null;
		}

		@Override
		protected void onPostExecute(Palette palette) {
			imageThumbnail.setImageURI(loadingUri);

			imageThumbnail.setVisibility(View.VISIBLE);
			instructionLabel.setVisibility(View.GONE);
			progressIndicator.setVisibility(View.GONE);

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
	}

}
