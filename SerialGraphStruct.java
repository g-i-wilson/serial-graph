import java.util.*;
import java.time.LocalDateTime;

public class SerialGraphStruct {

  private List<Integer> packet;
  private LocalDateTime time;
  private boolean dataIsValid;

  public SerialGraphStruct () {
    time = LocalDateTime.now();
    packet = new ArrayList<Integer>();
    dataIsValid = false;
  }

  private void printInt (int i) {
    System.out.print( i+": " );
    for (int a=31; a>=0; a--) {
      if ((i & 0x80000000)==0x80000000) {
        System.out.print( "1 " );
      } else {
        System.out.print( "0 " );
      }
      i = i << 1;
    }
    System.out.println();
  }

  private int value16 (int high, int low) {
    int combined = ((high << 8) + low);
    if ((0xffff7fff | combined) == 0xffff7fff) { // positive value
      combined = (0x0000ffff & combined); // make sure all higher bits are 0s
    } else if ((0x00008000 & combined) == 0x00008000) { // negative value
      combined = (0xffff0000 | combined); // make sure all higher bits are 1s
    }
    return combined;
  }

  public boolean valid () {
    if ( packet.size() > 2 && value16(packet.get(1), packet.get(0)) == packet.size()-2 ) return true;
    return false;
  }

  public List<Integer> data () throws Exception {

    if (valid()) {
      List data = new ArrayList<Integer>();
      for (int i=2; i<packet.size()-1; i+=2) {
        data.add( value16(packet.get(i), packet.get(i+1)) );
      }
      return data;
    } else {
      Exception e = new Exception("Incomplete packet at time: "+time);
      e.printStackTrace();
      return null;
    }

  }

  public void shift( int b ) {
    packet.add(b);
  }

  public List<Integer> packet () {
    return packet;
  }

  public String toString () {
    // print decimal
    String p = "\nDec: ";
    for (Integer i : packet) p += i+",";

    // print hex
    p += "\nHex: ";
    for (Integer i : packet) p += String.format("%02x", i)+" ";

    // print decimal with high-low bits combined
    p += "\n16-bit dec: ";
    for (int i=0; i<packet.size()-1; i+=2) p+= value16(packet.get(i), packet.get(i+1))+",";
    p += "\n";

    return p;
  }

  public static void main(String[] args) {
    SerialGraphStruct struct = new SerialGraphStruct();
    // tests value16() method
    System.out.println("-1 is "+struct.value16(0x000000ff, 0x000000ff));
    struct.printInt(struct.value16(0x000000ff, 0x000000ff));
    System.out.println("32767 is "+struct.value16(0x0000007f, 0x000000ff));
    struct.printInt(struct.value16(0x0000007f, 0x000000ff));
  }

}
