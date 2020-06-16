package com.company.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for showing panel 3 and response of selected request with response headers and body
 *
 * @author Maryam Goli
 */
public class Panel3 extends JPanel {

    // info panel :
    private JPanel responseInfoPanel;
    private JTextField statusField;
    private JTextField timeField;
    private JTextField sizeField;

    // body panel :
    private JPanel responseBodyPanel;
    private JTabbedPane tabbedPane;

    private JPanel buttonGroupPanelForBody;

    private JRadioButton radioButtonRaw;
    private JPanel rawPanel;
    private JScrollPane rawScrollPane;
    private JTextArea rawText;

    private JRadioButton radioButtonPreview;
    private JPanel previewPanel;

    //header panel :
    private JPanel responseHeaderPanel;
    private ArrayList<HeaderItemPanel> headersList;
    private JButton copyButton;
    private JPanel headersListPanel;

    /**
     * constructor method
     */
    public Panel3(){
        this.setLayout(new BorderLayout());
        addInfoPanel();

//        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Calibri", 45, 18));

        // body :
        addResponseBodyPanel();
        //header:
        addResponseHeadersPanel();

        // add panels to tabs of tabbedPane
        tabbedPane.add("Body",responseBodyPanel);
        tabbedPane.add("Header", responseHeaderPanel);

        this.add(tabbedPane, BorderLayout.CENTER);
    }

    /**
     * add panel for showing some information about response (for example size, status-code, ...)
     */
    public void addInfoPanel(){
        // top of panel 3:
        statusField = new JTextField(" [ Status ] ");
        statusField.setFont(new Font("Calibri", 45, 17));
        statusField.setEditable(false);
        statusField.setBackground(new Color(207, 214, 210));
        statusField.isOpaque();

        timeField = new JTextField(" [ Time ] ");
        timeField.setFont(new Font("Calibri", 45, 17));
        timeField.setEditable(false);
        timeField.setBackground(new Color(207, 214, 210));
        timeField.isOpaque();

        sizeField = new JTextField(" [ Size ] ");
        sizeField.setFont(new Font("Calibri", 45, 17));
        sizeField.setEditable(false);
        sizeField.setBackground(new Color(207, 214, 210));
        sizeField.isOpaque();

        responseInfoPanel = new JPanel();
        responseInfoPanel.setLayout(new FlowLayout(FlowLayout.LEFT, 6, 6));
        responseInfoPanel.setBackground(Color.WHITE);
        responseInfoPanel.add(statusField);
        responseInfoPanel.add(timeField);
        responseInfoPanel.add(sizeField);
        responseInfoPanel.setPreferredSize(new Dimension(responseInfoPanel.getWidth(), 50));

        this.add(responseInfoPanel, BorderLayout.PAGE_START);
    }

    /**
     * add panel for showing response headers with key and value
     */
    public void addResponseHeadersPanel(){
        responseHeaderPanel = new JPanel();
//        BoxLayout boxLayout = new BoxLayout(responseHeaderPanel, BoxLayout.Y_AXIS);
//        responseHeaderPanel.setLayout(boxLayout);
        responseHeaderPanel.setLayout(new BorderLayout());

        headersList = new ArrayList<>();

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //button copy to clipboard :

        copyButton = new JButton("Copy to Clipboard");
        copyButton.setFont(new Font("Calibri", 45, 15));
        copyButton.addActionListener(new CopyButtonHandler());

        JPanel copyButtonPanel = new JPanel();
        copyButtonPanel.setLayout(new GridLayout(1, 1));
        copyButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        copyButtonPanel.add(copyButton);

//        responseHeaderPanel.add(copyButtonPanel);
        panel.add(copyButtonPanel, BorderLayout.NORTH);

        JTextField keyField = new JTextField(" key ");
        keyField.setBackground(new Color(207, 214, 210));
        keyField.setFont(new Font("Calibri", 45, 15));
        keyField.setEditable(false);

        JTextField valueField = new JTextField(" value ");
        valueField.setBackground(new Color(207, 214, 210));
        valueField.setFont(new Font("Calibri", 45, 15));
        valueField.setEditable(false);

        JPanel keyValuePanel = new JPanel();
        keyValuePanel.setLayout(new GridLayout(1, 2));
        keyValuePanel.add(keyField);
        keyValuePanel.add(valueField);
        keyValuePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));

//        responseHeaderPanel.add(keyValuePanel);
        panel.add(keyValuePanel, BorderLayout.CENTER);

        responseHeaderPanel.add(panel, BorderLayout.PAGE_START);

        headersListPanel = new JPanel();
        BoxLayout boxLayout2 = new BoxLayout(headersListPanel, BoxLayout.Y_AXIS);
        headersListPanel.setLayout(boxLayout2);

        responseHeaderPanel.add(headersListPanel, BorderLayout.CENTER);
    }

    /**
     * add panel for showing response body [two tabs : Raw & Preview]
     */
    public void addResponseBodyPanel(){
        setRawPanel();
        setPreviewPanel();

        radioButtonRaw.setSelected(true);

        radioButtonRaw.addActionListener(new RadioButtonHandler());
        radioButtonPreview.addActionListener(new RadioButtonHandler());

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonRaw);
        buttonGroup.add(radioButtonPreview);

        buttonGroupPanelForBody = new JPanel();
        buttonGroupPanelForBody.setLayout(new FlowLayout());
        buttonGroupPanelForBody.add(radioButtonRaw);
        buttonGroupPanelForBody.add(radioButtonPreview);
        buttonGroupPanelForBody.setVisible(true);

        responseBodyPanel = new JPanel();
        responseBodyPanel.setLayout(new BorderLayout());

//        responseBodyPanel.add(new JScrollPane(buttonGroupPanelForBody), BorderLayout.NORTH);
//        responseBodyPanel.add(new JScrollPane(previewPanel), BorderLayout.CENTER);
//        responseBodyPanel.add(new JScrollPane(rawPanel), BorderLayout.CENTER);

        responseBodyPanel.add(buttonGroupPanelForBody, BorderLayout.NORTH);
        responseBodyPanel.add(previewPanel, BorderLayout.CENTER);
        responseBodyPanel.add(rawPanel, BorderLayout.CENTER);

        previewPanel.setVisible(false);
        rawPanel.setVisible(true);
    }

    /**
     * set panel for Raw body
     */
    public void setRawPanel(){
        radioButtonRaw = new JRadioButton("Raw");

        rawPanel = new JPanel();
        rawPanel.setLayout(new BorderLayout());

        rawText = new JTextArea();
        rawText.setFont(new Font("Calibri", 45, 15));
        rawText.setText(" -[ Response Body]-");
        rawText.setEditable(false);

        JPanel textPanel = new JPanel();
        textPanel.setLayout(new BorderLayout());
        textPanel.add(rawText, BorderLayout.CENTER);

        rawScrollPane = new JScrollPane(textPanel);
        rawPanel.add(rawScrollPane, BorderLayout.CENTER);


//        rawPanel.add(new JScrollPane(textPanel), BorderLayout.CENTER);
    }

    /**
     * set panel for Preview body
     */
    public void setPreviewPanel(){
        radioButtonPreview = new JRadioButton("Preview");
        previewPanel = new JPanel();
        previewPanel.setLayout(new BorderLayout());
    }

    //todo : copy button handler
    /**
     * An inner class for handling function of "Copy to Clipboard" button
     */
    private class CopyButtonHandler implements ActionListener{

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(copyButton)){

            }
        }
    }

    /**
     * An inner class for handling events that related to tabs of response body
     */
    private class RadioButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            // panel 3 :
            if (radioButtonRaw.isSelected()) {
                rawPanel.setVisible(true);
                //todo : pak
//                responseBodyPanel.add(buttonGroupPanelForBody, BorderLayout.NORTH);
//                responseBodyPanel.add(rawPanel, BorderLayout.CENTER);
                previewPanel.setVisible(false);
                updateUI();
            } else if (radioButtonPreview.isSelected()) {
                previewPanel.setVisible(true);
                //todo : pak
//                responseBodyPanel.add(previewPanel, BorderLayout.CENTER);
                rawPanel.setVisible(false);
                updateUI();
            }
        }
    }

    /**
     * a panel for response header item
     */
    private class HeaderItemPanel extends JPanel{

        private JPanel keyValuePanel;
        private JTextField keyField;
        private JTextField valueField;

        public HeaderItemPanel(String key, List<String> value){
            keyField = new JTextField(key);
            keyField.setEditable(false);
            keyField.setFont(new Font("Calibri", 45, 15));

            valueField = new JTextField(value.toString());
            valueField.setEditable(false);
            valueField.setFont(new Font("Calibri", 45, 15));

            keyValuePanel = new JPanel();
            keyValuePanel.setLayout(new GridLayout());
            keyValuePanel.add(keyField);
            keyValuePanel.add(valueField);
            keyValuePanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

//            responseHeaderPanel.add(keyValuePanel);
            this.setLayout(new BorderLayout());
            this.add(keyValuePanel, BorderLayout.CENTER);
        }

    }


    /**
     * set theme for panel 3
     * @param newColor new color for theme
     */
    public void setThemeForPanel3(Color newColor){
        responseBodyPanel.setBackground(newColor);
        rawPanel.setBackground(newColor);
        previewPanel.setBackground(newColor);

        responseHeaderPanel.setBackground(newColor);
    }

    // -> phase 3:

    public void setHeadersPanel(Map<String, List<String>> responseHeaders){

//        headersListPanel = new JPanel();
//        BoxLayout boxLayout2 = new BoxLayout(headersListPanel, BoxLayout.Y_AXIS);
//        headersListPanel.setLayout(boxLayout2);

        headersListPanel.removeAll();
        headersListPanel.revalidate();
        headersListPanel.repaint();

        BoxLayout boxLayout = new BoxLayout(headersListPanel, BoxLayout.Y_AXIS);
        headersListPanel.setLayout(boxLayout);

//        headersList = new ArrayList<>();

        String key = "";
        List<String> value ;
        HeaderItemPanel headerItemPanel;
        for(Map.Entry<String, List<String>> entry : responseHeaders.entrySet()){
            if(entry.getKey() != null){
                key = entry.getKey();
                value = entry.getValue();
                System.out.println(key + ".............." + value);
                headerItemPanel = new HeaderItemPanel(key, value);
                headersListPanel.add(headerItemPanel);
//                headersList.add(headerItemPanel);
            }
        }

        headersListPanel.setVisible(true);
        responseHeaderPanel.add(headersListPanel, BorderLayout.CENTER);

//        responseHeaderPanel.setVisible(true);
        updateUI();
    }

    public void setRawBodyPanel(String responseBody){
        rawText.setText(responseBody);
    }

    public void setPreviewBodyPanel(boolean isImage){
        if(isImage){

//            previewPanel.removeAll();
//            previewPanel.revalidate();
//            previewPanel.repaint();

            addImageToPreviewPanel();
        }else{
            //todo :nashooood
//            JPanel whitePanel = new JPanel();
//
//            previewPanel.add(whitePanel, BorderLayout.CENTER);
//            responseBodyPanel.add(previewPanel, BorderLayout.CENTER);
            radioButtonRaw.setSelected(true);
            previewPanel.removeAll();
            previewPanel.revalidate();
            previewPanel.repaint();

//            rawPanel.setVisible(true);
//            previewPanel.setVisible(false);
            updateUI();
        }
    }

    public void setInformationPanel(String status, String size, String time){
        statusField.setText(status);
        sizeField.setText(size);
        timeField.setText(time);
    }

    public void addImageToPreviewPanel(){
//        previewPanel = new JPanel();
//        previewPanel.setLayout(new BorderLayout());

//        JPanel panel = new JPanel();
//
//        panel.setLayout(new BorderLayout());
//        imagePanel = new JPanel();
//        imagePanel.setLayout(new BorderLayout());
//        imagePanel.add(new LoadImageApp(), BorderLayout.CENTER);
//        imagePanel.setVisible(true);
////
////        previewPanel.setLayout(new BorderLayout());
//        previewPanel.add(imagePanel, BorderLayout.CENTER);


        previewPanel.add(new LoadImageApp(), BorderLayout.CENTER);

        responseBodyPanel.add(previewPanel, BorderLayout.CENTER);
        previewPanel.setVisible(false);


        radioButtonRaw.setSelected(true);
        rawPanel.setVisible(true);

//        tabbedPane.getComponentAt(0).repaint();

        updateUI();
    }

    private class LoadImageApp extends Component {

        BufferedImage img;

        public void paint(Graphics g) {
//            g.drawImage(img, 0, 0, null);
            g.drawImage(img, 0, 0, null);
        }

        public LoadImageApp() {
            try {
                img = ImageIO.read(new File("./image.png"));
            } catch (IOException e) {
            }

        }
    }

    public JPanel getResponseBodyPanel() {
        return responseBodyPanel;
    }

    public JPanel getResponseHeaderPanel() {
        return responseHeaderPanel;
    }

    public JPanel getHeadersListPanel() {
        return headersListPanel;
    }

    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    public JPanel getPreviewPanel() {
        return previewPanel;
    }

    public JRadioButton getRadioButtonRaw() {
        return radioButtonRaw;
    }

    public JRadioButton getRadioButtonPreview() {
        return radioButtonPreview;
    }
}
