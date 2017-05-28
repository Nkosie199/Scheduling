package programgenerator;
import simulator.CPUInstruction;
/**
 * Write a description of class CPUGenerator here.
 * 
 * @author Nkosi Gumede 
 * @version 27/5/2017
 */
public class CPUGenerator extends InstructionGenerator {
    
    public CPUGenerator(BoundedExpRNG rng, int lowerBound, int upperBound) {
        super(rng, lowerBound, upperBound);
    }
    
    public CPUInstruction make() {
        return new CPUInstruction(super.nextBurst());
    }
        
    public String toString() { return "CPU Generator ("+super.toString()+")"; }
}
