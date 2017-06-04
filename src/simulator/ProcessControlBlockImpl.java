package simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 *
 * @author gmdnko003
 */
public class ProcessControlBlockImpl implements ProcessControlBlock {
    
    private static String programName;
    private static int PID = 0;
    private static int priority = 0;
    private static State state;
    private static List<Instruction> instructions;
//    private static List<Instruction> instructions = new ArrayList<>();
    private static int instructionIndex = 0;
    private static Instruction instruction = null;
    //private static Instruction nextInstruction;
    
    public ProcessControlBlockImpl(String programName, int priority, State state, List<Instruction> instructions){
        this.programName =  programName;
        this.PID = PID+1;
        this.priority = priority;
        this.state = state;
        this.instructions = instructions;
//        System.out.println("PCBImpl debug print instructions: "+instructions);
    }
        
    @Override
    public int getPID() {
        return PID;
    }

    @Override
    public String getProgramName() {
        return programName;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public int setPriority(int value) {
        priority = value;
        return priority;
    }

    @Override
    public Instruction getInstruction() {
        return instructions.get(instructionIndex);
    }

    @Override
    public boolean hasNextInstruction() {
        this.instructionIndex = instructionIndex+1; //increment the instruction index
//        System.out.println("PCBImpl debug print instruction index: "+instructionIndex);
        if (instructions.size() > instructionIndex){ // has next instruction
            return true;
        }
        else{ // does not have next instruction
            return false;
        }
    }

    @Override
    public void nextInstruction() { 
//        instructionIndex = instructionIndex+1; 
        instruction = instructions.get(instructionIndex);
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public void setState(State state) {
        this.state = state;
    }

    public static ProcessControlBlock loadProgram(String filename) throws FileNotFoundException, IOException{
        ProcessControlBlock pcb = null;
        instructions = new ArrayList<>();
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
                instruction = new CPUInstruction(burstTime);
//                System.out.println("PCBImpl: Debug print CPU instruction = "+instruction);
                instructions.add(instruction);        
            }
            else if (lineElements.length == 3){ // IO instruction
                // create IO device
                String deviceType = lineElements[0];

                int burstTime = Integer.parseInt(lineElements[1]);
                int deviceID = Integer.parseInt(lineElements[2]);
                // create IO process
                instruction = new IOInstruction(burstTime, deviceID);
//                System.out.println("PCBImpl: Debug print IO instruction = "+instruction);
                instructions.add(instruction);
            }
            line = br.readLine();
        }
        br.close();
        fr.close();
        pcb = new ProcessControlBlockImpl(filename, priority, state.READY, instructions);
        return pcb;
    }
    
    public String toString(){
        return "process(pid="+this.getPID()+", state="+this.getState()+", name=\""+this.getProgramName()+"\")";
    }
    
}
