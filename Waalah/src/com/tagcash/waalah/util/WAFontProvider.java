package com.tagcash.waalah.util;

import android.content.Context;
import android.graphics.Typeface;

public class WAFontProvider {
	
	public final static int GOTHAM_BOLD = 0;
	public final static int HELVETICA_BOLD = 1;
	public final static int HELVETICA = 2;
	public final static int HELVETICA_CE = 3;
	public final static int HELVETICA_NEUE_LIGHT = 4;
	public final static int HELVETICA_NEUE = 5;
	public final static int MUSEO_300 = 6;
	public final static int MUSEO_500 = 7;
	public final static int MUSEO_700 = 8;
	public final static int MYRIADPRO_REGULAR = 9;
	public final static int MYRIADPRO_SEMIBOLD = 10;
	
	private final static String FONT_FILE_NAMES[] = {
		"Gotham-Bold.otf",
		"Helvetica-Bold.otf",
		"Helvetica.otf",
		"HelveticaCE.otf",
		"HelveticaNeue-Light.otf",
		"HelveticaNeue.ttf",
		"Museo300-Regular.otf",
		"Museo500-Regular.otf",
		"Museo700-Regular.otf",
		"MyriadPro-Regular.otf",
		"MyriadPro-Semibold.otf"
	};
	
	static Typeface mFontTypefaces[] = new Typeface[FONT_FILE_NAMES.length];
	
	public static Typeface getFont(int fontIndex, Context context) {
		if (fontIndex < 0 && fontIndex >= mFontTypefaces.length)
			return getFont(HELVETICA_NEUE, context);
		
		if (mFontTypefaces[fontIndex] == null) {
			mFontTypefaces[fontIndex] = Typeface.createFromAsset(context.getAssets(),"fonts/"+FONT_FILE_NAMES[fontIndex]);
	    }
	    return mFontTypefaces[fontIndex];
	}
}
