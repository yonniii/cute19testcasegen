package cute19.interpreter.parser;

import cute19.interpreter.ast.*;

public class NodePrinter {
    private final String OUTPUT_FILENAME = "output08.txt";
    private StringBuffer sb = new StringBuffer();
    private Node root;

    public NodePrinter(Node root) {
        this.root = root;
    }

    private void printList(ListNode listNode) {
        if (listNode == ListNode.EMPTYLIST) {
//            sb.append("( )");
            return;
        }
        if (listNode == ListNode.EMPTYLIST) {
            return;
        }
        printNode(listNode.car());
        printList(listNode.cdr());

    }

    private void printNode(QuoteNode quoteNode) {
        if (quoteNode.nodeInside() == null)
            return;
        sb.append("\'");
        printNode(quoteNode.nodeInside());
    }

    private void printNode(Node node) {
        if (node == null) {
            return;
        }
        if (node instanceof ListNode) {
            if(((ListNode) node).car() instanceof QuoteNode){
                printNode((QuoteNode) ((ListNode) node).car());
                printList(((ListNode) node).cdr());
            }else{
                sb.append("( ");
                printList((ListNode) node);
                sb.append(") ");
            }
        }
        else if(node instanceof QuoteNode){
            printNode((QuoteNode) node);
        }
        else {
            sb.append(node.toString() +" ");
        }
    }

    public void prettyPrint() {
        printNode(root);
        System.out.println("... "+sb);
//        try (FileWriter fw = new FileWriter(OUTPUT_FILENAME);
//             PrintWriter pw = new PrintWriter(fw)) {
//            pw.write(sb.toString());
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}
