package cute19.interpreter.parser;

import cute19.interpreter.ast.*;
import cute19.interpreter.lexer.Scanner;
import cute19.interpreter.lexer.Token;
import cute19.interpreter.lexer.TokenType;

import java.io.FileNotFoundException;
import java.util.Iterator;

public class CuteParser {
    private Iterator<Token> tokens;
    private static Node END_OF_LIST = new Node() {
    };

    public CuteParser(String input) {
        try {
            tokens = Scanner.scan(input);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private Token getNextToken() {
        if (!tokens.hasNext()) {
            return null;
        }
        return tokens.next();
    }

    public Node parseExpr() {
        Token t = getNextToken();
        if (t == null) {
            System.out.println("No more token");
            return null;
        }
        TokenType tType = t.type();
        String tLexeme = t.lexme();
        switch (tType) {
            case ID:
                return new IdNode(tLexeme);
            case INT:
                if (tLexeme == null)
                    System.out.println("???");
                return new IntNode(tLexeme);
            case DIV:
            case EQ:
            case MINUS:
            case GT:
            case PLUS:
            case TIMES:
            case LT:
                BinaryOpNode binaryOpNode = new BinaryOpNode();
                binaryOpNode.setValue(tType);
                return binaryOpNode;

            case ATOM_Q:
            case CAR:
            case CDR:
            case COND:
            case CONS:
            case DEFINE:
            case EQ_Q:
            case LAMBDA:
            case NOT:
            case NULL_Q:
                FunctionNode functionNode = new FunctionNode();
                functionNode.setValue(tType);
                return functionNode;

            case FALSE:
                return BooleanNode.FALSE_NODE;
            case TRUE:
                return BooleanNode.TRUE_NODE;
            case L_PAREN:
                return parseExprList();
            case R_PAREN:
                return END_OF_LIST;
            case APOSTROPHE:
                QuoteNode quoteNode = new QuoteNode(parseExpr());
                ListNode listNode = ListNode.cons(quoteNode, ListNode.EMPTYLIST);
                return listNode;
            case QUOTE:
                return new QuoteNode(parseExpr());
            default:
                System.out.println("Parsing Error!");
                return null;
        }
    }

    private ListNode parseExprList() {
        Node head = parseExpr();
        if (head == null)
            return null;
        if (head == END_OF_LIST) // if next token is RPAREN
            return ListNode.EMPTYLIST;
        ListNode tail = parseExprList();
        if (tail == null)
            return null;
        return ListNode.cons(head, tail);
    }
}
