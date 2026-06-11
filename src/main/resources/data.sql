DROP TABLE IF EXISTS tb_conhecimento;

CREATE TABLE tb_conhecimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT NOT NULL,
    metadados VARCHAR(255)
);

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('O que é o SOPRO', 'O SOPRO é um projeto feito para ajudar no dia a dia de pessoas que têm muita dificuldade ou não conseguem falar e se movimentar. Criamos um aparelhinho leve e confortável que a pessoa pode usar no corpo, e ele consegue entender comandos e mensagens apenas com o sopro dela.', 'institucional, escopo, sobre, o que e, o que significa, projeto, wearable, dispositivo, aparelho, produto');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Como funciona a tecnologia', 'O nosso aparelhinho tem um medidor interno muito sensível que percebe a força do sopro da pessoa. Uma inteligência artificial muito moderna que fica guardada dentro do próprio aparelho estuda o ritmo desse sopro e transforma esse sopro em uma voz falada ou em comandos no aplicativo de celular.', 'tecnico, hardware, tinyml, funciona, como funciona, mecanismo, tecnologia, sensores, microcontrolador, processamento, inteligencia artificial, ia');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Público-alvo e Indicações', 'O sistema é indicado para qualquer pessoa que tenha dificuldades na fala, mas que consiga respirar por conta própria e consiga interagir ou encostar os lábios na pecinha de sopro do aparelho.', 'suporte, medico, publico, quem pode usar, indicacao, pacientes, pessoas, deficiencia, mutismo, paralisia, uso');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Preço e Comercialização', 'O projeto SOPRO está em fase de desenvolvimento e testes do nosso primeiro protótipo para o DemoDay. Se você tiver interesse em ajudar na pesquisa ou quiser saber sobre o futuro lançamento, você pode conversar com o nosso time por aqui mesmo.', 'comercial, financeiro, preco, valor, custo, comprar, adquirir, venda, comercializacao, quanto custa, investir, parceria');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Design e Ergonomia do Dispositivo', 'O aparelho do SOPRO tem um visual muito moderno, bonito e elegante na cor preta fosca, feito para não ficar manchado com marcas de dedos. Ele é bem leve, comprido e tem as pontas arredondadas e suaves para ser confortável de segurar ou usar. Ele mede cerca de 15 centímetros, sendo muito fácil de carregar para qualquer lugar.', 'design, tamanho, dimensoes, visual, formato, aparencia, estetica, cor, ergonomia, peso, portatil');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Especificações de Circulação de Ar', 'O aparelhinho do SOPRO tem pequenas aberturas e buraquinhos deitados nas laterais. Eles servem para deixar o ar entrar e sair de forma bem suave e eficiente, permitindo que o medidor interno sinta o seu sopro perfeitamente e sem esforço.', 'fluxo de ar, tecnico, aberturas, hardware, lateral, buraco, ar, ventilacao, circulacao, sensor de pressao');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Interface Física e LEDs', 'O visual do aparelho é muito simples e fácil de usar. Ele tem apenas um botão redondo na parte da frente para controle e uma luzinha bem pequena no centro que muda de cor ou pisca para avisar se ele está ligado, carregando a bateria ou funcionando direitinho.', 'botao, led, luz, ligar, interface, bateria, carregar, ligando, piscar, como liga, fisco, comandos');