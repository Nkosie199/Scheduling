package simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
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
    private static List<Instruction> instructions = new LinkedList<>();
    private static Instruction instruction;
    private static Instruction nextInstruction = null;
    
    public ProcessControlBlockImpl(String programName, int priority, State state, Instruction instruction){
        this.programName =  programName;
        this.PID = this.PID++;
        this.priority = priority;
        this.state = state;
        this.instruction = instruction;
        
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
        return instruction;
    }

    @Override
    public boolean hasNextInstruction() {
        if (instructions.isEmpty()){ // does not have next instruction
            return false;
        }
        else{ // has next instruction
            return false;
        }
    }

    @Override
    public void nextInstruction() {
        
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
        FileReader fr = new FileReader(filename);
        BufferedReader br = new BufferedReader(fr);
        String line = br.readLine();
        // for each line in the program
        while (line != null){
            //System.out.println(line);
            String[] lineElements = line.split(" ");
            if (lineElements.length == 2){ // CPU instruction
                
                String deviceType = lineElements[0];
                int burstTime = Integer.parseInt(lineElements[1]);
                // create CPU process
                instruction = new CPUInstruction(burstTime);
                System.out.println("PCBImpl: Debug print CPU instruction = "+instruction);
                pcb = new ProcessControlBlockImpl(filename, priority, state.READY, instruction);
            }
            else if (lineElements.length == 3){ // IO instruction
                
                String deviceType = lineElements[0];
                int burstTime = Integer.parseInt(lineElements[1]);
                int deviceID = Integer.parseInt(lineElements[2]);
                // create IO process
                instruction = new IOInstruction(burstTime, deviceID);
                System.out.println("PCBImpl: Debug print IO instruction = "+instruction);
                pcb = new ProcessControlBlockImpl(filename, priority, state.READY, instruction);
            }
            line = br.readLine();
        }
        //System.out.println(pcb.getInstruction());
        return pcb;
    }
    
}
