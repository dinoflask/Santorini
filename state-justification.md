# Justification for handling state
Below, describe where you stored each of the following states and justify your answers with design principles/goals/heuristics/patterns. Discuss the alternatives and trade-offs you considered during your design process.

Players
Players are stored in the Game class, since Game orchestrates the overall flow and must coordinate all players. This follows Information Expert and encapsulates player management within the game logic.

Current player
The current player index is managed by Game rather than by, say, the players. This centralizes turn logic and avoids coupling turn-tracking to any individual Player, promoting low coupling.

Worker locations
Each Worker stores its own location. This makes Worker the Information Expert for its position, enabling direct queries and updates without depending on Board or Space to look up worker locations.

Towers
Tower state is held in Space. Space best knows its own contents and levels, so it naturally encapsulates tower logic and queries. This maximizes cohesion and makes expansion (like new tower logic) easier.

Winner
Game stores the winner, as it has full visibility and authority over game progress and end conditions. No other class has enough context to determine or enforce victory.

Design goals/principles/heuristics considered
Key goals included high cohesion (each class does one thing well), low coupling (minimal dependencies), and Law of Demeter (only talk to immediate collaborators).

Alternatives considered and analysis of trade-offs
Considered tracking worker locations on the Board or Space, but this would split responsibility and require syncing between classes.

I also considered that Player could handle turn-tracking, but then Player and Game would need to coordinate closely, increasing coupling. Managing turns in Game keeps responsibilities clear.
