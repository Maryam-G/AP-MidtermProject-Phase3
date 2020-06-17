package com.company.gui;

import com.company.controller.Controller;
import com.company.jurl.HttpClient;
import com.company.jurl.Jurl;
import com.company.model.Request;
import com.company.model.Setting;
import com.company.utils.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.MutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.io.File;
import java.net.HttpURLConnection;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * A class for showing main frame of Insomnia (GUI)
 *
 * @author Maryam Goli
 */
public class InsomniaFrame extends JFrame{

    private Setting setting;
    private Controller controller;
    private Request selectedRequest;

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
    public InsomniaFrame(){

        setting = FileUtils.readSettingFromFile();

        this.setTitle("Insomnia");
        this.setSize(new Dimension(1200, 700));
        this.setLocation(400, 150);
        this.setResizable(true);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout(1, 1));
        this.setIconImage(Toolkit.getDefaultToolkit().getImage(getClass().getResource("/icon.png")));

        addMenu();
        addPanels();

        if(menu.getDarkTheme().isSelected()){
            setTheme(Color.DARK_GRAY);
        }else if(menu.getLightTheme().isSelected()){
            setTheme(Color.LIGHT_GRAY);
        }

        if(menu.getExitFromApp().isSelected()){
            menu.getExit().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });
            //todo :
        }else if(menu.getHideOnSystemTray().isSelected()){
            //todo:
//            hideToSystemTray();
            menu.getExit().addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    setExtendedState(JFrame.ICONIFIED);
                }
            });
        }

        controller = new Controller();
    }

    /**
     * add menu to insomnia frame
     */
    public void addMenu(){
        menu = new Menu();
        this.setJMenuBar(menu);

        visibilityOfPanel1 = true;
        isFullScreen = false;

        if(setting.hasDarkTheme()){
            menu.getDarkTheme().setSelected(true);
            menu.getLightTheme().setSelected(false);
        }else{
            menu.getDarkTheme().setSelected(false);
            menu.getLightTheme().setSelected(true);
        }

        if(setting.isHideOnSystemTray()){
            menu.getHideOnSystemTray().setSelected(true);
            menu.getExitFromApp().setSelected(false);
        }else{
            menu.getHideOnSystemTray().setSelected(false);
            menu.getExitFromApp().setSelected(true);
        }

        menu.getOptions().addActionListener(new MenuHandler());
        menu.getExit().addActionListener(new MenuHandler());
        menu.getAbout().addActionListener(new MenuHandler());
        menu.getHelpItem().addActionListener(new MenuHandler());
        menu.getToggleFullScreen().addActionListener(new MenuHandler());
        menu.getToggleSidebar().addActionListener(new MenuHandler());

        menu.getDarkTheme().addItemListener(new CheckBoxHandler());
        menu.getLightTheme().addItemListener(new CheckBoxHandler());
        menu.getExitFromApp().addItemListener(new CheckBoxHandler());
        menu.getHideOnSystemTray().addItemListener(new CheckBoxHandler());
    }

    /**
     * add panels to Insomnia frame
     */
    public void addPanels(){
        panel1 = new Panel1();
        panel1.getTreeOfCollections().addTreeSelectionListener(new TreeOfCollectionsHandler());
        panel1.getSaveButton().addActionListener(new SaveButtonHandler());

        panel2 = new Panel2();
        panel2.getSendButton().addActionListener(new SendButtonHandler());

        panel3 = new Panel3();
        panel3.getCopyButton().addActionListener(new CopyButtonHandler());

        JSplitPane splitPaneForPanel2And3 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, panel2, panel3);
        splitPaneForPanel2And3.setResizeWeight(0.5);
        splitPaneForPanel2And3.setDividerSize(2);

        splitPane = new JSplitPane();
        splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
        splitPane.setLeftComponent(panel1);
        splitPane.setRightComponent(splitPaneForPanel2And3);
        splitPane.setDividerLocation(300);
        splitPane.setDividerSize(2);

        setTheme(Color.DARK_GRAY);

        this.add(splitPane, BorderLayout.CENTER);
    }

    /**
     * set theme for insomnia GUI
     * @param newColor new color for theme
     */
    public void setTheme(Color newColor) {
        this.setBackground(newColor);

        panel1.setBackground(newColor);
        panel1.setThemeForPanel1(newColor);

        panel2.setBackground(newColor);
        panel2.setThemeForPanel2(newColor);

        panel3.setBackground(newColor);
        panel3.setThemeForPanel3(newColor);
    }

    /**
     * An inner class for handling events that related to menu
     */
    private class MenuHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if (e.getSource().equals(menu.getOptions())) {
                menu.getOptionsFrame().setVisible(true);
            } else if (e.getSource().equals(menu.getToggleFullScreen())) {
                if (isFullScreen == false) {
                    InsomniaFrame.this.setExtendedState(JFrame.MAXIMIZED_BOTH);
                } else {
                    InsomniaFrame.this.setExtendedState(JFrame.NORMAL);
                }
                isFullScreen = !isFullScreen;
            } else if (e.getSource().equals(menu.getToggleSidebar())) {
                visibilityOfPanel1 = !visibilityOfPanel1;
                panel1.setVisible(visibilityOfPanel1);
                splitPane.setLeftComponent(panel1);
            } else if (e.getSource().equals(menu.getAbout())) {
                menu.getAboutFrame().setVisible(true);
            } else if (e.getSource().equals(menu.getHelpItem())) {
                menu.getHelpItemFrame().setVisible(true);
            } else if (e.getSource().equals(menu.getExit())) {
                if(menu.getExitFromApp().isSelected()){
                    //todo : exit
                    System.exit(0);
                }else if(menu.getHideOnSystemTray().isSelected()){
                    hideToSystemTray();
//                    //todo : system tray
                }
                //todo : pak kardan e ezafiat
//                insomniaFrame.dispose();
            }
        }
    }

    /**
     * An inner class for handling events that related to check boxes in options menuItem in Application menu
     */
    private class CheckBoxHandler implements ItemListener {
        @Override
        public void itemStateChanged(ItemEvent e) {
            if (e.getSource().equals(menu.getLightTheme())) {
                setTheme(Color.LIGHT_GRAY);
                setting.setHasDarkTheme(false);
            } else if (e.getSource().equals(menu.getDarkTheme())) {
                setTheme(Color.DARK_GRAY);
                setting.setHasDarkTheme(true);
            } else if (e.getSource().equals(menu.getExitFromApp())) {
                menu.getExit().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        System.exit(0);
                    }
                });
                setting.setHideOnSystemTray(false);
            } else if (e.getSource().equals(menu.getHideOnSystemTray())) {
                menu.getExit().addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        setExtendedState(JFrame.ICONIFIED);
                    }
                });
                setting.setHideOnSystemTray(true);
            }
            FileUtils.writeSettingInFile(setting);
        }
    }

    // -> phase 3:

    public void sendRequestFromGUI(boolean hasSaveArgument, String collectionName, String requestName){
        controller.setUrlAddress(panel2.getUrlAddress().getText());
        controller.setMethod(panel2.getComboBoxForMethod().getSelectedItem().toString());
        controller.setRequestHeaders(panel2.getHeaders());
        controller.setRequestBody(panel2.getBody());

        controller.createJurl(hasSaveArgument, collectionName, requestName);
    }

    public void showResponseInGUI(){
        panel3.setHeadersPanel(controller.getResponseHeaders());

        panel3.setRawBodyPanel(controller.getResponseBody());

        String status = "[ " + controller.getStatus().substring(10, controller.getStatus().length()-1) + " ]";
        String size = "[ " + controller.getSize() + " KB ]";
        String time = "[ " + controller.getTime() + " s ]";
        panel3.setInformationPanel(status, size, time);

        panel3.setPreviewBodyPanel(HttpClient.isImage());

        panel3.getTabbedPane().getComponentAt(1).repaint();
        panel3.getTabbedPane().getComponentAt(0).repaint();
        panel3.getRadioButtonRaw().setSelected(true);
        panel3.getRadioButtonPreview().setSelected(false);

        panel3.getPreviewPanel().setVisible(false);

        panel3.updateUI();
    }

    private class SendButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(panel2.getSendButton())){
                sendRequestFromGUI(false, null, null);
                showResponseInGUI();
            }
        }
    }

    public void showRequestInGUI(){
        panel2.getUrlAddress().setText(controller.getUrlAddress());
        panel2.getComboBoxForMethod().setSelectedItem(controller.getMethod());
        panel2.showRequestHeaders(controller.getRequestHeaders());
        panel2.showRequestBodyFormData(controller.getRequestBody());
    }

    public void openRequestInGUI(){
        controller.setUrlAddress(selectedRequest.getUrlString());
        controller.setMethod(selectedRequest.getMethod());
        controller.setRequestHeaders(selectedRequest.getRequestHeaders());
        controller.setRequestBody(selectedRequest.getRequestBody());

        controller.createJurl(false, null, null);

        showRequestInGUI();
        showResponseInGUI();

        panel2.updateUI();
        panel3.updateUI();
    }

    private class TreeOfCollectionsHandler implements TreeSelectionListener {

        @Override
        public void valueChanged(TreeSelectionEvent e) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) panel1.getTreeOfCollections().getLastSelectedPathComponent();

            // if nothing is selected
            if (node == null) return;

            // retrieve the node that was selected
            Object nodeInfo = node.getUserObject();
            String selectedNodeName = nodeInfo.toString();

            if(selectedNodeName.contains(".txt")){
                File file = new File("./Requests/AllCollections/" + node.getParent() + "/" + selectedNodeName);
                if(file.isFile()){
                    Request currentRequest = FileUtils.readRequestFromFile(file);
                    selectedRequest = currentRequest;
                    openRequestInGUI();
                    //todo : por kardan e gui
                }
            }
        }
    }

    private class SaveButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(panel1.getSaveButton())){
                buildFrameForSave();
            }
        }

        public void buildFrameForSave(){
            //selected request for saving ...

            Frame saveFrame = new JFrame("Save Request");
            saveFrame.setSize(new Dimension(500, 230));
            saveFrame.setLocation(900, 400);
            saveFrame.setLayout(new BorderLayout());
            saveFrame.setResizable(false);

            JPanel savePanel = new JPanel();
            savePanel.setLayout(new BorderLayout());
            savePanel.setBorder(new EmptyBorder(20, 10, 10, 10));

            // create panel and add to frame
            JPanel nameOfRequestPanel = new JPanel();
            nameOfRequestPanel.setLayout(new BorderLayout());
            nameOfRequestPanel.setBorder(new EmptyBorder(30, 10, 30, 10));

            JLabel nameOfRequestLabel = new JLabel("Name Of Request");
            nameOfRequestLabel.setFont(new Font("Calibri", 14, 19));
            nameOfRequestLabel.setBorder(new EmptyBorder(0, 5, 10, 10));

            JTextField nameOfRequestField = new JTextField();
            nameOfRequestField.setText("MyRequest");

            JPanel nameOfCollectionPanel = new JPanel();
            nameOfCollectionPanel.setLayout(new BorderLayout());
            nameOfCollectionPanel.setBorder(new EmptyBorder(30, 10, 30, 10));

            JLabel nameOfCollectionLabel = new JLabel("Name Of Collection");
            nameOfCollectionLabel.setFont(new Font("Calibri", 14, 19));
            nameOfCollectionLabel.setBorder(new EmptyBorder(0, 5, 10, 10));

            JComboBox collectionsComboBox = new JComboBox();
            File directory = new File("./Requests/AllCollections");
            File[] allCollections = directory.listFiles();
            for(File f : allCollections) {
                if (f.isDirectory()) {
                    collectionsComboBox.addItem(f.getName());
                }
            }

            JButton saveInSelectedCollectionButton = new JButton("< Save in selected collection >");

            nameOfRequestPanel.add(nameOfRequestLabel, BorderLayout.NORTH);
            nameOfRequestPanel.add(nameOfRequestField, BorderLayout.CENTER);

            nameOfCollectionPanel.add(nameOfCollectionLabel, BorderLayout.NORTH);
            nameOfCollectionPanel.add(collectionsComboBox, BorderLayout.CENTER);

            JPanel finalPanel = new JPanel();
            finalPanel.setLayout(new GridLayout(1, 2, 2, 2));
            finalPanel.add(nameOfRequestPanel);
            finalPanel.add(nameOfCollectionPanel);

            savePanel.add(finalPanel, BorderLayout.CENTER);
            savePanel.add(saveInSelectedCollectionButton, BorderLayout.SOUTH);

            saveFrame.add(savePanel, BorderLayout.CENTER);
            saveFrame.setVisible(true);

            saveInSelectedCollectionButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String nameOfNewRequest = nameOfRequestField.getText().replaceAll(" ", "");
                    String nameOfSelectedCollection = collectionsComboBox.getSelectedItem().toString();

                    InsomniaFrame.this.sendRequestFromGUI(true, nameOfSelectedCollection, nameOfNewRequest);

                    DefaultMutableTreeNode newSavedRequest = new DefaultMutableTreeNode(nameOfNewRequest+".txt");

                    DefaultMutableTreeNode parentNode = new DefaultMutableTreeNode();
                    for(int i = 0; i < panel1.getRootCollections().getChildCount(); i++){
                        if(panel1.getRootCollections().getChildAt(i).toString().equals(nameOfSelectedCollection)){
                             parentNode = (DefaultMutableTreeNode) panel1.getRootCollections().getChildAt(i);
                             break;
                        }
                    }
                    DefaultTreeModel model = (DefaultTreeModel) panel1.getTreeOfCollections().getModel();
                    model.insertNodeInto(newSavedRequest, parentNode, parentNode.getChildCount());
                    panel1.getTreeOfCollections().scrollPathToVisible(new TreePath(newSavedRequest.getPath()));

                    saveFrame.setVisible(false);
                }
            });
        }
    }

    private class CopyButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(panel3.getCopyButton())){
                String headersContent = "Response headers : \n\n";
                for(Map.Entry<String, List<String>> entry : controller.getResponseHeaders().entrySet()){
                    headersContent += entry.getKey() + ": " + entry.getValue() + "\n";
                }
                StringSelection stringSelection = new StringSelection(headersContent);
                Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
                clipboard.setContents(stringSelection, null);
            }
        }
    }

    public void hideToSystemTray(){
        if (SystemTray.isSupported()) {
            TrayIcon trayIcon;
            SystemTray tray;
            tray = SystemTray.getSystemTray();

            Image image = Toolkit.getDefaultToolkit().getImage("./src/icon.png");
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Open");
            defaultItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    setVisible(true);
                    setExtendedState(JFrame.NORMAL);
                }
            });
            popup.add(defaultItem);
            trayIcon = new TrayIcon(image, "SystemTray", popup);
            trayIcon.setImageAutoSize(true);

            addWindowStateListener(new WindowStateListener() {
                public void windowStateChanged(WindowEvent e) {
                    if (e.getNewState() == ICONIFIED) {
                        try {
                            tray.add(trayIcon);
                            setVisible(false);
                        } catch (AWTException ex) {
                        }
                    }
                    if (e.getNewState() == 7) {
                        try {
                            tray.add(trayIcon);
                            setVisible(false);
                        } catch (AWTException ex) {
                        }
                    }
                    if (e.getNewState() == MAXIMIZED_BOTH) {
                        tray.remove(trayIcon);
                        setVisible(true);
                    }
                    if (e.getNewState() == NORMAL) {
                        tray.remove(trayIcon);
                        setVisible(true);
                    }
                }
            });
            setIconImage(Toolkit.getDefaultToolkit().getImage("./src/icon.png"));
            setVisible(true);
        } else {
        }
    }
}
