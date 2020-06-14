package com.company.gui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.util.ArrayList;
import java.util.Iterator;

/**
 * A class for showing panel 2 and selected request with its headers and body
 *
 * @author Maryam Goli
 */
public class Panel2 extends JPanel {

    // URL panel :
    private JButton sendButton;
    private JComboBox comboBoxForMethod;
    private JTextField urlAddress;

    // body panel :
    private JPanel requestBodyPanel;

    private JPanel noBodyPanel;
    private JRadioButton radioButtonNoBody;

    private JPanel formDataPanel;
    private JRadioButton radioButtonFormData;
    private ArrayList<BodyItemPanel> bodyList;

    // headers panel :
    private JPanel requestHeaderPanel;
    private ArrayList<HeaderItemPanel> headersList;

    /**
     * constructor method
     */
    public Panel2(){
        this.setLayout(new BorderLayout());

        // URL :
        addUrlPanel();

        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setFont(new Font("Calibri", 45, 18));

        // body :
        addBodyPanel();
        //header:
        addHeadersPanel();

        // add panels to tabs of tabbedPane
        tabbedPane.add("Body", new JScrollPane(requestBodyPanel));
        tabbedPane.add("Headers", new JScrollPane(requestHeaderPanel));

        this.add(tabbedPane, BorderLayout.CENTER);

    }

    /**
     * add panel for write url address
     */
    public void addUrlPanel(){
        JPanel urlPanel = new JPanel();
        urlPanel.setLayout(new BorderLayout());
        urlPanel.setPreferredSize(new Dimension(urlPanel.getWidth(), 50));

        comboBoxForMethod = new JComboBox();
        comboBoxForMethod.setFont(new Font("Calibri", 45, 15));
        comboBoxForMethod.addItem("GET");
        comboBoxForMethod.addItem("POST");
        comboBoxForMethod.addItem("PUT");
        comboBoxForMethod.addItem("DELETE");

        urlAddress = new JTextField();
        urlAddress.setText("Enter URL ...");
        urlAddress.setFont(new Font("Calibri", 45, 15));

        sendButton = new JButton("Send");
        sendButton.setFont(new Font("Calibri", 45, 15));
        sendButton.setHorizontalAlignment(JButton.CENTER);

        urlPanel.add(comboBoxForMethod, BorderLayout.WEST);
        urlPanel.add(urlAddress, BorderLayout.CENTER);
        urlPanel.add(sendButton, BorderLayout.EAST);
        urlPanel.setVisible(true);

        this.add(urlPanel, BorderLayout.PAGE_START);
    }

    /**
     * add panel for showing request headers with key and value
     */
    public void addHeadersPanel(){
        requestHeaderPanel = new JPanel();

        HeaderItemPanel firstHeader = new HeaderItemPanel();

        BoxLayout boxLayout = new BoxLayout(requestHeaderPanel, BoxLayout.Y_AXIS);
        requestHeaderPanel.setLayout(boxLayout);
        requestHeaderPanel.add(firstHeader);

        headersList = new ArrayList<>();
        headersList.add(firstHeader);

        firstHeader.getKeyField().addFocusListener(new FocusHandler());
        firstHeader.getValueField().addFocusListener(new FocusHandler());
    }

    /**
     * add panel for showing request body [two tabs : No-Body & Form-Data]
     */
    public void addBodyPanel(){
        requestBodyPanel = new JPanel();

        // No-Body :
        noBodyPanel = new JPanel();
        radioButtonNoBody = new JRadioButton("No-Body");
        // Form-Data :
        setFormDataPanel();

        radioButtonNoBody.setSelected(true);

        radioButtonNoBody.addActionListener(new RadioButtonHandler());
        radioButtonFormData.addActionListener(new RadioButtonHandler());

        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.add(radioButtonNoBody);
        buttonGroup.add(radioButtonFormData);

        JPanel buttonGroupPanel = new JPanel();
        buttonGroupPanel.setLayout(new FlowLayout());
        buttonGroupPanel.add(radioButtonNoBody);
        buttonGroupPanel.add(radioButtonFormData);

        requestBodyPanel.setLayout(new BorderLayout());
        requestBodyPanel.add(new JScrollPane(buttonGroupPanel), BorderLayout.NORTH);
    }

    /**
     * set panel for showing Multipart/Form-Data body
     */
    public void setFormDataPanel() {

        BodyItemPanel firstFormDataItem = new BodyItemPanel();

        formDataPanel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(formDataPanel, BoxLayout.Y_AXIS);
        formDataPanel.setLayout(boxLayout);
        formDataPanel.add(firstFormDataItem);

        bodyList = new ArrayList<>();
        bodyList.add(firstFormDataItem);

        firstFormDataItem.getKeyField().addFocusListener(new FocusHandler());
        firstFormDataItem.getValueField().addFocusListener(new FocusHandler());

        radioButtonFormData = new JRadioButton("Form-Data");
    }

    /**
     * An inner class for handling events that related to focusing on header items or body items
     */
    private class FocusHandler implements FocusListener {

        @Override
        public void focusGained(FocusEvent e) {
            if(e.getSource().equals(requestHeaderPanel)){
                System.out.println("l");
            }

            HeaderItemPanel lastHeaderItem = new HeaderItemPanel();
            lastHeaderItem = headersList.get(headersList.size()-1);
            BodyItemPanel lastFormDataItemPanel = new BodyItemPanel();
            lastFormDataItemPanel = bodyList.get(bodyList.size()-1);

            // focus on request headers panel -> Headers
            if (e.getSource().equals(lastHeaderItem.getKeyField()) || e.getSource().equals(lastHeaderItem.getValueField())) {
                HeaderItemPanel newHeaderItem = new HeaderItemPanel();
                newHeaderItem.getKeyField().addFocusListener(this);
                newHeaderItem.getValueField().addFocusListener(this);
                newHeaderItem.setVisible(true);

                requestHeaderPanel.add(newHeaderItem);
                headersList.add(newHeaderItem);

                Panel2.this.updateUI();
            }

            //focus on request body panel -> Form-Data
            else if (e.getSource().equals(lastFormDataItemPanel.getKeyField()) || e.getSource().equals(lastFormDataItemPanel.getValueField())) {
                BodyItemPanel newFormDataItem = new BodyItemPanel();
                newFormDataItem.getKeyField().addFocusListener(this);
                newFormDataItem.getValueField().addFocusListener(this);
                newFormDataItem.setVisible(true);

                formDataPanel.add(newFormDataItem);
                bodyList.add(newFormDataItem);

                Panel2.this.updateUI();
            }

        }

        @Override
        public void focusLost(FocusEvent e) {
        }
    }

    /**
     * An inner class for handling events that related to tabs of body panel
     */
    private class RadioButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {
            if (radioButtonNoBody.isSelected()) {
                noBodyPanel.setVisible(true);
                requestBodyPanel.add(noBodyPanel, BorderLayout.CENTER);
                formDataPanel.setVisible(false);
                Panel2.this.updateUI();
            } else if (radioButtonFormData.isSelected()) {
                formDataPanel.setVisible(true);
                requestBodyPanel.add(formDataPanel, BorderLayout.CENTER);
                noBodyPanel.setVisible(false);
                Panel2.this.updateUI();
            }
        }
    }

    /**
     * a panel for header item [key and value]
     */
    private class HeaderItemPanel extends JPanel{

        private JTextField keyField;
        private JTextField valueField;
        private JButton deleteButton;
        private JCheckBox checkBox;

        public HeaderItemPanel(){
            keyField = new JTextField("-[key]-");
            valueField = new JTextField("-[value]-");
            deleteButton = new JButton("\u2716");
            checkBox = new JCheckBox();

            setPanel();
        }

        public void setPanel(){

            keyField.setFont(new Font("Calibri", 45, 15));
            valueField.setFont(new Font("Calibri", 45, 15));

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource().equals(deleteButton)){
                        if(headersList.size() > 1){
                            HeaderItemPanel.this.setVisible(false);
                            Iterator<HeaderItemPanel> iterator = headersList.iterator();
                            while (iterator.hasNext()){
                                if(iterator.next().equals(HeaderItemPanel.this)){
                                    iterator.remove();
                                    break;
                                }
                            }

                            headersList.get(headersList.size()-1).getKeyField().addFocusListener(new FocusHandler());
                            headersList.get(headersList.size()-1).getValueField().addFocusListener(new FocusHandler());

                        }
                    }
                }
            });

            JPanel keyValuePanel = new JPanel();
            keyValuePanel.setLayout(new GridLayout());
            keyValuePanel.add(keyField);
            keyValuePanel.add(valueField);

            this.setLayout(new BorderLayout());
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            this.add(checkBox, BorderLayout.WEST);
            this.add(keyValuePanel, BorderLayout.CENTER);
            this.add(deleteButton, BorderLayout.EAST);

            this.setVisible(true);
        }

        public JTextField getKeyField() {
            return keyField;
        }

        public JTextField getValueField() {
            return valueField;
        }
    }

    /**
     * a panel for body item [key and value]
     */
    private class BodyItemPanel extends JPanel{

        private JTextField keyField;
        private JTextField valueField;
        private JButton deleteButton;
        private JCheckBox checkBox;

        public BodyItemPanel(){
            keyField = new JTextField("-[key]-");
            valueField = new JTextField("-[value]-");
            deleteButton = new JButton("\u2716");
            checkBox = new JCheckBox();

            setPanel();
        }

        public void setPanel(){

            keyField.setFont(new Font("Calibri", 45, 15));
            valueField.setFont(new Font("Calibri", 45, 15));

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(e.getSource().equals(deleteButton)){
                        if(bodyList.size() > 1){
                            BodyItemPanel.this.setVisible(false);
                            Iterator<BodyItemPanel> iterator = bodyList.iterator();
                            while (iterator.hasNext()){
                                if(iterator.next().equals(BodyItemPanel.this)){
                                    iterator.remove();
                                    break;
                                }
                            }

                            bodyList.get(bodyList.size()-1).getKeyField().addFocusListener(new FocusHandler());
                            bodyList.get(bodyList.size()-1).getValueField().addFocusListener(new FocusHandler());

                        }
                    }
                }
            });

            JPanel keyValuePanel = new JPanel();
            keyValuePanel.setLayout(new GridLayout());
            keyValuePanel.add(keyField);
            keyValuePanel.add(valueField);

            this.setLayout(new BorderLayout());
            this.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));

            this.add(checkBox, BorderLayout.WEST);
            this.add(keyValuePanel, BorderLayout.CENTER);
            this.add(deleteButton, BorderLayout.EAST);

            this.setVisible(true);
        }

        public JTextField getKeyField() {
            return keyField;
        }

        public JTextField getValueField() {
            return valueField;
        }
    }

    /**
     * set theme for panel 2
     * @param newColor new color for theme
     */
    public void setThemeForPanel2(Color newColor){
        requestBodyPanel.setBackground(newColor);
        noBodyPanel.setBackground(newColor);
        formDataPanel.setBackground(newColor);

        requestHeaderPanel.setBackground(newColor);
    }

    public JButton getSendButton() {
        return sendButton;
    }

    public JComboBox getComboBoxForMethod() {
        return comboBoxForMethod;
    }

    public JTextField getUrlAddress() {
        return urlAddress;
    }
}
