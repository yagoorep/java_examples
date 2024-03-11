package yagoo.patterns.behavioral.templatemethod;

enum Game {
	Arcade,
	Tactical
}

abstract class AGame {
	//
	protected abstract void connectGameServer();
	protected abstract void loadGame();
	protected abstract void createPlayer();
	protected abstract void showResults();
	//
	public final void play() {
		connectGameServer();
		loadGame();
		createPlayer();
		showResults();
	}
}

class Arcade extends AGame {

	@Override
	protected void connectGameServer() {
		System.out.println("Connecting to Arcade server...");
	}

	@Override
	protected void loadGame() {
		System.out.println("Loading arcade..");
	}

	@Override
	protected void createPlayer() {
		System.out.println("Arcade player");
	}

	@Override
	protected void showResults() {
		System.out.println("Arcade results");
	}
	
}

class Tactical extends AGame {

	@Override
	protected void connectGameServer() {
		System.out.println("Connecting to Tactical server...");
	}

	@Override
	protected void loadGame() {
		System.out.println("Loading tactical..");
	}

	@Override
	protected void createPlayer() {
		System.out.println("Tactical player");
	}

	@Override
	protected void showResults() {
		System.out.println("Tactical results");
	}
	
}

public class TemplateMethodMain {
    
	public static void main(String[] args) {
		Game myGame = Game.Tactical;
		AGame game;
		switch(myGame) {
		case Arcade:
			game = new Arcade();
			break;
		case Tactical:
			game = new Tactical();
			break;
		default:
			throw new RuntimeException();
		}
		game.play();
	}
	
}
