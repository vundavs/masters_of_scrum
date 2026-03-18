# Masters of Scrum — INF2-SEPP Coursework 3

## Setup

### Prerequisites
- Java JDK 17
- IntelliJ IDEA Community Edition
- Checkstyle-IDEA plugin (Preferences → Plugins → search "Checkstyle-IDEA")

### Getting started
1. Clone the repo: `git clone https://github.com/vundavs/masters_of_scrum.git`
2. Open in IntelliJ: File → Open → select the cloned folder
3. IntelliJ will detect the `pom.xml` — click **Load Maven Project** when prompted
4. Dependencies (JUnit 5, Mockito, Checkstyle) will download automatically

### Checkstyle setup
- Preferences → Plugins → search "Checkstyle-IDEA" → Install → restart IntelliJ
- Style violations will be flagged automatically when you build
- Optional: Preferences → Tools → Checkstyle → add Google Checks for live inline warnings

---

## Project structure
```
src/
├── main/java/
│   ├── model/          ← data classes (User, Event, Booking etc.)
│   ├── controller/     ← EventsApp.java (all use cases)
│   └── external/       ← provided mock files, do not modify
└── test/java/          ← all unit and system tests
```

---

## Git practices

- **Never commit directly to `main`** — always create your own branch first:
```bash
  git checkout -b feature/your-feature-name
```
- **Pull before you start working** every session:
```bash
  git pull origin main
```
- **Commit often** with clear messages:
```bash
  git commit -m "add logIn method to EventsApp"
```
- **Run all tests before pushing** — never push broken code to main
- If you change a shared method signature in `EventsApp` or `User`, tell the group first

---

## Code rules

- Follow the [Google Java Style Guide](https://google.github.io/styleguide/javaguide.html)
- 100 character line limit, no tabs (use spaces)
- Every public method must have a Javadoc comment
- Class names in PascalCase, methods and variables in camelCase
- Do not modify any files in `external/`
- Keep the Faculty Members code completely separate — do not put it anywhere in `src/`

---

## Deadline
**Monday 6 April 2026, 12:00 noon**
