# --------------------------------------------------
# VARIABLES
# --------------------------------------------------
# Variablen sind Eingabeparameter für Terraform
# → ermöglichen flexible, wiederverwendbare Infrastruktur
# → können über CLI, .tfvars oder Umgebungsvariablen gesetzt werden

# Projekt-ID im STACKIT Account
variable "project_id" {
  # Datentyp: String
  # → Pflichtvariable (kein Default vorhanden)
  # → muss beim Ausführen angegeben werden
  type = string
}

# Region für die Infrastruktur
variable "region" {
  type = string

  # Default-Wert → wird verwendet, wenn nichts explizit gesetzt wird
  # → hier: eu01 (z. B. Europa Region)
  default = "eu01"
}

# Basisname der Anwendung
variable "name" {
  type = string

  # Wird für Naming vieler Ressourcen genutzt (über locals)
  # → sorgt für konsistente Namen
  default = "bottletube"
}

# Name der Datenbank
variable "db_name" {
  type = string

  # Default DB-Name
  default = "bottletube"
}

# Benutzername für die Datenbank
variable "db_user" {
  type = string

  # Default DB-User
  default = "bottletube"
}

# Passwort für die Datenbank
variable "db_password" {
  type = string

  # sensitive = true
  # → verhindert, dass der Wert in Logs oder Outputs angezeigt wird
  # → wichtig für Security!
  sensitive = true
}

# Öffentlicher SSH-Key für Zugriff auf VMs
variable "ssh_public_key" {
  type = string

  # Kein Default → muss vom Nutzer bereitgestellt werden
  # → wird in stackit_key_pair verwendet
}