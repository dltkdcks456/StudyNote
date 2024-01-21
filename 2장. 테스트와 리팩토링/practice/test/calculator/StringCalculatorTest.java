package calculator;

import org.junit.jupiter.api.*;

@DisplayName("계산관련 메서드 테스트 실행")
public class StringCalculatorTest {

    private StringCalculator stringCalculator;

    @BeforeEach
    void setup() {
        stringCalculator = new StringCalculator();
    }

    @Test
    @DisplayName("null이나 빈 문자를 입력을 받을 경우")
    void add_null_또는_빈문자() {
        // given
        String data1 = "";
        String data2 = null;

        // when
        int result1 = stringCalculator.add(data1);
        int result2 = stringCalculator.add(data2);

        // then
        Assertions.assertEquals(result1, 0);
        Assertions.assertEquals(result2, 0);
    }

    @Test
    @DisplayName("숫자 하나를 입력 받았을 경우")
    void addSingleNum() {
        //given
        String data = "1";
        //when
        int result = stringCalculator.add(data);
        //then
        Assertions.assertEquals(result, 1);
    }

    @Test
    @DisplayName("숫자 2개를 입력 받았을 경우 합을 반환한다.")
    void addTwoNums() {
        //given
        String data = "1,2";
        //when
        int result = stringCalculator.add(data);
        //then
        Assertions.assertEquals(result, 3);
    }

    @Test
    @DisplayName(",|:의 3가지 구분자가 포함된 데이터의 합을 반환")
    void addDiffIdentifierNums() {
        //given
        String data = "1,2:3";
        //when
        int result = stringCalculator.add(data);
        //then
        Assertions.assertEquals(result, 6);
    }

    @Nested
    @DisplayName("음수를 넣을 경우 에러 발생")
    class ErrorTypeWhenInputNegative {

        @Test
        @DisplayName("Junit5 예외 처리 활용")
        void negativeNumException1() {
            //given
            String data = "//;\n1;2;-3";
            //when
            //then
            Assertions.assertThrows(RuntimeException.class, () -> {
                stringCalculator.add(data);
            });
        }
    }
}
