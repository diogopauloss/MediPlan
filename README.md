<p align="center">
  <img width="198" height="160" alt="Logo MediPlan" src="https://github.com/user-attachments/assets/5613b379-71e1-4b41-b927-3f6a0b8f0132" />
</p>

<h1 align="center">MediPlan</h1>

## 1. Introdução
O **MediPlan** é uma solução móvel desenvolvida em ambiente Android que visa simplificar a gestão diária de medicamentos através de uma interface intuitiva e conectada. A aplicação funciona como um assistente pessoal onde o utilizador começa por realizar a sua autenticação segura para aceder a um ecossistema personalizado de saúde.

---

## 2. Desenvolvimento Técnico
No coração da aplicação, o utilizador pode explorar um catálogo completo de medicamentos através de um sistema de pesquisa em tempo real, permitindo identificar dosagens e formas farmacêuticas específicas. A principal utilidade do sistema reside na criação de planos de medicação individuais, onde é possível associar cada fármaco a horários específicos do dia, como o pequeno-almoço ou o jantar, garantindo que o utilizador tem sempre uma lista organizada das suas tomas na página principal.

Toda esta estrutura é suportada por uma arquitetura robusta baseada nos seguintes pilares técnicos:

* **Comunicação API:** Utilização do **Retrofit 2** para a comunicação com um servidor externo, garantindo que os dados são sincronizados de forma segura e assíncrona.
* **Gestão de Sessão:** A sessão do utilizador é mantida localmente (SharedPreferences) para uma experiência contínua, fluida e sem interrupções.
* **Separação de Responsabilidades:** Implementação de uma camada de **Managers** (`PlanManager`, `MedicationManager`) que isola a lógica de negócio e os pedidos de rede das Activities.



---

## 3. Conclusão
O MediPlan transforma a complexidade da gestão terapêutica numa experiência digital simplificada. Ao unir uma arquitetura sólida a uma interface centrada no utilizador, o projeto cumpre o seu objetivo de promover a adesão ao tratamento e organizar a rotina de saúde de forma eficiente e moderna.


---
### Autores:
Afonso Moisão 25912 <p>
Diogo Paulos 26600 <p>
Ricardo Manique 25907
