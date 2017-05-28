package programgenerator;
import simulator.Instruction;
/**
 * Write a description of interface InstructionGenerator here.
 * 
 * @author Nkosi Gumede 
 * @version 28/5/2017
 */
public abstract class InstructionGenerator {

    private final BoundedExpRNG rng;
    private final int lowerBound;
    private final int upperBound;
    
    public InstructionGenerator(BoundedExpRNG rng, int lowerBound, int upperBound) {
        this.rng=rng;
        this.lowerBound=lowerBound;
        this.upperBound=upperBound;
    }
    
    protected int nextBurst() {
        return rng.nextInt(upperBound-lowerBound)+lowerBound;
    }
    
    public abstract Instruction make();

    public String toString() { return "lambda="+rng.getLambda()+", lower_bound="+lowerBound+", upper_bound="+upperBound; }
}
