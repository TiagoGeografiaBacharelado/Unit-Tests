package org.example;

import org.example.Produto;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class ProdutoTest {

    private Produto produto;

    @BeforeEach
    void setUp(){
        produto = new Produto("Camiseta", 50.0,10);
    }

    @Test
    void deveCriarProdutoComValoresValidos() {

        assertNotNull(produto);

        assertEquals("Camiseta", produto.getNome());

        assertEquals(50.0, produto.getPreco(), 0.001);

        assertEquals(10, produto.getEstoque());
    }

    @Test
    void deveCriarProdutoComPrecoNegativo(){
        Produto produtoPrecoNegativo = new Produto ("Camiseta", -50.0, 10);
        assertEquals(-50.0, produtoPrecoNegativo.getPreco(), 0.001);
    }

    @Test
    void deveCriarProdutoComEstoqueNegativo(){
        Produto produtoEstoqueNegativo = new Produto ("Camiseta", 50.0, -10);
        assertEquals(-10, produtoEstoqueNegativo.getEstoque());
    }

    @Test
    void deveAlterarNomeParaValorValido(){
        produto.setNome("Camiseta Premium");
        assertEquals("Camiseta Premium", produto.getNome());
    }

    @Test
    void deveAlterarPrecoParaValorValido() {
        produto.setPreco(59.99);

        assertEquals(59.99, produto.getPreco(), 0.001);
    }

    @Test
    void deveAlterarEstoqueParaValorPositivo(){
        produto.setEstoque(20);
        assertEquals(20, produto.getEstoque());
    }

    @Test
    void deveFalharAoAlterarPrecoParaValorNegativo(){
        produto.setPreco(-55);

        assertEquals(-55,produto.getPreco(), "ERRO: O preço não deveria aceitar valores negativos!");
    }

    @Test
    void deveAumentarEstoqueProdutoAposVenda(){

        produto.aumentarEstoque(5);


        assertEquals(15, produto.getEstoque(), "O estoque deveria ter aumentado para 15 após a reposição.");
    }

    @Test
    void deveDiminuirEstoqueAposVenda() {
        boolean vendaEfetuada = produto.diminuirEstoque(3);

        assertTrue(vendaEfetuada, "A venda deveria ter sido realizada com sucesso.");

        assertEquals(7, produto.getEstoque(), "O estoque deveria ter diminuído para 7 após a venda.");
    }

    @Test
    void naoDeveAlterarEstoqueQuandoVendaForMaiorQueDisponivel() {

        boolean vendaEfetuada = produto.diminuirEstoque(20);

        assertFalse(vendaEfetuada, "A venda não deveria ocorrer com estoque insuficiente.");

        assertEquals(10, produto.getEstoque(), "O estoque não deveria ter mudado após tentativa inválida.");
    }
}
