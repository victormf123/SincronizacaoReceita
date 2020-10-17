package br.com.matheusfreitas.sincronizacaoreceita.services;

import static org.junit.Assert.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import br.com.matheus.freitas.sincronizacaoreceita.models.Conta;


@SpringBootTest
@Import(ReceitaService.class)
public class ReceitaServiceTests {
	
	@Autowired
	ReceitaService receitaService;
	
	@Test
	public void testAtualizarConta() {
		try {
			Conta conta = new Conta("0101", "12225-6".replace("-", ""), Double.parseDouble("100,00".replace(",", ".")), "A");
			Boolean resposta = receitaService.atualizarConta(conta.getAgencia(), conta.getConta(), conta.getSaldo(), conta.getStatus());
			assertTrue(resposta);
			
		} catch (RuntimeException e) {
			throw new RuntimeException();
		} catch (InterruptedException e) {
			throw new RuntimeException();
		}
	}
}
