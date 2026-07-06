# Calculadora de Empréstimos - Desafio TOTVS

Este projeto consiste em uma aplicação Full Stack desenvolvida para simular o cálculo de parcelas de um empréstimo.

O sistema aplica regras de:

- Juros compostos com base em dias corridos;
- Cálculo de amortização;
- Projeção do saldo devedor;

atendendo aos requisitos do desafio técnico da TOTVS.

---

# Tecnologias Utilizadas

# Back-end
- Java
- Spring Boot
- Maven

# Front-end
- React
- TypeScript
- Vite

# Comunicação
- API REST (JSON)

---

# Arquitetura e Decisões Técnicas

# Separação de Responsabilidades

Toda a regra de negócio e o motor de cálculos financeiros foram implementados no Back-end (Java), garantindo:

- Segurança;
- Integridade dos cálculos;
- Centralização da lógica de negócio;
- Melhor performance.

O Front-end (React) é responsável por:

- Interface do usuário;
- Coleta dos dados;
- Validações de entrada;
- Exibição do demonstrativo financeiro.

---

# Gestão de Datas

Foi utilizada a API de datas do Java (java.time), principalmente as classes:

- `YearMonth`
- `TemporalAdjusters`

Essa abordagem evita problemas relacionados a:

- Meses com diferentes quantidades de dias;
- Anos bissextos;
- Datas no final do mês.

Dessa forma, o cronograma de parcelas permanece consistente independentemente do calendário.

---

# Validações (UX/UI)

Antes de enviar qualquer solicitação para a API, o Front-end realiza validações preventivas para impedir cronogramas inválidos.

Exemplo:

- Primeiro pagamento maior que a data final do contrato.

Essa validação melhora a experiência do usuário e evita processamento desnecessário no servidor.

---

# Como Executar o Projeto

Para executar a aplicação localmente, é necessário iniciar o Back-end e o Front-end.

# Executando o Back-end

Entre na pasta do projeto onde está localizado o arquivo `pom.xml`.

Execute:

```bash
./mvnw spring-boot:run
```

No Windows (PowerShell):

```powershell
.\mvnw spring-boot:run
```

O servidor será iniciado em:

```
http://localhost:8080
```

---

# Executando o Front-end

Entre na pasta do Front-end e instale as dependências:

```bash
npm install
```

Depois execute:

```bash
npm run dev
```

A aplicação ficará disponível em:

```
http://localhost:5173
```

---

# Estrutura do Projeto

```
Calculadora-Emprestimos/
│
├── calculadora/          # Back-end (Spring Boot)
│
├── frontend/             # Front-end (React + Vite)
│
└── README.md
```

---

# Funcionalidades

- Simulação de empréstimos
- Cálculo de juros compostos
- Cálculo de amortização
- Projeção do saldo devedor
- Cronograma completo de parcelas
- Validação de datas
- Integração via API REST

---

# Observações

Este projeto foi desenvolvido como solução para o Desafio Técnico da TOTVS, priorizando:

- Organização do código;
- Boas práticas de desenvolvimento;
- Separação de responsabilidades;
- Facilidade de manutenção;
- Escalabilidade.