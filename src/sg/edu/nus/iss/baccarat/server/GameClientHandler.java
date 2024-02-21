package sg.edu.nus.iss.baccarat.server;

import java.io.IOException;
import java.net.Socket;

public class GameClientHandler implements Runnable {
    private Socket socket;
    private BacarratEngine gameEngine;
    private static userDB db = null;
    private static int betAmount = 0; //this should be migrated to userDB so we can have multiple users with different bet amount

    public GameClientHandler(Socket socket, BacarratEngine gameEngine) {
        this.socket = socket;
        this.gameEngine = gameEngine;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName());
        System.out.println(Thread.currentThread().threadId());
        String request = "";
        String reply = "";
        
        try {
            NetworkIO networkIO = new NetworkIO(this.socket);

            request = networkIO.readRequest().toLowerCase();
            System.out.println(request);
            //handle requests below
            String[] requestDetails = request.split("\\|");
            //System.out.println(requestDetails[0] + ", " + requestDetails[1]);

            //can further parse user input to check if commands are valid in a separate class.
            switch(requestDetails[0]) {
                case "login": 
                    String username = requestDetails[1];
                    int money = Integer.parseInt(requestDetails[2]);
                    this.db = new userDB(username, money);
                    reply = request + "|OK";
                    break;
                case "bet":
                    this.betAmount = Integer.parseInt(requestDetails[1]);
                    System.out.println(this.betAmount);
                    int moneyInDB = this.db.getMoney();
                    if(moneyInDB < this.betAmount) {
                        reply = "Insufficient amount. Please place a lower bet or top up your balance.";
                        this.betAmount = 0;
                    } else {
                        reply = request + "|OK";
                        //this.db.deductMoney(this.betAmount);
                    }
                    break;
                case "deal":
                    String dealSide = requestDetails[1];
                    System.out.println(dealSide);
                    if( !(dealSide.equals("p") || dealSide.equals("b"))) {
                        reply = "You can only bet on 'p' or 'b'.";
                        break;
                    }
                    reply = this.gameEngine.play(dealSide);
                    char winner = this.gameEngine.getCurrentWinner();

                    if(Character.valueOf(winner).compareTo(Character.valueOf(dealSide.charAt(0))) == 0 ) {
                        System.out.println("User wins " + this.betAmount);
                        this.db.addMoney(this.betAmount);
                    } else if (winner == 't') {
                        System.out.println("It is a tie. Bet amount of " + this.betAmount + " returned to user.");
                        //do nothing
                    } else {
                        System.out.println("User loses " + this.betAmount);
                        this.db.deductMoney(this.betAmount);
                    }
                    break;
                default:
                    reply = "Invalid command. Please try again.";
            }
            
            System.out.println(reply);
            networkIO.writeResult(reply);
            networkIO.closeConnection();
            this.socket.close();

        } catch (IOException e) {
            e.printStackTrace();
        } 
        
    }
}

