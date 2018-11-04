import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;


public class ClientThread extends Inquiry implements Runnable {
    private Socket s;//здесь будем хранить ссылку на наш сокет
    private InputStreamReader inStream;
    private OutputStreamWriter outStream;
    private PrintWriter out;
    private Thread t;
    private ArrayList<String> codSity;
    private String myName;

    ClientThread(Socket s ) {   //конструктор,в который мы передаем
        this.s = s;

    }

    @Override
    public void run() {
        Scanner scanner;
        String message;
        ClientThread p = ClientThread.this;
        t = Thread.currentThread();

        try {
            inStream = new InputStreamReader(s.getInputStream(),"UTF8");//входящий поток данных
            outStream = new OutputStreamWriter(s.getOutputStream(),"UTF8");//исходящий поток
        } catch (IOException e) {                e.printStackTrace();            }

        while( s != null ){ //пока сокет "жив"
            StoreTest store = new StoreTest();
            SimpleDateFormat formatTime = new SimpleDateFormat("[HH:mm:ss]");
            out = new PrintWriter(outStream,true);//создаем объект, который будет писать в исходящий поток
            scanner=new Scanner(inStream);//слушем входящий поток

            while(scanner.hasNextLine()) {//если мы вручную не останавливаем сокет и есть сообщение
                message = scanner.nextLine();   //считываем его
                Date dat = new Date();
                System.out.println(message);
                if (message.startsWith("#"))
                    switch (message.substring(0,4)) {
                        case "#***":
                            SendCityKC(p);
                            break;
                        case "#reg":
                            System.out.println(message);
                            sendmessage("0");
                            break;
                        case "#usr":
                            codSity = new ArrayList<>();
                            t.setName(message.substring(4, message.indexOf('#', 4)));
                            myName = String.valueOf( StoreInquiry.Users.compute(getT().getName(), (s, s2) -> { return s2 != null ? s2 : "Неизвестный";  }));
                            getcod.func(message.substring(message.indexOf("#", 4)), codSity);
                            sendmessage("&*" + myName);
                            System.out.println("клиент " +  getT().getName());
                            StartSocket.registrationObs(p);
                        case "#inc":
                            sendmessage("&~Вкл");
                            SendInquiry(p);
                            break;
                        case "#get":
                            StartSocket.start_soc.notifyall(message.substring(4));
                            break;
                        case "#his":
                            sendmessage(StartSocket.his.read_history());
                            break;
                        case "#rst":
                            System.out.println(formatTime.format(new Date()) + "-запуск перезагрузки.");
                            sendmessage(formatTime.format(new Date()) + " запуск перезагрузки.");
                            StoreInquiry.Start = Inquiry.restartdriver();
                            sendmessage(formatTime.format(new Date()) + "-перезагрузка завершена.");
                            break;
                        default:
                            try {
                                sendmessage(store.storeTest(message));
                            } catch (IOException | InterruptedException | NotBoundException e) {
                                e.printStackTrace(); System.out.println("-->" + e);
                            }
                    }
                else {
                    System.out.println(getT().getName() + "->" + Calendar.getInstance().getTime() + message);
                    StartSocket.his.write_history( Calendar.getInstance().getTime() + " " + message );
                    StartSocket.start_soc.notifyall(Calendar.getInstance().getTime() + " " + message );
                }
            }
        }
        StartSocket.removeObs(p);
    }

    Thread getT()   {       return t;       }
    ArrayList<String> getcodSity() {   return codSity; }
    String getname() {      return myName;   }


    synchronized void sendmessage(String s) {

        out.println(s);     // функция отправки клиенту сообщения
    }

    private codSity< String, ArrayList<String>> getcod = new codSity<String, ArrayList<String>>() {

        @Override
        public String func(String s, ArrayList<String> a) {
            try {
                a.add(s.substring(1, s.indexOf('#', 1)));
                getcod.func(s.substring(s.indexOf('#', 1)), a);
                return null;
            } catch (StringIndexOutOfBoundsException e) {
                a.add(s.substring(s.lastIndexOf('#') + 1));
                return null;
            }
        }
    };

}
