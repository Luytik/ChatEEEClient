package ua.kiev.prog;

import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Account {
    private String name;
    private String status = "Available";

    public Account(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int send () throws IOException {

        URL url = new URL(Utils.getURL() + "/users" );
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        Gson gson = new GsonBuilder().create();
        try(OutputStream os = conn.getOutputStream()){
            os.write(gson.toJson(this).getBytes(StandardCharsets.UTF_8));
            return conn.getResponseCode();
        }
    }

    public void changeStatus() throws IOException{
        URL url = new URL(Utils.getURL() + "/users" );
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        String req = "-changeStatus" + toString();
        try(OutputStream os = conn.getOutputStream()){
            os.write(req.getBytes(StandardCharsets.UTF_8));
            conn.getResponseCode();
        }
    }

    public void exitAccount() throws IOException{
        URL url = new URL(Utils.getURL() + "/users" );
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        String req = "-deleteAccount" + toString();
        try(OutputStream os = conn.getOutputStream()){
            os.write(req.getBytes(StandardCharsets.UTF_8));
            conn.getResponseCode();
        }
    }

    public String toString(){
        return " " + name + " " + status;
    }

}
