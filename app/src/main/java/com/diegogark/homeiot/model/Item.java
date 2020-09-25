package com.diegogark.homeiot.model;

public class Item {

    private String item;
    private boolean validade;
    private int imagem;
    private String caminho;
    private String lista;
    private boolean titulo;

    public Item() {
    }

    public Item(String item) {
        this.item = item;
    }

    public Item(String item, int imagem) {
        this.item = item;
        this.imagem = imagem;
    }

    public Item(String item, int imagem, String caminho) {
        this.item = item;
        this.imagem = imagem;
        this.caminho = caminho;
    }

    public String getLista() {
        return lista;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }

    public boolean isTitulo() {
        return titulo;
    }

    public void setTitulo(boolean titulo) {
        this.titulo = titulo;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public int getImagem() {
        return imagem;
    }

    public void setImagem(int imagem) {
        this.imagem = imagem;
    }

    public String getCaminho() {
        return caminho;
    }

    public void setCaminho(String caminho) {
        this.caminho = caminho;
    }

    public boolean isValidade() {
        return validade;
    }

    public void setValidade(boolean validade) {
        this.validade = validade;
    }
}
