import java.util.HashSet;
import java.util.Set;


public class Atom {
	Set<Integer> connection;
	Point coordinate;
	String atom;
	int atomType;
	int atomNum;
	
	public Atom(String atom,int atomNum,int atomType,double x,double y,double z,Set<Integer> connection)
	{
		this.atom = atom;
		this.atomNum = atomNum;
		this.atomType = atomType;
		this.coordinate = new Point(x,y,z);
		this.connection = connection;
	}
	
	public double distance(Atom atm2)
	{
		return this.coordinate.distance(atm2.coordinate);
	}
	
	public double torsionCalc(Atom b,Atom c,Atom d)
	{
		return this.coordinate.torsionCalc(b.coordinate, c.coordinate, d.coordinate);
	}
	
	public String toString()
	{
		Object[] connect = connection.toArray();
		String atomInfo = atomNum + " is " + atomType + " at " + coordinate.toString() + " connect to ";
		for(int i=0;i<connect.length;i++)
		{
			atomInfo += connect[i] + " ";
		}
		return atomInfo;
	}
}
