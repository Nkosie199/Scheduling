import simulator.Config;
import simulator.IODevice;
import simulator.Kernel;
import simulator.ProcessControlBlock;
//
import java.io.FileNotFoundException;
import java.io.IOException;
//
import java.util.ArrayDeque;
import java.util.Deque;

/**
 *
 * @author Nkosingiphile
 * @version 28/5/2017
 */
public class RoundRobinKernel implements Kernel {

    @Override
    public int syscall(int number, Object... varargs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void interrupt(int interruptType, Object... varargs) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
