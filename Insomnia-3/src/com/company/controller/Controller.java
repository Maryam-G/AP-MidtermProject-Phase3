package com.company.controller;

import com.company.jurl.Jurl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for creating an appropriate input for jurl app from information of GUI
 *
 * @author Maryam Goli
 */
public class Controller {

    private String jurlInput;

    private String urlAddress;
    private String method;
    private HashMap<String, String> requestHeaders;
    private String requestHeadersString;
    private HashMap<String, String> requestBody;
    private String requestBodyString;

    private Map<String, List<String>> responseHeaders;
    private String responseBody;
    private String status;
    private String size;
    private String time;

    /**
     * constructor method
     */
    public Controller(){
        requestHeaders = new HashMap<>();
        requestHeadersString = "";
        requestBody = new HashMap<>();
        requestBodyString = "";

        responseBody = "";
    }

    /**
     * set url address of request
     * @param urlAddress url address
     */
    public void setUrlAddress(String urlAddress){
        if(urlAddress.equals("Enter URL ...")){
            this.urlAddress = "";
        }else{
            this.urlAddress = urlAddress;
        }
    }

    /**
     * set method of request
     * @param method request method
     */
    public void setMethod(String method) {
        this.method = method;
    }

    /**
     * set headers of request
     * @param requestHeaders headers of request
     */
    public void setRequestHeaders(HashMap<String, String> requestHeaders) {
        this.requestHeaders = requestHeaders;
        ArrayList<String> headerItems = new ArrayList<>();
        String keyValue;

        for(Map.Entry<String, String> entry : requestHeaders.entrySet()){
            keyValue = entry.getKey() + ":" + entry.getValue();
            headerItems.add(keyValue);
        }

        int count = 1;
        requestHeadersString += "\"";
        for(String currentHeader : headerItems){
            requestHeadersString += currentHeader;
            if(count < headerItems.size()){
                requestHeadersString += ";" ;
            }
            count++;
        }
        requestHeadersString += "\"";
    }

    /**
     * set body of request
     * @param requestBody body of request
     */
    public void setRequestBody(HashMap<String, String> requestBody) {
        this.requestBody = requestBody;
        ArrayList<String> bodyItems = new ArrayList<>();
        String keyValue;

        for(Map.Entry<String, String> entry : requestBody.entrySet()){
            keyValue = entry.getKey() + "=" + entry.getValue();
            bodyItems.add(keyValue);
        }

        int count = 1;
        requestBodyString += "\"";
        for(String currentHeader : bodyItems){
            requestBodyString += currentHeader;
            if(count < bodyItems.size()){
                requestBodyString += "&" ;
            }
            count++;
        }
        requestBodyString += "\"";
    }

    /**
     * create input for jurl
     * @param hasSaveArgument has --save or -S argument
     * @param collectionName name of selected collection for saving request
     * @param requestName name of request
     */
    public void createJurl(boolean hasSaveArgument, String collectionName, String requestName){
        // set request :
        jurlInput = ">jurl ";
        jurlInput += "--url " + urlAddress;
        jurlInput += " --method " + method;
        if(requestHeaders.size() > 0){
            jurlInput += " --headers " + requestHeadersString;
        }
        if(requestBody.size() > 0){
            jurlInput += " --data " + requestBodyString;
        }
        if(hasSaveArgument){
            jurlInput += " --save " + collectionName + " " + requestName;
        }

        // establish connection :
        Jurl jurl = new Jurl(jurlInput);

        // get response :
        responseHeaders = jurl.getResponseHeaders();
        responseBody = jurl.getResponseBody();
        status = jurl.getStatus();
        size = jurl.getSize();
        time = jurl.getTime();

    }

    /**
     * get response headers
     * @return responseHeaders field
     */
    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * get response body
     * @return responseBody field
     */
    public String getResponseBody() {
        return responseBody;
    }

    /**
     * get status of response
     * @return status field
     */
    public String getStatus() {
        return status;
    }

    /**
     * get size of response body
     * @return size field
     */
    public String getSize() {
        return size;
    }

    /**
     * get response time
     * @return time field
     */
    public String getTime() {
        return time;
    }

    /**
     * get url address of request
     * @return urlAddress field
     */
    public String getUrlAddress() {
        return urlAddress;
    }

    /**
     * get request method
     * @return method field
     */
    public String getMethod() {
        return method;
    }

    /**
     * get headers of request
     * @return requestHeaders field
     */
    public HashMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    /**
     * get body of request
     * @return requestBody field
     */
    public HashMap<String, String> getRequestBody() {
        return requestBody;
    }
}
