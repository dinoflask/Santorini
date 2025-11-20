interface GameState {
  cells: Cell[];
  currentPlayer: string;  
  winner: null;
}

interface Cell {
  text: string; // [] [[]] [[A]] [[[O]]]
  playable: boolean;
  x: number;
  y: number;
}

export type { GameState, Cell }