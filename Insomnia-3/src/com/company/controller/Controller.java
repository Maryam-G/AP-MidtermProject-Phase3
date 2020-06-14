package com.company.controller;

import com.company.jurl.HttpClient;
import com.company.jurl.Jurl;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public Controller(){
        requestHeaders = new HashMap<>();
        requestHeadersString = "";
        requestBody = new HashMap<>();
        requestBodyString = "";

        responseBody = "";
    }

    public void setUrlAddress(String urlAddress){
        if(urlAddress.equals("Enter URL ...")){
            this.urlAddress = "";
        }else{
            this.urlAddress = urlAddress;
        }
    }

    public void setMethod(String method) {
        this.method = method;
    }

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

    public void createJurl(){
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

        // establish connection :
        Jurl jurl = new Jurl(jurlInput);

        // get response :
        responseHeaders = jurl.getResponseHeaders();
        responseBody = jurl.getResponseBody();
        status = jurl.getStatus();
        size = jurl.getSize();
        time = jurl.getTime();

    }

    //todo : pak kardan e ezafiat
    public String getMethod() {
        return method;
    }

    public String getUrlAddress() {
        return urlAddress;
    }

    public HashMap<String, String> getRequestBody() {
        return requestBody;
    }

    public HashMap<String, String> getRequestHeaders() {
        return requestHeaders;
    }

    public String getRequestBodyString() {
        return requestBodyString;
    }

    public String getRequestHeadersString() {
        return requestHeadersString;
    }

    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public String getStatus() {
        return status;
    }

    public String getSize() {
        return size;
    }

    public String getTime() {
        return time;
    }

}
