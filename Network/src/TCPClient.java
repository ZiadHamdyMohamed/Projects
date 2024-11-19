import java.io.*;
import java.net.*;

public class TCPClient {
	static boolean b=false;
	public static void main(String argv[]) throws Exception
	{
		String sentence;
		BufferedReader inFromUser =
				new BufferedReader(new InputStreamReader(System.in));
		System.out.println("To Connect type: \"CONNECT\" in Uppercase");
		while (!inFromUser.readLine().equals("CONNECT")){}
		Socket clientSocket = new Socket("192.168.1.91", 60010);

		DataOutputStream outToServer =
				new DataOutputStream(clientSocket.getOutputStream());
		BufferedReader inFromServer =
				new BufferedReader(new
						InputStreamReader(clientSocket.getInputStream()));

		// Start a new thread for reading server messages
		new Thread(() -> {
			String modifiedSentence;
			try {
				while((modifiedSentence = inFromServer.readLine()) != null) {
					System.out.println("FROM SERVER: " + modifiedSentence);
				}
			} catch(IOException e) {
				e.printStackTrace();
			}
		}).start();

		// Main thread for sending messages
		while(true) {
			sentence = inFromUser.readLine();

			if(sentence.equals("quit")) {
				b=true;
				break;
			}
			if (b){
				break;
			}
			outToServer.writeBytes(sentence + '\n');
		}

		clientSocket.close();
	}
}