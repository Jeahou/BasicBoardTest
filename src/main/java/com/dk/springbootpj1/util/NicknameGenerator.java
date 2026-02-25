package com.dk.springbootpj1.util;

public class NicknameGenerator {
    private static final String[] adjectives = {"행복한", "즐거운", "용감한", "귀여운", "배고픈", "빛나는", "신난"};
    private static final String[] animals = {"사자", "호랑이", "다람쥐", "토끼", "판다", "펭귄", "여우"};

    public static String generate() {
        String adj = adjectives[(int) (Math.random() * adjectives.length)];
        String ani = animals[(int) (Math.random() * animals.length)];
        int num = (int) (Math.random() * 999); // 중복 방지를 위한 숫자 추가

        return adj + "_" + ani + num;
    }
}
