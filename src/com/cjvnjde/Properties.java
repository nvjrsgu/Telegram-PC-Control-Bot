package com.cjvnjde;

import javax.xml.bind.DatatypeConverter;
import java.io.*;

/**
 * Created by nvjrsgu on 6/30/2017.
 */
public class Properties {
    public String botUserName;
    public String botToken;
    public String myChatId;
    File settings;
    Properties(String path){
        settings = new File(path);
        getProps();
    }
    Properties(String botUserName, String botToken, String myChatId, File settings){
        this.botUserName = botUserName;
        this.myChatId = myChatId;
        this.botToken = botToken;
        this.settings = settings;
    }

    private void getProps(){
        botUserName = ""; botToken = ""; myChatId = "";
        BufferedReader in;
        if(!settings.exists()){
            try {
                in = new BufferedReader(new InputStreamReader(System.in));
                System.out.println("Write bot's username");
                botUserName = in.readLine();
                System.out.println("Write botToken");
                botToken = in.readLine();
                System.out.println("Write your chat id");
                myChatId = in.readLine();
                in.close();
                Properties prop = new Properties(botUserName, botToken, myChatId, settings);
                prop.saveProps();
               // System.out.println("2");
            } catch (IOException e) {
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }

        }else{
            try {
                in = new BufferedReader(new InputStreamReader(new FileInputStream(settings)));
                String base64 = "";
                String line;
                while ((line = in.readLine()) != null)
                    base64 += line;
                in.close();
                String json = new String(_Base64.FromBase64String(base64));
                botUserName = extractStringValue(json, "botUserName");
                botToken = extractStringValue(json, "botToken");
                myChatId = extractStringValue(json, "myChatId");
                //System.out.println(json);
            }catch (IOException e){
                e.printStackTrace();
            } catch (Throwable throwable) {
                throwable.printStackTrace();
            }
        }
    }

    public void saveProps() throws Throwable {
       // System.out.println("save");
        BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(settings)));
       // System.out.println(toString());
        bw.write(_Base64.ToBase64String(toString().getBytes()));
        bw.close();
    }

    public static class _Base64 {
        public static byte[] FromBase64String(String s) throws Throwable {
            try {
                return DatatypeConverter.parseBase64Binary(s);
            } catch (Exception ex) {
                throw new Throwable("Invalid Base64 string!", ex);
            }
        }

        public static String ToBase64String(byte[] b) {
            return DatatypeConverter.printBase64Binary(b);
        }
    }

    public String toString(){
        return    "{"
                + "botUserName=\"" + botUserName + "\", "
                + "botToken=\"" + botToken + "\", "
                + "myChatId=\"" + myChatId + "\", "
                + "}";
    }

    public static String extractStringValue(String JSON, String value) {
        char[] json = JSON.toCharArray();
        String tmp = "";
        boolean finded = false;

        int index = JSON.indexOf(value);
        if(index < 0)
            return "";
        for (int i = index + value.length() + 1; i < json.length; i++) {
            char element = json[i];

            if (element == '"')
                if (finded)
                    break;
                else {
                    finded = true;
                    continue;
                }

            if (finded)
                tmp += element;
        }

        return tmp;
    }
}
