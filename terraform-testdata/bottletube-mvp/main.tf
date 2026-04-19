# --------------------------------------------------
# LOCALS BLOCK
# --------------------------------------------------

locals {
  # Definiert eine lokale Variable "app_name"
  # → wird aus der Terraform-Variable "var.name" übernommen
  # → dient zur zentralen Namensbildung für alle Ressourcen
  # → Vorteil: konsistente Naming-Strategie im gesamten Setup
  app_name = var.name
}

# --------------------------------------------------
# SSH Key
# --------------------------------------------------

resource "stackit_key_pair" "default" {
  # Name des SSH-Schlüssels
  # → wird dynamisch zusammengesetzt: <app_name>-key
  # → Beispiel: bottletube-key
  name = "${local.app_name}-key"

  # Öffentlicher SSH-Key (kommt von außen über Variable)
  # → wird genutzt, um Zugriff auf VMs zu ermöglichen
  public_key = var.ssh_public_key
}

# --------------------------------------------------
# Compute: Bottle App VM
# --------------------------------------------------

# Datenquelle (kein Resource!)
# → holt ein bestehendes Image vom Provider (STACKIT)
data "stackit_server_image" "ubuntu" {
  # gewünschtes OS-Image
  # → hier: Ubuntu 24.04 LTS
  name = "Ubuntu 24.04 LTS"
}

# Erste VM
resource "stackit_server" "app_1" {
  # Name der VM
  # → basiert auf app_name + Suffix
  name = "${local.app_name}-app-1"

  # Region (z. B. eu01)
  region = var.region

  # Flavor = Hardware-Konfiguration (CPU, RAM etc.)
  flavor = "g1.2"

  # Referenz auf das oben geladene Image
  # → data.source.attribute
  image_id = data.stackit_server_image.ubuntu.id

  # Referenz auf den SSH Key
  key_pair = stackit_key_pair.default.name
}

# Zweite VM (identisch, nur anderer Name)
resource "stackit_server" "app_2" {
  name     = "${local.app_name}-app-2"
  region   = var.region
  flavor   = "g1.2"
  image_id = data.stackit_server_image.ubuntu.id
  key_pair = stackit_key_pair.default.name
}

# --------------------------------------------------
# Database: PostgreSQL
# --------------------------------------------------

resource "stackit_postgresqlflex_instance" "db" {
  # Name der DB-Instanz
  name = "${local.app_name}-pg"

  # Region
  region = var.region

  # Instanz-Typ (single = keine Replikation)
  instance_type = "single"

  # Größe / Leistungsklasse
  flavor = "small"

  # Storage-Typ
  storage_class = "standard"

  # Festplattengröße (GB)
  disk_size = 20
}

# Logische Datenbank innerhalb der Instanz
resource "stackit_postgresqlflex_database" "app" {
  # Referenz auf DB-Instanz
  instance_id = stackit_postgresqlflex_instance.db.instance_id

  # Name der DB
  name = var.db_name
}

# Datenbank-User
resource "stackit_postgresqlflex_user" "app" {
  # Referenz auf DB-Instanz
  instance_id = stackit_postgresqlflex_instance.db.instance_id

  # Username + Passwort aus Variablen
  username = var.db_user
  password = var.db_password
}

# --------------------------------------------------
# Object Storage
# --------------------------------------------------

# Object Storage Instanz (ähnlich wie S3 Service)
resource "stackit_objectstorage_instance" "assets" {
  # Name der Storage-Instanz
  name = "${local.app_name}-obj"

  # Region
  region = var.region
}

# Bucket innerhalb der Storage-Instanz
resource "stackit_objectstorage_bucket" "media" {
  # Referenz zur Storage-Instanz
  instance_id = stackit_objectstorage_instance.assets.id

  # Bucket-Name
  name = "${local.app_name}-media"
}

# "Virtueller Ordner" für statische Dateien
resource "stackit_objectstorage_object" "static_prefix" {
  # Ziel-Bucket
  bucket = stackit_objectstorage_bucket.media.name

  # Prefix / Key (Pfad)
  key = "static/"

  # leerer Inhalt → dient nur als Ordnerstruktur
  content = ""
}

# "Virtueller Ordner" für User Uploads
resource "stackit_objectstorage_object" "uploads_prefix" {
  bucket  = stackit_objectstorage_bucket.media.name
  key     = "user_uploads/"
  content = ""
}

# --------------------------------------------------
# Load Balancer
# --------------------------------------------------

resource "stackit_loadbalancer" "web" {
  # Name des Load Balancers
  name = "${local.app_name}-lb"

  # Region
  region = var.region

  # Typ: application (Layer 7)
  # → verteilt HTTP/HTTPS Traffic auf VMs
  type = "application"
}