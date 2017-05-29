package simulator;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 *
 * @author gmdnko003
 */
public class ProcessControlBlockImpl implements ProcessControlBlock {
    Instruction instruction;
    List<Instruction> instructions; 
    int PID;
    String programName;
    int priority;
    State state;
    
        
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
            return true;
        }
    }

    @Override
    public void nextInstruction() {
        System.out.println(instructions.get(0));
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
            System.out.println(line);
            String[] lineElements = line.split(" ");
            if (lineElements.length == 2){ // CPU instruction
                String deviceType = lineElements[0];
                int burstTime = Integer.parseInt(lineElements[1]);
                // create CPU process
                
            }
            else if (lineElements.length == 3){ // IO instruction
                String deviceType = lineElements[0];
                int burstTime = Integer.parseInt(lineElements[1]);
                int deviceID = Integer.parseInt(lineElements[2]);
                // create IO process
                
            }
            line = br.readLine();
        }
        return pcb;
    }
    
}
