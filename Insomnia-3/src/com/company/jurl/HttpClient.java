package com.company.jurl;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A class for establish a connection between client and server
 *
 * @author Maryam Goli
 */
public class HttpClient {

    private String status;
    private String size;
    private String time;

    private HttpURLConnection connection;
    private Map<String, List<String>> responseHeaders;
    private String responseBody;

    private static boolean isImage;

    /**
     * constructor method
     * create URL and establish connection
     *
     * @param urlString url-address
     * @param method method of request
     * @param header headers of request
     * @param body body of request
     */
    public HttpClient(String urlString, String method, HashMap<String, String> header,HashMap<String, String> body) {
        isImage = false;

        try {
            if(!urlString.startsWith("http://")){
                urlString = "http://" + urlString;
            }
            URL url = new URL(urlString);
            connection = (HttpURLConnection) url.openConnection();

            long start = System.currentTimeMillis();

            // request setup

            // set method :
            connection.setRequestMethod(method);

            // set headers of request :
            for(Map.Entry<String, String> entry : header.entrySet()){
                connection.setRequestProperty(entry.getKey(), entry.getValue());
            }

            // set body of request for "POST" :
            if(method.equals("POST") || method.equals("PUT") || method.equals("DELETE")){
                String boundary = System.currentTimeMillis() + "";
                connection.setDoOutput(true);
                connection.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);
                BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(connection.getOutputStream());
                formData(body, boundary, bufferedOutputStream);
            }

            // response body :
            BufferedReader reader;
            if(connection.getErrorStream() != null){
                reader = new BufferedReader(new InputStreamReader(connection.getErrorStream()));
            }else{
                reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            }
            String line;
            StringBuffer responseContent = new StringBuffer();
            while((line = reader.readLine()) != null){
                responseContent.append("\n" + line);
            }
            reader.close();
            responseBody = responseContent.toString();

            //response headers :
            responseHeaders = connection.getHeaderFields();

            //image preview body:
            boolean flag = true;
            for(Map.Entry<String, List<String>> entry : responseHeaders.entrySet()) {
                if(entry.getKey() != null){
                    if (entry.getKey().equals("Content-Type")) {
                        if (entry.getValue().contains("image/png")) {
                            flag = false;
                            isImage = true;
                            saveImagePreviewResponse(url);
                            break;
                        }
                    }
                }
            }
            if(flag){
                isImage = false;
            }

            // time of response :
            long finish = System.currentTimeMillis();
            long totalTime = (finish - start) ;
            time = String.valueOf(Math.round(totalTime * 100.0) / 100000.0);

            // status of response :
            for(Map.Entry<String, List<String>> entry : responseHeaders.entrySet()){
                if(entry.getKey() == null){
                    status = entry.getValue().toString();
                    break;
                }
            }

            // size of response:
            byte[] responseBytes= responseBody.getBytes();
            Double sizeLong = Double.valueOf(responseBytes.length) / 1000;
            size = Double.toString(sizeLong);

            connection.disconnect();

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * get headers of response
     * @return responseHeaders field
     */
    public Map<String, List<String>> getResponseHeaders() {
        return responseHeaders;
    }

    /**
     * get body of response
     * @return responseBody field
     */
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

    /**
     * set body of requests [Multipart/Form-Data] in method "POST", "PUT", "DELETE"
     * @param body
     * @param boundary
     * @param bufferedOutputStream
     * @throws IOException
     */
    public static void formData(HashMap<String, String> body, String boundary, BufferedOutputStream bufferedOutputStream) throws IOException {
        for (String key : body.keySet()) {
            bufferedOutputStream.write(("--" + boundary + "\r\n").getBytes());
            if (key.contains("file")) {
                bufferedOutputStream.write(("Content-Disposition: form-data; filename=\"" + (new File(body.get(key))).getName() + "\"\r\nContent-Type: Auto\r\n\r\n").getBytes());
                try {
                    File file = new File(body.get(key));
                    BufferedInputStream tempBufferedInputStream = new BufferedInputStream(new FileInputStream(file));
                    int size = (int) file.length();
                    byte[] bytes = new byte[size];
                    bufferedOutputStream.write(tempBufferedInputStream.read(bytes, 0, bytes.length));
                    bufferedOutputStream.write("\r\n".getBytes());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                bufferedOutputStream.write(("Content-Disposition: form-data; name=\"" + key + "\"\r\n\r\n").getBytes());
                bufferedOutputStream.write((body.get(key) + "\r\n").getBytes());
            }
        }
        bufferedOutputStream.write(("--" + boundary + "--\r\n").getBytes());
        bufferedOutputStream.flush();
        bufferedOutputStream.close();
    }

    public void saveImagePreviewResponse(URL url){
        String path = "./image.png";
        BufferedImage image = null;
        try {
            image = ImageIO.read(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            ImageIO.write(image, "png", new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean isImage() {
        return isImage;
    }
}
