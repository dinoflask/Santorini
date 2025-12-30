package edu;

import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    private Game game;
    private Player playerA = new Player("A");
    private Player playerB = new Player("B");

    public static void main(String[] args) {
        System.out.println("=== MAIN START ===");
        try {
            App app = new App();
            System.out.println("2. App created! Port: " + app.getListeningPort());
            System.out.println("3. Server running on port " + app.getListeningPort() + ". Press Ctrl+C to stop.");
            Thread.currentThread().join();
        } catch (Exception e) {
            System.err.println("*** MAIN EXCEPTION: " + e);
            e.printStackTrace();
            System.exit(1);
        }
    }

    // Public no-arg constructor - FIRST calls private helper
    public App() throws IOException {
        this(determinePort());
    }

    // Private constructor with port parameter - reads env here
    private App(int port) throws IOException {
        super(8080);
        stop();
        start(port, false);

        int actualPort = getListeningPort();
        if (actualPort == -1 || actualPort != port) {
            throw new IOException("FATAL: Expected port " + port + ", got " + actualPort);
        }

        System.out.println("✅ BOUND CORRECTLY: " + actualPort);
        this.game = new Game(playerA, playerB);
        System.out.println("✓ Server started on " + getListeningPort());
    }

    // Static helper - reads env after container startup
    private static int determinePort() {
        String portStr = System.getenv("PORT");
        if (portStr == null)
            portStr = "10000"; // Render default
        return Integer.parseInt(portStr);
    }

    @Override
    public Response serve(IHTTPSession session) {
        System.out.println("Method: " + session.getMethod() + " URI: " + session.getUri());

        if (session.getMethod() == Method.OPTIONS) {
            Response resp = NanoHTTPD.newFixedLengthResponse(Response.Status.OK, "text/plain", "");
            resp.addHeader("Access-Control-Allow-Origin", "*");
            resp.addHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            resp.addHeader("Access-Control-Allow-Headers", "*");
            resp.addHeader("Access-Control-Max-Age", "86400");
            return resp;
        }

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
