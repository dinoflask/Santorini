package edu;

import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        System.out.println("=== MAIN START ===");
        try {
            System.out.println("1. Creating App...");
            App app = new App();
            System.out.println("2. App created! Port: " + app.getListeningPort());
            System.out.println("3. Starting keep-alive...");

            Thread.currentThread().join();
            System.out.println("4. Should never reach here!");

        } catch (Exception e) {
            System.err.println("*** MAIN EXCEPTION: " + e);
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
        // 1. FIRST: Use fixed port 8080 (legal super call)
        super(8080);

        // 2. THEN: Rebind to correct Render port
        String portStr = System.getenv("PORT");
        if (portStr == null)
            portStr = System.getProperty("server.port", "8080");
        int port = Integer.parseInt(portStr);

        // Stop default, start on real port
        stop();
        start(port, false);

        System.out.println("PORT env: " + portStr);
        System.out.println("Listening on: " + getListeningPort());

        this.game = new Game(playerA, playerB);
        System.out.println("✓ Server started on " + getListeningPort());
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
