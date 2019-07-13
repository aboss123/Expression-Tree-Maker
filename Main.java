import java.util.ArrayList;

public class Main {

    private static final int EVAL = 0;
    private static final int LPAREN = 1;
    private static final int OP = 2;
    private static final int NUM = 3;
    private static final int RPAREN = 4;
    private static final int ERROR = 5;

    public static void main(String[] args) {
        String eval = "eval(3 * 3)";
        for (String s : genTree(eval)) {
            System.out.println(s);
        }
    }

    private static boolean isOp(String str, int pos) {
        return String.valueOf(str.charAt(pos)).matches("[\\\\*|\\\\+|\\\\/|\\\\-]");
    }

    private static ArrayList<String> genTree(String s) {
        String indent = "";
        int parenCount = 0;
        s = s.replaceAll(" ", "");
        ArrayList<String> tree = new ArrayList<>();
        int searchStatus = EVAL;

        do {
            switch (searchStatus) {
                case EVAL:
                    tree.add(indent + "EvalExpression");
                    s = s.substring(4);
                    searchStatus = LPAREN;
                    break;
                case LPAREN:
                    parenCount++;
                    indent += "    ";
                    tree.add(indent + "LParenthesis: (");
                    s = s.substring(1);
                    if (s.charAt(0) == '(') break;
                    else if (s.indexOf("eval") == 0) searchStatus = EVAL;
                    else if (Character.isDigit(s.charAt(0))) searchStatus = NUM;
                    else searchStatus = ERROR;
                    break;
                case NUM:
                    String num = "";
                    while (Character.isDigit(s.charAt(0))) {
                        num += s.charAt(0);
                        s = s.substring(1);
                    }
                    tree.add(indent + "NumberExpression");
                    indent += "    ";
                    tree.add(indent + "NumberToken: " + num);
                    if (isOp(s, 0)) {
                        indent = indent.substring(4);
                        searchStatus = OP;
                    } else if (s.charAt(0) == ')') {
                        indent = indent.substring(4);
                        searchStatus = RPAREN;
                    } else searchStatus = ERROR;
                    break;
                case OP:
                    tree.add(indent + "BinaryOperator: " + s.charAt(0));
                    s = s.substring(1);
                    if (Character.isDigit(s.charAt(0))) searchStatus = NUM;
                    else if (s.indexOf("eval") == 0) searchStatus = EVAL;
                    else searchStatus = ERROR;
                    break;
                case RPAREN:
                    parenCount--;
                    tree.add(indent + "RParenthesis: )");
                    s = s.substring(1);
                    if (s.length() > 0 && isOp(s, 0)) searchStatus = OP;
                    else if (s.length() > 0 && s.charAt(0) == ')') {
                        indent = indent.substring(4);
                        break;
                    } else searchStatus = ERROR;
                    break;
                case ERROR:
                    return new ArrayList<>();
            }
            if(parenCount < 0) return new ArrayList<>();
        } while (s.length() > 0);
        return tree;
    }
}
