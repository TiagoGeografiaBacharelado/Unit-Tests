package org.example;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VendaTest {

    private Produto produto;

    @BeforeEach
    void setUp() {

        produto = new Produto("Camiseta", 50.0, 10);
    }

    @Test
    public void deveVenderQuantidadeMenorEstoqueDisponivel() {
        Venda venda = new Venda(produto, 3);
        boolean sucesso = venda.realizarVenda();

        assertTrue(sucesso, "A venda deve retornar true quando há estoque suficiente.");

        double expectedTotal = produto.getPreco() * 3;
        assertEquals(expectedTotal, venda.getTotalVenda(), 0.0001,
                "O total da venda deve ser preço * quantidade.");

        int estoqueEsperado = 10 - 3;
        assertEquals(estoqueEsperado, produto.getEstoque(),
                "O estoque do produto deve diminuir pela quantidade vendida.");
    }

    @Test
    public void deveVenderQuantidadeIgualAoEstoqueDisponivel() {
        Venda venda = new Venda(produto, 10);
        boolean sucesso = venda.realizarVenda();

        assertTrue(sucesso, "Venda com quantidade igual ao estoque deve ser bem-sucedida.");
        assertEquals(50.0 * 10, venda.getTotalVenda(), 0.0001, "Total deve ser preço * quantidade");
        assertEquals(0, produto.getEstoque(), "Estoque deve ficar em 0 após venda total");
    }

    @Test
    public void naoDeveVenderQuantidadeMaiorQueEstoque() {
        Venda venda = new Venda(produto, 15);
        boolean sucesso = venda.realizarVenda();

        assertFalse(sucesso, "A venda deve retornar false quando a quantidade é maior que o estoque.");
        assertEquals(10, venda.getProduto().getEstoque(), "Estoque não deve ser alterado quando a venda falha.");
        assertEquals(0.0, venda.getTotalVenda(), 0.0001, "Total da venda deve permanecer 0 em venda falha.");
    }

    @Test
    public void deveCalcularTotalDaVendaCorretamente() {
        Venda venda = new Venda(produto, 2);
        boolean sucesso = venda.realizarVenda();

        assertTrue(sucesso, "Venda deve ter sucesso");
        assertEquals(100.0, venda.getTotalVenda(), 0.0001, "50 * 2 = 100");
    }

    @Test
    public void deveVenderComProdutoNuloDeveFalhar() {
        Venda venda = new Venda(null, 1);

        assertThrows(NullPointerException.class, () -> {
            venda.realizarVenda();
        }, "Realizar venda com produto nulo deve lançar NullPointerException (comportamento atual).");
    }

    @Test
    public void deveCriarVendaComQuantidadeNegativa() {
        Venda venda = new Venda(produto, -5);

        boolean sucesso = venda.realizarVenda();

        assertTrue(sucesso, "Comportamento atual: aceita quantidade negativa e retorna true");
        assertEquals(50.0 * -5, venda.getTotalVenda(), 0.0001, "Total será negativo (preco * quantidade negativa)");
        assertEquals(15, produto.getEstoque(), "Estoque inicial 10 -> 10 - (-5) = 15");
    }


    @Test
    public void deveCriarVendasMultipasCompartilhamEstoqueDoMesmoProduto() {

        Venda v1 = new Venda(produto, 4);
        Venda v2 = new Venda(produto, 3);

        assertTrue(v1.realizarVenda(), "Primeira venda deve ter sucesso");
        assertEquals(6, produto.getEstoque(), "Estoque após primeira venda deve ser 6");

        assertTrue(v2.realizarVenda(), "Segunda venda deve ter sucesso");
        assertEquals(3, produto.getEstoque(), "Estoque após segunda venda deve ser 3");
    }

    @Test
    public void deveCalcularTotalRefleteMudancaDePrecoAntesDaVenda() {
        produto.setPreco(60.0);
        Venda venda = new Venda(produto, 2);
        assertTrue(venda.realizarVenda(), "Venda com preço alterado deve ter sucesso");
        assertEquals(60.0 * 2, venda.getTotalVenda(), 0.0001,
                "Total deve considerar o preço atualizado antes da venda");
    }

    @Test
    public void deveVenderFalhaQuandoEstoqueInicialZero() {
        Produto pZero = new Produto("Boné", 20.0, 0);
        Venda venda = new Venda(pZero, 1);
        boolean sucesso = venda.realizarVenda();

        assertFalse(sucesso, "Venda deve falhar quando estoque inicial é 0");
        assertEquals(0, pZero.getEstoque(), "Estoque deve permanecer 0");
    }

    @Test
    public void deveReposicaoPermiteVendaPosterior() {
        Produto p = new Produto("Meia", 10.0, 0);
        Venda tentativa = new Venda(p, 2);
        assertFalse(tentativa.realizarVenda(), "Tentativa sem estoque deve falhar");


        p.aumentarEstoque(5);
        Venda vendaAposReposicao = new Venda(p, 3);
        boolean sucesso = vendaAposReposicao.realizarVenda();

        assertTrue(sucesso, "Venda após reposição deve ser bem-sucedida");
        assertEquals(2, p.getEstoque(), "Estoque final deve refletir a venda (5 - 3 = 2)");
    }
}
