
# Bank Transactions

Este projeto é uma aplicação para gerenciar transações bancárias. Ele fornece uma API para criar e consultar usuários e transações financeiras.

## Requisitos

- Java 11 ou superior
- Gradle
- Docker

## Como rodar o aplicativo

1. Clone o repositório:
   ```bash
   git clone https://github.com/bragaLeandro/bank-transactions.git
   ```

2. Navegue até o diretório do projeto:
   ```bash
   cd bank-transactions
   ```

3. Construa o projeto:
    ```bash
    ./gradlew build
    ```

4. Execute a aplicação:
    ```bash
    docker-compose up
    ```

4. A aplicação estará disponível em `http://localhost:8080`.

## Endpoints

### Usuário

- **Criar usuário**

  `POST /user`

  ```json
  {
    "name": "Usuario Exemplo",
    "email": "user@example.com",
    "birthDate": "2001-01-01",
    "address": "Rua Exemplo",
    "password": "Example",
    "accountNumber": "123456"
  }
  ```

- **Obter saldo do usuário logado**

  `GET /balance`

  **Resposta:**

  ```json
  {
    "amount": 10000
  }
  ```

### Transações

- **Fazer uma transação**

  `POST /transaction/transfer`

  ```json
  {
    "senderAccountNumber": "1234567",
    "destinationAccountNumber": "7654321",
    "amount": 100,
    "type": "DEBIT"
  }
  ```

- **Obter transações do usuário logado**

  `GET /balance`

  **Resposta:**

  ```json
  [
      {
          "amount": 10.00,
          "status": "Success",
          "type": "CREDIT",
          "transactionDate": "2024-05-27T21:30:12.161809"
      },
      {
          "amount": 10.00,
          "status": "Success",
          "type": "DEBIT",
          "transactionDate": "2024-05-27T21:30:23.646229"
      }
  ]
  ```
---
