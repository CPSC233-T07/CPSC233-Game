import java.util.ArrayList;

public class Player extends Entity{
	
	private static final Personality SASSY = null;

	public Player(String name, Personality personality, int happinessPoints, int height, int width) {
		super(name, personality, happinessPoints, height, width);
	}

	private ArrayList<Entity> party = new ArrayList<Entity>();
	
	/*
	 * @return an ArrayList of objects of type Entity that 
	 * define the members of the player's team.
	 */
	public ArrayList<Entity> getParty()
	{	
		ArrayList<Entity> copiedParty = new ArrayList<Entity>();
		for(Entity currentMember: party)
		{
			Entity copiedEntity = new Entity(currentMember);
			copiedParty.add(copiedEntity);
		}
		return copiedParty;
	}
	
	/*
	 * @param an object of type Entity to add to an ArrayList.
	 */
	public void addPartyMember(Entity newMember)
	{
		Entity toAdd = new Entity(newMember);
		party.add(toAdd);
		
	}
	
	/*
	 * @param an int that represents the index of the party 
	 * member being removed from the ArrayList 'party'.
	 */
	public void removePartyMember(int index)
	{
		party.remove(index);
	}
	
	
	public static void main(String args[])
	{
		Player liana = new Player("Liana", SASSY, 100, 50, 50);
		System.out.println(liana.getHeight());
		Entity newCopy = new Entity(liana);
		newCopy.setHeight(25);
		liana.addPartyMember(newCopy);
		System.out.println(liana.getParty().get(0));
		System.out.println(liana.getParty().get(0).getHeight());
	}
}


