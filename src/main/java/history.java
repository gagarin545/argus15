import java.util.ArrayList;

public class history {
private ArrayList <String> history = new ArrayList<>();

void write_history(String s) {
    System.out.println(s);
    history.add(s);
}

String read_history(){
    String str = "";
    for(String s: history)
        str += s + '\n';
    System.out.println("len=" + history.size());
    return str;
}

void clear_history(){ history.clear();  }

}

