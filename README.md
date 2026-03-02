# AgroNorte – O SaaS Definitivo para o Agro do Mato Grosso

Bem-vindo ao repositório oficial do **AgroNorte**, um SaaS B2B completo focado na conexão entre comerciantes de insumos e produtores rurais do Mato Grosso. 

Construído com zero tolerância a espaguete, voltado pra alta performance, manutenibilidade e escalabilidade real. Se você está cansado de sistemas que caem na sexta-feira à noite, você chegou ao lugar certo.

## 🚀 Stack Tecnológica (A mesma das Big Techs, sem buzzwords inúteis)

### Backend
- **Java 21 + Spring Boot 3** (A espinha dorsal corporativa que não te deixa na mão)
- **Clean Architecture** absoluta: as regras de negócio (Domain) não conhecem REST nem Banco de Dados
- **Spring Security + JWT (HS512)**: Segurança stateless e escalável horizontalmente
- **MySQL 8 + Flyway**: Controle de schema rigoroso. O banco não é brinquedo.

### Frontend
- **React 18 + TypeScript (Vite)**
- **Zustand** para gerenciamento de estado global (sem Redux boilerplate)
- **TanStack Query (React Query)** pra cache e server-state impecável
- **Axios** com interceptors blindados contra token expirado
- Design System customizado (nada de Tailwind poluindo seus componentes)
- **Recharts** para os gráficos de venda que os gestores amam ver

### Infra & DevOps
- **Docker & Docker Compose**: O ambiente sobe em uma única linha no terminal
- **Kubernetes (K8s)**: Próxima parada, Produção — Deployments, StatefulSets (MySQL) e Services prontos 
- **GitHub Actions**: Pipeline de CI/CD completa para testar e buildar tudo automático

## ⚙️ Como rodar esse canhão na sua máquina (Ambiente Local)

Pré-requisito: Ter o **Docker Desktop** rodando. Mãos à obra:

```bash
# 1. Clone o repositório (ou acesse a pasta raiz)
cd agronorte

# 2. Suba o ambiente inteiro (Backend, Frontend, MySQL e Adminer)
docker-compose -f infra/docker-compose.yml up --build -d
```

O ambiente vai subir silenciosamente:
- **Frontend / Dashboard**: [http://localhost:3000](http://localhost:3000)
- **Backend / API Docs (Swagger)**: [http://localhost:8080/swagger-ui.html](http://localhost:8080/swagger-ui.html)
- **Banco de Dados UI (Adminer)**: [http://localhost:8081](http://localhost:8081)

## 🏗️ Estrutura de Diretórios (O Monorepo)

```
agronorte/
 ├─ backend/             → Microserviço Spring Boot (Domínio forte, JPA só na infra)
 ├─ frontend/            → SPA ultra-rápida (React + Vite, dark mode por default)
 ├─ infra/               → IaC: Manifestos Kubernetes e Docker Compose
 └─ .github/workflows/    → Pipeline do GitHub Actions (CI/CD)
```

## 🔐 Conta Admin de Homologação 
O Flyway já roda as migrations injetando uma conta admin pronta pra uso:
- **E-mail:** `admin@agronorte.com.br`
- **Senha:** `Admin@2024`

## 💬 Filosofia de Código (Se não concordar, nem sobe PR)
1. **Domain é rei:** Entidade anêmica aqui não entra. Lógica mora no modelo.
2. **Tell, Don't Ask:** Você manda o objeto mudar o próprio estado, não fica catando getters e setters na Controller.
3. **Double pra dinheiro é crime inafiançável:** Valores monetários são `BigDecimal` com escala fixa empacotados num Value Object. (Fica o aviso).

Feito por alguém de TI pra aguentar a vida real. Bom código!
