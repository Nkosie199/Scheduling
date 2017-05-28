<<<<<<< HEAD
=======


>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
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
 * Concrete Kernel type
 * 
 * @author Stephan Jamieson
 * @version 8/3/15
 */
public class FCFSKernel implements Kernel {
    
<<<<<<< HEAD
    private Deque<ProcessControlBlock> readyQueue; // holds processes ready to be executed
        
    public FCFSKernel() {
        // Set up the ready queue.
        readyQueue = new ArrayDeque<>();
    }
    
    private ProcessControlBlock dispatch() {
        // Perform context switch, swapping process currently on CPU with one at front of ready queue.
        // If ready queue empty then CPU goes idle (holds a null value). Returns process removed from CPU.
        
    }
=======

    private Deque<ProcessControlBlock> readyQueue;
        
    public FCFSKernel() {
		// Set up the ready queue.
    }
    
    private ProcessControlBlock dispatch() {
		// Perform context switch, swapping process
		// currently on CPU with one at front of ready queue.
		// If ready queue empty then CPU goes idle ( holds a null value).
		// Returns process removed from CPU.
	}
            
    
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
                
    public int syscall(int number, Object... varargs) {
        int result = 0;
        switch (number) {
             case MAKE_DEVICE:
                {
                    IODevice device = new IODevice((Integer)varargs[0], (String)varargs[1]);
                    Config.addDevice(device);
                }
                break;
             case EXECVE: 
                {
                    ProcessControlBlock pcb = this.loadProgram((String)varargs[0]);
                    if (pcb!=null) {
                        // Loaded successfully.
<<<<<<< HEAD
                        
                        // Now add to end of ready queue.
                        // If CPU idle then call dispatch.
=======
						// Now add to end of ready queue.
						// If CPU idle then call dispatch.
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
                    }
                    else {
                        result = -1;
                    }
                }
                break;
             case IO_REQUEST: 
                {
<<<<<<< HEAD
                    // IO request has come from process currently on the CPU.
                    // Get PCB from CPU.
                    // Find IODevice with given ID: Config.getDevice((Integer)varargs[0]);
                    // Make IO request on device providing burst time (varages[1]),
                    // the PCB of the requesting process, and a reference to this kernel (so // that the IODevice can call interrupt() when the request is completed.
                    //
                    // Set the PCB state of the requesting process to WAITING.
                    // Call dispatch().
=======
					// IO request has come from process currently on the CPU.
					// Get PCB from CPU.
					// Find IODevice with given ID: Config.getDevice((Integer)varargs[0]);
					// Make IO request on device providing burst time (varages[1]),
					// the PCB of the requesting process, and a reference to this kernel (so // that the IODevice can call interrupt() when the request is completed.
					//
					// Set the PCB state of the requesting process to WAITING.
					// Call dispatch().
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
                }
                break;
             case TERMINATE_PROCESS:
                {
<<<<<<< HEAD
                    // Process on the CPU has terminated.
                    // Get PCB from CPU.
                    // Set status to TERMINATED.
                    
=======
					// Process on the CPU has terminated.
					// Get PCB from CPU.
					// Set status to TERMINATED.
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
                    // Call dispatch().
                }
                break;
             default:
                result = -1;
        }
        return result;
    }
<<<<<<< HEAD
=======
   
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
    
    public void interrupt(int interruptType, Object... varargs){
        switch (interruptType) {
            case TIME_OUT:
                throw new IllegalArgumentException("FCFSKernel:interrupt("+interruptType+"...): this kernel does not suppor timeouts.");
            case WAKE_UP:
<<<<<<< HEAD
                // IODevice has finished an IO request for a process.
                // Retrieve the PCB of the process (varargs[1]), set its state
                // to READY, put it on the end of the ready queue.
                // If CPU is idle then dispatch().
=======
				// IODevice has finished an IO request for a process.
				// Retrieve the PCB of the process (varargs[1]), set its state
				// to READY, put it on the end of the ready queue.
				// If CPU is idle then dispatch().
>>>>>>> cc743fda113227879e53af53c30da02d10d3a4fe
                break;
            default:
                throw new IllegalArgumentException("FCFSKernel:interrupt("+interruptType+"...): unknown type.");
        }
    }
    
    private static ProcessControlBlock loadProgram(String filename) {
        try {
            return ProcessControlBlockImpl.loadProgram(filename);
        }
        catch (FileNotFoundException fileExp) {
            return null;
        }
        catch (IOException ioExp) {
            return null;
        }
    }
}
