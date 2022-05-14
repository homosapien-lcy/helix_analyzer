import java.awt.Color;
import java.io.BufferedReader;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;


import javax.swing.JFrame;

import org.math.plot.Plot2DPanel;

public class Analyzer {

	Molecule helix;
	int[] taritari;
	HashSet<Integer> backBoneSet;
	int repeats;
	int head;
	int tail;
	
	int from;
	int to;
	
	boolean doRMSD;
	boolean writeBifurcation;
	//donor and acceptor atm type
	HashSet<Integer> donor;
	HashSet<Integer> acceptor;
	double cutoff;
	double NOECutoff;

	ArrayList<double[]> mean;
	ArrayList<double[]> var;

	ArrayList<Double> headToTail;

	ArrayList<double[][]> angleTimeSeries;
	ArrayList<double[]> straightAngleSeries;

	//start atoms
	Atom[] classified;
	ArrayList<Atom> backBone;
	//Index starts from the C terminus
	Atom[][] donorAcceptor;

	ArrayList<int[][]> hBondMatrixTimeSeries;
	ArrayList<double[][]> hBondDistanceMatrixSeries;

	ArrayList[][] hydrogenBondPersistentSeries;
	double[][] avgHBondPersistent;
	double[][] varHBondPersistent;
	int[][]  hydrogenBondTrajNum;

	ArrayList[][] donorAcceptorPersistentSeries;
	double[][] avgDonorAcceptorPersistent;
	double[][] varDonorAcceptorPersistent;
	int[][] donorAcceptorTrajNum;
	int[][] donorAcceptorTrajNumComparison;
	double[][] percentDecrease;

	ArrayList[][] weightedDonorAcceptorPersistentSeries;
	double[][] avgWeightedDonorAcceptorPersistent;
	double[][] varWeightedDonorAcceptorPersistent;

	int[][] hBondHotSpot;
	int[] hBondStat;

	public Analyzer(int from, int to, int[] taritari, int repeats, int head, int tail, HashSet<Integer> donor, HashSet<Integer> acceptor, double cutoff, boolean doRMSD, boolean writeBifurcation)
	{
		this.from = from;
		this.to = to;
		this.head = head;
		this.tail = tail;
		this.repeats = repeats;
		this.taritari = taritari;
		this.headToTail = new ArrayList<Double>();
		this.mean = new ArrayList<double[]>();
		this.var = new ArrayList<double[]>();
		this.helix = new Molecule();
		this.backBoneSet = new HashSet<Integer>();
		this.cutoff = cutoff;
		this.donor = donor;
		this.acceptor = acceptor;
		this.hBondMatrixTimeSeries = new ArrayList<int[][]>();
		this.hBondDistanceMatrixSeries = new ArrayList<double[][]>();
		this.angleTimeSeries = new ArrayList<double[][]>();
		this.straightAngleSeries = new ArrayList<double[]>();
		this.doRMSD = doRMSD;
		this.writeBifurcation = writeBifurcation;

		for(int i = 0;i < taritari.length;i++)
		{
			backBoneSet.add(taritari[i]);
		}

		backBoneSet.add(head);
		backBoneSet.add(tail);

	}

	public Analyzer(Molecule helix, int from, int to, int[] taritari, int repeats, int head, int tail, HashSet<Integer> donor, HashSet<Integer> acceptor, double cutoff, boolean doRMSD, boolean writeBifurcation)
	{
		this.from = from;
		this.to = to;
		this.head = head;
		this.tail = tail;
		this.repeats = repeats;
		this.taritari = taritari;
		this.headToTail = new ArrayList<Double>();
		this.mean = new ArrayList<double[]>();
		this.var = new ArrayList<double[]>();
		this.helix = helix;
		this.backBoneSet = new HashSet<Integer>();
		this.cutoff = cutoff;
		this.donor = donor;
		this.acceptor = acceptor;
		this.hBondMatrixTimeSeries = new ArrayList<int[][]>();
		this.hBondDistanceMatrixSeries = new ArrayList<double[][]>();
		this.angleTimeSeries = new ArrayList<double[][]>();
		this.straightAngleSeries = new ArrayList<double[]>();
		this.doRMSD = doRMSD;
		this.writeBifurcation = writeBifurcation;

		for(int i = 0;i < taritari.length;i++)
		{
			backBoneSet.add(taritari[i]);
		}

		backBoneSet.add(head);
		backBoneSet.add(tail);

	}

	//castings
	public static Integer[][] toInteger(int[][] in)
	{
		Integer[][] out = new Integer[in.length][in[0].length];
		for(int i = 0;i < in.length;i++)
		{
			for(int j = 0;j < in[i].length;j++)
			{
				out[i][j] = Integer.valueOf(in[i][j]);
			}
		}

		return out;
	}

	public static Integer[] toInteger(int[] in)
	{
		Integer[] out = new Integer[in.length];
		for(int i = 0;i < in.length;i++)
		{
			out[i] = Integer.valueOf(in[i]);
		}

		return out;
	}

	public static Double[][] toDouble(double[][] in)
	{
		Double[][] out = new Double[in.length][in[0].length];
		for(int i = 0;i < in.length;i++)
		{
			for(int j = 0;j < in[i].length;j++)
			{
				out[i][j] = Double.valueOf(in[i][j]);
			}
		}

		return out;
	}

	public static Double[] toDouble(double[] in)
	{
		Double[] out = new Double[in.length];
		for(int i = 0;i < in.length;i++)
		{
			out[i] = Double.valueOf(in[i]);
		}

		return out;
	}

	public static double[] castToDouble(int[] integer)
	{
		double[] Double = new double[integer.length];
		for(int i = 0;i < integer.length;i++)
		{
			Double[i] = integer[i];
		}

		return Double;
	}

	public static double[][] castToDouble(int[][] integer)
	{
		double[][] Double = new double[integer.length][integer[0].length];
		for(int i = 0;i < integer.length;i++)
		{
			for(int j = 0;j < integer[0].length;j++)
			{
				Double[i][j] = integer[i][j];
			}
		}

		return Double;
	}

	//initializations
	public static int[] zeroArray(int l)
	{
		int[] zeros = new int[l];
		for(int i = 0;i < l;i++)
		{
			zeros[i] = 0;
		}
		return zeros;
	}

	public static int[][] zeroMatrix(int r, int c)
	{
		int[][] zeros = new int[r][c];
		for(int i = 0;i < r;i++)
		{
			for(int j = 0;j < c;j++)
			{
				zeros[i][j] = 0;
			}
		}
		return zeros;
	}

	public static boolean[] booleanArray(int l, boolean which)
	{
		boolean[] yn = new boolean[l];
		for(int i = 0;i < l;i++)
		{
			yn[i] = which;
		}
		return yn;
	}

	public static boolean[][] booleanMatrix(int r, int c, boolean which)
	{
		boolean[][] yn = new boolean[r][c];
		for(int i = 0;i < r;i++)
		{
			for(int j = 0;j < c;j++)
			{
				yn[i][j] = which;
			}
		}
		return yn;
	}

	//initialize persistence analysis
	public void persisAnalysisIni()
	{
		//initialization for H bond series
		this.hydrogenBondPersistentSeries = new ArrayList[repeats+1][repeats+1];
		for(int i = 0;i < repeats+1;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				hydrogenBondPersistentSeries[i][j] = new ArrayList<Integer>();
			}
		}
		avgHBondPersistent = new double[repeats+1][repeats+1];
		varHBondPersistent = new double[repeats+1][repeats+1];
		hydrogenBondTrajNum = new int[repeats+1][repeats+1];

		//initialization for donor acceptor persistent series
		this.donorAcceptorPersistentSeries = new ArrayList[2][repeats+1];
		for(int i = 0;i < 2;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				donorAcceptorPersistentSeries[i][j] = new ArrayList<Integer>();
			}
		}
		avgDonorAcceptorPersistent = new double[2][repeats+1];
		varDonorAcceptorPersistent = new double[2][repeats+1];
		donorAcceptorTrajNum = new int[2][repeats+1];
		donorAcceptorTrajNumComparison = new int[2][repeats+1];
		percentDecrease = new double[2][repeats+1];

		this.weightedDonorAcceptorPersistentSeries = new ArrayList[2][repeats+1];
		for(int i = 0;i < 2;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				weightedDonorAcceptorPersistentSeries[i][j] = new ArrayList<Integer>();
			}
		}
		avgWeightedDonorAcceptorPersistent = new double[2][repeats+1];
		varWeightedDonorAcceptorPersistent = new double[2][repeats+1];
	}

	//calculations
	public static <T extends Number> double sum(T[] x)
	{
		double sum = 0;
		for(int i = 0;i < x.length;i++)
		{
			sum += x[i].doubleValue();
		}

		return sum;
	}

	public static<T extends Number> double mean(T[] x)
	{
		double sum = sum(x);
		double avg = sum/x.length;

		return avg;
	}


	public static<T extends Number> double var(T[] x)
	{
		if(x.length == 1)
		{
			return Double.POSITIVE_INFINITY;
		}

		double var = 0;
		double mean = mean(x);

		for(int i = 0;i < x.length;i++)
		{
			var += (x[i].doubleValue() - mean)*
					(x[i].doubleValue() - mean);
		}

		var /= (x.length - 1);
		return var;
	}

	public static<T extends Number> double var(T[] x, double mean)
	{
		if(x.length == 1)
		{
			return Double.POSITIVE_INFINITY;
		}

		double var = 0;

		for(int i = 0;i < x.length;i++)
		{
			var += (x[i].doubleValue() - mean)*
					(x[i].doubleValue() - mean);
		}

		var /= (x.length - 1);
		return var;
	}

	public static<T extends Number> double sum(ArrayList<T> x)
	{
		double sum = 0;
		for(int i = 0;i < x.size();i++)
		{
			sum += x.get(i).doubleValue();
		}

		return sum;
	}

	public static<T extends Number> double mean(ArrayList<T> x)
	{
		double sum = sum(x);
		double avg = sum/x.size();

		return avg;
	}

	public static<T extends Number> double var(ArrayList<T> x)
	{

		if(x.size() == 1)
		{
			return Double.POSITIVE_INFINITY;
		}

		double var = 0;
		double mean = mean(x);

		for(int i = 0;i < x.size();i++)
		{
			var += (x.get(i).doubleValue() - mean)*
					(x.get(i).doubleValue() - mean);
		}

		var /= (x.size() - 1);
		return var;
	}

	public static<T extends Number> double var(ArrayList<T> x, double mean)
	{

		if(x.size() == 1)
		{
			return Double.POSITIVE_INFINITY;
		}

		double var = 0;

		for(int i = 0;i < x.size();i++)
		{
			var += (x.get(i).doubleValue() - mean)*
					(x.get(i).doubleValue() - mean);
		}

		var /= (x.size() - 1);
		return var;
	}

	//normalize
	public static <T extends Number> double[] normalize(T[] weight)
	{
		double[] normal = new double[weight.length];
		double sum = sum(weight);

		for(int i = 0;i < weight.length;i++)
		{
			normal[i] = weight[i].doubleValue()/sum;
		}

		return normal;
	}

	public static <T extends Number> double weightedSum(T[] x, T[] weight)
	{
		double sum = 0;
		for(int i = 0;i < x.length;i++)
		{
			sum += x[i].doubleValue() * weight[i].doubleValue();
		}

		return sum;
	}

	public static <T extends Number> double weightedMean(T[] x, T[] weight)
	{
		double totalWeight = sum(weight);
		double weightedSum = weightedSum(x, weight);

		return weightedSum/totalWeight;
	}

	public static <T extends Number> double weightedVar(T[] x, T[] weight)
	{
		if(x.length == 1)
		{
			return Double.POSITIVE_INFINITY;
		}

		if(sum(x) == 0)
		{
			return 0;
		}

		double[] normalWeight = normalize(weight);
		double weightedMean = weightedMean(x, weight);
		double V2 = arrayProduct(toDouble(normalWeight),toDouble(normalWeight));
		double V1 = 1;

		double weightedVar = 0;
		for(int i = 0;i < x.length;i++)
		{
			weightedVar += normalWeight[i] * 
					(x[i].doubleValue() - weightedMean) *
					(x[i].doubleValue() - weightedMean);
		}

		return (V1/(V1*V1-V2))*
				weightedVar;
	}

	public static boolean rowExist(int[][] matrix,int rowNum)
	{
		boolean exist = false;

		for(int i = 0;i < matrix[rowNum].length;i++)
		{
			if(matrix[rowNum][i] == 1)
			{
				exist = true;
				break;
			}
		}

		return exist;
	}

	public static boolean colExist(int[][] matrix,int colNum)
	{
		boolean exist = false;

		for(int i = 0;i < matrix.length;i++)
		{
			if(matrix[i][colNum] == 1)
			{
				exist = true;
				break;
			}
		}

		return exist;
	}

	public static<T extends Number> double rowSum(T[][] matrix,int rowNum)
	{
		double sum = 0;

		for(int i = 0;i < matrix[rowNum].length;i++)
		{
			sum += matrix[rowNum][i].doubleValue();
		}

		return sum;
	}

	public static<T extends Number> double colSum(T[][] matrix,int colNum)
	{
		double sum = 0;

		for(int i = 0;i < matrix.length;i++)
		{
			sum += matrix[i][colNum].doubleValue();
		}

		return sum;
	}

	public static<T extends Number> double[][] matrixSwap(T[][] angles)
	{
		double[][] swap = new double[angles[0].length][angles.length];
		for(int i = 0;i < angles.length;i++)
		{
			for(int j = 0;j < angles[i].length;j++)
			{
				swap[j][i] = angles[i][j].doubleValue();
			}
		}

		return swap;
	}

	public static<T extends Number> double[][] matrixSum(T[][] A, T[][] B)
	{
		double[][] sum = new double[A.length][A[0].length];
		for(int i = 0;i < A.length;i++)
		{
			for(int j = 0;j < A[i].length;j++)
			{
				sum[i][j] = A[i][j].doubleValue() + B[i][j].doubleValue();
			}
		}

		return sum;
	}

	public static<T extends Number> double[][] matrixSub(T[][] A, T[][] B)
	{
		double[][] sub = new double[A.length][A[0].length];
		for(int i = 0;i < A.length;i++)
		{
			for(int j = 0;j < A[i].length;j++)
			{
				sub[i][j] = A[i][j].doubleValue() - B[i][j].doubleValue();
			}
		}

		return sub;
	}

	public static <T extends Number> double arrayProduct(T[] A, T[] B)
	{
		double arrayProduct = 0;
		for(int i = 0;i < A.length;i++)
		{
			arrayProduct += A[i].doubleValue() * B[i].doubleValue();
		}

		return arrayProduct;
	}

	//calculate the percent decrease of hydrogon bond 
	//breakage due to multi-center hydrogen bonding
	public static<T extends Number> double[][] percentDecrease(T[][] A,T[][] B)
	{
		double[][] decrease = new double[A.length][A[0].length];

		for(int i = 0;i < A.length;i++)
		{
			for(int j = 0;j < A[i].length;j++)
			{
				if(Math.abs(B[i][j].doubleValue()) < Point.accuracy)
				{
					if(Math.abs(A[i][j].doubleValue()) < Point.accuracy)
					{
						decrease[i][j] = 0;
					}
					else if(A[i][j].doubleValue() > 0)
					{
						decrease[i][j] = Double.POSITIVE_INFINITY;
					}
					else
					{
						decrease[i][j] = Double.NEGATIVE_INFINITY;
					}
				}
				else
				{
					decrease[i][j] = (B[i][j].doubleValue() - 
							A[i][j].doubleValue())/B[i][j].doubleValue(); 
				}
			}
		}

		return decrease;
	}

	public static<T extends Number> double[][] sqrt(T[][] A)
	{
		double[][] sqrt = new double[A.length][A[0].length];

		for(int i = 0;i < A.length;i++)
		{
			for(int j = 0;j < A[0].length;j++)
			{
				sqrt[i][j] = Math.sqrt(A[i][j].doubleValue());
			}
		}

		return sqrt;
	}

	//compute the hotspot diagonal
	public int[] hBondStat(int[][] hBondHotSpot)
	{
		int[] hBondClasses = zeroArray(repeats+1);

		for(int i = 0;i < hBondHotSpot.length;i++)
		{
			for(int j = 0;j < hBondHotSpot[i].length;j++)
			{
				hBondClasses[Math.abs(j-i)] += hBondHotSpot[i][j];
			}
		}

		return hBondClasses;
	}

	//sum up the matrix trajectory
	public int[][] hBondHotSpot()
	{
		int[][] hotSpotMatrix = zeroMatrix(repeats+1,repeats+1);

		for(int i = 0;i < hBondMatrixTimeSeries.size();i++)
		{
			for(int j = 0;j < hotSpotMatrix.length;j++)
			{
				for(int k = 0;k < hotSpotMatrix[j].length;k++)
				{
					hotSpotMatrix[j][k] += hBondMatrixTimeSeries.get(i)[j][k];
				}
			}
		}

		return hotSpotMatrix;
	}

	//intake a 2*(repeat+1) matrix, the 2 rows are H bond donor(0) and accepter(1)
	public double[][] hBondMatrixCalc(Atom[][] classifiedAtm)
	{
		double[][] hBondDistance = new double[repeats+1][repeats+1];

		for(int i = 0;i < repeats+1;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				hBondDistance[i][j] = classifiedAtm[0][i].distance(classifiedAtm[1][j]);
			}
		}

		return hBondDistance;
	}
	
	public int[][] hBondMatrixIdentify(double[][] distanceMatrix, double cutoff)
	{
		int[][] hBondMatrix = new int[repeats+1][repeats+1];
		
		for(int i = 0;i < repeats+1;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				if(distanceMatrix[i][j] < cutoff)
				{
					hBondMatrix[i][j] = 1;
				}
				else
				{
					hBondMatrix[i][j] = 0;
				}
			}
		}
		
		return hBondMatrix;
	}
	
	//intake a 2*(repeat+1) matrix, the 2 rows are H bond donor(0) and accepter(1)
	public double[][] hBondSoftMatrixCalc(Atom[][] classifiedAtm, double cutoff)
	{
		double[][] hBondMatrix = new double[repeats+1][repeats+1];

		for(int i = 0;i < repeats+1;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				double distance = classifiedAtm[0][i].distance(classifiedAtm[1][j]);
				
				if(distance < cutoff)
				{
					hBondMatrix[i][j] = 1;
				}
				else
				{
					hBondMatrix[i][j] = Math.exp(cutoff - distance);
				}
			}
		}			
		
		return hBondMatrix;
	}
	
	public int[][] NOEMatrixCalc(Atom[] targetHydrogens, double NOECutoff)
	{
		int size = targetHydrogens.length;
		int[][] NOEMatrix = new int[size][size];
		
		for(int i = 0;i < size;i++)
		{
			for(int j = 0;i < size;j++)
			{
				if(targetHydrogens[i].distance(targetHydrogens[j]) < NOECutoff)
				{
					NOEMatrix[i][j] = 1;
				}
				else
				{
					NOEMatrix[i][j] = 0;
				}
			}
		}
		
		return NOEMatrix;
	}

	public double RMSD(Point[] a,Point[] b)
	{
		if(a.length != b.length)
		{
			try {
				throw Point.CALCULATION_EXCEPTION;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		double RMSD = 0;

		for(int i = 0;i < a.length;i++)
		{
			RMSD += a[i].distanceSquare(b[i]);
		}

		return Math.sqrt(RMSD/a.length);
	}

	//calculate single H Bond persistent
	public void hydrogenBondPersistentCalc()
	{
		persisAnalysisIni();

		boolean[][] lastFrameBond = booleanMatrix(repeats+1, repeats+1, false);
		boolean[][] lastFrameDonorAcceptor = booleanMatrix(2, repeats+1, false);

		//for time i, go through the bond from donor j to acceptor k 
		for(int i = 0;i < hBondMatrixTimeSeries.size();i++)
		{
			for(int j = 0;j < hBondMatrixTimeSeries.get(i).length;j++)
			{
				for(int k = 0;k < hBondMatrixTimeSeries.get(i)[j].length;k++)
				{
					//bond?
					if(hBondMatrixTimeSeries.get(i)[j][k] == 1)
					{
						//if there is bond from donor j to acceptor k in last frame
						if(lastFrameBond[j][k])
						{
							int lastElementIndex = hydrogenBondPersistentSeries[j][k].size() - 1;
							int lastElement = (Integer) hydrogenBondPersistentSeries[j][k].get(lastElementIndex);
							hydrogenBondPersistentSeries[j][k].set(lastElementIndex,lastElement+1);

						}
						else
						{
							hydrogenBondPersistentSeries[j][k].add(new Integer(1));

						}

						lastFrameBond[j][k] = true;
					}
					else
					{
						lastFrameBond[j][k] = false;
					}
				}
			}

			for(int j = 0;j < hBondMatrixTimeSeries.get(i).length;j++)
			{
				int rowSum = (int) rowSum(toInteger(hBondMatrixTimeSeries.get(i)),j);
				//Fifth Seal
				//if(rowExist(hBondMatrixTimeSeries.get(i),j))
				if(rowSum > 0)
				{
					if(lastFrameDonorAcceptor[0][j])
					{
						int lastElementIndex = donorAcceptorPersistentSeries[0][j].size() - 1;
						int lastElement = (Integer) donorAcceptorPersistentSeries[0][j].get(lastElementIndex);
						donorAcceptorPersistentSeries[0][j].set(lastElementIndex,lastElement+1);

						int lastWElement = (Integer) weightedDonorAcceptorPersistentSeries[0][j].get(lastElementIndex);
						weightedDonorAcceptorPersistentSeries[0][j].set(lastElementIndex,lastWElement+rowSum);
					}
					else
					{
						donorAcceptorPersistentSeries[0][j].add(new Integer(1));

						weightedDonorAcceptorPersistentSeries[0][j].add(new Integer(rowSum));
					}

					lastFrameDonorAcceptor[0][j] = true;
				}
				else
				{
					lastFrameDonorAcceptor[0][j] = false;
				}
			}

			for(int j = 0;j < hBondMatrixTimeSeries.get(i)[0].length;j++)
			{
				int colSum = (int) colSum(toInteger(hBondMatrixTimeSeries.get(i)),j);
				//if(colExist(hBondMatrixTimeSeries.get(i),j))
				if(colSum > 0)
				{
					if(lastFrameDonorAcceptor[1][j])
					{
						int lastElementIndex = donorAcceptorPersistentSeries[1][j].size() - 1;
						int lastElement = (Integer) donorAcceptorPersistentSeries[1][j].get(lastElementIndex);
						donorAcceptorPersistentSeries[1][j].set(lastElementIndex,lastElement+1);

						int lastWElement = (Integer) weightedDonorAcceptorPersistentSeries[1][j].get(lastElementIndex);
						weightedDonorAcceptorPersistentSeries[1][j].set(lastElementIndex,lastWElement+colSum);
					}
					else
					{
						donorAcceptorPersistentSeries[1][j].add(new Integer(1));

						weightedDonorAcceptorPersistentSeries[1][j].add(new Integer(colSum));
					}

					lastFrameDonorAcceptor[1][j] = true;
				}
				else
				{
					lastFrameDonorAcceptor[1][j] = false;
				}
			}	

		}

	}

	public double[][] angleGet()
	{
		//list ini
		ArrayList<Double>[] allList = new ArrayList[taritari.length];
		for(int i = 0;i < taritari.length;i++)
		{
			allList[i] = new ArrayList<Double>();
		}

		double[] angleArray = new double[backBone.size() - 3];

		//calc and add
		for(int i = 0;i < backBone.size() - 3;i++)
		{
			double temp = (backBone.get(i).torsionCalc(backBone.get(i+1), 
					backBone.get(i+2), 
					backBone.get(i+3)))
					*360/(2*Math.PI);

			//add to straight series
			angleArray[i] = temp;

			//add to series
			for(int j = 0;j < taritari.length;j++)
			{
				if(backBone.get(i).atomType == taritari[j])
				{
					allList[j].add(temp);
					break;
				}
			}

			if(backBone.get(i).atomType == head)
			{
				allList[taritari.length - 1].add(temp);
			}
		}

		//to the type
		double[][] allAngles = new double[taritari.length][];
		for(int i = 0;i < taritari.length;i++)
		{
			allAngles[i] = new double[allList[i].size()];
			for(int j = 0;j < allList[i].size();j++)
			{
				allAngles[i][j] = allList[i].get(j);
			}
		}

		this.straightAngleSeries.add(angleArray);

		//return
		return allAngles;
	}

	//Third Seal
	//classified atoms
	/*public void classifier()
	{
		ArrayList<Atom> atoms = helix.atoms;
		int counts = 0;

		//go through the helix, and find the start of each segment
		for(int i = 0;i < helix.numAtom;i++)
		{
			if(atoms.get(i).atomType == taritari[0])
			{
				classified[counts] = atoms.get(i);
				counts++;
			}
		}

	}

	//Fourth Seal
	//Angle Calc
	public Double[][] angleGet(Atom[] start)
	{
		ArrayList<Atom> molecule = helix.atoms;
		Atom[][] targetGroup = new Atom[repeats][taritari.length + 2];
		Double[][] allAngles = new Double[taritari.length - 1][repeats];

		//in each segment
		for(int i = 0;i < repeats;i++)
		{
			targetGroup[i][1] = start[i];

			//add the last atom from the last segment, use to compute the torsion
			for(Integer intel : start[i].connection)
			{
				if(molecule.get(intel - 1).atomType == taritari[taritari.length - 1])
				{
					targetGroup[i][0] = helix.atoms.get(intel - 1);
					break;
				}
				else if(molecule.get(intel - 1).atomType == helix.capHead.atomType)
				{
					targetGroup[i][0] = helix.capHead;
					break;
				}
			}

			//start from the first atom of the segment, go through the connection list and put the atoms needed for torsion calculation into one array
			Atom current = start[i];
			int next = taritari[1];
			int nextPos = 2;

			while(targetGroup[i][targetGroup[i].length - 1] == null)
			{
				for(Integer intel : current.connection)
				{
					if(molecule.get(intel - 1).atomType == next)
					{
						current = molecule.get(intel - 1);
						targetGroup[i][nextPos] = current;
						next = taritari[nextPos % taritari.length];
						nextPos++;
						break;
					}
					else if(molecule.get(intel - 1).atomType == helix.capTail.atomType)
					{
						targetGroup[i][nextPos] = helix.capTail;
						break;
					}
				}
			}

		}

		//calculate
		for(int i = 0;i < repeats;i++)
		{
			for(int j = 0;j < (taritari.length - 1);j++)
			{
				double ang = (targetGroup[i][j].torsionCalc(targetGroup[i][j+1],targetGroup[i][j+2],targetGroup[i][j+3]))*360/(2*Math.PI);
				allAngles[j][i] = ang;
			}
		}

		return allAngles;
	}*/

	public void persistenceStat()
	{
		for(int i = 0;i < repeats+1;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				if(hydrogenBondPersistentSeries[i][j].size() == 0)
				{
					avgHBondPersistent[i][j] = 0;
					varHBondPersistent[i][j] = Double.POSITIVE_INFINITY;
				}
				else
				{
					avgHBondPersistent[i][j] = mean(hydrogenBondPersistentSeries[i][j]);
					varHBondPersistent[i][j] = var(hydrogenBondPersistentSeries[i][j],avgHBondPersistent[i][j]);
				}

				hydrogenBondTrajNum[i][j] = hydrogenBondPersistentSeries[i][j].size();
			}
		}

		for(int i = 0;i < 2;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				if(donorAcceptorPersistentSeries[i][j].size() == 0)
				{
					avgDonorAcceptorPersistent[i][j] = 0;
					varDonorAcceptorPersistent[i][j] = Double.POSITIVE_INFINITY;
				}
				else
				{
					avgDonorAcceptorPersistent[i][j] = mean(donorAcceptorPersistentSeries[i][j]);
					varDonorAcceptorPersistent[i][j] = var(donorAcceptorPersistentSeries[i][j],avgDonorAcceptorPersistent[i][j]);
				}

				donorAcceptorTrajNum[i][j] = donorAcceptorPersistentSeries[i][j].size();

				//sum the row and col as comparison
				if(i == 0)
				{
					donorAcceptorTrajNumComparison[i][j] = (int) rowSum(toInteger(hydrogenBondTrajNum),j);
				}
				else
				{
					donorAcceptorTrajNumComparison[i][j] = (int) colSum(toInteger(hydrogenBondTrajNum),j);
				}

			}
		}

		for(int i = 0;i < 2;i++)
		{
			for(int j = 0;j < repeats+1;j++)
			{
				if(weightedDonorAcceptorPersistentSeries[i][j].size() == 0)
				{
					avgWeightedDonorAcceptorPersistent[i][j] = 0;
					varWeightedDonorAcceptorPersistent[i][j] = Double.POSITIVE_INFINITY;
				}
				else
				{
					avgWeightedDonorAcceptorPersistent[i][j] = mean(weightedDonorAcceptorPersistentSeries[i][j]);
					varWeightedDonorAcceptorPersistent[i][j] = var(weightedDonorAcceptorPersistentSeries[i][j],avgWeightedDonorAcceptorPersistent[i][j]);
				}
			}
		}

		percentDecrease = percentDecrease(toInteger(donorAcceptorTrajNum),toInteger(donorAcceptorTrajNumComparison));
	}

	public static double jaccardDist(int[] vectorA,int[] vectorB,boolean modified)
	{
		int common = 0;
		int total = 0;

		if(vectorA.length != vectorB.length)
		{
			try {
				throw Point.CALCULATION_EXCEPTION;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		for(int i = 0;i < vectorA.length;i++)
		{
			if(vectorA[i] == 1 || vectorB[i] == 1)
			{
				total++;
			}

			if(vectorA[i] == 1 && vectorB[i] == 1)
			{
				common++;
			}
		}

		if(modified)
		{
			return (total-common)/(common+1);
		}
		else
		{
			return (total-common)/total;
		}
	}


	public static double jaccardDist(int[][] vectorA,int[][] vectorB,boolean modified)
	{
		int common = 0;
		int total = 0;

		if(vectorA.length != vectorB.length)
		{
			try {
				throw Point.CALCULATION_EXCEPTION;
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}

		for(int i = 0;i < vectorA.length;i++)
		{

			if(vectorA[i].length != vectorB[i].length)
			{
				try {
					throw Point.CALCULATION_EXCEPTION;
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}

			for(int j = 0;j < vectorA[i].length;j++)
			{
				if(vectorA[i][j] == 1 || vectorB[i][j] == 1)
				{
					total++;
				}

				if(vectorA[i][j] == 1 && vectorB[i][j] == 1)
				{
					common++;
				}
			}
		}

		double result;

		if(total == 0)
		{
			result = 1;
		}
		else
		{
			if(modified)
			{
				result = (double)(total-common)/(common+1);
			}
			else
			{
				result = (double)(total-common)/total;
			}
		}

		return result;
	}

	//analysis
	public void analyze()
	{
		extraction();

		//Sixth Seal
		//Old version calculate the angle
		//this.classified = new Atom[repeats];
		//classifier();

		double[][] angles = angleGet();

		double[][] distanceMatrix = hBondMatrixCalc(donorAcceptor);
		int[][] hBondMatrix = hBondMatrixIdentify(distanceMatrix,cutoff);

		//calculate the mean and variance
		double[] angleAvg = new double[angles.length];
		double[] angleVar = new double[angles.length];
		Double helixLength;

		for(int i = 0;i < angles.length;i++)
		{
			angleAvg[i] = mean(toDouble(angles[i]));
			angleVar[i] = var(toDouble(angles[i]),angleAvg[i]);
		}
		helixLength = helix.capHead.distance(helix.capTail);

		mean.add(angleAvg);
		var.add(angleVar);
		headToTail.add(helixLength);
		
		if(writeBifurcation)
		{
			hBondDistanceMatrixSeries.add(distanceMatrix);
		}
		
		hBondMatrixTimeSeries.add(hBondMatrix);

		//Seventh Seal
		//old version		
		//angleTimeSeries.add(matrixSwap(angles));

		angleTimeSeries.add(angles);
	}

	public void operation(String file) {

		if(doRMSD)
		{
			calcAndSaveRMSD(file,"RMSD.matrix");
		}
		//read the file line by line and put into Molecule
		try {
			FileInputStream IS = new FileInputStream(new File(file));
			BufferedReader DI = new BufferedReader(new InputStreamReader(IS));
			String flag = DI.readLine();
			
			//frame start from 1
			int counter = 1;

			//read and process
			for(String read = DI.readLine();read != null;read = DI.readLine())
			{
				//read until the end of the first frame
				if(read.equals(flag))
				{
					if(counter >= from)
					{
						this.analyze();
					}
					
					counter++;
					
					if(counter > to)
					{
						break;
					}
					//clean up the old data
					helix = new Molecule();
				}
				//if not hit the next frame, add the helix to the analyzer
				else
				{
					String[] info = cleanUp(read);

					int ATP = Integer.parseInt(info[5]);

					Atom newAtom;
					//Check for water
					if(ATP != 402 && ATP != 403)
					{
						Set<Integer> connection = new HashSet<Integer>();

						for(int i = 6;i < info.length;i++)
						{
							connection.add(Integer.parseInt(info[i]));
						}

						newAtom = new Atom(info[1],Integer.parseInt(info[0]),ATP,Double.parseDouble(info[2]),Double.parseDouble(info[3]),Double.parseDouble(info[4]),connection);
						this.helix.addAtom(newAtom);

						if(ATP == head)
						{
							helix.capHead = newAtom;
						}
						else if(ATP == tail)
						{
							helix.capTail = newAtom;
						}
					}

				}
			}

			//last frame
			if(counter <= to)
			{
				this.analyze();
			}

			hBondHotSpot = hBondHotSpot();
			hBondStat = hBondStat(hBondHotSpot);
			hydrogenBondPersistentCalc();
			persistenceStat();

			IS.close();
		} 
		catch (FileNotFoundException e) {
			System.out.println("No such file, please try again.");
			//e.printStackTrace();
		} 
		catch (EOFException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}

	}

	public static String[] cleanUp(String s){
		ArrayList<String> clean = new ArrayList<String>();

		String cutPut = s;
		StringTokenizer st = new StringTokenizer(cutPut);

		while(st.hasMoreElements())
		{
			clean.add(st.nextToken());
		}

		String[] output = new String[clean.size()];
		for(int i = 0;i < clean.size();i++)
		{
			output[i] = clean.get(i);
		}

		return output;
	}

	//classified and extract
	public void extraction()
	{
		this.donorAcceptor = new Atom[2][repeats+1];
		this.backBone = new ArrayList<Atom>();

		ArrayList<Atom> atoms = helix.atoms;
		int donorCount = 0;
		int acceptorCount = 0;

		HashSet<Integer> visited = new HashSet<Integer>();

		int currentATN = helix.capHead.atomNum - 1;
		//int terminate = helix.capTail.atomNum - 1;

		//walk through the backbone, and gather the donor and acceptor
		//Check weather the ATN is in the visited set, if not, go to it
		//if no unvisited place, break
		while(true)
		{
			Atom current = atoms.get(currentATN);
			int next = -1;
			for(int i : current.connection)
			{
				if(donor.contains(atoms.get(i-1).atomType))
				{
					donorAcceptor[0][donorCount] = atoms.get(i-1);
					donorCount++;
				}
				else if(acceptor.contains(atoms.get(i-1).atomType))
				{
					donorAcceptor[1][acceptorCount] = atoms.get(i-1);
					acceptorCount++;
				}
				else if(backBoneSet.contains(atoms.get(i-1).atomType) && 
						!visited.contains(atoms.get(i-1).atomNum - 1))
				{
					next = atoms.get(i-1).atomNum - 1;
					visited.add(current.atomNum - 1);
				}
			}

			backBone.add(helix.atoms.get(currentATN));

			if(next == -1)
			{
				break;
			}
			else
			{
				currentATN = next;
			}
		}
		
		/*for(int i = 0;i < donorAcceptor.length;i++)
		{
			for(int j = 0;j < donorAcceptor[i].length;j++)
			{
				System.out.println(donorAcceptor[i][j]);
			}
		}*/

	}

	public static void plotBar(double[] data)
	{
		Plot2DPanel hBondBar = new Plot2DPanel();
		hBondBar.addBarPlot("Histogram Statistics", data);

		JFrame frame = new JFrame("Histogram");
		frame.setSize(700, 700);
		frame.setContentPane(hBondBar);
		frame.setVisible(true);
	}

	//plot hotspots
	public static<T extends Number> void plotHotSpot(String[] corner, String[] horizontal, String[] vertical, T[][] hotSpotMatrix, int rows, int cols, T Max, String file, String title)
	{

		Grid grid = new Grid(rows, cols, corner, horizontal, vertical, hotSpotMatrix, Max);
		JFrame frame = new JFrame(title);
		frame.setSize((cols+1)*77, (rows+1)*77);
		frame.setContentPane(grid);
		frame.setVisible(true);

		grid.saveImage(file);
	}

	//plot psi phi
	public void reportPhiPsi()
	{
		double[] helix = new double[mean.size()];
		double[][] means = new double[mean.get(0).length][mean.size()];
		double[][] vars = new double[mean.get(0).length][mean.size()];
		double[] lengths = new double[mean.size()]; 

		for(int i = 0;i < mean.size();i++)
		{	
			helix[i] = i;
			lengths[i] = headToTail.get(i);
			for(int j = 0;j < mean.get(0).length;j++)
			{
				means[j][i] = mean.get(i)[j];
				vars[j][i] = var.get(i)[j];
			}
		}

		Plot2DPanel[] meanPlot = new Plot2DPanel[means.length];
		Plot2DPanel[] varPlot = new Plot2DPanel[vars.length];
		Plot2DPanel headToTailPlot;

		JFrame[] meanFrame = new JFrame[means.length];
		JFrame[] varFrame = new JFrame[means.length];
		JFrame headToTailFrame;

		for(int i = 0;i < means.length;i++)
		{
			meanPlot[i] = new Plot2DPanel();
			varPlot[i] = new Plot2DPanel();

			meanPlot[i].addLinePlot("mean " + i, helix, means[i]);
			varPlot[i].addLinePlot("var " + i, helix, vars[i]);

			meanFrame[i] = new JFrame("mean for angle " + i);
			varFrame[i] = new JFrame("var for angle " + i);

			meanFrame[i].setContentPane(meanPlot[i]);
			varFrame[i].setContentPane(varPlot[i]);

			meanFrame[i].setVisible(true);
			varFrame[i].setVisible(true);
		}

		headToTailPlot = new Plot2DPanel();
		headToTailPlot.addLinePlot("head to tail distance", helix, lengths);
		headToTailFrame = new JFrame("head to tail distance");
		headToTailFrame.setContentPane(headToTailPlot);
		headToTailFrame.setVisible(true);
	}

	//plot H bond
	public void reportHBond()
	{
		int daNum = 2;
		int rows = repeats + 1;
		int cols = repeats + 1;

		String[] thirteen = new String[repeats + 1];
		for(int i = 1;i <= repeats + 1;i++)
		{
			thirteen[i-1] = Integer.toString(i);
		}

		plotBar(castToDouble(hBondStat));

		plotHotSpot(new String[]{"donor","acptr"},
				thirteen,
				thirteen,
				toInteger(hBondHotSpot),
				rows,cols,
				hBondMatrixTimeSeries.size(),
				"H_Bond_Matrix.png",
				"Hydrogen Bonding Hot Spots");

		plotHotSpot(new String[]{"donor","acptr"},
				thirteen,
				thirteen,
				toDouble(avgHBondPersistent),
				rows,cols,
				100,
				"H_Bond_Persistence.png",
				"Hydrogen Bond Persistence Hot Spots");

		plotHotSpot(new String[]{"type","segs"},
				new String[]{"donor","acptr"},
				thirteen,
				toDouble(avgDonorAcceptorPersistent),
				daNum,cols,
				100,
				"Donor_Acceptor_Persistence.png",
				"Donor Acceptor Persistence Hot Spots");

		plotHotSpot(new String[]{"type","segs"},
				new String[]{"donor","acptr"},
				thirteen,
				toDouble(percentDecrease),
				daNum,cols,
				1,
				"Decrease_Traj.png",
				"Traj Decrease");

		/*for(int i = 0;i < hBondStat.length;i++)
		{
			System.out.println("+" + i + " H bond number: " + hBondStat[i] + "\n");
		}*/

	}

	//writings
	public void writeHBondInformations(boolean writeHBonds,boolean writeJaccard,boolean modifiedJaccard)
	{
		if(writeHBonds)
		{
			writeHBondMatrix("Hydrogen_Bond.matrix");
			writePersistent("Hydrogen_Bond_Persistent.matrix");
		}

		if(writeJaccard)
		{
			calcAndSaveJaccardMatrix("H_Bond_Jaccard.matrix",modifiedJaccard);
		}
	}

	public void writePersistent(String file)
	{
		try {
			File outputFile = new File(file);
			if(!outputFile.exists()) {
				try {
					outputFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			FileOutputStream OS = new FileOutputStream(new File(file),true);	
			PrintStream persistentWriting = new PrintStream(OS);

			persistentWriting.println("Persistent Matrix:");
			for(int i = 0;i < avgHBondPersistent.length;i++)
			{
				for(int j = 0;j < avgHBondPersistent[i].length;j++)
				{
					persistentWriting.print(avgHBondPersistent[i][j] + " ");
				}

				persistentWriting.println();
			}

			persistentWriting.println();

			persistentWriting.println("Donor Acceptor Persistence:");
			for(int i = 0;i < avgDonorAcceptorPersistent.length;i++)
			{
				for(int j = 0;j < avgDonorAcceptorPersistent[i].length;j++)
				{
					persistentWriting.print(avgDonorAcceptorPersistent[i][j] + " ");
				}

				persistentWriting.println();
			}

			persistentWriting.println();

			persistentWriting.println("Weighted:");
			for(int i = 0;i < avgWeightedDonorAcceptorPersistent.length;i++)
			{
				for(int j = 0;j < avgWeightedDonorAcceptorPersistent[i].length;j++)
				{
					persistentWriting.print(avgWeightedDonorAcceptorPersistent[i][j] + " ");
				}

				persistentWriting.println();
			}

			persistentWriting.println();

			persistentWriting.println("Donor Acceptor Trajectory Number:");
			for(int i = 0;i < donorAcceptorTrajNum.length;i++)
			{
				for(int j = 0;j < donorAcceptorTrajNum[i].length;j++)
				{
					persistentWriting.print(donorAcceptorTrajNum[i][j] + " ");
				}

				persistentWriting.println();
			}

			persistentWriting.println();

			persistentWriting.println("Donor Acceptor Trajectory Number Comparison:");
			for(int i = 0;i < donorAcceptorTrajNumComparison.length;i++)
			{
				for(int j = 0;j < donorAcceptorTrajNumComparison[i].length;j++)
				{
					persistentWriting.print(donorAcceptorTrajNumComparison[i][j] + " ");
				}

				persistentWriting.println();
			}

			persistentWriting.println();

			persistentWriting.println("Percent Decrease:");
			for(int i = 0;i < percentDecrease.length;i++)
			{
				for(int j = 0;j < percentDecrease[i].length;j++)
				{
					persistentWriting.print(percentDecrease[i][j] + " ");
				}

				persistentWriting.println();
			}

			persistentWriting.println();

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public void writePersistent(PrintStream persistentWriting)
	{
		persistentWriting.println("Persistent Matrix:");
		for(int i = 0;i < avgHBondPersistent.length;i++)
		{
			for(int j = 0;j < avgHBondPersistent[i].length;j++)
			{
				persistentWriting.print(avgHBondPersistent[i][j] + " ");
			}

			persistentWriting.println();
		}

		persistentWriting.println();

		persistentWriting.println("Donor Acceptor Persistence:");
		for(int i = 0;i < avgDonorAcceptorPersistent.length;i++)
		{
			for(int j = 0;j < avgDonorAcceptorPersistent[i].length;j++)
			{
				persistentWriting.print(avgDonorAcceptorPersistent[i][j] + " ");
			}

			persistentWriting.println();
		}

		persistentWriting.println();

		persistentWriting.println("Weighted:");
		for(int i = 0;i < avgWeightedDonorAcceptorPersistent.length;i++)
		{
			for(int j = 0;j < avgWeightedDonorAcceptorPersistent[i].length;j++)
			{
				persistentWriting.print(avgWeightedDonorAcceptorPersistent[i][j] + " ");
			}

			persistentWriting.println();
		}

		persistentWriting.println();

		persistentWriting.println("Donor Acceptor Trajectory Number:");
		for(int i = 0;i < donorAcceptorTrajNum.length;i++)
		{
			for(int j = 0;j < donorAcceptorTrajNum[i].length;j++)
			{
				persistentWriting.print(donorAcceptorTrajNum[i][j] + " ");
			}

			persistentWriting.println();
		}

		persistentWriting.println();

		persistentWriting.println("Donor Acceptor Trajectory Number Comparison:");
		for(int i = 0;i < donorAcceptorTrajNumComparison.length;i++)
		{
			for(int j = 0;j < donorAcceptorTrajNumComparison[i].length;j++)
			{
				persistentWriting.print(donorAcceptorTrajNumComparison[i][j] + " ");
			}

			persistentWriting.println();
		}

		persistentWriting.println();

		persistentWriting.println("Percent Decrease:");
		for(int i = 0;i < percentDecrease.length;i++)
		{
			for(int j = 0;j < percentDecrease[i].length;j++)
			{
				persistentWriting.print(percentDecrease[i][j] + " ");
			}

			persistentWriting.println();
		}

		persistentWriting.println();

	}

	public void writeHBondMatrix(String file)
	{
		try {
			File outputFile = new File(file);
			if(!outputFile.exists()) {
				try {
					outputFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			FileOutputStream OS = new FileOutputStream(new File(file),true);	
			PrintStream HBondWriting = new PrintStream(OS);

			for(int i = 0;i < hBondMatrixTimeSeries.size();i++)
			{
				HBondWriting.println("Matrix at frame " + (i+1));
				for(int j = 0;j < hBondMatrixTimeSeries.get(i).length;j++)
				{
					for(int k = 0;k < hBondMatrixTimeSeries.get(i)[j].length;k++)
					{
						HBondWriting.print(hBondMatrixTimeSeries.get(i)[j][k] + " ");
					}

					HBondWriting.println();
				}

				HBondWriting.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void writeAngles(String file)
	{
		try {
			File outputFile = new File(file);
			if(!outputFile.exists()) {
				try {
					outputFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 

			FileOutputStream OS = new FileOutputStream(new File(file),true);	
			PrintStream angleWriting = new PrintStream(OS);

			for(int i = 0;i < angleTimeSeries.size();i++)
			{
				angleWriting.println("Angles at frame " + (i+1) + ":");
				for(int j = 0;j < angleTimeSeries.get(i).length;j++)
				{
					for(int k = 0;k < angleTimeSeries.get(i)[j].length;k++)
					{
						angleWriting.print(angleTimeSeries.get(i)[j][k] + " ");
					}

					angleWriting.println();
				}

				angleWriting.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	public void writeAnglesNew(String file)
	{
		try {
			File outputFile = new File(file);
			if(!outputFile.exists()) {
				try {
					outputFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 

			FileOutputStream OS = new FileOutputStream(new File(file),true);	
			PrintStream angleWriting = new PrintStream(OS);

			for(int i = 0;i < angleTimeSeries.size();i++)
			{
				angleWriting.println("Angles at frame " + (i+1) + ":");

				ArrayList<Integer> dataSize = new ArrayList<Integer>();
				for(int j = 0;j < angleTimeSeries.get(i).length;j++)
				{
					dataSize.add(angleTimeSeries.get(i)[j].length);
				}

				int loopSize = Collections.max(dataSize);

				for(int j = 0;j < loopSize;j++)
				{
					for(int k = 0;k < taritari.length;k++)
					{
						if(angleTimeSeries.get(i)[k].length > j)
						{
							angleWriting.print(angleTimeSeries.get(i)[k][j] + " ");
						}
					}

					angleWriting.println();
				}

				angleWriting.println();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}

	//water down version of RMSD, not superposed
	public void calcAndSaveRMSD(String infile,String outfile)
	{
		ArrayList<Point[]> atomPositionList = new ArrayList<Point[]>();

		try {
			FileInputStream IS = new FileInputStream(new File(infile));
			BufferedReader DI = new BufferedReader(new InputStreamReader(IS));
			String flag = DI.readLine();

			ArrayList<Point> frame = new ArrayList<Point>();

			Point[] frameArray;
			//read and process
			for(String read = DI.readLine();read != null;read = DI.readLine())
			{
				if(read.equals(flag))
				{
					frameArray = new Point[frame.size()];
					frameArray = frame.toArray(frameArray);
					atomPositionList.add(frameArray);
					frame = new ArrayList<Point>();
				}
				//if not hit the next frame, add the atoms
				else
				{
					String[] info = cleanUp(read);

					//int ATP = Integer.parseInt(info[5]);
					//Check for Hydrogen
					if(!info[1].equals("H"))
					{
						frame.add(new Point(Double.parseDouble(info[2]),Double.parseDouble(info[3]),Double.parseDouble(info[4])));
					}

				}
			}

			frameArray = new Point[frame.size()];
			frameArray = frame.toArray(frameArray);
			atomPositionList.add(frameArray);
			/*for(int i = 0;i < frameArray.length;i++)
			{
				System.out.println(frameArray[i]);
			}*/

			IS.close();

			try {
				File outputFile = new File(outfile);
				if(!outputFile.exists()) {
					try {
						outputFile.createNewFile();
					} catch (IOException e) {
						e.printStackTrace();
					}
				} 
				FileOutputStream OS = new FileOutputStream(new File(outfile),true);	
				PrintStream RMSDWriting = new PrintStream(OS);

				int cfrom = Math.max(from - 1,0);
				int cto = Math.min(to,atomPositionList.size());

				double oneCellRMSDMatrix;

				for(int i = cfrom;i < cto;i++)
				{
					for(int j = cfrom;j < cto;j++)
					{
						oneCellRMSDMatrix = RMSD(atomPositionList.get(i),atomPositionList.get(j));
						RMSDWriting.print(oneCellRMSDMatrix + " ");
					}

					RMSDWriting.println();
				}

			} catch (FileNotFoundException e) {
				e.printStackTrace();
			}
		} 
		catch (FileNotFoundException e) {
			System.out.println("No such file, please try again.");
			//e.printStackTrace();
		} 
		catch (EOFException e)
		{
			e.printStackTrace();
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void calcAndSaveJaccardMatrix(String file,boolean modified)
	{
		try {
			File outputFile = new File(file);
			if(!outputFile.exists()) {
				try {
					outputFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			FileOutputStream OS = new FileOutputStream(new File(file),true);	
			PrintStream JaccardWriting = new PrintStream(OS);

			int size = hBondMatrixTimeSeries.size();

			double oneCellJaccardMatrix;

			for(int i = 0;i < size;i++)
			{
				for(int j = 0;j < size;j++)
				{
					oneCellJaccardMatrix = jaccardDist(hBondMatrixTimeSeries.get(i),hBondMatrixTimeSeries.get(j),modified);
					JaccardWriting.print(oneCellJaccardMatrix + " ");
				}

				JaccardWriting.println();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}

	}
	
	public void writeBifurcationInfo(String file,boolean donor,ArrayList<Integer> phase)
	{
		try {
			File outputFile = new File(file);
			if(!outputFile.exists()) {
				try {
					outputFile.createNewFile();
				} catch (IOException e) {
					e.printStackTrace();
				}
			} 
			FileOutputStream OS = new FileOutputStream(new File(file),true);	
			PrintStream BifurWriting = new PrintStream(OS);

			int size = hBondMatrixTimeSeries.size();

			for(int i = 0;i < size;i++)
			{
				for(int j = 0;j < phase.size();j++)
				{
					BifurWriting.print("Bond Phase " + phase.get(j) + ": ");
					if(donor)
					{
						for(int k = phase.get(phase.size() - 1) - 1;k <= repeats;k++)
						{
							BifurWriting.print(hBondDistanceMatrixSeries.get(i)[k][k - phase.get(j) + 1] + " ");
						}
					}
					else
					{
						for(int k = 0;k <= repeats - (phase.get(phase.size() - 1) - 1);k++)
						{
							BifurWriting.print(hBondDistanceMatrixSeries.get(i)[k + phase.get(j) - 1][k] + " ");
						}
					}
					BifurWriting.println();
				}

				BifurWriting.println();
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args)
	{
		double[] x1 = new double[]{0,3,4};
		double[] x2 = new double[]{1,1,1};
		double[] x3 = new double[]{2,1,2};
		double[] weight = new double[]{1,2,1};

		double var1 = weightedVar(toDouble(x1),toDouble(weight));
		double var2 = weightedVar(toDouble(x2),toDouble(weight));
		double var3 = weightedVar(toDouble(x3),toDouble(weight));

		System.out.println(var1 + " " + var2 + " " + var3);

	}

}
