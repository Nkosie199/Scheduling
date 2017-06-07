import java.util.Scanner;
import simulator.Config;
import simulator.Kernel;
import simulator.SystemTimer;
import simulator.TRACE;

/**
 * @author Nkosingiphile
 * @version 28/5/2017
 */

public class SimulateRR extends RoundRobinKernel{
    static String configFileName;
    static int sliceTime;
    static int costOfSyscall;
    static int costOfContextSwitch;
    static int traceLevel;
    static Scanner sc;
    
    public static void main(String[] args){
        sc = new Scanner(System.in);
        System.out.println("*** RR Simulator ***");
        
        // get configuration file name
        System.out.println("Enter configuration file name: "); //eg. Test2C/config.cfg
        configFileName = sc.nextLine();
        
        // get configuration file name
        System.out.println("Enter slice time: "); //eg. 80
        sliceTime = sc.nextInt();
        
        // get cost of system call
        System.out.println("Enter cost of system call: "); //eg. 1
        costOfSyscall = sc.nextInt();
        
        // get cost of context switch
        System.out.println("Enter cost of context switch: "); //eg. 3
        costOfContextSwitch = sc.nextInt();
        
        // get trace level (0 or 1)
        System.out.println("Enter trace level: "); //eg. 0
        traceLevel = sc.nextInt();
        
        // run the experiment
        TRACE.SET_TRACE_LEVEL(traceLevel); //Set the level of trace detail
        final Kernel kernel = new RoundRobinKernel(sliceTime); //Create the kernel.
        Config.init(kernel, costOfContextSwitch, costOfSyscall); //Initialise the simulation with the given kernel, dispatch cost and system call cost
        Config.buildConfiguration(configFileName); //Build the workload configuration described in the given configuration file
        Config.run(); //Run the simulation.
        
        // print out the results
        System.out.println("*** Results ***");
        SystemTimer timer = Config.getSystemTimer(); //Get the SystemTimer object
        System.out.println(timer); //Print the SystemTimer (outputs a string describing system time, kernel time, user time, idle time)
        System.out.println("Context switches:"
        +Config.getCPU().getContextSwitches()); //Print the number of context switches recorded by the CPU
        System.out.printf("CPU utilization: %.2f\n",
        ((double)timer.getUserTime())/timer.getSystemTime()*100); //Calculate and print the CPU utilisation based on the available timings
    
    }

    public SimulateRR(int sliceTime) {
        super(sliceTime);
    }
}
