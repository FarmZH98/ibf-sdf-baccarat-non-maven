package sg.edu.nus.iss.baccarat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

public class userDB {
    private final String username;
    private Integer money;
    private File userDB;

    public userDB(String username, Integer money) {
        this.username = username;
        this.money = money;
        this.userDB = new File(this.username + ".db");
        load();
        save();
    }

    public String getUsername() {
        return this.username;
    }

    public int getMoney() {
        System.out.println("get money");
        return this.money;
    }

    public void addMoney(Integer money) {
        this.money += money;
        save();
    }

    public void deductMoney(Integer money) {
        this.money -= money;
        save();
    }

    public void load() {
        InputStream is = null;
        try {
            if(!this.userDB.exists()) {
                this.userDB.createNewFile();
                return;
            }

            is = new FileInputStream(userDB);
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);
            String item = br.readLine();
            System.out.println(this.username + " has this amount of money before top up: " + this.money);
            if(item != null || item != "") {
                this.money += Integer.parseInt(item);
            }
            System.out.println(this.username + " has this amount of money after top up: " + this.money);
            br.close();
            isr.close();
            is.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void save() {
        OutputStream os = null;

        try {
            os = new FileOutputStream(this.userDB);
            OutputStreamWriter osw = new OutputStreamWriter(os);
            BufferedWriter bw = new BufferedWriter(osw);
            bw.write(String.valueOf(this.money));
            osw.flush();
            bw.flush();
            osw.close();
            bw.close();
            os.flush();
            os.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
