package entity;

import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Participante {
    private static int nextId = 1;
    
    private int id;
    private String nome;
    private String email;
    private String telefone;
    private LocalDate dataNascimento;
    private List<Integer> eventosInscritos;
    
    public Participante(String nome, String email, String telefone, LocalDate dataNascimento) {
        this.id = nextId++;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.dataNascimento = dataNascimento;
        this.eventosInscritos = new ArrayList<>();
    }
    
    public int getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }
    public List<Integer> getEventosInscritos() { return eventosInscritos; }
    
    public void participarEvento(int eventoId) {
        if (!eventosInscritos.contains(eventoId)) {
            eventosInscritos.add(eventoId);
        }
    }
    
    public void cancelarParticipacao(int eventoId) {
        eventosInscritos.remove((Integer) eventoId);
    }
    
    public int calcularIdade() {
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }
    
    @Override
    public String toString() {
        DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        return id + "|" + nome + "|" + email + "|" + telefone + "|" + 
               dataNascimento.format(fmt) + "|" + 
               eventosInscritos.stream().map(String::valueOf).collect(Collectors.joining(","));
    }
    
    public static Participante fromString(String str) {
        String[] parts = str.split("\\|", -1); // O -1 mantém os campos vazios
        if (parts.length < 6) {
            System.out.println("Erro: Linha de participante com menos de 6 partes: " + parts.length);
            return null;
        }
        
        try {
            // Debug: mostrar todas as partes
            System.out.println("Partes do participante: " + java.util.Arrays.toString(parts));
            
            DateTimeFormatter fmt = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            
            // Verificar se a data não está vazia
            if (parts[4].trim().isEmpty()) {
                System.out.println("Erro: Data de nascimento vazia");
                return null;
            }
            
            Participante participante = new Participante(
                parts[1], 
                parts[2], 
                parts[3], 
                LocalDate.parse(parts[4], fmt)
            );
            
            participante.id = Integer.parseInt(parts[0]);
            
            // Processar eventos inscritos (pode estar vazio)
            if (!parts[5].isEmpty()) {
                String[] eventos = parts[5].split(",");
                for (String eventoId : eventos) {
                    if (!eventoId.trim().isEmpty()) {
                        participante.eventosInscritos.add(Integer.parseInt(eventoId));
                    }
                }
            }
            
            // Atualizar nextId se necessário
            if (participante.id >= nextId) {
                nextId = participante.id + 1;
            }
            
            System.out.println("Participante carregado com sucesso: " + participante.getNome());
            return participante;
            
        } catch (Exception e) {
            System.out.println("Erro ao converter linha para Participante: " + e.getMessage());
            System.out.println("Linha problematica: " + str);
            return null;
        }
    }
}