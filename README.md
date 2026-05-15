# Automanager

Sistema para gerenciamento de lojas de manutenção veicular e venda de autopeças.

## Como rodar
Rodar no terminal, dentro de "<AV3-DWIII\atviii-autobots-microservico-spring-master\automanager>"

Linux:
```
./mvnw spring-boot:run -DskipTests
```
Windows:
```
.\mvnw.cmd spring-boot:run -DskipTests"
```

A aplicação sobe em `http://localhost:8080`.  

## Endpoints

 CRUD:

- `/empresa`
- `/usuario`
- `/veiculo`
- `/mercadoria`
- `/servico`
- `/venda`

### Exemplos de uso

**Listar todos:**
```
GET http://localhost:8080/empresa
```

**Buscar por ID:**
```
GET http://localhost:8080/empresa/1
```

**Cadastrar:**
```
POST http://localhost:8080/empresa

{
  "razaoSocial": "Fatec Salgados LTDA",
  "nomeFantasia": "Fatec Salgados",
  "cadastro": "2026-01-01"
}
```

**Atualizar:**
```
PUT http://localhost:8080/empresa/1

{
  "nomeFantasia": "Fatec Salgadões"
}
```

**Deletar:**
```
DELETE http://localhost:8080/empresa/1
```

> O mesmo padrão se aplica a todos os outros endpoints.