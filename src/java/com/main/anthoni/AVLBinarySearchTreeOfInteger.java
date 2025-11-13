package com.anthoni;

import java.util.Queue;
import java.util.LinkedList;

public class AVLBinarySearchTreeOfInteger
{

    private class Node
    {
        public Node father;
        public Node left;
        public Node right;
        private Integer element;
        private Integer height;

        // Construtor da classe node
        public Node(Integer element)
        {
            this.father = null;
            this.left = null;
            this.right = null;
            this.element = element;
            this.height = 0;
        }

        // método da classe node que retorna a altura da subárvore da esquerda
        public int getLeftHeight()
        {
            if (left != null)
            {
                return left.height;
            }
            return -1;
        }

        // método da classe node que retorna a altura da subárvore da direita
        public int getRightHeight()
        {
            if (right != null)
            {
                return right.height;
            }
            return -1;
        }

        // método que atualiza a altura do nodo
        public void attHeight()
        {
            this.height = 1 + Math.max(getRightHeight(), getLeftHeight());
        }
    }


    // Atributos da classe AVL       
    private int count; // contagem do número de nodos
    private Node root; // referência para o nodo raiz

    // Construtor da classe AVL
    public AVLBinarySearchTreeOfInteger()
    {
        count = 0;
        root = null;
    }

    // limpa a Árvore
    public void clear()
    {
        count = 0;
        root = null;
    }

    // retorna true se a árvore for vazia
    public boolean isEmpty()
    {
        return (root == null);
    }

    // retorna a qtd de elementos da árvore
    public int size()
    {
        return count;
    }

    // Método da classe AVL que adiciona um elemento
    public void add(Integer element) {
        root = add(root, element, null);
        count++;
    }
    private Node add(Node n, Integer e, Node father) {
        if (n == null) { // insere
            Node aux = new Node(e);
            aux.father = father;
            return aux;
        }
        // Senao insere na subarvore da esq ou da dir
        if (e.compareTo(n.element) > 0) { // elemento a ser inserido é maior que o valor do nodo
            n.right = add(n.right, e, n); // vai para a direita
        }
        else {                            // vai para a esquerda
            n.left = add(n.left, e, n);
        }

        // AVL: ajusta alturas e faz o balanceamento
        n.attHeight();
        n = balancedNode(n);
        return n;
    }


    // Método para rotação para a esquerda
    private Node rotaEsq(Node n)
    {
        Node nRoot = n.right;
        Node nRight = nRoot.left; // Armazena os nodos

        nRoot.left = n;
        n.right = nRight; // Faz a rotação para a esquerda, balanceando os nodos

        // Atualiza pais
        nRoot.father = n.father;
        n.father = nRoot;
        if (nRight != null)
            nRight.father = n;

        // Atualiza ligação do pai anterior
        if (nRoot.father != null) {
            if (nRoot.father.left == n)
                nRoot.father.left = nRoot;
            else
                nRoot.father.right = nRoot;
        }

        n.attHeight();
        nRoot.attHeight(); // Atualiza as alturas dos nodos
        return nRoot; // Retorna os valores
    }

    // método para rotação para a direita
    private Node rotaDir(Node n)
    {
        Node nRoot = n.left;
        Node nLeft = nRoot.right; // Armazena os nodos

        nRoot.right = n;
        n.left = nLeft; // Faz a rotação para a esquerda, balanceando os nodos

        // Atualiza pais
        nRoot.father = n.father;
        n.father = nRoot;
        if (nLeft != null)
            nLeft.father = n;

        // Atualiza ligação do pai anterior
        if (nRoot.father != null) {
            if (nRoot.father.left == n)
                nRoot.father.left = nRoot;
            else
                nRoot.father.right = nRoot;
        }

        n.attHeight();
        nRoot.attHeight(); // Atualiza as alturas dos nodos
        return nRoot; // Retorna os valores
    }

    // método de balanceamento da árvore
    private Node balancedNode(Node n)
    {
        if (n == null) // verifica se a árvore está vazia
        {
            return n;
        }
        n.attHeight(); // define a altura do Nodo a ser inserido

        int balancedNode = n.getLeftHeight() - n.getRightHeight(); // verifica a altura do nodo

        if (balancedNode < -1) // caso esteja desbalanceado para direita
        {
            if (n.right.getRightHeight() >= n.right.getLeftHeight()) // verifica se é um caso de rotação dupla
            {
                n = rotaEsq(n); // se não for
            }
            else
            {
                n.right = rotaDir(n.right); // se for, faz duas rotações
                n = rotaEsq(n);
            }
        }
        else if (balancedNode > 1) // caso esteja desbalanceado para a esquerda
        {
            if (n.left.getLeftHeight() >= n.left.getRightHeight()) // verifica se é um caso de rotação dupla
            {
                n = rotaDir(n); // se não for
            }
            else
            {
                n.left = rotaEsq(n.left); // se for, faz duas rotações
                n = rotaDir(n);
            }
        }
        return n;
    }

    // Método para cálcular a altura
    public int height()
    {
        return heightAux(root) -1;
    }

    // Método auxiliar para calcular a altura
    private int heightAux(Node n)
    {
        if (n == null)
        {
            return 0;
        }

        Integer dirAlt = heightAux(n.right);
        Integer esqAlt = heightAux(n.left);

        return 1 + Math.max(esqAlt, dirAlt); // soma as alturas de cada subárvore
    }

    // verifica se o elemento existe ou retorna null se o elemento não existe
    public boolean contains(Integer element)
    {
        Node aux = searchNodeRef(element, root);
        return (aux != null);
    }

    public boolean remove(Integer element) {
        // Se arvore vazia ou elemento nulo
        if (element == null || root == null)
            return false;

        Node aux = searchNodeRef(element, root);
        if (aux == null) { // se não encontrou o elemento
            return false;
        }

        remove(aux);   // chama método que faz a remoção física
        count--;       // atualiza o count
        return true;   // sucesso
    }

    private void remove(Node n) {
        Node father = n.father;

        // Caso 1: nodo folha
        if (n.left == null && n.right == null) {
            if (root == n) {
                root = null;
                return;
            }
            if (father.left == n)
                father.left = null;
            else
                father.right = null;
        }

        // Caso 2: tem apenas filho à direita
        else if (n.left == null && n.right != null) {
            if (root == n) {
                root = root.right;
                root.father = null;
                return;
            }
            if (father.left == n)
                father.left = n.right;
            else
                father.right = n.right;
            n.right.father = father;
        }

        // Caso 3: tem apenas filho à esquerda
        else if (n.left != null && n.right == null) {
            if (root == n) {
                root = root.left;
                root.father = null;
                return;
            }
            if (father.left == n)
                father.left = n.left;
            else
                father.right = n.left;
            n.left.father = father;
        }

        // Caso 4: dois filhos
        else {
            Node menor = smallest(n.right);
            n.element = menor.element;
            remove(menor);
            return; // o rebalanceamento será feito pela chamada recursiva
        }

        // 🔹 Após remover o nó, atualiza alturas e reequilibra subindo até a raiz
        rebalanceUp(father);
    }

    // Método que percorre subindo e reequilibra cada nó
    private void rebalanceUp(Node n) {
        while (n != null) {
            n.attHeight();
            n = balancedNode(n);
            if (n.father == null)
                root = n; // se virou raiz
            n = n.father;
        }
    }

    // IMPLEMENTE ESTE MÉTODO
    // retorna o elemento da raíz
    public Integer getRoot()
    {
        return null;
    }

    // IMPLEMENTE ESTE MÉTODO
    // retorna o pai do elemento
    public Integer getParent(Integer element)
    {
        return null;
    }

    // retorna o maior valor da árvore
    public Integer getBiggest()
    {
        if (root == null)
        {
            return null;
        }
        Node aux = root;
        while (aux.right != null)
        {
            aux = aux.right;
        }
        return aux.element;
    }
    /**
     * Retorna o menor elemento da arvore.
     * @return o menor elemento
     */
    public Integer getSmallest() {
        Node n = smallest(root);
        if (n==null)
            return null;
        else
            return n.element;
    }
    private Node smallest(Node n) {
        if (n == null)
            return null;
        while (n.left != null) {
            n = n.left;
        }
        return n;
    }

    // retorna o elemento armazenado na esquerda do nodo com o elemento passado como parâmetro
    public Integer getLeft(Integer element) {
        Node aux = searchNodeRef(element,root);
        if (aux == null) // se nao achou element
            return null;
        if (aux.left == null) // se nao tem filho a esq
            return null;
        return aux.left.element;

    }
    // IMPLEMENTE ESTE MÉTODO
    // retorna o elemento armazenado na direita do nodo com o elemento passado como parâmetro
    public Integer getRight(Integer element) {
        return null;
    }

    // verifica se o nodo que armazena o elemento é um nodo folha
    public boolean isExternal(int element) {
        // verifica se a raiz é nula
        if (root == null)
        {
            return false;
        }
        // percorre a árvore
        Node aux = searchNodeRef(element, root);
        // verifica se achou o elemento
        if (aux == null)
        {
            return false;
        }
        // verifica se é folha
        if (aux.right == null && aux.left == null)
        {
            return true;
        }
        return false;
    }

    // IMPLEMENTE ESTE MÉTODO
    // verifica se o nodo que armazena o elemento é um nodo interno
    public boolean isInternal(int element) {
        return false;
    }

    // Retorna os elementos da árvore - caminhamento pré-fixado
    public LinkedListOfInteger positionsPre()
    {
        LinkedListOfInteger res = new LinkedListOfInteger();
        positionsPreAux(root, res);
        return res;
    }

    // Método auxiliar do caminhamento pré-fixado
    private void positionsPreAux(Node n, LinkedListOfInteger res)
    {
        if (n != null)
        {
            res.add(n.element); //Visita o nodo
            positionsPreAux(n.left, res); //Visita a subárvore da esquerda
            positionsPreAux(n.right, res); //Visita a subárvore da direita
        }
    }

    // Retorna os elementos da árvore - caminhamento pós-fixado
    public LinkedListOfInteger positionsPos()
    {
        LinkedListOfInteger res = new LinkedListOfInteger();
        positionsPosAux(root, res);
        return res;
    }

    // IMPLEMENTE ESTE MÉTODO
    // Método auxiliar do caminhamento pós-fixado
    private void positionsPosAux(Node n, LinkedListOfInteger res)
    {

    }

    // Retorna os elementos da árvore - caminhamento central
    public LinkedListOfInteger positionsCentral()
    {
        LinkedListOfInteger res = new LinkedListOfInteger();
        positionsCentralAux(root, res);
        return res;
    }

    // IMPLEMENTE ESTE MÉTODO
    // Método auxiliar do caminhamento central
    private void positionsCentralAux(Node n, LinkedListOfInteger res)
    {

    }

    // Retorna os elementos da árvore - caminhamento width
    public LinkedListOfInteger positionsWidth()
    {
        Queue<Node> fila = new LinkedList<>();
        Node atual = null;
        LinkedListOfInteger res = new LinkedListOfInteger();
        if (root != null) {
            fila.add(root);
            while (!fila.isEmpty()) {
                atual = fila.poll();
                if (atual.left != null) {
                    fila.add(atual.left);
                }
                if (atual.right != null) {
                    fila.add(atual.right);
                }
                res.add(atual.element);
            }
        }
        return res;
    }

    // pesquisa a referência (Node) do elemento pesquisado
    private Node searchNodeRef(Integer element, Node target) {
        if(element==null || target==null)
            return null;

        // Visita a raiz
        int c = element.compareTo(target.element);
        if (c == 0) // se encontrou
            return target;
        else if (c>0)
            return searchNodeRef(element, target.right);
        else
            return searchNodeRef(element, target.left);
    }

    // Método para gerar o DOT
    private void GeraConexoesDOT(Node nodo) {
        if (nodo == null) {
            return;
        }

        GeraConexoesDOT(nodo.left);
        //   "nodeA":esq -> "nodeB" [color="0.650 0.700 0.700"]
        if (nodo.left != null) {
            System.out.println("\"node" + nodo.element + "\":esq -> \"node" + nodo.left.element + "\" " + "\n");
        }

        GeraConexoesDOT(nodo.right);
        //   "nodeA":dir -> "nodeB";
        if (nodo.right != null) {
            System.out.println("\"node" + nodo.element + "\":dir -> \"node" + nodo.right.element + "\" " + "\n");
        }
        //"[label = " << nodo->hDir << "]" <<endl;
    }

    private void GeraNodosDOT(Node nodo) {
        if (nodo == null) {
            return;
        }
        GeraNodosDOT(nodo.left);
        //node10[label = "<esq> | 10 | <dir> "];
        System.out.println("node" + nodo.element + "[label = \"<esq> | " + nodo.element + " | <dir> \"]" + "\n");
        GeraNodosDOT(nodo.right);
    }

    public void GeraConexoesDOT() {
        GeraConexoesDOT(root);
    }

    public void GeraNodosDOT() {
        GeraNodosDOT(root);
    }

    // Gera uma saida no formato DOT
    // Esta saida pode ser visualizada no GraphViz
    // Versoes online do GraphViz pode ser encontradas em
    // http://www.webgraphviz.com/
    // http://viz-js.com/

    public void GeraDOT() {
        System.out.println("digraph g { \nnode [shape = record,height=.1];\n" + "\n");

        GeraNodosDOT();
        System.out.println("");
        GeraConexoesDOT(root);
        System.out.println("}" + "\n");
    }
}