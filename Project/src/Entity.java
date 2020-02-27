
public class Entity{

	String name;
	Personality personality;
	int happinessPoints;
	int height;
	int width;
	
	
	public Entity(String name, Personality personality, int happinessPoints,int height, int width) {
		this.name = name;
		this.personality = personality;
		this.happinessPoints = happinessPoints;
		this.height = height;
		this.width = width;
	}
	
	public Entity(Entity toCopy) {
		this.name = toCopy.getName();
		this.personality = toCopy.getPersonality();
		this.happinessPoints = toCopy.getHP();
		this.height = toCopy.getHeight();
		this.width = toCopy.getWidth();	
	}
	
	public int getHP() {
		return this.happinessPoints;
	}
	public void setHP(int newHP) {
		this.happinessPoints = newHP;
	}

	public String getName() {
		return this.name;
	}
	public void setName(String newName) {
		this.name = newName;
	}
	
	public Personality getPersonality() {
		return this.personality;
	}
	public void setPersonality(Personality newPersonality) {
		this.personality = newPersonality;
	}
	
	public int getHeight() {
		return this.height;
	}
	public void setHeight(int newHeight) {
		this.height = newHeight;
	}
	
	public int getWidth() {
		return this.width;
	}
	public void setWidth(int newWidth) {
		this.width = newWidth;
	}
	
	public void takeTurn() {
		//To-Do
	}
	public void strongAgainst(Entity opponent) 
    { 
        switch (this.personality) 
        { 
        case BAD_BOY: 
           
            break; 
        case SHY: 
            
            break; 
        case ROMANTIC: 
        	
        	break;
        case ENERGETIC: 
            
            break; 
        case SASSY: 
            
            break; 
        default: 
            
            break; 
        } 
    } 
	
}
