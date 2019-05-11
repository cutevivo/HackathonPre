import java.text.Collator;
import java.util.*;
import java.util.List;


public class Editor extends Worker {

    public Editor() {
        super();
    }

    //初始化Editor
    public Editor(String name, int age, int salary) {
        super(name, age, salary, "Editor");
    }

    /**
     * 文本对齐
     *
     * 根据统计经验，用户在手机上阅读英文新闻的时候，
     * 一行最多显示32个字节（1个中文占2个字节）时最适合用户阅读。
     * 给定一段字符串，重新排版，使得每行恰好有32个字节，并输出至控制台
     * 首行缩进4个字节，其余行数左对齐，每个短句不超过32个字节，
     * 每行最后一个有效字符必须为标点符号
     *
     * 示例：
     *
     * String：给定一段字符串，重新排版，使得每行恰好有32个字符，并输出至控制台首行缩进，其余行数左对齐，每个短句不超过32个字符。
     *
     * 控制台输出:
     *     给定一段字符串，重新排版，
     * 使得每行恰好有32个字符，
     * 并输出至控制台首行缩进，
     * 其余行数左对齐，
     * 每个短句不超过32个字符。
     * @param data 待分行字符串
     */
    public void textExtraction(String data){
        int strLen = data.length();
        List<String> reformedBlocks = new ArrayList<String>();
        String temp = "";
        int tempLen = 0;
        int beginIndex = 0;
        int lastSymbolIndex = 0;
        for (int i = 0; i < strLen; i++){
            char currentCharacter = data.charAt(i);
            if (isChinesePunctuation(currentCharacter)){
                lastSymbolIndex = i;
                tempLen += 1;
            } else{
                tempLen += 2;
            }
            temp = temp + currentCharacter;
            if(tempLen >= 32){
                int endIndex = lastSymbolIndex + 1;
                String splitBlock = temp.substring(0, endIndex - beginIndex);
                reformedBlocks.add(splitBlock);
                temp = "";
                tempLen = 0;
                i = lastSymbolIndex;
                beginIndex = lastSymbolIndex + 1;
            }
        }
        reformedBlocks.add(temp);
        String firstBlock = reformedBlocks.get(0);
        System.out.println("    " + firstBlock);
        for(int j = 1; j < reformedBlocks.size(); j++){
            System.out.println(reformedBlocks.get(j));
        }
    }


    /**
     * 标题排序
     *
     * 将给定的新闻标题按照拼音首字母进行排序，
     * 首字母相同则按第二个字母，如果首字母相同，则首字拼音没有后续的首字排在前面，如  鹅(e)与二(er)，
     *            以鹅为开头的新闻排在以二为开头的新闻前。
     * 如果新闻标题第一个字的拼音完全相同，则按照后续单词进行排序。如 新闻1为 第一次...  新闻2为 第二次...，
     *            则新闻2应该排在新闻1之前。
     * 示例：
     *
     * newsList：我是谁；谁是我；我是我
     *
     * return：谁是我；我是谁；我是我；
     *
     * @param newsList
     * @return 按拼音排序后的新闻标题列表
     */
    public ArrayList<String> newsSort(ArrayList<String> newsList){
        Comparator<Object> com = Collator.getInstance(Locale.CHINA);
        Collections.sort(newsList, com);
        return newsList;
    }


    /**
     * 热词搜索
     *
     * 根据给定的新闻内容，找到文中出现频次最高的一个词语，词语长度最少为2（即4个字节），最多为10（即20个字节），且词语中不包含标点符号，可以出现英文，同频词语选择在文中更早出现的词语。
     *
     * 示例：
     *
     * String: 今天的中国，呈现给世界的不仅有波澜壮阔的改革发展图景，更有一以贯之的平安祥和稳定。这平安祥和稳定的背后，凝聚着中国治国理政的卓越智慧，也凝结着中国公安民警的辛勤奉献。
     *
     * return：中国
     *
     * @param newsContent
     */
    public String findHotWords(String newsContent){
        return newsContent;

    }

    /**
     *
     *相似度对比
     *
     * 为了检测新闻标题之间的相似度，公司需要一个评估字符串相似度的算法。
     * 即一个新闻标题A修改到新闻标题B需要几步操作，我们将最少需要的次数定义为 最少操作数
     * 操作包括三种： 替换：一个字符替换成为另一个字符，
     *              插入：在某位置插入一个字符，
     *              删除：删除某位置的字符
     *  示例：
     *      中国队是冠军  -> 我们是冠军
     *      最少需要三步来完成。第一步删除第一个字符  "中"
     *                       第二步替换第二个字符  "国"->"我"
     *                       第三步替换第三个字符  "队"->"们"
     *      因此 最少的操作步数就是 3
     *
     * 定义相似度= 1 - 最少操作次数/较长字符串的长度
     * 如在上例中：相似度为  (1 - 3/6) * 100= 50.00（结果保留2位小数，四舍五入，范围在0.00-100.00之间）
     *
     *
     * @param lhs
     * @param rhs
     */
    public double minDistance(String lhs, String rhs){
        if(lhs.equals(rhs)) {
            return 0.0;
        }
        int steps = editDistance(lhs, rhs);
        double similarity = (1.0 - (double)steps / Math.max(lhs.length(), rhs.length())) * 100;
        return similarity;

    }

    /**
     * 以动态规划的方式计算两个字符串的编辑距离。
     *
     * @param lhs 待比较字符串。
     * @param rhs 待比较字符串。
     * @return 两字符串的编辑距离。
     */
    private int editDistance(String lhs, String rhs) {

        int dp[][] = new int[lhs.length() + 1][rhs.length() + 1];

        for (int i = 0; i < lhs.length() + 1; i++) {
            dp[i][0] = i;
        }
        for (int i = 0; i < rhs.length() + 1; i++) {
            dp[0][i] = i;
        }

        for (int i = 1; i < lhs.length() + 1; i++) {
            for (int j = 1; j < rhs.length() + 1; j++) {
                if (lhs.charAt(i - 1) == rhs.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    dp[i][j] = 1 + Math.min(dp[i - 1][j - 1], Math.min(dp[i - 1][j], dp[i][j - 1]));
                }
            }
        }
        return dp[lhs.length()][rhs.length()];
    }

    /**
     * 判断字符是否是标点符号
     *
     * @param c 需判断字符
     * @return 表示是否是标点符号的布尔值
     */
    public boolean isChinesePunctuation(char c) {
        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
        if (ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_FORMS
                || ub == Character.UnicodeBlock.VERTICAL_FORMS) {
            return true;
        } else {
            return false;
        }
    }
}
