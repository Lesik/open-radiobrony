package com.lesikapk.openradiobrony;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;

public class LayoutSuppressingImageView extends ImageView {

    public LayoutSuppressingImageView(final Context context, final AttributeSet attrs) {
        super(context, attrs);

    }
    @Override
    public void requestLayout() {
        forceLayout();
    }

}