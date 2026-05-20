variable "region" {
  type    = string
  default = "eu-central-1"
}

variable "name" {
  type    = string
  default = "bottletube"
}

variable "ami_id" {
  type    = string
  default = "ami-placeholder"
}

variable "db_name" {
  type    = string
  default = "bottletube"
}

variable "db_user" {
  type    = string
  default = "bottletube"
}

variable "db_password" {
  type      = string
  sensitive = true
  default   = "placeholder"
}