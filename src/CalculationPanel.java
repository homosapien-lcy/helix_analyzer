import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.StringTokenizer;
import java.awt.Checkbox;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

import javax.swing.JEditorPane;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;
import javax.swing.JCheckBox;

/**
 * @author homosapien
 *
 */
public class CalculationPanel {

	private JFrame frame;
	//private JTextField txtFileNames;
	private JEditorPane txtFileNames;
	private JTextField txtBackboneAtomTypes;
	private JTextField txtNumberOfRepeats;
	private JTextField txtBackboneHeadCappingGroup;
	private JTextField txtBackboneTailCappingGroup;
	private JTextField txtdonors;
	private JTextField txtacceptors;
	private JTextField txtHBondCutOff;
	private JTextField txtBondPhase;
	private JTextField txtFrom;
	private JTextField txtTo;
	private JScrollPane scrollPane_1;
	private JCheckBox write_H_Bond;
	private JCheckBox write_Jaccard;
	private JCheckBox RMSD;
	private JCheckBox modified_Jaccard;
	private JCheckBox display_H_Bonds;
	private JCheckBox trajectory_Only;
	private JCheckBox Bifurcation;
	private JCheckBox Donor;
	private JCheckBox WriteAngles;
	private JCheckBox NewFormat;
	
	private ArrayList<Instructor> conference;

	//private JTextField txtFileHead;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					CalculationPanel window = new CalculationPanel();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public CalculationPanel() {
		conference = new ArrayList<Instructor>();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 889, 612);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		scrollPane_1 = new JScrollPane();
		scrollPane_1.setBounds(47, 30, 382, 132);
		frame.getContentPane().add(scrollPane_1);

		txtFileNames = new JEditorPane();
		scrollPane_1.setViewportView(txtFileNames);
		//txtFileName.setText("File Name");
		txtFileNames.setText("Structure_Sample/12_BetaAlanine_Alpha+4_extract.arc");

		JScrollPane scrollPane = new JScrollPane();
		scrollPane_1.setColumnHeaderView(scrollPane);

		//txtFileNames = new JTextField();
		//txtFileName.setText("File Name");
		//txtFileNames.setText("12_BetaAlanine_Alpha+4_extract.arc");
		//txtFileNames.setBounds(47, 113, 382, 25);
		//frame.getContentPane().add(txtFileNames);
		//txtFileNames.setColumns(10);

		txtBackboneAtomTypes = new JTextField();
		//txtBackboneAtomTypes.setText("Backbone Atom Types, Seperate by space");
		txtBackboneAtomTypes.setText("501 502 503 504");//501 502 503 504 for beta alanine
		//1 2 3 for glycine
		//7 8 9 for alanine
		txtBackboneAtomTypes.setBounds(47, 194, 382, 25);
		frame.getContentPane().add(txtBackboneAtomTypes);
		txtBackboneAtomTypes.setColumns(10);

		txtNumberOfRepeats = new JTextField();
		//txtNumberOfRepeats.setText("Number of Repeats in the helix");
		txtNumberOfRepeats.setText("12");
		txtNumberOfRepeats.setBounds(47, 258, 382, 25);
		frame.getContentPane().add(txtNumberOfRepeats);
		txtNumberOfRepeats.setColumns(10);

		txtBackboneHeadCappingGroup = new JTextField();
		//txtBackboneHeadCappingGroup.setText("Backbone Head Capping Group Atom Type (The one after the methyl group)");
		txtBackboneHeadCappingGroup.setText("226");
		txtBackboneHeadCappingGroup.setBounds(47, 325, 382, 25);
		frame.getContentPane().add(txtBackboneHeadCappingGroup);
		txtBackboneHeadCappingGroup.setColumns(10);

		txtBackboneTailCappingGroup = new JTextField();
		//txtBackboneTailCappingGroup.setText("Backbone Tail Capping Group Atom Type (The one before the methyl group)");
		txtBackboneTailCappingGroup.setText("230");
		txtBackboneTailCappingGroup.setBounds(47, 394, 382, 25);
		frame.getContentPane().add(txtBackboneTailCappingGroup);
		txtBackboneTailCappingGroup.setColumns(10);

		txtdonors = new JTextField();
		txtdonors.setText("505 231");//505 231 for beta alanine
		//4 231 for glycine
		//10 231 for alanine
		txtdonors.setBounds(544, 80, 226, 25);
		frame.getContentPane().add(txtdonors);
		txtdonors.setColumns(10);

		txtacceptors = new JTextField();
		txtacceptors.setText("506 227");//506 227 for beta alanine
		//5 227 for glycine
		//11 227 for alanine
		txtacceptors.setBounds(544, 153, 226, 22);
		frame.getContentPane().add(txtacceptors);
		txtacceptors.setColumns(10);

		txtHBondCutOff = new JTextField();
		txtHBondCutOff.setText("2.5");
		txtHBondCutOff.setBounds(544, 220, 226, 25);
		frame.getContentPane().add(txtHBondCutOff);
		txtHBondCutOff.setColumns(10);
		
		txtBondPhase = new JTextField();
		txtBondPhase.setBounds(740, 328, 114, 19);
		frame.getContentPane().add(txtBondPhase);
		txtBondPhase.setColumns(10);
		
		txtFrom = new JTextField();
		txtFrom.setText("from");
		txtFrom.setBounds(546, 261, 114, 19);
		frame.getContentPane().add(txtFrom);
		txtFrom.setColumns(10);
		
		txtTo = new JTextField();
		txtTo.setText("to");
		txtTo.setBounds(682, 261, 114, 19);
		frame.getContentPane().add(txtTo);
		txtTo.setColumns(10);

		RMSD = new JCheckBox("RMSD");
		RMSD.setBounds(544, 353, 226, 41);
		frame.getContentPane().add(RMSD);

		modified_Jaccard = new JCheckBox("Modified Jaccard");
		modified_Jaccard.setBounds(544, 386, 226, 41);
		frame.getContentPane().add(modified_Jaccard);

		write_H_Bond = new JCheckBox("Write Hydrogen Bond Informations");
		write_H_Bond.setBounds(544, 453, 310, 41);
		frame.getContentPane().add(write_H_Bond);

		write_Jaccard = new JCheckBox("Write Jaccard");
		write_Jaccard.setBounds(544, 420, 226, 41);
		frame.getContentPane().add(write_Jaccard);

		display_H_Bonds = new JCheckBox("Display Hydrogen Bonds");
		display_H_Bonds.setBounds(544, 532, 275, 23);
		frame.getContentPane().add(display_H_Bonds);

		trajectory_Only = new JCheckBox("Write Trajectory Only");
		trajectory_Only.setBounds(544, 498, 275, 23);
		frame.getContentPane().add(trajectory_Only);
		
		Bifurcation = new JCheckBox("Bifurcation");
		Bifurcation.setBounds(544, 326, 102, 23);
		frame.getContentPane().add(Bifurcation);
		
		Donor = new JCheckBox("Donor");
		Donor.setBounds(650, 326, 68, 23);
		frame.getContentPane().add(Donor);
		
		WriteAngles = new JCheckBox("Write Angles");
		WriteAngles.setBounds(544, 289, 116, 23);
		frame.getContentPane().add(WriteAngles);
		
		NewFormat = new JCheckBox("Vertical Format");
		NewFormat.setBounds(667, 289, 144, 23);
		frame.getContentPane().add(NewFormat);

		JButton btnStartAnalysis = new JButton("Start Analysis");
		btnStartAnalysis.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {

				//String fileHead = txtFileHead.getText();
				int repeats = Integer.parseInt(txtNumberOfRepeats.getText());
				int head = Integer.parseInt(txtBackboneHeadCappingGroup.getText());
				int tail = Integer.parseInt(txtBackboneTailCappingGroup.getText());
				//double cutoff = Double.parseDouble(HBondCutOff.getText());
				
				int from;
				int to;
				
				try{
					from = Integer.parseInt(txtFrom.getText());
					to = Integer.parseInt(txtTo.getText());
				}
				//default: all frames
				catch(NumberFormatException e) { 
			        from = 1;
			        to = Integer.MAX_VALUE;
			    }

				boolean doRMSD = RMSD.isSelected();
				boolean writeJaccard = write_Jaccard.isSelected();
				boolean modifiedJaccard = modified_Jaccard.isSelected();
				boolean writeHBond = write_H_Bond.isSelected();
				boolean displayHBonds = display_H_Bonds.isSelected();
				boolean trajectoryOnly = trajectory_Only.isSelected();
				boolean writeBifurcation = Bifurcation.isSelected();
				boolean donorBifurcation = Donor.isSelected();
				boolean writeAngles = WriteAngles.isSelected();
				boolean newFormat = NewFormat.isSelected();

				String fileNames = txtFileNames.getText();
				String tari = txtBackboneAtomTypes.getText();
				String donor = txtdonors.getText();
				String acceptor = txtacceptors.getText();
				String cutoff = txtHBondCutOff.getText();

				ArrayList<Integer> taritaritari = new ArrayList<Integer>();
				ArrayList<Integer> donorsL = new ArrayList<Integer>();
				ArrayList<Integer> acceptorsL = new ArrayList<Integer>();
				ArrayList<String> fileNamesL = new ArrayList<String>();
				ArrayList<Double> cutoffL = new ArrayList<Double>();

				StringTokenizer fileSt = new StringTokenizer(fileNames);
				StringTokenizer tariSt = new StringTokenizer(tari);
				StringTokenizer doSt = new StringTokenizer(donor);
				StringTokenizer acSt = new StringTokenizer(acceptor);
				StringTokenizer cutoffSt = new StringTokenizer(cutoff);

				while(fileSt.hasMoreElements())
				{
					fileNamesL.add(fileSt.nextToken());
				}
				while(tariSt.hasMoreElements())
				{
					taritaritari.add(Integer.parseInt(tariSt.nextToken()));
				}
				while(doSt.hasMoreElements())
				{
					donorsL.add(Integer.parseInt(doSt.nextToken()));
				}
				while(acSt.hasMoreElements())
				{
					acceptorsL.add(Integer.parseInt(acSt.nextToken()));
				}
				while(cutoffSt.hasMoreElements())
				{
					cutoffL.add(Double.parseDouble(cutoffSt.nextToken()));
				}

				int[] taritari = new int[taritaritari.size()];
				for(int i = 0;i < taritaritari.size();i++)
				{
					taritari[i] = taritaritari.get(i);
				}
				HashSet<Integer> acceptorSet = new HashSet<Integer>(acceptorsL);
				HashSet<Integer> donorSet = new HashSet<Integer>(donorsL);

				if(cutoffL.size() > 2)
				{
					System.out.println("Doesn't support more than 2 file right not");
					return;
				}

				for(int i = 0;i < cutoffL.size();i++)
				{
					//analysis
					Analyzer[] researchGroup = new Analyzer[fileNamesL.size()];

					for(int j = 0;j < researchGroup.length;j++)
					{
						researchGroup[j] = new Analyzer(from,to,taritari,repeats,head,tail,donorSet,acceptorSet,cutoffL.get(i),doRMSD,writeBifurcation);
						researchGroup[j].operation(fileNamesL.get(j));
						
						//First Seal
						//researchGroup[i].reportPhiPsi();
						//researchGroup[i].reportHBond();
						//researchGroup[j].writeHBondInformations(writeHBond,writeJaccard,modifiedJaccard);
						
						//Second Seal
						//researchGroup[i].writeAngles("angle.series");
						//researchGroup[j].writeAnglesNew("angle.series");

					}

					boolean graduated = true;

					//start instruction interating everything
					Instructor instructor = new Instructor(1,Integer.MAX_VALUE,taritari,repeats,head,tail,donorSet,acceptorSet,cutoffL.get(i),doRMSD,writeBifurcation);
					instructor.groupMeeting(researchGroup, repeats);

					if(cutoffL.size() == 1)
					{
						if(displayHBonds)
						{
							instructor.reportHBond();
						}

						instructor.writeHBondInformations(writeHBond,writeJaccard,modifiedJaccard);

						if(trajectoryOnly)
						{
							writeTraj(researchGroup,instructor,"Trajectory.summary");
						}
					}

					if(graduated)
					{
						for(int j = 0;j < researchGroup.length;j++)
						{
							researchGroup[j] = null;
						}
					}

					conference.add(instructor);
				}

				if(cutoffL.size() == 2 && displayHBonds)
				{
					int daNum = 2;
					int rows = repeats + 1;
					int cols = repeats + 1;

					String[] thirteen = new String[repeats + 1];
					for(int i = 1;i <= repeats + 1;i++)
					{
						thirteen[i-1] = Integer.toString(i);
					}

					Instructor.plotComparison(new String[]{"donor","acptr"},
							thirteen,
							thirteen,
							Analyzer.toInteger(conference.get(0).hBondHotSpot),
							Analyzer.toInteger(conference.get(1).hBondHotSpot),
							rows,cols,
							conference.get(0).hBondMatrixTimeSeries.size(),
							"H_Bond_Comparison.png",
							"Hydrogen Bond Hot Spots Comparison");
					
					Instructor.plotComparison(new String[]{"donor","acptr"},
							thirteen,
							thirteen,
							Analyzer.toDouble(conference.get(0).avgHBondPersistent),
							Analyzer.toDouble(conference.get(1).avgHBondPersistent),
							rows,cols,
							100,
							"H_Bond_Persistence_Comparison.png",
							"Hydrogen Bond Persistence Hot Spots Comparison");
					
					Instructor.plotComparison(new String[]{"type","segs"},
							new String[]{"donor","acptr"},
							thirteen,
							Analyzer.toDouble(conference.get(0).avgDonorAcceptorPersistent),
							Analyzer.toDouble(conference.get(1).avgDonorAcceptorPersistent),
							daNum,cols,
							100,
							"Donor_Acceptor_Persistence_Comparison.png",
							"Donor Acceptor Persistence Hot Spots Comparison");
				}
				
				if(writeBifurcation)
				{
					StringTokenizer phaseSt = new StringTokenizer(txtBondPhase.getText());
					ArrayList<Integer> phaseL = new ArrayList<Integer>();
					while(phaseSt.hasMoreElements())
					{
						phaseL.add(Integer.parseInt(phaseSt.nextToken()));
					}
					
					//phase should be enter in order!
					conference.get(0).writeBifurcationInfo("Bifurcation.summary",donorBifurcation,phaseL);
				}
				
				if(writeAngles)
				{
					if(newFormat)
					{
						conference.get(0).writeAnglesNew("Angles.summary");
					}
					else
					{
						conference.get(0).writeAngles("Angles.summary");
					}
				}

				conference = new ArrayList<Instructor>();

			}
		});
		btnStartAnalysis.setBounds(47, 472, 170, 40);
		frame.getContentPane().add(btnStartAnalysis);

	}

	public void writeTraj(Analyzer[] researchGroup,Instructor instructor,String file)
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

			for(int i = 0;i < researchGroup.length;i++)
			{
				persistentWriting.println("In Trajector " + (i+1) + ":");
				researchGroup[i].writePersistent(persistentWriting);
			}

			persistentWriting.println("In summary");
			instructor.writePersistent(persistentWriting);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
}