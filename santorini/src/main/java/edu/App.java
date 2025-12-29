package edu;

import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            App app = new App();
            System.out.println("🚀 Server running forever... Port: " + app.getListeningPort());

            // KEEP ALIVE FOREVER
            Thread.currentThread().join();

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private Game game;
    private Player playerA = new Player("A");
    private Player playerB = new Player("B");

    /**
     * Start the server at dynamic port (Render PORT env or 8080 local).
     * 
     * @throws IOException
     */
    public App() throws IOException {
        // Render uses PORT env var (10000), fallback to 8080 local
        super((System.getenv("PORT") != null && !System.getenv("PORT").isEmpty())
                ? Integer.parseInt(System.getenv("PORT"))
                : 8080);

        this.game = new Game(playerA, playerB);
        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning on port " + getListeningPort() + "!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        System.out.println("Method: " + session.getMethod() + " URI: " + session.getUri());

        // Handle OPTIONS preflight FIRST
        if (session.getMethod() == Method.OPTIONS) {
            Response resp = NanoHTTPD.newFixedLengthResponse(Response.Status.OK, "text/plain", "");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            resp.addHeader("Access-Control-Allow-Headers", "*");
            resp.addHeader("Access-Control-Max-Age", "86400");
            return resp;
        }

        // Your original business logic - UNCHANGED
        String uri = session.getUri();
        Map<String, String> params = session.getParms();

        if (uri.equals("/newgame")) {
            playerA = new Player("A");
            playerB = new Player("B");
            this.game = new Game(playerA, playerB);

        } else if (uri.equals("/play")) {
            try {
                int x = Integer.parseInt(params.get("x"));
                int y = Integer.parseInt(params.get("y"));
                this.game.takeTurn(x, y);
                System.out.println("Play: x=" + x + ", y=" + y);
            } catch (Exception e) {
                System.err.println("Play error: " + e);
            }
        } else if (uri.equals("/choose")) {
            try {
                String godName = params.get("god");
                this.game.chooseGodForCurrentPlayer(godName);
                System.out.println("Choose: " + godName);
            } catch (Exception e) {
                System.err.println("Choose error: " + e);
            }
        } else if (uri.equals("/passBuild")) {
            try {
                game.passBuild();
            } catch (Exception e) {
                System.err.println("PassBuild error: " + e);
            }
        }

        // Your original response - UNCHANGED
        GameState gameplay = GameState.forGame(this.game);
        System.out.println("Sending cells: " + gameplay.toString());
        Response resp = NanoHTTPD.newFixedLengthResponse(Response.Status.OK, "application/json", gameplay.toString());
        resp.addHeader("Access-Control-Allow-Origin", "*");
        resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
        resp.addHeader("Access-Control-Allow-Headers", "*");
        return resp;
    }

    public static class Test {
        public String getText() {
            return "Hello World!";
        }
    }
}
