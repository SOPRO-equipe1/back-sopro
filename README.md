<div align= "center">


<img src="https://github.com/SOPRO-equipe1/.github/blob/main/profile/logo.png" alt="SOPRO Logo" width="250">
  
#  API  🌬️

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.6-6DB33F?style=for-the-badge&logo=springboot&logoColor=white)
![Java](https://img.shields.io/badge/Java-21-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-005C84?style=for-the-badge&logo=mysql&logoColor=white)


O **SOPRO** é uma solução de tecnologia assistiva focada em devolver a autonomia de comunicação a indivíduos com mutismo ou limitações motoras severas. Esta API é o motor que gere a inteligência, a segurança e os dados por trás da plataforma, permitindo a conversão de inputs físicos em voz sintetizada e a gestão de perfis de utilizador.

```Se quiser ver uma documentação mais detalhada sobre a arquitetura:```

[![GitBook](https://img.shields.io/badge/GITBOOK-000?style=for-the-badge&logoColor=FFF&color=000)](https://app.gitbook.com/invite/3BzJD9kc8XUB2pCNxAEC/kKoTDjNlltVAsoU7THWY)



## 📌 Funcionalidades principais

 **Gestão de utilizadores:** Registo e autenticação segura de perfis.
 **Controle de assinaturas:** Monitorização de acesso e validade dos planos dos utilizadores.
 **Base de conhecimento:** Repositório de metadados e conteúdos para suporte à comunicação assistida.
 **Segurança robusta:** Implementação de Spring Security com suporte a OAuth2 Authorization Server.

## 🏗️ Arquitetura do sistema

O projeto segue o padrão **MVC** (Model-View-Controller) para garantir a separação de responsabilidades e a escalabilidade do sistema.


```mermaid
graph TD
    %% Estilos Gerais com Cores Vibrantes (Estética do Contraste)
    classDef client fill:#37474F,stroke:#00E5FF,stroke-width:2px,color:#fff;
    classDef security fill:#1A237E,stroke:#7C4DFF,stroke-width:2px,color:#fff;
    classDef controller fill:#1B5E20,stroke:#00E676,stroke-width:2px,color:#fff;
    classDef service fill:#B71C1C,stroke:#FF1744,stroke-width:2px,color:#fff;
    classDef repository fill:#F57F17,stroke:#FFEA00,stroke-width:2px,color:#fff;
    classDef database fill:#006064,stroke:#00E5FF,stroke-width:2px,color:#fff;
    classDef external fill:#4A148C,stroke:#E040FB,stroke-width:2px,color:#fff;

    %% Client Layer
    subgraph Client_Layer ["Camada Cliente"]
        A[React Frontend / App Mobile]:::client
    end

    %% Security & Infrastructure Layer
    subgraph Inf_Layer ["Segurança & Infraestrutura (Spring Security)"]
        B[JwtAuthenticationFilter]:::security
        C[SecurityConfig / Endpoints]:::security
    end

    %% Controller Layer (REST Endpoints)
    subgraph Controller_Layer ["Controllers (MVC - HTTP REST)"]
        D[AuthController]:::controller
        E[UsuarioController]:::controller
        F[PerfilController]:::controller
        G[AssinaturaController]:::controller
        H[PreferenciasController]:::controller
        I[ConhecimentoController]:::controller
    end

    %% Service Layer (Business Logic)
    subgraph Service_Layer ["Services (Regras de Negócio)"]
        J[AuthService]:::service
        K[UsuarioService]:::service
        L[PerfilService]:::service
        M[AssinaturaService]:::service
        N[PreferenciasService]:::service
        O[ConhecimentoService]:::service
    end

    %% Repository Layer (Data Access)
    subgraph Repository_Layer ["Repositories (Spring Data JPA)"]
        P[UsuarioRepository]:::repository
        Q[PedidoRepository]:::repository
        R[AssinaturaRepository]:::repository
        S[PagamentoRepository]:::repository
        T[PreferenciasAcessibilidadeRepository]:::repository
        U[ConhecimentoRepository]:::repository
    end

    %% Database & External Engines
    subgraph Storage_Layer ["Persistência & Serviços Externos"]
        V[(MySQL / PostgreSQL)]:::database
        W[Google Gemini API]:::external
    end

    %% Fluxo de Conexões e Requisições
    A -->|1. Envia Requisição HTTP + JWT| B
    B -->|2. Valida Token| C
    C -->|3. Redireciona para o Endpoints| D & E & F & G & H & I
    
    %% Vinculação Controller -> Service
    D --> J
    E --> K
    F --> L
    G --> M
    H --> N
    I --> O

    %% Vinculação Service -> Repository / External
    J --> P
    K --> P & T
    L --> P & Q
    M --> P & R & S
    N --> P & T
    O -->|Busca dados para o RAG| U
    O -->|Injeta Contexto / Prompt| W

    %% Vinculação Repository -> Database
    P & Q & R & S & T & U --> V

```
  



## DER

![DER](docs/driagrams/modelo-logico.png)

## 🛠️ Stack tecnológica

 **Linguagem:** Java 21.
 **Framework:** Spring Boot 4.0.6.
 **Persistência:** Spring Data JPA.
 **Banco de Dados:** MySQL.
 **Segurança:** Spring Security & OAuth2.

 </div>

## Como Executar o projeto

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

<div align= "center"> Desenvolvido com 💙 e muito café pela equipe do Back-end. </div>
