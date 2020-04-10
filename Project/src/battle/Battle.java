package battle;

import java.util.ArrayList;

import com.almasb.fxgl.entity.Entity;

public class Battle {
	private Entity player;
	private Entity fighting;
	private int playerFP;
	private int enemyFP;
	private String[] playerMoves;
	private String[] enemyMoves;
	
	boolean ongoing = false;
	
	/*
	 * Constructor for a battle
	 * 
	 * @param player - The entity that is the main player
	 * @param fighting - The entity that the player is fighting
	 * @param playerFP - The integer value of "Friend Points" the player has
	 * @param maxEnemyFP - The integer value of "Friend Points" to end the battle "Well"
	 * @param playerMoves - An array of strings with each string formatted as "[move]+/-[FPchange]"
	 * 
	 * @throws IndexOutOfBoundsException - thrown if either enemy or player moves exceeds 4 moves
	 * @throws InvalidMoveFormatException - thrown if any of the strings in player or enemy moves isn't formatted properly 
	 * 
	 * 
	 */
	public Battle(Entity player, Entity fighting, int playerFP, int maxEnemyFP, String[] playerMoves, String[] enemyMoves) throws IndexOutOfBoundsException, InvalidMoveFormatException{
		this.player = player;
		this.fighting = fighting;
		this.playerFP = playerFP;
		this.enemyFP = maxEnemyFP;
		if(playerMoves.length <= 4)
			this.playerMoves = playerMoves;
		else
			throw new IndexOutOfBoundsException("Length of playerMoves is greater then the maximum 4");
		if(enemyMoves.length <= 4)
			this.enemyMoves = enemyMoves;
		else
			throw new IndexOutOfBoundsException("Length of enemyMoves is greater then the maximum 4");
		
		
		for(String move : playerMoves) {
			if(move.indexOf('+') == -1 && move.indexOf('-') == -1) {
				throw new InvalidMoveFormatException("Please have the string formatted as \"[move]+/-[FPchange]\"");
			}
		
		}
		for(String move : enemyMoves) {
			if(move.indexOf('+') == -1 && move.indexOf('-') == -1) {
				throw new InvalidMoveFormatException("Please have the string formatted as \"[move]+/-[FPchange]\"");
			}
		}
	}
	
	/*
	 * Starts the battle;
	 */
	public void start() {
		ongoing = true;
	}
	
	public boolean isFinished() {
		return !ongoing;
	}
	
	/*
	 * Uses a move from the players moveset
	 * 
	 * @param indexOfMove - the index of the move being used
	 *
	 */
	public void playerUseMove(int indexOfMove) {
		
	}
	
	/*
	 * uses a random move from the enemies moveset
	 */
	public void useRandomEnemyMove() {
		
	}
	
	
}
