package calculator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.StringTokenizer;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Calculator {

    private static StringCalculator stringCalculator = new StringCalculator();

    public static void main(String[] args) throws IOException {
/** //;\n1;2;3
 * 1. 구분자를 파악해 낸다.
 * 2. 구분자로 각 영역을 배열로 구분해 내고 숫자를 list로 추출한다.
 * 3. list로 추출된 숫자들의 합을 구한다.
 */
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 2; i++) {
            sb.append(br.readLine()).append('\n');
        }

        // 0, 1의 두 정수를 생성한다.
        List<String> lines = IntStream.range(0, 2)
                .parallel() // 병렬 처리 (Multi Thread를 생성)
                .mapToObj(i -> { // 각 정수마다 해당 람다식을 호출
                    try {
                        return br.readLine();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toList()); // 스트림의 요소들을 리스트로 수집

        // lines를 스트림으로 변환 후 \n 개행 문자로 합침
        String data = lines.stream().collect(Collectors.joining("\n"));
        System.out.println(data);
        int sumV = stringCalculator.add(data);
        System.out.println("sumV = " + sumV);
    }
}