package com.example.plogginglovers.Model;

import java.util.ArrayList;
import java.util.List;

public enum RecyclingManager {
    INSTANCE;

    private List<Garbage> garbages;

    RecyclingManager() {
        this.garbages = new ArrayList<>();
        //Amarelo
        this.garbages.add(new Garbage("Latas", "Amarelo"));
        this.garbages.add(new Garbage("Pacotes", "Amarelo"));
        this.garbages.add(new Garbage("Aerossóis", "Amarelo"));
        this.garbages.add(new Garbage("Tabuleiros", "Amarelo"));
        this.garbages.add(new Garbage("Garrafas de Plástico", "Amarelo"));
        this.garbages.add(new Garbage("Garrafões de Plástico", "Amarelo"));
        this.garbages.add(new Garbage("Frascos de Plástico", "Amarelo"));
        this.garbages.add(new Garbage("Sacos", "Amarelo"));
        this.garbages.add(new Garbage("Esferovite", "Amarelo"));
        //Azul
        this.garbages.add(new Garbage("Cartão", "Azul"));
        this.garbages.add(new Garbage("Jornais", "Azul"));
        this.garbages.add(new Garbage("Revistas", "Azul"));
        this.garbages.add(new Garbage("Papel", "Azul"));
        //Verde
        this.garbages.add(new Garbage("Garrafas de vidro", "Verde"));
        this.garbages.add(new Garbage("Frascos de vidro", "Verde"));
        this.garbages.add(new Garbage("Boiões de vidro", "Verde"));
        //Comum
        this.garbages.add(new Garbage("Pilhas", "Pilhão"));
        this.garbages.add(new Garbage("Loiça", "Comum"));
        this.garbages.add(new Garbage("Comida", "Comum"));
        this.garbages.add(new Garbage("Janelas", "Comum"));
        this.garbages.add(new Garbage("Espelhos", "Comum"));
        this.garbages.add(new Garbage("Lâmpadas", "Comum"));
    }

    public List<Garbage> getGarbages() {
        return garbages;
    }

    public String getEcoponto(String garbage) {
        for (Garbage garbageFromList: getGarbages()) {
            if (garbageFromList.getGarbage().equals(garbage)){
                return garbageFromList.getEcoponto();
            }
        }

        return null;
    }

    public ArrayList<String> getGarbagesWithoutEcopontos(){
        ArrayList<String> garbages = new ArrayList<>();

        for (Garbage garbage: getGarbages()) {
            garbages.add(garbage.getGarbage());
        }

        return garbages;
    }
}
