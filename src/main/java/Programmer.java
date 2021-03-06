import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Programmer extends Worker {
    private static final String SHOW_FORMAT = "My name is %s ; age : %d ; language : %s ; salary : %d.";

    private static final String DEPARTMENT_TYPE = "Programmer";
    private static final String TYPE_DEVELOP = "Develop";
    private static final String TYPE_TEST = "Test";
    private static final String TYPE_UI = "UI";

    private static final DecimalFormat BONUS_FORMAT = new DecimalFormat("#,###.00");

    private String language;
    private String type;

    public Programmer() {
    }

    // Programmer类的初始化
    public Programmer(String name, int age, int salary, String language,
                      String type) {
        super(name, age, salary, Programmer.DEPARTMENT_TYPE);
        this.language = language;
        this.type = type;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    // 按照规则计算当月的奖金
    public String getBonus(int overtime) {
        if (overtime < 0) {
            throw new IllegalArgumentException("Overtime illegal!");
        }
        double bonus = 0.0;
        int overtimeBonus = 0;
        switch (type) {
            case TYPE_DEVELOP:
                bonus = 0.2 * salary;
                overtimeBonus = overtime * 100;
                overtimeBonus = overtimeBonus > 500 ? 500 : overtimeBonus;
                break;
            case TYPE_TEST:
                bonus = 0.15 * salary;
                overtimeBonus = overtime * 150;
                overtimeBonus = overtimeBonus > 1000 ? 1000 : overtimeBonus;
                break;
            case TYPE_UI:
                bonus = 0.25 * salary;
                overtimeBonus = overtime * 50;
                overtimeBonus = overtimeBonus > 300 ? 300 : overtimeBonus;
                break;
            default:
                break;
        }
        bonus += overtimeBonus;
        return BONUS_FORMAT.format(bonus);
    }

    // 展示基本信息
    @Override
    public String show() {
        return String.format(Programmer.SHOW_FORMAT, name, age, language, salary);
    }

    private static final String EMAIL_PATTERN = "^[a-zA-Z0-9]{2,}@[a-zA-Z0-9]+(\\.[a-zA-Z0-9]+)+$";
    private static final String PHONE_PATTERN = "^[\\d\\+\\(\\)\\-\\s]+$";

    /**
     * 信息隐藏
     * <p>
     * 为了保护用户的隐私，系统需要将用户号码和邮箱隐藏起来。 对输入的邮箱或电话号码进行加密。 comment
     * 可能是一个邮箱地址，也可能是一个电话号码。
     * <p>
     * 1. 电子邮箱 邮箱格式为 str1@str2
     * 电子邮箱的名称str1是一个长度大于2.并且仅仅包含大小写字母和数字的字符串，名称str1后紧接符号@
     * 最后是邮箱所在的服务器str2,str2中可能包含多个. 如qq.com smail.nju.edu.cn等 邮箱地址是有效的，一个正确的示例为:
     * str1@smail.nju.edu.cn 为了隐藏电子邮箱，所有的str1和str2中的字母必须被转换成小写的，
     * 并且名称str1的第一个字和最后一个字的中间的所有字由 5 个 '*' 代替。 如果邮箱中含有非法字符或格式不正确，则返回illegal
     * <p>
     * 示例：
     * <p>
     * comment: "Qm@Qq.com"
     * <p>
     * return: "q*****m@qq.com"
     * <p>
     * 2. 电话号码 电话号码是一串包括数字 0-9，以及 {'+', '-', '(', ')', ' '} 这几个字符的字符串。
     * 你可以假设电话号码包含 10 到 13 个数字。 电话号码的最后 10 个数字组成本地号码，在这之前的数字组成国际号码。
     * 国际号码是可选的。我们只暴露最后 4 个数字并隐藏所有其他数字。 本地号码有格式，并且如 "***-***-1111" 这样显示，
     * 为了隐藏有国际号码的电话号码，像 "+111 111 111 1111"，我们以 "+***-***-***-1111"
     * 的格式来显示。在本地号码前面的 '+' 号 和第一个 '-' 号仅当电话号码中包含国际号码时存在。 例如，一个 12 位的电话号码应当以
     * "+**-" 开头进行显示。 注意：像 "("，")"，" " 这样的不相干的字符以及不符合上述格式的额外的减号或者加号都应当被删除。
     * 示例1:
     * <p>
     * comment: "1(234)567-890"
     * <p>
     * return: "***-***-7890"
     * <p>
     * 示例2:
     * <p>
     * comment: "86-(10)12345678"
     * <p>
     * return: "+**-***-***-5678"
     *
     * @param comment
     */
    public String hideUserinfo(String comment) {
        if (Pattern.matches(EMAIL_PATTERN, comment)) {
            return hideEmail(comment);
        } else if (Pattern.matches(PHONE_PATTERN, comment)) {
            return hidePhone(comment);
        } else {
            return "illegal";
        }
    }

    /**
     * 隐藏邮箱
     * @param email 邮箱字符串
     * @return 隐藏信息后的邮箱
     */
    private String hideEmail(String email) {
        String[] tokens = email.split("@");
        String lhs = tokens[0].toLowerCase();
        String rhs = tokens[1].toLowerCase();
        StringBuilder sb = new StringBuilder();
        sb.append(lhs.charAt(0));
        sb.append("*****");
        sb.append(lhs.charAt(lhs.length() - 1));
        sb.append('@');
        sb.append(rhs);
        return sb.toString();
    }

    /**
     * 隐藏电话号码
     * @param phone 电话字符串
     * @return 隐藏信息后的电话号码
     */
    private String hidePhone(String phone) {
        Pattern pattern = Pattern.compile("[^\\d]");
        Matcher matcher = pattern.matcher(phone);
        String numbers = matcher.replaceAll("");
        if (numbers.length() < 10 || numbers.length() > 13) {
            return "illegal";
        }
        int length = numbers.length();
        StringBuilder sb = new StringBuilder();
        if (numbers.length() > 10) {
            sb.append("+");
            for (int i = 0; i < length - 10; i++) {
                sb.append('*');
            }
            sb.append('-');
        }
        sb.append("***-***-");
        // append visible numbers
        sb.append(numbers.substring(length - 4));
        return sb.toString();
    }
}
