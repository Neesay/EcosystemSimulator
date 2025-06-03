# Ecosystem Simulator

A Java-based GUI ecosystem simulation of predator-prey interactions with inheritance-driven species behavior, gene-based evolution mechanics, and real-time visualization. Developed as part of a coursework project.

Includes:

* Predator-prey ecosystem (Deer, Coyote, Grass)
* Genetic traits (mutation, inheritance via `Gene.java`)
* Real-time visual output via Swing
* Statistics tracking (via `FieldStats.java`)
* Object-Oriented Programming (OOP) principles throughout
* PDF report outlining design and decisions

---

## Table of Contents

1. [Installation](#installation)
2. [Usage](#usage)
3. [Contributing](#contributing)
4. [License](#license)

---

## Installation

> *Tested on Windows 10/11 and macOS Sonoma using Java 17.*

### 1  Clone the repo

```bash
https://github.com/your-username/PreySimulator.git
cd PreySimulatorUlvisAndYaseen
```

### 2  Compile the project

```bash
javac *.java
```

### 3  Run the simulation

```bash
java SimulatorView
```

You should see a Swing GUI window visualising the simulation in real time.

---

## Usage

### Features:

* **GUI Simulation**: Click `Run` to watch interactions in real-time.
* **Species**: Deer eat grass; Coyotes hunt deer and etc.
* **Genetics**: Attributes (e.g., energy, speed) evolve each generation via mutation and inheritance.
* **Pause & Rerun**: Use GUI controls to step through or restart.

### Example Workflow:

1. Launch the app: `java SimulatorView`
2. Observe population changes, hunting behavior, and ecological balance.
3. Optionally modify gene parameters in `Gene.java` for experimentation.

---

## Report

📄 The full technical breakdown, design rationale, and testing evaluation can be found in:

[`EcosystemSimulator Report.pdf`](./EcosystemSimulator%20Report.pdf)

---

## Contributing

1. Fork the repo
2. `git checkout -b feature/improvement`
3. Commit: `git commit -am "✨ Improve gene mutation logic"`
4. Push: `git push origin feature/improvement`
5. Open a **Pull Request**

---

## License

MIT. See [`LICENSE`](LICENSE).

---

> *Nature is beautiful—and now, programmable.*
