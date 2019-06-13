package com.example.plogginglovers.Model;

import java.util.ArrayList;
import java.util.List;

public enum RecyclingManager {
    INSTANCE;

    private List<Garbage> garbages;

    RecyclingManager() {
        this.garbages = new ArrayList<>();
        //Amarelo
        this.garbages.add(new Garbage("Latas de metal", "Amarelo"));
        this.garbages.add(new Garbage("Pacotes de plástico", "Amarelo"));
        this.garbages.add(new Garbage("Aerossóis", "Amarelo"));
        this.garbages.add(new Garbage("Tabuleiros", "Amarelo"));
        this.garbages.add(new Garbage("Garrafas de Plástico", "Amarelo"));
        this.garbages.add(new Garbage("Garrafões de Plástico", "Amarelo"));
        this.garbages.add(new Garbage("Frascos de Plástico", "Amarelo"));
        this.garbages.add(new Garbage("Sacos", "Amarelo"));
        this.garbages.add(new Garbage("Esferovite", "Amarelo"));
        this.garbages.add(new Garbage("Caixas de CD", "Amarelo"));
        this.garbages.add(new Garbage("Caricas", "Amarelo"));
        this.garbages.add(new Garbage("Copos de plástico", "Amarelo"));
        this.garbages.add(new Garbage("Embalagens de iogurtes", "Amarelo"));
        this.garbages.add(new Garbage("Embalagens detergente", "Amarelo"));
        this.garbages.add(new Garbage("Embalagens produtos higiene", "Amarelo"));
        this.garbages.add(new Garbage("Folha de alumínio", "Amarelo"));
        this.garbages.add(new Garbage("Loiça descartável", "Amarelo"));
        this.garbages.add(new Garbage("Pacote de arroz", "Amarelo"));
        this.garbages.add(new Garbage("Pacote de massa", "Amarelo"));
        this.garbages.add(new Garbage("Pacote de natas", "Amarelo"));
        this.garbages.add(new Garbage("Pacote de sumo", "Amarelo"));
        this.garbages.add(new Garbage("Metal", "Amarelo"));
        this.garbages.add(new Garbage("Plástico", "Amarelo"));
        this.garbages.add(new Garbage("Palhinhas", "Amarelo"));
        this.garbages.add(new Garbage("Película aderente", "Amarelo"));
        this.garbages.add(new Garbage("Saco de congelados", "Amarelo"));
        this.garbages.add(new Garbage("Sacos de plástico", "Amarelo"));
        this.garbages.add(new Garbage("Sacos reutilizáveis", "Amarelo"));
        this.garbages.add(new Garbage("Talheres de plástico", "Amarelo"));
        this.garbages.add(new Garbage("Tubo de cola", "Amarelo"));
        this.garbages.add(new Garbage("Tubo de pasta de dentes", "Amarelo"));
        //Azul
        this.garbages.add(new Garbage("Cartão", "Azul"));
        this.garbages.add(new Garbage("Jornais", "Azul"));
        this.garbages.add(new Garbage("Revistas", "Azul"));
        this.garbages.add(new Garbage("Papel", "Azul"));
        this.garbages.add(new Garbage("Caixas de cereais", "Azul"));
        this.garbages.add(new Garbage("Caixas de fósforos", "Azul"));
        this.garbages.add(new Garbage("Caixas de ovos", "Azul"));
        this.garbages.add(new Garbage("Caixas de pizza s/ gordura", "Azul"));
        this.garbages.add(new Garbage("Cartas", "Azul"));
        this.garbages.add(new Garbage("Envelopes", "Azul"));
        this.garbages.add(new Garbage("Guardanapos limpos", "Azul"));
        this.garbages.add(new Garbage("Maços de tabaco", "Azul"));
        this.garbages.add(new Garbage("Papel vegetal s/ gordura", "Azul"));
        this.garbages.add(new Garbage("Rolo de papel", "Azul"));
        this.garbages.add(new Garbage("Sacos de papel limpos", "Azul"));
        //Verde
        this.garbages.add(new Garbage("Garrafas de vidro", "Verde"));
        this.garbages.add(new Garbage("Frascos de vidro", "Verde"));
        this.garbages.add(new Garbage("Boiões de vidro", "Verde"));
        this.garbages.add(new Garbage("Frasco de perfume", "Verde"));
        this.garbages.add(new Garbage("Vidro", "Verde"));
        //Pilhão
        this.garbages.add(new Garbage("Pilhas", "Pilhão"));
        this.garbages.add(new Garbage("Baterias", "Pilhão"));
        //Ecocentro
        this.garbages.add(new Garbage("Alguidar", "Ecocentro"));
        this.garbages.add(new Garbage("Baldes", "Ecocentro"));
        this.garbages.add(new Garbage("Cabides metálicos", "Ecocentro"));
        this.garbages.add(new Garbage("Cabides plásticos", "Ecocentro"));
        this.garbages.add(new Garbage("Caixas de plásticos", "Ecocentro"));
        this.garbages.add(new Garbage("Colchão", "Ecocentro"));
        this.garbages.add(new Garbage("Eletrodomésticos", "Ecocentro"));
        this.garbages.add(new Garbage("Equipamentos Elétricos", "Ecocentro"));
        this.garbages.add(new Garbage("Ferramentas", "Ecocentro"));
        this.garbages.add(new Garbage("Lâmpadas", "Ecocentro"));
        this.garbages.add(new Garbage("Óleos", "Ecocentro"));
        this.garbages.add(new Garbage("Madeira", "Ecocentro"));
        this.garbages.add(new Garbage("Pneus", "Ecocentro"));
        this.garbages.add(new Garbage("Roupa", "Ecocentro"));
        this.garbages.add(new Garbage("Saco de pano", "Ecocentro"));
        this.garbages.add(new Garbage("Talheres de metal", "Ecocentro"));
        this.garbages.add(new Garbage("Tinteiros", "Ecocentro"));
        this.garbages.add(new Garbage("Toners", "Ecocentro"));
        this.garbages.add(new Garbage("Vernizes", "Ecocentro"));
        //Farmácia
        this.garbages.add(new Garbage("Medicamentos", "Farmácia"));
        //Comum
        this.garbages.add(new Garbage("Loiça", "Comum"));
        this.garbages.add(new Garbage("Comida", "Comum"));
        this.garbages.add(new Garbage("Vidro das janelas", "Comum"));
        this.garbages.add(new Garbage("Espelhos", "Comum"));
        this.garbages.add(new Garbage("Agrafos", "Comum"));
        this.garbages.add(new Garbage("Alfinetes", "Comum"));
        this.garbages.add(new Garbage("Caixas de pizza c/ gordura", "Comum"));
        this.garbages.add(new Garbage("Calçado", "Comum"));
        this.garbages.add(new Garbage("Canetas", "Comum"));
        this.garbages.add(new Garbage("Canecas", "Comum"));
        this.garbages.add(new Garbage("Cápsulas de café", "Comum"));
        this.garbages.add(new Garbage("Cassete de áudio", "Comum"));
        this.garbages.add(new Garbage("CD", "Comum"));
        this.garbages.add(new Garbage("Copos de cristal", "Comum"));
        this.garbages.add(new Garbage("Copos de vidro", "Comum"));
        this.garbages.add(new Garbage("Esfregão de aço", "Comum"));
        this.garbages.add(new Garbage("Esfregão verde", "Comum"));
        this.garbages.add(new Garbage("Esponja", "Comum"));
        this.garbages.add(new Garbage("Etiquetas autocolantes", "Comum"));
        this.garbages.add(new Garbage("Fita adesiva", "Comum"));
        this.garbages.add(new Garbage("Fraldas", "Comum"));
        this.garbages.add(new Garbage("Garrafão de combustível", "Comum"));
        this.garbages.add(new Garbage("Guardanapos sujos", "Comum"));
        this.garbages.add(new Garbage("Isqueiro", "Comum"));
        this.garbages.add(new Garbage("Jarros", "Comum"));
        this.garbages.add(new Garbage("Papel vegetal c/ gordura", "Comum"));
        this.garbages.add(new Garbage("Pincel/Esponja de cosmética", "Comum"));
        this.garbages.add(new Garbage("Cortiça", "Comum"));
        this.garbages.add(new Garbage("Porcelana", "Comum"));
        this.garbages.add(new Garbage("Pirex", "Comum"));
        this.garbages.add(new Garbage("Sacos de chá", "Comum"));
        this.garbages.add(new Garbage("Sacos de papel sujos", "Comum"));
        this.garbages.add(new Garbage("Toalhetes", "Comum"));

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
