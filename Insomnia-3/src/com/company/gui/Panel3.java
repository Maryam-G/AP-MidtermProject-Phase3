package com.company.gui;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
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
    private JButton copyButton;
    private JPanel headersListPanel;

    /**
     * constructor method
     */
    public Panel3(){
        this.setLayout(new BorderLayout());
        addInfoPanel();

        tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Calibri", 45, 18));

        // body :
        addResponseBodyPanel();
        //header:
        addResponseHeadersPanel();

        // add panels to tabs of tabbedPane
        tabbedPane.add("Body",responseBodyPanel);
        tabbedPane.add("Headers", responseHeaderPanel);

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
        responseHeaderPanel.setLayout(new BorderLayout());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());

        //button copy to clipboard :

        copyButton = new JButton("Copy to Clipboard");
        copyButton.setFont(new Font("Calibri", 45, 15));

        JPanel copyButtonPanel = new JPanel();
        copyButtonPanel.setLayout(new GridLayout(1, 1));
        copyButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        copyButtonPanel.add(copyButton);

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

    }

    /**
     * set panel for Preview body
     */
    public void setPreviewPanel(){
        radioButtonPreview = new JRadioButton("Preview");
        previewPanel = new JPanel();
        previewPanel.setLayout(new BorderLayout());
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
                previewPanel.setVisible(false);
                updateUI();
            } else if (radioButtonPreview.isSelected()) {
                previewPanel.setVisible(true);
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

    /**
     * set response headers panel
     * @param responseHeaders headers of response
     */
    public void setHeadersPanel(Map<String, List<String>> responseHeaders){

        headersListPanel.removeAll();
        headersListPanel.revalidate();
        headersListPanel.repaint();

        BoxLayout boxLayout = new BoxLayout(headersListPanel, BoxLayout.Y_AXIS);
        headersListPanel.setLayout(boxLayout);

        String key = "";
        List<String> value ;
        HeaderItemPanel headerItemPanel;
        for(Map.Entry<String, List<String>> entry : responseHeaders.entrySet()){
            if(entry.getKey() != null){
                key = entry.getKey();
                value = entry.getValue();
                headerItemPanel = new HeaderItemPanel(key, value);
                headersListPanel.add(headerItemPanel);
            }
        }

        headersListPanel.setVisible(true);
        responseHeaderPanel.add(headersListPanel, BorderLayout.CENTER);

        updateUI();
    }

    /**
     * set raw body panel
     * @param responseBody body of response
     */
    public void setRawBodyPanel(String responseBody){
        rawText.setText(responseBody);
    }

    /**
     * set preview body panel
     * @param isImage content type of response is image/png or not
     */
    public void setPreviewBodyPanel(boolean isImage){
        if(isImage){
            addImageToPreviewPanel();
        }else{
            radioButtonRaw.setSelected(true);
            previewPanel.removeAll();
            previewPanel.revalidate();
            previewPanel.repaint();

            updateUI();
        }
    }

    /**
     * set info panel (status , size , time)
     * @param status response status
     * @param size response size
     * @param time response time
     */
    public void setInformationPanel(String status, String size, String time){
        statusField.setText(status);
        sizeField.setText(size);
        timeField.setText(time);
    }

    /**
     * add image to preview panel
     */
    public void addImageToPreviewPanel(){

        previewPanel.add(new LoadImageApp(), BorderLayout.CENTER);

        responseBodyPanel.add(previewPanel, BorderLayout.CENTER);

        radioButtonRaw.setSelected(true);
        rawPanel.setVisible(true);
        previewPanel.setVisible(false);

        updateUI();
    }

    /**
     * An inner class for loading image
     */
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

    /**
     * get tabbed pane of response (headers and body)
     * @return tabbedPane field
     */
    public JTabbedPane getTabbedPane() {
        return tabbedPane;
    }

    /**
     * get preview panel
     * @return previewPanel field
     */
    public JPanel getPreviewPanel() {
        return previewPanel;
    }

    /**
     * get radio button for raw body
     * @return radioButtonRaw field
     */
    public JRadioButton getRadioButtonRaw() {
        return radioButtonRaw;
    }

    /**
     * get radio button for preview body
     * @return radioButtonPreview field
     */
    public JRadioButton getRadioButtonPreview() {
        return radioButtonPreview;
    }

    /**
     * get copy button
     * @return copyButton field
     */
    public JButton getCopyButton() {
        return copyButton;
    }
}
