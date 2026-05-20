locals {
  app_name = var.name
}

resource "aws_instance" "app_1" {
  name          = "${local.app_name}-app-1"
  ami           = var.ami_id
  instance_type = "t3.small"
  region        = var.region
}

resource "aws_instance" "app_2" {
  name          = "${local.app_name}-app-2"
  ami           = var.ami_id
  instance_type = "t3.small"
  region        = var.region
}

resource "aws_db_instance" "db" {
  identifier        = "${local.app_name}-postgres"
  engine            = "postgres"
  instance_class    = "db.t3.micro"
  allocated_storage = 20
  db_name           = var.db_name
  username          = var.db_user
  password          = var.db_password
}

resource "aws_s3_bucket" "media" {
  bucket = "${local.app_name}-media"
}

resource "aws_lb" "web" {
  name               = "${local.app_name}-lb"
  load_balancer_type = "application"
}