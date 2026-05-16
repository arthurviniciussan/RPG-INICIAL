import java.util.Random;
import java.util.Scanner;

// ╔═════════════════════════════════════════════════════════════════════╗
// ║              BLOODDIVINE RPG — SUMÁRIO DO PROJETO                   ║
// ╠═════════════════════════════════════════════════════════════════════╣
// ║  [MAIN]                → Ponto de entrada do programa               ║
// ║                                                                     ║
// ║  [UTILITÁRIOS]                                                      ║
// ║    [> Console]         → digitar(), limparTela(), lerOpcaoValida()  ║
// ║                          limparTelaDevagar()                        ║
// ║    [> Cores]           → Constantes ANSI de cor (classe Cores)      ║
// ║                                                                     ║
// ║  [CLASSES RPG]         → Nomes e exibição das classes do jogador    ║
// ║                                                                     ║
// ║  [INIMIGOS]                                                         ║
// ║    [> Constantes]      → Chaves dos inimigos (classe Inimigos)      ║
// ║    [> Modelo]          → Classe Inimigo: criação e ações            ║
// ║                                                                     ║
// ║  [PLAYER]                                                           ║
// ║    [> Modelo]          → Classe Player: ações e buffs               ║
// ║    [> Criação]         → escolhaClasse(), opcaoParaClasse()         ║
// ║    [> Buffs]           → escolherEAplicarBuff()                     ║
// ║                                                                     ║
// ║  [COMBATE]                                                          ║
// ║    [> Iniciativa]      → playerTemIniciativa()                      ║
// ║    [> Crítico]         → calcularDanoCritico()  ← 20–80% extra      ║
// ║    [> Ataques]         → aplicarAtaquePlayer(), aplicarAtaqueInimigo║
// ║    [> Turnos]          → turnoAtaque(), turnoCura()                 ║
// ║    [> Fluxo]           → combate(), executarFase()                  ║
// ║                                                                     ║
// ║  [GRÁFICOS]                                                         ║
// ║    [> HUD]             → mostrarHud(), mostrarStatus()              ║
// ║    [> Arte ASCII]      → imprimirTituloBloodDivine(), mostrarSala() ║
// ╚═════════════════════════════════════════════════════════════════════╝


public class Main7 {

    // Globais do Random e Scanner
    private static final Random  rand  = new Random();
    private static final Scanner scan = new Scanner(System.in);

  // [MAIN]
    public static void main(String[] args) throws InterruptedException {
        Player player = escolhaClasse();

        Inimigo esqueleto      = Inimigo.criar(Inimigos.ESQUELETO);
        Inimigo goblin         = Inimigo.criar(Inimigos.GOBLIN);
        Inimigo demonio        = Inimigo.criar(Inimigos.DEMONIO);
        Inimigo cavaleiroNegro = Inimigo.criar(Inimigos.CAVALEIRO_NEGRO);
        Inimigo reiDemonio     = Inimigo.criar(Inimigos.REI_DEMONIO);

        digitar("🎮⚔️ " + Cores.NEGRITO + Cores.VERMELHO_FORTE + "BLOODDIVINE "
                + Cores.AMARELO_FORTE + "INICIANDO... "
                + Cores.CIANO_FORTE + "Prepare-se para a batalha! 🎮⚔️" + Cores.RESET);
        Thread.sleep(3000);
        if (!executarFase(player, esqueleto,      1, 1)) return;
        if (!executarFase(player, goblin,         2, 2)) return;
        if (!executarFase(player, demonio,        3, 3)) return;
        if (!executarFase(player, cavaleiroNegro, 4, 4)) return;
        if (!executarFase(player, reiDemonio,     5, 5)) return;

        limparTela();
        digitar(Cores.AMARELO_FORTE + "🏆 Você derrotou o Rei-Demônio e salvou o mundo!" + Cores.RESET);
        digitar(Cores.CIANO_FORTE   + "★ BLOODDIVINE COMPLETO! Parabéns, herói! ★"       + Cores.RESET);

        scan.close();
    }


    // [UTILITÁRIOS]
 

    // [UTILITÁRIOS > Console] 

    public static void digitar(String texto) throws InterruptedException {
        for (char c : texto.toCharArray()) {
            System.out.print(c);
            Thread.sleep(20);
        }
        System.out.println();
    }

    public static void limparTela() {
        for (int i = 0; i < 50; i++) System.out.println();
    }

    public static void limparTelaDevagar() throws InterruptedException {
        for (int i = 0; i < 50; i++) {System.out.println(); Thread.sleep(40);}
    }

    public static int lerOpcaoValida(int min, int max) throws InterruptedException {
        while (true) {
            if (!scan.hasNextInt()) {
  
                digitar("❌ Opção inválida. Digite um número de " + min + " a " + max + ".");
                scan.next();
                Thread.sleep(1000);

                continue;
            }
            int opcao = scan.nextInt();
            if (opcao >= min && opcao <= max) return opcao;

            digitar("❌ Opção inválida. Digite um número de " + min + " a " + max + ".");
            Thread.sleep(1000);

        }
    }

    // [UTILITÁRIOS > Cores]

    public static class Cores {
        public static final String VERDE            = "\033[32m";
        public static final String VERDE_BRILHANTE  = "\033[1;32m";
        public static final String VERMELHO         = "\033[31m";
        public static final String AZUL             = "\033[34m";
        public static final String AZUL_BRILHANTE   = "\033[1;34m";
        public static final String ROXO             = "\033[35m";
        public static final String AMARELO_FORTE    = "\033[1;33m";
        public static final String CIANO_FORTE      = "\033[1;36m";
        public static final String VERMELHO_FORTE   = "\033[1;31m";
        public static final String NEGRITO          = "\033[1m";
        public static final String RESET            = "\033[0m";
    }

    // [CLASSES RPG]
    public static class ClassesRpg {
        public static final String GUERREIRO = "GUERREIRO";
        public static final String ASSASSINO = "ASSASSINO";
        public static final String TANK      = "TANK";

        // Separei a string colorida da string normal para poder usar EX: Player.criar(ClassesRpg.GUERREIRO)
        // Esse método aqui mostra a string colorida
        public static String formatado(String classe) {
            switch (classe) {
                case GUERREIRO: return Cores.NEGRITO + Cores.VERMELHO_FORTE + "GUERREIRO" + Cores.RESET;
                case ASSASSINO: return Cores.NEGRITO + Cores.ROXO           + "ASSASSINO" + Cores.RESET;
                case TANK:      return Cores.NEGRITO + Cores.AZUL           + "TANK"      + Cores.RESET;
                default:        return classe;
            }
        }
    }


    // [INIMIGOS]

    // [INIMIGOS > Constantes] 

    public static class Inimigos {
        public static final String ESQUELETO       = "ESQUELETO";
        public static final String GOBLIN          = "GOBLIN";
        public static final String DEMONIO         = "DEMÔNIO";
        public static final String CAVALEIRO_NEGRO = "CAVALEIRO-NEGRO";
        public static final String REI_DEMONIO     = "REI-DEMÔNIO";
    }

    // [INIMIGOS > Modelo]

    public static class Inimigo {
        int vida;
        int dano;
        String nome;

        public void atacar(Player player) {
            player.receberDano(dano);
        }

        public static Inimigo criar(String nome) {
            Inimigo ini = new Inimigo();
            ini.nome = nome;

            switch (nome) {
                case Inimigos.ESQUELETO:
                    ini.vida = 50;  ini.dano = rand.nextInt(6)  + 5;  break;
                case Inimigos.GOBLIN:
                    ini.vida = 80;  ini.dano = rand.nextInt(10) + 8;  break;
                case Inimigos.DEMONIO:
                    ini.vida = 100; ini.dano = rand.nextInt(15) + 12; break;
                case Inimigos.CAVALEIRO_NEGRO:
                    ini.vida = 130; ini.dano = rand.nextInt(16) + 15; break;
                case Inimigos.REI_DEMONIO:
                    ini.vida = 220; ini.dano = rand.nextInt(20) + 20; break;
                default:
                    throw new IllegalArgumentException("Inimigo desconhecido: " + nome);
            }
            return ini;
        }
    }

    // [PLAYER]


    // [PLAYER > Modelo]

    public static class Player {
        int vida;
        int vidaMax;
        int dano;
        int cura;
        int quantidadeCura;
        int iniciativa;
        String classe;

        public void receberDano(int danoRecebido) {
            vida -= danoRecebido;
        }

        public void curar() {
            if (quantidadeCura <= 0) {
                System.out.println(Cores.AMARELO_FORTE + "❌ Sem curas disponíveis!" + Cores.RESET);
                return;
            }
            vida = Math.min(vida + cura, vidaMax);
            quantidadeCura--;
            System.out.println("✨ Curado! Vida: " + Cores.VERDE + vida + "/" + vidaMax + Cores.RESET);
        }

        public void resetarVida() {
            vida = vidaMax;
        }

        public void aplicarBuff(int tipoBuff, int nivel) {
            int[] bonus = calcularBonusPorNivel(nivel);
            switch (tipoBuff) {
                case 1:
                    vidaMax += bonus[0];
                    vida = vidaMax;
                    System.out.printf(Cores.VERMELHO      + "❤️  +%d de vida máxima! " + Cores.RESET + "Vida: %d/%d%n",  bonus[0], vida, vidaMax);
                    break;
                case 2:
                    dano += bonus[1];
                    System.out.printf(Cores.AMARELO_FORTE + "⚔️  +%d de dano!        " + Cores.RESET + "Dano atual: %d%n", bonus[1], dano);
                    break;
                case 3:
                    quantidadeCura += bonus[2];
                    System.out.printf(Cores.VERDE         + "🧪 +%d de cura!         " + Cores.RESET + "Curas: %d%n",      bonus[2], quantidadeCura);
                    break;
            }
        }

        // Retorna um vetor de int [vidaBonus, danoBonus, curaBonus] diferente em cada nível
        private int[] calcularBonusPorNivel(int nivel) {
            switch (nivel) {
                case 1: return new int[]{ rand.nextInt(10) + 15, rand.nextInt(4) + 4,  1 };
                case 2: return new int[]{ rand.nextInt(11) + 25, rand.nextInt(6) + 7,  1 };
                case 3: return new int[]{ rand.nextInt(6)  + 35, rand.nextInt(5) + 10, 1 };
                case 4: return new int[]{ rand.nextInt(16) + 30, rand.nextInt(5) + 13, 1 };
                case 5: return new int[]{ rand.nextInt(8)  + 43, rand.nextInt(5) + 16, 2 };
                default: return new int[]{ 0, 0, 0 };
            }
        }

        public static Player criar(String classe) {
            Player p = new Player();
            p.classe = classe;
            switch (classe) {
                case ClassesRpg.GUERREIRO:
                    p.vida = p.vidaMax = 100; p.dano = 20; p.cura = 30; p.quantidadeCura = 4; p.iniciativa = 10; break;
                case ClassesRpg.ASSASSINO:
                    p.vida = p.vidaMax = 90;  p.dano = 25; p.cura = 25; p.quantidadeCura = 3; p.iniciativa = 15; break;
                case ClassesRpg.TANK:
                    p.vida = p.vidaMax = 150; p.dano = 17; p.cura = 40; p.quantidadeCura = 5; p.iniciativa = 5;  break;
            }
            return p;
        }
    }

    // [PLAYER > Criação]

    // esse método carrega o título, e lida com a escolha de classe retornando um Player
    public static Player escolhaClasse() throws InterruptedException {
        digitar("Bem-Vindo à história de");
        System.out.println();
        Thread.sleep(1000);
        imprimirTituloBloodDivine();
        Thread.sleep(1500);
        limparTelaDevagar();

        digitar("Escolha sua classe no RPG de " + Cores.VERMELHO_FORTE + "BLOODDIVINE" + Cores.RESET);
        System.out.println("1) ⚔️  " + ClassesRpg.formatado(ClassesRpg.GUERREIRO) + " — 100HP | 20ATK | 4 curas | Iniciativa média");
        System.out.println("2) 🗡️  " + ClassesRpg.formatado(ClassesRpg.ASSASSINO) + " — 90HP  | 25ATK | 3 curas | Alta iniciativa");
        System.out.println("3) 🛡️  " + ClassesRpg.formatado(ClassesRpg.TANK)      + " — 150HP | 15ATK | 5 curas | Baixa iniciativa");
        System.out.print("Opção: ");

        String classe = opcaoParaClasse(lerOpcaoValida(1, 3));
        digitar("Sua classe: " + ClassesRpg.formatado(classe));
        Thread.sleep(1000);
        limparTela();
        return Player.criar(classe);
    }

    private static String opcaoParaClasse(int opcao) {
        switch (opcao) {
            case 1:  return ClassesRpg.GUERREIRO;
            case 2:  return ClassesRpg.ASSASSINO;
            default: return ClassesRpg.TANK;
        }
    }

    // [PLAYER > Buffs]

    // extraindo a lógica do buff da classe do player
    public static void escolherEAplicarBuff(Player player, int nivelBuff) throws InterruptedException {
        limparTela();
        digitar(Cores.VERDE + "Parabéns! Buff de Nível " + nivelBuff + " disponível!" + Cores.RESET);
        Thread.sleep(1500);
        limparTela();

        digitar("Escolha o buff (Nível " + nivelBuff + "):");
        System.out.println("1) ❤️  Vida Máxima — " + Cores.VERMELHO        + "Aumenta o HP total."          + Cores.RESET);
        System.out.println("2) ⚔️  Dano        — " + Cores.AZUL_BRILHANTE  + "Aumenta o dano por ataque."   + Cores.RESET);
        System.out.println("3) 🧪 Curas       — " + Cores.VERDE_BRILHANTE + "Recebe mais poções de cura."  + Cores.RESET);
        System.out.print("Opção: ");

        player.aplicarBuff(lerOpcaoValida(1, 3), nivelBuff);
        Thread.sleep(1000);
        limparTelaDevagar();
    }

    // [COMBATE]

    //Variáveis globais do combate
    private static final int CHANCE_CRITICO_PERCENT = 30; //%(porcentagem) de chance de acertar um crítico
    private static final int CRITICO_BONUS_MIN      = 20; //%(porcentagem) mínima de dano extra no crítico
    private static final int CRITICO_BONUS_MAX      = 80; //%(porcentagem) máxima de dano extra no crítico

    // [COMBATE > Iniciativa]

    // Rola o "D20" e retorna true se o player age primeiro e false se o inimigo age primeiro 
    private static boolean playerTemIniciativa(Player player) {
        int rolagemPlayer  = rand.nextInt(20) + player.iniciativa;
        int rolagemInimigo = rand.nextInt(20);
        return rolagemPlayer >= rolagemInimigo;
    }

    // [COMBATE > Crítico]

    // Chance de 30%. Bônus de 20% a 80% do dano base.
    private static int calcularDanoCritico(int danoBase) throws InterruptedException {
        boolean FoiCritico = rand.nextInt(100) < CHANCE_CRITICO_PERCENT;
        if (!FoiCritico) return danoBase;

        int bonusPercent = rand.nextInt(CRITICO_BONUS_MAX - CRITICO_BONUS_MIN) + CRITICO_BONUS_MIN;
        int danoFinal    = danoBase + (danoBase * bonusPercent / 100);
        digitar(Cores.AMARELO_FORTE + "💥 CRÍTICO! (+" + bonusPercent + "%) " + danoBase + " → " + danoFinal + Cores.RESET);
        return danoFinal;
    }

    // [COMBATE > Ataques]

    // Aqui aplico o dano que já vem calculado e se foi crítico ou não
    private static void aplicarAtaquePlayer(Player player, Inimigo inimigo) throws InterruptedException {
        int dano = calcularDanoCritico(player.dano);
        inimigo.vida -= dano;
        digitar("⚔️ Você atacou " + Cores.VERMELHO + inimigo.nome + Cores.RESET
                + "! Dano: " + Cores.AZUL + dano + Cores.RESET);
    }

    // podeDarCritico = false quando o player está se curando
    // Inimigo não pode acertar um critico durante a cura
    private static void aplicarAtaqueInimigo(Inimigo inimigo, Player player, boolean podeDarCritico) throws InterruptedException {
        int dano = 0;
        if (podeDarCritico)
            dano = calcularDanoCritico(inimigo.dano);
        else
            dano = inimigo.dano;
        player.receberDano(dano);
        digitar(Cores.VERMELHO + "🩸 " + inimigo.nome + " causou " + dano + " de dano!" + Cores.RESET);
    }

    // [COMBATE > Turnos]

    // Turno de ataque: rola iniciativa e resolve a ordem das ações.
    private static void turnoAtaque(Player player, Inimigo inimigo) throws InterruptedException {
        if (playerTemIniciativa(player)) {
            digitar(Cores.CIANO_FORTE + "⚡ Você age primeiro!" + Cores.RESET);
            aplicarAtaquePlayer(player, inimigo);
            if (inimigo.vida > 0) {
                digitar(Cores.VERMELHO + inimigo.nome + " contra-ataca!" + Cores.RESET);
                aplicarAtaqueInimigo(inimigo, player, true); // crítico permitido
            }
        } else {
            digitar(Cores.VERMELHO + "⚡ " + inimigo.nome + " age primeiro!" + Cores.RESET);
            aplicarAtaqueInimigo(inimigo, player, true); // crítico permitido
            if (player.vida > 0) aplicarAtaquePlayer(player, inimigo);
        }
    }

    // Turno de cura: inimigo pode atacar de volta, mas SEM chance de crítico.
    private static void turnoCura(Player player, Inimigo inimigo) throws InterruptedException {
        int inimigoAcertouAtaque = rand.nextInt(0,2);
        player.curar();
        if (inimigoAcertouAtaque == 2){
            digitar("O " + inimigo.nome + " ataca durante sua cura!");
        aplicarAtaqueInimigo(inimigo, player, false); // crítico BLOQUEADO
        }
        else {
            digitar("O " + inimigo.nome + " errou o ataque durante a cura!");
        }
    }

    // [COMBATE > Fluxo]

    public static boolean ExecutarCombate(Player player, Inimigo inimigo) throws InterruptedException {
        int round         = 1;
        int danoAcumulado = 0;

        digitar("\nApareceu um " + Cores.VERMELHO + inimigo.nome + Cores.RESET + "!!");

        while (player.vida > 0 && inimigo.vida > 0) {
            int vidaAntes = player.vida;
            mostrarHud(inimigo, player, round);

            int escolha = lerOpcaoValida(1, 3);

            // Status não consome round
            if (escolha == 3) {
                mostrarStatus(player, danoAcumulado);
                limparTelaDevagar();
                continue;
            }

            if (escolha == 1) turnoAtaque(player, inimigo);
            else              turnoCura(player, inimigo);

            danoAcumulado += Math.max(0, vidaAntes - player.vida);
            Thread.sleep(1500);
            round++;
            limparTela();
        }

        if (player.vida <= 0) {
            digitar("☠️ Você morreu...");
            return false;
        }
        digitar("🎖️ Você venceu " + Cores.VERMELHO + inimigo.nome + Cores.RESET + "!");
        Thread.sleep(1500);
        return true;
    }

    // Encapsula combate + reset de vida + escolha de buff em uma só fase
    private static boolean executarFase(Player player, Inimigo inimigo, int nivelBuff, int salaAtual) throws InterruptedException {
        mostrarSala(salaAtual);
        if (!ExecutarCombate(player, inimigo)) return false;
        player.resetarVida();
        escolherEAplicarBuff(player, nivelBuff);
        return true;
    }

    // [GRÁFICOS]


    // [GRÁFICOS > HUD]

    public static void mostrarHud(Inimigo inimigo, Player player, int round) {
        System.out.println("— Round " + round + " ──────────────────────────────");
        System.out.println(Cores.VERDE    + "❤️  Sua Vida:          " + player.vida + "/" + player.vidaMax    + Cores.RESET);
        System.out.println(Cores.VERMELHO + "👾 " + inimigo.nome + ":           " + inimigo.vida              + Cores.RESET);
        System.out.println(Cores.AZUL     + "📦 Curas disponíveis:  " + player.quantidadeCura                 + Cores.RESET);
        System.out.println("────────────────────────────────────────");
        System.out.println("1) ⚔️  Atacar   2) 🧪 Curar   3) 📊 Status");
        System.out.print("Opção: ");
    }

    public static void mostrarStatus(Player player, int danoAcumulado) throws InterruptedException {
        System.out.println("═══════════ 📊 Status ═══════════");
        System.out.println("Classe:              " + ClassesRpg.formatado(player.classe));
        System.out.println("Vida:                " + Cores.VERDE          + player.vida + "/" + player.vidaMax + Cores.RESET);
        System.out.println("Dano base:           " + Cores.VERMELHO_FORTE + player.dano                        + Cores.RESET);
        System.out.println("Curas restantes:     " + Cores.AZUL           + player.quantidadeCura              + Cores.RESET);
        System.out.println("Dano total recebido: " + Cores.VERMELHO       + "-" + danoAcumulado                + Cores.RESET);
        System.out.println("═════════════════════════════════");
        Thread.sleep(1500);
    }

    // [GRÁFICOS > Arte ASCII] ────────────────────────────────────────────

    public static void imprimirTituloBloodDivine() {
        String[] linhas = {
            "██████╗ ██╗      ██████╗  ██████╗ ██████╗ ██╗██╗   ██╗██╗███╗   ██╗███████╗",
            "██╔══██╗██║     ██╔═══██╗██╔═══██╗██╔══██╗██║██║   ██║██║████╗  ██║██╔════╝",
            "██████╔╝██║     ██║   ██║██║   ██║██║  ██║██║██║   ██║██║██╔██╗ ██║█████╗  ",
            "██╔══██╗██║     ██║   ██║██║   ██║██║  ██║██║╚██╗ ██╔╝██║██║╚██╗██║██╔══╝  ",
            "██████╔╝███████╗╚██████╔╝╚██████╔╝██████╔╝██║ ╚████╔╝ ██║██║ ╚████║███████╗",
            "╚═════╝ ╚══════╝ ╚═════╝  ╚═════╝ ╚═════╝ ╚═╝  ╚═══╝  ╚═╝╚═╝  ╚═══╝╚══════╝"
        };
        int[][] coresRGB = {
            {255, 0, 0}, {220, 0, 20}, {190, 0, 40},
            {160, 0, 60}, {130, 0, 80}, {100, 0, 100}
        };
        for (int i = 0; i < linhas.length; i++) {
            System.out.println(
                "\u001B[38;2;" + coresRGB[i][0] + ";" + coresRGB[i][1] + ";" + coresRGB[i][2] + "m"
                + linhas[i]
            );
        }
        System.out.println("\u001B[0m");
    }

 public static void mostrarSala(int sala) throws InterruptedException {
        switch (sala) {
            case 1 -> {
                limparTela();
                System.out.println("* * * * * * * * * * * * * * * ");
                System.out.println("* * * * * * * * * * * * * * * ");
                for (int i = 0; i < 3; i++) {
                System.out.print("*" + "\u001B[31m" + " 1 " + "\u001B[0m" + "* * 2 * * 3 * * 4 * * 5 * ");
                
                Thread.sleep(500);
                for (int j = 0; j < 30; j++) {
                    System.out.print("\b");
                }
                System.out.print("*" + "\u001B[31m" + "  "+" " + "\u001B[0m" + "* * 2 * * 3 * * 4 * * 5 * ");
                
                Thread.sleep(500);

                for (int j = 0; j < 30; j++) {
                    System.out.print("\b");
                }
                }
                System.out.println();
                limparTela();
                System.out.println("Entrando na sala 1...");
                digitar("Os ossos da caverna começam a se juntar...              ");
                digitar("O inimigo é um " + Cores.VERMELHO + Inimigos.ESQUELETO + Cores.RESET + "!");
                Thread.sleep(1500);
                limparTelaDevagar();
            }
            case 2 -> {
                System.out.println("* * * * * * * * * * * * * * * ");
                System.out.println("* * * * * * * * * * * * * * * ");
                for (int i = 0; i < 3; i++) {
                System.out.print("* 1 * *" + "\u001B[31m" + "   "+"\u001B[0m"+"* * 3 * * 4 * * 5 * ");

                Thread.sleep(450);
                for (int j = 0; j < 30; j++) {
                    System.out.print("\b");
                }

                System.out.print("* 1 * *" + "\u001B[31m" + " 2 "+"\u001B[0m"+"* * 3 * * 4 * * 5 * ");

                Thread.sleep(450);
                for (int j = 0; j < 30; j++) {
                    System.out.print("\b");
                }
                }
                System.out.println();
                limparTela();
                System.out.println("Entrando na sala 2...");
                digitar("De uma silhueta escura se avista uma pele" + Cores.VERDE + " esverdeada..." + Cores.RESET);
                Thread.sleep(450);
                digitar("O inimigo é um " + Cores.VERMELHO + Inimigos.GOBLIN + Cores.RESET + "!");
                Thread.sleep(1500);
                limparTelaDevagar();
            }
            case 3 -> {
                limparTela();
                System.out.println("* * * * * * * * * * * * * * * ");
                System.out.println("* * * * * * * * * * * * * * * ");
                for (int i = 0; i < 4; i++) {
                System.out.print("* 1 * * 2 * *"+"\u001B[31m"+"   "+"\u001B[0m"+"* * 4 * * 5 * ");

                Thread.sleep(450);
                for (int j = 0; j < 30; j++) {
                    System.out.print("\b");
                }
                System.out.print("* 1 * * 2 * *"+"\u001B[31m"+" 3 "+"\u001B[0m"+"* * 4 * * 5 * ");

                Thread.sleep(450);
                for (int j = 0; j < 30; j++) {
                    System.out.print("\b");
                }

                }
                System.out.println();
                limparTela();
                System.out.println("Entrando na sala 3...");
                digitar("Do teto escuro uma criatura de "+ Cores.NEGRITO + "asas negras " + Cores.RESET + " desce em sua direção...");
                Thread.sleep(450);
                digitar("O inimigo é um " + Cores.VERMELHO + Inimigos.DEMONIO + Cores.RESET + "!");
                Thread.sleep(1500);
                limparTelaDevagar();
            }
            case 4 -> {
                limparTela();
                System.out.println("* * * * * * * * * * * * * * * ");
                System.out.println("* * * * * * * * * * * * * * * ");
                for (int i = 0; i < 4; i++) {
                System.out.print("* 1 * * 2 * * 3 * *"+"\u001B[31m"+"   "+"\u001B[0m"+"* * 5 * ");

                Thread.sleep(450);
                for (int j = 0; j < 30; j++) {
                    System.out.print("\b");
                }

                System.out.print("* 1 * * 2 * * 3 * *"+"\u001B[31m"+" 4 "+"\u001B[0m"+"* * 5 * ");

                Thread.sleep(450);
                for (int j = 0; j < 30; j++) {
                    System.out.print("\b");
                }
                }
                System.out.println();
                limparTela();
                System.out.println("Entrando na sala 4...");
                digitar("Ao centro de uma sala branca...");
                Thread.sleep(200);
                digitar("Uma figura imponente se ergue, vestida com uma" + Cores.NEGRITO + " armadura negra reluzente..." + Cores.RESET);
                Thread.sleep(450);
                digitar("O inimigo é o " + Cores.VERMELHO + Inimigos.CAVALEIRO_NEGRO + Cores.RESET + "!");
                Thread.sleep(1500);
                limparTelaDevagar();
            }
            case 5 -> {
                limparTela();
                System.out.println("* * * * * * * * * * * * * * * ");
                System.out.println("* * * * * * * * * * * * * * * ");
                for (int i = 0; i < 4; i++) {


                System.out.print("* 1 * * 2 * * 3 * * 4 * *"+"\u001B[31m"+"   "+"\u001B[0m"+"* ");

                Thread.sleep(450);
                
                for (int j = 0; j < 30; j++) {System.out.print("\b");}

                System.out.print("* 1 * * 2 * * 3 * * 4 * *"+"\u001B[31m"+" 5 "+"\u001B[0m"+"* ");
                
                Thread.sleep(450);
                for (int j = 0; j < 30; j++) {System.out.print("\b");}
                }
                System.out.println();
                limparTela();
                System.out.println("Entrando na sala 5...");
                digitar("No trono de um castelo sombrio, o " + Cores.VERMELHO + "Rei Demônio" + Cores.RESET + " aguarda sua chegada...");
                Thread.sleep(450);
                digitar("O inimigo é o " + Cores.VERMELHO + Inimigos.REI_DEMONIO + Cores.RESET + "!");
                Thread.sleep(450);
                digitar("A batalha final se aproxima... Prepare-se para o desafio supremo! ⚔️👑");
                Thread.sleep(2200);
                limparTelaDevagar();
            }
            default -> { System.out.println("ERRO! Número de sala inválido, apenas de 1 até 5");}
        }
        }
 }
