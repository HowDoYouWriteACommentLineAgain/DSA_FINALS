package org.dsa.utils;

import com.formdev.flatlaf.FlatDarkLaf;
import com.formdev.flatlaf.FlatLightLaf;
import com.formdev.flatlaf.intellijthemes.*;
import java.util.prefs.Preferences;

public final class ThemeManager {
    private static final String PREF_NODE = "org.dsa";
    private static final String PREF_KEY = "theme";
    public static final String THEME_LIGHT = "Light";
    public static final String THEME_DARK = "Dark";
    public static final String THEME_DARK_PURPLE = "DarkPurple";
    public static final String THEME_NORD = "Nord";
    public static final String THEME_CARBON = "Carbon";
    public static final String THEME_MONOCAI = "Monocai";
    public static final String THEME_ONE_DARK = "OneDark";
    public static final String THEME_CYAN_LIGHT = "CyanLight";

    public static void applyTheme() {
        Preferences prefs = Preferences.userRoot().node(PREF_NODE);
        try {
            String theme = prefs.get(PREF_KEY, THEME_LIGHT);
            switch (theme) {
                case THEME_DARK -> FlatDarkLaf.setup();
                case THEME_LIGHT -> FlatLightLaf.setup();
                case THEME_DARK_PURPLE -> new FlatDarkPurpleIJTheme().setup();
                case THEME_NORD -> new FlatNordIJTheme().setup();
                case THEME_CARBON -> new FlatCarbonIJTheme().setup();
                case THEME_MONOCAI -> new FlatMonocaiIJTheme().setup();
                case THEME_ONE_DARK -> new FlatOneDarkIJTheme().setup();
                case THEME_CYAN_LIGHT -> new FlatCyanLightIJTheme().setup();
                default -> FlatLightLaf.setup();
            }
        } catch(Exception e){
            e.printStackTrace();
        }
    }

    public static String getCurrentTheme () {
        return Preferences.userRoot().node(PREF_NODE).get(PREF_KEY, THEME_LIGHT);
    }

    public static void setTheme (String theme){
        Preferences.userRoot().node(PREF_NODE).put(PREF_KEY, theme);
    }
}


