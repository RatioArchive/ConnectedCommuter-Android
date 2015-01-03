package com.ratio.connectedcommuter.application;

import java.util.HashMap;
import java.util.Map;

import com.ratio.common.application.RatioApplication;
import com.ratio.common.interfaces.ILogger;
import com.ratio.common.utils.FontUtils;
import com.ratio.common.utils.Logger;
import com.ratio.connectedcommuter.R;

public class CCApp extends RatioApplication implements ILogger {

	private static final boolean LOGGING_ENABLED = true;
	private static final String TAG = CCApp.class.getSimpleName();

    // Fonts need to be initialized in static block to prevent memory leaks
    static {
        initFonts();
    }

    // Protected static methods //
    protected static void initFonts() {
        Map<String, Integer> fontMap = new HashMap<String, Integer>();
        fontMap.put("HelveticaNeueBoldItalic", R.raw.helvetica_neue_bold_italic);
        fontMap.put("HelveticaNeueBold", R.raw.helvetica_neue_bold);
        fontMap.put("HelveticaNeueLightItalic", R.raw.helvetica_neue_light_italic);
        fontMap.put("HelveticaNeueLight", R.raw.helvetica_neue_light);
        fontMap.put("HelveticaNeueMedium", R.raw.helvetica_neue_medium);
        fontMap.put("HelveticaNeueUltraLight", R.raw.helvetica_neue_ultra_light);
        fontMap.put("HelveticaNeue", R.raw.helvetica_neue);
        FontUtils.setFontMap(fontMap);
    }

    @Override
    protected Logger createLogger() {
        return Logger.createInstance(this);
    }

    @Override
    public boolean getLoggingEnabled() {
        return LOGGING_ENABLED;
    }

    @Override
    public String getAppTag() {
        return TAG;
    }
}
