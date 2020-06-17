package com.company.gui;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

/**
 * A class for showing menuBar in insomnia frame
 * menu items : Application : [Options & Exit]
 *              View :        [ToggleFullScreen & ToggleSidebar]
 *              Help :        [About & Help]
 *
 * @author Maryam Goli
 */
public class Menu extends JMenuBar {

    private JMenu application;
    private JMenu view;
    private JMenu help;

    //application menu:
    private JMenuItem options;
    private JMenuItem exit;

    private JFrame optionsFrame;
    private JCheckBox exitFromApp;
    private JCheckBox hideOnSystemTray;
    private JCheckBox lightTheme;
    private JCheckBox darkTheme;

    //view menu:
    private JMenuItem toggleFullScreen;
    private JMenuItem toggleSidebar;

    //help menu:
    private JMenuItem helpItem;
    private JMenuItem about;

    private JTextArea helpItemText;
    private JFrame helpItemFrame;
    private JTextArea aboutText;
    private JFrame aboutFrame;

    /**
     * constructor method
     */
    public Menu(){

        initApplicationMenu();
        initViewMenu();
        initHelpMenu();

        this.add(application);
        this.add(view);
        this.add(help);
    }

    /**
     * init menu item "Application"
     */
    public void initApplicationMenu(){
        application = new JMenu("Application");
        application.setFont(new Font("Calibri", 14, 15));
        application.setMnemonic(KeyEvent.VK_A);

        options = new JMenuItem("Options");
        options.setFont(new Font("Calibri", 14, 15));
        options.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O, ActionEvent.ALT_MASK));
        setOptionsFrame();

        exit = new JMenuItem("Exit");
        exit.setFont(new Font("Calibri", 14, 15));
        exit.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, ActionEvent.ALT_MASK));

        application.add(options);
        application.add(exit);
    }

    /**
     * build frame for showing options
     */
    public void setOptionsFrame(){
        optionsFrame = new JFrame("Options");
        optionsFrame.setSize(new Dimension(300, 300));
        optionsFrame.setLayout(new GridLayout(2, 1, 2, 1));
        optionsFrame.setLocation(800, 400);
        optionsFrame.setResizable(false);

        lightTheme = new JCheckBox("Light Theme");
        darkTheme = new JCheckBox("Dark Theme");
        exitFromApp = new JCheckBox("Exit From App");
        hideOnSystemTray = new JCheckBox("Hide On System Tray");

        JPanel exitPanel = new JPanel();
        exitPanel.setLayout(new GridLayout(2, 1, 1, 1));
        exitPanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY
                , 1, true), " [ Exit ]", TitledBorder.LEFT, TitledBorder.TOP));
        ButtonGroup buttonGroup2 = new ButtonGroup();
        buttonGroup2.add(exitFromApp);
        buttonGroup2.add(hideOnSystemTray);

        exitPanel.add(exitFromApp);
        exitPanel.add(hideOnSystemTray);

        JPanel themePanel = new JPanel();
        themePanel.setLayout(new GridLayout(2, 1, 1, 1));
        themePanel.setBorder(BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.DARK_GRAY
                , 1, true), " [ Theme ]", TitledBorder.LEFT, TitledBorder.TOP));
        ButtonGroup buttonGroup3 = new ButtonGroup();
        buttonGroup3.add(darkTheme);
        buttonGroup3.add(lightTheme);

        themePanel.add(lightTheme);
        themePanel.add(darkTheme);

        optionsFrame.add(exitPanel);
        optionsFrame.add(themePanel);

    }

    /**
     * init menu item "View"
     */
    public void initViewMenu(){
        view = new JMenu("View");
        view.setFont(new Font("Calibri", 14, 15));
        view.setMnemonic(KeyEvent.VK_V);

        toggleFullScreen = new JMenuItem("Toggle Full Screen");
        toggleFullScreen.setFont(new Font("Calibri", 14, 15));
        toggleFullScreen.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_F, ActionEvent.ALT_MASK));

        toggleSidebar = new JMenuItem("Toggle Sidebar");
        toggleSidebar.setFont(new Font("Calibri", 14, 15));
        toggleSidebar.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S, ActionEvent.ALT_MASK));

        view.add(toggleFullScreen);
        view.add(toggleSidebar);
    }

    /**
     * init menu item "Help"
     */
    public void initHelpMenu(){
        help = new JMenu("Help");
        help.setFont(new Font("Calibri", 14, 15));
        help.setMnemonic(KeyEvent.VK_H);

        helpItem = new JMenuItem("Help");
        helpItem.setFont(new Font("Calibri", 14, 15));
        helpItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_M, ActionEvent.ALT_MASK));

        about = new JMenuItem("About");
        about.setFont(new Font("Calibri", 14, 15));
        about.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, ActionEvent.ALT_MASK));

        setHelpFrame();

        help.add(helpItem);
        help.add(about);
    }

    /**
     * build frame for showing help-text and about-text
     */
    public void setHelpFrame(){
        aboutText = new JTextArea();
        aboutText.setFont(new Font("Calibri", 14, 14));
        aboutText.setText("Hi, I'm Maryam Goli!\n\nMy student number : 9831054\nMy email : goli.mary.m@gmail.com\n\nI hope you enjoy the app :)\n\n~Maryam ");
        aboutText.setEditable(false);
        aboutFrame = new JFrame("About");
        aboutFrame.setSize(300, 300);
        aboutFrame.setLocation(800, 400);
        aboutFrame.add(aboutText);

        helpItemText = new JTextArea();
        helpItemText.setFont(new Font("Calibri", 14, 16));
        String help =
                "<-> PANEL-1 -> COLLECTIONS:\n" +
                "       * [Button]" + "              {Save}: saving request in selected collection" + "\n" +
                "       * [Button]" + "              {Create Collection}: create a new collection" + "\n" +
                "       * [Panel]" + "                {All Collections}: showing list of all collections and saved requests" + "\n\n" +
                "<-> PANEL-2 -> SET REQUEST:\n" +
                "       * [Combo-Box]" + "          {-}: select method of request -> GET-POST-PUT-DELETE" + "\n" +
                "       * [Text-Field]" + "             {-}: enter URL address of request here" + "\n" +
                "       * [Button]" + "                  {Send}: send your request and get response " + "\n" +
                "       * [Tab]" + "                      {Body}: set body of your request in this part -> No-Body and Form-Data" + "\n" +
                "       * [Radio-Button]" + "       {No-Body}: no body for request" + "\n" +
                "       * [Radio-Button]" + "       {Form-Data}: set Multipart/Form-Data body for request" + "\n" +
                "       * [Tab]" + "                      {Headers}: set headers of request in this part" + "\n\n" +
                "<-> PANEL-3 -> SHOW RESPONSE :\n" +
                "       * [Label]" +  "                    {Status}: showing status of response (for example : 200 OK)" + "\n" +
                "       * [Label]" + "                    {Time}: showing response time " + "\n" +
                "       * [Label]" + "                    {Size}: showing size of response body" + "\n" +
                "       * [Tab]" + "                       {Body}: showing response body -> Raw and Preview " + "\n" +
                "       * [Radio-Button]" + "        {Raw}: for showing response body in text format" + "\n" +
                "       * [Radio-Button]" + "        {Preview}: for showing image with <image/png> content type " + "\n" +
                "       * [Tab]" + "                       {Headers}: showing headers of response " + "\n" +
                "       * [Button]" + "                  {Copy to Clipboard}: copy response headers to clipboard" + "\n";
        helpItemText.setText(help);

        helpItemText.setEditable(false);
        helpItemFrame = new JFrame("Help");
        helpItemFrame.setSize(700, 700);
        helpItemFrame.setLocation(650, 150);
        helpItemFrame.add(new JScrollPane(helpItemText));
    }

    /**
     * get menuItem "Options"
     * @return options field
     */
    public JMenuItem getOptions() {
        return options;
    }

    /**
     * get Options frame
     * @return optionsFrame field
     */
    public JFrame getOptionsFrame() {
        return optionsFrame;
    }

    /**
     * get menuItem "Exit"
     * @return exit field
     */
    public JMenuItem getExit() {
        return exit;
    }

    /**
     * get menuItem "About"
     * @return about field
     */
    public JMenuItem getAbout() {
        return about;
    }

    /**
     * get about frame
     * @return aboutFrame field
     */
    public JFrame getAboutFrame() {
        return aboutFrame;
    }

    /**
     * get menuItem "Help"
     * @return helpItem field
     */
    public JMenuItem getHelpItem() {
        return helpItem;
    }

    /**
     * get helpItem frame
     * @return helpItemFrame field
     */
    public JFrame getHelpItemFrame() {
        return helpItemFrame;
    }

    /**
     * get menuItem "ToggleFullScreen"
     * @return toggleFullScreen field
     */
    public JMenuItem getToggleFullScreen() {
        return toggleFullScreen;
    }

    /**
     * get menuItem "ToggleSidebar"
     * @return toggleSidebar field
     */
    public JMenuItem getToggleSidebar() {
        return toggleSidebar;
    }

    /**
     * get checkBox "Dark Theme"
     * @return darkTheme field
     */
    public JCheckBox getDarkTheme() {
        return darkTheme;
    }

    /**
     * get checkBox "Light Theme"
     * @return lightTheme field
     */
    public JCheckBox getLightTheme() {
        return lightTheme;
    }

    /**
     * get checkBox "Exit From App"
     * @return exitFromApp field
     */
    public JCheckBox getExitFromApp() {
        return exitFromApp;
    }

    /**
     * get checkBox "Hide On System Tray"
     * @return hideOnSystemTray field
     */
    public JCheckBox getHideOnSystemTray() {
        return hideOnSystemTray;
    }

}

