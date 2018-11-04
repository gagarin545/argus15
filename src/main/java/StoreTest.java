import java.io.IOException;
import java.rmi.NotBoundException;

class StoreTest extends Store {

     String storeTest(String message) throws IOException, InterruptedException, NotBoundException {
         String s = "";
         switch (message.substring(1, 4)) {
             case "777":
             case "770":
                 if(message.length() != 12) {
                     s = ("&# Неверный логин : Введено " + message.length() + " символов, а должно быть 11");
                     break;
                 }
             case "IMS":
                 System.out.print("послано:" + message);
                 test<testShpd, String> mytestshpd = testShpd::new;
                 testShpd testshpd = Store.MyClasFactory(mytestshpd, message, "");
                 s = testshpd.test();
                 System.out.println("ШПД" + s);
                 break;
             case "К62":
             case "К65":
                 test<testRTS, String> mytestrts = testRTS::new;
                 testRTS testrts = Store.MyClasFactory(mytestrts,  message.substring(message.indexOf(' ')), "192.168.12.1");
                 s = testrts.test();
                 break;
             case "К66":
             case "К67":
                 test<testMT20, String> mytestmt20 = testMT20::new;
                 if(message.contains("К66"))
                     s = "10.183.5.66";
                 if(message.contains("К67"))
                     s = "10.183.5.67";
                 testMT20 testmt20 = Store.MyClasFactory(mytestmt20,  message.substring(message.indexOf(' ')), s);
                 s = testmt20.test();
                 break;
             case "К1 ":
             case "К2 ":
             case "К3 ":
             case "К13":
                 System.out.println(message);
                 test<testPhone, String> mytestp1 = testPhone::new;
                 testPhone testp1 = Store.MyClasFactory(mytestp1,  message.substring(message.indexOf(' ')), "10.11.104.21");
                 s = testp1.test();
                 break;
             case "К4 ":
             case "К5 ":
             case "К6 ":
             case "К7 ":
             case "К8 ":
             case "К9 ":
             case "К10":
             case "К11":
             case "К12":
                 System.out.println(message);
                 test<testPhone, String> mytestp4 = testPhone::new;
                 testPhone testp4 = Store.MyClasFactory(mytestp4,  message.substring(message.indexOf(' ')), "10.11.104.20");
                 s = testp4.test();
                 break;
             case "ШПД":
                 if(message.contains(";")) message = message.substring(0, message.indexOf(';')) + ' ';
                 System.out.println( message);
                 test<changeShpd, String> mytestchange = changeShpd::new;
                 changeShpd testchange = Store.MyClasFactory(mytestchange,  message.substring(4) , "");
                 s = testchange.test();
                 break;
             default:
                 s =( message.substring(1) + " Эта услуга не тестируется : !");
                 System.out.print("->ничего");
         }
         return s;
    }
}
