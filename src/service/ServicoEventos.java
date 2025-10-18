package service;

import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import entity.Evento;

public class ServicoEventos {
    
    public static List<Evento> filtrarEventosPorStatus(List<Evento> eventos, String status) {
        return eventos.stream()
            .filter(e -> e.getStatus().equals(status))
            .sorted(Comparator.comparing(Evento::getHorario))
            .collect(Collectors.toList());
    }
    
    public static List<Evento> ordenarEventosPorProximidade(List<Evento> eventos) {
        return eventos.stream()
            .sorted(Comparator.comparing(Evento::getHorario))
            .collect(Collectors.toList());
    }
    
    public static List<Evento> getEventosOcorrendoAgora(List<Evento> eventos) {
        return eventos.stream()
            .filter(Evento::isOcorrendoAgora)
            .collect(Collectors.toList());
    }
    
    public static List<Evento> getEventosFuturos(List<Evento> eventos) {
        return eventos.stream()
            .filter(Evento::isFuturo)
            .collect(Collectors.toList());
    }
    
    public static List<Evento> getEventosPassados(List<Evento> eventos) {
        return eventos.stream()
            .filter(Evento::isPassado)
            .collect(Collectors.toList());
    }
    
    public static List<Evento> buscarEventosPorNome(List<Evento> eventos, String nome) {
        return eventos.stream()
            .filter(e -> e.getNome().toLowerCase().contains(nome.toLowerCase()))
            .sorted(Comparator.comparing(Evento::getHorario))
            .collect(Collectors.toList());
    }
    
    public static Evento buscarEventoPorNomeExato(List<Evento> eventos, String nome) {
        return eventos.stream()
            .filter(e -> e.getNome().equalsIgnoreCase(nome))
            .findFirst()
            .orElse(null);
    }
}