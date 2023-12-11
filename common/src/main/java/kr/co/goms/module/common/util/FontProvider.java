package kr.co.goms.module.common.util;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.text.TextUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import androidx.annotation.Nullable;

/**
 * extracting Typeface from Assets is a heavy operation,
 * we want to make sure that we cache the typefaces for reuse
 */
public class FontProvider {

    private static final String DEFAULT_FONT_NAME = "NanumGothic";

    private final Map<String, Typeface> typefaces;
    private final Map<String, String> fontNameToTypefaceFile;
    private final Resources resources;
    private final List<String> fontNames;

    public FontProvider(Resources resources) {
        this.resources = resources;

        typefaces = new HashMap<>();

        // populate fonts
        fontNameToTypefaceFile = new LinkedHashMap<>();
        fontNameToTypefaceFile.put("NanumMyeongjo", "nanummyeongjo.ttf");
        fontNameToTypefaceFile.put("NanumGothic", "nanumgothic.ttf");
        fontNameToTypefaceFile.put("Arial", "arial.ttf");
        fontNameToTypefaceFile.put("Eutemia", "eutemia.ttf");
        fontNameToTypefaceFile.put("GREENPIL", "greenpil.ttf");
        fontNameToTypefaceFile.put("Grinched", "grinched.ttf");
        fontNameToTypefaceFile.put("Helvetica", "helvetica.ttf");
        fontNameToTypefaceFile.put("Libertango", "libertango.ttf");
        fontNameToTypefaceFile.put("Metal Macabre", "metalmacabre.ttf");
        fontNameToTypefaceFile.put("Parry Hotter", "harrypotter.ttf");
        fontNameToTypefaceFile.put("SCRIPTIN", "scriptin.ttf");
        fontNameToTypefaceFile.put("The Godfather v2", "thegodfather_v2.ttf");
        fontNameToTypefaceFile.put("Aka Dora", "akadora.ttf");
        fontNameToTypefaceFile.put("Waltograph", "waltograph42.ttf");
        fontNameToTypefaceFile.put("DroidSerif", "droidserif.ttf");

        fontNames = new ArrayList<>(fontNameToTypefaceFile.keySet());
    }

    /**
     * @param typefaceName must be one of the font names provided from {@link FontProvider#getFontNames()}
     * @return the Typeface associated with {@code typefaceName}, or {@link Typeface#DEFAULT} otherwise
     */
    public Typeface getTypeface(@Nullable String typefaceName) {
        if (TextUtils.isEmpty(typefaceName)) {
            return Typeface.DEFAULT;
        } else {
            //noinspection Java8CollectionsApi
            if (typefaces.get(typefaceName) == null) {
                typefaces.put(typefaceName, Typeface.createFromAsset(resources.getAssets(), "font/" + fontNameToTypefaceFile.get(typefaceName)));
            }
            return typefaces.get(typefaceName);
        }
    }

    /**
     * use {@link FontProvider#getTypeface(String) to get Typeface for the font name}
     *
     * @return list of available font names
     */
    public List<String> getFontNames() {
        return fontNames;
    }

    /**
     * @return Default Font Name - <b>Helvetica</b>
     */
    public String getDefaultFontName() {
        return DEFAULT_FONT_NAME;
    }
}