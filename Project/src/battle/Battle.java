package battle;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.entity.Entity;

public class Battle {
	private enum Players {
		PLAYER, ENEMY
	};

	private enum EndStatus {
		FRIENDED, UNFRENDED
	};

	Thread timer;


	private final int maxPlayerFP;
	private int playerFP;
	private final int maxEnemyFP;
	private HashMap<String, Integer> playerMoves;
	private String[] enemyMoves;
	private int enemyFP;
	boolean finished;

	private Players winner;
	private EndStatus endStatus;
	private Players turn = Players.PLAYER;

	private String status = "";

	boolean ongoing = true;

	/*
	 * Constructor for a battle
	 * 
	 * @param player - The entity that is the main player
	 * 
	 * @param fighting - The entity that the player is fighting
	 * 
	 * @param playerFP - The integer value of "Friend Points" the player has
	 * 
	 * @param maxEnemyFP - The integer value of "Friend Points" to end the battle
	 * "Well"
	 * 
	 * @param playerMoves - An array of strings with each string formatted as
	 * "[move]+/-[FPchange]"
	 * 
	 * @throws IndexOutOfBoundsException - thrown if either enemy or player moves
	 * exceeds 4 moves
	 * 
	 * @throws InvalidMoveFormatException - thrown if any of the strings in player
	 * or enemy moves isn't formatted properly
	 * 
	 * 
	 */
	public Battle(int playerFP, int maxEnemyFP, String[] playerMoves,
			String[] enemyMoves) throws  InvalidMoveFormatException {

		this.playerMoves = new HashMap<String, Integer>();
		this.playerFP = playerFP;
		this.maxPlayerFP = playerFP;
		this.maxEnemyFP = maxEnemyFP;

		for (String move : playerMoves) {
			if (move.indexOf('+') == -1 && move.indexOf('-') == -1) {
				throw new InvalidMoveFormatException("Please have the string formatted as \"[move]+/-[FPchange]\"");
			} else if (move.indexOf('-') != -1) {
				this.playerMoves.put(move.substring(0, move.indexOf('-')),
						Integer.parseInt(move.substring(move.indexOf('-'))));
			} else if (move.indexOf('+') != -1) {
				this.playerMoves.put(move.substring(0, move.indexOf('+')),
						Integer.parseInt(move.substring(move.indexOf('+'))));
			}

		}

		for (String move : enemyMoves) {
			if (move.indexOf('+') == -1 && move.indexOf('-') == -1) {
				throw new InvalidMoveFormatException("Please have the string formatted as \"[move]+/-[FPchange]\"");
			}
		}
		this.enemyMoves = enemyMoves;

		enemyFP = maxEnemyFP / 2;
		
		initTimer();
	}

	/*
	 * Checks if battle is finished
	 */

	public boolean isFinished() {
		updateBattle();
		return !ongoing;
	}

	/*
	 * Uses a move from the players move-set
	 * 
	 * @param move - the move from the move-set that is being used
	 *
	 */
	public void playerUseMove(String move) {
		String moveName;
		if (turn.equals(Players.PLAYER)) {
			if (move.indexOf('-') != -1) {
				moveName = move.substring(0, move.indexOf('-'));
			} else {
				moveName = move.substring(0, move.indexOf('+'));
			}

			enemyFP += playerMoves.get(moveName);

			status = String.format("Player used %s! Enemys FP changed by %d", moveName, playerMoves.get(moveName));
		}
		turn = Players.ENEMY;
	}

	/*
	 * uses a random move from the enemies move-set
	 */
	public void useRandomEnemyMove() {
		Random r = new Random();

		if (turn.equals(Players.ENEMY)) {
			int randomIndex = r.nextInt(4);

			String move = enemyMoves[randomIndex];

			if (move.indexOf('-') != -1) {
				playerFP += Integer.parseInt(move.substring(move.indexOf('-')));
			} else if (move.indexOf('+') != -1) {
				playerFP += Integer.parseInt(move.substring(move.indexOf('+')));
			}

			if (move.indexOf('-') != -1) {
				status = String.format("Enemy used %s! Player Lost %s FP", move.substring(0, move.indexOf('-')),
						move.substring(move.indexOf('-')));
			} else if (move.indexOf('+') != -1) {
				status = String.format("Enemy used %s! Player Lost %s FP", move.substring(0, move.indexOf('+')),
						move.substring(move.indexOf('+')));
			}

		}
		turn = Players.PLAYER;
	}
	
	
	/*
	 * Updates the battle, if the turn is the enemies then it uses a timer thread to give the illusion of the enemy "Thinking"
	 * also tests for end-battle conditions
	 */
	
	private void updateBattle() {

		if (turn.equals(Players.ENEMY)) {

			if (!timer.isAlive()) {
				initTimer();
				timer.start();
			}
		}
		if (enemyFP <= 0) {
			ongoing = false;
			winner = Players.PLAYER;
			endStatus = EndStatus.UNFRENDED;
		} else if (enemyFP >= maxEnemyFP) {
			ongoing = false;
			winner = Players.PLAYER;
			endStatus = EndStatus.FRIENDED;
		} else if (playerFP <= 0) {
			ongoing = false;
			winner = Players.ENEMY;
		}
	}
	
	/*
	 * @returns Returns the players current FP
	 */
	public String getPlayerFP() {
		return String.format("%d/%d", playerFP, maxPlayerFP);
	}
	/*
	 * @returns Returns the enemies current FP
	 */
	public String getEnemyFP() {
		return String.format("%d/%d", enemyFP, maxEnemyFP);
	}
	/*
	 * @returns Returns the current status of the battle
	 */
	public String getStatus() {
		return status;
	}
	
	
	/*
	 * Initializes the timer thread that is needed to use a random enemy move
	 */
	private void initTimer() {
		timer  = new Thread() {
			public void run() {
				long startTime = System.currentTimeMillis();
				finished = false;
				while (!finished) {
					long elapsedTime = System.currentTimeMillis() - startTime;
					long elapsedSeconds = elapsedTime / 1000;
					if (elapsedSeconds > 1) {
						finished = true;
						useRandomEnemyMove();
					}
				}

			}
		};
	}
	
	
	/*
	 * Returns the winner of the battle
	 */
	public String getWinner() {
		if(winner.equals(Players.ENEMY)) {
			return "Enemy";
		}else if(winner.equals(Players.PLAYER) && endStatus.equals(EndStatus.FRIENDED)){
			return "PlayerF";
		}else {
			return "PlayerU";
		}
		
		
	}

}
