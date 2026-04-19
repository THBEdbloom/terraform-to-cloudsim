# --------------------------------------------------
# TERRAFORM BLOCK
# --------------------------------------------------

terraform {
  # Mindestversion von Terraform
  # → stellt sicher, dass nur kompatible CLI-Versionen genutzt werden
  required_version = ">= 1.6.0"

  # Definition der benötigten Provider
  required_providers {
    stackit = {
      # Quelle des Providers (Registry Namespace)
      # → stackitcloud ist der Anbieter
      source = "stackitcloud/stackit"

      # Versionsconstraint (~> 0.52)
      # → erlaubt z. B. 0.52.x, aber keine 0.53
      # → sorgt für stabile Builds ohne Breaking Changes
      version = "~> 0.52"
    }
  }
}

# --------------------------------------------------
# PROVIDER CONFIGURATION
# --------------------------------------------------

provider "stackit" {
  # Projekt-ID im STACKIT-Account
  # → bestimmt, in welchem Projekt die Ressourcen erstellt werden
  project_id = var.project_id

  # Standard-Region für alle Ressourcen
  # → wird von vielen Ressourcen automatisch übernommen
  region = var.region
}