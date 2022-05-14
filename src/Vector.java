
public class Vector {
	
	double x;
	double y;
	double z;
	
	public Vector(double d,double e,double f)
	{
		this.x = d;
		this.y = e;
		this.z = f;
	}
	
	public double vectorLength()
	{
		return Math.sqrt(x*x+y*y+z*z);
	}
	
	public double vectorProduct(Vector b)
	{
		return (x*b.x+y*b.y+z*b.z);
	}
	
	public Vector crossProduct(Vector b)
	{
		double crossX = (y*b.z - z*b.y);
		double crossY = -(x*b.z - z*b.x);
		double crossZ = (x*b.y - y*b.x);
		return new Vector(crossX,crossY,crossZ);
	}
	
	//angle from 0-pi
	public double angleCalc(Vector b)
	{
		Point a1 = new Point(0,0,0);
		Point b1 = new Point(this.x,this.y,this.z);
		Point c1 = new Point(b.x,b.y,b.z);
		double angle;
		
		if(Math.abs(this.vectorProduct(b)) < 0.000000000000000001)
		{
			angle = Math.PI/2;
		}
		else
		{
			angle = Math.atan2((this.crossProduct(b)).vectorProduct(a1.normalCalc(b1,c1)), this.vectorProduct(b));
		}
		
		return angle;
	}
	
	public Vector vectorTranslate(Vector tran)
	{
		return new Vector(this.x - tran.x,this.y - tran.y,this.z - tran.z);
	}
	
	//counter clock
	public Vector vectorRotation(double angle)
	{
		double x = this.x * Math.cos(angle) - this.y * Math.sin(angle);
		double y = this.x * Math.sin(angle) + this.y * Math.cos(angle);
		double z = this.z;
		
		return new Vector(x,y,z);
	}
	
	public Vector normalize()
	{
		double length = this.vectorLength();
		return new Vector(this.x/length,this.y/length,this.z/length);
	}
	
	public String toString()
	{
		return "Vector(" + x + "," + y + "," + z +")"; 
	}
	
}
