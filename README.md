# Terraform zu CloudSim – Prototyp zur Infrastruktursimulation

## Überblick

Dieses Projekt demonstriert eine prototypische Pipeline, die Terraform-basierte Cloud-Infrastrukturdefinitionen in ein Simulationsmodell mit CloudSim Plus überführt.

Ziel ist es, zu untersuchen, wie sich unterschiedliche Infrastrukturkonfigurationen (z. B. Anzahl von VMs, Vorhandensein von Datenbank- und Storage-Diensten) auf die Performance einer Anwendung unter verschiedenen Lastprofilen auswirken.

Als Beispiel dient ein vereinfachtes STACKIT BottleTube-Szenario.

---

## Motivation

Cloud-Infrastrukturen werden üblicherweise mit Terraform definiert und anschließend direkt in realen Umgebungen bereitgestellt.

Dieses Projekt verfolgt einen alternativen Ansatz:

Kann das Verhalten einer Infrastruktur bereits vor der tatsächlichen Bereitstellung simuliert werden?

Durch die Transformation von Terraform-Konfigurationen in ein Simulationsmodell lassen sich:

- verschiedene Architekturen vergleichen (z. B. 1 VM vs. 2 VMs)
- Performance unter unterschiedlichen Lasten analysieren
- potenzielle Engpässe frühzeitig erkennen

---

## Architektur

Das System ist als Pipeline aufgebaut und besteht aus vier Hauptphasen:

1. **Terraform Parsing**  
   Einlesen aller Terraform-Dateien und Extraktion von:
   - Variablen
   - Locals
   - Ressourcen

2. **Kontextauflösung**  
   Auflösung von Referenzen wie:
   - `var.*`
   - `local.*`
   - `${...}` (Interpolation)

3. **Mapping auf Infrastrukturmodell**  
   Transformation der Terraform-Ressourcen in interne Modellobjekte:
   - Compute Nodes
   - Database Nodes
   - Storage Nodes
   - Load Balancer

4. **Simulation mit CloudSim Plus**  
   Überführung des Infrastrukturmodells in:
   - VMs
   - Cloudlets (Requests)

   Anschließend Durchführung der Simulation und Auswertung der Ergebnisse.

---

## Projektstruktur

src/main/java/de/thb/cloudsim/
- app/           → Einstiegspunkt (Main-Klasse)
- terraform/     → Parsing & Auflösung von Terraform
- mapping/       → Transformation in Infrastrukturmodell
- model/         → Domänenmodell (Nodes etc.)
- simulation/    → CloudSim-Anbindung
- workload/      → Lastprofile
- scenario/      → Szenarioausführung
- reporting/     → CSV-Export

terraform-testdata/
- bottletube-mvp/
  - main.tf
  - variables.tf
  - provider.tf
  - outputs.tf

results/
- simulation-results.csv
- capability-report.csv

---

## Unterstützte Terraform-Features

### Unterstützte Ressourcen

- `stackit_server` → Compute Nodes
- `stackit_postgresqlflex_instance` → Datenbankinstanzen
- `stackit_objectstorage_bucket` → Storage
- `stackit_loadbalancer` → Load Balancer

### Unterstützte Funktionen

- Variablen mit Default-Werten
- Locals
- einfache Interpolation (`var.*`, `local.*`, `${...}`)

---

## Einschränkungen

Dieses Projekt ist ein Forschungsprototyp und keine vollständige Terraform-Engine.

### Nicht unterstützt

- Module
- `count` / `for_each`
- dynamische Blöcke
- komplexe verschachtelte Strukturen
- vollständiger STACKIT-Provider-Support

### Vereinfachungen

- Datenbank → als zusätzliche Latenz modelliert
- Object Storage → als zusätzliche Latenz modelliert
- Load Balancer → abstrahiert über Scheduling
- Netzwerk → nicht modelliert

Nicht unterstützte Ressourcen werden ignoriert und im Import-Report dokumentiert.

---

## Workload-Modell

Es wird eine vereinfachte BottleTube-Anwendung simuliert mit folgenden Request-Typen:

- STATIC_ASSET
- GALLERY_VIEW
- IMAGE_DETAIL
- IMAGE_UPLOAD

Jeder Request wird als Cloudlet modelliert mit:

- Basis-Rechenaufwand
- optionalen Zusatzkosten für:
  - Datenbankzugriffe
  - Storage-Zugriffe

### Lastprofile

- Light
- Medium
- Heavy

---

## Simulationsszenarien

Es werden folgende Szenarien untersucht:

- 1 VM – Light
- 2 VM – Light
- 1 VM – Medium
- 2 VM – Medium
- 1 VM – Heavy
- 2 VM – Heavy

---

## Beispielergebnisse

1 VM – Light:    Avg = 8.665   Max = 12.378  
2 VM – Light:    Avg = 5.495   Max = 8.710  

1 VM – Medium:   Avg = 25.143  Max = 34.439  
2 VM – Medium:   Avg = 13.007  Max = 19.907  

1 VM – Heavy:    Avg = 52.778  Max = 73.712  
2 VM – Heavy:    Avg = 26.493  Max = 37.003  

### Zentrale Erkenntnis

Horizontale Skalierung (mehr VMs) reduziert die Bearbeitungszeiten signifikant über alle Laststufen hinweg.

---

## Ausführung

### Voraussetzungen

- Java 21
- Maven

### Start

mvn clean compile  
mvn exec:java -Dexec.mainClass="de.thb.cloudsim.app.App"

---

## Eingabedaten

Terraform-Dateien befinden sich in:

terraform-testdata/bottletube-mvp/

Diese dienen als Input für Parsing und Simulation.

Hinweis:
Nicht alle Terraform-Ressourcen werden unterstützt.

---

## Ausgabe

Die Simulation erzeugt CSV-Dateien:

results/simulation-results.csv  
results/capability-report.csv  

---

## Beitrag des Projekts

Dieses Projekt liefert:

- eine Pipeline von Terraform zu Simulation
- ein Mapping-Konzept für Cloud-Infrastruktur → CloudSim
- ein Framework zur Bewertung von Architekturentscheidungen

---

## Zukünftige Arbeiten

- Unterstützung weiterer Terraform-Features
- genauere Modellierung von Datenbank- und Netzwerkverhalten
- Integration realer Cloud-Metriken
- automatische Generierung von Szenarien

---

## Autor

Laurin Krüger  
Masterprojekt – Technische Hochschule Brandenburg
