import java.io.IOException;
import java.net.*;

public class CustomMulticastSender extends Thread {
    private static final String MULTICAST_GROUP = "224.0.0.2";
    private static final int PORT = 9999;

    private char vote;

    public CustomMulticastSender(char vote) {
        this.vote = vote;
    }

    @Override
    public void run() {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
            try (MulticastSocket socket = new MulticastSocket(PORT)) {
                socket.joinGroup(group);
                byte[] data = String.valueOf(vote).getBytes();
                DatagramPacket packet = new DatagramPacket(data, data.length, group, PORT);
                socket.send(packet);
                System.out.println("Vote " + vote + " sent successfully.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        char[] votes = {'A', 'B', 'A', 'B', 'A'}; // Example votes

        // Send votes
        for (char vote : votes) {
            CustomMulticastSender sender = new CustomMulticastSender(vote);
            sender.start();
        }
    }
}
