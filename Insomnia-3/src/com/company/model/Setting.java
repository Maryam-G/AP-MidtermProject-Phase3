package com.company.model;

import java.io.Serializable;

/**
 * A class for saving setting of Insomnia app (for example : Insomnia-Theme & how to exit the program)
 *
 * @author Maryam Goli
 */
public class Setting implements Serializable {

    private boolean hideOnSystemTray;
    private boolean hasDarkTheme;

    /**
     * constructor method
     * @param hasDarkTheme dark or light theme?
     * @param hideOnSystemTray hide on system tray or exit from app?
     */
    public Setting(boolean hasDarkTheme, boolean hideOnSystemTray){
        this.hideOnSystemTray = hideOnSystemTray;
        this.hasDarkTheme = hasDarkTheme;
    }

    /**
     * get hasDarkTheme
     * @return hasDarkTheme field
     */
    public boolean hasDarkTheme() {
        return hasDarkTheme;
    }

    /**
     * get isHideOnSystemTray
     * @return isHideOnSystemTray field
     */
    public boolean isHideOnSystemTray() {
        return hideOnSystemTray;
    }

    /**
     * set hasDarkTheme
     * @param hasDarkTheme field
     */
    public void setHasDarkTheme(boolean hasDarkTheme) {
        this.hasDarkTheme = hasDarkTheme;
    }

    /**
     * set hideOnSystemTray
     * @param hideOnSystemTray field
     */
    public void setHideOnSystemTray(boolean hideOnSystemTray) {
        this.hideOnSystemTray = hideOnSystemTray;
    }
}
