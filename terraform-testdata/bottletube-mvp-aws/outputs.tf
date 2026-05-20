output "app_server_1_name" {
  value = aws_instance.app_1.name
}

output "app_server_2_name" {
  value = aws_instance.app_2.name
}

output "postgres_instance_name" {
  value = aws_db_instance.db.identifier
}

output "bucket_name" {
  value = aws_s3_bucket.media.bucket
}

output "loadbalancer_name" {
  value = aws_lb.web.name
}