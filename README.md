<div align= "center">


<img src="https://github.com/SOPRO-equipe1/.github/blob/main/profile/logo.png" alt="SOPRO Logo" width="250">
  
#  API  🌬️

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)


A **Sopro** é uma solução de tecnologia assistiva focada em devolver a autonomia de comunicação a indivíduos com mutismo ou limitações motoras severas. Esta API é o motor que gere a inteligência, a segurança e os dados por trás da plataforma, permitindo a conversão de inputs físicos em voz sintetizada e a gestão de perfis de utilizador.

```Se quiser ver uma documentação mais detalhada sobre a arquitetura:```

[![GitBook](https://img.shields.io/badge/GITBOOK-000?style=for-the-badge&logoColor=FFF&color=000)](https://app.gitbook.com/o/3BzJD9kc8XUB2pCNxAEC/s/jGShUQkZLDYVFAp0pbGV/)



## 📌 Funcionalidades principais

 **Gestão de utilizadores:** Registo e autenticação segura de perfis.
 **Controle de assinaturas:** Monitorização de acesso e validade dos planos dos utilizadores.
 **Base de conhecimento:** Repositório de metadados e conteúdos para suporte à comunicação assistida.
 **Segurança robusta:** Implementação de Spring Security com suporte a OAuth2 Authorization Server.

## 🏗️ Arquitetura do sistema

O projeto segue o padrão **MVC** (Model-View-Controller) para garantir a separação de responsabilidades e a escalabilidade do sistema.


```mermaid

graph TD
    %% Definição de estilos avançados
    classDef azul fill:#1A53FF,stroke:#FAFCFF,color:#FAFCFF,stroke-width:3px,font-weight:bold;
    classDef roxo fill:#9333EA,stroke:#FAFCFF,color:#FAFCFF,stroke-width:3px,font-weight:bold;
    classDef verde fill:#30BD30,stroke:#1D252A,color:#1D252A,stroke-width:3px,font-weight:bold;
    classDef laranja fill:#F97316,stroke:#1D252A,color:#1D252A,stroke-width:3px,font-weight:bold;
    classDef preto fill:#1D252A,stroke:#FAFCFF,color:#FAFCFF,stroke-width:3px,font-weight:bold;
    classDef branco fill:#FAFCFF,stroke:#1D252A,color:#1D252A,stroke-width:3px,font-weight:bold;

    %% Camada cliente
    subgraph Camada_Cliente ["Camada Cliente"]
        A[React Frontend / App Mobile]:::azul
    end

    %% Segurança & Infraestrutura
    subgraph Camada_Seguranca ["Segurança & Infraestrutura"]
        B[JwtAuthenticationFilter]:::roxo
        C[SecurityConfig / Endpoints]:::roxo
    end

    %% Controllers
    subgraph Camada_Controllers ["Controllers (MVC - REST)"]
        D[AuthController]:::verde
        E[UsuarioController]:::verde
        F[PerfilController]:::verde
        G[AssinaturaController]:::verde
        H[PreferenciasController]:::verde
        I[ConhecimentoController]:::verde
    end

    %% Services
    subgraph Camada_Services ["Services (Regras de Negócio)"]
        J[AuthService]:::laranja
        K[UsuarioService]:::laranja
        L[PerfilService]:::laranja
        M[AssinaturaService]:::laranja
        N[PreferenciasService]:::laranja
        O[ConhecimentoService]:::laranja
    end

    %% Repositories
    subgraph Camada_Repositories ["Repositories (Data Access)"]
        P[UsuarioRepository]:::preto
        Q[PedidoRepository]:::preto
        R[AssinaturaRepository]:::preto
        S[PagamentoRepository]:::preto
        T[PreferenciasAcessibilidadeRepository]:::preto
        U[ConhecimentoRepository]:::preto
    end

    %% Persistência & Motores externos
    subgraph Camada_Externos ["Persistência & Motores Externos"]
        V[(MySQL / PostgreSQL)]:::branco
        W[Google Gemini API]:::branco
    end

    %% Fluxo de conexões e requisições
    A -->|1. Requisição HTTP + JWT| B
    B -->|2. Valida Token| C
    C -->|3. Encaminha Requisição| D & E & F & G & H & I
    
    %% Controller -> Service
    D --> J
    E --> K
    F --> L
    G --> M
    H --> N
    I --> O

    %% Service -> Repository / API
    J --> P
    K --> P & T
    L --> P & Q
    M --> P & R & S
    N --> P & T
    O -->|Busca dados para o RAG| U
    O -->|Injeta Contexto / Prompt| W

    %% Repository -> Banco de Dados
    P & Q & R & S & T & U --> V

```
  
> <details>
> <summary>Fluxo Cloud</summary>
>   
> ![img](docs/driagrams/Azure.jpg)
> 
> </details>


## DER

![DER](docs/driagrams/modelo-logico.png)

## 🛠️ Stack tecnológica

 **Linguagem:** Java 21.
 **Framework:** Spring Boot 4.0.6.
 **Persistência:** Spring Data JPA.
 **Banco de Dados:** MySQL.
 **Segurança:** Spring Security & OAuth2.



 ## 🌐 Infraestrutura e hospedagem (Azure App Services)

O ambiente de produção do backend do projeto Sopro utiliza o **Azure App Services** para hospedagem da API em Spring Boot. Durante os ciclos de teste (Sprint 3), foram documentados comportamentos específicos da infraestrutura que impactam diretamente os indicadores de performance do sistema.

### Análise de latência e "Cold Start"

Identificou-se um comportamento de **Cold Start (Inicialização a Frio)** nas janelas de inatividade da aplicação. Como a API foi construída utilizando a stack Java 21+ Spring Boot, o processo de inicialização do container e carregamento do contexto do Spring exige um consumo inicial considerável de CPU e memória.

 ```Evidência: Em análises de telemetria, registrou-se um pico onde apenas **2 requisições** resultaram em um *Response Time (Max)* de **1.18 minutos** após um período de ociosidade do servidor.```
 
``` Causa raiz: O plano de serviço atual (camada gratuita/básica) coloca a aplicação em estado de hibernação (*idling*) quando não há tráfego constante. A primeira requisição subsequente precisa acordar a instância, resultando na latência observada. ```
 
 ```Comportamento normalizado:** Após a inicialização inicial (aquecimento do ambiente), o tempo de resposta estabiliza-se na casa dos milissegundos para as requisições seguintes.```

![AZ](docs/driagrams/appService.png)

##  FinOps: Governança e otimização de custos (Azure App Services)

O projeto **Sopro** conta com um ecossistema integrado de **FinOps (Cloud Financial Operations)** desenvolvido nativamente no backend em Spring Boot. O objetivo principal deste módulo é aplicar políticas de governança automatizada sobre a infraestrutura em nuvem, eliminando o desperdício financeiro gerado por recursos ociosos fora do horário comercial.

### O problema: Desperdício em ambientes de desenvolvimento
Durante os ciclos de testes, microsserviços e APIs de ambientes de desenvolvimento (ex: `sopro-api-dev` e `sopro-web-dev`) costumam permanecer ativos de forma ininterrupta (24/7), mesmo em períodos de total inatividade, como madrugadas e finais de semana. 

###  A solução: varredura automatizada & inteligência de infraestrutura
O módulo monitora proativamente os grupos de recursos da Azure por meio de gatilhos temporais agendados (`@Scheduled` via CRON expressions) e requisições sob demanda.

 **Filtro por tags de governança**: O sistema varre os Resource Groups buscando recursos tagueados estritamente com as chaves `Environment: Development` ou `Environment: Test`.
 **Desalocação**: Ao interceptar instâncias ativas fora do horário operacional, o backend dispara chamadas assíncronas via SDK para interromper o ciclo de vida do recurso operacional (`webApp.stop()`), reduzindo o consumo de processamento de nuvem a **zero**.
 **Métricas de saving financeiro**: A API calcula instantaneamente a projeção matemática de economia gerada pela interrupção nas janelas de ociosidade (estimando um impacto de redução de custos fixos por hora por máquina alocada) e retorna relatórios estruturados de auditoria para o negócio.

### 📊 Exemplo prático (Retorno da API)

![AZ](docs/driagrams/FinOps.png)

   </div>

## Como executar o projeto

### 1. Pré-requisitos
 Java 21 instalado.
 MySQL Server em execução.
 Maven (ou utilizar o Wrapper incluído).

</div>

### 2. Variáveis de ambiente
Configura o acesso ao banco de dados criando um ficheiro `.env` na raiz do projeto seguindo o modelo abaixo:

```env
DB_HOST=localhost
DB_PORT=3306
DB_NAME=sopro_db
DB_USER=root
DB_PASSWORD=suasenha

```

<div align= "center"> Desenvolvido com 💙 e muito café pela equipe do Back-end - Instituto PROA, 2026 </div>
