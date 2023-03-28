package ssoclient;

import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import merrimackutil.cli.LongOption;
import merrimackutil.cli.OptionParser;
import merrimackutil.util.Tuple;

public class SSOClient {

    public static void main(String[] args) throws NoSuchAlgorithmException {
        OptionParser op = new OptionParser(args);
        LongOption[] ar = new LongOption[2];
        ar[0] = new LongOption("hosts", true, 'h');
        ar[1] = new LongOption("user", true, 'u');
        ar[1] = new LongOption("service", true, 's');
        op.setLongOpts(ar);
        Tuple<Character, String> opt = op.getLongOpt(false);
        if (opt == null) {
            System.out.println("usage:\n"
                    + "   client --hosts <configfile> --user <user> --service <service>\n"
                    + "   client --user <user> --service <service>\n"
                    + "options:\n"
                    + "   -h, --hosts Set the hosts file.\n"
                    + "   -u, --user The user name.\n"
                    + "   -s, --service The name of the service");
            System.exit(0);
        } else if (Objects.equals(opt.getFirst(), 'h')) {
            //load hosts config
        }
        Scanner scan = new Scanner(System.in);
        Socket sock;
        Scanner recv = null;
        PrintWriter send = null;

        try {
            // Set up a connection to the echo server running on the same machine.
            sock = new Socket("127.0.0.1", 5000);

            // Set up the streams for the socket.
            recv = new Scanner(sock.getInputStream());
            send = new PrintWriter(sock.getOutputStream(), true);
        } catch (UnknownHostException ex) {
            System.out.println("Host is unknown.");
            return;
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }

        // Prompt the user for a string to send.
        System.out.print("Username: ");
        String msg = scan.nextLine();

        // Send username
        send.println(msg);

        // KDC checks username validity and if valid, demands password and gives a nonce
        String recvMsg = recv.nextLine();
        System.out.println("Server Said: " + "\n" + recvMsg);

        // Extract nonce
        String ExtractedNonce = "";
        Pattern pattern = Pattern.compile("nonce is: (\\S+)");
        Matcher matcher = pattern.matcher(recvMsg);
        if (matcher.find()) {
            ExtractedNonce = matcher.group(1);
        } else {
            System.exit(0);
        }

        // Client sends hashed password and nonce
        String msg2 = scan.nextLine();
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] clientHashPass = digest.digest(msg2.getBytes(StandardCharsets.UTF_8));
        byte[] clientHashNonce = digest.digest(ExtractedNonce.getBytes(StandardCharsets.UTF_8));
        send.println(Arrays.toString(clientHashPass));
        send.println(Arrays.toString(clientHashNonce));

        // Server recieved hash pass and nonce and will check validity via comparison
        String recvMsg2 = recv.nextLine();
        System.out.println("Server Said: " + "\n" + recvMsg2);
        String recvMsg3 = recv.nextLine();
        System.out.println(recvMsg3);
    }
}