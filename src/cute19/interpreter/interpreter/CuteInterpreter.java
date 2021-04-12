package cute19.interpreter.interpreter;

import cute19.interpreter.ast.*;
import cute19.interpreter.parser.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;
import java.util.Scanner;


public class CuteInterpreter {
    public static void main(String[] args) throws IOException {

        CuteParser cuteParser;
        CuteInterpreter interpreter;
        Node parseTree;
        Node resultNode;
        NodePrinter nodePrinter;

        while (true) {
            cuteParser = new CuteParser(scanner());
            interpreter = new CuteInterpreter();
            parseTree = cuteParser.parseExpr();
            resultNode = interpreter.runExpr(parseTree);
            nodePrinter = new NodePrinter(resultNode);
            nodePrinter.prettyPrint(false);
        }

    }

    static Scanner s = new Scanner(System.in);

    private static String scanner() {
        System.out.print("> ");
        return s.nextLine();
    }

    public static List<String> callInterpreter(List<String> codes) throws IOException {
        List<String> results = new ArrayList<>();
        CuteParser cuteParser;
        CuteInterpreter interpreter;
        Node parseTree;
        Node resultNode;
        NodePrinter nodePrinter;
        for (String line: codes) {
            cuteParser = new CuteParser(line);
            interpreter = new CuteInterpreter();
            parseTree = cuteParser.parseExpr();
            resultNode = interpreter.runExpr(parseTree);
            nodePrinter = new NodePrinter(resultNode);
            results.add(nodePrinter.prettyPrint(true));
//            nodePrinter.prettyPrint();
        }
        return results;
    }

    private static List<String> readFile(String filename) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filename),
                StandardCharsets.UTF_8);
        return lines;
    }


    private void errorLog(String err) {
        System.out.println(err);
    }

    public Node runExpr(Node rootExpr) {
        if (rootExpr == null)
            return null;
        if (rootExpr instanceof IdNode) {
            if(param_lookupTable(rootExpr.toString())!=null){
                return param_lookupTable(rootExpr.toString());
            }else if (Table.lookupTable(rootExpr.toString()) != null) {
                return Table.lookupTable(rootExpr.toString());
            } else {
                return null;
            }
        } else if (rootExpr instanceof IntNode) {
            return rootExpr;
        } else if (rootExpr instanceof BooleanNode)
            if(param_lookupTable(rootExpr.toString())!=null){
                return param_lookupTable(rootExpr.toString());
            }else if (Table.lookupTable(rootExpr.toString()) != null) {
                return Table.lookupTable(rootExpr.toString());
            } else {
                return rootExpr;
            }
        else if (rootExpr instanceof ListNode)
            return runList((ListNode) rootExpr);
        else
            errorLog("run Expr error");
        return null;
    }

    Hashtable<String, Node> lambdatable=new Hashtable<>();
    void param_insertTable(String id, Node value){
        lambdatable.put(id,value);
    }
    Node param_lookupTable(String key){
        return lambdatable.get(key);
    }
    void insertParam( ListNode formal, ListNode actual ){
        if(formal.equals(ListNode.EMPTYLIST)){
            return;
        }
        param_insertTable(formal.car().toString(),runExpr(actual.car()));
        insertParam(formal.cdr(),actual.cdr());
    }


    private Node runList(ListNode list) {
        if (list.equals(ListNode.EMPTYLIST))
            return list;
        if( list.car() instanceof  ListNode){
            if( list.cdr() instanceof ListNode ){
                insertParam((ListNode) ((ListNode) list.car()).cdr().car(),(ListNode) list.cdr());
                return runFunction((FunctionNode) ((ListNode) list.car()).car(),(ListNode) ((ListNode) list.car()).cdr().cdr().car());
            }
        }
        if (list.car() instanceof FunctionNode) {
            if(((FunctionNode) list.car()).funcType== FunctionNode.FunctionType.LAMBDA){
                return list;
            }
            return runFunction((FunctionNode) list.car(), (ListNode) stripList(list.cdr()));
        }
        if (list.car() instanceof BinaryOpNode) {
            return runBinary(list);
        }
        if(list.car() instanceof IdNode){
            return runList(ListNode.cons(runExpr(list.car()),list.cdr()));
        }
        return list;
    }

    private Node runFunction(FunctionNode operator, ListNode operand) {
        Node front;
        Node rear;
        Node quotelist;
        switch (operator.funcType) { // CAR, CDR, CONS등에 대한 동작 구현
            case CAR:
                Node carCar=operand.car();
                if(operand.car() instanceof IdNode||operand.car() instanceof BooleanNode){ // define된 값이면
                    carCar=runExpr(operand.car());
                }
                if( carCar instanceof FunctionNode){ // 쿼트노드 안오고 functionNode가 왔을 때
                    carCar=runExpr(operand);
                    if(carCar instanceof QuoteNode){
                        carCar=((QuoteNode)carCar).nodeInside();
                    }
                    else{
                        carCar=runQuote((ListNode)carCar);
                    }
                    if(((ListNode)carCar).car() instanceof IntNode||((ListNode)carCar).car() instanceof BooleanNode){
                        return ((ListNode)carCar).car();
                    }
                    else
                        return new QuoteNode(((ListNode)carCar).car());
                }else{ // 쿼트노드 왔을 때
                    Node result;
                    if(operand.car() instanceof IdNode||operand.car() instanceof BooleanNode){
                        // 쿼트노드이고 operand.car이 define된 값이라면
                        result=runQuote((ListNode) carCar);
                        result=((ListNode)result).car();
                    }
                    else // 정의 된 값아니면
                        result=((ListNode) runQuote(operand)).car();
                    if(result instanceof IntNode||result instanceof BooleanNode) // result가 인트,불린노드만 나오면 쿼트 안붙임
                        return result;
                    else // 나머지는 붙임
                        return new QuoteNode(result);
                }
            case CDR:
                if(operand.car() instanceof IdNode||operand.car() instanceof BooleanNode){
                    Node a=runExpr(operand.car());
                    return new QuoteNode(((ListNode) runQuote((ListNode)a)).cdr());
                }
                if(operand.car() instanceof FunctionNode){
                    Node result=runExpr(operand);
                    if(result instanceof QuoteNode){
                        result=((QuoteNode)result).nodeInside();
                    }
                    else if(result instanceof ListNode&&((ListNode) result).car() instanceof QuoteNode){
                        result=runQuote((ListNode)result);
                    }
                    return new QuoteNode(((ListNode)result).cdr());
                }else{
                    return new QuoteNode(((ListNode)runQuote(operand)).cdr());
                }
            case CONS:
                Node head = runExpr(operand.car());
                Node tail = runExpr(operand.cdr().car());
                if ((head instanceof ListNode)) {
                    if (((ListNode) head).car() instanceof QuoteNode) {
                        head = runQuote((ListNode) head);
                    }
                } else if(head instanceof QuoteNode){
                    head=((QuoteNode) head).nodeInside();
                }
                if (tail instanceof ListNode) {
                    if (((ListNode) tail).car() instanceof QuoteNode) {
                        tail = runQuote((ListNode) tail);
                    }
                } else {
                    if(tail instanceof QuoteNode){
                        tail=((QuoteNode) tail).nodeInside();
                    }
                    else
                        tail = ListNode.cons(tail, ListNode.EMPTYLIST);
                }
                Node temp = new QuoteNode(ListNode.cons(head, (ListNode) tail));
                return ListNode.cons(temp, ListNode.EMPTYLIST);
            case NULL_Q:
                if (operand.car() instanceof QuoteNode) {
                    quotelist = runQuote(operand);
                } else if (operand.car() instanceof IdNode || operand.car() instanceof BooleanNode) {
                    quotelist = runExpr(operand.car());
                    quotelist = runQuote((ListNode)quotelist);
                } else if(operand.car() instanceof FunctionNode){ //연산이 중첩 될 때
                    quotelist =runExpr(operand);
                } else {
                    quotelist = operand.car();
                }
                if(quotelist instanceof QuoteNode && ((QuoteNode) quotelist).nodeInside().equals(ListNode.EMPTYLIST)){
                    return BooleanNode.TRUE_NODE;
                }
                if (quotelist.equals(ListNode.EMPTYLIST)) {
                    return BooleanNode.TRUE_NODE;
                } else {
                    return BooleanNode.FALSE_NODE;
                }
            case NOT:
                BooleanNode bn = BooleanNode.FALSE_NODE;
                Node node = runExpr(operand);
                if (node instanceof ListNode) {
                    node = runExpr(((ListNode) node).car());
                    if (node instanceof QuoteNode) {
                        return BooleanNode.FALSE_NODE;
                    }
                }

                if (node instanceof BooleanNode) {
                    if (node.toString().equals("#T")) {
                        bn = BooleanNode.FALSE_NODE;
                    } else {
                        bn = BooleanNode.TRUE_NODE;
                    }
                } else {
                    bn = BooleanNode.FALSE_NODE;
                }
                return bn;
            case ATOM_Q:
                Node atomOperand=operand;
                if(operand.car() instanceof IdNode||operand.car() instanceof BooleanNode){
                    atomOperand=runExpr(((ListNode)atomOperand).car());
//                    return runFunction(operator, (ListNode) runExpr(operand.car()));
                }
                if(operand.car() instanceof FunctionNode) {
                    atomOperand=runExpr(atomOperand);
                }
                if (atomOperand instanceof ListNode&&((ListNode)atomOperand).car() instanceof QuoteNode&&runQuote((ListNode)atomOperand) instanceof ListNode) {
                    if (((ListNode) runQuote((ListNode)atomOperand)).car() == null)
                        return BooleanNode.TRUE_NODE;
                    return BooleanNode.FALSE_NODE;
                } else
                    return BooleanNode.TRUE_NODE;
            case COND:
                if ((front = runExpr(operand.car())) instanceof BooleanNode) { //조건이 하나뿐인 경우, 리스트로 묶여있지 않음
                    if (front.equals(BooleanNode.TRUE_NODE)) {
                        return runExpr(operand.cdr().car());
                    } else {
                        return BooleanNode.FALSE_NODE;
                    }
                }
                front = runExpr(operand.car());
                if (runExpr(((ListNode) front).car()).equals(BooleanNode.TRUE_NODE)) {
                    return runExpr(((ListNode) front).cdr().car());
                }
                if (!(operand.cdr().equals(ListNode.EMPTYLIST))) {
                    rear = runExpr(operand.cdr().car());
                    if (runExpr(((ListNode) rear).car()).equals(BooleanNode.TRUE_NODE)) {
                        return runExpr(((ListNode) rear).cdr().car());
                    }
                    if (!(operand.cdr().cdr().equals(ListNode.EMPTYLIST))) {
                        rear = runExpr(operand.cdr().cdr().car());
                        if (runExpr(((ListNode) rear).car()).equals(BooleanNode.TRUE_NODE)) {
                            return runExpr(((ListNode) rear).cdr().car());
                        }
                    }
                }
                return BooleanNode.FALSE_NODE;
            case EQ_Q:
                if((operand.car() instanceof IdNode||operand.car() instanceof BooleanNode)&&(operand.cdr().car() instanceof IdNode||operand.cdr().car() instanceof BooleanNode)){
                    front=runExpr(operand.car());
                    if(front instanceof ListNode)
                        front = runQuote((ListNode)front);
                    rear=runExpr(operand.cdr().car());
                    if(rear instanceof ListNode)
                        rear = runQuote((ListNode)rear);
                } else if(operand.car() instanceof IdNode||operand.car() instanceof BooleanNode){
                    front=runExpr(operand.car());
                    if(front instanceof ListNode)
                        front = runQuote((ListNode)front);
                    rear = runQuote((ListNode) (operand.cdr()).car());
                } else if(operand.cdr().car() instanceof IdNode||operand.cdr().car() instanceof BooleanNode){
                    front = runQuote((ListNode) operand.car());
                    rear=runExpr(operand.cdr().car());
                    if(rear instanceof ListNode)
                        rear = runQuote((ListNode)rear);
                } else {
                    front = runQuote((ListNode) operand.car());
                    rear = runQuote((ListNode) (operand.cdr()).car());
                }

                if (front instanceof ListNode) {
                    if (front.equals(ListNode.EMPTYLIST) && rear.equals(ListNode.EMPTYLIST)) {
                        return (bn = BooleanNode.TRUE_NODE);
                    }
                    bn = BooleanNode.FALSE_NODE;
                } else if (rear instanceof ListNode) {
                    bn = BooleanNode.FALSE_NODE;
                } else {
                    if (((ValueNode) front).equals(rear)) {
                        bn = BooleanNode.TRUE_NODE;
                    } else {
                        bn = BooleanNode.FALSE_NODE;
                    }
                }
                return bn;
            case DEFINE:
                Node id = null;
                Node value;
                if (operand.car() instanceof IdNode || operand.car() instanceof BooleanNode) {
                    id=operand.car();
                }
                value = runExpr(operand.cdr().car());
                Table.insertTable(id.toString()+"", value);
                return null;
            case LAMBDA:
                return runExpr(operand);
            default:
                break;
        }
        return null;
    }

    private Node stripList(ListNode node) {
        if (node.car() instanceof ListNode && node.cdr() == ListNode.EMPTYLIST) {
            Node listNode = node.car();
            return listNode;
        } else {
            return node;
        }
    }

    private Node runBinary(ListNode list) {
        BinaryOpNode operator = (BinaryOpNode) list.car(); // 구현과정에서 필요한 변수 및 함수 작업 가능
        Node a, b;
        String str;
        int value;
        a = ((IntNode) runExpr(list.cdr().car()));
        b = ((IntNode) runExpr(list.cdr().cdr().car()));


        switch (operator.binType) { // +,-,/ 등에 대한 바이너리 연산 동작 구현
            case PLUS:
                value = ((IntNode) a).getValue() + ((IntNode) b).getValue();
                return new IntNode(Integer.toString(value));
            case MINUS:
                value = ((IntNode) a).getValue() - ((IntNode) b).getValue();
                return new IntNode(Integer.toString(value));
            case TIMES:
                value = ((IntNode) a).getValue() * ((IntNode) b).getValue();
                return new IntNode(Integer.toString(value));
            case DIV:
                value = ((IntNode) a).getValue() / ((IntNode) b).getValue();
                return new IntNode(Integer.toString(value));
            case LT:
                BooleanNode bn;
                if (((IntNode) a).getValue().compareTo(((IntNode) b).getValue()) == -1) {
                    bn = BooleanNode.TRUE_NODE;
                } else {
                    bn = BooleanNode.FALSE_NODE;
                }
                return bn;
            case GT:
                if (((IntNode) a).getValue().compareTo(((IntNode) b).getValue()) == 1) {
                    bn = BooleanNode.TRUE_NODE;
                } else {
                    bn = BooleanNode.FALSE_NODE;
                }
                return bn;
            case EQ:
                if (((IntNode) a).getValue().compareTo(((IntNode) b).getValue()) == 0) {
                    bn = BooleanNode.TRUE_NODE;
                } else {
                    bn = BooleanNode.FALSE_NODE;
                }
                return bn;
            default:
                break;
        }
        return null;
    }

    private Node runQuote(ListNode node) {
        return ((QuoteNode) node.car()).nodeInside();
    }
}