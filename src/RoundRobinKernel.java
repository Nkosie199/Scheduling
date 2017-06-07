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
import simulator.ProcessControlBlockImpl;

/**
 *
 * @author Nkosingiphile
 * @version 28/5/2017
 */
public class RoundRobinKernel implements Kernel {

    private static Deque<ProcessControlBlock> readyQueue;
    private static int timeSlice;
        
    public RoundRobinKernel(int sliceTime) {
        // Set up the ready queue.
        readyQueue = new ArrayDeque<>();
        timeSlice = sliceTime;
    }
    
    private ProcessControlBlock dispatch() {
        // Perform context switch, swapping process currently on CPU with one at front of ready queue.
        // If ready queue empty then CPU goes idle ( holds a null value).
        ProcessControlBlock out;
        if (readyQueue.isEmpty()){
            out = Config.getCPU().contextSwitch(null);
        }
        else{
            ProcessControlBlock pcb = readyQueue.removeFirst();
            out = Config.getCPU().contextSwitch(pcb);
        }
        // Returns process removed from CPU.
        return out;
    }
                
    public int syscall(int number, Object... varargs) {
        int result = 0;
        switch (number) {
             case MAKE_DEVICE:
                // called from Config using: syscall(1, deviceID, deviceName)
                {
                    IODevice device = new IODevice((Integer)varargs[0], (String)varargs[1]);
                    Config.addDevice(device);
                }
                break;
             case EXECVE: 
                // called using: syscall(2, fileName)
                {
                    ProcessControlBlock pcb = loadProgram((String)varargs[0]);
                    if (pcb!=null) {
                        // Loaded successfully.
                        // Now divide the process into slices and add each slice to end of ready queue.
                        float slices = pcb.getInstruction().getDuration()/timeSlice;
                        System.out.println("RRKernel debug print # of slices: "+slices);
                        for (int i=0; i<slices; i++){
                            // edit each intruction duration
                            int duration =pcb.getInstruction().getDuration();
                            System.out.println("Duration: "+duration);
                            readyQueue.add(pcb);
                        }                     
                        System.out.println("RRKernel debug print ready queue: "+readyQueue);
			// If CPU idle then call dispatch.
                        if (Config.getCPU().isIdle()){
                            dispatch();
                        }
                    }
                    else {
                        result = -1;
                    }
                }
                break;
             case IO_REQUEST: 
                // syscall(3, deviceID, burstTime) 
                {
                    // IO request has come from process currently on the CPU.
                    // Get PCB from CPU.
                    ProcessControlBlock pcb = Config.getCPU().getCurrentProcess();
                    // Find IODevice with given ID: Config.getDevice((Integer)varargs[0]);
                    IODevice device = Config.getDevice((Integer)varargs[0]);
                    // Make IO request on device providing burst time (varages[1]),
                    // the PCB of the requesting process, and a reference to this kernel (so 
                    // that the IODevice can call interrupt() when the request is completed.
                    device.requestIO((Integer)varargs[1], pcb, this);
                    // Set the PCB state of the requesting process to WAITING.
                    pcb.setState(ProcessControlBlock.State.WAITING);
                    // Call dispatch().
                    dispatch();
                }
                break;
             case TERMINATE_PROCESS:
                // called using syscall(4)
                {
                    // Process on the CPU has terminated.
                    // Get PCB from CPU.
                    ProcessControlBlock pcb = Config.getCPU().getCurrentProcess();
                    // Set status to TERMINATED.
                    pcb.setState(ProcessControlBlock.State.TERMINATED);
                    // Call dispatch().
                    dispatch();
                }
                break;
             default:
                result = -1;
        }
        return result;
    }
   
    
    public void interrupt(int interruptType, Object... varargs){
        switch (interruptType) {
            case TIME_OUT:
                throw new IllegalArgumentException("FCFSKernel:interrupt("+interruptType+"...): this kernel does not suppor timeouts.");
            case WAKE_UP:
                // IODevice has finished an IO request for a process.
                // Retrieve the PCB of the process (varargs[1]), set its state
                ProcessControlBlock pcb = (ProcessControlBlock) varargs[1];
                // to READY, put it on the end of the ready queue.
                pcb.setState(ProcessControlBlock.State.READY);
                readyQueue.add(pcb);
                // If CPU is idle then dispatch().
                if (Config.getCPU().isIdle()){
                    dispatch();
                }
                break;
            default:
                throw new IllegalArgumentException("FCFSKernel:interrupt("+interruptType+"...): unknown type.");
        }
    }
    
    public static ProcessControlBlock loadProgram(String filename) {
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
