package entity;

public enum TipoEvento {
    FESTA("Festa"),
    ESPORTIVO("Evento Esportivo"),
    SHOW("Show"),
    TEATRO("Peça de Teatro"),
    CONFERENCIA("Conferência"),
    FEIRA("Feira"),
    EXPOSICAO("Exposição"),
    ENCONTRO("Encontro Social");
    
    private final String descricao;
    
    TipoEvento(String descricao) {
        this.descricao = descricao;
    }
    
    public String getDescricao() {
        return descricao;
    }
    
    public static TipoEvento fromInt(int value) {
        if (value >= 0 && value < values().length) {
            return values()[value];
        }
        return FESTA;
    }
    
    public static String[] getDescricoes() {
        TipoEvento[] tipos = values();
        String[] descricoes = new String[tipos.length];
        for (int i = 0; i < tipos.length; i++) {
            descricoes[i] = tipos[i].getDescricao();
        }
        return descricoes;
    }
}