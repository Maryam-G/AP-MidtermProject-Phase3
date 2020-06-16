package com.company.utils;

import com.company.model.Request;

import javax.swing.*;
import javax.swing.filechooser.FileView;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

/**
 * A class for managing files and directories
 *
 * @author Maryam Goli
 */
public class FileUtils {

    // create directories ...
    static {
        boolean directoryCreated0 = new File("./Requests/").mkdirs();
        boolean directoryCreated1 = new File("./Requests/AllCollections/").mkdirs();
        boolean directoryCreated2 = new File("./Requests/History/").mkdirs();
        boolean directoryCreated3 = new File("./Responses/").mkdirs();
    }

    /**
     * find a directory in directory "AllCollections"
     * @param directoryName input name for directory
     * @return true or false
     */
    public static boolean findDirectoryInRequestDirectories(String directoryName){
        File directory = new File("./Requests/AllCollections");
        File[] fList = directory.listFiles();
        for(File currentFile : fList){
            if(currentFile.isDirectory()){
                if(currentFile.getName().equals(directoryName)){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * write object with type "Request" in a file in selected directory
     * @param newRequest object with type "Request"
     * @param collectionName name of selected directory
     */
    public static void writeRequestInFile(boolean saveInHistory, Request newRequest, String collectionName, String requestName){
        File file;
        if(saveInHistory){
            file = new File("./Requests/History/Request-" + (listOfAllRequestsInDirectory("History").size() + 1) + ".txt");
        }else{
            file = new File("./Requests/AllCollections/" + collectionName + "/" + requestName + ".txt");
        }
        try (ObjectOutputStream objectOutput = new ObjectOutputStream(new FileOutputStream(file))){
            objectOutput.writeObject(newRequest);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * read object "Request" from file
     * @param file file
     * @return object with type "Request"
     */
    public static Request readRequestFromFile(File file){
        try (ObjectInputStream objectInput = new ObjectInputStream(new FileInputStream(file))){
            Request request = (Request) objectInput.readObject();
            return request;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * return array list of Requests in selected directory
     * @param directoryName name of selected directory
     * @return ArrayList of requests
     */
    public static ArrayList<Request> listOfAllRequestsInDirectory(String directoryName){
        ArrayList<Request> requests = new ArrayList<>();
        File directory = new File("./Requests/" + directoryName + "/");
        File[] fList = directory.listFiles();
        Request currentRequest;
        for(File currentFile : fList){
            if(currentFile.isFile()){
                currentRequest = readRequestFromFile(currentFile);
                requests.add(currentRequest);
            }
        }
        return requests;
    }

    /**
     * return array list of all collections name
     * @return ArrayList of name of all collections
     */
    public static ArrayList<String> listOfAllCollections(){
        ArrayList<String> nameOfAllCollections = new ArrayList<>();
        File mainDirectory = new File("./Requests/AllCollections");
        File[] fList = mainDirectory.listFiles();
        for(File currentFile : fList){
            if(currentFile.isDirectory()){
                nameOfAllCollections.add(currentFile.getName());
            }
        }
        return nameOfAllCollections;
    }

    /**
     * create new collection
     * @param collectionName name of new collection
     */
    public static void createNewCollection(String collectionName){
        boolean directoryCreated = new File("./Requests/AllCollections/" + collectionName + "/").mkdirs();
        System.out.println("\u2192 New directory created : " + directoryCreated);
    }

    /**
     * save response body in file
     * @param responseBodyString response body
     * @param hasName this file has name or not
     * @param fileName name of file (if hasName == true)
     */
    public static void saveResponseBodyInFile(String responseBodyString, boolean hasName, String fileName){
        File file;
        if(hasName){
            file = new File("./Responses/" + fileName );
        }else{
            String currentDate = new SimpleDateFormat("[yyyy-MM-dd...HH;mm;ss]").format(new Date());
            file = new File("./Responses/output_" + currentDate + ".txt");
        }

        // write in file
        try (BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file))){
            bufferedWriter.write(responseBodyString);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // -> phase 3:

    public static DefaultTreeModel createListOfAllCollections(DefaultMutableTreeNode root){
        File fileRoot = new File("./Requests/AllCollections/");

        DefaultTreeModel model = new DefaultTreeModel(root);

        File[] subItems = fileRoot.listFiles();
        for (File file : subItems) {
            DefaultMutableTreeNode newDirectory = new DefaultMutableTreeNode(file.getName());
            root.add(newDirectory);

            File[] filesInNewDirectory = (new File("./Requests/AllCollections/" + file.getName())).listFiles();
            for(File currentFile : filesInNewDirectory){
                DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(currentFile.getName());
                newDirectory.add(newFile);
            }

        }

        return model;
    }

    public static DefaultTreeModel createListOfAllRequests(DefaultMutableTreeNode root){
        File fileRoot = new File("./Requests/History/");

        DefaultTreeModel model = new DefaultTreeModel(root);

        File[] subItems = fileRoot.listFiles();
        for (File file : subItems) {
            DefaultMutableTreeNode newFile = new DefaultMutableTreeNode(file.getName());
            root.add(newFile);
        }

        return model;
    }


}
