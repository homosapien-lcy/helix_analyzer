import java.util.ArrayList;


public class Molecule {
	String name;
	int numAtom;
	ArrayList<Atom> atoms;
	Atom capHead;
	Atom capTail;
	
	public Molecule()
	{
		this.name = "Don't know";
		this.numAtom = 0;
		this.atoms = new ArrayList<Atom>();
	}
	
	public Molecule(Atom[] atoms)
	{
		this.name = "Don't know";
		this.numAtom = atoms.length;
		this.atoms = new ArrayList<Atom>();
		
		for(int i=0;i < atoms.length;i++)
		{
			this.atoms.add(atoms[i]);
		}
	}
	
	public Molecule(Atom[] atoms, String name)
	{
		this.name = name;
		this.numAtom = atoms.length;
		this.atoms = new ArrayList<Atom>();
		
		for(int i=0;i < atoms.length;i++)
		{
			this.atoms.add(atoms[i]);
		}
	}
	
	public Molecule(Atom[] atoms, String name, Atom head, Atom tail)
	{
		this.capHead = head;
		this.capTail = tail;
		this.name = name;
		this.numAtom = atoms.length;
		this.atoms = new ArrayList<Atom>();
		
		for(int i=0;i < atoms.length;i++)
		{
			this.atoms.add(atoms[i]);
		}
	}
	
	public void addAtom(Atom a)
	{
		numAtom++;
		atoms.add(a);
	}
	
	public String toString()
	{
		String moleculeInfo = name + " compose of " + "\n";
		for(int i=0;i < atoms.size();i++)
		{
			moleculeInfo += atoms.get(i).toString() + "\n";
		}
		return moleculeInfo;
	}
	
	
}
