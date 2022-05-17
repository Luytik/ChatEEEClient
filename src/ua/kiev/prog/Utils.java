package ua.kiev.prog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Utils {
    private static final String URL = "http://127.0.0.1";
    private static final int PORT = 8080;

    public static String getURL() {
        return URL + ":" + PORT;
    }

    public static String getUsersList() throws IOException{
        System.out.println("sending get request -users");
        String html = "";
        java.net.URL url = new URL(Utils.getURL()+ "/users");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(con.getInputStream(), "UTF-8"))){
            String str = "";
            for(;;){
                str = br.readLine();

                if(str == null)
                    break;
                html += str;
            }

        } catch (IOException e) {
            throw e;
        }
        System.out.println(con.getResponseCode());
        return html;
    }
}
