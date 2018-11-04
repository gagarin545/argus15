import org.snmp4j.*;
import org.snmp4j.event.ResponseEvent;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.*;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;
import java.net.BindException;

abstract class SnmpCommand
{
    private Snmp snmp = null;
    private String address;
    private String val;
    private ResponseEvent event;

    SnmpCommand(String address) {
        this.address = address;
    }

    String getAsString(OID oid, String val) throws IOException {
        this.val = val;
        String s;
        if(val.length() > 0)
            event = snmpset(new OID(oid));
        else
            event = snmpget(new OID(oid));
        try {
            s = event.getResponse().get(0).getVariable().toString();
        } catch (NullPointerException e) { System.out.println(e); return "";  }
            return s;
    }

    private ResponseEvent snmpget(OID oid) throws IOException {

        PDU pdu = new PDU();

        pdu.add(new VariableBinding(oid));
        pdu.setType(PDU.GET);
        event = snmp.send(pdu, target(address, "public"));

        if(event != null)
            return event;
        throw new RuntimeException("GET timed out");
    }

    private ResponseEvent snmpset(OID oid) throws IOException {

        ResponseEvent event;
        PDU pdu = new PDU();

        pdu.add(new VariableBinding(oid, new OctetString(val)));

        pdu.setType(PDU.SET);
        event = snmp.set(pdu, target(address, "private"));

        if(event != null)
            return event;
        throw new RuntimeException("SET timed out");
    }

    boolean start()  {
        try {
            TransportMapping transport = new DefaultUdpTransportMapping();
            snmp = new Snmp(transport);
            transport.listen();
        } catch ( BindException er)
        { er.printStackTrace();
            System.out.println("111");
            return false;  }
            catch (IOException e) {  e.printStackTrace();
            System.out.println("222");
        }
        return true;
    }

    private Target target(String address, String Type) {
        Address targetAddress = GenericAddress.parse(address);
        CommunityTarget target = new CommunityTarget();
        target.setCommunity(new OctetString(Type));
        target.setAddress((Address) targetAddress);
        target.setRetries(2);
        target.setTimeout(1500);
        target.setVersion(SnmpConstants.version2c);
        return target;
    }
}
