# Advantage Shopping – Automação de Testes
Automação de testes End-to-End (UI e API) para a aplicação Advantage Online Shopping, implementada com Java + Playwright + Cucumber.
Este projeto valida fluxos de compra — desde a adição de produtos ao carrinho, edição/remoção até a finalização — e integra com o Allure para relatórios detalhados.

## Funcionalidades Cobertas
- Adicionar produtos ao carrinho — Garante que os itens sejam adicionados corretamente e que o preço total esteja correto.

- Remover produtos do carrinho — Confirma que o carrinho fica vazio após a remoção.

- Editar produtos no carrinho — Atualiza quantidade, cor e valida o preço.

- Fluxo completo de checkout — Do carrinho até o pagamento e resumo do pedido.

- Adiciona Item ao carrinho sem estar logado no sistema

- Validações via API — Limpa o carrinho por API ao final de cada teste.

- Registro em banco de dados — Armazena os resultados dos testes para rastreamento e auditoria.

- Os arquivos de cenário são escritos em Gherkin (Português) para facilitar a leitura.

## Estrutura do projeto

```bash
.
├── .mvn/
├── allure-results/
├── db/
├── logs/
├── src/
│   ├── main/
│   │   └── java/
│   │       ├── db/                # Acesso e manipulação de dados no SQLite
│   │       ├── fixture/           # Massa de dados e configurações fixas
│   │       ├── pages/             # Page Object Models
│   │       ├── serializers/       # POJOs para API/DB
│   │       ├── stepdefinitions/   # Definições de passos do Cucumber
│   │       └── testrunner/        # Runner de testes
│   └── resources/
│       ├── features/              # Cenários Cucumber
│       │   └── advantage.feature
│       ├── allure.properties
│       └── log4j2.xml
├── target/
├── .env.dev
├── .gitignore
├── README.md
├── docker-compose.yml
└──pom.xml
```

## Tecnologias Utilizadas

- Java 21

- Playwright – Automação de browser

- Cucumber – Framework BDD

- JUnit 5 – Execução de testes

- SQLite – Armazenamento dos resultados

- OkHttp – Requisições de API

- Jackson – Serialização/Deserialização JSON

- Allure – Relatórios de testes

- Docker & Docker Compose – Execução em containers

## Execução Local

Pré-requisitos:

- Java 21+

- Maven 3.9+

Rodar testes em bash:

```bash
  ENV=dev mvn clean test -Dcucumber.filter.tags=@Checkout
```

Você pode trocar @Checkout por qualquer tag dos seus arquivos .feature (ex.: @Auth, @FullCheckout).


ENV é a váriavel de ambiente que define em qual ambiente o teste será executado, verificar arquivo: .env.dev na raíz do projeto

## Execução com Docker

Pré-requisitos:

- docker

### Execução

Rodar tudo em containers (testes + serviço do Allure):

```bash
  docker compose build test-runner
```

Execução de testes:

```bash
  TAGS="@Checkout" docker compose run --rm -e ENV=dev test-runner
```

TAGS → Filtro de tags do Cucumber (padrão: @Checkout)


## Relatórios Allure
Após executar os testes:

Certifique-se de que o serviço do Allure está ativo:

```bash
  docker compose up allure
```

ou localmente:

```bash
 allure serve allure-results
```

Acesse o relatório Allure em:
http://localhost:5050
http://localhost:5050/static/projects/default/reports/latest/index.html

## CI - Execução

É executado uma esteira sempre que uma pull request é feita para a master,
o arquivo está localizado em: [CI Workflow](.github/workflows/ci.yml)

## Hooks de Teste

@BeforeAll – Cria tabelas no banco e inicializa o Playwright.

@Before – Abre o navegador e registra o início do cenário.

@Before("@Auth") – Realiza login antes de cenários autenticados.

@After – Salva resultados no banco, limpa carrinho via API e captura screenshot em caso de falha.

@AfterAll – Fecha o contexto e encerra a instância do Playwright.

### Licença
Licença MIT — livre para usar, modificar e compartilhar.