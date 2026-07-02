DROP TABLE IF EXISTS tb_conhecimento;

CREATE TABLE tb_conhecimento (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(255) NOT NULL,
    conteudo TEXT NOT NULL,
    metadados VARCHAR(255)
);

-- ====== SEÇÃO 1: INSTITUCIONAL, ESCOPO E PERSONA ======

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('O que é o SOPRO', 'O SOPRO é um projeto feito para ajudar no dia a dia de pessoas que têm muita dificuldade ou não conseguem falar e se movimentar. Criamos um aparelhinho leve e confortável que a pessoa pode usar no corpo, e ele consegue entender comandos e mensagens apenas com o sopro dela.', 'institucional, escopo, sobre, o que e, o que significa, projeto, wearable, dispositivo, aparelho, produto');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Quem é José Clemente', 'José Clemente é o avô de Iasmin Lopes (desenvolvedora da equipe). Ele sofreu um AVC que provocou a Afasia, retirando sua autonomia e comunicação verbal. Ele é a principal inspiração da Sopro, motivando o desenvolvimento do dispositivo para ajudar milhões de pessoas em situações semelhantes.', 'persona, jose clemente, avo, iasmin lopes, inspiracao, historia, avc, afasia, verbal, autonomia');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Missão Visão e Valores', 'A nossa missão é transformar o ato biológico do sopro em uma ferramenta de comunicação e autonomia para quem perdeu a fala. Nossa visão é ser a ponte tecnológica que minimiza o isolamento social. Nossos valores são baseados em Inovação, Empatia e Integridade ética.', 'missao, visao, valores, institucional, empatia, integridade, inovação, isolamento social, ética');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Alinhamento com as ODS', 'O projeto SOPRO atua diretamente alinhado com as Metas de Desenvolvimento Sustentável da Agenda 2030 da ONU, focando em três pilares principais: ODS 3 (Saúde e Bem-Estar), ODS 8 (Trabalho Digno e Crescimento Econômico) e ODS 9 (Indústria, Inovação e Infraestrutura).', 'ods, metas, sustentavel, onu, agenda 2030, brasil, saude, bem-estar, industria, inovacao, trabalho digno');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Apoio e Origem PROA', 'A Sopro surgiu através da união de 8 jovens desenvolvedores e foi impulsionada pela capacitação do Instituto PROA e do SENAC. Expressamos profunda gratidão a essas instituições que forneceram o suporte, conhecimento e infraestrutura cruciais para a concretização desta iniciativa.', 'proa, instituto proa, senac, equipe, criadores, fundadores, agradecimento, suporte, capacitação, jovens');


-- ====== SEÇÃO 2: DADOS MÉDICOS E PÚBLICO-ALVO ======

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('O que é Afasia', 'A afasia é um distúrbio de linguagem adquirido que afeta a capacidade de se expressar, compreender, ler ou escrever, comumente surgindo após lesões cerebrais como o AVC. Apesar de comprometer a comunicação verbal, ela preserva a capacidade cognitiva do indivíduo.', 'afasia, disturbio, linguagem, comunicacao, avc, cerebro, lesao, cognitivo, compreensao, fala');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Dados e Impacto da Afasia', 'Segundo dados publicados em 2025 pelo National Institute on Deafness, a afasia afeta mais de 2 milhões de pessoas mundialmente, registrando mais de 180 mil novos casos diagnosticados todos os anos. O tratamento médico e fonoaudiológico possibilita a recuperação parcial ou total.', 'dados, estatisticas, mercado, populacao, milhoes, estudos, cura, tratamento, casos por ano, national institute');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Público-alvo e Indicações', 'O sistema é indicado para qualquer pessoa que tenha dificuldades na fala, mas que consiga respirar por conta própria e consiga interagir ou encostar os lábios na pecinha de sopro do aparelho. Útil em diagnósticos de Afasia, ELA (Esclerose Lateral Amiotrófica), traqueostomizados e paralisias.', 'suporte, medico, publico, quem pode usar, indicacao, pacientes, pessoas, deficiencia, mutismo, paralisia, uso, ela, traqueostomia');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Validação e Respaldo Médico', 'O projeto possui forte respaldo prático. Realizamos uma pesquisa de campo coletando cinquenta e sete respostas diretamente de atuantes da área da saúde e pacientes, obtendo expressivos números de validações e feedbacks que confirmam a viabilidade clínica do dispositivo.', 'pesquisa de campo, validacao, respaldo, medico, saude, medicos, enfermeiros, profissionais, clinico, feedbacks');



-- ====== SEÇÃO 3: HARDWARE E TECNOLOGIA ======


INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES ('Como funciona a tecnologia', 'O nosso aparelho tem um medidor interno muito sensível que percebe a força do sopro da pessoa. Uma inteligência artificial traduz essa pressão de ar em comandos verbais sintetizados.', 'hardware, tecnologia, como funciona, sensor, medidor, sopro, funcionamento');

-- ====== SEÇÃO 4: PLANOS E ASSINATURAS ======

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Plano Essencial', 'O plano essencial oferece o recurso básico para uso do dispositivo. Inclui um filtro inteligente que identifica o sopro sem interferência de ruídos externos, calibração de sensibilidade para entender a força do sopro e relatórios de uso para acompanhamento terapêutico.', 'planos, assinatura, essencial, basico, filtro, sensibilidade, relatorio, quanto custa');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Plano Plus', 'O plano plus é focado em ampliar a autonomia social e a comunicação no dia a dia. Inclui todos os recursos do plano básico e traz duas grandes vantagens: uma inteligência artificial que aprende o vocabulário do usuário e integração com o WhatsApp e redes sociais para enviar mensagens por meio de sopros.', 'planos, assinatura, plus, whatsapp, redes sociais, internet, mensagem');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Plano Pro', 'O plano pro é voltado para saúde, segurança e acompanhamento médico à distância. Engloba todos os recursos dos planos anteriores e adiciona a opção de vozes personalizadas, uma IA preditiva para detecção de fadiga e prevenção de crises, e um portal de telemetria para monitoramento clínico por terapeutas e médicos em tempo real.', 'planos, assinatura, pro, profissional, telemetria, voz personalizada, fadiga, medico, saude, clinico');

INSERT INTO tb_conhecimento (titulo, conteudo, metadados) VALUES
('Como funciona a integracao com redes sociais', 'Após conectar o dispositivo a uma rede Wi-Fi, o usuário consegue enviar mensagens e interagir no WhatsApp e outras redes sociais utilizando apenas os padrões de seus sopros, promovendo autonomia também no ambiente digital.', 'whatsapp, redes sociais, wi-fi, internet, mensagem, integracao');