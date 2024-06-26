package blocks;

import component.Board;

import java.awt.Color;

public class ZBlock extends Block {
	
	public ZBlock() {
		//2X3 크기의 2차원 정수 배열 설정
		shape = new int[][]{
				{1, 1, 0},
				{0, 1, 1}
		};
		if (Board.colorBlindMode) {
			color = new Color(50, 50, 150);
		} else {
			color = Color.RED;
		}
	}
}
