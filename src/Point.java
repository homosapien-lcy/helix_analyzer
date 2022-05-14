public class Point {
	
	double x;
	double y;
	double z;
	public static final double accuracy = 10e-50;
	
	public Point(double d,double e,double f)
	{
		this.x = d;
		this.y = e;
		this.z = f;
	}
	
	/**
	 * calculation exception
	 */
	static final Throwable CALCULATION_EXCEPTION = new IllegalArgumentException();

	class CaluculationException extends IllegalArgumentException{

		/**
		 * calculation error
		 */
		private static final long serialVersionUID = 5505043316921860409L;

	}
	
	public Point pointTranslate(Vector tran)
	{
		return new Point(this.x - tran.x,this.y - tran.y,this.z - tran.z);
	}
	
	//counter clock
	public Point pointRotation(double angle)
	{
		double x = this.x * Math.cos(angle) 
				- this.y * Math.sin(angle);
		double y = this.x * Math.sin(angle) 
				+ this.y * Math.cos(angle);
		double z = this.z;
		
		return new Point(x,y,z);
	}
	
	public double distanceSquare(Point end)
	{
		return (end.x - x)*(end.x - x)+
				(end.y - y)*(end.y - y)+
				(end.z - z)*(end.z - z);
	}
	
	public double distance(Point end)
	{
		return Math.sqrt(this.distanceSquare(end));
	}
	
	public Vector normalCalcOld(Point b,Point c)
	{
		Vector ab = this.vectorCalc(b);
		Vector bc = b.vectorCalc(c);
		
		if(Math.abs(ab.x) < accuracy)
		{
			if(Math.abs(bc.x) < accuracy)
			{
				return new Vector(1,0,0);

			}
			else
			{
				Vector tmp = ab;
				ab = bc;
				bc = tmp;
			}
		}
		double solveFactor1 = (bc.x/ab.x);
		
		if(Math.abs(ab.y*solveFactor1-bc.y) < accuracy)
		{
			if(Math.abs(ab.y) < accuracy)
			{
				return new Vector(0,1,0);
			}
			else
			{
				return (new Vector(1,-ab.x/ab.y,0)).normalize();
			}
		}
		else
		{
			double second = (bc.z-solveFactor1*ab.z)/
					(ab.y*solveFactor1-bc.y);
			double first;

			first = -(ab.z+second*ab.y)/ab.x;
		
			return (new Vector(first,second,1)).normalize();
		}
		
	}
	
	public Vector normalCalc(Point b,Point c)
	{
		Vector ab = this.vectorCalc(b);
		Vector bc = b.vectorCalc(c);
		Vector getDaze = ab.crossProduct(bc);
		return getDaze.normalize();
	}
	
	public double torsionCalc(Point b,Point c,Point d)
	{
		Vector Jia = this.normalCalc(b,c);
		Vector Yi = b.normalCalc(c,d);
		
		Vector middleBond = b.vectorCalc(c);
		Vector test = Jia.crossProduct(Yi);
		
		if(Math.abs(middleBond.vectorLength()) < accuracy 
				|| Math.abs(Jia.vectorLength()) < accuracy 
				|| Math.abs(Yi.vectorLength()) < accuracy)
		{
			throw new CaluculationException();
		}
		else if(Math.abs(test.vectorLength()) < accuracy)
		{
			if(Jia.vectorProduct(Yi) < 0)
			{
				return Math.PI;
			}
			else
			{
				return 0;
			}
		}
		else if(middleBond.vectorProduct(test) < 0)
		{
			return -Jia.angleCalc(Yi);
		}
		else
		{
			return Jia.angleCalc(Yi);
		}
		
	}
	
	public Vector vectorCalc(Point end)
	{
		Vector v = new Vector(end.x-x,end.y-y,end.z-z);
		return v;
	}
	
	public String toString()
	{
		return "Point(" + x + "," + y + "," + z +")";
	}
	
	public static void main(String[] args)
	{
		Point a = new Point(0,0,1);
		Point b = new Point(0,0,0);
		Point c = new Point(0,1,0);
		Point d = new Point(-1,1,0);
		
		System.out.println(a.torsionCalc(b,c,d) * 360/(2*Math.PI));
		
		//System.out.println(b.normalCalc(c,d));
		//System.out.println(b.vectorCalc(d).vectorProduct(b.normalCalc(c,d)));
		//System.out.println(c.vectorCalc(b).vectorProduct(b.normalCalc(c,d)));
		//System.out.println(c.vectorCalc(d).vectorProduct(b.normalCalc(c,d)));
	}
}
