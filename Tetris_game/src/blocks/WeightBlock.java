package blocks;

import java.awt.*;

public class WeightBlock extends Block{
    public WeightBlock() {

        //2X3 크기의 2차원 정수 배열 설정
        shape = new int[][] {
                {0, 1, 1, 0},
                {1, 1, 1, 1}
        };
        color = Color.WHITE;
    }

}
