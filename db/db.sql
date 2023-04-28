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
-- Table `ayaRestaurant`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`users` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NOT NULL,
  `name` VARCHAR(45) NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `phone` INT NOT NULL,
  `role` INT NOT NULL DEFAULT 0,
  `salt` VARCHAR(45) NOT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NULL,
  `deletedAt` DATETIME NULL,
  `isDeleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `salt_UNIQUE` (`salt` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`dishes`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`dishes` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `description` VARCHAR(300) NOT NULL,
  `ingredients` VARCHAR(300) NOT NULL,
  `price` DECIMAL NOT NULL,
  `photo` VARCHAR(100) NOT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NULL,
  `deletedAt` DATETIME NULL,
  `isDeleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`orders`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`orders` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `userId` INT NOT NULL,
  `address` VARCHAR(45) NOT NULL,
  `state` VARCHAR(45) NOT NULL DEFAULT 'created',
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NULL,
  `deletedAt` DATETIME NULL,
  `isDeleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`, `userId`),
  INDEX `fk_pedidos_usuarios_idx` (`userId` ASC),
  CONSTRAINT `fk_pedidos_usuarios`
    FOREIGN KEY (`userId`)
    REFERENCES `ayaRestaurant`.`users` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`categories`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`categories` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NOT NULL,
  `createdAt` DATETIME NOT NULL,
  `updatedAt` DATETIME NULL,
  `deletedAt` DATETIME NULL,
  `isDeleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`dishes_categorie`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`dishes_categorie` (
  `categorieId` INT NOT NULL,
  `dishId` INT NOT NULL,
  `deletedAt` DATETIME NULL,
  `isDeleted` TINYINT NOT NULL DEFAULT 0,
  PRIMARY KEY (`categorieId`, `dishId`),
  INDEX `fk_categoria_has_platos_platos1_idx` (`dishId` ASC),
  INDEX `fk_categoria_has_platos_categoria1_idx` (`categorieId` ASC),
  CONSTRAINT `fk_categoria_has_platos_categoria1`
    FOREIGN KEY (`categorieId`)
    REFERENCES `ayaRestaurant`.`categories` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_categoria_has_platos_platos1`
    FOREIGN KEY (`dishId`)
    REFERENCES `ayaRestaurant`.`dishes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `ayaRestaurant`.`order_items`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `ayaRestaurant`.`order_items` (
  `orderId` INT NOT NULL,
  `dishId` INT NOT NULL,
  PRIMARY KEY (`orderId`, `dishId`),
  INDEX `fk_pedidos_has_platos_platos1_idx` (`dishId` ASC),
  INDEX `fk_pedidos_has_platos_pedidos1_idx` (`orderId` ASC),
  CONSTRAINT `fk_pedidos_has_platos_pedidos1`
    FOREIGN KEY (`orderId`)
    REFERENCES `ayaRestaurant`.`orders` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_pedidos_has_platos_platos1`
    FOREIGN KEY (`dishId`)
    REFERENCES `ayaRestaurant`.`dishes` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
