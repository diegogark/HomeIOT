package com.diegogark.homeiot.model;

public class Comandos {
    String  sTime, sNome, sID;
    int iComando;
    boolean comRetorno, resposta;

    public Comandos() {
    }

    public String getsTime() {
        return sTime;
    }

    public void setsTime(String sTime) {
        this.sTime = sTime;
    }

    public String getsNome() {
        return sNome;
    }

    public void setsNome(String sNome) {
        this.sNome = sNome;
    }

    public String getsID() {
        return sID;
    }

    public void setsID(String sID) {
        this.sID = sID;
    }

    public int getiComando() {
        return iComando;
    }

    public void setiComando(int iComando) {
        this.iComando = iComando;
    }

    public boolean isComRetorno() {
        return comRetorno;
    }

    public void setComRetorno(boolean comRetorno) {
        this.comRetorno = comRetorno;
    }

    public boolean isResposta() {
        return resposta;
    }

    public void setResposta(boolean resposta) {
        this.resposta = resposta;
    }
}
