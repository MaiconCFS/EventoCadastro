package main;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import entity.Evento;
import entity.Participante;
import entity.TipoEvento;
import service.GerenciadorDados;
import service.ServicoEventos;
import util.FormatadorData;

public class AgendaEventosApp {
    private List<Participante> participantes;
    private List<Evento> eventos;
    private Participante usuarioAtual;
    
    public AgendaEventosApp() {
        System.out.println("Iniciando Agenda de Eventos...");
        
        // Verifica onde estão os arquivos
        GerenciadorDados.verificarEstadoArquivos();
        
        // Carrega os dados
        participantes = GerenciadorDados.carregarParticipantes();
        eventos = GerenciadorDados.carregarEventos();
        usuarioAtual = null;
        
        System.out.println("Sistema carregado com " + participantes.size() + 
                          " participantes e " + eventos.size() + " eventos.");
    }
    
    public void executar() {
        Scanner scanner = new Scanner(System.in);
        int opcao;
        
        System.out.println("==========================================");
        System.out.println("    BEM-VINDO A AGENDA DE EVENTOS!");
        System.out.println("==========================================");
        
        if (participantes.isEmpty()) {
            System.out.println("Parece que e sua primeira vez aqui!");
            System.out.println("Vamos criar seu cadastro para comecar:");
            registrarParticipante(scanner);
        } else {
            System.out.println("Ola! E bom ter voce de volta!");
            System.out.println("Fac-a login para continuar:");
            autenticarOuCadastrar(scanner);
        }
        
        do {
            exibirMenuPrincipal();
            try {
                opcao = scanner.nextInt();
                scanner.nextLine();
                
                switch (opcao) {
                    case 1:
                        autenticarOuCadastrar(scanner);
                        break;
                    case 2:
                        criarEvento(scanner);
                        break;
                    case 3:
                        listarEventos();
                        break;
                    case 4:
                        confirmarPresencaPorNome(scanner);
                        break;
                    case 5:
                        cancelarPresencaPorNome(scanner);
                        break;
                    case 6:
                        visualizarMinhasConfirmacoes();
                        break;
                    case 7:
                        visualizarEventosOcorrendoAgora();
                        break;
                    case 8:
                        visualizarEventosFuturos();
                        break;
                    case 9:
                        visualizarEventosPassados();
                        break;
                    case 10:
                        visualizarMeuPerfil();
                        break;
                    case 11:
                        buscarEventosPorNome(scanner);
                        break;
                    case 0:
                        encerrarPrograma();
                        break;
                    default:
                        System.out.println("Opcao invalida. Tente novamente.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Por favor, digite um numero valido.");
                scanner.nextLine();
                opcao = -1;
            }
        } while (opcao != 0);
        
        scanner.close();
    }
    
    private void salvarTudo() {
        System.out.println("Salvando todos os dados...");
        GerenciadorDados.salvarEventos(eventos);
        GerenciadorDados.salvarParticipantes(participantes);
    }
    
    private void encerrarPrograma() {
        System.out.println("Salvando todos os dados antes de sair...");
        salvarTudo();
        System.out.println("Dados salvos com sucesso!");
        System.out.println("Obrigado por usar nossa Agenda de Eventos!");
        System.out.println("   - " + eventos.size() + " eventos salvos");
        System.out.println("   - " + participantes.size() + " participantes salvos");
    }
    
    private void autenticarOuCadastrar(Scanner scanner) {
        System.out.println("--- ACESSO ---");
        System.out.println("1. Fazer login com email");
        System.out.println("2. Criar novo cadastro");
        System.out.println("3. Voltar ao menu principal");
        System.out.print("Escolha uma opcao: ");
        
        try {
            int escolha = scanner.nextInt();
            scanner.nextLine();
            
            switch (escolha) {
                case 1:
                    fazerLoginPorEmail(scanner);
                    break;
                case 2:
                    registrarParticipante(scanner);
                    break;
                case 3:
                    break;
                default:
                    System.out.println("Opcao invalida.");
            }
        } catch (InputMismatchException e) {
            System.out.println("Por favor, digite um numero valido.");
            scanner.nextLine();
        }
    }
    
    private void fazerLoginPorEmail(Scanner scanner) {
        System.out.println("--- LOGIN ---");
        System.out.print("Digite seu email: ");
        String email = scanner.nextLine().trim().toLowerCase();
        
        for (Participante p : participantes) {
            if (p.getEmail().equalsIgnoreCase(email)) {
                usuarioAtual = p;
                System.out.println("Login realizado com sucesso!");
                System.out.println("Bem-vindo(a) de volta, " + p.getNome() + "!");
                return;
            }
        }
        
        System.out.println("Email nao encontrado.");
        System.out.println("Deseja criar um novo cadastro? (S/N)");
        String resposta = scanner.nextLine();
        if (resposta.equalsIgnoreCase("S")) {
            registrarParticipante(scanner);
        }
    }
    
    private void registrarParticipante(Scanner scanner) {
        System.out.println("--- NOVO CADASTRO ---");
        
        String nome;
        do {
            System.out.print("Nome completo: ");
            nome = scanner.nextLine().trim();
            if (nome.isEmpty()) {
                System.out.println("O nome e obrigatorio. Tente novamente.");
            }
        } while (nome.isEmpty());
        
        String email;
        boolean emailValido = false;
        do {
            System.out.print("Email: ");
            email = scanner.nextLine().trim().toLowerCase();
            
            boolean emailExiste = false;
            for (Participante p : participantes) {
                if (p.getEmail().equalsIgnoreCase(email)) {
                    emailExiste = true;
                    break;
                }
            }
            
            if (emailExiste) {
                System.out.println("Este email ja esta cadastrado. Use outro email.");
            } else if (!email.contains("@") || !email.contains(".")) {
                System.out.println("Email invalido. Digite um email valido.");
            } else {
                emailValido = true;
            }
        } while (!emailValido);
        
        System.out.print("Telefone (opcional): ");
        String telefone = scanner.nextLine().trim();
        if (telefone.isEmpty()) {
            telefone = "Nao informado";
        }
        
        LocalDate dataNasc;
        while (true) {
            System.out.print("Data de nascimento (DD/MM/AAAA): ");
            String dataStr = scanner.nextLine().trim();
            
            try {
                dataNasc = FormatadorData.parseData(dataStr);
                
                if (dataNasc.isAfter(LocalDate.now())) {
                    System.out.println("Data de nascimento nao pode ser futura. Tente novamente.");
                    continue;
                }
                
                if (dataNasc.getYear() < 1900) {
                    System.out.println("Data de nascimento invalida. Tente novamente.");
                    continue;
                }
                
                break;
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage() + " Tente novamente.");
            }
        }
        
        Participante novo = new Participante(nome, email, telefone, dataNasc);
        participantes.add(novo);
        usuarioAtual = novo;
        
        System.out.println("Cadastro realizado com sucesso!");
        System.out.println("Seja bem-vindo(a), " + nome + "!");
        System.out.println("Seu email de acesso: " + email);
        
        // Salva imediatamente após cadastro
        GerenciadorDados.salvarParticipantes(participantes);
        System.out.println("Cadastro salvo no arquivo!");
    }
    
    private void exibirMenuPrincipal() {
        System.out.println("==========================================");
        System.out.println("            MENU PRINCIPAL");
        System.out.println("==========================================");
        
        if (usuarioAtual != null) {
            System.out.println("Usuario: " + usuarioAtual.getNome());
            System.out.println("Eventos confirmados: " + usuarioAtual.getEventosInscritos().size());
        }
        
        System.out.println("1. Gerenciar minha conta");
        System.out.println("2. Criar evento");
        System.out.println("3. Listar todos os eventos");
        System.out.println("4. Confirmar presenca em evento");
        System.out.println("5. Cancelar presenca em evento");
        System.out.println("6. Meus eventos confirmados");
        System.out.println("7. Eventos ocorrendo agora");
        System.out.println("8. Proximos eventos");
        System.out.println("9. Eventos anteriores");
        System.out.println("10. Meu perfil");
        System.out.println("11. Buscar eventos por nome");
        System.out.println("0. Sair e salvar tudo");
        System.out.print("Escolha uma opcao: ");
    }
    
    private void criarEvento(Scanner scanner) {
        if (usuarioAtual == null) {
            System.out.println("E necessario estar autenticado para criar eventos.");
            return;
        }
        
        System.out.println("--- NOVO EVENTO ---");
        System.out.print("Nome do evento: ");
        String nome = scanner.nextLine();
        
        System.out.print("Endereco: ");
        String endereco = scanner.nextLine();
        
        System.out.println("Categorias disponiveis:");
        String[] categorias = TipoEvento.getDescricoes();
        for (int i = 0; i < categorias.length; i++) {
            System.out.println(i + " - " + categorias[i]);
        }
        
        System.out.print("Selecione a categoria: ");
        int catIndex = scanner.nextInt();
        scanner.nextLine();
        
        if (catIndex < 0 || catIndex >= categorias.length) {
            System.out.println("Categoria invalida. Usando padrao (Festa).");
            catIndex = 0;
        }
        
        System.out.print("Data (DD/MM/AAAA): ");
        String dataStr = scanner.nextLine();
        
        System.out.print("Hora (HH:MM): ");
        String horaStr = scanner.nextLine();
        
        LocalDateTime dataHora;
        try {
            dataHora = FormatadorData.parseDataHora(dataStr + " " + horaStr);
        } catch (IllegalArgumentException e) {
            System.out.println(e.getMessage() + " Usando data/hora atual.");
            dataHora = LocalDateTime.now().plusDays(1);
        }
        
        System.out.print("Descricao: ");
        String descricao = scanner.nextLine();
        
        Evento novoEvento = new Evento(nome, endereco, TipoEvento.fromInt(catIndex), dataHora, descricao);
        eventos.add(novoEvento);
        
        System.out.println("Evento criado com sucesso! Nome: " + novoEvento.getNome());
        
        // Salva imediatamente após criar evento
        GerenciadorDados.salvarEventos(eventos);
        System.out.println("Evento salvo no arquivo!");
    }
    
    private void listarEventos() {
        System.out.println("--- EVENTOS DISPONIVEIS ---");
        
        if (eventos.isEmpty()) {
            System.out.println("Nenhum evento cadastrado.");
            return;
        }
        
        List<Evento> eventosOrdenados = ServicoEventos.ordenarEventosPorProximidade(eventos);
        
        for (Evento e : eventosOrdenados) {
            System.out.println("Nome: " + e.getNome());
            System.out.println("Local: " + e.getEndereco());
            System.out.println("Categoria: " + e.getCategoria().getDescricao());
            System.out.println("Data/Hora: " + FormatadorData.formatarDataHora(e.getHorario()));
            System.out.println("Status: " + e.getStatus());
            System.out.println("Descricao: " + e.getDescricao());
            System.out.println("Participantes: " + e.getParticipantes().size());
            System.out.println("----------------------------------------");
        }
    }
    
    private void confirmarPresencaPorNome(Scanner scanner) {
        if (usuarioAtual == null) {
            System.out.println("E necessario estar autenticado para confirmar presenca.");
            return;
        }
        
        System.out.println("--- CONFIRMAR PRESENCA ---");
        System.out.print("Digite o nome do evento: ");
        String nomeEvento = scanner.nextLine().trim();
        
        if (nomeEvento.isEmpty()) {
            System.out.println("Nome do evento nao pode estar vazio.");
            return;
        }
        
        List<Evento> eventosEncontrados = ServicoEventos.buscarEventosPorNome(eventos, nomeEvento);
        
        if (eventosEncontrados.isEmpty()) {
            System.out.println("Nenhum evento encontrado com o nome: " + nomeEvento);
            return;
        }
        
        if (eventosEncontrados.size() == 1) {
            Evento evento = eventosEncontrados.get(0);
            processarConfirmacaoPresenca(evento);
            return;
        }
        
        System.out.println("Foram encontrados " + eventosEncontrados.size() + " eventos:");
        for (int i = 0; i < eventosEncontrados.size(); i++) {
            Evento e = eventosEncontrados.get(i);
            System.out.println((i + 1) + ". " + e.getNome() + " - " + 
                             FormatadorData.formatarDataHora(e.getHorario()) + " - " + e.getEndereco());
        }
        
        System.out.print("Selecione o numero do evento: ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha > 0 && escolha <= eventosEncontrados.size()) {
                Evento eventoSelecionado = eventosEncontrados.get(escolha - 1);
                processarConfirmacaoPresenca(eventoSelecionado);
            } else {
                System.out.println("Selecao invalida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, digite um numero valido.");
        }
    }
    
    private void processarConfirmacaoPresenca(Evento evento) {
        if (evento.isPassado()) {
            System.out.println("Este evento ja ocorreu: " + evento.getNome());
            return;
        }
        
        if (usuarioAtual.getEventosInscritos().contains(evento.getId())) {
            System.out.println("Voce ja esta inscrito neste evento: " + evento.getNome());
            return;
        }
        
        evento.adicionarParticipante(usuarioAtual.getId());
        usuarioAtual.participarEvento(evento.getId());
        
        System.out.println("Presenca confirmada para: " + evento.getNome());
        System.out.println("Data: " + FormatadorData.formatarDataHora(evento.getHorario()));
        
        salvarTudo();
        System.out.println("Inscricao salva!");
    }
    
    private void cancelarPresencaPorNome(Scanner scanner) {
        if (usuarioAtual == null) {
            System.out.println("E necessario estar autenticado para cancelar presenca.");
            return;
        }
        
        System.out.println("--- CANCELAR PRESENCA ---");
        System.out.print("Digite o nome do evento: ");
        String nomeEvento = scanner.nextLine().trim();
        
        if (nomeEvento.isEmpty()) {
            System.out.println("Nome do evento nao pode estar vazio.");
            return;
        }
        
        List<Evento> meusEventos = eventos.stream()
            .filter(e -> usuarioAtual.getEventosInscritos().contains(e.getId()))
            .filter(e -> e.getNome().toLowerCase().contains(nomeEvento.toLowerCase()))
            .collect(Collectors.toList());
        
        if (meusEventos.isEmpty()) {
            System.out.println("Nenhum evento encontrado ou voce nao esta inscrito em: " + nomeEvento);
            return;
        }
        
        if (meusEventos.size() == 1) {
            Evento evento = meusEventos.get(0);
            processarCancelamentoPresenca(evento);
            return;
        }
        
        System.out.println("Foram encontrados " + meusEventos.size() + " eventos:");
        for (int i = 0; i < meusEventos.size(); i++) {
            Evento e = meusEventos.get(i);
            System.out.println((i + 1) + ". " + e.getNome() + " - " + 
                             FormatadorData.formatarDataHora(e.getHorario()) + " - " + e.getEndereco());
        }
        
        System.out.print("Selecione o numero do evento: ");
        try {
            int escolha = Integer.parseInt(scanner.nextLine());
            if (escolha > 0 && escolha <= meusEventos.size()) {
                Evento eventoSelecionado = meusEventos.get(escolha - 1);
                processarCancelamentoPresenca(eventoSelecionado);
            } else {
                System.out.println("Selecao invalida.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Por favor, digite um numero valido.");
        }
    }
    
    private void processarCancelamentoPresenca(Evento evento) {
        evento.removerParticipante(usuarioAtual.getId());
        usuarioAtual.cancelarParticipacao(evento.getId());
        
        System.out.println("Presenca cancelada para: " + evento.getNome());
        salvarTudo();
    }
    
    private void visualizarMinhasConfirmacoes() {
        if (usuarioAtual == null) {
            System.out.println("E necessario estar autenticado.");
            return;
        }
        
        System.out.println("--- MEUS EVENTOS CONFIRMADOS ---");
        
        List<Integer> meusEventos = usuarioAtual.getEventosInscritos();
        if (meusEventos.isEmpty()) {
            System.out.println("Voce nao confirmou presenca em nenhum evento.");
            return;
        }
        
        for (Evento e : eventos) {
            if (meusEventos.contains(e.getId())) {
                System.out.println("Nome: " + e.getNome());
                System.out.println("Data/Hora: " + FormatadorData.formatarDataHora(e.getHorario()));
                System.out.println("Status: " + e.getStatus());
                System.out.println("Local: " + e.getEndereco());
                System.out.println("----------------------------------------");
            }
        }
    }
    
    private void visualizarEventosOcorrendoAgora() {
        System.out.println("--- EVENTOS OCORRENDO AGORA ---");
        
        List<Evento> ocorrendoAgora = ServicoEventos.getEventosOcorrendoAgora(eventos);
        
        if (ocorrendoAgora.isEmpty()) {
            System.out.println("Nenhum evento ocorrendo no momento.");
            return;
        }
        
        for (Evento e : ocorrendoAgora) {
            System.out.println("Nome: " + e.getNome());
            System.out.println("Local: " + e.getEndereco());
            System.out.println("Descricao: " + e.getDescricao());
            System.out.println("Horario: " + FormatadorData.formatarDataHora(e.getHorario()));
            System.out.println("----------------------------------------");
        }
    }
    
    private void visualizarEventosFuturos() {
        System.out.println("--- PROXIMOS EVENTOS ---");
        
        List<Evento> futuros = ServicoEventos.getEventosFuturos(eventos);
        
        if (futuros.isEmpty()) {
            System.out.println("Nenhum evento futuro agendado.");
            return;
        }
        
        for (Evento e : futuros) {
            System.out.println("Nome: " + e.getNome());
            System.out.println("Data/Hora: " + FormatadorData.formatarDataHora(e.getHorario()));
            System.out.println("Local: " + e.getEndereco());
            System.out.println("----------------------------------------");
        }
    }
    
    private void visualizarEventosPassados() {
        System.out.println("--- EVENTOS ANTERIORES ---");
        
        List<Evento> passados = ServicoEventos.getEventosPassados(eventos);
        
        if (passados.isEmpty()) {
            System.out.println("Nenhum evento anterior registrado.");
            return;
        }
        
        for (Evento e : passados) {
            System.out.println("Nome: " + e.getNome());
            System.out.println("Data/Hora: " + FormatadorData.formatarDataHora(e.getHorario()));
            System.out.println("Local: " + e.getEndereco());
            System.out.println("----------------------------------------");
        }
    }
    
    private void visualizarMeuPerfil() {
        if (usuarioAtual == null) {
            System.out.println("E necessario estar autenticado.");
            return;
        }
        
        System.out.println("--- MEU PERFIL ---");
        System.out.println("Nome: " + usuarioAtual.getNome());
        System.out.println("Email: " + usuarioAtual.getEmail());
        System.out.println("Telefone: " + usuarioAtual.getTelefone());
        System.out.println("Data de Nascimento: " + FormatadorData.formatarData(usuarioAtual.getDataNascimento()));
        System.out.println("Idade: " + usuarioAtual.calcularIdade() + " anos");
        System.out.println("Eventos confirmados: " + usuarioAtual.getEventosInscritos().size());
    }
    
    private void buscarEventosPorNome(Scanner scanner) {
        System.out.println("--- BUSCAR EVENTOS POR NOME ---");
        System.out.print("Digite o nome do evento (ou parte dele): ");
        String nomeBusca = scanner.nextLine().trim();
        
        if (nomeBusca.isEmpty()) {
            System.out.println("Por favor, digite um nome para buscar.");
            return;
        }
        
        List<Evento> eventosEncontrados = ServicoEventos.buscarEventosPorNome(eventos, nomeBusca);
        
        if (eventosEncontrados.isEmpty()) {
            System.out.println("Nenhum evento encontrado com: '" + nomeBusca + "'");
            return;
        }
        
        System.out.println("Foram encontrados " + eventosEncontrados.size() + " eventos:");
        for (Evento e : eventosEncontrados) {
            System.out.println("Nome: " + e.getNome());
            System.out.println("Data/Hora: " + FormatadorData.formatarDataHora(e.getHorario()));
            System.out.println("Local: " + e.getEndereco());
            System.out.println("Categoria: " + e.getCategoria().getDescricao());
            System.out.println("Status: " + e.getStatus());
            System.out.println("----------------------------------------");
        }
    }
    
    public static void main(String[] args) {
        AgendaEventosApp app = new AgendaEventosApp();
        app.executar();
    }
}