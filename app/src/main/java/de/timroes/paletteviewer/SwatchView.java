package de.timroes.paletteviewer;

import android.app.AlertDialog;
import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

public class SwatchView extends FrameLayout {

	private TextView titleText;
	private TextView bodyText;

	private Palette.Swatch swatch;

	public SwatchView(Context context) {
		super(context);
		init(context, null);
	}

	public SwatchView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public SwatchView(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context, attrs);
	}

	public SwatchView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		init(context, attrs);
	}

	private void init(Context ctx, AttributeSet attrs) {
		inflate(ctx, R.layout.swatch_view, this);
		titleText = (TextView) findViewById(R.id.title);
		bodyText = (TextView) findViewById(R.id.body_text);

		setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			if(swatch == null) {
				Toast.makeText(getContext(), R.string.swatch_does_not_exist, Toast.LENGTH_SHORT).show();
			} else {
				String dialogTitle = getResources().getString(R.string.swatch_dialog_title,
						titleText.getText(),
						bodyText.getText()
				);
				String dialogMessage = getResources().getString(R.string.swatch_dialog,
						swatch.getRgb(),
						swatch.getTitleTextColor(),
						swatch.getBodyTextColor(),
						swatch.getPopulation()
				);
				new AlertDialog.Builder(getContext())
						.setTitle(dialogTitle)
						.setMessage(dialogMessage)
						.show();
			}
			}
		});

		TypedArray attributes = ctx.obtainStyledAttributes(attrs, R.styleable.SwatchView);
		for(int i = 0; i < attributes.getIndexCount(); i++) {
			int attrIndex = attributes.getIndex(i);
			switch(attrIndex) {
				case R.styleable.SwatchView_titleText:
					titleText.setText(attributes.getString(attrIndex));
					break;
				case R.styleable.SwatchView_bodyText:
					bodyText.setText(attributes.getString(attrIndex));
					break;
			}
		}
		attributes.recycle();

		setNullSwatch();
	}

	public void setSwatch(Palette.Swatch swatch) {

		this.swatch = swatch;

		if(swatch == null) {
			setNullSwatch();
			return;
		}

		try {
			this.setBackgroundColor(swatch.getRgb());
			titleText.setTextColor(swatch.getTitleTextColor());
			bodyText.setTextColor(swatch.getBodyTextColor());
		} catch(IllegalArgumentException ex) {
			setNullSwatch();
		}

	}

	private void setNullSwatch() {
		this.swatch = null;
		this.setBackgroundResource(R.drawable.swatch_not_available);
		titleText.setTextColor(0x33000000);
		bodyText.setTextColor(0x33000000);
	}

}
