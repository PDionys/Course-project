import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import service.AuthenticationService;
import service.CRUDfileService;
import service.PermisionService;
import service.LogService;

public class Server {
	private final AuthenticationService authService;
	private final CRUDfileService crudService;
	private final PermisionService permService;
	private final LogService logService;
	
	public Server(AuthenticationService authService, CRUDfileService crudService, PermisionService permService, LogService logService) {
		this.authService = authService;
		this.crudService = crudService;
		this.permService = permService;
		this.logService = logService;
	}
	
	private void start() {
		try(ServerSocket server = new ServerSocket(8000))
        {
            System.out.println("Server started");

            while (true)
                try (
                        Socket socket = server.accept();
                        BufferedWriter writer =
                                 new BufferedWriter(
                                         new OutputStreamWriter(
                                                 socket.getOutputStream()));
                        BufferedReader reader =
                                new BufferedReader(
                                        new InputStreamReader(
                                                socket.getInputStream()));
                ) {
                    Integer whatToDo = Integer.valueOf(reader.readLine());
                    switch (whatToDo){
                        case 1:// Login
                        	authService.handleLogin(writer, reader);
                            break;
                        case 2:// Check is user name exist
                        	authService.handleRegistration(writer, reader);
                            break;
                        case 3:// Create new file
                        	crudService.handleCreate(writer, reader);
                            break;
                        case 4:// Load file
                            crudService.handleRead(writer, reader);
                            break;
                        case 5:// Save file
                        	crudService.handleUpdate(writer, reader);
                            break;
                        case 6:
                        	permService.handlePermision(writer, reader);
                            break;
                        case 7:
                        	permService.handleOddFile(writer, reader);
                            break;
                        default:
                            break;
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
	}
	
    public static void main(String[] args) throws IOException {
    	LogService logService = new LogService();
    	AuthenticationService authService = new AuthenticationService(logService);
    	CRUDfileService crudService = new CRUDfileService();
    	PermisionService permService = new PermisionService(authService, logService);
    	
    	Server server = new Server(authService, crudService, permService, logService);
    	server.start();
    }
}
