package br.com.matheusfreitas.sincronizacaoreceita.processor;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;
import com.opencsv.CSVWriter;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;
import br.com.matheus.freitas.sincronizacaoreceita.models.Conta;
import br.com.matheusfreitas.sincronizacaoreceita.services.ReceitaService;

public class Processor {
	
	List<Conta> processarArquivo(String arquivo) throws IOException {
		Reader reader = Files.newBufferedReader(Paths.get(arquivo));
        CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
        List<String[]> contas = csvReader.readAll();
        List<Conta> listaContas = contas.parallelStream()
        		.map(csv -> converteCSVEmContas(csv))
        		.collect(Collectors.toList());
        return listaContas;
	}
	
	Conta converteCSVEmContas(String[] csv) {
		return new Conta(csv[0], csv[1].replace("-", ""), Double.parseDouble(csv[2].replace(",", ".")), csv[3]);
	}
	
	String[] converteContaEmCSV(Conta conta) {
		try {
			 Boolean statusProcessamento = new ReceitaService().atualizarConta(conta.getAgencia(), conta.getConta(), conta.getSaldo(), conta.getStatus());
			return new String[]{conta.getAgencia(), conta.getConta(), Double.toString(conta.getSaldo()).replace(".", ","), conta.getStatus(), statusProcessamento.toString()};
		} catch (RuntimeException | InterruptedException e) {
			throw new RuntimeException();
		}
	}
	
	public Boolean gerarRelatorioProcessamentoContas(String arquivo) {
		List<Conta> listaContasProcessadas;
		try {
			String[] cabecalho = {"agencia", "conta", "saldo", "status", "statusProcessamento"};
			listaContasProcessadas = this.processarArquivo(arquivo);
			
	        List<String[]> linhas = listaContasProcessadas.parallelStream()
	        		.map(conta -> converteContaEmCSV(conta))
	        		.collect(Collectors.toList());

	        Writer writer = Files.newBufferedWriter(Paths.get(arquivo));
	        
	        @SuppressWarnings("resource")
			CSVWriter csvWriter = new CSVWriter(writer);

	        csvWriter.writeNext(cabecalho);
	        csvWriter.writeAll(linhas);

	        csvWriter.flush();
	        writer.close();
	        return true;
		} catch (IOException e) {
			throw new RuntimeException();
		}
	}
}
