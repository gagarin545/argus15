import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

class StartSocket {
    static StartSocket start_soc;
    static history his;
    private static ArrayList<ClientThread> ClientList  = new ArrayList<>();

    StartSocket() {
        start_soc = this;
        StartServer();
    }

    private void StartServer() {

        try {
            ServerSocket ss = new ServerSocket( config.PORT );
            System.out.println("Сервер старт, жду...");
            his = new history();

            do {
                Socket incoming = ss.accept();

                ClientThread client = new ClientThread(incoming);
                Thread t = new Thread(client);
                t.start();

            } while (ss.isBound());
        } catch (IOException ex) {  System.out.println("Server internal error " + ex.getMessage());        }
    }

    static synchronized void registrationObs(ClientThread o) {

        ClientList.removeIf(se -> (se.getT().getName()).equals(o.getT().getName()));
        ClientList.add(o);
    }

    static synchronized void removeObs(ClientThread o) {
        o.getT().interrupt();
        System.out.println("Удаляю " + o.getT().getName());
        ClientList.remove(o);
    }

    void notifyObs() {
        for(ClientThread client: ClientList) {
            Inquiry.SendInquiry(client);
            System.out.println("Послано " + client.getT().getName());
        }
    }

    void notifyall(String message) {
        for(ClientThread client: ClientList)
            client.sendmessage(message);
    }
}
