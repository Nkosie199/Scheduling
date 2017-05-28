package programgenerator;
<<<<<<< HEAD

=======
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
//
import simulator.Instruction;
/**
 * Write a description of class ProgramGenerator here.
 * 
<<<<<<< HEAD
 * @author Nkosi Gumede
 * @version 28/5/2017
=======
<<<<<<< HEAD
 * @author (your name) 
 * @version (a version number or a date)
=======
 * @author Nkosi Gumede 
 * @version 26/5/2017
>>>>>>> 0d2a49eabd93494734b0d375ade47ee0c1992da2
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
 */
public class ProgramGenerator {
    private CPUGenerator cpuGen;
    private IOGenerator ioGen;

    private String name;    
    private final List<Instruction> instructions; 
    private int totalTime;
    
    /**
     * @Param numInstructions - an odd number integer.
     */
    public ProgramGenerator(String name, CPUGenerator cpuGen, IOGenerator ioGen, int numInstructions) {
<<<<<<< HEAD
        // make sure that there is an odd number of instructions
        assert(numInstructions%2==1);
        // initialize all my private variables
=======
        assert(numInstructions%2==1);

>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
        this.name=name;
        this.totalTime=0;
        this.cpuGen=cpuGen;
        this.ioGen=ioGen;
        this.instructions=new ArrayList<Instruction>(); 
<<<<<<< HEAD
        // run make method
=======

>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
        make(cpuGen);
        for (int i=1; i<numInstructions; i=i+2) {
            make(ioGen);
            make(cpuGen);
        }
    }
<<<<<<< HEAD
=======
            
    public int getTotalTime() { return this.totalTime; }
    public int getSize() { return instructions.size(); }
    public String getName() { return this.name; }
    
    private void make(InstructionGenerator instGen) {
        Instruction instruction = instGen.make();
        instructions.add(instruction);
        totalTime=totalTime+instruction.getDuration();
    }
    
    public void make() {
        make(ioGen);
        make(cpuGen);
    }
    
    public List<String> getText() {
        final List<String> result = new ArrayList<String>();
        result.add("# Program name: "+this.getName());
        result.add("# "+cpuGen);
        result.add("# "+ioGen);
        for(Instruction instruction : instructions) {
            result.add(instruction.toString());
        }
        return result;
    }
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
    
    public static void main(String args[]) {
        try {
            final Scanner in = new Scanner(System.in);
<<<<<<< HEAD
            // get CPU bounds
=======

>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
            System.out.print("CPU Bounds (lambda, min burst, max burst): ");
            final Scanner cpuBounds = new Scanner(in.nextLine());
            cpuBounds.useDelimiter("\\s*,\\s*");
            final CPUGenerator cpuGen = new CPUGenerator(new BoundedExpRNG(cpuBounds.nextDouble()), cpuBounds.nextInt(), cpuBounds.nextInt());
<<<<<<< HEAD
            // get IO bounds
            System.out.print("IO Bounds (lambda, min burst, max burst): ");
            final Scanner ioBounds = new Scanner(in.nextLine());
            ioBounds.useDelimiter("\\s*,\\s*");
            // get device identifiers
=======

            System.out.print("IO Bounds (lambda, min burst, max burst): ");
            final Scanner ioBounds = new Scanner(in.nextLine());
            ioBounds.useDelimiter("\\s*,\\s*");
            
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
            System.out.print("Device identifiers (<integer>, ..., <integer>): ");
            final Scanner deviceIDs = new Scanner(in.nextLine());
            deviceIDs.useDelimiter("\\s*,\\s*");
            final List<Integer> deviceList = new ArrayList<Integer>();
            while (deviceIDs.hasNextInt()) { deviceList.add(deviceIDs.nextInt()); }
            
            final IOGenerator ioGen = new IOGenerator(new BoundedExpRNG(ioBounds.nextDouble()), deviceList.toArray(new Integer[0]), ioBounds.nextInt(), ioBounds.nextInt());
<<<<<<< HEAD
            // get number of instructions
            System.out.print("Length (number of instructions - must be odd number): ");
            final int length = in.nextInt();
            in.nextLine();
            // get program name
            System.out.print("Program name: ");
            final String name = in.nextLine();
            System.out.println(); 
            
            // Hold your breath, here we go...
            final ProgramGenerator progGen = new ProgramGenerator(name, cpuGen, ioGen, length);
            // print results to the program name specified
=======
            
            System.out.print("Length (number of instructions - must be odd number): ");
            final int length = in.nextInt();
            in.nextLine();
            
            System.out.print("Program name: ");
            final String name = in.nextLine();
            System.out.println(); 
            // Hold your breath, here we go...
            final ProgramGenerator progGen = new ProgramGenerator(name, cpuGen, ioGen, length);
            
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
            final BufferedWriter writer = new BufferedWriter(new FileWriter(name));
            for(String line : progGen.getText()) {
                System.out.println(line);
                writer.write(line+"\n");
            }
            writer.close();
        }
        catch (Exception Excep){
            System.out.println("Oops, something broke.");
        }
    }
<<<<<<< HEAD
            
    public int getTotalTime() { return this.totalTime; }
    public int getSize() { return instructions.size(); }
    public String getName() { return this.name; }
    
    private void make(InstructionGenerator instGen) {
        Instruction instruction = instGen.make();
        instructions.add(instruction);
        totalTime=totalTime+instruction.getDuration();
    }
    
    public void make() {
        make(ioGen);
        make(cpuGen);
    }
    
    public List<String> getText() {
        final List<String> result = new ArrayList<String>();
        result.add("# Program name: "+this.getName());
        result.add("# "+cpuGen);
        result.add("# "+ioGen);
        for(Instruction instruction : instructions) {
            result.add(instruction.toString());
        }
        return result;
    }
    
=======
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
}
