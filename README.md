# Terraform to CloudSim – Infrastructure Simulation Prototype

## Overview

This project demonstrates a prototype pipeline that transforms Terraform-based cloud infrastructure definitions into a simulation model using CloudSim Plus.

The goal is to evaluate how infrastructure configurations (e.g., number of virtual machines, presence of database and storage services) influence application performance under different workloads.

The project is based on a simplified STACKIT BottleTube scenario and focuses on parsing Terraform configurations, mapping infrastructure resources to an internal model, simulating workloads, and exporting performance metrics.

---

## Motivation

Cloud infrastructures defined via Terraform are usually deployed and tested in real environments.

This project explores an alternative approach:

Can infrastructure behavior be simulated before deployment?

By transforming Terraform configurations into a simulation model, it becomes possible to compare different architectures, evaluate performance under varying workloads, and identify potential bottlenecks early in the design phase.

---

## Architecture

The system follows a pipeline-based architecture with four main stages:

1. Terraform Parsing  
   All Terraform (.tf) files are loaded and analyzed. Variables, locals, and resource definitions are extracted.

2. Context Resolution  
   References such as var.*, local.*, and ${...} interpolations are resolved into concrete values.

3. Infrastructure Mapping  
   Terraform resources are transformed into an internal infrastructure model consisting of compute nodes, database nodes, storage nodes, and load balancers.

4. Simulation (CloudSim Plus)  
   The infrastructure model is translated into CloudSim entities such as virtual machines and cloudlets. Workloads are executed and performance metrics are collected.

---

## Project Structure

src/main/java/de/thb/cloudsim/
- app/           → Application entry point
- terraform/     → Terraform parsing and value resolution
- mapping/       → Transformation to infrastructure model
- model/         → Infrastructure domain model
- simulation/    → CloudSim integration
- workload/      → Workload definitions
- scenario/      → Scenario execution logic
- reporting/     → CSV export and reporting

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

## Supported Terraform Features

Supported resources:
- stackit_server (mapped to compute nodes)
- stackit_postgresqlflex_instance (mapped to database nodes)
- stackit_objectstorage_bucket (mapped to storage nodes)
- stackit_loadbalancer (mapped to load balancers)

Supported features:
- variable defaults
- locals
- simple interpolation (var.*, local.*, ${...})

---

## Limitations

This project is a research prototype and does not implement a full Terraform evaluation engine.

Not supported:
- modules
- count / for_each
- dynamic blocks
- full nested block semantics
- complete STACKIT provider support

Simplifications:
- database usage is modeled as additional request latency
- object storage is modeled as additional request latency
- load balancing is abstracted via CloudSim scheduling
- network latency is not explicitly modeled

Unsupported resources are ignored and reported in the import report.

---

## Workload Model

The system simulates a simplified BottleTube-like application with different request types:

- STATIC_ASSET
- GALLERY_VIEW
- IMAGE_DETAIL
- IMAGE_UPLOAD

Each request type is modeled as a CloudSim Cloudlet with:
- base processing length
- optional penalties for database and storage usage

Three workload profiles are defined:
- Light
- Medium
- Heavy

---

## Simulation Scenarios

The following scenarios are executed:

- 1 VM – Light
- 2 VM – Light
- 1 VM – Medium
- 2 VM – Medium
- 1 VM – Heavy
- 2 VM – Heavy

Each scenario compares infrastructure size and workload intensity.

---

## Example Results

1 VM – Light:    Avg = 8.665   Max = 12.378  
2 VM – Light:    Avg = 5.495   Max = 8.710  

1 VM – Medium:   Avg = 25.143  Max = 34.439  
2 VM – Medium:   Avg = 13.007  Max = 19.907  

1 VM – Heavy:    Avg = 52.778  Max = 73.712  
2 VM – Heavy:    Avg = 26.493  Max = 37.003  

Key observation:
Horizontal scaling (increasing the number of virtual machines) significantly reduces processing time across all workload levels.

---

## How to Run

Requirements:
- Java 21
- Maven

Run the application:

mvn clean compile  
mvn exec:java -Dexec.mainClass="de.thb.cloudsim.app.App"

---

## Input Data

Terraform files are located in:

terraform-testdata/bottletube-mvp/

These files serve as input for parsing and simulation.

Note:
Not all Terraform resources are supported. Unsupported resources are ignored and listed in the import report.

---

## Output

Simulation results are exported as CSV files:

results/simulation-results.csv  
results/capability-report.csv  

---

## Key Contribution

This project provides:

- a prototype pipeline from Terraform to simulation
- a mapping approach for cloud infrastructure to CloudSim
- a framework for comparing infrastructure architectures under different workloads

---

## Future Work

- support for additional Terraform features (modules, loops)
- more accurate database and network modeling
- integration with real cloud metrics
- automated scenario generation

---

## Author

Laurin Krüger  
Master Project – Technische Hochschule Brandenburg

## Architektur

Terraform Files (.tf)
        │
        ▼
TerraformLoader
        │
        ▼
TerraformContextParser
        │
        ▼
TerraformResourceParser
        │
        ▼
TerraformValueResolver
        │
        ▼
TerraformToInfrastructureMapper
        │
        ▼
InfrastructureModel
        │
        ▼
InfrastructureToCloudSim
        │
        ▼
CloudSim Simulation
        │
        ▼
SimulationSummary
        │
        ▼
ResultExporter (CSV)
