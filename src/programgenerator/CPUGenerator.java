package programgenerator;
import simulator.CPUInstruction;
/**
 * Write a description of class CPUGenerator here.
 * 
<<<<<<< HEAD
 * @author Nkosi Gumede 
 * @version 27/5/2017
 */
public class CPUGenerator extends InstructionGenerator {
    
=======
 * @author (your name) 
 * @version (a version number or a date)
 */
public class CPUGenerator extends InstructionGenerator {
    
    
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
    public CPUGenerator(BoundedExpRNG rng, int lowerBound, int upperBound) {
        super(rng, lowerBound, upperBound);
    }
    
    public CPUInstruction make() {
        return new CPUInstruction(super.nextBurst());
    }
        
    public String toString() { return "CPU Generator ("+super.toString()+")"; }
}
