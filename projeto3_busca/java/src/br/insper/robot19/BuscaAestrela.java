package br.insper.robot19;

import java.util.*;


/**
 * Classe que implementa o algoritmo de busca
 * @author antonio
 *
 */
public class BuscaAEstrela {

    private Block start = null;
    private Block end = null;
    private GridMap map = null;

    private PriorityQueue<Node> border;

    /**
     * Construtor
     * @param map
     * @param start - ponto inicial
     * @param end - ponto final
     */
    public BuscaAEstrela(GridMap map, Block start, Block end) {
        this.map = map;
        this.start = start;
        this.end = end;
    }

    /**
     * Método que realiza a busca
     * @return
     */
    public Node buscar() {

        int heuristica = Math.abs(this.end.col - this.start.col)+Math.abs(this.end.row -this.start.row);
        Node root = new Node(start, null, null, 0, heuristica);

        Comparator<Node> comparator = new FunComparator();
        //Limpa a fronteira e insere o nó raiz
        border = new PriorityQueue<>(comparator);
        border.add(root);
        HashSet<Block> listablocos = new HashSet<Block>();

        while(!border.isEmpty()) {

            Node node = border.remove();
            Block atual = node.getValue();
            listablocos.add(atual);

            if(atual.row == end.row && atual.col == end.col) {
                return node;

            } else for(RobotAction acao : RobotAction.values()) {

                Block proximo = map.nextBlock(atual, acao);

                if(proximo != null && proximo.type != BlockType.WALL  && !listablocos.contains(proximo)) {
                    Node novoNode = new Node(proximo, node, acao, proximo.type.cost, Math.abs(this.end.col - proximo.col)+Math.abs(this.end.row - proximo.row));
                    border.add(novoNode);
                }
            }
        }
        return null;
    }

    /**
     * Resolve o problema com base em busca, realizando
     * o backtracking após chegar ao estado final
     *
     * @return A solução encontrada
     */
    public RobotAction[] resolver() {

        // Encontra a solução através da busca
        Node destino = buscar();
        if(destino == null) {
            return null;
        }

        //Faz o backtracking para recuperar o caminho percorrido
        Node atual = destino;
        Deque<RobotAction> caminho = new LinkedList<RobotAction>();
        while(atual.getAction() != null) {
            caminho.addFirst(atual.getAction());
            atual = atual.getParent();
        }
        return caminho.toArray(new RobotAction[caminho.size()]);
    }
}