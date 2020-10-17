package br.com.matheusfreitas.sincronizacaoreceita.processor;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

import br.com.matheus.freitas.sincronizacaoreceita.models.Conta;


@SpringBootTest
@Import(Processor.class)
public class ProcessorTests {
	
	@Autowired
	private Processor processor;
	

	@Test
	public void testConverterCSVEmContas() {
		String[] csv = {"0101","12225-6","100,00","A"};
		Conta conta = processor.converteCSVEmContas(csv);
		assertTrue(conta != null);
		assertThat(conta.getAgencia() == "0101");
		assertThat(conta.getConta() == "12225-6");
		assertThat(conta.getSaldo() == 100.00);
		assertThat(conta.getStatus() == "A");
	}
	
	@Test
	public void testConverterContaEmCSV() {
		Conta conta = new Conta("0101", "12225-6", Double.parseDouble("100,00".replace(",", ".")), "A");
		String[] csv = processor.converteContaEmCSV(conta);
		assertTrue(csv.length > 0);
		assertThat(csv[0] == "0101");
		assertThat(csv[1] == "12225-6");
		assertThat(csv[2] == "100.00");
		assertThat(csv[3] == "A");
	}
	
	@Test
	public void testProcessamentoDoArquivo() {
		Conta conta = new Conta("0101", "12225-6", Double.parseDouble("100,00".replace(",", ".")), "A");
		String[] csv = processor.converteContaEmCSV(conta);
		assertTrue(csv.length > 0);
	}
	
	@Test
	public void testGerarRelatorioDeProcessamentoContas() {
		Boolean resposta = processor.gerarRelatorioProcessamentoContas("../sincronizacaoreceita/src/test/java/br/com/matheusfreitas/sincronizacaoreceita/processor/CSV.csv");
		assertTrue(resposta);
		
	}
	
}
