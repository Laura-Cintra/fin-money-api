package br.com.fiap.fin_money_api.model;

import java.util.Random;

public class Category {
    private Long id;
    private String name;
    private String icon;

    public Category(Long id, String name, String icon) {
        this.id = Math.abs(new Random().nextLong()); // abs - pega o valor absoluto, tirando negativos
        this.name = name;
        this.icon = icon;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getIcon() {
        return icon;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "Category [id=" + id + ", name=" + name + ", icon=" + icon + "]";
    }
}
