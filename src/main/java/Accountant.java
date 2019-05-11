import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Map;
import java.util.Deque;
import java.util.regex.Pattern;

public class Accountant extends Worker {
    protected String password;
    private static final Map<Character, String> unitNumDict;
    private static final Map<Character, String> decadeNumDict;
    private static final Map<Character, String> oneDecadeNumDict;
    private static final String[] numUnit = {"", "Thousand", "Million", "Billion"};
    private static final String ACCOUNTANT_DEPARTMENT = "Accountant";

    static {
        unitNumDict = new HashMap<>();
        decadeNumDict = new HashMap<>();
        oneDecadeNumDict = new HashMap<>();
        unitNumDict.put('0', "Zero");
        unitNumDict.put('1', "One");
        unitNumDict.put('2', "Two");
        unitNumDict.put('3', "Three");
        unitNumDict.put('4', "Four");
        unitNumDict.put('5', "Five");
        unitNumDict.put('6', "Six");
        unitNumDict.put('7', "Seven");
        unitNumDict.put('8', "Eight");
        unitNumDict.put('9', "Nine");
        decadeNumDict.put('2', "Twenty");
        decadeNumDict.put('3', "Thirty");
        decadeNumDict.put('4', "Forty");
        decadeNumDict.put('5', "Fifty");
        decadeNumDict.put('6', "Sixty");
        decadeNumDict.put('7', "Seventy");
        decadeNumDict.put('8', "Eighty");
        oneDecadeNumDict.put('0', "Ten");
        oneDecadeNumDict.put('1', "Eleven");
        oneDecadeNumDict.put('2', "Twelve");
        oneDecadeNumDict.put('3', "Thirteen");
        oneDecadeNumDict.put('5', "Fifteen");
        oneDecadeNumDict.put('4', "Fourteen");
        oneDecadeNumDict.put('6', "Sixteen");
        oneDecadeNumDict.put('7', "Seventeen");
        oneDecadeNumDict.put('8', "Eighteen");
        oneDecadeNumDict.put('9', "Nineteen");
    }

    public static boolean isAllNumber(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (!Character.isDigit(str.charAt(i))) {

                return false;
            }
        }
        return true;
    }


    public Accountant() {
    }

    //初始化Accountant
    public Accountant(String name, int age, int salary, String password) {
        super(name, age, salary, ACCOUNTANT_DEPARTMENT);
        this.password = password;
    }

    /**
     * 数字转换
     * 随着公司业务的开展，国际性业务也有相应的拓宽，
     * 会计们需要一个自动将数字转换为英文显示的功能。
     * 编辑们希望有一种简约的方法能将数字直接转化为数字的英文显示。
     *
     * 给定一个非负整数型输入，将数字转化成对应的英文显示，省略介词and
     * 正常输入为非负整数，且输入小于2^31-1;
     * 如果有非法输入（字母，负数，范围溢出等），返回illegal
     *
     * 示例：
     *
     * number: 2132866842
     * return: "Two Billion One Hundred Thirty Two Million Eight Hundred Sixty Six Thousand Eight Hundred Forty Two"
     *
     * number：-1
     * return："illegal"
     *
     * @param number
     */
    public String numberToWords(String number) {
        Deque<String> result = new ArrayDeque<>();
        if (!isAllNumber(number)) {
            return "illegal";
        }

        try {
            Integer.parseInt(number);
        } catch (Exception e) {
            return "illegal";
        }

        for (int i = number.length() - 1; i >= 0; i -= 3) {
            result.push(numUnit[(number.length() - 1 - i) / 3]);
            if (i - 1 < 0 || number.charAt(i - 1) == '0') {
                result.push(unitNumDict.get(number.charAt(i)));
                if (i - 1 < 0) {
                    break;
                }
            }
            //个位
            else if (number.charAt(i - 1) == '1') {
                result.push(oneDecadeNumDict.get(number.charAt(i)));//十
                if (i - 2 < 0) {
                    break;
                }
            } else {
                result.push(unitNumDict.get(number.charAt(i)));
                result.push(decadeNumDict.get(number.charAt(i - 1)));//十
                if (i - 2 < 0) {
                    break;
                }
            }
            result.add("Hundred");
            result.push(unitNumDict.get(number.charAt(i - 2)));
        }

        StringBuilder sb = new StringBuilder();
        while (!result.isEmpty()) {
            sb.append(result.pop());
            sb.append(" ");
        }
        String tmp = sb.toString();
        password = tmp.substring(0, tmp.length() - 2);
        return password;

    }

    private static final int LEGAL_PASSWORD = 0;
    private static final String PATTERN_PASSWORD = "^(?!\\d+$)(?![a-z]+$)(?![A-Z]+$)[0-9A-Za-z]{8,20}$";
    private static final String PATTERN_REPEAT = ".*(.)\\1{2,}.*";

    /**
     * 检验密码
     * 由于会计身份的特殊性，对会计的密码安全有较高的要求，
     * 会计的密码需要由8-20位字符组成；
     * 至少包含一个小写字母，一个大写字母和一个数字，不允许出现特殊字符；
     * 同一字符不能连续出现三次 (比如 "...ccc..." 是不允许的, 但是 "...cc...c..." 可以)。
     *
     * 示例：
     *
     * password: HelloWorld6
     * return: 0
     *
     * password: HelloWorld
     * return: 1
     *
     * @return 如果密码符合要求，则返回0;
     * 如果密码不符合要求，则返回将该密码修改满足要求所需要的最小操作数n，插入、删除、修改均算一次操作。
     */
    public int checkPassword() {
        // 通过正则表达式判断是否合法
        if (Pattern.matches(Accountant.PATTERN_PASSWORD, password)
                && !Pattern.matches(Accountant.PATTERN_REPEAT, password)) {
            return Accountant.LEGAL_PASSWORD;
        }

        int length = password.length();
        int changes = 0;
        int num = 0;
        int lower = 0;
        int higher = 0;
        int special = 0;
        for (int i = 0; i < password.length(); ++i) {
            Character cur = password.charAt(i);
            if (Character.isDigit(cur)) {
                ++num;
            } else if ('a' <= cur && 'z' >= cur) {
                ++lower;
            } else if ('A' <= cur && 'Z' >= cur) {
                ++higher;
            } else {
                ++special;
            }
        }
        // 计算需要改变的个数
        if (length < 7) {
            changes = 8 - length;
            changes += special;
        } else if (length == 7) {
            if (special == 0) {
                changes = 1;
                if ((num == 0 && lower == 0) || (lower == 0 && higher == 0) || (num == 0 && higher == 0)) {
                    changes += 1;
                }
            } else {
                changes = special + 3;
            }
        } else if (length > 20) {
            changes = length - 20;
        } else {
            changes = special;
        }
        return changes;
    }
}
