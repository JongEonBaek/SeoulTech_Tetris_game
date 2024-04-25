
package blocks;

import java.awt.*;

public class TimeBlock extends Block{

    public TimeBlock() {

        //2x3 크기의 2차원 정수 배열 설정
        shape = new int[][] {
                {3, 3, 3}, // 2는 B라고 하자.
                {0, 3, 0}
        };
        color = Color.WHITE;
    }
}