package com.company.gui;

import com.company.jurl.Jurl;
import com.company.model.Request;
import com.company.utils.FileUtils;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

/**
 * A class for showing panel 1 with list of requests and collections
 *
 * @author Maryam Goli
 */
public class Panel1 extends JPanel {

    private JPanel panelForCollections;
    private JButton newCollection;
    private JTree treeOfCollections;
    private DefaultMutableTreeNode rootCollections;

    private String nameOfNewCollection;

    private JPanel filesPanel;
    private JButton saveButton;

    /**
     * constructor method
     */
    public Panel1() {
        this.setMinimumSize(new Dimension(300, 600));
        this.setLayout(new BorderLayout());

        filesPanel = new JPanel();
        filesPanel.setLayout(new BorderLayout());

        addInsomniaLabel();
        addPanelForCollections();

        saveButton = new JButton("Save");
        saveButton.setFont(new Font("Calibri", 45, 15));
        saveButton.setHorizontalAlignment(JButton.CENTER);
        saveButton.setBackground(new Color(255, 255, 255));
        saveButton.isOpaque();
        saveButton.addActionListener(new ButtonHandler());


        JPanel saveButtonPanel = new JPanel();
        saveButtonPanel.setLayout(new BorderLayout());
        saveButtonPanel.setBorder(new EmptyBorder(3, 3, 5, 0));
        saveButtonPanel.add(saveButton, BorderLayout.CENTER);

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(saveButtonPanel, BorderLayout.PAGE_START);
        panel.add(filesPanel, BorderLayout.CENTER);

        this.add(panel, BorderLayout.CENTER);
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
     * add panel for showing list of all collections
     */
    public void addPanelForCollections() {
        panelForCollections = new JPanel();
        panelForCollections.setLayout(new BorderLayout(1, 1));
        panelForCollections.setBorder(new EmptyBorder(3, 3, 3, 0));

        JLabel collectionsLabel = new JLabel();
        collectionsLabel.setText("            -- All Collections [For-Saved-Requests] --");
        collectionsLabel.setFont(new Font("Calibri", 45, 12));
        collectionsLabel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 30));


        newCollection = new JButton("New Collection");
        newCollection.setFont(new Font("Calibri", 45, 15));
        newCollection.addActionListener(new ButtonHandler());

        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        panel.add(newCollection, BorderLayout.NORTH);
        panel.add(collectionsLabel, BorderLayout.CENTER);

        panelForCollections.add(panel, BorderLayout.NORTH);

        addListOfCollections();

        filesPanel.add(panelForCollections, BorderLayout.CENTER);

    }

    /**
     * add list of collections
     */
    public void addListOfCollections(){

        treeOfCollections = new JTree();
        rootCollections = new DefaultMutableTreeNode("AllCollections");

        treeOfCollections.setModel(FileUtils.createListOfAllCollections(rootCollections));
        panelForCollections.add(new JScrollPane(treeOfCollections), BorderLayout.CENTER);

        DefaultTreeCellRenderer renderer = (DefaultTreeCellRenderer) (treeOfCollections.getCellRenderer());
        renderer.setFont(new Font("Calibri", 45, 16));
        treeOfCollections.setRowHeight(30);

    }

    /**
     * an inner class for handling events that related to creating new collection
     */
    private class ButtonHandler implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource().equals(newCollection)){
                buildFrameForNameOfFolder();
            }
        }

        public void buildFrameForNameOfFolder() {
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
                    nameOfNewCollection = nameField.getText().replaceAll(" ", "");
                    Jurl jurlApp = new Jurl(">jurl create " + nameOfNewCollection);

                    DefaultMutableTreeNode parentNode = rootCollections;

                    DefaultMutableTreeNode newFolder = new DefaultMutableTreeNode(nameOfNewCollection, true);
                    DefaultTreeModel model = (DefaultTreeModel) treeOfCollections.getModel();
                    model.insertNodeInto(newFolder, parentNode, parentNode.getChildCount());
                    treeOfCollections.scrollPathToVisible(new TreePath(newFolder.getPath()));

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

    /**
     * get tree of collections
     * @return treeOfCollections field
     */
    public JTree getTreeOfCollections() {
        return treeOfCollections;
    }

    /**
     * get save button
     * @return saveButton field
     */
    public JButton getSaveButton() {
        return saveButton;
    }

    /**
     * get root collections
     * @return rootCollections field
     */
    public DefaultMutableTreeNode getRootCollections() {
        return rootCollections;
    }
}