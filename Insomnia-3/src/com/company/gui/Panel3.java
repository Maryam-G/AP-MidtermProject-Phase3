package com.company.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
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

    private JRadioButton radioButtonRaw;
    private JPanel rawPanel;
    private JTextArea rawText;

    private JRadioButton radioButtonPreview;
    private JPanel previewPanel;

    //header panel :
    private JPanel responseHeaderPanel;
    private ArrayList<HeaderItemPanel> headersList;
    private JButton copyButton;

    /**
     * constructor method
     */
    public Panel3(){
        this.setLayout(new BorderLayout());
        addInfoPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Calibri", 45, 18));

        // body :
        addResponseBodyPanel();
        //header:
        addResponseHeadersPanel();

        // add panels to tabs of tabbedPane
        tabbedPane.add("Body", new JScrollPane(responseBodyPanel));
        tabbedPane.add("Header", new JScrollPane(responseHeaderPanel));

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
        BoxLayout boxLayout = new BoxLayout(responseHeaderPanel, BoxLayout.Y_AXIS);
        responseHeaderPanel.setLayout(boxLayout);

        headersList = new ArrayList<>();

        //button copy to clipboard :

        copyButton = new JButton("Copy to Clipboard");
        copyButton.setFont(new Font("Calibri", 45, 15));
        copyButton.addActionListener(new CopyButtonHandler());

        JPanel copyButtonPanel = new JPanel();
        copyButtonPanel.setLayout(new GridLayout(1, 1));
        copyButtonPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        copyButtonPanel.add(copyButton);

        responseHeaderPanel.add(copyButtonPanel);

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

        responseHeaderPanel.add(keyValuePanel);

    }

    /**
     * add panel for showing response body [two tabs : Raw & Preview]
     */
    public void addResponseBodyPanel(){
        setRawPanel();
        setPreviewPanel();

        radioButtonRaw.addActionListener(new RadioButtonHandler());
        radioButtonPreview.addActionListener(new RadioButtonHandler());

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonRaw);
        buttonGroup.add(radioButtonPreview);
        radioButtonRaw.setSelected(true);

        JPanel buttonGroupPanel = new JPanel();
        buttonGroupPanel.setLayout(new FlowLayout());
        buttonGroupPanel.add(radioButtonRaw);
        buttonGroupPanel.add(radioButtonPreview);

        responseBodyPanel = new JPanel();
        responseBodyPanel.setLayout(new BorderLayout());
        responseBodyPanel.add(buttonGroupPanel, BorderLayout.NORTH);

        responseBodyPanel.add(rawPanel, BorderLayout.CENTER);
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

        rawPanel.add(new JScrollPane(rawText), BorderLayout.CENTER);
    }

    /**
     * set panel for Preview body
     */
    public void setPreviewPanel(){
        radioButtonPreview = new JRadioButton("Preview");
        previewPanel = new JPanel();
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
                responseBodyPanel.add(rawPanel, BorderLayout.CENTER);
                previewPanel.setVisible(false);
                Panel3.this.updateUI();
            } else if (radioButtonPreview.isSelected()) {
                previewPanel.setVisible(true);
                responseBodyPanel.add(previewPanel, BorderLayout.CENTER);
                rawPanel.setVisible(false);
                Panel3.this.updateUI();
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

            responseHeaderPanel.add(keyValuePanel);
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
        String key ;
        List<String> value ;
        for(Map.Entry<String, List<String>> entry : responseHeaders.entrySet()){
            if(entry.getKey() != null){
                key = entry.getKey();
                value = entry.getValue();
                HeaderItemPanel headerItemPanel = new HeaderItemPanel(key, value);
                responseBodyPanel.add(headerItemPanel);
            }
        }
    }

    public void setRawBodyPanel(String responseBody){
        rawText.setText(responseBody);
    }

    public void setInformationPanel(String status, String size, String time){
        statusField.setText(status);
        //todo
        sizeField.setText(size);
        //todo
        timeField.setText(time);
    }

}
