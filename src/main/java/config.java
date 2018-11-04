import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

class config {
    private static final String PROPERTIES_FILE = "properties";
    //private static final String PROPERTIES_FILE = "/home/ura/bin/properties";

    static int PORT;
    static int START;
    static int STOP;
    static int INTERVAL;
    static int INTERVAL_L;
    static String LOGIN;
    static String PASSWORD;
    static String FIREFOX;
    static String MC;
    static String UserFile;
    static String CodCity;


    static {
        Properties properties = new Properties();
        FileInputStream propertiesFile = null;

        try {
            propertiesFile = new FileInputStream(PROPERTIES_FILE);
            properties.load(propertiesFile);
            START        = Integer.parseInt(properties.getProperty("START"));
            STOP        = Integer.parseInt(properties.getProperty("STOP"));
            PORT        = Integer.parseInt(properties.getProperty("PORT"));
            INTERVAL    = Integer.parseInt (properties.getProperty("INTERVAL"));
            INTERVAL_L    = Integer.parseInt (properties.getProperty("INTERVAL_L"));
            LOGIN       = properties.getProperty("LOGIN");
            PASSWORD    = properties.getProperty("PASSWORD");
            FIREFOX     = properties.getProperty("FIREFOX");
            MC          = properties.getProperty("MC");
            UserFile    = properties.getProperty("UserFile");
            CodCity     = properties.getProperty("CodCity");

            System.out.println(LOGIN + PORT + PASSWORD + " " + START + "!" + STOP + " " + INTERVAL + INTERVAL_L + UserFile);

        } catch (FileNotFoundException ex) {
            System.err.println("Не найден файл " + PROPERTIES_FILE);
        } catch (IOException ex) {
            System.err.println("Error while reading file");
        } finally {
            try {
                propertiesFile.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }
}
