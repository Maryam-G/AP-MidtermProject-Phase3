package com.company.gui;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A class for showing panel 1 with list of requests and collections
 *
 * @author Maryam Goli
 */
public class Panel1 extends JPanel {

    private JPanel panelForCollections;

    private JButton newCollection;
    private JButton newRequest;
    private String nameOfNewRequest;
    private String nameOfNewCollection;

    private DefaultMutableTreeNode allCollections;
    private JTree treeOfCollections;

    /**
     * constructor method
     */
    public Panel1() {
        this.setMinimumSize(new Dimension(300, 600));
        this.setLayout(new BorderLayout());

        addInsomniaLabel();
        addPanelForCollections();
    }

    /**
     * add label "Insomnia" at the top of panel 1
     */
    public void addInsomniaLabel() {
        JPanel panelForInsomniaLabel = new JPanel();
        panelForInsomniaLabel.setLayout(new BorderLayout());
        panelForInsomniaLabel.setBackground(new Color(148, 40, 255));
        panelForInsomniaLabel.setPreferredSize(new Dimension(panelForInsomniaLabel.getWidth(), 50));

        JLabel insomniaLabel = new JLabel();
        insomniaLabel.setText("  Insomnia");
        insomniaLabel.setFont(new Font("Calibri", 45, 22));
        insomniaLabel.setForeground(Color.WHITE);
        insomniaLabel.setHorizontalAlignment(JLabel.LEFT);
        insomniaLabel.setVerticalAlignment(JLabel.CENTER);

        panelForInsomniaLabel.add(insomniaLabel);
        this.add(panelForInsomniaLabel, BorderLayout.PAGE_START);
    }

    /**
     * add buttons for create new collection and new request
     */
    public void addButtons() {
        newCollection = new JButton("New Collection");
        newCollection.setFont(new Font("Calibri", 45, 15));
        newCollection.addActionListener(new ButtonHandler());

        newRequest = new JButton("New Request");
        newRequest.setFont(new Font("Calibri", 45, 15));
        newRequest.addActionListener(new ButtonHandler());

        JPanel panelForButtons = new JPanel();
        panelForButtons.setBorder(new EmptyBorder(1, 0, 2, 0));
        panelForButtons.setLayout(new GridLayout(1, 2, 2, 2));

        panelForButtons.add(newCollection);
        panelForButtons.add(newRequest);

        panelForCollections.add(panelForButtons, BorderLayout.NORTH);
    }

    /**
     * add list of all collections
     */
    public void addListOfCollections() {
        allCollections = new DefaultMutableTreeNode("All Collections", true);
        DefaultMutableTreeNode firstRequest = new DefaultMutableTreeNode("FirstRequest");
        allCollections.add(firstRequest);

        treeOfCollections = new JTree(allCollections);
        treeOfCollections.scrollPathToVisible(new TreePath(allCollections.getPath()));

        final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) (treeOfCollections.getCellRenderer());
        renderer.setFont(new Font("Calibri", 45, 19));
        treeOfCollections.setRowHeight(30);

        allCollections.breadthFirstEnumeration();

        panelForCollections.add(new JScrollPane(treeOfCollections), BorderLayout.CENTER);
    }

    /**
     * add panel for list of all collections to panel 1
     */
    public void addPanelForCollections() {
        panelForCollections = new JPanel();
        panelForCollections.setLayout(new BorderLayout(1, 1));
        panelForCollections.setBorder(new EmptyBorder(3, 3, 3, 0));

        addButtons();
        addListOfCollections();

        this.add(panelForCollections, BorderLayout.CENTER);
    }

    /**
     * An inner class for handling events that related to buttons
     */
    private class ButtonHandler implements ActionListener {
        @Override
        public void actionPerformed(ActionEvent e) {

            DefaultMutableTreeNode parentNode;
            TreePath parentPath = treeOfCollections.getSelectionPath();

            //button : New Collection
            if (e.getSource().equals(newCollection)) {
                if (parentPath == null) {
                    //There is no selection. Default to the root node (all Collections).
                    parentNode = allCollections;
                    buildFrameForNameOfFolder(parentNode);
                } else {
                    parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                    if(parentNode.equals(allCollections)){
                        buildFrameForNameOfFolder(parentNode);
                    } else {
                        buildFrameForNameOfFolder(allCollections);
                    }
                }
            }
            //button : New Request
            else if (e.getSource().equals(newRequest)) {
                if (parentPath == null) {
                    //There is no selection.
                    //TODO
                    JOptionPane.showMessageDialog(null, "You can not create new Request! \n Please select or create one collection...", "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    parentNode = (DefaultMutableTreeNode) (parentPath.getLastPathComponent());
                    if (parentNode.equals(allCollections)){
                        JOptionPane.showMessageDialog(null, "You can not create new Request! \n Please select or create one collection...", "Error", JOptionPane.ERROR_MESSAGE);
                    }else if (parentNode.getChildCount() == 0) {
                        JOptionPane.showMessageDialog(null, "You can not create new Request! \n Please select or create one collection...", "Error", JOptionPane.ERROR_MESSAGE);
                    } else {
                        buildFrameForNameOfRequest(parentNode);
                    }
                }
            }
        }

        public void buildFrameForNameOfFolder(DefaultMutableTreeNode parentNode) {
            JFrame setNameFrame = new JFrame("New Collection");
            setNameFrame.setSize(new Dimension(500, 200));
            setNameFrame.setLocation(900, 400);
            setNameFrame.setLayout(new BorderLayout());
            setNameFrame.setResizable(false);

            JPanel namePanel = new JPanel();
            namePanel.setLayout(new BorderLayout());
            namePanel.setBorder(new EmptyBorder(30, 10, 30, 10));

            JLabel nameLabel = new JLabel("Name");
            nameLabel.setFont(new Font("Calibri", 14, 19));
            nameLabel.setBorder(new EmptyBorder(0, 5, 10, 10));

            JTextField nameField = new JTextField();
            nameField.setText("MyCollection");

            JButton createButton = new JButton("Create");

            namePanel.add(nameLabel, BorderLayout.NORTH);
            namePanel.add(nameField, BorderLayout.CENTER);

            setNameFrame.add(namePanel, BorderLayout.CENTER);
            setNameFrame.add(createButton, BorderLayout.SOUTH);

            setNameFrame.setVisible(true);

            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nameOfNewCollection = nameField.getText();
                    DefaultMutableTreeNode newFolder = new DefaultMutableTreeNode(nameOfNewCollection, true);
                    DefaultMutableTreeNode firstRequest = new DefaultMutableTreeNode("FirstRequest");
                    newFolder.add(firstRequest);

                    DefaultTreeModel model = (DefaultTreeModel) treeOfCollections.getModel();
                    model.insertNodeInto(newFolder, parentNode, parentNode.getChildCount());
                    treeOfCollections.scrollPathToVisible(new TreePath(newFolder.getPath()));

                    setNameFrame.setVisible(false);
                }
            });
        }


        public void buildFrameForNameOfRequest(DefaultMutableTreeNode parentNode) {
            JFrame setNameFrame = new JFrame("New Request");
            setNameFrame.setSize(new Dimension(500, 230));
            setNameFrame.setLocation(900, 400);
            setNameFrame.setLayout(new BorderLayout());
            setNameFrame.setResizable(false);

            JPanel newRequestPanel = new JPanel();
            newRequestPanel.setLayout(new BorderLayout());
            newRequestPanel.setBorder(new EmptyBorder(20, 10, 10, 10));

            // create panel and add to frame
            JPanel namePanel = new JPanel();
            namePanel.setLayout(new BorderLayout());
            namePanel.setBorder(new EmptyBorder(30, 10, 30, 10));

            JLabel nameLabel = new JLabel("Name");
            nameLabel.setFont(new Font("Calibri", 14, 19));
            nameLabel.setBorder(new EmptyBorder(0, 5, 10, 10));

            JTextField nameField = new JTextField();
            nameField.setText("MyRequest");

            JButton createButton = new JButton("Create");

            namePanel.add(nameLabel, BorderLayout.NORTH);
            namePanel.add(nameField, BorderLayout.CENTER);

            newRequestPanel.add(namePanel, BorderLayout.CENTER);
            newRequestPanel.add(createButton, BorderLayout.SOUTH);

            setNameFrame.add(newRequestPanel, BorderLayout.CENTER);
            setNameFrame.setVisible(true);

            createButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    nameOfNewRequest = nameField.getText();
                    DefaultMutableTreeNode newRequest = new DefaultMutableTreeNode(nameOfNewRequest);

                    DefaultTreeModel model = (DefaultTreeModel) treeOfCollections.getModel();
                    model.insertNodeInto(newRequest, parentNode, parentNode.getChildCount());
                    treeOfCollections.scrollPathToVisible(new TreePath(newRequest.getPath()));

                    setNameFrame.setVisible(false);
                }
            });
        }
    }

    /**
     * set theme for panel 1
     * @param newColor new color
     */
    public void setThemeForPanel1(Color newColor){
        treeOfCollections.setBackground(newColor);
        final DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) (treeOfCollections.getCellRenderer());
        if (newColor.equals(Color.DARK_GRAY)) {
            renderer.setTextNonSelectionColor(Color.WHITE);
            renderer.setTextSelectionColor(new Color(197, 138, 255));
        } else {
            renderer.setTextNonSelectionColor(Color.BLACK);
            renderer.setTextSelectionColor(new Color(197, 138, 255));
        }
    }
}