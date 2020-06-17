package com.company.model;

import java.io.Serializable;

public class Setting implements Serializable {

    private boolean hideOnSystemTray;
    private boolean hasDarkTheme;

    public Setting(boolean hasDarkTheme, boolean hideOnSystemTray){
        this.hideOnSystemTray = hideOnSystemTray;
        this.hasDarkTheme = hasDarkTheme;
    }

    public boolean hasDarkTheme() {
        return hasDarkTheme;
    }

    public boolean isHideOnSystemTray() {
        return hideOnSystemTray;
    }

    public void setHasDarkTheme(boolean hasDarkTheme) {
        this.hasDarkTheme = hasDarkTheme;
    }

    public void setHideOnSystemTray(boolean hideOnSystemTray) {
        this.hideOnSystemTray = hideOnSystemTray;
    }
}
