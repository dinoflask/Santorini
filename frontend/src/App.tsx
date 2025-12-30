import React from "react";
import "./App.css";
import { GameState, Cell } from "./game";
import BoardCell from "./Cell.tsx";

const API_BASE = "https://santorini-2.onrender.com/";

interface Props {}

interface AppState {
  cells: Cell[];
  currentPlayer: string;
  winner: number | null;
  turnPhase: string | null;
  godCards: string[];
  canPassBuild: boolean;
  canPassMove: boolean;
}

class App extends React.Component<Props, AppState> {
  private initialized: boolean = false;

  constructor(props: Props) {
    super(props);
    this.state = {
      cells: [],
      currentPlayer: "",
      winner: null,
      turnPhase: null,
      godCards: [],
      canPassBuild: false,
      canPassMove: false,
    };
  }

  newGame = async () => {
    console.log("🔍 Calling newGame →", `${API_BASE}newgame`);
    try {
      const response = await fetch(`${API_BASE}newgame`);
      console.log("📡 Response status:", response.status);
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }
      const json = await response.json();
      console.log("✅ New game loaded:", json);
      this.setState({
        cells: json["cells"] || [],
        currentPlayer: json["currentPlayer"] || "",
        winner: json["winner"] || null,
        turnPhase: json["turnPhase"] || null,
        godCards: json["godCards"] ?? [],
        canPassBuild: json["canPassBuild"] ?? false,
        canPassMove: json["canPassMove"] ?? false,
      });
    } catch (error) {
      console.error("❌ New Game failed:", error);
    }
  };

  play = (x: number, y: number): React.MouseEventHandler => {
    return async (e: React.MouseEvent) => {
      e.preventDefault();
      console.log("🔍 Playing:", x, y);
      try {
        const response = await fetch(`${API_BASE}play?x=${x}&y=${y}`);
        console.log("📡 Play response:", response.status);
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        const json = await response.json();
        this.setState({
          cells: json["cells"] || [],
          currentPlayer: json["currentPlayer"] || "",
          winner: json["winner"] || null,
          turnPhase: json["turnPhase"] || null,
          godCards: json["godCards"] ?? [],
          canPassBuild: json["canPassBuild"] ?? false,
          canPassMove: json["canPassMove"] ?? false,
        });
      } catch (error) {
        console.error("❌ Play failed:", error);
      }
    };
  };

  chooseGod = (god: string): React.MouseEventHandler => {
    return async (e: React.MouseEvent) => {
      e.preventDefault();
      console.log("🔍 Choosing god:", god);
      try {
        const response = await fetch(`${API_BASE}choose?god=${god}`);
        if (!response.ok) {
          throw new Error(`HTTP ${response.status}`);
        }
        const json = await response.json();
        this.setState({
          cells: json["cells"] || [],
          currentPlayer: json["currentPlayer"] || "",
          winner: json["winner"] || null,
          turnPhase: json["turnPhase"] || null,
          godCards: json["godCards"] ?? [],
          canPassBuild: json["canPassBuild"] ?? false,
          canPassMove: json["canPassMove"] ?? false,
        });
      } catch (error) {
        console.error("❌ Choose failed:", error);
      }
    };
  };

  passBuild: React.MouseEventHandler = async (e: React.MouseEvent) => {
    e.preventDefault();
    console.log("🔍 Pass build");
    try {
      const response = await fetch(`${API_BASE}passBuild`);
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }
      const json = await response.json();
      this.setState({
        cells: json["cells"] || [],
        currentPlayer: json["currentPlayer"] || "",
        winner: json["winner"] || null,
        turnPhase: json["turnPhase"] || null,
        godCards: json["godCards"] ?? [],
        canPassBuild: json["canPassBuild"] ?? false,
        canPassMove: json["canPassMove"] ?? false,
      });
    } catch (error) {
      console.error("❌ PassBuild failed:", error);
    }
  };

  passMove: React.MouseEventHandler = async (e: React.MouseEvent) => {
    e.preventDefault();
    console.log("🔍 Pass move");
    try {
      const response = await fetch(`${API_BASE}passMove`);
      if (!response.ok) {
        throw new Error(`HTTP ${response.status}`);
      }
      const json = await response.json();
      this.setState({
        cells: json["cells"] || [],
        currentPlayer: json["currentPlayer"] || "",
        winner: json["winner"] || null,
        turnPhase: json["turnPhase"] || null,
        godCards: json["godCards"] ?? [],
        canPassBuild: json["canPassBuild"] ?? false,
        canPassMove: json["canPassMove"] ?? false,
      });
    } catch (error) {
      console.error("❌ PassMove failed:", error);
    }
  };

  createCell(cell: Cell, index: number): React.ReactNode {
    if (cell.playable) {
      return (
        <div key={index}>
          <a href="/" onClick={this.play(cell.x, cell.y)}>
            <BoardCell cell={cell} />
          </a>
        </div>
      );
    } else {
      return (
        <div key={index}>
          <BoardCell cell={cell} />
        </div>
      );
    }
  }

  componentDidMount(): void {
    console.log("🚀 App mounted, testing backend...");
    if (!this.initialized) {
      // Test backend first
      fetch(API_BASE)
        .then((r) => r.text())
        .then((text) => {
          console.log("✅ Backend alive:", text.substring(0, 200));
          this.newGame();
        })
        .catch((e) => {
          console.error("❌ Backend unreachable:", e);
        });
      this.initialized = true;
    }
  }

  render(): React.ReactNode {
    if (this.state.turnPhase === "GOD_SELECTION") {
      return this.renderGodSelection();
    }

    let instructionsText = "";
    if (this.state.winner === null || this.state.winner === -1) {
      instructionsText = `Current turn: ${
        this.state.currentPlayer || "Loading..."
      }`;
    } else {
      instructionsText = `Winner: ${this.state.winner === 0 ? "A" : "B"}`;
    }

    return (
      <div id="app-container">
        <header id="app-header">Santorini</header>
        {this.state.winner !== null && this.state.winner !== -1 && (
          <div id="winner-popup">
            <div id="winner-message">
              Winner: {this.state.winner === 0 ? "A" : "B"}
            </div>
          </div>
        )}

        <div id="game-ui">
          <div id="board">
            {this.state.cells.map((cell, i) => this.createCell(cell, i))}
          </div>
          <div id="instructions">{instructionsText}</div>
          <div id="bottombar">
            <button onClick={this.newGame}>New Game</button>
            {this.state.turnPhase === "BUILD" && this.state.canPassBuild && (
              <button onClick={this.passBuild}>Skip extra build</button>
            )}
            {this.state.turnPhase === "MOVE" && this.state.canPassMove && (
              <button onClick={this.passMove}>Pass Move</button>
            )}
          </div>
        </div>
      </div>
    );
  }

  renderGodSelection(): React.ReactNode {
    const gods = this.state.godCards;
    return (
      <div
        id="god-popup"
        role="dialog"
        aria-modal="true"
        aria-labelledby="god-popup-title"
      >
        <div id="god-popup-content">
          <h2 id="god-popup-title">
            Choose your God power (Player {this.state.currentPlayer})
          </h2>
          <div id="god-grid">
            {gods.map((god) => (
              <button key={god} onClick={this.chooseGod(god)}>
                {god}
              </button>
            ))}
          </div>
        </div>
      </div>
    );
  }
}

export default App;
