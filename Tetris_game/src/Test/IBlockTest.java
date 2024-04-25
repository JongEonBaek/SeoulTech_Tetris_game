package Test;

import java.util.Random;



public class IBlockTest

{

    private void Test(int mode){
        Random rnd = new Random(); // 반복문 밖에서 Random 객체 생성
        for (int i = 0; i < 50000; i++) { // 1000번의 테스트 케이스 실행
            int slot = 0;
            switch (mode) {
                case 0: //easy
                    slot = rnd.nextInt(36); // 0부터 35 사이의 난수 생성
                    if (slot < 6) {
                        count[0]++;
                    } else if (slot < 11) {
                        count[1]++;
                    } else if (slot < 16) {
                        count[2]++;
                    } else if (slot < 21) {
                        count[3]++;
                    } else if (slot < 26) {
                        count[4]++;
                    } else if (slot < 31) {
                        count[5]++;
                    } else {
                        count[6]++;
                    }
                    break;
                case 1: //normal
                    slot = rnd.nextInt(7); // 0부터 6 사이의 난수 생성
                    if (slot == 0) {
                        count[0]++;
                    } else if (slot == 1) {
                        count[1]++;
                    } else if (slot == 2) {
                        count[2]++;
                    } else if (slot == 3) {
                        count[3]++;
                    } else if (slot == 4) {
                        count[4]++;
                    } else if (slot == 5) {
                        count[5]++;
                    } else {
                        count[6]++;
                    }
                    break;
                case 2: //hard
                    slot = rnd.nextInt(34); // 0부터 33 사이의 난수 생성
                    if (slot < 4) {
                        count[0]++;
                    } else if (slot < 9) {
                        count[1]++;
                    } else if (slot < 14) {
                        count[2]++;
                    } else if (slot < 19) {
                        count[3]++;
                    } else if (slot < 24) {
                        count[4]++;
                    } else if (slot < 29) {
                        count[5]++;
                    } else {
                        count[6]++;
                    }
                    break;
            }
        }

        System.out.print("I : " + count[0]); // I형 블록의 개수 출력
        System.out.print(", J : " + count[1]); // I형 블록의 개수 출력
        System.out.print(", L : " + count[2]); // I형 블록의 개수 출력
        System.out.print(", O : " + count[3]); // I형 블록의 개수 출력
        System.out.print(", S : " + count[4]); // I형 블록의 개수 출력
        System.out.print(", T : " + count[5]); // I형 블록의 개수 출력
        System.out.println(", Z : " + count[6]); // I형 블록의 개수 출력

        System.out.print("I : 1"); // I형 블록의 개수는 항상 1로 고정
        System.out.print(", J : " + String.format("%.1f", (float)count[1]/count[0])); // I형 블록 대비 J형 블록의 비율 출력
        System.out.print(", L : " + String.format("%.1f", (float)count[2]/count[0])); // I형 블록 대비 L형 블록의 비율 출력
        System.out.print(", O : " + String.format("%.1f", (float)count[3]/count[0])); // I형 블록 대비 O형 블록의 비율 출력
        System.out.print(", S : " + String.format("%.1f", (float)count[4]/count[0])); // I형 블록 대비 S형 블록의 비율 출력
        System.out.print(", T : " + String.format("%.1f", (float)count[5]/count[0])); // I형 블록 대비 T형 블록의 비율 출력
        System.out.print(", Z : " + String.format("%.1f", (float)count[6]/count[0])); // I형 블록 대비 Z형 블록의 비율 출력



    }


    private  int[] count = new int[7];
    public static void main(String[] args)
    {

        IBlockTest test = new IBlockTest();

        test.Test(2); // easy : 0, normal : 1, hard : 2

    }


}












