import java.util.ArrayList;


public class Frame {
	int numMolecule;
	ArrayList<Molecule> molecules;
	int which;
	
	public Frame(int which,Molecule[] molecules)
	{
		this.which = which;
		this.numMolecule = molecules.length;
		this.molecules = new ArrayList<Molecule>();
		
		for(int i=0;i < molecules.length;i++)
		{
			this.molecules.add(molecules[i]);
		}
	}
	
	public String toString()
	{
		return "You wanna print out all the Molecules in the system?! Are you insane?!";
	}
}
