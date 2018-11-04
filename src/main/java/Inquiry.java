import org.openqa.selenium.By;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import static java.lang.System.out;

abstract class Inquiry {
    private static WebDriver driver;
    private static int tmlong = 600, tmout = config.INTERVAL;
    static ArrayList<String[]> listout;

    static SimpleDateFormat formatTime = new SimpleDateFormat("[HH:mm:ss]");
    private static int[] num_shpd = {
            //       номер записи
          // Интервал решения 1
            23,   // Адрес            2
            24,   // Пом.             3
            1,    // Услуга           4
            2,    // Заявлено         5
            16,   // Технология       6
            25,   // Телефон          7, Комментарии      9
       //     21,   // Прим. оп наряду  8
       //     23,   // Комментарии      9
            28,   // Тех.данные       10,   Прим. оп наряду  8
            17,   // Работник         11,   Интервал решения 1
            3,    // Контрольный срок 12
            //4,    // Контрольный срок 12
            //6,    // Контрольный срок 12
            11     // Кл.              13
            };
    private static int[] num_line = {
            //       номер записи
               // 3, Интервал решения 1
            24,   // Адрес            2
            25,   // Пом.             3
            1,    // Услуга           4
            2,    // Заявлено         5
            17,   // Технология       6
            26,   // Телефон          7
               // Прим. оп наряду  8
            29,   // Комментарии      9
            30,   // Тех.данные       10
            18,   // Работник         11  интервал по нар 1
            3,    // Контрольный срок 12
            //5,    // Контрольный срок 12
           // 6,    // Контрольный срок 12
           // 7,    // Контрольный срок 12
            12    // Кл.              13
    };

    void prepareShpd(String s) throws IOException {
        int i, next;
        InquiryShpd testsh;
       // System.out.println("|" + s);
        try {
            i = s.indexOf("et\" target=");
            while (i > 0) {
                next = s.indexOf("target=", i + 11);

                test<InquiryShpd, String> inShpd = InquiryShpd::new;
                if ( next == -1)
                    testsh = Store.MyClasFactory(inShpd, s.substring(i), "");
                else
                    testsh = Store.MyClasFactory(inShpd, s.substring(i, next), "");
                i += testsh.compare(num_shpd);
            }
        }catch (StringIndexOutOfBoundsException | InterruptedException SIO) {
            System.out.println("Жопа->" + SIO);
        }
        System.out.println("нарядов ШДП  " + StoreInquiry.list.size() );
    }

    void prepareLine(String s)  {
        int i, next;
        InquiryLine testLine;

        try {
            i = s.indexOf("et\" target=");

            while (i > 0) {
                next = s.indexOf("target=", i + 11);
                test<InquiryLine, String> inLine = InquiryLine::new;
                if ( next == -1)
                    testLine = Store.MyClasFactory(inLine, s.substring(i), "");
                else
                    testLine = Store.MyClasFactory(inLine, s.substring(i, next), "");
                i += testLine.compare(num_line);
            }
        } catch (StringIndexOutOfBoundsException SIO) {
            System.out.println("Жопа-->" + SIO);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("нарядов на линию " + StoreInquiry.list.size()  );
    }


    Boolean init() throws InterruptedException {

        String url_name = "http://argusweb.ur.rt.ru:8080/argus";

        System.setProperty(FirefoxDriver.SystemProperty.DRIVER_USE_MARIONETTE,"true");
        System.setProperty("webdriver.gecko.driver", config.FIREFOX);

        driver =new FirefoxDriver();
        driver.get(url_name);

        Runid(driver,"login_form-username", config.LOGIN, tmlong );
        Runid(driver,"login_form-password", config.PASSWORD, tmlong );
        Runid(driver,"login_form-submit",null,  tmlong );
        TimeUnit.SECONDS.sleep(tmout);


        Runccs(driver,"li.ui-menu-parent:nth-child(2)",tmlong);
        out.println("Задачи");
        TimeUnit.SECONDS.sleep(tmout);

        Runccs(driver,"li.ui-menu-parent:nth-child(2) > ul:nth-child(2) > li:nth-child(1) > a:nth-child(1) > span:nth-child(1)",tmlong);
        out.println("список задач");
        TimeUnit.SECONDS.sleep(tmout);

        Runccs(driver,"#slcts-slct_acc-dsp_f_title > span:nth-child(1)",tmlong);
        out.println("участки");
        TimeUnit.SECONDS.sleep(tmout);

        Runccs(driver,config.MC, tmlong);  // МЦТЭТ
        out.println("МЦТЭТ");

        TimeUnit.SECONDS.sleep(tmout);

        return true;
    }

    static Boolean shutdriver() {

        Runccs(driver,"#mmf-logout", tmlong);
        out.println("Выход");

        driver.close();

        return false;
    }

    static Boolean restartdriver() {
        driver.close();
        return true;
    }

    String ReadShpd() throws InterruptedException {

        Runccs(driver,"#slcts-slct_acc-dsp_f-role_select_label", tmlong);
        TimeUnit.SECONDS.sleep(tmout);

        Runccs(driver,"#slcts-slct_acc-dsp_f-role_select_9", tmlong);
        TimeUnit.SECONDS.sleep(config.INTERVAL_L);

        out.println("ШПД завтра");

        return driver.getPageSource();
    }
    String ReadShpd_today() throws InterruptedException {
        String str = null;

        if(driver.findElements(By.cssSelector("#global_fltr_f-j_idt534-0-status_value > div:nth-child(2) > span:nth-child(1)")).size() > 0)
            str = "#global_fltr_f-j_idt534-0-status_value > div:nth-child(2) > span:nth-child(1)";
        if(driver.findElements(By.cssSelector("#global_fltr_f-j_idt502-0-status_value > div:nth-child(2) > span:nth-child(1)")).size() > 0)
            str = "#global_fltr_f-j_idt502-0-status_value > div:nth-child(2) > span:nth-child(1)";
        if(driver.findElements(By.cssSelector("#global_fltr_f-j_idt484-0-status_value > div:nth-child(2) > span:nth-child(1)")).size() > 0)
            str = "#global_fltr_f-j_idt484-0-status_value > div:nth-child(2) > span:nth-child(1)";
        if(driver.findElements(By.cssSelector("#global_fltr_f-j_idt505-0-status_value > div:nth-child(2) > span:nth-child(1)")).size() > 0)
            str = "#global_fltr_f-j_idt505-0-status_value > div:nth-child(2) > span:nth-child(1)";

        System.out.println(str);
        Runccs(driver, str, tmlong);
        TimeUnit.SECONDS.sleep(config.INTERVAL_L);

        out.println("ШПД сегодня");

        return driver.getPageSource();
    }

    String ReadLine_Next() throws InterruptedException {
        String str = null;

        if(driver.findElements(By.cssSelector("#global_fltr_f-j_idt534-1-status_value > div:nth-child(2) > span:nth-child(1)")).size() > 0)
            str = "#global_fltr_f-j_idt534-1-status_value > div:nth-child(2) > span:nth-child(1)";
        if(driver.findElements(By.cssSelector("#global_fltr_f-j_idt502-1-status_value > div:nth-child(2) > span:nth-child(1)")).size() > 0)
            str = "#global_fltr_f-j_idt502-1-status_value > div:nth-child(2) > span:nth-child(1)";
        if(driver.findElements(By.cssSelector("#global_fltr_f-j_idt484-1-status_value > div:nth-child(2) > span:nth-child(1)")).size() > 0)
            str = "#global_fltr_f-j_idt484-1-status_value > div:nth-child(2) > span:nth-child(1)";
        if(driver.findElements(By.cssSelector("#global_fltr_f-j_idt505-1-status_value > div:nth-child(2) > span:nth-child(1)")).size() > 0)
            str = "#global_fltr_f-j_idt505-1-status_value > div:nth-child(2) > span:nth-child(1)";

        System.out.println(str);

        Runccs(driver, str, tmlong);
        TimeUnit.SECONDS.sleep(config.INTERVAL_L);

        out.println("линия завтра");

        return driver.getPageSource();
    }

    String ReadLine() throws InterruptedException {

        Runccs(driver,"#slcts-slct_acc-dsp_f-role_select_label", tmlong);
        TimeUnit.SECONDS.sleep(tmout);

        Runccs(driver,"#slcts-slct_acc-dsp_f-role_select_7", tmlong);
        TimeUnit.SECONDS.sleep(config.INTERVAL_L);

        out.println("Линия сегодня");

        return driver.getPageSource();
    }

    private static void Runid(WebDriver dr, final String str, String val, int tm) {

        (new WebDriverWait(dr,tm, tm)).until(new ExpectedCondition<WebElement>() {
            public WebElement apply(WebDriver d) {
                return d.findElement(By.id(str));
            }
        });
        if( val == null)
            dr.findElement(By.id(str)).click();
        else
            dr.findElement(By.id(str)).sendKeys(val);
    }

    private static void Runccs(WebDriver dr, final String str, long tm)  {

        try {
            (new WebDriverWait(dr, tm, 10000)).until(new ExpectedCondition<WebElement>() {
                public WebElement apply(WebDriver d) {
                    return d.findElement(By.cssSelector(str));
                }
            });

            dr.findElement(By.cssSelector(str)).click();
        }catch ( ElementNotInteractableException el) {
            System.out.println("элемент не найден");
            StoreInquiry.Start = true;
        }
    }

    synchronized static void SendInquiry(ClientThread client) {
        int count = 0;
        String send;
        client.sendmessage("&&" + Calendar.getInstance().getTime());

        if( listout != null )
            for (String[] s : listout)
                for(String codSity : client.getcodSity())
                    if (codSity.equals(s[0])) {
                        send = "";
                        for (int i = 1; i < 15; i++)
                            send += s[i] + "|";
                        client.sendmessage(send);
                        count++;
                }

        client.sendmessage("&-");
        System.out.println(client.getname() + "->" + client.getcodSity() + " послано " + count);
    }

    void SendCityKC( ClientThread client) {
        int count = 0, count_all = 0;
        String send;
        Map<String, String> City =  StoreInquiry.Citys;

        client.sendmessage("&^");
        client.sendmessage(String.valueOf(Calendar.getInstance().getTime()) + "<br />");

        for( Map.Entry<String, String> codSity: City.entrySet() ) {
            send = "<font color=\"BLUE\">" + codSity.getValue() + "</font><br />";
            if (listout != null)
                for (String[] s : listout)
                    if (codSity.getKey().substring(1).equals(s[0])) {
                        if (s[14].contains("ЮЛ")) {
                            send += "<font color=\"#DAA520\">" + s[1].substring(3) + " <em>" + s[13] + " </em></font><font color=\"BLUE\"><br/><small>" + s[2] + ' ' + s[3] + "</font><font color=\"RED\"> " + s[14] + "</font></small><br/>";
                            count++;
                        }
                        else
                            if (s[13].contains("СКВ")) {
                                send += "<font color=\"RED\">" + s[1].substring(3) + " <em>" + s[13] + " </em></font><font color=\"BLUE\"><br/><small>" + s[2] + ' ' + s[3] + ' ' + s[14] + "</font></small><br/>";
                                count++; }
                            else
                            if (Integer.parseInt(InquiryShpd.o.apply(s[13].substring(s[13].indexOf(':') + 1))) < 4) {
                                send += "<font color=\"#FFA500\">" + s[1].substring(3) + " <em>" + s[13] + " </em></font><font color=\"BLUE\"><br/><small>" + s[2] + ' ' + s[3] + ' ' + s[14] + "</font></small><br/>";
                                count++;
                            }
                    count_all++;
                }
                    client.sendmessage(send + "<font color=\"#006400\">Всего :" + count_all + "</font><font color=\"#B22222\"> Опасных: " + count + "</font><br/>");
                    System.out.println(client.getname() + '|' + client.getT().getName() + "->" + codSity.getKey() + " послано " + count + send);
                    send = "";
                    count_all = count = 0;
        }
    }
}

