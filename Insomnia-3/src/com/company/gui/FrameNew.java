package com.company.gui;

import com.company.controller.Controller;
import com.company.jurl.HttpClient;
import com.company.jurl.Jurl;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.HttpURLConnection;
import java.sql.SQLOutput;

/**
 * A class for showing main frame of Insomnia (GUI)
 *
 * @author Maryam Goli
 */
public class FrameNew extends JFrame{

    private Controller controller;

    private Menu menu;
    private boolean visibilityOfPanel1;
    private boolean isFullScreen;

    private JSplitPane splitPane;

    private Panel1 panel1;
    private Panel2 panel2;
    private Panel3 panel3;

    /**
     * constructor method
     */
    public FrameNew(){

        this.setTitle("Insomnia");
        this.setSize(new Dimension(1200, 700));
        this.setLocation(400, 150);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(1, 1));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));

//        addMenu();
        addPanels();

        controller = new Controller();
    }

    /**
     * add menu to insomnia frame
     */
//    public void addMenu(){
//        menu = new Menu();
//        this.setJMenuBar(menu);
//
//        visibilityOfPanel1 = true;
//        isFullScreen = false;
//
//        menu.getOptions().addActionListener(new MenuHandler());
//        menu.getExit().addActionListener(new MenuHandler());
//        menu.getAbout().addActionListener(new MenuHandler());
//        menu.getHelpItem().addActionListener(new MenuHandler());
//        menu.getToggleFullScreen().addActionListener(new MenuHandler());
//        menu.getToggleSidebar().addActionListener(new MenuHandler());
//
//        menu.getDarkTheme().addItemListener(new CheckBoxHandler());
//        menu.getLightTheme().addItemListener(new CheckBoxHandler());
//        menu.getExitFromApp().addItemListener(new CheckBoxHandler());
//        menu.getHideOnSystemTray().addItemListener(new CheckBoxHandler());
//    }

    /**
     * add panels to Insomnia frame
     */
    public void addPanels(){
        panel1 = new Panel1();

        panel2 = new Panel2();
        panel2.getSendButton().addActionListener(new SendButtonHandler());

        panel3 = new Panel3();

        JSplitPane splitPaneForPanel2And3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel2, panel3);
        splitPaneForPanel2And3.setResizeWeight(0.5);
        splitPaneForPanel2And3.setDividerSize(2);

        splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(panel1);
        splitPane.setRightComponent(splitPaneForPanel2And3);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(2);

//        setTheme(Color.DARK_GRAY);

        this.add(splitPane, BorderLayout.CENTER);
    }

    /**
     * set theme for insomnia GUI
     * @param newColor new color for theme
     */
//    public void setTheme(Color newColor) {
//        this.setBackground(newColor);
//
//        panel1.setBackground(newColor);
//        panel1.setThemeForPanel1(newColor);
//
//        panel2.setBackground(newColor);
//        panel2.setThemeForPanel2(newColor);
//
//        panel3.setBackground(newColor);
//        panel3.setThemeForPanel3(newColor);
//    }

    /**
     * An inner class for handling events that related to menu
     */
//    private class MenuHandler implements ActionListener {
//
//        @Override
//        public void actionPerformed(ActionEvent e) {
//            if (e.getSource().equals(menu.getOptions())) {
//                menu.getOptionsFrame().setVisible(true);
//            } else if (e.getSource().equals(menu.getToggleFullScreen())) {
//                if (isFullScreen == false) {
//                    InsomniaFrame.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
//                } else {
//                    InsomniaFrame.this.setExtendedState(JFrame.NORMAL);
//                }
//                isFullScreen = !isFullScreen;
//            } else if (e.getSource().equals(menu.getToggleSidebar())) {
//                visibilityOfPanel1 = !visibilityOfPanel1;
//                panel1.setVisible(visibilityOfPanel1);
//                splitPane.setLeftComponent(panel1);
//            } else if (e.getSource().equals(menu.getAbout())) {
//                menu.getAboutFrame().setVisible(true);
//            } else if (e.getSource().equals(menu.getHelpItem())) {
//                menu.getHelpItemFrame().setVisible(true);
//            } else if (e.getSource().equals(menu.getExit())) {
//                if(menu.getExitFromApp().isSelected()){
//                    //todo : exit
//                }else if(menu.getHideOnSystemTray().isSelected()){
//                    //todo : system tray
//                }
//                //todo : pak kardan e ezafiat
////                insomniaFrame.dispose();
//            }
//        }
//    }

    /**
     * An inner class for handling events that related to check boxes in options menuItem in Application menu
     */
//    private class CheckBoxHandler implements ItemListener {
//        @Override
//        public void itemStateChanged(ItemEvent e) {
//            if (e.getSource().equals(menu.getLightTheme())) {
//                setTheme(Color.LIGHT_GRAY);
//            } else if (e.getSource().equals(menu.getDarkTheme())) {
//                setTheme(Color.DARK_GRAY);
//            } else if (e.getSource().equals(menu.getExitFromApp())) {
//                InsomniaFrame.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//            } else if (e.getSource().equals(menu.getHideOnSystemTray())) {
//                InsomniaFrame.this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
//            }
//        }
//    }

    // -> phase 3:

    public void setRequestGUI(){
        controller.setUrlAddress(panel2.getUrlAddress().getText());
        controller.setMethod(panel2.getComboBoxForMethod().getSelectedItem().toString());
        controller.setRequestHeaders(panel2.getHeaders());
        controller.setRequestBody(panel2.getBody());

        controller.createJurl();
    }

    public void setResponseGUI(){
        panel3.setHeadersPanel(controller.getResponseHeaders());

        panel3.setRawBodyPanel(controller.getResponseBody());

        String status = "[ " + controller.getStatus().substring(10, controller.getStatus().length()-1) + " ]";
        String size = "[ " + controller.getSize() + " KB ]";
        String time = "[ " + controller.getTime() + " s ]";
        panel3.setInformationPanel(status, size, time);

        if(HttpClient.isImage()){
            panel3.addImageToPreviewPanel();
        }

        panel3.updateUI();
    }

    private class SendButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(panel2.getSendButton())){
                setRequestGUI();
                setResponseGUI();
            }
        }
    }

}

