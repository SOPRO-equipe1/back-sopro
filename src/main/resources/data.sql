DROP TABLE IF EXISTS tb_conhecimento;

CREATE TABLE tb_conhecimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT NOT NULL,
    metadados VARCHAR(255)
);

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('O que é o SOPRO', 'O SOPRO é uma tecnologia assistiva voltada para pessoas com limitações severas de fala ou motoras (como mutismo). Ele utiliza um dispositivo wearable com sensores de pressão que captam comandos físicos através do sopro.', 'institucional, escopo, sobre, o que e, o que significa, projeto, wearable, dispositivo, aparelho, produto');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('O que é o SOPRO', 'O SOPRO é uma tecnologia assistiva voltada para pessoas com limitações severas de fala ou motoras (como mutismo). Ele utiliza um dispositivo wearable com sensores de pressão que captam comandos físicos através do sopro.', 'institucional, escopo, sobre, o que e, o que significa, projeto, wearable, dispositivo, aparelho, produto');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Como funciona a tecnologia', 'O dispositivo possui sensores conectados a um microcontrolador que mede a intensidade do sopro. Esses dados são processados localmente por um modelo de TinyML (Machine Learning embarcado) para identificar padrões e traduzi-los em voz sintetizada ou comandos no aplicativo.', 'tecnico, hardware, tinyml, funciona, como funciona, mecanismo, tecnologia, sensores, microcontrolador, processamento, inteligencia artificial, ia');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Público-alvo e Indicações', 'O sistema é indicado para pessoas com limitações na comunicação verbal que preservem a capacidade respiratória voluntária e controle dos músculos faciais para interagir com o bocal do sensor.', 'suporte, medico, publico, quem pode usar, indicacao, pacientes, pessoas, deficiencia, mutismo, paralisia, uso');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Preço e Comercialização', 'O projeto SOPRO está em fase de desenvolvimento e validação de protótipo para o DemoDay. Para informações sobre parcerias de pesquisa ou futuras aquisições comerciais, entre em contato com o nosso time através do site oficial.', 'comercial, financeiro, preco, valor, custo, comprar, adquirir, venda, comercializacao, quanto custa, investir, parceria');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Design e Ergonomia do Dispositivo', 'O dispositivo SOPRO possui um design moderno, minimalista e premium com acabamento em preto fosco para evitar marcas de dedo. Ele tem formato retangular alongado com cantos arredondados (sem quinas agressivas), medindo cerca de 15 cm de largura, 8 cm de comprimento e 3,2 cm de altura, sendo altamente ergonômico e portátil.', 'design, tamanho, dimensoes, visual, formato, aparencia, estetica, cor, ergonomia, peso, portatil');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Especificações de Circulação de Ar', 'O hardware do SOPRO conta com múltiplas aberturas retangulares de 1,2 cm alinhadas horizontalmente nas laterais. Elas servem para garantir a entrada e a circulação eficiente de ar necessária para o funcionamento preciso dos sensores de pressão durante o sopro.', 'fluxo de ar, tecnico, aberturas, hardware, lateral, buraco, ar, ventilacao, circulacao, sensor de pressao');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Interface Física e LEDs', 'A interface do aparelho é extremamente limpa e intuitiva, contendo um botão circular minimalista na extremidade frontal para comandos diretos e um pequeno LED indicador centralizado que informa os estados de carregamento, conexão e funcionamento do sistema.', 'botao, led, luz, ligar, interface, bateria, carregar, ligando, piscar, como liga, fisco, comandos');
