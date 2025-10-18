package entity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Evento {
    private static int nextId = 1;
    
    private int id;
    private String nome;
    private String endereco;
    private TipoEvento categoria;
    private LocalDateTime horario;
    private String descricao;
    private List<Integer> participantes;
    
    public Evento(String nome, String endereco, TipoEvento categoria, 
                 LocalDateTime horario, String descricao) {
        this.id = nextId++;
        this.nome = nome;
        this.endereco = endereco;
        this.categoria = categoria;
        this.horario = horario;
        this.descricao = descricao;
        this.participantes = new ArrayList<>();
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }
    public TipoEvento getCategoria() { return categoria; }
    public void setCategoria(TipoEvento categoria) { this.categoria = categoria; }
    public LocalDateTime getHorario() { return horario; }
    public void setHorario(LocalDateTime horario) { this.horario = horario; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public List<Integer> getParticipantes() { return participantes; }
    
    public void adicionarParticipante(int usuarioId) {
        if (!participantes.contains(usuarioId)) {
            participantes.add(usuarioId);
        }
    }
    
    public void removerParticipante(int usuarioId) {
        participantes.remove((Integer) usuarioId);
    }
    
    public String getStatus() {
        LocalDateTime agora = LocalDateTime.now();
        if (agora.isAfter(horario.plusHours(2))) {
            return "Ja ocorreu";
        } else if (agora.isAfter(horario.minusMinutes(30)) && agora.isBefore(horario.plusHours(2))) {
            return "Esta ocorrendo agora";
        } else {
            return "Ainda vai ocorrer";
        }
    }
    
    public boolean isOcorrendoAgora() {
        LocalDateTime agora = LocalDateTime.now();
        return agora.isAfter(horario.minusMinutes(30)) && agora.isBefore(horario.plusHours(2));
    }
    
    public boolean isFuturo() {
        return horario.isAfter(LocalDateTime.now());
    }
    
    public boolean isPassado() {
        return horario.plusHours(2).isBefore(LocalDateTime.now());
    }
    
    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        return id + "|" + nome + "|" + endereco + "|" + categoria.ordinal() + "|" + 
               horario.format(formatter) + "|" + descricao + "|" + 
               participantes.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    
    public static Evento fromString(String str) {
        String[] parts = str.split("\\|", -1); // O -1 mantém os campos vazios
        if (parts.length < 7) {
            System.out.println("Erro: Linha de evento com menos de 7 partes: " + parts.length);
            return null;
        }
        
        try {
            // Debug: mostrar todas as partes
            System.out.println("Partes do evento: " + java.util.Arrays.toString(parts));
            
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            
            // Verificar se a data/hora não está vazia
            if (parts[4].trim().isEmpty()) {
                System.out.println("Erro: Data/hora vazia");
                return null;
            }
            
            Evento evento = new Evento(
                parts[1], 
                parts[2], 
                TipoEvento.fromInt(Integer.parseInt(parts[3])), 
                LocalDateTime.parse(parts[4], formatter), 
                parts[5]
            );
            
            evento.id = Integer.parseInt(parts[0]);
            
            // Processar participantes (pode estar vazio)
            if (!parts[6].isEmpty()) {
                String[] participantes = parts[6].split(",");
                for (String participanteId : participantes) {
                    if (!participanteId.trim().isEmpty()) {
                        evento.participantes.add(Integer.parseInt(participanteId));
                    }
                }
            }
            
            // Atualizar nextId se necessário
            if (evento.id >= nextId) {
                nextId = evento.id + 1;
            }
            
            System.out.println("Evento carregado com sucesso: " + evento.getNome());
            return evento;
            
        } catch (Exception e) {
            System.out.println("Erro ao converter linha para Evento: " + e.getMessage());
            System.out.println("Linha problematica: " + str);
            return null;
        }
    }
}