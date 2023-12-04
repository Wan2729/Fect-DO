package com.example.fectdo.Soalan;

public class Soalan {
    private String soalan, jawapan;
    private String[] pilihanJawapan;

    public Soalan(String soalan, String jawapan, String[] pilihanJawapan) {
        this.soalan = soalan;
        this.jawapan = jawapan;
        this.pilihanJawapan = pilihanJawapan;
    }

    public String getSoalan() {
        return soalan;
    }

    public String getJawapan() {
        return jawapan;
    }

    public String[] getPilihanJawapan() {
        return pilihanJawapan;
    }
}
