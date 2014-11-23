package de.timroes.palettedemo;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcelable;
import android.support.v7.graphics.Palette;
import android.util.AttributeSet;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

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
	}

	public void setSwatch(Palette.Swatch swatch) {
		if(swatch == null) {
			// TODO: overlay some icon and gray out
			this.setAlpha(0.4f);
			this.setBackgroundColor(0x666666);
			bodyText.setText("not found in picture");
			return;
		}

		this.setBackgroundColor(swatch.getRgb());
		titleText.setTextColor(swatch.getTitleTextColor());
		bodyText.setTextColor(swatch.getBodyTextColor());
	}

}
