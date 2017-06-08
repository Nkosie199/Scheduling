package simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author gmdnko003
 */
public class ProcessControlBlockImpl implements ProcessControlBlock {
//    private static ProcessControlBlock pcb;
    private String programName;
    public static int PIDcounter = 0;
    private int PID;
    private int priority;
    private State state = null;
    private List<Instruction> instructions;
//    private static List<Instruction> instructions = new ArrayList<>();
    private int instructionIndex = 0;
    private Instruction instruction = null;
    //private static Instruction nextInstruction;
    
    public ProcessControlBlockImpl(String programName, List<Instruction> instructions){
        this.programName =  programName;
        this.PIDcounter = this.PIDcounter+1;
        this.PID = PIDcounter;
        this.instructions = instructions;
//        System.out.println("PCBImpl debug print instructions: "+instructions);
    }
        
    @Override
    public int getPID() {
        return this.PID;
    }

    @Override
    public String getProgramName() {
        return this.programName;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public int setPriority(int value) {
        this.priority = value;
        return this.priority;
    }

    @Override
    public Instruction getInstruction() {
        return this.instructions.get(this.instructionIndex);
    }

    @Override
    public boolean hasNextInstruction() {
//        this.instructionIndex = instructionIndex+1; //increment the instruction index
//        System.out.println("PCBImpl debug print instruction index: "+instructionIndex);
        if (this.instructions.size() > this.instructionIndex+1){ // has next instruction
            return true;
        }
        else{ // does not have next instruction
            return false;
        }
    }

    @Override
    public void nextInstruction() { 
        this.instructionIndex = this.instructionIndex+1; 
        this.instruction = this.instructions.get(this.instructionIndex);
    }

    @Override
    public State getState() {
        return this.state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    // going to be called twice by programs A & B
    public static ProcessControlBlock loadProgram(String filename) throws FileNotFoundException, IOException{
        ProcessControlBlock pcb;
        List<Instruction> instructions = new ArrayList<>();
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        // for each line in the program
        while (line != null){
            //System.out.println(line);
            String[] lineElements = line.split(" ");
            if (lineElements.length == 2){ // CPU instruction
                // create CPU device
                String deviceType = lineElements[0];
                
                int burstTime = Integer.parseInt(lineElements[1]);
                // create CPU process
                Instruction instr = new CPUInstruction(burstTime);
//                System.out.println("PCBImpl: Debug print CPU instruction = "+instruction);
                instructions.add(instr);        
            }
            else if (lineElements.length == 3){ // IO instruction
                // create IO device
                String deviceType = lineElements[0];

                int burstTime = Integer.parseInt(lineElements[1]);
                int deviceID = Integer.parseInt(lineElements[2]);
                // create IO process
                Instruction instr = new IOInstruction(burstTime, deviceID);
//                System.out.println("PCBImpl: Debug print IO instruction = "+instruction);
                instructions.add(instr);
            }
            line = br.readLine();
        }
        br.close();
        fr.close();
        pcb = new ProcessControlBlockImpl(filename, instructions);
//        pcb.setPriority(0);
        pcb.setState(ProcessControlBlock.State.READY);
        return pcb;
    }
    
    // going to be called twice by programs A & B
    // this method splits instructions into slices, where noOfSlices = burstTime/sliceTime  
    public static ProcessControlBlock loadProgram2(String filename, int sliceTime) throws FileNotFoundException, IOException{
        ProcessControlBlock pcb;
        List<Instruction> instructions = new ArrayList<>();
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        // for each line in the program
        while (line != null){
            //System.out.println(line);
            String[] lineElements = line.split(" ");
            if (lineElements.length == 2){ // CPU instruction
                // create CPU device
                String deviceType = lineElements[0];
                
                int burstTime = Integer.parseInt(lineElements[1]);
                if (burstTime > sliceTime){
                    int noOfSlices = burstTime/sliceTime;
                    for (int i=0; i<noOfSlices; i++){
                        // create CPU process
                        Instruction instr = new CPUInstruction(burstTime/noOfSlices);
                        System.out.println("PCBImpl: Debug print CPU instruction = "+instr);                       
                        instructions.add(instr);  
                    }
                }
                else{
                    // create CPU process
                    Instruction instr = new CPUInstruction(burstTime);
                    System.out.println("PCBImpl: Debug print CPU instruction = "+instr);                    
                    instructions.add(instr);  
                }              
            }
            else if (lineElements.length == 3){ // IO instruction
                // create IO device
                String deviceType = lineElements[0];

                int burstTime = Integer.parseInt(lineElements[1]);
                int deviceID = Integer.parseInt(lineElements[2]);
                
                if (burstTime > sliceTime){
                    int noOfSlices = burstTime/sliceTime;
                    for (int i=0; i<noOfSlices; i++){
                        // create CPU process
                        Instruction instr = new IOInstruction(burstTime/noOfSlices, deviceID);
                        System.out.println("PCBImpl: Debug print CPU instruction = "+instr);                       
                        instructions.add(instr);  
                    }
                }
                else{
                    // create IO process
                    Instruction instr = new IOInstruction(burstTime, deviceID);
                    System.out.println("PCBImpl: Debug print IO instruction = "+instr);
                    instructions.add(instr);                    
                }
            }
            line = br.readLine();
        }
        br.close();
        fr.close();
        pcb = new ProcessControlBlockImpl(filename, instructions);
//        pcb.setPriority(0);
        pcb.setState(ProcessControlBlock.State.READY);
        return pcb;
    }
    
    
    public String toString(){
        return "process(pid="+this.getPID()+", state="+this.getState()+", name=\""+this.getProgramName()+"\")";
    }
    
}
