-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema ayaRestaurant
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema ayaRestaurant
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `ayaRestaurant` DEFAULT CHARACTER SET utf8 ;
USE `ayaRestaurant` ;

-- -----------------------------------------------------
-- Table `ayaRestaurant`.`usuarios`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`usuarios` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  `direccion` VARCHAR(45) NOT NULL,
  `telefono` INT NOT NULL,
  `rol` VARCHAR(45) NOT NULL DEFAULT 'cliente',
  `baja` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`platos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`platos` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `nombre` VARCHAR(45) NOT NULL,
  `descripcion` VARCHAR(300) NOT NULL,
  `ingredientes` VARCHAR(300) NOT NULL,
  `foto` VARCHAR(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`pedidos`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`pedidos` (
  `id` INT NOT NULL,
  `usuario_id` INT NOT NULL,
  `estado` VARCHAR(45) NOT NULL DEFAULT 'creado',
  PRIMARY KEY (`id`, `usuario_id`),
  INDEX `fk_pedidos_usuarios_idx` (`usuario_id` ASC),
  CONSTRAINT `fk_pedidos_usuarios`
    FOREIGN KEY (`usuario_id`)
    REFERENCES `ayaRestaurant`.`usuarios` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`categoria`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`categoria` (
  `id` INT NOT NULL,
  `nombre` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`categorias_del_plato`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`categorias_del_plato` (
  `categoria_id` INT NOT NULL,
  `platos_id` INT NOT NULL,
  PRIMARY KEY (`categoria_id`, `platos_id`),
  INDEX `fk_categoria_has_platos_platos1_idx` (`platos_id` ASC),
  INDEX `fk_categoria_has_platos_categoria1_idx` (`categoria_id` ASC),
  CONSTRAINT `fk_categoria_has_platos_categoria1`
    FOREIGN KEY (`categoria_id`)
    REFERENCES `ayaRestaurant`.`categoria` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_categoria_has_platos_platos1`
    FOREIGN KEY (`platos_id`)
    REFERENCES `ayaRestaurant`.`platos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`articulos_del_pedido`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`articulos_del_pedido` (
  `pedido_id` INT NOT NULL,
  `plato_id` INT NOT NULL,
  PRIMARY KEY (`pedido_id`, `plato_id`),
  INDEX `fk_pedidos_has_platos_platos1_idx` (`plato_id` ASC),
  INDEX `fk_pedidos_has_platos_pedidos1_idx` (`pedido_id` ASC),
  CONSTRAINT `fk_pedidos_has_platos_pedidos1`
    FOREIGN KEY (`pedido_id`)
    REFERENCES `ayaRestaurant`.`pedidos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_has_platos_platos1`
    FOREIGN KEY (`plato_id`)
    REFERENCES `ayaRestaurant`.`platos` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
