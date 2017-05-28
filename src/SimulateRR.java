/**
 * @author Nkosingiphile
 * @version 28/5/2017
 */

public class SimulateRR extends RoundRobinKernel{

    public static void main(String[] args){
        System.out.println("*** RR Simulator ***");
        
        // get configuration file name
        System.out.println("Enter configuration file name: "); //eg. Test1F/config.cfg
        
        // get slice time
        System.out.println("Enter slice time: "); //eg. 80
        
        // get cost of system call
        System.out.println("Enter cost of system call: "); //eg. 1
        
        // get cost of context switch
        System.out.println("Enter cost of context switch: "); //eg. 3
        
        // get trace level (0 or 1)
        System.out.println("Enter trace level: "); //eg. 0
        
        // print out results
        System.out.println("*** Results ***");
        System.out.println("System time: "); //eg. 103      
        System.out.println("Kernel time: "); //eg. 13       
        System.out.println("User time: "); //eg. 90     
        System.out.println("Idle time: "); //eg. 80     
        System.out.println("Context switches: "); //eg. 3      
        System.out.println("CPU utilization: "); //eg. 87.38
    }

}
