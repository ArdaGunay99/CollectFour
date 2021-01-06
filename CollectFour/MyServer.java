
package CollectFour;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class MyServer {

    public static CopyOnWriteArrayList<SessionThread> gameSessionThreads = new CopyOnWriteArrayList<>();

    public static void main(String[] args) {

        System.out.println("The CollectFour server is running...");
        ExecutorService pool = Executors.newFixedThreadPool(500);
        try (ServerSocket listener = new ServerSocket(5000)) {
            while (true) {
                pool.execute(new MyServer().new ClientHandler(listener.accept()));
            }
        } catch (Exception e) {
            e.getStackTrace();
        }
    }

    class ClientHandler extends SocketAction {
        String userName;
        int playerNumber = 0;


        boolean isBingo = false;
        boolean isCreating = false;
        boolean isFinding = false;


        ClientHandler(Socket s) throws IOException {
            super(s);
            userName = receive();
        }

        @Override
        public void run() {
            try {
                while (!isCreating && !isFinding) {
                    send("Press C to create a game lobby or press F to find one: ");
                    String temp = receive();
                    if (temp.equalsIgnoreCase("c")) {
                        createGame();
                        isCreating = true;
                    } else if (temp.equalsIgnoreCase("f")) {
                        findGame();
                        isFinding = true;
                    }
                }
            } catch (Exception e) {
                e.getStackTrace();
            }
        }


        public void createGame() {
            String sessionName;
            String password;
            int numberOfPlayers;

            try {
                send("Enter new session name: ");
                sessionName = receive();

                send("Enter new optional password or enter 0 : ");
                password = receive();

                send("Enter number of players : ");
                numberOfPlayers = Integer.parseInt(receive());


                // gameSession.insertSession(sessionName,password);
                SessionThread createdSession = new SessionThread(socket, sessionName, numberOfPlayers);
                gameSessionThreads.add(createdSession);
                createdSession.playersWithPoints.putIfAbsent(createdSession.playerCnt.incrementAndGet(), 0);
                createdSession.playersWithHands.putIfAbsent(createdSession.playerCnt.get(), new ArrayList<Integer>());

                createdSession.playerThreadsWithPlayerNumbers.putIfAbsent(createdSession.playerCnt.get(), this);
                createdSession.start();
                createdSession.join();


            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        public void findGame() {
            String sessionName;
            String password;
            boolean gameFound = false;


            try {
                send("all available games: ");
                //gameSession.printRs();
                send("enter the name of the session that you want to join:");
                sessionName = receive();
                for (int i = 0; i < gameSessionThreads.size(); i++) {
                    if (gameSessionThreads.get(i).sessionName.equalsIgnoreCase(sessionName)) {
                        gameSessionThreads.get(i).playersWithPoints.putIfAbsent(gameSessionThreads.get(i).playerCnt.incrementAndGet(), 0);
                        gameSessionThreads.get(i).playersWithHands.putIfAbsent(gameSessionThreads.get(i).playerCnt.get(), new ArrayList<Integer>());
                        gameSessionThreads.get(i).playerThreadsWithPlayerNumbers.putIfAbsent(gameSessionThreads.get(i).playerCnt.get(), this);
                        //   gameSessionThreads.get(i).start();
                        // gameSessionThreads.get(i).join();
                        gameFound = true;
                        break;
                    }
                }
                if (gameFound == false) {
                    send("game doesn't exist");
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


        }
    }
}