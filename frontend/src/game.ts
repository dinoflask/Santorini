interface GameState {
  cells: Cell[];
  currentPlayer: string;  
  winner: null;
  turnPhase: null;
  godCards: string[];
}

interface Cell {
  text: string; // [] [[]] [[A]] [[[O]]]
  playable: boolean;
  x: number;
  y: number;
}

export type { GameState, Cell }