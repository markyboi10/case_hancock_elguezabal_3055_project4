package echoservice;

import communication.Communication;
import echoservice.config.Config;
import java.io.FileNotFoundException;
import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.io.InvalidObjectException;
import java.security.NoSuchAlgorithmException;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.util.Tuple;
import packets.Packet;

public class EchoService {
    
    private static Config config;  
    
    private static ServerSocket server;

    public static void main(String[] args) throws FileNotFoundException, InvalidObjectException {
        OptionParser op = new OptionParser(args);
        LongOption[] ar = new LongOption[2];
        ar[0] = new LongOption("config", true, 'c');
        ar[1] = new LongOption("help", false, 'h');
        op.setLongOpts(ar);
        op.setOptString("hc:");
        Tuple<Character, String> opt = op.getLongOpt(false);
        if (opt == null || Objects.equals(opt.getFirst(), 'h')) {
            System.out.println("usage:\n"
                    + " echoservice\n"
                    + " echoservice --config <configfile>\n"
                    + " echoservice --help\n"
                    + "options:\n"
                    + " -c, --config Set the config file.\n"
                    + " -h, --help Display the help.");
            System.exit(0);
        } else if (Objects.equals(opt.getFirst(), 'c')) {
            config = new Config(opt.getSecond()); //load config
        }
        try {
            // Create the server
            server = new ServerSocket(config.getPort());

            // Poll for input
            poll();
            
            // Close the server when completed or error is thrown.
            server.close();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        } catch (NoSuchMethodException ex) {
            System.out.println("EchoService No Such Method Exception");
            Logger.getLogger(EchoService.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            System.out.println("EchoService No Such Algorithm Exception");
            Logger.getLogger(EchoService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Waits for a connection with a peer socket, then polls for a message being
     * sent. Each iteration of the loop operates for one message, as not to
     * block multi-peer communication.
     *
     * @throws IOException
     */
    private static void poll() throws IOException, NoSuchMethodException, NoSuchAlgorithmException {
        while (true) { // Consistently accept connections

            // Establish the connection & read the message
            Socket peer = server.accept();

            // Determine the packet type.
            System.out.println("Waiting for a packet...");
            final Packet packet = Communication.read(peer);
            
            System.out.println("Packet Recieved: ["+packet.getType().name()+"]");
            
            // Switch statement only goes over packets expected by the KDC, any other packet will be ignored.
            switch (packet.getType()) {

                
            } 
        }

    }

}
