package component;

import blocks.*;

import javax.swing.*;
import javax.swing.Timer;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.*;

import Menu.Main;

import org.json.simple.JSONObject;
import org.json.simple.JSONArray;
import org.json.simple.parser.JSONParser;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.List;


// JFrame 상속받은 클래스 Board
public class Board3 extends JPanel {

	public static final int HEIGHT = 20; // 높이
	//직렬화 역직렬화 과정에서 클래스 버전의 호환성 유지하기 위해 사용됨.
	public static final int WIDTH = 10; // 너비
	public static final char BORDER_CHAR = 'X'; //게임 테두리 문자
	private static final long serialVersionUID = 2434035659171694595L; // 이 클래스의 고유한 serialVersionUID
	private int initInterval = 1000; //블록이 자동으로 아래로 떨어지는 속도 제어 시간, 현재 1초
	private final JTextPane pane; //게임 상태 표시하는 JTextPane 객체
	private final KeyListener playerKeyListener; // 사용자의 키 입력을 처리하는 KeyListener 객체
	private final SimpleAttributeSet styleSet; // 텍스트 스타일 설정하는 SimpleAttributeSet
	public final Timer timer; // 블록이 자동으로 아래로 떨어지게 하는 Timer
	int x = 3; //Default Position. 현재 블록 위치
	int y = 0; // 현재 블록 위치
	int point = 1; // 한칸 떨어질때 얻는 점수
	int scores = 0; // 현재 스코어
	int level = 0; // 현재 레벨
	int lines = 0; // 현재 지워진 라인 수
	int bricks = 0; // 생성된 벽돌의 개수
	String name;

	private boolean isPaused = false; // 게임이 일시 중지되었는지 나타내는 변수
	public static boolean colorBlindMode; // 색맹모드

	private JTextPane nextpane;// 넥스트블록 표시하는 판
	private int[][] board; // 게임 보드의 상태를 나타내는 2차원 배열

	private Color[][] color_board;
	private Block curr; // 현재 움직이고 있는 블록
	private Block nextcurr; // 다음 블럭

	private String curr_name = "";
	private String nextcurr_name = "";

	public int mode = 1; // 난이도 설정 easy == 0, normal == 1, hard == 2;
	public int item = 0; // itemMode 0 == false(보통모드), 1 == true(아이템모드);
	public boolean gameOver = false; // 게임오버를 알려주는변수 true == 게임오버

	public boolean weightblockLock = false;

	boolean create_item = true;

	// 생성자 Board, 게임 창 설정 및 초기게임 보드 준비, 첫 번째 블록 생성하고, 타이머 시작
	public Board3() {
		this.colorBlindMode = Main.isColorBlindnessMode;
		//Board display setting.
		pane = new JTextPane(); // 텍스트 패널 생성
		pane.setEditable(false); // 텍스트 패널 편집 불가하도록 설정
		pane.setBackground(Color.BLACK); // 텍스트 패널의 배경색을 검은색으로 설정
		CompoundBorder border = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY, 10),
				BorderFactory.createLineBorder(Color.DARK_GRAY, 5)); // 복합 테두리 생성
		pane.setBorder(border); // 텍스트 패널에 테두리를 설정
		Border innerPadding = new EmptyBorder(0, 0, 0, 0); // 상단, 왼쪽, 하단, 오른쪽 여백 설정
		pane.setPreferredSize(new Dimension(Main.SCREEN_WIDTH[3]/2 - 20, Main.SCREEN_HEIGHT[3] - 50)); // 가로 300, 세로 200의 크기로 설정


		// 기존 복합 테두리와 내부 여백을 결합한 새로운 복합 테두리 생성
		CompoundBorder newBorder = new CompoundBorder(border, innerPadding);



		// 텍스트 패널에 새로운 테두리 설정
		pane.setBorder(newBorder);
		this.add(pane, BorderLayout.WEST); // 텍스트 패널을 창의 west에 추가.this는 Board클래스의 인스턴스를 지칭
		sideBoard(); // textpane인 sideBoard 생성

		//Document default style.
		styleSet = new SimpleAttributeSet(); // 스타일 설정을 위한 객체 생성
		StyleConstants.setFontSize(styleSet, 19); // 폰트 크기를 18로 설정
		StyleConstants.setFontFamily(styleSet, "consolas");// 폰트 종류를 mac은 Courier로 설정, window는 consolas로 설정
		StyleConstants.setBold(styleSet, true); // 폰트를 굵게 설정
		StyleConstants.setForeground(styleSet, Color.WHITE); // 폰트 색상을 흰색으로 설정
		StyleConstants.setAlignment(styleSet, StyleConstants.ALIGN_CENTER); // 텍스트 정렬을 가운데로 설정


		//Set timer for block drops.
		timer = new Timer(initInterval, new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				moveDown(); // 블록 아래로 이동
				drawBoard(); // 보드 그리기

			}
		});

		//Initialize board for the game.
		board = new int[HEIGHT][WIDTH]; // 게임 보드 초기화
		color_board = new Color[HEIGHT][WIDTH];
		for(int i=0;i<HEIGHT;i++){
			for(int j=0;j<WIDTH;j++){
				color_board[i][j]=Color.white;
			}
		} // color_board 초기화
		playerKeyListener = new PlayerKeyListener(); // 플레이어 키 리스너를 생성
		addKeyListener(playerKeyListener); //키 리스너 추가
		setFocusable(true); // 키 입력을 받을 수 있도록 설정
		requestFocus(); //  입력 포커스 요청

		//Create the first block and draw.
		curr = getRandomBlock(); // 첫 번째 블록을 무작위로 선택
		bricks--;

		nextcurr = getRandomBlock(); // 다음 블록을 무작위로 선택

		placeBlock(); //  선택된 블록을 배치합니다.
		drawBoard(); // 보드를 그린다.
		// timer.start(); // 타이머 시작
	}



	private Block getRandomBlock() {
		Random rnd = new Random(System.currentTimeMillis()); // 현재 시간 기준으로 랜덤 객체 생성
		bricks++;
		setLevel();
		int slot = 0;

		if(item == 0)
		{
			switch (mode) {

				case 0: //easy
					slot = rnd.nextInt(36); // 0부터 35사이의 난수를 생성 (총 36개 슬롯) 6 : 5 : 5 : 5 : 5 : 5 :5
					if (slot < 6) { // 0번 블럭을 6번 포함 (0, 1, 2, 3, 4, 5) 6
						return new IBlock(); // I 모양 블록 생성 반환
					} else if (slot < 11) { // 1번 블럭을 5번 포함 (6, 7, 8, 9, 10) 5
						return new JBlock(); // J 모양 블록 생성 반환
					} else if (slot < 16) { // 2번 블럭을 5번 포함 (11, 12, 13, 14, 15)5
						return new LBlock(); // L 모양 블록 생성 반환
					} else if (slot < 21) { // 3번 블럭을 5번 포함 (16, 17, 18, 19, 20)5
						return new ZBlock(); // Z 모양 블록 생성 반환
					} else if (slot < 26) { // 4번 블럭을 5번 포함 (21, 22, 23, 24, 25)5
						return new SBlock(); // S 모양 블록 생성 반환
					} else if (slot < 31) { // 5번 블럭을 5번 포함 (26, 27, 28, 29, 30)5
						return new TBlock(); // T 모양 블록 생성 반환
					} else { // 나머지는 6번 블럭 (31, 32, 33, 34, 35)
						return new OBlock(); // O 모양 블록 생성 반환
					}
				case 1: //normal
					slot = rnd.nextInt(7);
					if (slot == 0) { // 0번 블럭을 4번 포함 (0, 1, 2, 3)
						return new IBlock();
					} else if (slot == 1) { // 1번 블럭
						return new JBlock();
					} else if (slot == 2) { // 2번 블럭
						return new LBlock();
					} else if (slot == 3) { // 3번 블럭
						return new ZBlock();
					} else if (slot == 4) { // 4번 블럭
						return new SBlock();
					} else if (slot == 5) { // 5번 블럭
						return new TBlock();
					} else { // 나머지는 6번 블럭
						return new OBlock();
					}
				case 2: //hard //8 : 10 : 10 : 10 : 10 : 10 : 10  -> 4 : 5 : 5 : 5 : 5 : 5 :5
					slot = rnd.nextInt(34); // 0부터 33사이의 난수를 생성 (총 34개 슬롯)
					if (slot < 4) { // 0번 블럭을 4번 포함 (0, 1, 2, 3)
						return new IBlock();
					} else if (slot < 9) { // 1번 블럭을 5번 포함 (4, 5, 6, 7, 8)
						return new JBlock();
					} else if (slot < 14) { // 2번 블럭을 5번 포함 (9, 10, 11, 12, 13)
						return new LBlock();
					} else if (slot < 19) { // 3번 블럭을 5번 포함 (14, 15, 16, 17, 18)
						return new ZBlock();
					} else if (slot < 24) { // 4번 블럭을 5번 포함 (19, 20, 21, 22, 23)
						return new SBlock();
					} else if (slot < 29) { // 5번 블럭을 5번 포함 (24, 25, 26, 27, 28)
						return new TBlock();
					} else { // 나머지는 6번 블럭 (29, 30, 31, 32, 33)
						return new OBlock();
					}
			}
		}
		else if(item == 1)
		{
			System.out.println(bricks);
			if(create_item && lines != 0 && lines % 10 == 0) // 일단은 10번째마다 무게추 블록이 나오도록. 나중에 변경 예정.
			{
				create_item = false;
				slot = rnd.nextInt(5);
				if(slot == 0) {
					curr_name = nextcurr_name;
					nextcurr_name = "WeightBlock";
					return new WeightBlock();
				}
				else if(slot == 1)
				{
					curr_name = nextcurr_name;
					nextcurr_name = "BombBlock";
					return new BombBlock();
				}
				else if(slot == 2)
				{
					curr_name = nextcurr_name;
					nextcurr_name = "TimeBlock";
					return new TimeBlock();
				}
				else if(slot == 3)
				{
					item = 0;
					Block temp = getRandomBlock();
					bricks--;
					replaceOneWithL(temp.shape);
					item = 1;
					curr_name = nextcurr_name;
					nextcurr_name = "ItemLBlock";
					return temp;
				}
				else if(slot == 4)
				{
					item = 0;
					Block temp = getRandomBlock();
					bricks--;
					replaceOneWithV(temp.shape);
					item = 1;
					curr_name = nextcurr_name;
					nextcurr_name = "ItemVBlock";
					return temp;
				}
			}
			switch (mode) {

				case 0: //easy
					slot = rnd.nextInt(36); // 0부터 35사이의 난수를 생성 (총 36개 슬롯) 6 : 5 : 5 : 5 : 5 : 5 :5
					if (slot < 6) { // 0번 블럭을 6번 포함 (0, 1, 2, 3, 4, 5) 6
						curr_name = nextcurr_name;
						nextcurr_name = "I";
						return new IBlock(); // I 모양 블록 생성 반환
					} else if (slot < 11) { // 1번 블럭을 5번 포함 (6, 7, 8, 9, 10) 5
						curr_name = nextcurr_name;
						nextcurr_name = "J";
						return new JBlock(); // J 모양 블록 생성 반환
					} else if (slot < 16) { // 2번 블럭을 5번 포함 (11, 12, 13, 14, 15)5
						curr_name = nextcurr_name;
						nextcurr_name = "L";
						return new LBlock(); // L 모양 블록 생성 반환
					} else if (slot < 21) { // 3번 블럭을 5번 포함 (16, 17, 18, 19, 20)5
						curr_name = nextcurr_name;
						nextcurr_name = "Z";
						return new ZBlock(); // Z 모양 블록 생성 반환
					} else if (slot < 26) { // 4번 블럭을 5번 포함 (21, 22, 23, 24, 25)5
						curr_name = nextcurr_name;
						nextcurr_name = "S";
						return new SBlock(); // S 모양 블록 생성 반환
					} else if (slot < 31) { // 5번 블럭을 5번 포함 (26, 27, 28, 29, 30)5
						curr_name = nextcurr_name;
						nextcurr_name = "T";
						return new TBlock(); // T 모양 블록 생성 반환
					} else { // 나머지는 6번 블럭 (31, 32, 33, 34, 35)
						curr_name = nextcurr_name;
						nextcurr_name = "O";
						return new OBlock(); // O 모양 블록 생성 반환
					}
				case 1: //normal
					slot = rnd.nextInt(7);
					if (slot == 0) { // 0번 블럭을 4번 포함 (0, 1, 2, 3)
						curr_name = nextcurr_name;
						nextcurr_name = "I";
						return new IBlock();
					} else if (slot == 1) { // 1번 블럭
						curr_name = nextcurr_name;
						nextcurr_name = "J";
						return new JBlock();
					} else if (slot == 2) { // 2번 블럭
						curr_name = nextcurr_name;
						nextcurr_name = "L";
						return new LBlock();
					} else if (slot == 3) { // 3번 블럭
						curr_name = nextcurr_name;
						nextcurr_name = "Z";
						return new ZBlock();
					} else if (slot == 4) { // 4번 블럭
						curr_name = nextcurr_name;
						nextcurr_name = "S";
						return new SBlock();
					} else if (slot == 5) { // 5번 블럭
						curr_name = nextcurr_name;
						nextcurr_name = "T";
						return new TBlock();
					} else { // 나머지는 6번 블럭
						curr_name = nextcurr_name;
						nextcurr_name = "O";
						return new OBlock();
					}
				case 2: //hard //8 : 10 : 10 : 10 : 10 : 10 : 10  -> 4 : 5 : 5 : 5 : 5 : 5 :5
					slot = rnd.nextInt(34); // 0부터 33사이의 난수를 생성 (총 34개 슬롯)
					if (slot < 4) { // 0번 블럭을 4번 포함 (0, 1, 2, 3)
						curr_name = nextcurr_name;
						nextcurr_name = "I";
						return new IBlock();
					} else if (slot < 9) { // 1번 블럭을 5번 포함 (4, 5, 6, 7, 8)
						curr_name = nextcurr_name;
						nextcurr_name = "J";
						return new JBlock();
					} else if (slot < 14) { // 2번 블럭을 5번 포함 (9, 10, 11, 12, 13)
						curr_name = nextcurr_name;
						nextcurr_name = "L";
						return new LBlock();
					} else if (slot < 19) { // 3번 블럭을 5번 포함 (14, 15, 16, 17, 18)
						curr_name = nextcurr_name;
						nextcurr_name = "Z";
						return new ZBlock();
					} else if (slot < 24) { // 4번 블럭을 5번 포함 (19, 20, 21, 22, 23)
						curr_name = nextcurr_name;
						nextcurr_name = "S";
						return new SBlock();
					} else if (slot < 29) { // 5번 블럭을 5번 포함 (24, 25, 26, 27, 28)
						curr_name = nextcurr_name;
						nextcurr_name = "T";
						return new TBlock();
					} else { // 나머지는 6번 블럭 (29, 30, 31, 32, 33)
						curr_name = nextcurr_name;
						nextcurr_name = "O";
						return new OBlock();
					}
			}
		}
		return null;
	}


	private void eraseCurr() {
		// 블록이 이동하거나 회전할 때 이전위치의 블록을 지우는 기능을 수행하는 메소드
		for (int i = x; i < x + curr.width(); i++) {// 현재 블록의 너비만큼 반복합니다.
			for (int j = y; j < y + curr.height(); j++) {// 현재 블록의 높이만큼 반복합니다.
				if (curr.getShape(i - x, j - y) != 0 && board[j][i] != 0) {// 현재 블록의 일부인 경우에만 발동
					board[j][i] = 0;// 게임 보드에서 현재 블록의 위치를 0으로 설정하여 지웁니다.

				}
			}
		}
	}



	private void checkLines() {
		for (int i = HEIGHT - 1; i >= 0; i--) {
			boolean lineFull = true;
			for (int j = 0; j < WIDTH; j++) {
				if (board[i][j] == 0) {
					lineFull = false;
					break;
				}
			}
			if (lineFull) {
				for (int k = i; k > 0; k--) {
					board[k] = Arrays.copyOf(board[k - 1], WIDTH);
					color_board[k] = Arrays.copyOf(color_board[k - 1], WIDTH);
				}
				Arrays.fill(board[0], 0);
				Arrays.fill(color_board[0], Color.WHITE);
				scores += 100;
				lines++; // 완성된 라인 수 증가
				create_item = true;
				i++; // 줄을 지운 후, 같은 줄을 다시 검사하기 위해 i 값을 증가시킵니다.
			}
		}
	}




	// 현재 블록을 아래로 이동할 수 있는지 확인하는 메소드
	private boolean canMoveDown() {
		// 블럭이 아래로 내려갈 수 있는지 확인하는 메소드
		if (y + curr.height() == HEIGHT) return false; // 바닥에 닿은 경우

		for (int i = 0; i < curr.width(); i++) {
			for (int j = 0; j < curr.height(); j++) {
				if (curr.getShape(i, j) != 0) { // 블록의 일부인 경우
					if (board[y + j + 1][x + i] != 0) { // 아래 칸이 비어있지 않은 경우
						if(curr_name.equals("WeightBlock"))
						{
							weightblockLock = true;
							return true; // 무게추 블록이면 여기선 true임.
						}
						weightblockLock = false;
						return false; // 이동할 수 없음
					}
				}
			}
		}
		return true; // 모든 검사를 통과하면 이동할 수 있음
	}

	protected boolean canMoveLeft() {
		// 블록을 왼쪽으로 이동할 수 있는지 확인하는 메소드
		// 이 메소드는 블록의 왼쪽에 다른 블록이 없고, 블록이 게임 보드의 왼쪽 경계를 넘지 않는 경우에만 true를 반환합니다.
		if(curr_name.equals("WeightBlock"))
		{
			if(weightblockLock)
				return false;
		}
		for (int i = 0; i < curr.height(); i++) {
			for (int j = 0; j < curr.width(); j++) {
				if (curr.getShape(j, i) != 0) {
					if (x + j - 1 < 0 || board[y + i][x + j - 1] != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected boolean canMoveRight() {
		// 블록을 오른쪽으로 이동할 수 있는지 확인하는 메소드
		// 블록의 오른쪽에 다른 블록이 없고, 블록이 게임 보드의 오른쪽 경계를 넘지 않는 경우에만 true를 반환합니다.
		if(curr_name.equals("WeightBlock")) {
			if (weightblockLock)
				return false;
		}
		for (int i = 0; i < curr.height(); i++) {
			for (int j = 0; j < curr.width(); j++) {
				if (curr.getShape(j, i) != 0) {
					if (x + j + 1 >= WIDTH || board[y + i][x + j + 1] != 0) {
						return false;
					}
				}
			}
		}
		return true;
	}

	protected boolean canRotate() {
		curr.rotate();
		for (int i = 0; i < curr.height(); i++) {
			for (int j = 0; j < curr.width(); j++) {
				if (curr.getShape(j, i) != 0) {
					if (x + j < 0 || x + j >= WIDTH || y + i < 0 || y + i >= HEIGHT || board[y + i][x + j] != 0) {
						curr.rotate();
						curr.rotate();
						curr.rotate();
						return false;
					}
				}
			}
		}
		curr.rotate();
		curr.rotate();
		curr.rotate();
		return true;
	}
	public static void replaceOneWithV(int[][] board) {
		// '1' 위치를 저장할 리스트 생성
		List<int[]> positions = new ArrayList<>();

		// 배열을 탐색하여 '1'의 위치를 찾는다
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 1) {
					positions.add(new int[]{i, j});
				}
			}
		}

		// '1'이 하나도 없으면 함수를 종료
		if (positions.isEmpty()) {
			return;
		}

		// '1'의 위치 중 무작위로 하나를 선택하여 'V'로 변경
		Collections.shuffle(positions);
		int[] selected = positions.get(0);
		board[selected[0]][selected[1]] = 5;
	}


	public static void replaceOneWithL(int[][] board) {
		// '1' 위치를 저장할 리스트 생성
		List<int[]> positions = new ArrayList<>();

		// 배열을 탐색하여 '1'의 위치를 찾는다
		for (int i = 0; i < board.length; i++) {
			for (int j = 0; j < board[i].length; j++) {
				if (board[i][j] == 1) {
					positions.add(new int[]{i, j});
				}
			}
		}

		// '1'이 하나도 없으면 함수를 종료
		if (positions.isEmpty()) {
			return;
		}

		// '1'의 위치 중 무작위로 하나를 선택하여 'L'로 변경
		Collections.shuffle(positions);
		int[] selected = positions.get(0);
		board[selected[0]][selected[1]] = 4;
	}

	// 현재 블록을 아래로 한 칸 이동시킨다. 만약 블록이 바닥이나 다른 블록에 닿았다면, 그 위치에 블록을 고정하고 새로운 블록 생성
	protected void moveDown() {

		eraseCurr(); // 현재 블록의 위치를 한칸 내리기 위해 게임 보드에서 지웁니다.
		int Linei = 0, Linej = 0;
		if(curr_name.equals("WeightBlock"))
		{
			if(canMoveDown())
			{
				y++;
				for(int i=0;i<4;++i) {
					board[y+1][x+i] = 0;
				}
				placeBlock();
			}
			else {
				placeBlock(); // 현재 위치에 블록을 고정시킵니다.
				checkLines(); // 완성된 라인이 있는지 확인합니다.
				checkLines(); // 완성된 라인이 있는지 확인합니다.
				curr = nextcurr; // 다음블록을 현재 블록으로 설정합니다.
				nextcurr = getRandomBlock(); // 새로운 블록을 무작위로 가져옵니다.
				x = 3; // 새 블록의 x좌표를 시작 x 좌표를 설정합니다.
				y = 0; // 새 블록의 y좌표를 시작 y 좌표를 설정합니다.
				if (!canMoveDown()) { // 새 블록이 움직일 수 없는 경우 (게임 오버)
					GameOver();
				}
			}

		}
		else if (canMoveDown()) { // 아래로 이동할 수 있는 경우
			y++; // 블록을 아래로 이동
			scores += point;
			placeBlock(); // 게임 보드에 현재 블록의 새 위치를 표시합니다.

		} else { // 아래로 이동할 수 없는 경우 (다른 블록에 닿거나 바닥에 닿은 경우)
			if(curr_name.equals("BombBlock"))
			{
				for(int i=-1;i<3;++i)
				{
					for(int j= -1;j<3;++j)
					{
						if(y + j < 0 || y + j > 19 || x+i <0 || x+i > 9)
							continue;
						board[y+j][x+i] = 0;
					}
				}
				eraseCurr();
			}
			else if(curr_name.equals("ItemLBlock"))
			{
				for(int i=0;i<curr.width();++i)
				{
					for(int j=0;j<curr.height();++j)
					{
						System.out.println(String.format("%d %d", x, y));
						if(curr.getShape(i, j) == 4)
						{
							Linei = i;
							Linej = j;
						}
					}
				}
			}
			else if(curr_name.equals("ItemVBlock"))
			{
				for(int i=0;i<curr.width();++i)
				{
					for(int j=0;j<curr.height();++j)
					{
						System.out.println(String.format("%d %d", x, y));
						if(curr.getShape(i, j) == 5)
						{
							Linei = i;
							Linej = j;
						}
					}
				}
			}
			else if(curr_name.equals("TimeBlock"))
			{

				timer.stop();
				timer.setDelay(initInterval); // 기본 속도 1000으로 초기화
				timer.start();
			}
			if(!curr_name.equals("BombBlock")) {
				placeBlock(); // 현재 위치에 블록을 고정시킵니다.

				if(curr_name.equals("ItemLBlock")) {
					for (int a = -9; a < 10; ++a) {
						if (x + Linei + a < 0 || x + Linei + a > 9)
							continue;
						board[y + Linej][x + Linei + a] = 0;

					}
					for (int k = y + Linej; k > 0; k--) {
						board[k] = Arrays.copyOf(board[k - 1], WIDTH);
						color_board[k] = Arrays.copyOf(color_board[k - 1], WIDTH);
					}



				}

				if(curr_name.equals("ItemVBlock")) {
					for (int b = -19; b < 20; ++b) {
						if (y + Linej + b < 0 || y + Linej + b > 19)
							continue;
						board[y + Linej+b][x + Linei] = 0;
					}

				}
			}

			curr = nextcurr; // 다음블록을 현재 블록으로 설정합니다.
			nextcurr = getRandomBlock(); // 새로운 블록을 무작위로 가져옵니다.
			x = 3; // 새 블록의 x좌표를 시작 x 좌표를 설정합니다.
			y = 0; // 새 블록의 y좌표를 시작 y 좌표를 설정합니다.

			checkLines(); // 완성된 라인이 있는지 확인합니다.
			if (!canMoveDown()) { // 새 블록이 움직일 수 없는 경우 (게임 오버)
				GameOver();
			}

			placeBlock();

		}


	}


	protected void moveLeft() {
		// moveLeft 메서드는 현재 블록을 왼쪽으로 한 칸 이동시킵니다.

		eraseCurr(); // 현재 블록의 위치를 게임 보드에서 지웁니다.
		if (canMoveLeft()) {
			x--;
		}
		placeBlock(); // 게임 보드에 현재 블록의 새 위치를 표시합니다.
	}


	protected void moveRight() {
		// moveRight 메서드는 현재 블록을 오른쪽으로 한 칸 이동시킵니다.
		eraseCurr(); // 현재 블록의 위치를 게임 보드에서 지웁니다.
		if (canMoveRight()) {
			x++;
		}
		placeBlock(); // 게임 보드에 현재 블록의 새 위치를 표시합니다.
	}
	private void placeBlock() {

		for (int j = 0; j < curr.height(); j++) {// 현재*/ 블록의 높이만큼 반복합니다.
			for (int i = 0; i < curr.width(); i++) {// 현재 블록의 너비만큼 반복합니다.
				if (curr.getShape(i, j) != 0 && board[y + j][x + i] == 0) {// 보드에 0이아니면 그대로 유지해야만 함. 아니면 내려가면서 다른 블럭 지움
					board[y + j][x + i] = curr.getShape(i, j);// 게임 보드 배열에 블록의 모양을 저장합니다.
					color_board[y+j][x+i] = curr.getColor();
				}
			}
		}
	}



	public void sideBoard() {
		// Next블럭을 그리기 위한 텍스트패널 생성

		nextpane = new JTextPane(); // 텍스트 패널 생성
		nextpane.setEditable(false); // 텍스트 패널 편집 불가하도록 설정
		nextpane.setBackground(Color.GRAY); // 텍스트 패널의 배경색을 검은색으로 설정

		CompoundBorder border = BorderFactory.createCompoundBorder(
				BorderFactory.createLineBorder(Color.GRAY, 10),
				BorderFactory.createLineBorder(Color.DARK_GRAY, 5)); // 복합 테두리 생성
		nextpane.setBorder(border); // 텍스트 패널에 테두리를 설정

		Border innerPadding = new EmptyBorder(0, 0, 0, 0); // 상단, 왼쪽, 하단, 오른쪽 여백 설정
		nextpane.setPreferredSize(new Dimension(Main.SCREEN_WIDTH[3]/2 - 10, Main.SCREEN_HEIGHT[3] -50)); // 가로 300, 세로 200의 크기로 설정
		// 기존 복합 테두리와 내부 여백을 결합한 새로운 복합 테두리 생성
		CompoundBorder newBorder = new CompoundBorder(border, innerPadding);
		// 텍스트 패널에 새로운 테두리 설정
		nextpane.setBorder(newBorder);
		this.add(nextpane, BorderLayout.EAST); // 텍스트 패널을 창의 EAST에 추가.this는 Board클래스의 인스턴스를 지칭
	}

	public void drawBoard() {
		// drawBoard() 메소드는 게임 보드의 현재 상태를 JTextPane에 그리는 역할을 합니다.
		StyledDocument doc = pane.getStyledDocument();
		StyleConstants.setForeground(styleSet, Color.WHITE);
		pane.setText("");
		// 상단 경계선을 그립니다.

		try {
			for (int t = 0; t < WIDTH + 2; t++)
				doc.insertString(doc.getLength(), "X", styleSet);
			doc.insertString(doc.getLength(), "\n", styleSet);
			// 게임 보드의 각 행을 순회합니다.

			for (int i = 0; i < board.length; i++) {
				doc.insertString(doc.getLength(), "X", styleSet);
				for (int j = 0; j < board[i].length; j++) {
					if(board[i][j] == 5)
					{
						StyleConstants.setForeground(styleSet, color_board[i][j]);
						doc.insertString(doc.getLength(), Character.toString(" OBTLVTOXXXXXXX".charAt(board[i][j])), styleSet);
						StyleConstants.setForeground(styleSet, Color.WHITE);
					}
					else if(board[i][j] == 4)
					{
						StyleConstants.setForeground(styleSet, color_board[i][j]);
						doc.insertString(doc.getLength(), Character.toString(" OBTLVTOXXXXXXX".charAt(board[i][j])), styleSet);
						StyleConstants.setForeground(styleSet, Color.WHITE);
					}
					else if(board[i][j] == 2)
					{
						StyleConstants.setForeground(styleSet, color_board[i][j]);
						doc.insertString(doc.getLength(), Character.toString(" OBTLVTOXXXXXXX".charAt(board[i][j])), styleSet);
						StyleConstants.setForeground(styleSet, Color.WHITE);
					}
					else if (board[i][j] == 1) {
						StyleConstants.setForeground(styleSet, color_board[i][j]);
						doc.insertString(doc.getLength(), Character.toString(" OBTLVTOXXXXXXX".charAt(board[i][j])), styleSet);
						StyleConstants.setForeground(styleSet, Color.WHITE);
					} else {
						doc.insertString(doc.getLength(), " ", styleSet);
					}
				}
				doc.insertString(doc.getLength(), BORDER_CHAR + "\n", styleSet);
			}

			// 하단 경계선을 그립니다.
			for (int t = 0; t < WIDTH + 2; t++)
				doc.insertString(doc.getLength(), "X", styleSet);// 보드의 너비만큼 하단에 경계문자(BORDER_CHAR)를 추가합니다.
		} catch	(BadLocationException e){
			System.out.println(e);
		}

		doc.setParagraphAttributes(0, doc.getLength(), styleSet, false); // 가져온 문서에 스타일 속성을 적용합니다.
		pane.setStyledDocument(doc); // 스타일이 적용된 문서를 다시 JTextPane에 설정
		NextBlocknscore();
	}

	public void NextBlocknscore() {

		StyledDocument doc = nextpane.getStyledDocument();
		StyleConstants.setForeground(styleSet, Color.WHITE);
		nextpane.setText("");
		// 상단 경계선을 그립니다.

		if(colorBlindMode) {setColorBlindMode(true);}

		try {
			doc.insertString(doc.getLength(), "NEXT", styleSet);
			doc.insertString(doc.getLength(), "\n", styleSet);
			doc.insertString(doc.getLength(), "\n", styleSet);



			// 다음블럭을 처리하는 로직
			for (int i = 0; i < 2; i++) {
				//NEXT 블럭 표시

				if(nextcurr_name == "WeightBlock")// WeightBlock
				{
					for (int k = 0; k < nextcurr.width(); k++) {
						if (nextcurr.getShape(k, i) == 1 ) {
							doc.insertString(doc.getLength(), "O", styleSet);
						}
						else doc.insertString(doc.getLength(), " ", styleSet);
					}
				}
				else {
					for (int k = 0; k < nextcurr.width(); k++) {
						if (nextcurr.width() == 4 && i == 1) // "OOOO"만 너비가 4이므로 따로 처리
							break;
						if (nextcurr.getShape(k, i) == 1) {
							StyleConstants.setForeground(styleSet, nextcurr.getColor());
							doc.insertString(doc.getLength(), "O", styleSet);
							StyleConstants.setForeground(styleSet, Color.WHITE);

						} else if (nextcurr.getShape(k, i) == 2) {//BombBlock
							StyleConstants.setForeground(styleSet, nextcurr.getColor());
							doc.insertString(doc.getLength(), "B", styleSet);
							StyleConstants.setForeground(styleSet, Color.WHITE);
						}
						else if (nextcurr.getShape(k, i) == 3) {//BombBlock
							StyleConstants.setForeground(styleSet, nextcurr.getColor());
							doc.insertString(doc.getLength(), "T", styleSet);
							StyleConstants.setForeground(styleSet, Color.WHITE);
						}
						else if (nextcurr.getShape(k, i) == 4){
							StyleConstants.setForeground(styleSet, nextcurr.getColor());
							doc.insertString(doc.getLength(), "L", styleSet);
							StyleConstants.setForeground(styleSet, Color.WHITE);
						}
						else if (nextcurr.getShape(k, i) == 5){
							StyleConstants.setForeground(styleSet, nextcurr.getColor());
							doc.insertString(doc.getLength(), "V", styleSet);
							StyleConstants.setForeground(styleSet, Color.WHITE);
						}
						else doc.insertString(doc.getLength(), " ", styleSet);
					}
				}
				doc.insertString(doc.getLength(), "\n", styleSet);
			}




			//공백추가
			for (int i = 0; i < 7; i++) {
				doc.insertString(doc.getLength(), "\n", styleSet);
			}

			String blockFormatted = String.format("%3d", bricks);
			String linesFormatted = String.format("%3d", lines);
			String scoresFormatted = String.format("%3d", scores);
			String levelFormatted = String.format("%3d", level);

			doc.insertString(doc.getLength(), "BLOCK : ", styleSet);
			if (colorBlindMode) {
				StyleConstants.setForeground(styleSet, Color.PINK);
			} else {
				StyleConstants.setForeground(styleSet, Color.GREEN);}
			doc.insertString(doc.getLength(), blockFormatted + "\n\n", styleSet);
			StyleConstants.setForeground(styleSet, Color.WHITE);

			doc.insertString(doc.getLength(), "LINES : " , styleSet);
			if (colorBlindMode) {
				StyleConstants.setForeground(styleSet, Color.PINK);
			} else {
				StyleConstants.setForeground(styleSet, Color.GREEN);}
			doc.insertString(doc.getLength(), linesFormatted + "\n\n", styleSet);
			StyleConstants.setForeground(styleSet, Color.WHITE);

			doc.insertString(doc.getLength(), "SCORE : ", styleSet);
			if (colorBlindMode) {
				StyleConstants.setForeground(styleSet, Color.PINK);
			} else {
				StyleConstants.setForeground(styleSet, Color.GREEN);}
			doc.insertString(doc.getLength(), scoresFormatted + "\n\n", styleSet);
			StyleConstants.setForeground(styleSet, Color.WHITE);

			doc.insertString(doc.getLength(), "LEVEL : ", styleSet);
			if (colorBlindMode) {
				StyleConstants.setForeground(styleSet, Color.PINK);
			} else {
				StyleConstants.setForeground(styleSet, Color.GREEN);}
			doc.insertString(doc.getLength(), levelFormatted + "\n\n", styleSet);
			StyleConstants.setForeground(styleSet, Color.WHITE);

		} catch(BadLocationException e)
		{
			System.out.println(e);
		}

		doc.setParagraphAttributes(0, doc.getLength(), styleSet, false); // 가져온 문서에 스타일 속성을 적용합니다.
		nextpane.setStyledDocument(doc); // 스타일이 적용된 문서를 다시 JTextPane에 설정
	}


	//일정 점수 도달하면 레벨+, 속도+, 얻는 점수+ 조정하는 함수, moveDown(), TimerAction에 호출됨
	public void setLevel() {
		double decreaseTime = 200; // 일정 블럭 수 도달 시 감소할 값(속도 증가)

		switch (mode) {
			case 0:  //easy
				if (bricks == 20 || bricks == 40 || bricks == 60 || bricks == 80) {
					level++;
					point++;
					timer.stop();
					initInterval = (int) (initInterval - decreaseTime);
					timer.setDelay(initInterval);
					timer.start();
				}
				break;
			case 1:
				if (bricks == 20 || bricks == 40 || bricks == 60 || bricks == 80) {
					level++;
					point++;
					timer.stop();
					initInterval = (int) (initInterval - (decreaseTime * 0.8));
					timer.setDelay(initInterval);
					timer.start();
				}
				break;
			case 2:
				if (bricks == 20 || bricks == 40 || bricks == 60 || bricks == 80) {
					level++;
					point++;
					timer.stop();
					initInterval = (int) (initInterval - (decreaseTime * 1.2));
					timer.setDelay(initInterval);
					timer.start();
				}
				break;
		}
	}

	// 색맹 모드 설정
	public static void setColorBlindMode(boolean A) {
		colorBlindMode = A;
	}

	public void GameInit(){
		initInterval = 1000; //블록이 자동으로 아래로 떨어지는 속도 제어 시간, 현재 1초
		timer.setDelay(initInterval);

		if (colorBlindMode) {
			StyleConstants.setForeground(styleSet, Color.PINK);
		} else {
			StyleConstants.setForeground(styleSet, Color.GREEN);}

		x = 3; //Default Position. 현재 블록 위치
		y = 0; // 현재 블록 위치
		scores = 0; // 현재 스코어
		point = 1; // 한칸 떨어질때 얻는 점수
		level = 0; // 현재 레벨
		lines = 0; // 현재 지워진 라인 수
		bricks = 0; // 생성된 벽돌의 개수
		isPaused = false; // 게임이 일시 중지되었는지 나타내는 변수
		curr =  getRandomBlock();// 현재 움직이고 있는 블록
		bricks--;
		nextcurr = getRandomBlock(); // 다음 블럭
		gameOver = false; // 게임오버를 알려주는변수 true == 게임오버

		for(int i = 0; i<HEIGHT; i++) {
			for (int u = 0; u < WIDTH; u++)
				board[i][u] = 0;
		}

		//timer.start();
		placeBlock(); //  선택된 블록을 배치합니다.
		drawBoard(); // 보드를 그린다.
	}

	public void switchToScreen(JPanel newScreen) {
		Main.cardLayout.show(Main.mainPanel, newScreen.getName()); // 화면 전환
		newScreen.setFocusable(true); // 새 화면이 포커스를 받을 수 있도록 설정
		newScreen.requestFocusInWindow(); // 새 화면에게 포커스 요청
	}


	// 게임 종료 이벤트
	public void GameOver() {
		timer.stop(); // 타이머를 멈춥니다.
		gameOver = true;
		if(item == 0) {
			Main.classicScoreBoard1.update();
			Main.frame.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
			switchToScreen(Main.classicScoreBoard1);
			int response = JOptionPane.showConfirmDialog(this, "점수를 저장하시겠습니까?", "Game Over", JOptionPane.YES_NO_OPTION);

			if (response == JOptionPane.YES_OPTION) {
				//점수 저장 구현 순위, 이름, 점수, 모드
				name = JOptionPane.showInputDialog(this, "이름을 입력하세요:"); // 이름입력하는 대화상자
				//정상적으로 이름을 입력했다면
				if (name != null && !name.isEmpty()) {


					JSONArray scoreList = new JSONArray();
					JSONParser parser = new JSONParser();

					try {
						FileReader reader = new FileReader(String.format(Main.path) + "/Tetris_game/src/ClassicScoreData.json");
						Object obj = parser.parse(reader);
						scoreList = (JSONArray) obj;
						reader.close();
					} catch (Exception e) {
						// 파일이 없거나 읽을 수 없을 때 예외 처리
					}

					for (Object item : scoreList) // 최신 상태임을 나타내는 키값 recent에 대항하는 값을 기존의 모든 object들에 대하여 0으로 바꿔줌.
					{
						JSONObject gameData = (JSONObject) item;
						gameData.put("recent", 0);
					}

					// 새 데이터 추가
					JSONObject scoreData = new JSONObject();
					scoreData.put("mode", mode);
					scoreData.put("scores", scores); // 'scores' 변수의 실제 타입에 따라 적절히 처리해야 함
					scoreData.put("name", name);
					scoreData.put("item", item);
					scoreData.put("recent", 1); // 가장 최근에 끝난 게임임을 알려주는 심볼
					scoreList.add(scoreData);

					// 파일에 새 데이터 쓰기
					try (FileWriter file = new FileWriter(String.format(Main.path) + "/Tetris_game/src/ClassicScoreData.json")) {
						file.write(scoreList.toJSONString());
						file.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}

					Main.classicScoreBoard1.update();
					switchToScreen(Main.classicScoreBoard1);

					System.out.println(name);
					System.out.println(scores);
					System.out.println(mode);

				} else // 빈칸을 입력했거나, 이름입력대화상자에서 취소 눌렀을 때
				{
					Main.frame.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
					switchToScreen(Main.mainMenu1);
				}

			} else if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) { //점수 저장하시겠습니까? -> No일 때
				Main.frame.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
				switchToScreen(Main.mainMenu1);
			}
		}
		else if(item == 1)
		{
			Main.itemScoreBoard1.update();
			Main.frame.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
			switchToScreen(Main.itemScoreBoard1);
			int response = JOptionPane.showConfirmDialog(this, "점수를 저장하시겠습니까?", "Game Over", JOptionPane.YES_NO_OPTION);

			if (response == JOptionPane.YES_OPTION) {
				//점수 저장 구현 순위, 이름, 점수, 모드
				name = JOptionPane.showInputDialog(this, "이름을 입력하세요:"); // 이름입력하는 대화상자
				//정상적으로 이름을 입력했다면
				if (name != null && !name.isEmpty()) {


					JSONArray scoreList = new JSONArray();
					JSONParser parser = new JSONParser();

					try {
						FileReader reader = new FileReader(String.format(Main.path) + "/Tetris_game/src/ItemScoreData.json");
						Object obj = parser.parse(reader);
						scoreList = (JSONArray) obj;
						reader.close();
					} catch (Exception e) {
						// 파일이 없거나 읽을 수 없을 때 예외 처리
					}

					for (Object item : scoreList) // 최신 상태임을 나타내는 키값 recent에 대항하는 값을 기존의 모든 object들에 대하여 0으로 바꿔줌.
					{
						JSONObject gameData = (JSONObject) item;
						gameData.put("recent", 0);
					}

					// 새 데이터 추가
					JSONObject scoreData = new JSONObject();
					scoreData.put("mode", mode);
					scoreData.put("scores", scores); // 'scores' 변수의 실제 타입에 따라 적절히 처리해야 함
					scoreData.put("name", name);
					scoreData.put("item", item);
					scoreData.put("recent", 1); // 가장 최근에 끝난 게임임을 알려주는 심볼
					scoreList.add(scoreData);

					// 파일에 새 데이터 쓰기
					try (FileWriter file = new FileWriter(String.format(Main.path) + "/Tetris_game/src/ItemScoreData.json")) {
						file.write(scoreList.toJSONString());
						file.flush();
					} catch (Exception e) {
						e.printStackTrace();
					}

					Main.itemScoreBoard1.update();

					switchToScreen(Main.itemScoreBoard1);

					System.out.println(name);
					System.out.println(scores);
					System.out.println(mode);


				} else { // 빈칸을 입력했거나, 이름입력대화상자에서 취소 눌렀을 때
					Main.frame.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
					switchToScreen(Main.mainMenu1);
				}

			} else if (response == JOptionPane.NO_OPTION || response == JOptionPane.CLOSED_OPTION) { //점수 저장하시겠습니까? -> No일 때
				Main.frame.setSize(Main.SCREEN_WIDTH[0], Main.SCREEN_HEIGHT[0]);
				switchToScreen(Main.mainMenu1);

			}
		}
		GameInit();
	}






	public class PlayerKeyListener implements KeyListener {
		@Override
		public void keyTyped(KeyEvent e) {
			// 키가 타이핑됐을 때의 동작을 정의할 수 있으나, 여기서는 사용하지 않습니다.
		}

		@Override
		public void keyPressed(KeyEvent e) {
			int Linei = 0, Linej = 0;
			// 키가 눌렸을 때의 동작을 정의합니다.
			switch (e.getKeyCode()) { // 눌린 키에 따라 적절한 동작을 수행합니다.
				case KeyEvent.VK_DOWN:
					moveDown(); // 아래 방향키가 눌렸을 때, 현재 블록을 아래로 이동시킵니다.
					drawBoard(); // 게임 보드를 다시 그립니다.
					break;
				case KeyEvent.VK_RIGHT:
					moveRight(); // 오른쪽 방향키가 눌렸을 때, 현재 블록을 오른쪽으로 이동시킵니다.
					drawBoard(); // 게임 보드를 다시 그립니다.
					break;
				case KeyEvent.VK_LEFT:
					moveLeft(); // 왼쪽 방향키가 눌렸을 때, 현재 블록을 왼쪽으로 이동시킵니다.
					drawBoard(); // 게임 보드를 다시 그립니다.
					break;
				case KeyEvent.VK_UP:
					eraseCurr(); // 현재 블록을 지웁니다.
					if (canRotate()) { // 블록이 회전 가능한 경우에만 회전을 수행합니다.
						curr.rotate(); // 현재 블록을 회전시킵니다.
						placeBlock();
					}

					drawBoard(); // 게임 보드를 다시 그립니다.
					break;
				case KeyEvent.VK_SPACE:
					isPaused = !isPaused; // 게임의 상태를 전환합니다.
					if (isPaused) {
						timer.stop(); // 게임이 일시 중지된 경우, 타이머를 중지합니다.
						pane.setText("Game Paused\nPress SPACE to continue"); // 게임이 일시 중지된 상태를 표시합니다.
					} else {
						timer.start(); // 게임이 재개된 경우, 타이머를 시작합니다.
					}
					break;
				case KeyEvent.VK_ENTER:
					eraseCurr();
					if(curr_name.equals("WeightBlock"))
					{
						while (canMoveDown()) {
							y++;
							for(int i=0;i<4;++i) {
								board[y+1][x+i] = 0;
							}
						}
					}
					else if(curr_name.equals("BombBlock")) {
						while (canMoveDown()) {
							y++;
						}
					}
					else if(curr_name.equals("ItemLBlock"))
					{
						while(canMoveDown())
							y++;
					}
					else if(curr_name.equals("ItemVBlock"))
					{
						while(canMoveDown())
							y++;
					}
					else if(curr_name.equals("TimeBlock")) {
						while (canMoveDown()) {
							y++;
						}
					}
					else
					{
						while (canMoveDown()) {
							y++;
							scores += point*2;
						}
					}
					placeBlock();
					if(curr_name.equals("BombBlock"))
					{
						for(int i=-1;i<3;++i)
						{
							for(int j= -1;j<3;++j)
							{
								if(y+j < 0 || y + j > 19 || x+i <0 || x+i > 9)
									continue;
								board[y+j][x+i] = 0;
							}
						}
						eraseCurr();
					}
					else if(curr_name.equals("ItemLBlock"))
					{
						System.out.println("당첨4");
						for(int i=0;i<curr.width();++i)
						{System.out.println("당첨5");
							for(int j=0;j<curr.height();++j)
							{
								System.out.println(String.format("%d %d", x, y));
								if(curr.getShape(i, j) == 4)
								{System.out.println("당첨6");
									Linei = i;
									Linej = j;
								}
							}
						}
						for (int a = -9; a < 10; ++a) {
							if (x + Linei + a < 0 || x + Linei + a > 9)
								continue;
							board[y + Linej][x + Linei + a] = 0;
						}
					}
					else if(curr_name.equals("ItemVBlock"))
					{
						for(int i=0;i<curr.width();++i)
						{
							for(int j=0;j<curr.height();++j)
							{
								if(curr.getShape(i, j) == 5)
								{
									Linei = i;
									Linej = j;
								}
							}
						}
						for (int b = -19; b < 20; ++b) {
							if (y + Linej + b < 0 || y + Linej + b > 19)
								continue;
							board[y + Linej + b][x + Linei] = 0;
						}
					}
					else if(curr_name.equals("TimeBlock"))
					{
						timer.stop();
						timer.setDelay(initInterval); // 기본 속도 1000으로 초기화
						timer.start();
					}
					checkLines();
					curr = nextcurr;
					nextcurr = getRandomBlock();
					x = 3; // 새 블록의 x좌표를 시작 x 좌표를 설정합니다.
					y = 0; // 새 블록의 y좌표를 시작 y 좌표를 설정합니다.
					placeBlock();
					drawBoard();
					break;

				case KeyEvent.VK_Q:
					System.exit(0); // 'q' 키가 눌렸을 때, 프로그램을 종료합니다.

					break;
			}
		}

		@Override
		public void keyReleased(KeyEvent e) {
			// 키가 떼어졌을 때의 동작을 정의할 수 있으나, 여기서는 사용하지 않습니다.
		}
	}
}