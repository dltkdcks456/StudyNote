package calculator;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringCalculator {

    public int add(String data) {
        // null과 isEmpty()의 차이
        if (isBlank(data)) {
            return 0;
        }

        return getSumV(toInts(split(data)));
    }

    private static String[] split(String data) {
        Matcher matcher = Pattern.compile("//(.+)\n(.*)").matcher(data);
        if (matcher.find()) {
            String customDelimeter = matcher.group(1);
            return matcher.group(2).split(Pattern.quote(customDelimeter));
        }
        return data.split(",|:");
    }

    private static boolean isBlank(String data) {
        return data == null || data.isEmpty();
    }

    private static int[] toInts(String[] data) {
        int[] temp = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            int num = Integer.parseInt(data[i]);
            checkNegative(num);
            temp[i] = Integer.parseInt(data[i]);
        }
        return temp;
    }

    private static void checkNegative(int num) {
        if (num < 0) {
            throw new RuntimeException("입력값은 항상 0이상 이어야 합니다.");
        }
    }

    private static int getSumV(int[] tokens) {
        int sumV = 0;
        for (int number : tokens) {
            sumV += number;
        }
        return sumV;
    }


//    public String getIdentifier(String data) {
//        String tempData = data.replace("//", "").replace("\n", "");
//        String identifier = tempData.substring(0, 1);
//        return identifier;
//    }
//
//    public ArrayList<Integer> getList(String data, String identifier) {
//        String[] splitData = data
//                .replace("//", "")
//                .replace("\\n","")
//                .split(identifier);
//        ArrayList<Integer> numbers = new ArrayList<>();
//        for (int i = 1; i < splitData.length; i++) {
//            numbers.add(Integer.valueOf(splitData[i]));
//        }
//        return numbers;
//    }
//
//    public int getSum(ArrayList<Integer> numbers) {
//        return numbers.stream()
//                .reduce(0, Integer::sum);
//    }
}
