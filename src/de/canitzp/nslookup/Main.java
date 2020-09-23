package de.canitzp.nslookup;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.stream.Collectors;

@SuppressWarnings("SameParameterValue")
public class Main {
    
    public static void main(String[] args){
        // check if the user has set any hostnames to look for
        if(args.length > 0){
            
            // print header
            System.out.println("==================== NSLookup ====================");
            
            // loop over the given hostnames
            for(String hostName : args){
                System.out.printf("Hostname: %s\n", hostName);
                try{
                    // get the addresses from the hostnames. UnknownHostException could be thrown
                    InetAddress[] allByName = InetAddress.getAllByName(hostName);
                    
                    // print all ip addresses found (like ipv4 and ipv6)
                    System.out.printf("\t%s\n", getAllAddressesJoined(", ", allByName));
                    
                    // test and print if the hostname/address is reachable over ICMP or TCP ECHO
                    // This is true if **at least one** hostname/address is reachable
                    System.out.printf("\tReachable: %s\n\n", isAnyAddressReachable(1000, allByName));
                    
                } catch(UnknownHostException e){
                    // print that the hostname could not be resolved to a address
                    System.out.printf("\t'%s' could not be found.\n", hostName);
                }
            }
            // print footer
            System.out.println("==================================================");
        } else {
            // print error if no hostname was specified by the user
            System.out.println("No hostname specified!");
        }
    }
    
    /**
     * @param delimiter The delimiter between the joined strings
     * @param addresses The addresses that should be joined
     * @return A joined String of all addresses
     */
    private static String getAllAddressesJoined(CharSequence delimiter, InetAddress... addresses){
        return Arrays.stream(addresses).map(InetAddress::getHostAddress).collect(Collectors.joining(delimiter));
    }
    
    /**
     * @param timeoutMilliseconds Timeout for the reachable method in milliseconds
     * @param addresses The addresses trying to reach
     * @return If any of the specified addresses can be reached
     */
    private static boolean isAnyAddressReachable(int timeoutMilliseconds, InetAddress... addresses){
        return Arrays.stream(addresses).anyMatch(address -> isReachable(address, timeoutMilliseconds));
    }
    
    /**
     * @param address The address that we're trying to reach.
     * @param timeoutMilliseconds The timeout for the try.
     * @return If the given address is reachable, without throwing any Exception
     */
    private static boolean isReachable(InetAddress address, int timeoutMilliseconds){
        try{
            return address.isReachable(timeoutMilliseconds);
        } catch(IOException ignored){
            return false;
        }
    }
    
}
