# --------------------------------------------------
# OUTPUTS
# --------------------------------------------------
# Outputs werden in Terraform verwendet, um nach dem Apply
# wichtige Informationen aus der Infrastruktur zurückzugeben.
# → z. B. für Logging, Weiterverarbeitung oder Integration in andere Systeme

# Name der ersten App-VM
output "app_server_1_name" {
  # Referenz auf Resource:
  # resource_type.resource_name.attribute
  # → stackit_server.app_1.name
  value = stackit_server.app_1.name
}

# Name der zweiten App-VM
output "app_server_2_name" {
  value = stackit_server.app_2.name
}

# Name der PostgreSQL-Instanz
output "postgres_instance_name" {
  # wichtig: das ist die INSTANZ, nicht die DB oder User
  value = stackit_postgresqlflex_instance.db.name
}

# Name des Object Storage Buckets
output "bucket_name" {
  # Bucket, in dem Medien gespeichert werden
  value = stackit_objectstorage_bucket.media.name
}

# Name des Load Balancers
output "loadbalancer_name" {
  value = stackit_loadbalancer.web.name
}