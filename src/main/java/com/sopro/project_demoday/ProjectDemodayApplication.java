package com.sopro.project_demoday;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

@SpringBootApplication
public class ProjectDemodayApplication {

	public static void main(String[] args) {

		carregarVariaveisAmbiente();
		SpringApplication.run(ProjectDemodayApplication.class, args);
	}

	private static void carregarVariaveisAmbiente() {
		try {
			if (Files.exists(Paths.get(".env"))) {
				List<String> linhas = Files.readAllLines(Paths.get(".env"));
				for (String linha : linhas) {
					linha = linha.trim();
					// Ignora linhas totalmente vazias, comentários e garante que possui o caractere '='
					if (!linha.isEmpty() && !linha.startsWith("#") && linha.contains("=")) {
						String[] partes = linha.split("=", 2);
						String chave = partes[0].trim();
						String valor = partes[1].trim();
						System.setProperty(chave, valor);
					}
				}
			}
		} catch (IOException e) {
			System.err.println("Não foi possível ler o arquivo .env: " + e.getMessage());
		}
	}
}
