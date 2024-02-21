package sg.edu.nus.iss.baccarat.server;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class BacarratEngine {
    private int deckNum;
    private List<Double> cardsDeck = new ArrayList<>();
    private final List<Double> cardDeck = Arrays.asList(1.1, 1.2, 1.3, 1.4, 2.1, 2.2, 2.3, 2.4, 3.1, 3.2, 3.3, 3.4, 4.1, 4.2, 4.3, 4.4, 5.1, 5.2, 5.3, 5.4, 6.1, 6.2, 6.3, 6.4, 7.1, 7.2, 7.3, 7.4, 8.1, 8.2, 8.3, 8.4, 9.1, 9.2, 9.3, 9.4, 10.1, 10.2, 10.3, 10.4, 11.1, 11.2, 11.3, 11.4, 12.1, 12.2, 12.3, 12.4, 13.1, 13.2, 13.3, 13.4);
    private int cardCounter = 0; //to change this to remove card from file..
    private int playerHand;
    private int bankerHand;
    private char currentWinner;
    private final static String GAME_HISTORY_FILE =  "game_history.csv";

    public BacarratEngine(int deckNum) {
        this.deckNum = deckNum;

        for(int i=0; i<deckNum; ++i) {
            this.cardsDeck.addAll(this.cardDeck);
        }

        Collections.shuffle(cardsDeck);

        File cardsDB = new File("cards.db");

        try {
            if(!cardsDB.exists()) {
                cardsDB.createNewFile();
            }

            writeCardsIntoFile(cardsDB);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    private void writeCardsIntoFile(File cardsDB) {
        OutputStream os = null;
        OutputStreamWriter osw = null;
        BufferedWriter bw = null;

        try {
            os = new FileOutputStream(cardsDB);
            osw = new OutputStreamWriter(os);
            bw = new BufferedWriter(osw);
            for(int i=cardCounter; i<cardsDeck.size(); ++i) {
                bw.write(cardsDeck.get(i) + "\n");
            }
            osw.flush();
            bw.flush();
            osw.close();
            bw.close();
            os.flush();
            os.close();
        
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    //Read the contents of file, and write back into file. Only record 6 results per row
    private void writeWinnerIntoFile() {
        System.out.println("testing write winner into file");
        try {
            File game_historyFile = new File(GAME_HISTORY_FILE);
            if(!game_historyFile.exists()) {
                game_historyFile.createNewFile();
            }
            System.out.println("test a");

            BufferedReader br = new BufferedReader(new FileReader(GAME_HISTORY_FILE));
            List<String> lines = new ArrayList<>();
            String line = br.readLine();

            while(line != null) {
                lines.add(line);
                line = br.readLine();
            }

            System.out.println("test b");
            String lastline = "";
            if(lines.size() > 0) {
                lastline = lines.get(lines.size()-1);
            }
            
            //long commaCount = 2;
            int commaCount = lastline.length() - lastline.replace(",", "").length();
            // long commaCount = lastline.chars()
            //                             .filter(ch -> ch == ',') //vs code glitch?
            //                             .count(); 
            char winnerUpperCase = Character.toUpperCase(this.currentWinner);

            System.out.println("test c");
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
            System.out.println("test d");

            br.close();
            bw.flush();
            bw.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public char getCurrentWinner() {
        System.out.println("Current/Last winner is: " + currentWinner);
        return currentWinner;
    }

    //rules are referred from mbs doc https://www.gra.gov.sg/docs/default-source/game-rules/mbs/baccarat-games/mbs-baccarat-game-rules---ver-8.pdf
    public String play(String dealSide) {
        System.out.println("Start play");
        int playerCard1;
        int playerCard2;
        int playerCard3 = 0;
        int bankerCard1;
        int bankerCard2;
        int bankerCard3 = 0;
        double currentCard;
        File cardsDB = new File("cards.db");

        currentCard = this.cardsDeck.get(this.cardCounter++);
        playerCard1 = filter(currentCard);
        currentCard = this.cardsDeck.get(this.cardCounter++);
        bankerCard1 = filter(currentCard);
        currentCard = this.cardsDeck.get(this.cardCounter++);
        playerCard2 = filter(currentCard);
        currentCard = this.cardsDeck.get(this.cardCounter++);
        bankerCard2 = filter(currentCard);

        this.playerHand = (playerCard1 + playerCard2) % 10;
        this.bankerHand = (bankerCard1 + bankerCard2) % 10;
        
        if(this.playerHand >= 8 || this.bankerHand >= 8) {
            this.currentWinner = determineWinner(playerHand, bankerHand);
            writeCardsIntoFile(cardsDB);
            //writeWinnerIntoFile(); //migrate this function to client
            return replytoClient(playerCard1, playerCard2, playerCard3, bankerCard1, bankerCard2, bankerCard3);
        }

        
        if (this.playerHand < 6) {
            currentCard = this.cardsDeck.get(this.cardCounter++);
            playerCard3 = filter(currentCard);
        }

        if(this.bankerHand < 3 || (this.bankerHand == 3 && playerCard3 == 8) || (this.bankerHand == 4 && playerCard3 >= 2 && playerCard3 <= 7) || (this.bankerHand == 5 && playerCard3 >= 4 && playerCard3 <= 7) || (this.bankerHand == 6 && playerCard3 >= 6 && playerCard3 <= 7) || (this.bankerHand >= 0 && this.bankerHand <=5 && this.playerHand >=6 && this.playerHand <=7)) {
            currentCard = this.cardsDeck.get(this.cardCounter++);
            bankerCard3 = filter(currentCard);
        }

        this.playerHand = (this.playerHand + playerCard3) % 10;
        this.bankerHand = (this.bankerHand + bankerCard3) % 10;
        this.currentWinner = determineWinner(playerHand, bankerHand);
            

        //check cards if end liao and reshuffle
        if(this.cardCounter > ((this.deckNum * 52) - 6)) {
            this.cardCounter = 0;
            Collections.shuffle(cardsDeck);
            System.out.println("Cards are finished. Reshuffle the cards again.");
        }

        System.out.println("test 1");
        writeCardsIntoFile(cardsDB);
        System.out.println("test 2");
        //writeWinnerIntoFile();
        System.out.println("test 3");

        return replytoClient(playerCard1, playerCard2, playerCard3, bankerCard1, bankerCard2, bankerCard3);
    }

    public int filter (double card) {
        int cardValue = (int) card;
        if(cardValue > 10) cardValue = 10;

        return cardValue;
    }

    private char determineWinner(int playerHand, int bankerHand) {
        if (playerHand > bankerHand) {
            return 'p';
        } else if (playerHand < bankerHand) {
            return 'b';
        } else {
            return 't';
        }
    }

    private String replytoClient(int playerCard1, int playerCard2, int playerCard3, int bankerCard1, int bankerCard2, int bankerCard3) {
        String reply = "";
        reply += "P|" + playerCard1 + "|" + playerCard2 + "|" + playerCard3;
        reply += ",B|" + bankerCard1 + "|" + bankerCard2 + "|" + bankerCard3;

        return reply;
    }
    
}
