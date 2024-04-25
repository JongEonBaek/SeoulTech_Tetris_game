package blocks;

import java.awt.*;

public class BombBlock extends Block{

    public BombBlock() {

        //2x3 크기의 2차원 정수 배열 설정
        shape = new int[][] {
                {2, 2}, // 2는 B라고 하자.
                {2, 2}
        };
        color = Color.WHITE;
    }
}
