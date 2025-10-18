package service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import entity.Evento;
import entity.Participante;

public class GerenciadorDados {
    private static final String ARQUIVO_EVENTOS = "events.data";
    private static final String ARQUIVO_PARTICIPANTES = "participants.data";
    
    private static String getCaminhoConfiavel(String nomeArquivo) {
        String diretorioProjeto = System.getProperty("user.dir");
        return diretorioProjeto + File.separator + nomeArquivo;
    }
    
    public static void salvarEventos(List<Evento> eventos) {
        String caminho = getCaminhoConfiavel(ARQUIVO_EVENTOS);
        
        System.out.println("Salvando eventos em: " + caminho);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            for (Evento evento : eventos) {
                String linha = evento.toString();
                writer.println(linha);
                System.out.println("Evento salvo: " + linha);
            }
            System.out.println(eventos.size() + " eventos salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar eventos: " + e.getMessage());
        }
    }
    
    public static List<Evento> carregarEventos() {
        String caminho = getCaminhoConfiavel(ARQUIVO_EVENTOS);
        List<Evento> eventos = new ArrayList<>();
        File arquivo = new File(caminho);
        
        System.out.println("Tentando carregar eventos de: " + caminho);
        
        if (!arquivo.exists()) {
            System.out.println("Arquivo de eventos nao existe.");
            return eventos;
        }
        
        if (arquivo.length() == 0) {
            System.out.println("Arquivo de eventos esta vazio.");
            return eventos;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int contador = 0;
            int linhasInvalidas = 0;
            
            while ((linha = reader.readLine()) != null) {
                System.out.println("Lendo linha do arquivo: " + linha);
                
                if (linha.trim().isEmpty()) {
                    System.out.println("Linha vazia, ignorando...");
                    continue;
                }
                
                Evento evento = Evento.fromString(linha);
                if (evento != null) {
                    eventos.add(evento);
                    contador++;
                    System.out.println("Evento carregado: " + evento.getNome());
                } else {
                    linhasInvalidas++;
                    System.out.println("Linha invalida (nao pode converter): " + linha);
                }
            }
            
            System.out.println("Resultado do carregamento: " + contador + " eventos validos, " + linhasInvalidas + " linhas invalidas");
            
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo de eventos: " + e.getMessage());
        }
        
        return eventos;
    }
    
    public static void salvarParticipantes(List<Participante> participantes) {
        String caminho = getCaminhoConfiavel(ARQUIVO_PARTICIPANTES);
        
        System.out.println("Salvando participantes em: " + caminho);
        
        try (PrintWriter writer = new PrintWriter(new FileWriter(caminho))) {
            for (Participante participante : participantes) {
                String linha = participante.toString();
                writer.println(linha);
                System.out.println("Participante salvo: " + linha);
            }
            System.out.println(participantes.size() + " participantes salvos com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao salvar participantes: " + e.getMessage());
        }
    }
    
    public static List<Participante> carregarParticipantes() {
        String caminho = getCaminhoConfiavel(ARQUIVO_PARTICIPANTES);
        List<Participante> participantes = new ArrayList<>();
        File arquivo = new File(caminho);
        
        System.out.println("Tentando carregar participantes de: " + caminho);
        
        if (!arquivo.exists()) {
            System.out.println("Arquivo de participantes nao existe.");
            return participantes;
        }
        
        if (arquivo.length() == 0) {
            System.out.println("Arquivo de participantes esta vazio.");
            return participantes;
        }
        
        try (BufferedReader reader = new BufferedReader(new FileReader(arquivo))) {
            String linha;
            int contador = 0;
            int linhasInvalidas = 0;
            
            while ((linha = reader.readLine()) != null) {
                System.out.println("Lendo linha do arquivo: " + linha);
                
                if (linha.trim().isEmpty()) {
                    System.out.println("Linha vazia, ignorando...");
                    continue;
                }
                
                Participante participante = Participante.fromString(linha);
                if (participante != null) {
                    participantes.add(participante);
                    contador++;
                    System.out.println("Participante carregado: " + participante.getNome());
                } else {
                    linhasInvalidas++;
                    System.out.println("Linha invalida (nao pode converter): " + linha);
                }
            }
            
            System.out.println("Resultado do carregamento: " + contador + " participantes validos, " + linhasInvalidas + " linhas invalidas");
            
        } catch (IOException e) {
            System.out.println("Erro ao ler arquivo de participantes: " + e.getMessage());
        }
        
        return participantes;
    }
    
    public static void verificarEstadoArquivos() {
        String caminhoEventos = getCaminhoConfiavel(ARQUIVO_EVENTOS);
        String caminhoParticipantes = getCaminhoConfiavel(ARQUIVO_PARTICIPANTES);
        
        File arquivoEventos = new File(caminhoEventos);
        File arquivoParticipantes = new File(caminhoParticipantes);
        
        System.out.println("=== ESTADO DOS ARQUIVOS ===");
        System.out.println("Arquivo eventos: " + arquivoEventos.getAbsolutePath());
        System.out.println("  Existe: " + arquivoEventos.exists() + ", Tamanho: " + arquivoEventos.length() + " bytes");
        System.out.println("Arquivo participantes: " + arquivoParticipantes.getAbsolutePath());
        System.out.println("  Existe: " + arquivoParticipantes.exists() + ", Tamanho: " + arquivoParticipantes.length() + " bytes");
    }
}