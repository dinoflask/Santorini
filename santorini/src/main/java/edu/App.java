package edu;

import java.io.IOException;
import java.util.Map;
import fi.iki.elonen.NanoHTTPD;

public class App extends NanoHTTPD {

    public static void main(String[] args) {
        try {
            new App();
        } catch (IOException ioe) {
            System.err.println("Couldn't start server:\n" + ioe);
        }
    }

    private Game game;
    private Player playerA = new Player("A");
    private Player playerB = new Player("B");

    /**
     * Start the server at :8080 port.
     * 
     * @throws IOException
     */
    public App() throws IOException {
        super(8080);

        this.game = new Game(playerA, playerB);

        start(NanoHTTPD.SOCKET_READ_TIMEOUT, false);
        System.out.println("\nRunning!\n");
    }

    @Override
    public Response serve(IHTTPSession session) {
        String uri = session.getUri();
        Map<String, String> params = session.getParms();
        if (uri.equals("/newgame")) {
            this.game = new Game(playerA, playerB);
        } else if (uri.equals("/play")) {
            // e.g., /play?x=1&y=1
   
            //take in the x, y from frontend ONLY
            //the BACKEND should know all of this information. The frontend should have no idea whether or not what's happening is a build or a move,
            //but the way takeTurn is currently written doesn't take in an x or y. It should be fixed to have all of these parameters inside the function,
            //then update those based on the x and y that come in

            //Tell this to perplexity
            this.game.takeTurn(Integer.parseInt(params.get("x")), Integer.parseInt(params.get("y")));
        }
        // Extract the view-specific data from the game and apply it to the template.
        GameState gameplay = GameState.forGame(this.game);
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