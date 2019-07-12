import java.util.ArrayList;

public class Main {

    private static final int EVAL = 0;
    private static final int LPAREN = 1;
    private static final int OP = 2;
    private static final int NUM = 3;
    private static final int RPAREN = 4;

    public static void main(String[] args) {
        String eval = "eval(eval(33/3 + eval(2 * 2 + 1)) + eval(eval(2 + 1) * 3))";
        for(String s : solution2(eval)) {
            System.out.println(s);
        }
    }

    private static boolean isOp(String str, int pos) {
        return String.valueOf(str.charAt(pos)).matches("[\\\\*|\\\\+|\\\\/|\\\\-]");
    }

    private static ArrayList<String> solution2(String s) {
        if(!solution(s)) return null;
        String indent = "";
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
                    indent += "    ";
                    tree.add(indent + "LParenthesis: (");
                    s = s.substring(1);
                    if(s.charAt(0) == '(') break;
                    else if(s.indexOf("eval") == 0) {
                        searchStatus = EVAL;
                        break;
                    }
                    else searchStatus = NUM;
                    break;
                case NUM:
                    String num = "";
                    while(Character.isDigit(s.charAt(0))) {
                        num += s.charAt(0);
                        s = s.substring(1);
                    }
                    tree.add(indent + "NumberExpression");
                    indent += "    ";
                    tree.add(indent + "NumberToken: " + num);
                    if(isOp(s, 0)) {
                        indent = indent.substring(4);
                        searchStatus = OP;
                        break;
                    }
                    else {
                        indent = indent.substring(4);
                        searchStatus = RPAREN;
                        break;
                    }
                case OP:
                    tree.add(indent + "BinaryOperator: " + s.charAt(0));
                    s = s.substring(1);
                    if(Character.isDigit(s.charAt(0))) {
                        searchStatus = NUM;
                        break;
                    }
                    else if(s.indexOf("eval") == 0) {
                        searchStatus = EVAL;
                        break;
                    }
                    break;
                case RPAREN:
                    tree.add(indent + "RParenthesis: )");
                    s = s.substring(1);
                    if(s.length() > 0 && isOp(s, 0)) {
                        searchStatus = OP;
                        break;
                    }
                    indent = indent.substring(4);
                    break;
            }
        } while(s.length() > 0);
        return tree;
    }

    private static boolean solution(String s) {
        if(occurs("\\(", s) != occurs("\\)", s) || !s.contains("(") || !s.contains(")")) {
            return false;
        }
        s = s.replaceAll("eval", "");
        s = s.replaceAll("\\s", "");
        for(int i = 0; i < s.length(); i++) {
            switch (s.charAt(i)) {
                case '(':
                    if(i == s.length() - 1) return false;
                    else if(!Character.isDigit(s.charAt(i + 1)) && s.charAt(i + 1) != '(') return false;
                    break;
                case ')':
                    if(i == s.length() - 1) break;
                    if(s.charAt(i + 1) != ')' && !isOp(s, i + 1) ) return false;
                    break;
                default:
                    if(Character.isDigit(s.charAt(i)) && !Character.isDigit(s.charAt(i + 1)) && !isOp(s, i + 1) && s.charAt(i + 1) != ')') return false;
                    break;
            }
        }
        return true;
    }

    private static int occurs(String check, String str) {
        int count = 0;
        do {
            str = str.replaceFirst(check, "");
            count++;
        } while (str.contains(check));
        return count;
    }
}
