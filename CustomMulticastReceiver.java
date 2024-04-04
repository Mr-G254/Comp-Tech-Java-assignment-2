import java.io.IOException;
import java.net.*;

public class CustomMulticastReceiver extends Thread {
    private static final String MULTICAST_GROUP = "224.0.0.2";
    private static final int PORT = 9999;

    private static int countX = 0;
    private static int countY = 0;

    @Override
    public void run() {
        try {
            InetAddress group = InetAddress.getByName(MULTICAST_GROUP);
            try (MulticastSocket socket = new MulticastSocket(PORT)) {
                socket.joinGroup(group);

                byte[] buffer = new byte[1024];
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                while (true) {
                    socket.receive(packet);
                    String received = new String(packet.getData(), 0, packet.getLength());
                    System.out.println("Received vote: " + received);

                    // Count votes
                    if (received.equals("X")) {
                        countX++;
                    } else if (received.equals("Y")) {
                        countY++;
                    }

                    // Display winner
                    determineWinner();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void determineWinner() {
        System.out.println("Vote counting completed.");
        if (countX > countY) {
            System.out.println("Candidate X wins with " + countX + " votes.");
        } else if (countY > countX) {
            System.out.println("Candidate Y wins with " + countY + " votes.");
        } else {
            System.out.println("It's a tie.");
        }
    }

    public static void main(String[] args) {
        // Create and start multicast receiver
        CustomMulticastReceiver receiver = new CustomMulticastReceiver();
        receiver.start();
    }
}
