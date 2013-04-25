/*
 * 
 * Kudos to Andrew Tenne for the code to get the ip address
 */
package game.net;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class IPAddressGrabber {
    
    public String getIP() {
        String ipAddress = null;
        String[] arg = new String[]{"-u root", "-h localhost"};
        try {
            Process p = Runtime.getRuntime().exec("cmd.exe /c ipconfig");

            //obj.exec("cmd.exe /dir");
            BufferedWriter writeer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            writeer.write("dir");
            writeer.flush();
            BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));

            String line;

            while(ipAddress == null) {
                if((line = stdInput.readLine()).contains("IPv4 Address")) {
                    ipAddress = line.substring(line.indexOf(':') + 2);
                    System.out.println(ipAddress);
                }
            }

        } catch (IOException e) {
            System.err.println("FROM CATCH " + e.toString());
        }
        
        return ipAddress;
    }
}
