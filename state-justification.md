# Justification for handling state
Below, describe where you stored each of the following states and justify your answers with design principles/goals/heuristics/patterns. Discuss the alternatives and trade-offs you considered during your design process.

## Players
<Game class, as the game needs to control the players.>

## Current player
<Game class, as the players should not be responsible for keeping track of turn>

## Worker locations
<Worker class, as the worker should be able to return its own location.>

## Towers
<Space, because the Information Expert heuristic indicates that we should allow 
the class with the most information to calculate whether it itself is a
tower or not.>

## Winner
<Game, as no other class should realistically have to end the game.>

## Design goals/principles/heuristics considered
<High cohesion, Low coupling, Law of Demeter>

## Alternatives considered and analysis of trade-offs
<One alternative considered was giving Player the ability to build and move
Workers, however, I felt there was too much coupling with the Player and the
Game class. The player is not connected to the board instance, but Game is,
so it was eaiser to modify the board from Game.>