# Documentação do Projeto ERP Arecológico (Spring Boot + JPA)

Bem-vindo à documentação oficial do **ERP Arecológico**, um sistema corporativo desenvolvido em **Java 21** e **Spring Boot** voltado para a gestão completa de serviços de climatização, refrigeração, contratos de manutenção preventiva/corretiva e emissão de ordens de serviço (OS).

Esta documentação detalha a arquitetura do projeto, o modelo relacional de banco de dados baseado no script [`dba.sql`](src/main/resources/sql/dba.sql), a estrutura de classes e a aplicação didática de todos os conceitos e anotações ensinadas no material de referência **"Annotations Spring Boot"** (Profª. Dra. Luciene Cavalcanti).

---

## Sumário

1. [Visão Geral e Arquitetura](#1-visão-geral-e-arquitetura)
2. [Modelo Relacional de Dados (`dba.sql`)](#2-modelo-relacional-de-dados-dbasql)
   - [Diagrama Entidade-Relacionamento (ER)](#diagrama-entidade-relacionamento-er)
   - [Dicionário de Tabelas](#dicionário-de-tabelas)
3. [Guia Completo de Anotações Spring Boot & JPA](#3-guia-completo-de-anotações-spring-boot--jpa)
   - [Anotações Estruturais de Entidade](#anotações-estruturais-de-entidade)
   - [Mapeamento de Chaves e Colunas](#mapeamento-de-chaves-e-colunas)
   - [Mapeamento de Relacionamentos](#mapeamento-de-relacionamentos)
   - [Objetos Embutidos (`@Embeddable` e `@Embedded`)](#objetos-embutidos-embeddable-e-embedded)
   - [Anotações Especiais (`@Transient` e `@Version`)](#anotações-especiais-transient-e-version)
   - [Estratégias de Herança JPA](#estratégias-de-herança-jpa)
   - [Camada de Acesso a Dados (`@Repository` e `@Query`)](#camada-de-acesso-a-dados-repository-e-query)
4. [Configurações de Banco de Dados (`application.properties`)](#4-configurações-de-banco-de-dados-applicationproperties)
5. [Estrutura de Pacotes do Projeto](#5-estrutura-de-pacotes-do-projeto)
6. [Instruções de Execução](#6-instruções-de-execução)

---

## 1. Visão Geral e Arquitetura

O sistema foi arquitetado segundo as melhores práticas do ecossistema Spring:

- **Linguagem**: Java 21 (LTS)
- **Framework**: Spring Boot 4.x / Spring Framework 6.x
- **Persistência**: Spring Data JPA com Hibernate ORM
- **Banco de Dados**: Microsoft SQL Server (padrão) ou MySQL (configurável)
- **Utilitários**: Project Lombok (redução de boilerplate com `@Getter`, `@Setter`, etc.)
- **Gerenciador de Dependências**: Apache Maven

---

## 2. Modelo Relacional de Dados (`dba.sql`)

### Diagrama Entidade-Relacionamento (ER)

```mermaid
erDiagram
    MARCA ||--o{ EQUIPAMENTO : "possui (1:N)"
    PESSOA ||--|| CLIENTE : "herança JOINED"
    PESSOA ||--|| PRESTADOR_SERVICO : "herança JOINED"
    TIPO_CONTRATO ||--o{ CONTRATO : "define (1:N)"
    CLIENTE ||--o{ CONTRATO : "contrata (1:N)"
    CONTRATO }o--o{ EQUIPAMENTO : "CONTRATO_EQUIPAMENTO (N:N)"
    CONTRATO }o--o{ ORDEM_SERVICO : "CONTRATO_OS (N:N)"
    CLIENTE ||--o{ ORDEM_SERVICO : "solicita (1:N)"
    PRESTADOR_SERVICO ||--o{ ORDEM_SERVICO : "executa (1:N)"
    TIPO_OS ||--o{ ORDEM_SERVICO : "classifica (1:N)"
    TIPO_PAGAMENTO ||--o{ ORDEM_SERVICO : "paga com (1:N)"
    ORDEM_SERVICO }o--o{ SERVICO : "ORDEM_SERVICO_SERVICO (N:N)"

    MARCA {
        int COD_MARCA PK
        varchar NOME_MARCA
    }
    EQUIPAMENTO {
        int COD_EQUIPAMENTO PK
        varchar MODELO
        int BTUS
        int COD_MARCA FK
    }
    PESSOA {
        int COD_PESSOA PK
        varchar NOME
        char CPF
        varchar RG
        varchar TELEFONE
        varchar ENDERECO
        varchar NUMERO_CASA
        char CEP
        varchar CIDADE
        char UF
    }
    CLIENTE {
        int COD_PESSOA PK_FK
        varchar EMAIL
        char TIPO
        date DATA_NASCIMENTO
    }
    PRESTADOR_SERVICO {
        int COD_PESSOA PK_FK
        decimal PAGAMENTO
    }
    CONTRATO {
        int COD_CONTRATO PK
        varchar DESCRICAO_CONTRATO
        decimal VALOR_CONTRATO
        date DATA_CONTRATO_INICIO
        date DATA_CONTRATO_FIM
        int COD_TIPO_CONTRATO FK
        int COD_PESSOA FK
    }
    ORDEM_SERVICO {
        int COD_OS PK
        varchar OBS
        decimal VALOR_TOTAL
        date DATA_ABERTURA
        varchar STATUS
        date DATA_EXECUCAO
        varchar DESCRICAO_PROBLEMA
        varchar DESCRICAO_EXECUTADO
        int COD_PESSOA_CLIENTE FK
        int COD_PESSOA_PRESTADOR FK
        int COD_TIPO_OS FK
        int COD_TIPO_PAGAMENTO FK
    }
    SERVICO {
        int COD_SERVICO PK
        varchar DESCRICAO_SERVICO
        decimal VALOR_SERVICO
        date DATA_SERVICO_INICIO
        date DATA_SERVICO_FIM
    }
```

### Dicionário de Tabelas

| Tabela | Finalidade | Chave Primária | Chaves Estrangeiras |
| :--- | :--- | :--- | :--- |
| `MARCA` | Marcas de aparelhos de ar-condicionado (LG, Elgin, Daikin...) | `COD_MARCA` | Nenhuma |
| `EQUIPAMENTO` | Aparelhos cadastrados (Modelo, BTUs) | `COD_EQUIPAMENTO` | `COD_MARCA` → `MARCA` |
| `TIPO_CONTRATO` | Periodicidade/plano de contrato (Mensal, Anual...) | `COD_TIPO_CONTRATO` | Nenhuma |
| `TIPO_PAGAMENTO` | Métodos de pagamento aceitos (Pix, Cartão, Boleto...) | `COD_TIPO_PAGAMENTO` | Nenhuma |
| `TIPO_OS` | Tipos de OS (Preventiva, Corretiva, Instalação...) | `COD_TIPO_OS` | Nenhuma |
| `SERVICO` | Catálogo de serviços com valor e vigência | `COD_SERVICO` | Nenhuma |
| `PESSOA` | Tabela base de indivíduos e empresas (Herança) | `COD_PESSOA` | Nenhuma |
| `CLIENTE` | Extensão de Pessoa para clientes (PF/PJ) | `COD_PESSOA` | `COD_PESSOA` → `PESSOA` |
| `PRESTADOR_SERVICO` | Extensão de Pessoa para técnicos/prestadores | `COD_PESSOA` | `COD_PESSOA` → `PESSOA` |
| `CONTRATO` | Contratos de manutenção firmados | `COD_CONTRATO` | `COD_TIPO_CONTRATO`, `COD_PESSOA` |
| `CONTRATO_EQUIPAMENTO` | Tabela associativa (N:N) Contrato ↔ Equipamentos | `(COD_CONTRATO, COD_EQUIPAMENTO)` | `COD_CONTRATO`, `COD_EQUIPAMENTO` |
| `ORDEM_SERVICO` | Ordens de serviço abertas no ERP | `COD_OS` | `COD_PESSOA_CLIENTE`, `COD_PESSOA_PRESTADOR`, `COD_TIPO_OS`, `COD_TIPO_PAGAMENTO` |
| `ORDEM_SERVICO_SERVICO` | Tabela associativa (N:N) OS ↔ Serviços | `(COD_OS, COD_SERVICO)` | `COD_OS`, `COD_SERVICO` |
| `CONTRATO_OS` | Tabela associativa (N:N) Contrato ↔ Ordens de Serviço | `(COD_CONTRATO, COD_OS)` | `COD_CONTRATO`, `COD_OS` |

---

## 3. Guia Completo de Anotações Spring Boot & JPA

Abaixo são apresentados os conceitos e parâmetros ensinados no PDF com suas respectivas implementações no código:

### Anotações Estruturais de Entidade

#### 1. `@Entity` (PDF Slide 3)
- **Descrição**: Marca uma classe Java como entidade gerenciada pelo EntityManager/Hibernate.
- **Parâmetros**: `name` (nome da entidade na JPQL).
- **Exemplo no Projeto** (`MarcaEntity.java`):
  ```java
  @Entity(name = "MarcaEntity")
  public class MarcaEntity { ... }
  ```

#### 2. `@Table` (PDF Slide 4)
- **Descrição**: Especifica a tabela física correspondente no banco de dados.
- **Parâmetros**: `name` (nome da tabela), `schema` (esquema), `uniqueConstraints` (restrições de unicidade).
- **Exemplo no Projeto** (`EquipamentoEntity.java`):
  ```java
  @Table(name = "EQUIPAMENTO")
  public class EquipamentoEntity { ... }
  ```

---

### Mapeamento de Chaves e Colunas

#### 3. `@Id` (PDF Slide 5)
- **Descrição**: Indica que o atributo Java é a Chave Primária (Primary Key) da entidade.

#### 4. `@GeneratedValue` (PDF Slide 6)
- **Descrição**: Define como a chave primária é gerada.
- **Parâmetros**: `strategy` (`GenerationType.IDENTITY`, `SEQUENCE`, `TABLE`, `AUTO`).
- **Exemplo no Projeto** (`ServicoEntity.java`):
  ```java
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "COD_SERVICO")
  private Integer cod_Servico;
  ```

#### 5. `@Column` (PDF Slide 7)
- **Descrição**: Detalha restrições e propriedades da coluna no banco de dados.
- **Parâmetros**:
  - `name`: Nome da coluna física no SQL.
  - `nullable`: Se a coluna aceita `NULL` (`true` ou `false`).
  - `length`: Tamanho máximo para tipos texto (ex: `length = 100`).
  - `precision` e `scale`: Precisão total e casas decimais para números fracionários/monetários.
- **Exemplo no Projeto** (`ServicoEntity.java`):
  ```java
  @Column(name = "VALOR_SERVICO", precision = 10, scale = 2, nullable = false)
  private Float valor_Servico;
  ```

---

### Mapeamento de Relacionamentos

#### 6. `@ManyToOne` e `@OneToMany` (PDF Slides 11, 15 e 18)
- **`@ManyToOne`**: Relacionamento N:1 (muitos para um).
  - `fetch`: `FetchType.LAZY` (carregamento tardio) ou `FetchType.EAGER` (carregamento imediato).
  - `optional`: Define se a associação é opcional (`true`) ou obrigatória (`false`).
- **`@JoinColumn`**: Mapeia a coluna de chave estrangeira (FK).
  - `name`: Nome da coluna no banco (`COD_MARCA`).
  - `referencedColumnName`: Nome da coluna de destino na tabela referenciada.
- **`@OneToMany`**: Relacionamento 1:N (um para muitos).
  - `mappedBy`: Nome do atributo na classe filha que é dona do relacionamento.
  - `cascade`: Propagação de operações (`CascadeType.ALL`, `PERSIST`, `MERGE`, etc.).
- **Exemplo no Projeto** (`MarcaEntity.java` e `EquipamentoEntity.java`):
  ```java
  // Em EquipamentoEntity (lado N:1 - Dono da FK):
  @ManyToOne(fetch = FetchType.LAZY, optional = false)
  @JoinColumn(name = "COD_MARCA", referencedColumnName = "COD_MARCA", nullable = false)
  private MarcaEntity marca;

  // Em MarcaEntity (lado 1:N - Bidirecional):
  @OneToMany(mappedBy = "marca", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
  private List<EquipamentoEntity> equipamentos = new ArrayList<>();
  ```

#### 7. `@ManyToMany` e `@JoinTable` (PDF Slide 17)
- **Descrição**: Mapeia relacionamentos N:N através de uma tabela de junção intermediária.
- **Parâmetros de `@JoinTable`**:
  - `name`: Nome da tabela intermediária associativa (`CONTRATO_EQUIPAMENTO`).
  - `joinColumns`: Coluna FK que referencia esta entidade (`COD_CONTRATO`).
  - `inverseJoinColumns`: Coluna FK que referencia a entidade associada (`COD_EQUIPAMENTO`).
- **Exemplo no Projeto** (`ContratoEntity.java`):
  ```java
  @ManyToMany(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST, CascadeType.MERGE})
  @JoinTable(
      name = "CONTRATO_EQUIPAMENTO",
      joinColumns = @JoinColumn(name = "COD_CONTRATO"),
      inverseJoinColumns = @JoinColumn(name = "COD_EQUIPAMENTO")
  )
  private List<EquipamentoEntity> equipamentos = new ArrayList<>();
  ```

---

### Objetos Embutidos (`@Embeddable` e `@Embedded`)

#### 8. `@Embeddable` e `@Embedded` (PDF Slides 19 e 20)
- **`@Embeddable`**: Marca uma classe como embutível (não possui tabela própria, seus campos são incorporados em outra entidade).
- **`@Embedded`**: Indica na entidade proprietária que os atributos da classe `@Embeddable` devem fazer parte da sua tabela no banco de dados.
- **Implementação no Projeto** ([`EnderecoEmbeddable.java`](src/main/java/br/com/arecologico/erp/database/model/EnderecoEmbeddable.java) e [`PessoaEntity.java`](src/main/java/br/com/arecologico/erp/database/model/PessoaEntity.java)):
  ```java
  @Embeddable
  public class EnderecoEmbeddable {
      @Column(name = "ENDERECO", length = 200)
      private String endereco;
      @Column(name = "NUMERO_CASA", length = 10)
      private String numeroCasa;
      @Column(name = "CEP", length = 8)
      private String cep;
      @Column(name = "CIDADE", length = 100)
      private String cidade;
      @Column(name = "UF", length = 2)
      private String uf;
  }

  // Em PessoaEntity:
  @Embedded
  private EnderecoEmbeddable dadosEndereco = new EnderecoEmbeddable();
  ```

---

### Anotações Especiais (`@Transient` e `@Version`)

#### 9. `@Transient` (PDF Slide 21)
- **Descrição**: Indica que o atributo pertence apenas à lógica de negócio em memória e **NÃO** deve ser persistido no banco de dados.
- **Exemplo no Projeto** (`PessoaEntity.java`):
  ```java
  @Transient
  private String informacaoTemporaria;
  ```

#### 10. `@Version` (PDF Slide 21)
- **Descrição**: Habilita o controle de **Concorrência Otimista (Optimistic Locking)**. O JPA incrementa automaticamente este contador a cada atualização, lançando uma exceção se outra transação alterou o registro concorrentemente.
- **Exemplo no Projeto** (`PessoaEntity.java`):
  ```java
  @Version
  @Column(name = "VERSAO")
  private Long versao;
  ```

---

### Estratégias de Herança JPA

O PDF aborda as 3 estratégias de herança do JPA (Slides 23 a 34):

1. **`SINGLE_TABLE`**: Todas as classes da hierarquia compartilham uma única tabela. Uma coluna discriminadora (`@DiscriminatorColumn` / `@DiscriminatorValue`) define o tipo da linha.
2. **`JOINED`**: Cria uma tabela para cada classe da hierarquia. A superclasse contém os dados comuns e as subclasses contêm os dados específicos, ligadas por Chave Primária Compartilhada via `@PrimaryKeyJoinColumn`.
3. **`TABLE_PER_CLASS`**: Cada classe concreta tem sua própria tabela completa com todos os atributos duplicados.

#### Estratégia Adotada no ERP: `InheritanceType.JOINED`
Conforme especificado no script [`dba.sql`](src/main/resources/sql/dba.sql), as tabelas `PESSOA`, `CLIENTE` e `PRESTADOR_SERVICO` utilizam chaves primárias e estrangeiras compartilhadas (`COD_PESSOA`). Portanto, o mapeamento exato é:

```java
// Superclasse PessoaEntity (Slide 28 do PDF):
@Entity(name = "PessoaEntity")
@Table(name = "PESSOA")
@Inheritance(strategy = InheritanceType.JOINED)
public class PessoaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "COD_PESSOA")
    private Integer cod_Pessoa;
    ...
}

// Subclasse PessoaClienteEntity (Slide 29 do PDF):
@Entity(name = "PessoaClienteEntity")
@Table(name = "CLIENTE")
@PrimaryKeyJoinColumn(name = "COD_PESSOA")
public class PessoaClienteEntity extends PessoaEntity {
    @Column(name = "EMAIL")
    private String email;
    @Column(name = "TIPO")
    private String tipo;
    @Column(name = "DATA_NASCIMENTO")
    private LocalDate data_Nascimento;
}

// Subclasse PrestadorServicoEntity (Slide 29 do PDF):
@Entity(name = "PrestadorServicoEntity")
@Table(name = "PRESTADOR_SERVICO")
@PrimaryKeyJoinColumn(name = "COD_PESSOA")
public class PrestadorServicoEntity extends PessoaEntity {
    @Column(name = "PAGAMENTO")
    private Float pagamento;
}
```

---

### Camada de Acesso a Dados (`@Repository` e `@Query`)

#### 11. `@Repository` e `@Query` (PDF Slide 22)
- **`@Repository`**: Anotação Spring que registra a interface no container e traduz exceções de banco de dados.
- **`JpaRepository<Entidade, TipoId>`**: Interface base do Spring Data que disponibiliza métodos CRUD automáticos.
- **`@Query` com JPQL**: Consultas baseadas nas classes e atributos Java.
- **`@Query(nativeQuery = true)`**: Consultas escritas diretamente na sintaxe SQL do banco.

**Exemplos no Projeto**:
- Consulta JPQL com JOIN e parâmetros nomeados (`@Param`):
  ```java
  @Query("SELECT e FROM EquipamentoEntity e JOIN e.marca m WHERE LOWER(m.nome_Marca) = LOWER(:nomeMarca)")
  List<EquipamentoEntity> buscarPorNomeMarcaJPQL(@Param("nomeMarca") String nomeMarca);
  ```
- Consulta SQL Nativa:
  ```java
  @Query(value = "SELECT * FROM MARCA WHERE NOME_MARCA = :nome", nativeQuery = true)
  Optional<MarcaEntity> buscarPorNomeExatoSQLNativo(@Param("nome") String nome);
  ```
- Consulta JPQL com `JOIN FETCH` (Prevenção do problema N+1):
  ```java
  @Query("SELECT DISTINCT c FROM ContratoEntity c LEFT JOIN FETCH c.equipamentos WHERE c.valor_Contrato >= :valorMinimo")
  List<ContratoEntity> buscarContratosComEquipamentosJPQL(@Param("valorMinimo") Float valorMinimo);
  ```

---

## 4. Configurações de Banco de Dados (`application.properties`)

Baseado nos slides 35 e 36 do PDF, o arquivo [`src/main/resources/application.properties`](src/main/resources/application.properties) conta com as configurações completas documentadas:

```properties
# 1. Nome da Aplicação
spring.application.name=erp

# 2. Conexão Microsoft SQL Server (Slide 35 do PDF)
spring.datasource.url=jdbc:sqlserver://localhost:1433;databaseName=ArecologicoERP;encrypt=true;trustServerCertificate=true
spring.datasource.username=sa
spring.datasource.password=suaSenha123
spring.datasource.driver-class-name=com.microsoft.sqlserver.jdbc.SQLServerDriver

# 3. Propriedades Hibernate e JPA (Slide 35 do PDF)
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.hibernate.ddl-auto=none
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.SQLServerDialect

# 4. Encoding UTF-8 (Slide 36 do PDF)
server.servlet.encoding.charset=UTF-8
server.servlet.encoding.force=true
```

---

## 5. Estrutura de Pacotes do Projeto

```
src/main/java/br/com/arecologico/erp/
├── ErpApplication.java                     # Classe principal com @SpringBootApplication e carga mockada
├── ServletInitializer.java                 # Inicializador para deployment em container de servlets (WAR)
├── database/
│   ├── model/                             # Entidades JPA mapeadas conforme dba.sql e PDF
│   │   ├── ContratoEntity.java             # Entidade CONTRATO com @ManyToMany e @ManyToOne
│   │   ├── ContratoEquipamento.java        # Entidade associativa N:N com @EmbeddedId
│   │   ├── ContratoOS.java                 # Entidade associativa N:N com @EmbeddedId
│   │   ├── EnderecoEmbeddable.java         # Objeto de valor com @Embeddable (Slide 19-20)
│   │   ├── EquipamentoEntity.java          # Entidade EQUIPAMENTO
│   │   ├── MarcaEntity.java                # Entidade MARCA com @OneToMany
│   │   ├── OrdemServicoServico.java        # Entidade associativa N:N com @EmbeddedId
│   │   ├── OSEntity.java                   # Entidade ORDEM_SERVICO com @ManyToMany e @ManyToOne
│   │   ├── PessoaEntity.java               # Superclasse PESSOA com @Inheritance(JOINED), @Version, @Transient
│   │   ├── PessoaClienteEntity.java        # Subclasse CLIENTE com @PrimaryKeyJoinColumn
│   │   ├── PrestadorServicoEntity.java     # Subclasse PRESTADOR_SERVICO com @PrimaryKeyJoinColumn
│   │   ├── ServicoEntity.java              # Entidade SERVICO com precision/scale
│   │   ├── TipoContratoEntity.java         # Entidade TIPO_CONTRATO
│   │   ├── TipoOsEntity.java               # Entidade TIPO_OS
│   │   └── TipoPagamentoEntity.java        # Entidade TIPO_PAGAMENTO
│   └── repository/                        # Camada de Repositórios com @Repository e @Query (Slide 22)
│       ├── ContratoRepository.java
│       ├── EquipamentoRepository.java
│       ├── MarcaRepository.java
│       ├── OSRepository.java
│       ├── PessoaClienteRepository.java
│       ├── PessoaRepository.java
│       ├── PrestadorServicoRepository.java
│       ├── ServicoRepository.java
│       ├── TipoContratoRepository.java
│       ├── TipoOsRepository.java
│       └── TipoPagamentoRepository.java
src/main/resources/
├── application.properties                 # Configurações do Spring Boot, SQL Server/MySQL e JPA
└── sql/
    ├── dba.sql                            # DDL completo das tabelas, PKs, FKs e constraints
    └── inserts.sql                        # Carga de dados de teste
```

---

## 6. Instruções de Execução

### Pré-requisitos
- **Java JDK 21** instalado e configurado no `PATH`
- Servidor **Microsoft SQL Server** (ou MySQL) em execução

### Passo a Passo

1. **Criar o Banco de Dados**:
   Execute o script [`dba.sql`](src/main/resources/sql/dba.sql) no seu cliente de banco de dados (ex: SQL Server Management Studio / DBeaver) para criar o banco `ArecologicoERP` e todas as tabelas.

2. **Inserir Dados de Teste (Opcional)**:
   Execute o script [`inserts.sql`](src/main/resources/sql/inserts.sql) para popular o banco de dados com dados iniciais de marcas, equipamentos, clientes e serviços.

3. **Ajustar as Credenciais**:
   Edite o arquivo `src/main/resources/application.properties` informando o usuário e senha do seu banco de dados.

4. **Compilar e Executar a Aplicação**:
   Abra o terminal na raiz do projeto e execute:
   ```bash
   # Compilação
   ./mvnw clean compile

   # Execução
   ./mvnw spring-boot:run
   ```

Ao iniciar, o Spring Boot carregará as configurações do JPA, conectará ao banco de dados e executará a rotina de demonstração de carregamento e navegação entre relacionamentos de contratos, equipamentos e ordens de serviço.
