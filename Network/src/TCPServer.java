import java.io.*;
import java.net.*;

public class TCPServer {

	public static void main(String argv[]) throws Exception {
		String serverSentence;

		ServerSocket welcomeSocket = new ServerSocket(60010);
		BufferedReader inFromUser =
				new BufferedReader(new InputStreamReader(System.in));

		while(true) {
			Socket connectionSocket = welcomeSocket.accept();

			BufferedReader inFromClient =
					new BufferedReader(new
							InputStreamReader(connectionSocket.getInputStream()));
			DataOutputStream  outToClient =
					new DataOutputStream(connectionSocket.getOutputStream());

			// Send "Connected" message to client
			outToClient.writeBytes("Connected" + '\n');
			System.out.println("Client with IP: " + connectionSocket.getInetAddress() + " connected");
			// Start a new thread for reading client messages
			new Thread(() -> {
				String clientSentence;
				try {
					while((clientSentence = inFromClient.readLine()) != null) {
						System.out.println("From Client: "+clientSentence);
					}
				} catch(IOException e) {
					e.printStackTrace();
				}
			}).start();

			// Main thread for sending messages
			while(true) {
				serverSentence = inFromUser.readLine();

				if(serverSentence.equals("quit")) {
					break;
				}

				outToClient.writeBytes(serverSentence + '\n');
			}
		}
	}
}