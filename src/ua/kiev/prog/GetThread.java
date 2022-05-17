package ua.kiev.prog;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

// C -> HTTP -> HTTP -> D
// WebSockets


public class GetThread implements Runnable {
    private final Gson gson;
    private int n;
    String to;


    public GetThread(String to) {
        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        this.to = to;
    }

    @Override
    public void run() {
        try {
            while (!Thread.interrupted()) {
                URL url = new URL(Utils.getURL() + "/get?from=" + n);
                HttpURLConnection http = (HttpURLConnection) url.openConnection();

                InputStream is = http.getInputStream();
                try {
                    byte[] buf = responseBodyToArray(is);
                    String strBuf = new String(buf, StandardCharsets.UTF_8);

                    JsonMessages list = gson.fromJson(strBuf, JsonMessages.class);
                    if (list != null) {
                        for (Message m : list.getList()) {
                            if (m.getTo().equals("all"))
                                if(isFile(m)){
                                    fromJsonToFile(m);
                                } else {
                                    System.out.println(m);
                                }
                            if (m.getTo().equals(to))
                                if(isFile(m)){
                                    System.out.println("the file has been sent you, check chat folder");
                                    fromJsonToFile(m);
                                } else {
                                    System.out.println(m);
                                }
                            n++;
                        }
                    }
                } finally {
                    is.close();
                }

                Thread.sleep(500);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private byte[] responseBodyToArray(InputStream is) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[10240];
        int r;

        do {
            r = is.read(buf);
            if (r > 0) bos.write(buf, 0, r);
        } while (r != -1);

        return bos.toByteArray();
    }

    // true = file, false = message
    private boolean isFile(Message message) {
        if (message.getText().startsWith("-file")) {
            return true;
        }
        else{
            return false;
        }

    }

    private void fromJsonToFile(Message message){
        // array[0] - must be codename "file",
        // array[1] = filename
        // array[2] = code of file
        String[] fileMessage = message.getText().split(" ");
        byte[] f = Base64.getDecoder().decode(fileMessage[2]);
        try {
            File folder = new File("C:\\ChatEEEEE\\");
            folder.mkdir();
            Files.write(Paths.get("C:\\ChatEEEEE\\" + fileMessage[1]), f);
        } catch (IOException e) {
            System.out.println("Problems with file path");
        }

    }
}
