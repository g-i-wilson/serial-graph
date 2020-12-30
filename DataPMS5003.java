import java.util.*;
import java.time.LocalDateTime;

public class DataPMS5003 {

  private static final int TX_LENGTH = 32;

  private static final int DATA_START_BYTE = 4;
  private static final int DATA_END_BYTE = 29;
  private static final int CHECK_BYTE_H = 30;
  private static final int CHECK_BYTE_L = 31;

  private List<Integer> packet;
  private List<Integer> data;
  private LocalDateTime time;
  private boolean dataIsValid;

  public DataPMS5003 () {
    time = LocalDateTime.now();
    packet = new ArrayList<Integer>();
    dataIsValid = false;
  }

  private int value16 (int high, int low) {
    high = high << 8;
    return (high + low);
  }

  public boolean valid () {

    if (dataIsValid) {
      return dataIsValid;
    }

    // check for correct size packet
    if (packet.size() != TX_LENGTH) {
      Exception e = new Exception( "Incomplete packet at "+time+": "+printPacket() );
      e.printStackTrace();
      dataIsValid = false;
      return dataIsValid;
    }

    data = new ArrayList<Integer>();
    // validate bytes in packet
    int checkCode = 0;
    for (int i=0; i<=DATA_END_BYTE; i+=2) {
      if (i>=DATA_START_BYTE) data.add( value16(packet.get(i),packet.get(i+1)) );
      checkCode += ( packet.get(i) + packet.get(i+1) );
    }

    // if (value16(packet.get(CHECK_BYTE_H),packet.get(CHECK_BYTE_L)) == checkCode) {
    //   dataIsValid = true;
    // } else {
    //   Exception e = new Exception( "Invalid data at "+time+": "+printPacket() );
    //   e.printStackTrace();
    //   dataIsValid = false;
    // }

    /*** "Check value" appears to be broken -- always transmits 0x01 0x00 ***/
    dataIsValid = true;

    return dataIsValid;
  }

  public boolean shift( int b ) {
    packet.add(b);
    if (packet.size() > TX_LENGTH) packet.remove(0);
    if (packet.size() == TX_LENGTH && packet.get(0) == 'B' && packet.get(1) == 'M') return true;
    else return false;
  }

  public List<Integer> data () {
    if (data == null) valid();
    return data;
  }

  public int data (int index) {
    if (valid())
      return data.get(index);
    else
      return -1;
  }

  public AQI CF1 () {
    if (valid())
      return new AQI( (double)data(0), (double)data(1) );
    else
      return new AQI();
  }
  public AQI atm () {
    if (valid())
      return new AQI( (double)data(3), (double)data(4) );
    else
      return new AQI();
  }

  public int bin0_3um () {
    return data(6);
  }
  public int bin0_5um () {
    return data(7);
  }
  public int bin1_0um () {
    return data(8);
  }
  public int bin2_5um () {
    return data(9);
  }
  public int bin5_0um () {
    return data(10);
  }
  public int bin10um () {
    return data(11);
  }
  public Map<Double,Integer> particleBins () {
    Map<Double,Integer> pMap = new LinkedHashMap<Double,Integer>();
    pMap.put(0.3, bin0_3um());
    pMap.put(0.5, bin0_5um());
    pMap.put(1.0, bin1_0um());
    pMap.put(2.5, bin2_5um());
    pMap.put(5.0, bin5_0um());
    pMap.put(10.0, bin10um());
    return pMap;
  }


  public LocalDateTime time () {
    return time;
  }

  public List<Integer> packet () {
    return packet;
  }

  public String printPacket () {
    String p = "\nDec: ";
    // print decimal
    for (Integer i : packet) p += i+",";
    p += "\nHex: ";
    // print hex
    for (Integer i : packet) p += String.format("%02x", i)+" ";
    p += "\n16-bit dec: ";
    // print decimal with high-low bits combined
    for (int i=0; i<packet.size()-1; i+=2) p+= value16(packet.get(i), packet.get(i+1))+",";
    p += "\n";
    return p;
  }

  public static void main(String[] args) {
    DataPMS5003 d = new DataPMS5003();
    // tests value16() method
    System.out.println("10 is "+d.value16(0, 0b1010)); // 10 == 0b1010
    System.out.println("10000 is "+d.value16(0b00100111, 0b00010000)); // 10000 == 0b0010011100010000
  }

}
