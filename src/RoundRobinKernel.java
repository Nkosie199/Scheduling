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
import simulator.CPUInstruction;
import simulator.Instruction;
import simulator.InterruptHandler;
import simulator.ProcessControlBlockImpl;
import simulator.SystemTimer;
import simulator.SimulationClock;

/**
 *
 * @author Nkosingiphile
 * @version 28/5/2017
 */
public class RoundRobinKernel implements Kernel {

    private static Deque<ProcessControlBlock> readyQueue;
    private static int timeSlice;
    private static int timedOutPID;
        
    public RoundRobinKernel(int sliceTime) {
        // Set up the ready queue.
        readyQueue = new ArrayDeque<>();
        timeSlice = sliceTime;
    }
    
    private ProcessControlBlock dispatch() {
        // Perform context switch, swapping process currently on CPU with one at front of ready queue.
        // If ready queue empty then CPU goes idle ( holds a null value).
      
//        System.out.println("");
//        System.out.println("RRKernel debug print ready queue: "+readyQueue);       
        // Need to establish which process needs CPU time for each time slice
        int noOfProcesses = ProcessControlBlockImpl.PIDcounter;
//        System.out.println("RRKernel dispatch() debug print # of processes: "+noOfProcesses);       
        // Need to establish current system time
        long sysTime = Config.getSystemTimer().getSystemTime();
//        System.out.println("RRKernel dispatch() debug print current system time: "+sysTime);      
        // Need to establish slice # 
        int sliceNo = (int) sysTime/timeSlice;
//        System.out.println("RRKernel dispatch() debug print slice #: "+sliceNo);       
        // now each slice needs to be attributed to a process
        int priorityPID = (sliceNo%noOfProcesses)+1;
//        System.out.println("RRKernel dispatch() debug print priority process ID: "+priorityPID);    
        // if PCB.pid!=prioriyPID: switch PBC out and add it to the back of the readyQueue    
        ProcessControlBlock process = readyQueue.peek();
        ProcessControlBlock out = null;
 
        // 2ND METHOD
            if (readyQueue.isEmpty()){
                out = Config.getCPU().contextSwitch(null);
                // now wake up timed out event  
//                Config.getSystemTimer().cancelInterrupt(timedOutPID);
//                interrupt(1, timedOutPID, out);
            }
            else{
                ProcessControlBlock pcb = readyQueue.removeFirst();
                // always schedule interrupts in pcb != null ...
                if (pcb.getInstruction().getDuration() >= timeSlice){
//                    System.out.println("Scheduling time out for instruction: "+pcb.getInstruction());
                    Config.getSystemTimer().scheduleInterrupt(timeSlice, this , pcb.getPID()); 
                }                                       
                out = Config.getCPU().contextSwitch(pcb);
            }            
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
                        // Now add to end of ready queue.
                        readyQueue.add(pcb);

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
//                throw new IllegalArgumentException("FCFSKernel:interrupt("+interruptType+"...): this kernel does not suppor timeouts.");
                //
                timedOutPID = (int) varargs[0];
//                System.out.println("RRKernel.interrupt processID of timed out process : "+timedOutPID);
                
                // Switch in process at front of ready queue and add the current process to the back of it  
                if (Config.getCPU().isIdle()){
                    ProcessControlBlock processOut = dispatch();
                    processOut.setState(ProcessControlBlock.State.READY);
                    readyQueue.add(processOut);
                    //
//                    interrupt(1, timedOutPID, processOut); // wake up process that was timed out
//                    interrupt(1, timedOutPID, readyQueue.peek());
                }
                break;
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
   
//    private ProcessControlBlock dispatch() {
//        // Perform context switch, swapping process currently on CPU with one at front of ready queue.
//        // If ready queue empty then CPU goes idle ( holds a null value).
//        System.out.println("");
//        System.out.println("RRKernel debug print ready queue: "+readyQueue);
//        ProcessControlBlock out;
//        if (readyQueue.isEmpty()){
//            out = Config.getCPU().contextSwitch(null);
//        }
//        else{
//            ProcessControlBlock process = readyQueue.peek();
//            Instruction instr = process.getInstruction();
//            int duration = instr.getDuration();
//            System.out.println("RRKernel debug print instruction: "+instr);
//            System.out.println("RRKernel debug print intsruction duration: "+duration);
//            
//            if (duration > timeSlice){
//                ProcessControlBlock pcb = readyQueue.removeFirst();
////                System.out.println("");
////                System.out.println("RRKernel debug print process in: "+pcb);
//                out = Config.getCPU().contextSwitch(pcb);
////                System.out.println("RRkernel debug print process out: "+out);
////                System.out.println("");
//                // execute for duration then switch
//                readyQueue.add(out); //add the process switched out to the back of the ready queue 
//                //possibility of infinite loop!!
//                dispatch(); //switch in next process
//            }
//            else{
//                ProcessControlBlock pcb = readyQueue.removeFirst();
////                System.out.println("");
////                System.out.println("RRKernel debug print process in: "+pcb);
//                out = Config.getCPU().contextSwitch(pcb);
////                System.out.println("RRKernel debug print process out: "+out);
////                System.out.println("");                
//            }
//            
//        }
//        // Returns process removed from CPU.
//        return out;
//    }    
    
   
}
