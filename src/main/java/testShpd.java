import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.BindException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

class testShpd {
    private String service;
    testShpd(String service, String s1) {
        this.service = service;
    }

    String test(){
        StringBuilder s = new StringBuilder("&#Услуга :" + service.substring(1));
        String r = "";

        String url_get_page[] = {
                "http://10.183.116.238/cgi-bin/getTechData.php?svc=",   //  3513256145;PON;74-MIASS-AGG004-O6;10.228.36.197;3;7;5;;;;
                "http://10.183.116.238/cgi-bin/getline.cgi?uslogin="    //  ARGUS_port   : 74-MIASS-AGG004-O6(10.228.36.197)-3/7/5      ONT_distance : 0        Rx n/a
                };
        try {
            for(String[] ss: Inquiry.listout ) {

                if (ss[5].equals( service)) {
                    try {
                        System.out.print("=" + ss[5] + " " + ss[15] + " " + ss[16] + " " + ss[17] + " " + ss[18]);
                        if(ss[15] == null )
                            break;
                        if(ss[5].contains("IMS"))
                            service = service.replace("IMS", ss[0]);
                        r = SNMPManager.snmp_get(ss[15], ss[16], ss[17], ss[18]);
                        break;
                    } catch (NullPointerException | BindException e) {
                        System.out.println("Не нашла.");
                        e.printStackTrace();           }
                }
            }

            s.append(Forming(InitTest(  "http://10.183.116.238/cgi-bin/getBRM.cgi?uslogin=" + service.substring(1)))); //  User-Login: Start-time: BRAS: IP Address:  MAC Address:  Speed-Limit:  Output Policy Map:  Agent Circuit ID:
            System.out.println("s->" +s);

                if(r.length() == 0)
                    r = geting(InitTest(url_get_page[0] + service.substring(1)));
            System.out.println("r->" + r);
                if(r.length() > 0)
                     s.append( r);
                else
                    s.append(Forming(InitTest(url_get_page[1] + service.substring(1))));
                System.out.println(">>" + s);

        }catch (IOException e) {
            return s.toString() + "|! : Повторите тест позже|" + e + "|";
        }
        return s.toString() + "|";
    }

    private String geting(BufferedReader rd) throws IOException {
        String r;
        ArrayList<String> list = new ArrayList<String>();

        r = rd.readLine();

        for (int a = 0; r.indexOf(';', a + 1) != -1; a = r.indexOf(';', a + 1) + 1)
            list.add(r.substring(a, r.indexOf(';', a)));

        switch (list.get(1)) {
            //case "FTTx":
            case "BShPD":
            case "N.A.":
                return "|Результат : услуга " + list.get(1) + " пока не тестируется.";
            default:
                try {
                    return SNMPManager.snmp_get(list.get(3), list.get(4), list.get(5), list.get(6));
                } catch (BindException e) {                    e.printStackTrace();                    return "";                }
        }
    }
    private BufferedReader InitTest(String url_get_page) throws IOException {

        URL url = new URL(url_get_page);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        if (connection != null) {
            connection.setReadTimeout(60 * 1000); // ожидание на 5 сек
            connection.setDoOutput(true); // соединение доступно для вывода
            connection.setUseCaches(false); // не использовать кэш
            connection.setRequestMethod("GET"); // метод post
            connection.setRequestProperty("connection", "keep-alive");
            connection.setRequestProperty("Charset", "UTF-8");
        }
        connection.connect();
        return new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF8"));
    }
    private String Forming(BufferedReader rd) throws IOException {
        StringBuilder mes = new StringBuilder("");
        String line;
        String[] StringKey = {
                "ARGUS_port  ",
                "IP Address:",
                "MAC Address:",
                "Speed-Limit:",
                "Port_Profile",
                "SNR         ",
                "Attenuation",
                "Link_Speed",
                "Output Policy Map:",
                "ONT_SW_Version   :",
                "ONT_Equipment-ID :",
                "Rx_Power     :",
                "OLT Rx",
                "ONT_distance :"};
        String[] StrKeyRus = {
                "Тех.данные",
                "IP адрес  :",
                "MAC адрес :",
                "M.скорость:",
                "Профиль   ",
                "Сигнал/шум",
                "Затухание",
                "Скорость",
                "Политика  :",
                "Версия ПО ONT :",
                "Тип оборуд.:",
                "Rx мощность:",
                "Затухание :",
                "Растояние :"};
        while ((line = rd.readLine()) != null) {

            if (line.startsWith("ARGUS_port") & line.indexOf("not_found") > 0) return "/ Записай не найдено.";
            for (int i = 0; i < StringKey.length; i++)
                if (line.startsWith(StringKey[i]))
                    if(!mes.toString().contains(StrKeyRus[i])) {
                        if (line.startsWith("ARGUS_port"))
                            mes.append("| Сет. имя :").append(line.substring(line.indexOf(":") + 1, line.indexOf("("))).append("| Ip адрес  : ").append(line.substring(line.indexOf("(") + 1, line.indexOf(")"))).append("| порт/слот  : ").append(line.substring(line.indexOf(")-") + 2));
                        else
                            mes.append("| ").append(line.replace(StringKey[i], StrKeyRus[i]));
                    }
        }
        return mes.toString();
    }

}