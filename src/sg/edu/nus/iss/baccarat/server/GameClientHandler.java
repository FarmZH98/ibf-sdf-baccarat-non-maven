package sg.edu.nus.iss.baccarat.server;

import java.io.IOException;
import java.net.Socket;

public class GameClientHandler implements Runnable {
    private Socket socket;
    private BacarratEngine gameEngine;
    private static userDB db = null;
    private static int betAmount = 0; //due to userDB and betAmount being static, even though we uses threads, we cannot support different users accessing the server. To run more, need to spawn runnable from this class instead of from serverapp

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
                    GameClientHandler.db = new userDB(username, money);
                    reply = request + "|OK";
                    break;
                case "bet":
                    GameClientHandler.betAmount = Integer.parseInt(requestDetails[1]);
                    System.out.println(GameClientHandler.betAmount);
                    int moneyInDB = GameClientHandler.db.getMoney();
                    if(moneyInDB < GameClientHandler.betAmount) {
                        reply = "Insufficient amount. Please place a lower bet or top up your balance.";
                        GameClientHandler.betAmount = 0;
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
                        System.out.println("User wins " + GameClientHandler.betAmount);
                        GameClientHandler.db.addMoney(GameClientHandler.betAmount);
                    } else if (winner == 't') {
                        System.out.println("It is a tie. Bet amount of " + GameClientHandler.betAmount + " returned to user.");
                        //do nothing
                    } else {
                        System.out.println("User loses " + GameClientHandler.betAmount);
                        GameClientHandler.db.deductMoney(GameClientHandler.betAmount);
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

