package echoservice;

import java.net.Socket;
import java.net.ServerSocket;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.util.Objects;
import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.util.Tuple;

public class EchoService {

    public static void main(String[] args) {
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
            //load config
        }
        try {
            ServerSocket server = new ServerSocket(5000);

            // Loop forever handing connections.
            while (true) {
                // Wait for a connection.
                Socket sock = server.accept();

                System.out.println("Connection received.");

                // Setup the streams for use.
                Scanner recv = new Scanner(sock.getInputStream());
                PrintWriter send = new PrintWriter(sock.getOutputStream(), true);

                // Get the line from the client.
                String line = recv.nextLine();
                System.out.println("Client said: " + line);

                // Echo the line back.
                send.println(line);

                // Close the connection.
                sock.close();
            }
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
