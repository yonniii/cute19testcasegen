package cute19.interpreter.interpreter;

import cute19.interpreter.ast.Node;

import java.util.Hashtable;

public class Table {
    static Hashtable<String, Node> table=new Hashtable<>();
    static void insertTable(String id, Node value){
        table.put(id,value);
    }
    static Node lookupTable(String key){
        return table.get(key);
    }
}
