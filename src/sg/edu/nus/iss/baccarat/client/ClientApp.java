package sg.edu.nus.iss.baccarat.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.Console;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ClientApp {
    public static void main( String[] args )
    {
        System.out.println( "Client App running..." );
        String[] connInfo = args[0].split(":");
        System.out.println(connInfo[0] + " " + connInfo[1]);
        InputStream is = null;
        OutputStream os = null;
        DataInputStream dis = null;
        DataOutputStream dos = null;
        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        //userDB db = null;

        try {
            while (true) {
                Console cons = System.console();
                String input = cons.readLine("What would you like to do?").toLowerCase();
                if (input.equals("close")) {
                    break;
                } 
                
                Socket socket = new Socket(connInfo[0], Integer.parseInt(connInfo[1]));
                is = socket.getInputStream();
                bis = new BufferedInputStream(is);
                dis = new DataInputStream(bis);
                
                os = socket.getOutputStream();
                bos = new BufferedOutputStream(os);
                dos = new DataOutputStream(bos);
                
                input = input.replace(" ", "|");
                dos.writeUTF(input);
                dos.flush();

                String response = dis.readUTF().toLowerCase();
                String[] responseDetails = response.split("\\|");
                if(responseDetails[0].equals("login")) { //confirm bet amount.
                    System.out.println("Login successful. Welcome " + responseDetails[1] + ". " + responseDetails[2] + " was top up as well.");
                } else if (responseDetails[0].equals("bet")) { //reply and mention login succeeded
                    System.out.println("Bet amount successfully registered. The amount is " + responseDetails[1]);
                } else if (response.contains("p|") && response.contains(",b|")) { //return the result. Then client side will decode and see who wins
                    responseDetails[3] = responseDetails[3].split(",")[0];
                    System.out.println("responseDetails[3]: " + responseDetails[3]);
                    int playerHand = Integer.parseInt(responseDetails[1]) + Integer.parseInt(responseDetails[2]) + Integer.parseInt(responseDetails[3]);
                    int bankerHand = Integer.parseInt(responseDetails[4]) + Integer.parseInt(responseDetails[5]) + Integer.parseInt(responseDetails[6]);
                    
                    playerHand %= 10;
                    bankerHand %= 10;

                    System.out.println(response);
                    System.out.println("Player Hand: " + playerHand);
                    System.out.println("Banker Hand: " + bankerHand);
                    char winner;
                    if(playerHand > bankerHand) {
                        System.out.println("Player wins with " + playerHand + " points.");
                        winner = 'P';
                    } else if (playerHand < bankerHand) {
                        System.out.println("Banker wins with " + bankerHand + " points.");
                        winner = 'B';
                    } else {
                        System.out.println("It is a tie.");
                        winner = 'T';
                    }
                    writeWinnerIntoFile(winner);
                } else {
                    System.err.println(response);
                }

                is.close();
                os.close();
                socket.close();
            }
           
        } catch (NumberFormatException | IOException e) {
            e.printStackTrace();
        }

    }
    //Read the contents of file, and write back into file. Only record 6 results per row
    private static void writeWinnerIntoFile(char winner) {
        String GAME_HISTORY_FILE =  "game_history.csv";

        try {
            File game_historyFile = new File(GAME_HISTORY_FILE);
            if(!game_historyFile.exists()) {
                game_historyFile.createNewFile();
            }

            BufferedReader br = new BufferedReader(new FileReader(GAME_HISTORY_FILE));
            List<String> lines = new ArrayList<>();
            String line = br.readLine();

            while(line != null) {
                lines.add(line);
                line = br.readLine();
            }

            String lastline = "";
            if(lines.size() > 0) {
                lastline = lines.get(lines.size()-1);
            }
            
            //long commaCount = 2;
            int commaCount = lastline.length() - lastline.replace(",", "").length();
            // long commaCount = lastline.chars()
            //                             .filter(ch -> ch == ',') //vs code glitch?
            //                             .count(); 
            char winnerUpperCase = Character.toUpperCase(winner);

            FileWriter fw = new FileWriter(GAME_HISTORY_FILE, true);
            BufferedWriter bw = new BufferedWriter(fw);
            if(commaCount < 5 && lines.size() > 0) {
                lastline += "," + winnerUpperCase;
                lines.set(lines.size()-1, lastline);
                bw.write("," + winnerUpperCase);
            } else {
                lines.add(Character.toString(winnerUpperCase));
                bw.write("\n" + winnerUpperCase);
            }

            br.close();
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }
}

