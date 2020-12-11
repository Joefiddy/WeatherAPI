import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;


public class MyBotMain {
	
	public static void main(String[] args) throws Exception {
		
		//Now start our bot up
		MyBot bot = new MyBot();
		
		String nick = "bot_bot";
		String login = "bot_bot";
		
		// Enable debugging output
		bot.setVerbose(true);
		
		String server = "irc.freenode.net"; 
		String channel = "#KhansClass";
		// Connect to the IRC server
		bot.connect("irc.freenode.net");
		
		// Join the #pircbot channel. 
		bot.joinChannel("#KhansClass");
		
		// Socket
		Socket socket = new Socket(server, 6667);
		
		// I/O
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		
	    // Log on to the server
        writer.write("NICK " + nick + "\r\n");
        writer.write("USER " + login + " 8 * : Java IRC Hacks Bot\r\n");
        writer.flush();

         // Read lines from the server until it tells us we have connections
        String line = null;
        while ((line = reader.readLine()) != null) {
            if (line.indexOf("004") >= 0) {
                // We are now logged in
                break;
            }
            else if (line.indexOf("433") >= 0) {
                System.out.println("Nickname is already in use.");
                return;
            }
        }
		
        // Join the channel
        writer.write("JOIN " + channel + "\r\n");
        writer.flush();

        // bot introduction
        writer.write("PRIVMSG " + channel + " :SIMPLE_BOT CONNECTED! \r\n");
        writer.write("PRIVMSG " + channel + " :ASK ME ANYTHING ABOUT THE WEATHER TODAY! \r\n");
        writer.flush();
        
        // Keep reading lines from the server
        while ((line = reader.readLine()) != null) {

            if (line.toLowerCase().startsWith("PING ")) {
                // We must respond to PINGs to avoid being disconnected
                writer.write("PONG " + line.substring(5) + "\r\n");
                writer.write("PRIVMSG " + channel + " :I got pinged!\r\n");
                writer.flush();
            }
            else {
                // splits user input into easy to read list
                String[] strings = line.split(" ");
                // creates new instance of phrase object
                //Phrase phrase = new Phrase();
                // ensures the line has readable content
                if (strings[1].equals("PRIVMSG")) {
                	ArrayList<String> phrase = new ArrayList<>();
                    if (strings.length > 3) {
                        // adds all strings in command to phrase list
                        int i = 3;
                        
                        while (i < strings.length) {
                            phrase.add(strings[i]);
                            i++;
                        }
                        // parses the user input for keyword 'weather'
                        if (find("weather", phrase)) {
                            // attempts to find the specified location in the user's input
                            if (!getLoc(phrase).equals("")) {
                                writer.write("PRIVMSG " + channel + " :" + weatherAPI.getWeather(getLoc(phrase)) + "\r\n");
                                writer.flush();
                            }
                        }
                    }
                }
            }
                
        
		
	}
        
	}
	
	public static boolean find(String s, ArrayList<String> phrase) {
		
		boolean found = false;
		for (String str : phrase) {
			if (str.equals(":weather")) {
				found = true;
			}
			else if (str.equals(s)) {
				found = true;
			}
		}
		return found;
	}
	
	public static String getLoc(ArrayList<String> phrase) {
		String[] first = phrase.get(0).split("");
		String location = "";
		for (String s : phrase) {
			
			boolean zip;
			try {
				Integer.parseInt(s);
				zip = true;
			} catch(Exception e) {
				zip = false;
			}
			if(zip) {
				return s;
			}
		}
		return location;
	}

}
