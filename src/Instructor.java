import java.util.ArrayList;
import java.util.HashSet;

import javax.swing.JFrame;


public class Instructor extends Analyzer {
	
	public double[][] comparisonPersistent;
	public double[][] donorAcceptorTrajNumVar;
	
	public Instructor(int from, int to, int[] taritari, int repeats, int head, int tail, HashSet<Integer> donor, HashSet<Integer> acceptor, double cutoff, boolean doRMSD, boolean writeBifurcation)
	{
		super(from, to, taritari, repeats, head, tail, donor, acceptor, cutoff, doRMSD, writeBifurcation);
	}
	
	public Instructor(Molecule helix, int from, int to, int[] taritari, int repeats, int head, int tail, HashSet<Integer> donor, HashSet<Integer> acceptor, double cutoff, boolean doRMSD, boolean writeBifurcation)
	{
		super(helix, from, to, taritari, repeats, head, tail, donor, acceptor, cutoff, doRMSD, writeBifurcation);
	}
	
	public void persisAnalysisIni()
	{
		super.persisAnalysisIni();
		comparisonPersistent = new double[2][repeats+1];
		donorAcceptorTrajNumVar = castToDouble(zeroMatrix(2, repeats+1));
	}
	
	public void persistenceStat()
	{
		super.persistenceStat();
		
		for(int i = 0;i < repeats+1;i++)
    	{
			comparisonPersistent[0][i] = rowAvg(hydrogenBondPersistentSeries,i);
			comparisonPersistent[1][i] = colAvg(hydrogenBondPersistentSeries,i);
    	}
	}
	
	public double rowAvg(ArrayList[][] matrixList, int row)
	{
		int totalLength = 0;
		double sum = 0;
		
		for(int i = 0;i < matrixList[row].length;i++)
		{
			sum += sum(matrixList[row][i]);
			totalLength += matrixList[row][i].size();
		}
		
		if(totalLength == 0)
		{
			return 0;
		}
		
		return sum/totalLength;
	}
	
	public double colAvg(ArrayList[][] matrixList, int col)
	{
		int totalLength = 0;
		double sum = 0;
		
		for(int i = 0;i < matrixList.length;i++)
		{
			sum += sum(matrixList[i][col]);
			totalLength += matrixList[i][col].size();
		}
		
		if(totalLength == 0)
		{
			return 0;
		}
		
		return sum/totalLength;
	}
	
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
		
		plotComparison(new String[]{"type","segs"},
				new String[]{"donor","acptr"},
				thirteen,
				toDouble(percentDecrease),
				toDouble(sqrt(toDouble(donorAcceptorTrajNumVar))),
				daNum,cols,
				1,
				"Decrease_Traj.png",
				"Traj Decrease");
		
		plotComparison(new String[]{"type","segs"},
				new String[]{"donor","acptr"},
				thirteen,
				toDouble(avgWeightedDonorAcceptorPersistent),
				toDouble(comparisonPersistent),
				daNum,cols,
				100,
				"Donor_Acceptor_Comparison_Persistence.png",
				"Donor Acceptor Persistence Hot Spots(Comparison)");
		
		plotComparison(new String[]{"type","segs"},
				new String[]{"donor","acptr"},
				thirteen,
				toInteger(donorAcceptorTrajNum),
				toInteger(donorAcceptorTrajNumComparison),
				daNum,cols,
				hBondMatrixTimeSeries.size(),
				"Donor_Acceptor_Trajectory_Comparison_Persistence.png",
				"Donor Acceptor Trajectory Numbers(Comparison)");
	}
	
	public static<T extends Number> void plotComparison(String[] corner, String[] horizontal, String[] vertical, T[][] hotSpotMatrix, T[][] comparisonMatrix, int rows, int cols, T Max, String file, String title)
	{
		
		Grid grid = new Grid(rows, cols, corner, horizontal, vertical, hotSpotMatrix, comparisonMatrix, Max);
		JFrame frame = new JFrame(title);
		frame.setSize((cols+1)*77, (rows+1)*77);
		frame.setContentPane(grid);
		frame.setVisible(true);
		
		grid.saveImage(file);
	}
	
	public void groupMeeting(Analyzer[] researchGroup, int repeats)
    {
    	//H Bond plots
    	int[][] sumHotSpots = zeroMatrix(repeats+1, repeats+1);
    	int[] sumStat = zeroArray(repeats+1);
    	
    	for(int i = 0;i < researchGroup.length;i++)
    	{
    		this.hBondMatrixTimeSeries.addAll(researchGroup[i].hBondMatrixTimeSeries);
    		this.hBondDistanceMatrixSeries.addAll(researchGroup[i].hBondDistanceMatrixSeries);
    		this.angleTimeSeries.addAll(researchGroup[i].angleTimeSeries);
    		for(int j = 0;j < repeats+1;j++)
    		{
    			sumStat[j] += researchGroup[i].hBondStat[j];
    			for(int k = 0;k < repeats+1;k++)
    			{
    				sumHotSpots[j][k] += researchGroup[i].hBondHotSpot[j][k];
    			}
    		}
    		
    	}
    	
    	this.hBondStat = sumStat;
    	this.hBondHotSpot = sumHotSpots;
    	
    	//persistence analysis
    	this.persisAnalysisIni();
    	
    	Double[][][] trajDecrease = new Double[2][repeats+1][researchGroup.length];
    	Double[][][] weight = new Double[2][repeats+1][researchGroup.length];
    	
    	for(int i = 0;i < researchGroup.length;i++)
    	{
    		for(int j = 0;j < repeats+1;j++)
    		{
    			for(int k = 0;k < repeats+1;k++)
    			{
    				this.hydrogenBondPersistentSeries[j][k].addAll(researchGroup[i].hydrogenBondPersistentSeries[j][k]);
    			}
    		}
    		
    		for(int j = 0;j < 2;j++)
    		{
    			for(int k = 0;k < repeats+1;k++)
    			{
    				this.donorAcceptorPersistentSeries[j][k].addAll(researchGroup[i].donorAcceptorPersistentSeries[j][k]);
    				
    				this.weightedDonorAcceptorPersistentSeries[j][k].addAll(researchGroup[i].weightedDonorAcceptorPersistentSeries[j][k]);
    			}
    		}
    		
    		for(int j = 0;j < 2;j++)
    		{
    			for(int k = 0;k < repeats+1;k++)
    			{
    				weight[j][k][i] = Double.valueOf(researchGroup[i].donorAcceptorTrajNumComparison[j][k]);
    				trajDecrease[j][k][i] = Double.valueOf(researchGroup[i].percentDecrease[j][k]);
    			}
    		}
    		
    	}
    	
    	for(int i = 0;i < 2;i++)
    	{
    		for(int j = 0;j < repeats+1;j++)
    		{
    			donorAcceptorTrajNumVar[i][j] = weightedVar(trajDecrease[i][j],weight[i][j]);
    		}
    	}
    	
    	this.persistenceStat();
    }
}
