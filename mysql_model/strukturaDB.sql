CREATE DATABASE  IF NOT EXISTS `pomdb` /*!40100 DEFAULT CHARACTER SET utf8 */;
USE `pomdb`;
-- MySQL dump 10.13  Distrib 5.6.23, for Win64 (x86_64)
--
-- Host: localhost    Database: pomdb
-- ------------------------------------------------------
-- Server version	5.5.44-log

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `dilci_terminy`
--

DROP TABLE IF EXISTS `dilci_terminy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `dilci_terminy` (
  `Id_zakazky` int(10) unsigned NOT NULL,
  `dilci_termin` date NOT NULL,
  `pocet_kusu` smallint(5) unsigned NOT NULL,
  `splnen` tinyint(1) unsigned NOT NULL DEFAULT '0',
  PRIMARY KEY (`Id_zakazky`,`dilci_termin`),
  KEY `fk_dilci_terminy_seznam_zakazek1` (`Id_zakazky`),
  CONSTRAINT `fk_dilci_terminy_seznam_zakazek1` FOREIGN KEY (`Id_zakazky`) REFERENCES `seznam_zakazek` (`Id_zakazky`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fyzkusy`
--

DROP TABLE IF EXISTS `fyzkusy`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `fyzkusy` (
  `Id_kusu` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Id_zakazky` int(10) unsigned NOT NULL,
  `Id_modelu_odlito` int(10) unsigned NOT NULL,
  `Datum_liti` date DEFAULT NULL,
  `Cislo_tavby` varchar(10) DEFAULT NULL,
  `Odlito` tinyint(1) NOT NULL DEFAULT '0',
  `Vycisteno` tinyint(1) NOT NULL DEFAULT '0',
  `Zmetek` tinyint(1) NOT NULL DEFAULT '0',
  `Datum_odliti` date DEFAULT NULL,
  `Expedovano` tinyint(1) NOT NULL DEFAULT '0',
  `Cislo_faktury` varchar(19) DEFAULT NULL,
  `Datum_vycisteni` date DEFAULT NULL,
  `Datum_expedice` date DEFAULT NULL,
  `Datum_zadani_zmetku` date DEFAULT NULL,
  `Teplota_liti` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`Id_kusu`),
  KEY `fk_fyzkusy_seznam_zakazek_idx` (`Id_zakazky`),
  KEY `Id_modelu` (`Id_modelu_odlito`),
  CONSTRAINT `fk_fyzkusy_seznam_modelu1` FOREIGN KEY (`Id_modelu_odlito`) REFERENCES `seznam_modelu` (`Id_modelu`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_fyzkusy_seznam_zakazek` FOREIGN KEY (`Id_zakazky`) REFERENCES `seznam_zakazek` (`Id_zakazky`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4893 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seznam_modelu`
--

DROP TABLE IF EXISTS `seznam_modelu`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seznam_modelu` (
  `Id_modelu` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Jmeno_modelu` varchar(30) NOT NULL,
  `Cislo_modelu` varchar(30) NOT NULL,
  `Material` varchar(25) NOT NULL,
  `Material_vlastni` varchar(10) DEFAULT NULL,
  `Hmotnost` decimal(10,2) unsigned NOT NULL,
  `IsOdhadHmot` tinyint(1) NOT NULL DEFAULT '1',
  `Formovna` char(1) DEFAULT NULL,
  `Norma_slevac` decimal(9,1) unsigned DEFAULT NULL,
  `Poznamka_model` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`Id_modelu`),
  KEY `cislo_modelu_index` (`Cislo_modelu`)
) ENGINE=InnoDB AUTO_INCREMENT=729 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `seznam_zakazek`
--

DROP TABLE IF EXISTS `seznam_zakazek`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `seznam_zakazek` (
  `Id_zakazky` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `Id_zakaznika` smallint(5) unsigned NOT NULL,
  `Id_modelu` int(10) unsigned NOT NULL,
  `Cislo_objednavky` varchar(30) NOT NULL,
  `Pocet_kusu` smallint(6) unsigned NOT NULL,
  `Datum_prijeti_zakazky` date NOT NULL,
  `Termin_expedice` date NOT NULL,
  `Paganyrka` varchar(5) DEFAULT NULL,
  `Cena` decimal(13,3) unsigned NOT NULL,
  `IsCZK` tinyint(1) NOT NULL,
  `IsZaKus` tinyint(1) NOT NULL,
  `KurzEUnaCZK` decimal(8,4) unsigned DEFAULT NULL,
  `Poznamka` varchar(45) DEFAULT NULL,
  `Uzavreno` tinyint(1) NOT NULL DEFAULT '0',
  `Presna_cena_za_kus` decimal(17,6) unsigned NOT NULL DEFAULT '0.000000',
  PRIMARY KEY (`Id_zakazky`),
  KEY `fk_seznam_zakazek_zakaznici1_idx` (`Id_zakaznika`),
  KEY `fk_seznam_zakazek_seznam_modelu1_idx` (`Id_modelu`),
  CONSTRAINT `fk_seznam_zakazek_seznam_modelu1` FOREIGN KEY (`Id_modelu`) REFERENCES `seznam_modelu` (`Id_modelu`) ON DELETE NO ACTION ON UPDATE CASCADE,
  CONSTRAINT `fk_seznam_zakazek_zakaznici1` FOREIGN KEY (`Id_zakaznika`) REFERENCES `zakaznici` (`Id_zakaznika`) ON DELETE NO ACTION ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=1249 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vady`
--

DROP TABLE IF EXISTS `vady`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vady` (
  `idvady` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `vada` varchar(45) NOT NULL,
  PRIMARY KEY (`idvady`),
  UNIQUE KEY `vada_UNIQUE` (`vada`)
) ENGINE=InnoDB AUTO_INCREMENT=27 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vinici`
--

DROP TABLE IF EXISTS `vinici`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vinici` (
  `Id_vinika` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `Jmeno_vinika` varchar(45) NOT NULL,
  PRIMARY KEY (`Id_vinika`),
  UNIQUE KEY `Jmeno_vinika_UNIQUE` (`Jmeno_vinika`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `zakaznici`
--

DROP TABLE IF EXISTS `zakaznici`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zakaznici` (
  `Id_zakaznika` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `Jmeno_zakaznika` varchar(30) NOT NULL,
  PRIMARY KEY (`Id_zakaznika`),
  UNIQUE KEY `Jmeno_zakaznika_UNIQUE` (`Jmeno_zakaznika`)
) ENGINE=InnoDB AUTO_INCREMENT=46 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `zmetky_vady`
--

DROP TABLE IF EXISTS `zmetky_vady`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `zmetky_vady` (
  `Id_kusu` int(10) unsigned NOT NULL,
  `Id_vinika` smallint(5) unsigned NOT NULL,
  `Id_vady` smallint(5) unsigned NOT NULL,
  PRIMARY KEY (`Id_kusu`,`Id_vinika`,`Id_vady`),
  UNIQUE KEY `Id_kusu_UNIQUE` (`Id_kusu`),
  KEY `fk_zmetky_vady_fyzkusy1_idx` (`Id_kusu`),
  KEY `fk_zmetky_vady_vinici1_idx` (`Id_vinika`),
  KEY `fk_zmetky_vady_vady1_idx` (`Id_vady`),
  CONSTRAINT `fk_zmetky_vady_fyzkusy1` FOREIGN KEY (`Id_kusu`) REFERENCES `fyzkusy` (`Id_kusu`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_zmetky_vady_vinici1` FOREIGN KEY (`Id_vinika`) REFERENCES `vinici` (`Id_vinika`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_zmetky_vady_vady1` FOREIGN KEY (`Id_vady`) REFERENCES `vady` (`idvady`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping routines for database 'pomdb'
--
/*!50003 DROP FUNCTION IF EXISTS `getHmotnostFyzKusu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getHmotnostFyzKusu`(idFyzKusu int(10) unsigned) RETURNS decimal(10,2) unsigned
BEGIN
declare hmot decimal(10,2) unsigned;
declare idModelu int(10) unsigned;
select Id_modelu_odlito from fyzkusy where Id_kusu = idFyzKusu into idModelu;
select  Hmotnost  from seznam_modelu where Id_modelu = idModelu into hmot;
RETURN hmot;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `getNormaSlevaceFyzKusu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `getNormaSlevaceFyzKusu`(idFyzKusu int(10) unsigned) RETURNS decimal(10,2) unsigned
BEGIN
declare norma decimal(10,2) unsigned;
declare idModelu int(10) unsigned;
select Id_modelu_odlito from fyzkusy where Id_kusu = idFyzKusu into idModelu;
select Norma_slevac from seznam_modelu where Id_modelu = idModelu into norma;
RETURN norma;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP FUNCTION IF EXISTS `returnPresnouCenuZaKus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` FUNCTION `returnPresnouCenuZaKus`(idFyzKusu int(10) unsigned) RETURNS decimal(17,6)
BEGIN
declare isCzKPom tinyint(1);
declare isZaKusPom tinyint(1);
declare hmot decimal(10,2) unsigned;
declare kurzEuNaCzkpom decimal(8,4) unsigned;
declare cenaPom decimal(13,3) unsigned;
declare idZakazky int(10) unsigned;


select Id_zakazky from fyzkusy where Id_kusu = idFyzKusu into idZakazky;
select Cena from seznam_zakazek where seznam_zakazek.Id_zakazky = idZakazky into cenaPom;
select IsCZK from seznam_zakazek where seznam_zakazek.Id_zakazky = idZakazky into isCzKPom;
select IsZaKus from seznam_zakazek where seznam_zakazek.Id_zakazky = idZakazky into isZaKusPom;
select KurzEUnaCZK from seznam_zakazek where seznam_zakazek.Id_zakazky = idZakazky into kurzEuNaCzkpom;

if(isCzKPom)then
	if(isZaKusPom)then
		return cenaPom;
	else
		select Hmotnost from seznam_modelu where seznam_modelu.Id_modelu = (select Id_modelu_odlito from fyzkusy where Id_kusu = idFyzKusu) into hmot;
		return cenaPom * hmot;
	end if;
elseif(kurzEuNaCzkpom > 0)then
		if(isZaKusPom)then
			return cenaPom * kurzEuNaCzkpom;
		else
			select Hmotnost from seznam_modelu where seznam_modelu.Id_modelu = (select Id_modelu_odlito from fyzkusy where Id_kusu = idFyzKusu) into hmot;
			return cenaPom * hmot * kurzEuNaCzkpom;
		end if;
end if;
return null;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `generujKusy` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `generujKusy`(Idzakazky int)
BEGIN
DECLARE pocetNovychKusu INT;
declare pocetNeZmetku int;
declare idModelu int(10) unsigned;

select Id_modelu from seznam_zakazek where Id_zakazky = Idzakazky into idModelu;

select count(*) from fyzkusy where fyzkusy.Id_zakazky = Idzakazky and Zmetek = false into pocetNeZmetku;

select 
    Pocet_kusu
from
    pomdb.seznam_zakazek
where
    Id_zakazky = Idzakazky into pocetNovychKusu;
    
set pocetNovychKusu = pocetNovychKusu - pocetNeZmetku;
 WHILE pocetNovychKusu > 0 DO
    INSERT INTO `pomdb`.`fyzkusy`
		(`Id_zakazky`, `Id_modelu_odlito`)
	values(Idzakazky, idModelu);

    SET pocetNovychKusu = pocetNovychKusu - 1;
  END WHILE;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `kapacitniPropocet` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `kapacitniPropocet`(prvniDenVTydnu date, formovna char)
BEGIN
declare po date;
declare ut date;
declare st date;
declare ct date;
declare pa date;
declare odecistDnu int;
SET lc_time_names = 'cs_CZ';
set po = prvniDenVTydnu;

set odecistDnu = 2 - dayofweek(prvniDenVTydnu);
set po = date_add(po,INTERVAL odecistDnu day);
set ut = date_add(po,INTERVAL 1 day);
set st = date_add(ut,INTERVAL 1 day);
set ct = date_add(st,INTERVAL 1 day);
set pa = date_add(ct,INTERVAL 1 day);



select po as datum, sum(Norma_slevac) as 'Norma celkem', count(*) as 'počet kusů' from (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.fyzkusy on (seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
 where Datum_liti = po and seznam_modelu.Formovna = formovna
union all
select ut, sum(Norma_slevac), count(*) from (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.fyzkusy on (seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
 where Datum_liti = ut and seznam_modelu.Formovna = formovna
union all
select st, sum(Norma_slevac), count(*) from (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.fyzkusy on (seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
 where Datum_liti = st and seznam_modelu.Formovna = formovna
union all
select ct, sum(Norma_slevac), count(*) from (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.fyzkusy on (seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
 where Datum_liti = ct and seznam_modelu.Formovna = formovna
union all
select pa, sum(Norma_slevac), count(*) from (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.fyzkusy on (seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
 where Datum_liti = pa and seznam_modelu.Formovna = formovna
union all
select 
'celkem',
sum((select sum(Norma_slevac) from (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.fyzkusy on (seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
 where Datum_liti between po and pa and seznam_modelu.Formovna = formovna)),
sum((select count(*) from (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.fyzkusy on (seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
 where Datum_liti between po and pa and seznam_modelu.Formovna = formovna));
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `kontrolaPresneCenyZaKusCursor` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `kontrolaPresneCenyZaKusCursor`(idModelu int unsigned)
BEGIN
  DECLARE done INT DEFAULT FALSE;
  DECLARE idZakazky int unsigned;
  DECLARE cur CURSOR FOR SELECT Id_zakazky FROM seznam_zakazek where Id_modelu = idModelu;
  DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

  OPEN cur;

  read_loop: LOOP
    FETCH cur INTO idZakazky;
    IF done THEN
      LEAVE read_loop;
    END IF;
   -- prace s daty
	CALL `pomdb`.`prepocitatPresnouCenuZaKus`(idZakazky);
  END LOOP;

  CLOSE cur;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `liciPlanPlanovaci` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `liciPlanPlanovaci`(cisloTydne smallint, rok smallint, formovna varchar(1))
BEGIN
declare pomDate1 date;
declare pomDate2 date;

declare po date;
declare ut date;
declare st date;
declare ct date;
declare pa date;

	if(cisloTydne is null or rok is null) then
		set pomDate1 = null, pomDate2 = null;
	else
		set pomDate1 = curdate();
		set pomDate1 = date_add(pomDate1, interval (7 - dayofweek(pomDate1)) day); -- nastavit na ctvrtek 
		if(rok != year(pomDate1))then
			set pomDate1 = date_add(pomDate1, interval (rok - year(pomDate1)) year);
		end if;
		set pomDate1 = date_add(pomDate1, interval (2 - dayofweek(pomDate1)) day); -- mozna bych mel nastavit na ctvrtek a ne pondeli
		set pomDate1 = date_add(pomDate1, interval (cisloTydne - weekofyear(pomDate1)) week);
		set pomDate2 = pomDate1;
		set pomDate1 = date_add(pomDate1, interval (2 - dayofweek(pomDate1)) day); -- nastaveni dnu na pondeli
		set pomDate2 = date_add(pomDate1, interval (8 - dayofweek(pomDate1)) day); -- nastave dnu na patek
		-- select pomDate1, pomDate2;
		-- select weekofyear(pomDate1), dayname(pomDate1), weekofyear(pomDate2), dayname(pomDate2);
	end if;
set po = pomDate1;
set ut = date_add(pomDate1, interval 1 day);
set st = date_add(pomDate1, interval 2 day);
set ct = date_add(pomDate1, interval 3 day);
set pa = date_add(pomDate1, interval 4 day);

SELECT 
	`zakaznici`.`Jmeno_zakaznika` as `Zákazník`,
	seznam_modelu.Jmeno_modelu as `Jméno modelu`,
	seznam_modelu.Cislo_modelu as `Číslo modelu`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = po and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `po`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = ut and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `út`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = st and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `st`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = ct and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `čt`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = pa and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `pá`,
	 count(*) as 'Celk.',
	seznam_zakazek.Poznamka as 'Pozn. zakázka',

	seznam_modelu.Material_vlastni as `Mat. vl.`,
	seznam_modelu.Material as `Materiál`,
	seznam_modelu.Hmotnost as `Hmot.`,
	seznam_zakazek.Termin_expedice as `Termín exp.`,
	seznam_zakazek.Pocet_kusu as `Objed.`,
	(select count(*) from fyzkusy where seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky and fyzkusy.Odlito = true and fyzkusy.Zmetek = false and Datum_odliti  <= pomDate2) as `Odl. - zm`,
	seznam_modelu.Norma_slevac as `Norma`,
	seznam_modelu.Norma_slevac * count(*) as 'Norma celk.',
	seznam_modelu.Poznamka_model as 'Pozn. model'
FROM   
((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where 
	Datum_liti between pomDate1 and pomDate2
	 and
	seznam_modelu.Formovna = formovna
	 and 
	seznam_zakazek.Paganyrka is not null
group by `seznam_zakazek`.`Id_zakazky`
order by `zakaznici`.`Jmeno_zakaznika`,`seznam_modelu`.`Cislo_modelu`,fyzkusy.Datum_liti, `seznam_zakazek`.`Id_zakazky` desc
;

select 'Paganýrka 0-0','Nevydáno:','','','',
	   '','','','','','',
	   '','','','','',
		'','','';

SELECT 
	`zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
	seznam_modelu.Jmeno_modelu as `Jméno modelu`,
	seznam_modelu.Cislo_modelu as `Č. modelu`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = po and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `po`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = ut and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `út`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = st and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `st`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = ct and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `čt`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = pa and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `pá`,
	 count(*) as 'celk.',
	seznam_zakazek.Poznamka as 'Pozn. zakázka',

	seznam_modelu.Material_vlastni as `Mat. vl.`,
	seznam_modelu.Material as `Materál`,
	seznam_modelu.Hmotnost as `Hmot.`,
	seznam_zakazek.Termin_expedice as `Termín exp.`,
	seznam_zakazek.Pocet_kusu as `Objed.`,
	(select count(*) from fyzkusy where seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky and fyzkusy.Odlito = true and fyzkusy.Zmetek = false and Datum_odliti  <= pomDate2) as Odlito,
	-- seznam_zakazek.Cislo_objednavky as `Č. objed.`,
	seznam_modelu.Norma_slevac as `Norma`,
	seznam_modelu.Norma_slevac * count(*) as 'Norma celk.',
	seznam_modelu.Poznamka_model as 'Pozn. model'
FROM   
((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where 
	Datum_liti between pomDate1 and pomDate2
	 and
	seznam_modelu.Formovna = formovna
	 and 
	seznam_zakazek.Paganyrka is null
	-- seznam_zakazek.Uzavreno = false

group by `seznam_zakazek`.`Id_zakazky`
order by `zakaznici`.`Jmeno_zakaznika`,`seznam_modelu`.`Cislo_modelu`,fyzkusy.Datum_liti, `seznam_zakazek`.`Id_zakazky` desc
;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `liciPlanZakl` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `liciPlanZakl`(cisloTydne smallint, rok smallint, formovna varchar(1))
BEGIN
declare pomDate1 date;
declare pomDate2 date;

declare po date;
declare ut date;
declare st date;
declare ct date;
declare pa date;

	if(cisloTydne is null or rok is null) then
		set pomDate1 = null, pomDate2 = null;
	else
		set pomDate1 = curdate();
		set pomDate1 = date_add(pomDate1, interval (7 - dayofweek(pomDate1)) day); -- nastavit na ctvrtek 
		if(rok != year(pomDate1))then
			set pomDate1 = date_add(pomDate1, interval (rok - year(pomDate1)) year);
		end if;
		set pomDate1 = date_add(pomDate1, interval (2 - dayofweek(pomDate1)) day);
		set pomDate1 = date_add(pomDate1, interval (cisloTydne - weekofyear(pomDate1)) week);
		set pomDate2 = pomDate1;
		set pomDate1 = date_add(pomDate1, interval (2 - dayofweek(pomDate1)) day);
		set pomDate2 = date_add(pomDate1, interval (8 - dayofweek(pomDate1)) day);
		-- select pomDate1, pomDate2;
		-- select weekofyear(pomDate1), dayname(pomDate1), weekofyear(pomDate2), dayname(pomDate2);
	end if;
set po = pomDate1;
set ut = date_add(pomDate1, interval 1 day);
set st = date_add(pomDate1, interval 2 day);
set ct = date_add(pomDate1, interval 3 day);
set pa = date_add(pomDate1, interval 4 day);

SELECT 
	`zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka',
	seznam_modelu.Jmeno_modelu as 'Jméno modelu',
	seznam_modelu.Cislo_modelu as 'Číslo modelu',
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = po and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `po`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = ut and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `út`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = st and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `st`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = ct and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `čt`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = pa and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `pá`,
	 count(*) as celkem,
	seznam_zakazek.Poznamka as 'Pozn. zakázka',
	seznam_modelu.Poznamka_model as 'Pozn. model'
FROM   
((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where 
	Datum_liti between pomDate1 and pomDate2
	 and
	seznam_modelu.Formovna = formovna
	 and	
	seznam_zakazek.Paganyrka is not null
	-- seznam_zakazek.Uzavreno = false
group by `seznam_zakazek`.`Id_zakazky`
order by `zakaznici`.`Jmeno_zakaznika`,`seznam_modelu`.`Cislo_modelu`,fyzkusy.Datum_liti, `seznam_zakazek`.`Id_zakazky` desc
;

select 'Paganýrka 0-0','Nevydáno:','','','',
	   '','','','','','';

SELECT 
	`zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka',
	seznam_modelu.Jmeno_modelu as 'Jméno modelu',
	seznam_modelu.Cislo_modelu as 'Číslo modelu',
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = po and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `po`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = ut and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `út`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = st and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `st`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = ct and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `čt`,
	(select count(*) from fyzkusy where fyzkusy.Datum_liti = pa and seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky) as `pá`,
	 count(*) as celkem,
	seznam_zakazek.Poznamka as 'Pozn.',
	seznam_modelu.Poznamka_model as 'Pozn. model'
FROM   
((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where 
	Datum_liti between pomDate1 and pomDate2
	 and
	seznam_modelu.Formovna = formovna
	 and	
	seznam_zakazek.Paganyrka is null
	-- seznam_zakazek.Uzavreno = false
group by `seznam_zakazek`.`Id_zakazky`
order by `zakaznici`.`Jmeno_zakaznika`,`seznam_modelu`.`Cislo_modelu`,fyzkusy.Datum_liti, `seznam_zakazek`.`Id_zakazky` desc
;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `novaZakazka` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `novaZakazka`( idZakaznika smallint(5) unsigned, idModelu int(10) unsigned, cisloObjednavky varchar(30), pocetKusu smallint(6) unsigned, 
datumPrijetiZakazky date, paganyrka varchar(5), cena decimal(13,3) unsigned, isCzK tinyint(1), isZaKus tinyint(1), kurzEuNaCzk decimal(8,4), poznamka varchar(45), datumExpedice date)
BEGIN
declare pocetDuplicit int;
declare presnaCenaZaKus decimal(17,6) unsigned;
declare pom decimal (10,2) unsigned; -- pomocna promena na hmotnost
declare id int;

SELECT 
	count(*)
FROM pomdb.seznam_zakazek
where 
	Id_zakaznika = idZakaznika and
	Id_modelu = idModelu and
	Cislo_objednavky = cisloObjednavky and
	Pocet_kusu = pocetKusu and
	Datum_prijeti_zakazky = datumPrijetiZakazky and
	Termin_expedice = datumExpedice and
	Paganyrka = paganyrka and
	Cena = cena and
	IsCZK = isCzK and
	IsZaKus = isZaKus 
into pocetDuplicit;

if(pocetDuplicit = 0 and pocetKusu <= 1500 and pocetKusu > 0) then

if(isCzK)then
	if(isZaKus)then
		set presnaCenaZaKus = cena;
	else
		select Hmotnost from seznam_modelu where idModelu = seznam_modelu.Id_modelu into pom;
		set presnaCenaZaKus = cena * pom;
	end if;
else if(kurzEuNaCzk > 0)then
		if(isZaKus)then
			set presnaCenaZaKus = cena * kurzEuNaCzk;
		else
			select Hmotnost from seznam_modelu where idModelu = seznam_modelu.Id_modelu into pom;
			set presnaCenaZaKus = cena * pom * kurzEuNaCzk;
		end if;
	else 
		set presnaCenaZaKus = null, cena = null , kurzEuNaCzk = null;
	end if;
end if;

INSERT INTO `pomdb`.`seznam_zakazek`
(`Id_zakaznika`,
`Id_modelu`,
`Cislo_objednavky`,
`Pocet_kusu`,
`Datum_prijeti_zakazky`,
`Paganyrka`,
`Cena`,
`IsCZK`,
`IsZaKus`,
`KurzEUnaCZK`,
`Poznamka`,
Termin_expedice,
Presna_cena_za_kus)
VALUES
(idZakaznika,
idModelu,
cisloObjednavky,
pocetKusu,
datumPrijetiZakazky,
paganyrka,
cena,
isCzK,
isZaKus,
kurzEuNaCzk,
poznamka,
datumExpedice,
presnaCenaZaKus);

set id = LAST_INSERT_ID();
CALL `pomdb`.`generujKusy`(id);
select true, id;
else select false;
end if;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `novyModel` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `novyModel`(jmenoModelu varchar(30), cisloModelu varchar(30), material varchar(25), materialVlastni varchar(10), hmotnost double, isOdhatHmotnost tinyint(1), formovna char(1), norma double, poznamkaModel varchar(50))
BEGIN
INSERT INTO `pomdb`.`seznam_modelu`
(`Jmeno_modelu`,
`Cislo_modelu`,
`Material`,
`Material_vlastni`,
`Hmotnost`,
`IsOdhadHmot`,
`Formovna`,
`Norma_slevac`,
`Poznamka_model`)
VALUES
(jmenoModelu,
cisloModelu,
material,
materialVlastni,
hmotnost,
isOdhatHmotnost,
formovna,
norma,
poznamkaModel);
select LAST_INSERT_ID();
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `novyZakaznik` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `novyZakaznik`(jmeno varchar(30))
BEGIN

if(jmeno is not null and jmeno != '') then
INSERT INTO `pomdb`.`zakaznici`
(`Jmeno_zakaznika`)
VALUES
(jmeno);
select LAST_INSERT_ID();
end if;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `obnovDB` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `obnovDB`()
BEGIN
-- ..chapter/sql-syntax.html#load-data ... pouziju prikaz LOAD DATA infile a klicove slovo local, ktere mi zajisti, že se bude soubor pro
-- ahravani hledat na klientském PC. POzor: 1. chyba muze nastat kdyz neni povoleno jak na klientu tak na 
-- serveru pouziti tohoto slova! 
-- 2. chyba muze nastat pokud na servveru neni dostatek místa pro vytvoření kopie souboru ktery tam z klienta posílam
-- obvykle se pry uděla kopie ve C:/Windwos/TMP ...
-- pokud se chci vyhnout vynechal slovo local a server cte na jeho PC.
-- pro pouziti toho prikazu musim mít File privilege pro nahrani primo na serveru ... pokud
-- pouziji local a client tak toho privilegium mit nemusim
-- potom jeste dalsi prikazy tam jsou pro handlovani chyb.
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `obnovZakazku` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `obnovZakazku`(idZakazky int unsigned)
BEGIN
declare pom int;
select count(*) from seznam_zakazek where seznam_zakazek.Id_zakazky = idZakazky into pom;
if pom = 1 then
	update seznam_zakazek set Uzavreno = false where seznam_zakazek.Id_zakazky = idZakazky;
	select true;
else 
	select false;
end if;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `planovaniRozvrh` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `planovaniRozvrh`(idZakazky int, OUT pocetNeplanovanychKusu int)
BEGIN
-- DECLARE den int;
-- DECLARE do_ date;
SET lc_time_names = 'cs_CZ';
-- set den = dayofweek(od_) - 1;
-- set od_ =  DATE_SUB(od_,INTERVAL den day);
-- set do_ =  DATE_ADD(od_,INTERVAL pocetTydnu week);

select count(*) from fyzkusy where isnull(Datum_liti) and fyzkusy.Id_zakazky = idZakazky into pocetNeplanovanychKusu;
select 
    monthname(Datum_liti),
	WEEKOFYEAR(`fyzkusy`.`Datum_liti`),
	dayofweek(`fyzkusy`.`Datum_liti`),
	DAYNAME(`fyzkusy`.`Datum_liti`),
    `fyzkusy`.`Datum_liti` AS `Datum_liti`,
    count(0) AS `pocet`
from
    `fyzkusy`
where
   -- (Datum_liti between od_ and do_) and 
	 Datum_liti is not null 
		and
	fyzkusy.Id_zakazky = idZakazky
group by `fyzkusy`.`Datum_liti`
order by `fyzkusy`.`Datum_liti`;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `planovaniRozvrhVycisteno` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `planovaniRozvrhVycisteno`(idZakazky int, OUT pocetNeplanovanychKusu int)
BEGIN
-- DECLARE den int;
-- DECLARE do_ date;
-- SET lc_time_names = 'cs_CZ';
-- set den = dayofweek(od_) - 1;
-- set od_ =  DATE_SUB(od_,INTERVAL den day);
-- set do_ =  DATE_ADD(od_,INTERVAL pocetTydnu week);

select count(*) from fyzkusy where isnull(Datum_vycisteni) and fyzkusy.Id_zakazky = idZakazky into pocetNeplanovanychKusu;
select 
	monthname(Datum_vycisteni),
	WEEKOFYEAR(fyzkusy.Datum_vycisteni),
	dayofweek(fyzkusy.Datum_vycisteni),
	DAYNAME(fyzkusy.Datum_vycisteni),
    fyzkusy.Datum_vycisteni AS Datum_vycisteni,
    count(0) AS `pocet`
from
    fyzkusy
where
   -- (Datum_liti between od_ and do_) and 
	 Datum_vycisteni is not null 
		and
	fyzkusy.Id_zakazky = idZakazky
group by `fyzkusy`.`Datum_vycisteni`
order by `fyzkusy`.`Datum_vycisteni`;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `plan_expedice` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `plan_expedice`()
BEGIN
drop temporary table if exists pomTable;
create temporary table pomTable 
	(id_zakazky int unsigned,
	dilciTermin date,
	pocet_ks smallint unsigned
);
-- vyberu prvni tri nesplnene dílčí termíny pro každou zakazku z dílčích termínů
insert into pomTable
(id_zakazky, dilciTermin, pocet_ks)
select t1.Id_zakazky, t1.dilci_termin, t1.pocet_kusu  -- , count(*) as 'počet datumu přede mnou včetně mě'
from dilci_terminy as t1 join dilci_terminy as t2 
	on (t1.Id_zakazky = t2.Id_zakazky and t1.dilci_termin >= t2.dilci_termin)
where t1.splnen = false and t2.splnen = false
group by Id_zakazky, dilci_termin
having count(*) <= 3
order by t1.Id_zakazky, t1.dilci_termin;


SELECT 
	seznam_zakazek.Id_zakazky as `ID zakázky`,
    `zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
	seznam_modelu.Material as `Materiál`,
	`seznam_modelu`.`Cislo_modelu` as `Číslo modelu`,
	seznam_modelu.Hmotnost as `Hmotnost`,
	seznam_zakazek.Cislo_objednavky as `Číslo objednávky`,
	seznam_zakazek.Pocet_kusu as `Objed.` ,
	seznam_zakazek.Termin_expedice as `Termín expedice`,
	sum(fyzkusy.Odlito)  as `Odlito`,
	sum(fyzkusy.Vycisteno)  as `Vyčist.`,
	sum(fyzkusy.Expedovano) as  `Exped.`,
	sum(fyzkusy.Zmetek)  as `Zmetky`,
	seznam_modelu.IsOdhadHmot  as `Odhad. hmot.`,
	pomTable.dilciTermin as `Dílčí termín`,
	pomTable.pocet_ks  as `Ks`,
	seznam_modelu.Jmeno_modelu  as `Jméno modelu`,
	seznam_modelu.Material_vlastni as `Vlast. mater.`,
	seznam_zakazek.Poznamka  as `Pozn.`,
	weekofyear(seznam_zakazek.Termin_expedice) as `Č. týdne term. exped.`
FROM
    (((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)) left join pomTable
	on (pomTable.id_zakazky = seznam_zakazek.Id_zakazky)
where
	seznam_zakazek.Uzavreno = false 
group by seznam_zakazek.Id_zakazky, pomTable.dilciTermin
order by Jmeno_zakaznika, seznam_zakazek.Termin_expedice, seznam_zakazek.Id_zakazky, Cislo_modelu, pomTable.dilciTermin;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `prepocitatPresnouCenuZaKus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `prepocitatPresnouCenuZaKus`(idZakazky int unsigned)
BEGIN
declare isCzKpom tinyint(1);
declare isZaKuspom tinyint(1);
declare kurzEuNaCzkpom decimal(8,4) unsigned;
declare pom decimal(10,2) unsigned; -- Hmotnost
declare idModelupom int unsigned;
declare cenapom decimal(13,3) unsigned;
declare presnaCenaZaKuspom decimal(17,6) unsigned;


select IsCZK from seznam_zakazek where Id_zakazky = idZakazky into isCzKpom;
select IsZaKus from seznam_zakazek where Id_zakazky = idZakazky into isZaKuspom;
select KurzEUnaCZK from seznam_zakazek where Id_zakazky = idZakazky into kurzEuNaCzkpom;
select Id_modelu from seznam_zakazek where Id_zakazky = idZakazky into idModelupom;
select Cena from seznam_zakazek where Id_zakazky = idZakazky into cenapom;

if(isCzKpom)then
	if(isZaKuspom)then
		set presnaCenaZaKuspom = cenapom;
	else
		select Hmotnost from seznam_modelu where idModelupom = seznam_modelu.Id_modelu into pom;
		set presnaCenaZaKuspom = cenapom * pom;
	end if;
else 
	if(isZaKuspom)then
		set presnaCenaZaKuspom = cenapom * kurzEuNaCzkpom;
	else
		select Hmotnost from seznam_modelu where idModelupom = seznam_modelu.Id_modelu into pom;
		set presnaCenaZaKuspom = cenapom * pom * kurzEuNaCzkpom;
	end if;
end if;

UPDATE `pomdb`.`seznam_zakazek` 
SET 
	`Presna_cena_za_kus` = presnaCenaZaKuspom
WHERE
	`Id_zakazky` = idZakazky;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `pridejVadu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pridejVadu`(vada_s varchar(45))
BEGIN
INSERT INTO `pomdb`.`vady`
(`vada`)
VALUES
(vada_s);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `pridejVinika` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `pridejVinika`(vinik varchar(25))
BEGIN
INSERT INTO `pomdb`.`vinici`
(`Jmeno_vinika`)
VALUES
(vinik);
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `smaz_fyz_kus` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `smaz_fyz_kus`(idKusu int unsigned, out status_mazani int unsigned)
BEGIN
select count(*)
from fyzkusy
where Id_kusu = idKusu
into status_mazani;

if( status_mazani = 1) then
	delete from fyzkusy
	where Id_kusu = idKusu;
else 
	set status_mazani = 2;
end if;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `upravModel` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `upravModel`(idModelu int(10) unsigned, jmenoModelu varchar(30), cisloModelu varchar(30),
 material varchar(25), materialVlastni varchar(10), hmotnost double, isOdhatHmotnost tinyint(1), formovna char(1), norma double, poznamkaModel varchar(50))
BEGIN
if (jmenoModelu is not null)
	then UPDATE `pomdb`.`seznam_modelu` set`Jmeno_modelu` = jmenoModelu WHERE `Id_modelu` = idModelu;
end if;

if (cisloModelu is not null)
	then UPDATE `pomdb`.`seznam_modelu` set`Cislo_modelu` = cisloModelu WHERE `Id_modelu` = idModelu;
end if;

if (material is not null)
	then UPDATE `pomdb`.`seznam_modelu` set`Material` = material WHERE `Id_modelu` = idModelu;
end if;

if (materialVlastni is not null)
	then UPDATE `pomdb`.`seznam_modelu` set`Material_vlastni` = materialVlastni WHERE `Id_modelu` = idModelu;
end if;

if (hmotnost is not null)
	then UPDATE `pomdb`.`seznam_modelu` set`Hmotnost` = hmotnost WHERE `Id_modelu` = idModelu;
	CALL `pomdb`.`kontrolaPresneCenyZaKusCursor`(idModelu);
end if;

if (isOdhatHmotnost is not null)
	then UPDATE `pomdb`.`seznam_modelu` set`IsOdhadHmot` = isOdhatHmotnost WHERE `Id_modelu` = idModelu;
end if;

if (formovna is not null)
	then UPDATE `pomdb`.`seznam_modelu` set`Formovna` = formovna WHERE `Id_modelu` = idModelu;
end if;

if (norma is not null)
	then UPDATE `pomdb`.`seznam_modelu` set`Norma_slevac` = norma WHERE `Id_modelu` = idModelu;
end if;

UPDATE `pomdb`.`seznam_modelu` set`Poznamka_model` = poznamkaModel WHERE `Id_modelu` = idModelu;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `upravZakazku` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `upravZakazku`(idZakazky int(10) unsigned, idZakaznika smallint(5) unsigned, idModelu int(10) unsigned, cisloObjednavky varchar(30), pocetKusu smallint(6) unsigned, 
datumPrijetiZakazky date, paganyrka varchar(5), cena decimal(13,3) unsigned, isCzK tinyint(1), isZaKus tinyint(1), kurzEuNaCzk decimal(8,4), poznamka varchar(45), datumExpedice date)
BEGIN
declare presnaCenaZaKus decimal (17,6) unsigned;
declare pom decimal (10,2) unsigned;


declare pocetDuplicit int;
-- declare presnaCenaZaKus decimal(13,3);
-- declare pom int;
declare id int;

SELECT 
	count(*)
FROM pomdb.seznam_zakazek
where 
	Id_zakaznika = idZakaznika and
	Id_modelu = idModelu and
	Cislo_objednavky = cisloObjednavky and
	Pocet_kusu = pocetKusu and
	Datum_prijeti_zakazky = datumPrijetiZakazky and
	Termin_expedice = datumExpedice and
	Paganyrka = paganyrka and
	Cena = cena and
	IsCZK = isCzK and
	IsZaKus = isZaKus and
    Id_zakazky != idZakazky
into pocetDuplicit;

if(pocetDuplicit = 0 and pocetKusu <= 1500 and pocetKusu > 0) then
	if(isCzK)then
		if(isZaKus)then
			set presnaCenaZaKus = cena;
		else
			select Hmotnost from seznam_modelu where idModelu = seznam_modelu.Id_modelu into pom;
			set presnaCenaZaKus = cena * pom;
		end if;
	else if(kurzEuNaCzk > 0)then
			if(isZaKus)then
				set presnaCenaZaKus = cena * kurzEuNaCzk;
			else
				select Hmotnost from seznam_modelu where idModelu = seznam_modelu.Id_modelu into pom;
				set presnaCenaZaKus = cena * pom * kurzEuNaCzk;
			end if;
		else 
			set presnaCenaZaKus = null, cena = null , kurzEuNaCzk = null;
		end if;
	end if;

	UPDATE `pomdb`.`seznam_zakazek` 
	SET 
		`Id_zakaznika` = idZakaznika,
		`Id_modelu` = idModelu,
		`Cislo_objednavky` = cisloObjednavky,
		`Pocet_kusu` = pocetKusu,
		`Datum_prijeti_zakazky` = datumPrijetiZakazky,
		`Termin_expedice` = datumExpedice,
		`Paganyrka` = paganyrka,
		`Cena` = cena,
		`IsCZK` = isCzK,
		`IsZaKus` = isZaKus,
		`KurzEUnaCZK` = kurzEuNaCzk,
		`Poznamka` = poznamka,
		`Presna_cena_za_kus` = presnaCenaZaKus
	WHERE
		`Id_zakazky` = idZakazky;
	
	update fyzkusy join seznam_zakazek on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
	set fyzkusy.Id_modelu_odlito = seznam_zakazek.Id_modelu
	where fyzkusy.Odlito = false;

	CALL `pomdb`.`generujKusy`(idZakazky);
	SELECT TRUE;
else
	select false;
end if;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `upravZakaznika` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `upravZakaznika`(idZakaznika smallint(5) unsigned, noveJmeno varchar(30))
BEGIN
UPDATE `pomdb`.`zakaznici`
SET
`Jmeno_zakaznika` = noveJmeno
WHERE `Id_zakaznika` = idZakaznika;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `uprav_vadu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `uprav_vadu`(idVadyPom smallint unsigned, vadaNew varchar(45))
BEGIN
Update vady
set vady.vada = vadaNew
where vady.idvady = idVadyPom;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `uprav_vinika` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `uprav_vinika`(idVinikaPom smallint unsigned, vinikNew varchar(45))
BEGIN
Update vinici
set vinici.Jmeno_vinika = vinikNew
where vinici.Id_vinika = idVinikaPom;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `uzavriZakazku` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `uzavriZakazku`(idZakazky int unsigned, OUT pocetOdlitych int, OUT pocetVycistenych int, OUT pocetExpedovanych int,OUT pocetNeodlitych int, OUT pocetZmetkuBezVady int, OUT pocetNedokoncenychDilcichterminu int, OUT objednanoKs int)
BEGIN
declare lzeUzavrit tinyint(1);
declare uzavreno tinyint(1);
declare objednanoKusu smallint;
set lzeUzavrit = true;
set uzavreno = false;

select Pocet_kusu from seznam_zakazek where seznam_zakazek.Id_zakazky = idZakazky 
into objednanoKusu;

set objednanoKs = objednanoKusu;

select count(*) 
FROM (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		join 
        (`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu)) on(seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
where
 seznam_zakazek.Id_zakazky = idZakazky and
 fyzkusy.Zmetek = false and
 fyzkusy.Odlito = true 
into pocetOdlitych;
 
select count(*) 
FROM (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		join 
        (`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu)) on(seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
where
 seznam_zakazek.Id_zakazky = idZakazky and
 fyzkusy.Zmetek = false and
 fyzkusy.Vycisteno = true 
into pocetVycistenych;
 
select count(*) 
FROM (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		join 
        (`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu)) on(seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
where
 seznam_zakazek.Id_zakazky = idZakazky and
 fyzkusy.Zmetek = false and
 fyzkusy.Expedovano = true 
into pocetExpedovanych;
 
select count(*) 
FROM (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		join 
        (`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu)) on(seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
where
 seznam_zakazek.Id_zakazky = idZakazky and
 fyzkusy.Zmetek = false and
 fyzkusy.Odlito = false 
into pocetNeodlitych;
 
 
select count(*) 
from ((`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu)) left join vinici on (vinici.Id_vinika = zmetky_vady.Id_vinika))left join vady on (vady.idvady = zmetky_vady.Id_vady)
 where `fyzkusy`.`Zmetek` and Id_zakazky = idZakazky and zmetky_vady.Id_kusu is null 
into  pocetZmetkuBezVady;


select count(*)
from pomdb.dilci_terminy
where dilci_terminy.Id_zakazky = idZakazky and splnen = false
into pocetNedokoncenychDilcichterminu;


 if (objednanoKusu = pocetOdlitych and objednanoKusu = pocetVycistenych and objednanoKusu = pocetExpedovanych and pocetNeodlitych = 0 and pocetZmetkuBezVady = 0 and pocetNedokoncenychDilcichterminu = 0)then
  update seznam_zakazek set Uzavreno = true where seznam_zakazek.Id_zakazky = idZakazky;
 select true;
 else 
 select false;
 end if;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `verze` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `verze`()
BEGIN
select '1.7';
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberDilciTerminy` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberDilciTerminy`(idZakazky int unsigned)
BEGIN
SELECT 
    `dilci_terminy`.`dilci_termin`,
    `dilci_terminy`.`pocet_kusu`,
	 `dilci_terminy`.`splnen`
FROM `pomdb`.`dilci_terminy`
where Id_zakazky = idZakazky order by `dilci_terminy`.`splnen`, dilci_termin;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberFyzKusy` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberFyzKusy`(idZakazky int(10) unsigned, OUT pocetZmetkuBezVady smallint)
BEGIN
SELECT `fyzkusy`.`Id_kusu` as 'ID kusu',
    `fyzkusy`.`Datum_liti` as 'Plánované lití',
    `fyzkusy`.`Cislo_tavby` as 'Číslo tavby',
    `fyzkusy`.`Odlito`,
    `fyzkusy`.`Vycisteno` as 'Vyčištěno',
    `fyzkusy`.`Zmetek`,
    `fyzkusy`.`Datum_odliti` as 'Datum odlití',
    `fyzkusy`.`Expedovano` as 'Expedováno',
    `fyzkusy`.`Cislo_faktury` as 'Číslo faktury',
	`fyzkusy`.`Datum_vycisteni` as 'Datum vyčištění',
	vinici.Id_vinika as 'ID viníka',
	vinici.Jmeno_vinika as 'Jméno viníka', 
	vady.idvady as 'ID vady',
	vady.vada as 'Vada',
	fyzkusy.Datum_expedice as 'Datum expedice',
	fyzkusy.Datum_zadani_zmetku as 'Datum zadání zmetku',
	fyzkusy.Id_modelu_odlito as 'ID modelu',
	fyzkusy.Teplota_liti as 'Teplota lití'
FROM ((`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu)) left join vinici on (vinici.Id_vinika = zmetky_vady.Id_vinika))left join vady on (vady.idvady = zmetky_vady.Id_vady)
where Id_zakazky = idZakazky
limit 1500;
select count(*) 
from ((`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu)) left join vinici on (vinici.Id_vinika = zmetky_vady.Id_vinika))left join vady on (vady.idvady = zmetky_vady.Id_vady)
 where `fyzkusy`.`Zmetek` and Id_zakazky = idZakazky and zmetky_vady.Id_kusu is null into  pocetZmetkuBezVady;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberModely` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberModely`(jmeno varchar(30), cisloModelu varchar(30), nazevModelu varchar(30), idModelu int UNSIGNED, datumZakazky date, formovna char(1), idZakazky int UNSIGNED, pouzeUzavrene tinyint)
BEGIN
declare pomJmeno varchar(31);
declare pomCisloModelu varchar(31);
declare pomNazevModelu varchar(31);
declare pomFormovna char(2);

if (jmeno like ' %') then set jmeno = '';
end if;
if (cisloModelu like ' %') then set cisloModelu = '';
end if;
if (nazevModelu like ' %') then set nazevModelu = '';
end if;
if (formovna like ' %') then set formovna = '';
end if;
set pomJmeno = CONCAT(jmeno,'%');
set pomCisloModelu = CONCAT(cisloModelu,'%');
set pomNazevModelu = CONCAT(nazevModelu,'%');
set pomFormovna =  CONCAT(formovna,'%');




SELECT `seznam_modelu`.`Id_modelu` as 'ID modelu',
    `seznam_modelu`.`Jmeno_modelu` as 'Jméno modelu',
    `seznam_modelu`.`Cislo_modelu` as 'Číslo modelu',
    `seznam_modelu`.`Material` as 'Materiál',
    `seznam_modelu`.`Material_vlastni` as 'Vlastní materiál',
    `seznam_modelu`.`Hmotnost`,
    `seznam_modelu`.`IsOdhadHmot` as 'Odhadová hmotnost',
    `seznam_modelu`.`Formovna`,
    `seznam_modelu`.`Norma_slevac` as 'Norma slévač',
	`seznam_modelu`.`Poznamka_model` as 'Pozn. model',
	`zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka'
FROM (pomdb.seznam_modelu left join pomdb.seznam_zakazek on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) left join pomdb.zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika)
where 
	(`zakaznici`.`Jmeno_zakaznika` like pomJmeno or `zakaznici`.`Jmeno_zakaznika` is null) and
	`seznam_modelu`.`Cislo_modelu` like pomCisloModelu and
	`seznam_modelu`.`Jmeno_modelu` like pomNazevModelu and
	`seznam_modelu`.`Formovna` like pomFormovna and
	(case !isnull(datumZakazky) and !isnull(seznam_zakazek.Datum_prijeti_zakazky)
		when true then  seznam_zakazek.Datum_prijeti_zakazky >= datumZakazky
		else true
		end  or seznam_zakazek.Datum_prijeti_zakazky is null) and
	(case idModelu
		when 0 then true
		else `seznam_modelu`.Id_modelu = idModelu
		end )
	and(case idZakazky
		when 0 then true
		else `seznam_zakazek`.Id_zakazky = idZakazky
		end or `seznam_zakazek`.Id_zakazky is null)
	and (case isnull(pouzeUzavrene) or pouzeUzavrene = false
		when true then true
		else seznam_zakazek.Uzavreno = pouzeUzavrene end)
group by `seznam_modelu`.`Id_modelu`, `zakaznici`.`Jmeno_zakaznika`
order by `zakaznici`.`Jmeno_zakaznika`, `seznam_modelu`.`Cislo_modelu`
limit 500;
	
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberVady` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberVady`(jmenoVady varchar(45))
BEGIN
declare pomJmenoVady varchar(46);

if (jmenoVady like ' %' or jmenoVady is null) then set jmenoVady = '';
end if;
set pomJmenoVady = CONCAT(jmenoVady,'%');

SELECT vady.idvady as 'ID vady', vady.vada as 'Jméno vady'
FROM pomdb.vady
where vady.vada like pomJmenoVady;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberViniky` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberViniky`(jmenoVinika varchar(45))
BEGIN
declare pomJmenoVinika varchar(46);

if (jmenoVinika like ' %' or jmenoVinika is null) then set jmenoVinika = '';
end if;
set pomJmenoVinika = CONCAT(jmenoVinika,'%');
SELECT vinici.Id_vinika as 'ID viníka',vinici.Jmeno_vinika as 'Jméno viníka'
FROM pomdb.vinici
where vinici.Jmeno_vinika like pomJmenoVinika;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberVlastniMaterialy` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberVlastniMaterialy`()
BEGIN
select 
	'Vše' as 'Vlastní materiál'
union all
select distinct
	Material_vlastni
from
	seznam_modelu;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberZakazky2` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberZakazky2`(idZakazky int UNSIGNED, jmeno varchar(30), cisloModelu varchar(30), nazevModelu varchar(30), idModelu int UNSIGNED, datumZakazky date, cisloObjednavky varchar(30), bolPouzeUzavrene tinyint)
BEGIN
declare pomJmeno varchar(31);
declare pomCisloModelu varchar(31);
declare pomNazevModelu varchar(31);
declare pomCisloObjednavky varchar(31);

if (jmeno like ' %') then set jmeno = '';
end if;
if (cisloModelu like ' %') then set cisloModelu = '';
end if;
if (nazevModelu like ' %') then set nazevModelu = '';
end if;
if (cisloObjednavky like ' %') then set cisloObjednavky = '';
end if;
set pomJmeno = CONCAT(jmeno,'%');
set pomCisloModelu = CONCAT(cisloModelu,'%');
set pomNazevModelu = CONCAT(nazevModelu,'%');
set pomCisloObjednavky =  CONCAT(cisloObjednavky,'%');




SELECT 
	`seznam_zakazek`.`Id_zakazky` as 'ID zakázky',
	`zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka',
	`seznam_zakazek`.`Cislo_objednavky` as 'Číslo objednávky',
	`seznam_modelu`.`Cislo_modelu` as 'Číslo modelu',
	`seznam_zakazek`.`Datum_prijeti_zakazky` as 'Datum objednávky',
	`seznam_zakazek`.`Pocet_kusu` as 'Počet kusů',
	 seznam_zakazek.Termin_expedice as 'Termín expedice',
	`seznam_zakazek`.`Cena`,
    `seznam_zakazek`.`IsCZK` as 'Koruny',
    `seznam_zakazek`.`IsZaKus` as 'Za kus',
    `seznam_zakazek`.`KurzEUnaCZK` as 'Kurz EU/CZK',
	`seznam_zakazek`.`Id_modelu` 'ID modelu',
	`seznam_modelu`.`Jmeno_modelu` as 'Jméno modelu',
	`seznam_modelu`.`Material` as 'Materiál',
	`seznam_modelu`.`Material_vlastni` as 'Vlastní materiál',
	`seznam_modelu`.`Formovna`,
	`seznam_modelu`.`Hmotnost`,
	`seznam_modelu`.`IsOdhadHmot` as 'Odhadová hmotnost',
	`seznam_modelu`.`Norma_slevac` as 'Norma slévač',
	`seznam_zakazek`.`Id_zakaznika` as 'ID zákazníka',
	`seznam_zakazek`.`Uzavreno` as 'Uzavřeno',
	`seznam_zakazek`.`Poznamka` as 'Poznámka zakázky',
	`seznam_zakazek`.`Paganyrka` as 'Paganýrka',
	 seznam_zakazek.Presna_cena_za_kus as 'Přesná cena za kus',
	`seznam_modelu`.`Poznamka_model` as 'Poznámka k modelu'
FROM (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika)
where 
	`zakaznici`.`Jmeno_zakaznika` like pomJmeno and
	`seznam_modelu`.`Cislo_modelu` like pomCisloModelu and
	`seznam_modelu`.`Jmeno_modelu` like pomNazevModelu and
	`seznam_zakazek`.`Cislo_objednavky` like pomCisloObjednavky and
	(case !isnull(datumZakazky) and !isnull(seznam_zakazek.Datum_prijeti_zakazky)
		when true then  seznam_zakazek.Datum_prijeti_zakazky >= datumZakazky
		else true
		end ) and
	(case idModelu
		when 0 then true
		else `seznam_modelu`.Id_modelu = idModelu
		end ) and
	(case idZakazky
		when 0 then true
		else `seznam_zakazek`.Id_zakazky = idZakazky
		end ) and
	seznam_zakazek.Uzavreno = bolPouzeUzavrene
order by `zakaznici`.`Jmeno_zakaznika`,`seznam_modelu`.`Cislo_modelu`, `seznam_zakazek`.`Id_zakazky` desc

limit 500;
	
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberZakazniky` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberZakazniky`(jmeno varchar(30))
BEGIN
declare pom varchar(31);

if (jmeno like ' %') then set jmeno = null;
end if;

set pom = CONCAT(jmeno,'%');
SELECT `zakaznici`.`Id_zakaznika` as 'ID zákazníka',
    `zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka'
FROM `pomdb`.`zakaznici`
where `zakaznici`.`Jmeno_zakaznika` like pom
order by Jmeno_zakaznika, Id_zakaznika desc
limit 100;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vyberZmetky` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vyberZmetky`(idZakazky int UNSIGNED, jmeno varchar(30), cisloModelu varchar(30), nazevModelu varchar(30), idModelu int UNSIGNED, datumZakazky date, cisloObjednavky varchar(30), vcetneUzav tinyint)
BEGIN
declare pomJmeno varchar(31);
declare pomCisloModelu varchar(31);
declare pomNazevModelu varchar(31);
declare pomCisloObjednavky varchar(31);

if (jmeno like ' %') then set jmeno = '';
end if;
if (cisloModelu like ' %') then set cisloModelu = '';
end if;
if (nazevModelu like ' %') then set nazevModelu = '';
end if;
if (cisloObjednavky like ' %') then set cisloObjednavky = '';
end if;
set pomJmeno = CONCAT(jmeno,'%');
set pomCisloModelu = CONCAT(cisloModelu,'%');
set pomNazevModelu = CONCAT(nazevModelu,'%');
set pomCisloObjednavky =  CONCAT(cisloObjednavky,'%');




SELECT 
	-- fyzkusy.Datum_odliti as 'Datum lití',
	fyzkusy.Datum_zadani_zmetku as 'Datum zmetku',
	-- fyzkusy.Zmetek,
	vinici.Jmeno_vinika as 'Jméno viníka',
	vady.vada as 'Vada',
	`seznam_zakazek`.`Id_zakazky` as 'ID zakázky',
	`zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka',
	`seznam_modelu`.`Cislo_modelu` as 'Číslo modelu', 
	`seznam_zakazek`.`Paganyrka` as 'Paganýrka',
	`seznam_zakazek`.`Cena`,
	`seznam_zakazek`.`Id_modelu` as 'ID modelu',
	`seznam_modelu`.`Jmeno_modelu` as 'Jméno modelu',
	`seznam_modelu`.`Material`,
	`seznam_modelu`.`Material_vlastni` as 'Vlastní materiál',
	`seznam_modelu`.`Formovna`,
	`seznam_modelu`.`Hmotnost`,
	`seznam_modelu`.`IsOdhadHmot` as 'Odhadová hmotnost',
	`seznam_modelu`.`Norma_slevac` as 'Norma slévač',
	`seznam_zakazek`.`Id_zakaznika` as 'ID zákazníka',
	`seznam_zakazek`.`Poznamka` as 'Poznámka zakázky',
	seznam_zakazek.Termin_expedice as 'Termín expedice',
	seznam_zakazek.Presna_cena_za_kus as 'Přesná cena za kus'
FROM ((pomdb.seznam_zakazek
		join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		join pomdb.zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
		join (((`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu))	
										left join vinici on (vinici.Id_vinika = zmetky_vady.Id_vinika))	
										left join vady on (vady.idvady = zmetky_vady.Id_vady))
				on(seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)

where 
	`zakaznici`.`Jmeno_zakaznika` like pomJmeno and
	`seznam_modelu`.`Cislo_modelu` like pomCisloModelu and
	`seznam_modelu`.`Jmeno_modelu` like pomNazevModelu and
	`seznam_zakazek`.`Cislo_objednavky` like pomCisloObjednavky and
	(case !isnull(datumZakazky) and !isnull(seznam_zakazek.Datum_prijeti_zakazky)
		when true then  seznam_zakazek.Datum_prijeti_zakazky >= datumZakazky
		else true
		end ) and
	(case idModelu
		when 0 then true
		else `seznam_modelu`.Id_modelu = idModelu
		end ) and
	(case idZakazky
		when 0 then true
		else `seznam_zakazek`.Id_zakazky = idZakazky
		end ) and 
	fyzkusy.Zmetek = true
	and  seznam_zakazek.Uzavreno = vcetneUzav
order by fyzkusy.Datum_zadani_zmetku,`zakaznici`.`Jmeno_zakaznika` asc, `seznam_zakazek`.`Id_zakazky` desc
limit 30
;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisDenniOdlitychKusu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisDenniOdlitychKusu`(datum date)
BEGIN
declare odlitoCelkemZaDen int;

select count(Odlito) from fyzkusy where fyzkusy.Datum_odliti = datum into odlitoCelkemZaDen;
SELECT
	seznam_zakazek.Id_zakazky as 'ID zakázky',
    `zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka',
    `seznam_modelu`.`Cislo_modelu` as 'Číslo modelu',
	sum(Odlito) as Odlito,
	seznam_modelu.Material as 'Materiál',
	seznam_modelu.Material_vlastni as 'Materiál vlastní',
	Cislo_tavby as 'Číslo tavby'
FROM
    (((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky))
where fyzkusy.Datum_odliti = datum
group by seznam_zakazek.Id_zakazky
order by Jmeno_zakaznika, Cislo_modelu;

select '','','Celkem:',odlitoCelkemZaDen,'','','';


-- drop table if exists odlitykusy;
-- create temporary table if not exists odlitykusy (IdZakazkyOdlitku int unsigned, odlitku mediumint unsigned);

-- insert into
 -- odlitykusy (IdZakazkyOdlitku, odlitku) 
-- select seznam_zakazek.Id_zakazky, sum(fyzkusy.Odlito) from fyzkusy left join seznam_zakazek on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
  -- where
-- 	fyzkusy.Datum_odliti = datum
	-- Opravdu nech zakomentaovany and seznam_zakazek.Uzavreno = false
--  group by seznam_zakazek.Id_zakazky with rollup;

-- alter table odlitykusy add index (IdZakazkyOdlitku);

-- SELECT  distinct
	-- seznam_zakazek.Id_zakazky as 'ID zakázky',
    -- `zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka',
    -- `seznam_modelu`.`Cislo_modelu` as 'Číslo modelu',
	-- odlitku as Odlito,
	-- seznam_modelu.Material as 'Materiál',
	-- seznam_modelu.Material_vlastni as 'Materiál vlastní',
-- 	Cislo_tavby as 'Číslo tavby'
-- FROM
   -- ((((pomdb.seznam_zakazek
   -- join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
   -- join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
    --     join
   --  fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky))) right join odlitykusy on (seznam_zakazek.Id_zakazky = odlitykusy.IdZakazkyOdlitku)
-- order by zakaznici.Jmeno_zakaznika, seznam_zakazek.Id_zakazky;

-- drop table if exists odlitykusy;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisDleTerminuExpediceCisloTydne` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisDleTerminuExpediceCisloTydne`(od date, do_ date)
BEGIN
drop temporary table if exists pomTable;
create temporary table pomTable 
	(termin_ex date,
	dilci_ter date,
	id_zakazky int unsigned,
	jmeno_zakaznika varchar(30),
	jmeno_modelu varchar(30),
	cislo_modelu varchar(30),
	hmotnost decimal(11,2),
	cislo_objednavky varchar(30),
	dodat_ks smallint,
	pocet_ks smallint,
	odlito smallint,
	vycisteno smallint,
	expedovano smallint,
	-- zmetky smallint,
	isTerminExpedice tinyint(1)
-- pridat polozku zda to je dilcitermin nebo termin expedice
);

-- termin expedice
insert into pomTable 
(termin_ex, dilci_ter, id_zakazky, jmeno_zakaznika, jmeno_modelu, cislo_modelu, dodat_ks,
 pocet_ks, hmotnost, cislo_objednavky, odlito, vycisteno, expedovano, isTerminExpedice)
SELECT
	seznam_zakazek.Termin_expedice as `Termín expedice`,
	null as `Dílčí termín`,
   `seznam_zakazek`.`Id_zakazky` as `ID zakázky`,
    `zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
    `seznam_modelu`.`Jmeno_modelu` as `Jméno modelu`,
	`seznam_modelu`.`Cislo_modelu` as `Číslo modelu`,
	null as `Dodat ks.`,
	`seznam_zakazek`.`Pocet_kusu` as `Objed. c.`,
	seznam_modelu.Hmotnost as `Hmotnost ks.`,
	seznam_zakazek.Cislo_objednavky,
	sum(fyzkusy.Odlito),
	sum(fyzkusy.Vycisteno),
	sum(fyzkusy.Expedovano),
	1
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
	fyzkusy.Zmetek = false and
	seznam_zakazek.Termin_expedice between od and do_   
group by  seznam_zakazek.Id_zakazky, seznam_zakazek.Termin_expedice;
	
-- dilci terminy
insert into pomTable 
(termin_ex, dilci_ter, id_zakazky, jmeno_zakaznika, jmeno_modelu, cislo_modelu, dodat_ks,
 pocet_ks, hmotnost, cislo_objednavky, odlito, vycisteno, expedovano, isTerminExpedice)
SELECT 
	seznam_zakazek.Termin_expedice,
	dilci_terminy.dilci_termin,
   `seznam_zakazek`.`Id_zakazky`,
    `zakaznici`.`Jmeno_zakaznika`,
    `seznam_modelu`.`Jmeno_modelu`,
	`seznam_modelu`.`Cislo_modelu`,
	dilci_terminy.pocet_kusu as `Dodat ks.`,
	`seznam_zakazek`.`Pocet_kusu` as `Objed. c.`,
	seznam_modelu.Hmotnost as `Hmotnost ks.`,
	seznam_zakazek.Cislo_objednavky,
	sum(fyzkusy.Odlito),
	sum(fyzkusy.Vycisteno),
	sum(fyzkusy.Expedovano),
	0
FROM
    (((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)) join dilci_terminy on (dilci_terminy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
	fyzkusy.Zmetek = false and
	dilci_terminy.dilci_termin between od and do_  
group by  seznam_zakazek.Id_zakazky, dilci_terminy.dilci_termin;
	


-- zmetci
drop table if exists zmetci;
create temporary table if not exists zmetci (IdZakazkyZmetku int unsigned, pocetZmetku smallint unsigned, isTerminExpedice tinyint(1));

-- dle termínu expedice
insert into zmetci (IdZakazkyZmetku, pocetZmetku, isTerminExpedice) 
select seznam_zakazek.Id_zakazky, sum(Zmetek), 1
from fyzkusy left join seznam_zakazek on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky) 
where 
	seznam_zakazek.Termin_expedice between od and do_ 
group by seznam_zakazek.Id_zakazky, seznam_zakazek.Termin_expedice;

-- dle dilcich terminu
insert into zmetci (IdZakazkyZmetku, pocetZmetku, isTerminExpedice) 
select seznam_zakazek.Id_zakazky, sum(Zmetek), 0
from (fyzkusy left join seznam_zakazek on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)) 
	join dilci_terminy on (dilci_terminy.Id_zakazky = seznam_zakazek.Id_zakazky)
where 
	dilci_terminy.dilci_termin between od and do_ 
group by seznam_zakazek.Id_zakazky, dilci_terminy.dilci_termin;

-- pridat datum zmetku jako primary key
alter table zmetci add primary key (IdZakazkyZmetku, isTerminExpedice);



-- select
SELECT 
	jmeno_zakaznika as `Jméno zákazníka`,
	termin_ex as `Termín expedice`,
	dilci_ter as `Dílčí termín`,
	id_zakazky as `ID zakázky`,
	cislo_modelu as `Číslo modelu`,
	jmeno_modelu as `Jméno modelu`,
	cislo_objednavky as `Číslo objednávky`,
	hmotnost as `Hmotnost ks.`,
	dodat_ks as `Dodat ks.`,
	pocet_ks as `Objed.`,
	odlito as `Odlito`,
	vycisteno as `Vyčiš.`,
	expedovano as `Exped.`,
	pocetZmetku as `Zmetků`
FROM
   pomTable left join zmetci on (zmetci.IdZakazkyZmetku = pomTable.id_zakazky and zmetci.isTerminExpedice = pomTable.isTerminExpedice)
order by `Jméno zákazníka`, `Číslo modelu`, `Termín expedice`;

drop temporary table if exists pomTable;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisExpedovanychKusuOdDo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisExpedovanychKusuOdDo`(od date, do_ date)
BEGIN
-- pomocne tabulky
drop temporary table if exists pomTable;
create temporary table pomTable (datum_ex  date, id int unsigned, Jmeno_zakaznika varchar(30), Cislo_modelu varchar(30), Jmeno_modelu varchar(30),
	exped smallint, cenaCelk decimal(17,6), hmotCelk decimal(10,2), cisloFaktury varchar(19));
drop temporary table if exists pomTable2;
create temporary table pomTable2 (datum_ex  date, id int unsigned, Jmeno_zakaznika varchar(30), Cislo_modelu varchar(30), Jmeno_modelu varchar(30),
	exped smallint, cenaCelk decimal(17,6), hmotCelk decimal(10,2), cisloFaktury varchar(19));

-- prace s daty
insert into pomTable (datum_ex, id, Jmeno_zakaznika, Cislo_modelu, Jmeno_modelu, exped, cenaCelk, hmotCelk, cisloFaktury)
SELECT 
	fyzkusy.Datum_expedice as `Datum expedice`,
    `seznam_zakazek`.`Id_zakazky` as `ID zakázky`,
    `zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
	`seznam_modelu`.`Cislo_modelu` as `Číslo modelu`,
    `seznam_modelu`.`Jmeno_modelu` as `Jméno modelu`,
	sum(fyzkusy.Expedovano) as `Expedováno`,
	sum(fyzkusy.Expedovano * returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as `Cena celkem`,
	sum(fyzkusy.Expedovano * getHmotnostFyzKusu(fyzkusy.Id_kusu)) as `Hmotnost celkem`,
	fyzkusy.Cislo_faktury
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
    fyzkusy.Zmetek = false and 
	fyzkusy.Odlito = true and 
	fyzkusy.Vycisteno = true and	
	fyzkusy.Expedovano = true and
	fyzkusy.Datum_expedice between od and do_   
group by  seznam_zakazek.Id_zakazky, fyzkusy.Datum_expedice, fyzkusy.Cislo_faktury;

-- mezisoucty
insert into pomTable2 (datum_ex, id, Jmeno_zakaznika, Cislo_modelu, Jmeno_modelu, exped, cenaCelk, hmotCelk, cisloFaktury)
SELECT 
	null,
    null,
    Jmeno_zakaznika,
	null,
    'Celkem',
	sum(exped),
	sum(cenaCelk),
	sum(hmotCelk),
	null
FROM
   pomTable
group by  Jmeno_zakaznika;

-- celkovy soucet
insert into pomTable2 (datum_ex, id, Jmeno_zakaznika, Cislo_modelu, Jmeno_modelu, exped, cenaCelk, hmotCelk, cisloFaktury)
SELECT 
	null,
    null,
    '|',
	null,
    'Celkem',
	sum(exped),
	sum(cenaCelk),
	sum(hmotCelk),
	null
FROM
   pomTable;
-- zkopirovat data z pomTable
insert into pomTable2 (datum_ex, id, Jmeno_zakaznika, Cislo_modelu, Jmeno_modelu, exped, cenaCelk, hmotCelk, cisloFaktury)
select 
 datum_ex, id, Jmeno_zakaznika, Cislo_modelu, Jmeno_modelu, exped, cenaCelk, hmotCelk, cisloFaktury
from
	pomTable;

-- Vyber dat

select 
	datum_ex as `Datum expedice`,
    id as `ID zakázky`,
    Jmeno_zakaznika as `Jméno zákazníka`,
	Cislo_modelu as `Číslo modelu`,
    Jmeno_modelu as `Jméno modelu`,
	exped as `Expedováno`,
	cenaCelk as `Cena celkem`,
	hmotCelk as `Hmotnost celkem`,
	cisloFaktury as `Č. faktury`
from 
	pomTable2
order by 
	Jmeno_zakaznika, datum_ex desc;


drop temporary table if exists pomTable;
drop temporary table if exists pomTable2;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisKusuNaSkladu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisKusuNaSkladu`()
BEGIN
drop temporary table if exists pomTable;
create temporary table pomTable (Id_zakazky  int(10) unsigned, Termin_expedice date, Jmeno_zakaznika varchar(30), Cislo_modelu varchar(30), 
	KusNaSklad smallint, cenaKratKusy decimal(17,6), hmotKratKusy decimal(10,2));
insert into pomTable (Id_zakazky, Termin_expedice, Jmeno_zakaznika, Cislo_modelu, KusNaSklad, cenaKratKusy, hmotKratKusy)
SELECT 
	`seznam_zakazek`.`Id_zakazky` as 'ID zakázky',
 	seznam_zakazek.Termin_expedice as 'Termín expedice',
	`zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka',
	`seznam_modelu`.`Cislo_modelu` as 'Číslo modelu',
    count(*) as 'Kusů na skladě',
    sum(fyzkusy.Vycisteno * returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as 'P. cena * kusy',
    sum(fyzkusy.Vycisteno * getHmotnostFyzKusu(fyzkusy.Id_kusu)) as 'Hmotnost * kusy'

FROM ((pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu)) join pomdb.zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
 join fyzkusy on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where 
	 fyzkusy.Zmetek = false and
	 fyzkusy.Vycisteno = true and 
	 fyzkusy.Odlito = true and
	 fyzkusy.Expedovano = false and
	 seznam_zakazek.Uzavreno = false
group by seznam_zakazek.Id_zakazky
;

-- order by  seznam_zakazek.Termin_expedice,`zakaznici`.`Jmeno_zakaznika`, `seznam_modelu`.`Cislo_modelu`

SELECT 
	Id_zakazky as 'ID zakázky',
 	Termin_expedice as 'Termín expedice',
	Jmeno_zakaznika as 'Jméno zákazníka',
	Cislo_modelu as 'Číslo modelu',
    KusNaSklad as 'Kusů na skladě',
    cenaKratKusy as 'P. cena * kusy',
    hmotKratKusy as 'Hmotnost * kusy'
FROM 
	pomTable
order by  Termin_expedice, Jmeno_zakaznika;

SELECT 
	'Celkem',
 	'',
	'',
	'',
    sum(KusNaSklad) as 'Kusů na skladě',
    sum(cenaKratKusy) as 'P. cena * kusy',
    sum(hmotKratKusy) as 'Hmotnost * kusy'
FROM 
	pomTable;
    
drop temporary table if exists pomTable;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisMzdySlevacu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisMzdySlevacu`(datum_od date, datum_do date)
BEGIN
-- nekdy se muze stat ze nebude tabulka odpovidat, tzn Bude norma slevace, počet odlitych kusů a třetí sloupec se nebude rovnat odlito*norma. 
-- to protože počítám se skutečnou normou, podle puvodně odlitého modelu. to znamená že tato zakazka změnila model v jejim pruběhu.

drop temporary table if exists pomTable;
create temporary table pomTable 
	(idZakazky int(10) unsigned,
	jmenoZakaznika varchar(30),
	cisloModelu varchar(30),
	formovna char(1),
	norma_slevace decimal(9,1) unsigned,
	odlito smallint unsigned,
	vyrobeno decimal(12,1) unsigned
);
-- soucty
drop temporary table if exists pomTable2;
create temporary table pomTable2 
	(idZakazky int(10) unsigned,
	jmenoZakaznika varchar(30),
	cisloModelu varchar(30),
	formovna char(1),
	norma_slevace decimal(9,1) unsigned,
	odlito smallint unsigned,
	vyrobeno decimal(12,1) unsigned
);

insert into pomTable (idZakazky, jmenoZakaznika, cisloModelu, formovna, norma_slevace, odlito, vyrobeno)
SELECT 
	seznam_zakazek.Id_zakazky as `ID zakázky`,
    `zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
	`seznam_modelu`.`Cislo_modelu` as `Číslo modelu`,
	 seznam_modelu.Formovna,
	 seznam_modelu.Norma_slevac as `Norma slévač`,
	 sum(fyzkusy.Odlito) as Odlito,
     sum(getNormaSlevaceFyzKusu(fyzkusy.Id_kusu) * fyzkusy.Odlito) as Vyrobeno
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
    fyzkusy.Zmetek = false and
    fyzkusy.Odlito = true and
	fyzkusy.Datum_odliti between datum_od and datum_do
group by seznam_zakazek.Id_zakazky, seznam_modelu.Formovna; 

-- součty součtů
insert into pomTable2 (idZakazky, jmenoZakaznika, cisloModelu, formovna, norma_slevace, odlito, vyrobeno)
SELECT 
	null,
    '|',
	null,
	formovna,
	sum(norma_slevace),
	sum(odlito) as Odlito,
    sum(vyrobeno)
FROM
   pomTable
group by formovna;

insert into pomTable (idZakazky, jmenoZakaznika, cisloModelu, formovna, norma_slevace, odlito, vyrobeno)
SELECT 
	*
FROM
   pomTable2
group by formovna;

-- select
select  
	idZakazky as `ID zakázky`,
	jmenoZakaznika as `Jméno zákazníka`,
	cisloModelu  as `Číslo modelu`,
	formovna  as `Formovna`,
	norma_slevace  as `Norma slévač`,
	odlito  as `Odlito`,
	vyrobeno  as `Vyrobeno`
from pomTable
order by formovna, jmenoZakaznika, cisloModelu;

drop temporary table if exists pomTable;
drop temporary table if exists pomTable2;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisOdlituVKgKcOdDo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisOdlituVKgKcOdDo`(od date, do_ date)
BEGIN
drop temporary table if exists pomTable;
create temporary table pomTable 
	(formovna char(1),
	cena decimal(20,6), -- nepouzivam unsigned
	hmotnost decimal(12,2) -- nepouzivam unsgined
);

insert into pomTable (formovna, cena, hmotnost)
SELECT 
	seznam_modelu.Formovna,
    sum(returnPresnouCenuZaKus(fyzkusy.Id_kusu))  as 'Cena Kč',
    sum(getHmotnostFyzKusu(fyzkusy.Id_kusu)) as Hmotnost
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
	fyzkusy.Odlito = true and
    fyzkusy.Datum_odliti between od and do_

group by seznam_modelu.Formovna;

-- hmotnost a cena zmetků, kterou musím odečíst. PODLE Datum_zadani_zmetku
insert into pomTable (formovna, cena, hmotnost)
SELECT 
	seznam_modelu.Formovna,
    sum(returnPresnouCenuZaKus(fyzkusy.Id_kusu)) * (-1) as 'Cena Kč',
    sum(getHmotnostFyzKusu(fyzkusy.Id_kusu)) * (-1) as Hmonost
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
	fyzkusy.Odlito = true and
    fyzkusy.Datum_zadani_zmetku between od and do_ and
    fyzkusy.Zmetek = true
group by seznam_modelu.Formovna;

-- select
 select 
 	formovna as 'Formovna', 
	 sum(cena) as 'Cena Kč',
	  sum(hmotnost) as 'Hmotnost'
 from pomTable
group by formovna with rollup;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisOdlitychKusuOdDo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisOdlitychKusuOdDo`(od date, do_ date, formovna char(1), vlastni_material_reg_ex varchar(500))
BEGIN
declare pom_formovna char(2);

if (formovna like ' %') then set formovna = ''; -- zbytecny
end if;

set pom_formovna = CONCAT(formovna,'%');

SELECT 
	seznam_zakazek.Id_zakazky as `ID zakázky`,
    `zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
    `seznam_modelu`.`Cislo_modelu` as `Číslo modelu`,
	sum(fyzkusy.Odlito) as Odlito,
    sum(fyzkusy.Odlito * returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as `Celkem kč`, -- celkem kc
    sum(fyzkusy.Odlito * getHmotnostFyzKusu(fyzkusy.Id_kusu)) as `Celkem kg`, -- celkem kg
	seznam_modelu.Material_vlastni as `Materiál vlastní`,
	seznam_modelu.Formovna as `Formovna`
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
	fyzkusy.Datum_odliti between od and do_ and
    seznam_modelu.Formovna like pom_formovna and
	seznam_modelu.Material_vlastni rlike vlastni_material_reg_ex and
    fyzkusy.Odlito = true
group by seznam_zakazek.Id_zakazky
order by `zakaznici`.`Jmeno_zakaznika`, `seznam_modelu`.`Cislo_modelu`;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisPolozekSOdhadHmot` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisPolozekSOdhadHmot`()
BEGIN
SELECT 
	`seznam_zakazek`.`Id_zakazky` as `ID zakázky`,
    seznam_modelu.Id_modelu as `ID modelu`,
	`zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
	`seznam_modelu`.`Cislo_modelu` as `Číslo modelu`,
	`seznam_modelu`.`Jmeno_modelu` as `Jméno modelu`,
	`seznam_modelu`.`Hmotnost`
FROM (pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		join pomdb.zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika)
where
	`seznam_modelu`.`IsOdhadHmot` = true
order by `zakaznici`.`Jmeno_zakaznika` asc, `seznam_modelu`.`Cislo_modelu`; 
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisRozpracovaneVyroby` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisRozpracovaneVyroby`()
BEGIN
SELECT 
	seznam_zakazek.Id_zakazky as `ID zakázky`,
    `zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
    `seznam_modelu`.`Cislo_modelu` as `Číslo modelu`,
	count(*) as 'Počet kusů',
    sum(returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as 'Kč celkem',
	sum(getHmotnostFyzKusu(fyzkusy.Id_kusu)) as 'Celk. hmotnost'
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
    fyzkusy.Zmetek = false and
	fyzkusy.Vycisteno = false and 
	fyzkusy.Odlito = true 
group by  seznam_zakazek.Id_zakazky
order by `zakaznici`.`Jmeno_zakaznika` , `seznam_modelu`.`Cislo_modelu`;

SELECT 
	'Celkem',
    '',
    '',
	count(*) as 'Počet kusů',
    sum(returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as 'Kč celkem',
	sum(getHmotnostFyzKusu(fyzkusy.Id_kusu)) as 'Celk. hmotnost'
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
    fyzkusy.Zmetek = false and
	fyzkusy.Vycisteno = false and 
	fyzkusy.Odlito = true;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisStavNeuzavrenychZakazek` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisStavNeuzavrenychZakazek`(idZakazky int UNSIGNED, jmeno varchar(30), cisloModelu varchar(30), nazevModelu varchar(30), idModelu int UNSIGNED, datumZakazky date, cisloObjednavky varchar(30))
BEGIN
declare pomJmeno varchar(31);
declare pomCisloModelu varchar(31);
declare pomNazevModelu varchar(31);
declare pomCisloObjednavky varchar(31);

if (jmeno like ' %') then set jmeno = '';
end if;
if (cisloModelu like ' %') then set cisloModelu = '';
end if;
if (nazevModelu like ' %') then set nazevModelu = '';
end if;
if (cisloObjednavky like ' %') then set cisloObjednavky = '';
end if;
set pomJmeno = CONCAT(jmeno,'%');
set pomCisloModelu = CONCAT(cisloModelu,'%');
set pomNazevModelu = CONCAT(nazevModelu,'%');
set pomCisloObjednavky =  CONCAT(cisloObjednavky,'%');

drop temporary table if exists zmetci;
create temporary table if not exists zmetci (IdZakazkyZmetku int unsigned, pocetZmetku mediumint unsigned);
insert into zmetci (IdZakazkyZmetku, pocetZmetku) select seznam_zakazek.Id_zakazky, sum(Zmetek) from fyzkusy left join seznam_zakazek on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)  where Uzavreno = false group by seznam_zakazek.Id_zakazky;
alter table zmetci add primary key (IdZakazkyZmetku);

select
	seznam_zakazek.Id_zakazky as 'ID zakázky',
	Jmeno_zakaznika as 'Jméno zákazníka',
	Cislo_modelu as 'Číslo modelu',
	seznam_zakazek.Cislo_objednavky as 'Číslo objed.',
	seznam_modelu.Material_vlastni as 'Materiál vl.',
	seznam_zakazek.Termin_expedice as 'Termín exp.',
	Pocet_kusu as 'Objed.',
	sum(Odlito) as 'Odlito',
	sum(Vycisteno) as 'Vyčiš.',
	sum(Expedovano) as 'Exped.',
	pocetZmetku as  'Zmetků'
from (((seznam_zakazek left join zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
		left join seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		left join fyzkusy on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky))
		left join zmetci on (seznam_zakazek.Id_zakazky = zmetci.IdZakazkyZmetku)
where
	`zakaznici`.`Jmeno_zakaznika` like pomJmeno and
	`seznam_modelu`.`Cislo_modelu` like pomCisloModelu and
	`seznam_modelu`.`Jmeno_modelu` like pomNazevModelu and
	`seznam_zakazek`.`Cislo_objednavky` like pomCisloObjednavky and
	(case !isnull(datumZakazky) and !isnull(seznam_zakazek.Datum_prijeti_zakazky)
		when true then  seznam_zakazek.Datum_prijeti_zakazky >= datumZakazky
		else true
		end ) and
	(case idModelu
		when 0 then true
		else `seznam_modelu`.Id_modelu = idModelu
		end ) and
	(case idZakazky
		when 0 then true
		else `seznam_zakazek`.Id_zakazky = idZakazky
		end ) and
    Zmetek = false and
	Uzavreno = false
group by seznam_zakazek.Id_zakazky
order by zakaznici.Jmeno_zakaznika, Cislo_modelu
limit 500;

drop temporary table if exists zmetci;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisStavNeuzavrenychZakazek_short` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisStavNeuzavrenychZakazek_short`(idZakazky int UNSIGNED, jmeno varchar(30), cisloModelu varchar(30), nazevModelu varchar(30), idModelu int UNSIGNED, datumZakazky date, cisloObjednavky varchar(30))
BEGIN
declare pomJmeno varchar(31);
declare pomCisloModelu varchar(31);
declare pomNazevModelu varchar(31);
declare pomCisloObjednavky varchar(31);

if (jmeno like ' %') then set jmeno = '';
end if;
if (cisloModelu like ' %') then set cisloModelu = '';
end if;
if (nazevModelu like ' %') then set nazevModelu = '';
end if;
if (cisloObjednavky like ' %') then set cisloObjednavky = '';
end if;
set pomJmeno = CONCAT(jmeno,'%');
set pomCisloModelu = CONCAT(cisloModelu,'%');
set pomNazevModelu = CONCAT(nazevModelu,'%');
set pomCisloObjednavky =  CONCAT(cisloObjednavky,'%');

drop table if exists zmetci;
create temporary table if not exists zmetci (IdZakazkyZmetku int unsigned, pocetZmetku mediumint unsigned);
insert into zmetci (IdZakazkyZmetku, pocetZmetku) select seznam_zakazek.Id_zakazky, sum(Zmetek) from fyzkusy left join seznam_zakazek on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)  where Uzavreno = false group by seznam_zakazek.Id_zakazky;
alter table zmetci add primary key (IdZakazkyZmetku);



select
	Pocet_kusu as 'Obj.',
	sum(Odlito) as 'Odl.',
	sum(Vycisteno) as 'Vyč.',
	sum(Expedovano) as 'Exp.',
	pocetZmetku as  'Zm.'
from (((seznam_zakazek left join zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
		left join seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		left join fyzkusy on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky))
		left join zmetci on (seznam_zakazek.Id_zakazky = zmetci.IdZakazkyZmetku)
where
	`zakaznici`.`Jmeno_zakaznika` like pomJmeno and
	`seznam_modelu`.`Cislo_modelu` like pomCisloModelu and
	`seznam_modelu`.`Jmeno_modelu` like pomNazevModelu and
	`seznam_zakazek`.`Cislo_objednavky` like pomCisloObjednavky and
	(case !isnull(datumZakazky) and !isnull(seznam_zakazek.Datum_prijeti_zakazky)
		when true then  seznam_zakazek.Datum_prijeti_zakazky >= datumZakazky
		else true
		end ) and
	(case idModelu
		when 0 then true
		else `seznam_modelu`.Id_modelu = idModelu
		end ) and
	(case idZakazky
		when 0 then true
		else `seznam_zakazek`.Id_zakazky = idZakazky
		end ) and
    Zmetek = false
group by seznam_zakazek.Id_zakazky
order by zakaznici.Jmeno_zakaznika
limit 500;

drop table if exists zmetci;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `VypisStavZakazek` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `VypisStavZakazek`(jmeno varchar(30), cisloModelu varchar(30), nazevModelu varchar(30), idModelu int UNSIGNED, datumZakazky date)
BEGIN
declare pomJmeno varchar(31);
declare pomCisloModelu varchar(31);
declare pomNazevModelu varchar(31);
if (jmeno like ' %') then set jmeno = '';
end if;
if (cisloModelu like ' %') then set cisloModelu = '';
end if;
if (nazevModelu like ' %') then set nazevModelu = '';
end if;
set pomJmeno = CONCAT(jmeno,'%');
set pomCisloModelu = CONCAT(cisloModelu,'%');
set pomNazevModelu = CONCAT(nazevModelu,'%');
drop temporary table if exists zmetci;
create temporary table if not exists zmetci (IdZakazkyZmetku int unsigned, pocetZmetku mediumint unsigned);
insert into zmetci (IdZakazkyZmetku, pocetZmetku) select seznam_zakazek.Id_zakazky, sum(Zmetek) from fyzkusy left join seznam_zakazek on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky) group by seznam_zakazek.Id_zakazky;
alter table zmetci add primary key (IdZakazkyZmetku);
select
	seznam_zakazek.Id_zakazky as 'ID zakázky',
	Jmeno_zakaznika as 'Jméno zákazníka',
	Cislo_modelu as 'Číslo modelu',
	Jmeno_modelu as 'Název modelu',
	seznam_zakazek.Cislo_objednavky as 'Číslo objed.',
	seznam_modelu.Material_vlastni as 'Materiál vl.',
	seznam_zakazek.Termin_expedice as 'Termín exp.',
	Pocet_kusu as 'Objed.',
	sum(Odlito) as 'Odlito',
	sum(Vycisteno) as 'Vyčiš.',
	sum(Expedovano) as 'Exped.',
	pocetZmetku as  'Zmetků'
from (((seznam_zakazek left join zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
		left join seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		left join fyzkusy on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky))
		left join zmetci on (seznam_zakazek.Id_zakazky = zmetci.IdZakazkyZmetku)
where
	`zakaznici`.`Jmeno_zakaznika` like pomJmeno and
	`seznam_modelu`.`Cislo_modelu` like pomCisloModelu and
	`seznam_modelu`.`Jmeno_modelu` like pomNazevModelu and
	(case !isnull(datumZakazky) and !isnull(seznam_zakazek.Datum_prijeti_zakazky)
		when true then  seznam_zakazek.Datum_prijeti_zakazky >= datumZakazky
		else true
		end ) and
	(case idModelu
		when 0 then true
		else `seznam_modelu`.Id_modelu = idModelu
		end ) and
    Zmetek = false
group by seznam_zakazek.Id_zakazky
order by zakaznici.Jmeno_zakaznika, Cislo_modelu;
drop temporary table if exists zmetci;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisVinikyVKgKc` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisVinikyVKgKc`(od date, do_ date)
BEGIN
drop temporary table if exists pomTable;
create temporary table pomTable (Id_vinika smallint(5),vinik varchar(45), kusu smallint, Hmotnost decimal(10,2), Norma decimal(9,1), cena decimal(17,6));
insert into pomTable (Id_vinika, vinik, kusu, Hmotnost, Norma, cena)
SELECT
	vinici.Id_vinika,
	vinici.Jmeno_vinika as 'Jméno viníka',
	sum(fyzkusy.Zmetek) as 'kusů',
	sum(fyzkusy.Zmetek * getHmotnostFyzKusu(fyzkusy.Id_kusu)) as 'Hmotnost celkem',
	sum(fyzkusy.Zmetek * getNormaSlevaceFyzKusu(fyzkusy.Id_kusu)) as 'Norma celkem',
	sum(fyzkusy.Zmetek * returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as 'Cena celkem'
	
FROM ((pomdb.seznam_zakazek
		join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		join pomdb.zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
		join (((`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu))	
										left join vinici on (vinici.Id_vinika = zmetky_vady.Id_vinika))	
										left join vady on (vady.idvady = zmetky_vady.Id_vady))
				on(seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)

where 
	fyzkusy.Zmetek = true and
	Datum_zadani_zmetku between od and do_
group by seznam_zakazek.Id_zakazky, vinici.Id_vinika;


-- seznam viniku
SELECT
	vinik as 'Jméno viníka',
	sum(kusu) as 'kusů',
	sum(Hmotnost) as 'Hmotnost celkem',
	sum(Norma) as 'Norma celkem',
	sum(cena) as 'Cena celkem'
FROM
	pomTable
group by pomTable.Id_vinika
order by vinik;


-- celkovy soucet
select 'Celkem', sum(kusu), sum(Hmotnost), sum(Norma),  sum(cena)
from pomTable;


drop temporary table if exists pomTable;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisVycistenychKusuOdDo` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisVycistenychKusuOdDo`(od date, do_ date)
BEGIN
(SELECT 
	fyzkusy.Datum_vycisteni as 'Datum vyčištění',
	seznam_zakazek.Id_zakazky as `ID zakázky`,
    `zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
    `seznam_modelu`.`Cislo_modelu` as 'Číslo modelu',
	sum(fyzkusy.Vycisteno) as 'Vyčištěno ks',
	sum(returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as 'Cena celkem',
	sum(getHmotnostFyzKusu(fyzkusy.Id_kusu)) as 'Kg celkem'
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
	fyzkusy.Odlito = true
	and fyzkusy.Vycisteno = true
	and fyzkusy.Datum_vycisteni between od and do_
    -- and seznam_zakazek.Uzavreno = false
group by  seznam_zakazek.Id_zakazky, fyzkusy.Datum_vycisteni)

union all

(select null,null,`zakaznici`.`Jmeno_zakaznika`,'Celkem',
	sum(fyzkusy.Vycisteno) as 'Vyčištěno kusů',
	sum(returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as 'Cena celkem',
	sum(getHmotnostFyzKusu(fyzkusy.Id_kusu)) as 'Kg celkem'
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
	fyzkusy.Odlito = true
	and fyzkusy.Vycisteno = true
	and fyzkusy.Datum_vycisteni between od and do_
group by zakaznici.Jmeno_zakaznika)

 order by `Jméno zákazníka`, `ID zakázky` desc;

-- celkovy soucet
SELECT 
	'Celkem',
	'',
    '',
    '',
	sum(fyzkusy.Vycisteno) as 'Vyčištěno kusů',
	sum(returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as 'Cena celkem',
	sum(getHmotnostFyzKusu(fyzkusy.Id_kusu)) as 'Kg celkem'
FROM
    ((pomdb.seznam_zakazek
    join pomdb.seznam_modelu ON (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
    join pomdb.zakaznici ON (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
        join
    fyzkusy ON (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where
	fyzkusy.Odlito = true
	and fyzkusy.Vycisteno = true
	and fyzkusy.Datum_vycisteni between od and do_
;

 
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisZmetky` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisZmetky`(od date, do_ date)
BEGIN
SELECT 
	`zakaznici`.`Jmeno_zakaznika` as 'Jméno zákazníka',
	`seznam_modelu`.`Jmeno_modelu` as 'Jméno modelu',
	`seznam_modelu`.`Cislo_modelu` as 'Číslo modelu', 
	fyzkusy.Datum_zadani_zmetku as 'Datum zmetku',
	vinici.Jmeno_vinika as 'Jméno viníka',
	vady.vada as 'Vada',
	count(*) as 'ks',
	`seznam_modelu`.`Norma_slevac` as 'Norma',
	sum(fyzkusy.Zmetek * getNormaSlevaceFyzKusu(fyzkusy.Id_kusu)) as 'Norma c.',
	`seznam_modelu`.`Hmotnost` as 'Hmot./ks.',
	sum(fyzkusy.Zmetek * getHmotnostFyzKusu(fyzkusy.Id_kusu)) as 'Hmot. celk.',
	`seznam_modelu`.`Material_vlastni` as 'Vl. mater.',
	seznam_zakazek.Presna_cena_za_kus as 'cena/ks',
	sum(fyzkusy.Zmetek * returnPresnouCenuZaKus(fyzkusy.Id_kusu)) as 'Cena celk.'
	
FROM ((pomdb.seznam_zakazek
		join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
		join pomdb.zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
		join (((`pomdb`.`fyzkusy` left join zmetky_vady on (`fyzkusy`.`Id_kusu` = zmetky_vady.Id_kusu))	
										left join vinici on (vinici.Id_vinika = zmetky_vady.Id_vinika))	
										left join vady on (vady.idvady = zmetky_vady.Id_vady))
				on(seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)

where 
	fyzkusy.Zmetek = true and
	Datum_zadani_zmetku between od and do_
group by `seznam_zakazek`.`Id_zakazky`, fyzkusy.Datum_zadani_zmetku
order by fyzkusy.Datum_zadani_zmetku,`zakaznici`.`Jmeno_zakaznika` asc, `seznam_zakazek`.`Id_zakazky` desc;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `vypisZpozdeneVyroby` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `vypisZpozdeneVyroby`(od date)
BEGIN
SELECT 
	seznam_zakazek.Termin_expedice as `Termín expedice`,
	`seznam_zakazek`.`Id_zakazky` as `ID zakázky`,
	`zakaznici`.`Jmeno_zakaznika` as `Jméno zákazníka`,
	`seznam_modelu`.`Cislo_modelu` as `Číslo modelu`,
	`seznam_zakazek`.`Pocet_kusu` as `Objednáno`,
	sum(fyzkusy.Odlito) as `Odlito`,
	sum(fyzkusy.Vycisteno) as `Vyčištěno`,
	sum(fyzkusy.Expedovano) as `Expedováno`	
FROM 
	((pomdb.seznam_zakazek join pomdb.seznam_modelu on (seznam_zakazek.Id_modelu = seznam_modelu.Id_modelu))
	 join pomdb.zakaznici on (seznam_zakazek.Id_zakaznika = zakaznici.Id_zakaznika))
	 join fyzkusy on (fyzkusy.Id_zakazky = seznam_zakazek.Id_zakazky)
where 
	fyzkusy.Zmetek = false
	and 
	(case !isnull(od)
		when true then  seznam_zakazek.Termin_expedice <= od
		else true
		end)
	and seznam_zakazek.Uzavreno = false
group by seznam_zakazek.Termin_expedice, seznam_zakazek.Id_zakazky
order by seznam_zakazek.Termin_expedice, seznam_zakazek.Id_zakazky
;


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `zadejDatumVycistenehoKusu` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `zadejDatumVycistenehoKusu`(idKusu int, datumVycisteni Date)
BEGIN
UPDATE `pomdb`.`fyzkusy`
SET
`Vycisteno` = true,
`Datum_vycisteni` = datumVycisteni
WHERE `Id_kusu` = idKusu;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `zadejDilciTerminy` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `zadejDilciTerminy`(idZakazky int unsigned, dilciTermin date, pocetKusuKDodani smallint unsigned, isSplneno tinyint(1) unsigned)
BEGIN
declare existuje smallint unsigned;
if (idZakazky is not null and idZakazky > 0)then
	select count(*) from dilci_terminy where Id_zakazky = idZakazky and dilci_termin = dilciTermin into existuje;
	if(existuje = 0 and pocetKusuKDodani > 0)then 
		INSERT INTO `pomdb`.`dilci_terminy`
			(`Id_zakazky`, `dilci_termin`, `pocet_kusu`, `splnen`)
		VALUES
			(idZakazky, dilciTermin, pocetKusuKDodani, isSplneno);
	else 
		if (pocetKusuKDodani = 0 and existuje > 0)then
			delete from `pomdb`.`dilci_terminy` where Id_zakazky = idZakazky and dilci_termin = dilciTermin;
		else
			select pocet_kusu from dilci_terminy where Id_zakazky = idZakazky and dilci_termin = dilciTermin into existuje;
			UPDATE `pomdb`.`dilci_terminy`
			SET
				`pocet_kusu` = pocetKusuKDodani,
				`splnen` = isSplneno
			WHERE `Id_zakazky` = idZakazky and dilci_termin = dilciTermin;
		end if;
	end if;
end if;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `zadejOdlitek` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `zadejOdlitek`(idKusu int(10) unsigned, isOdlito tinyint(1), datumOdliti date, isVycisteno tinyint(1), datumVycisteni date,
 isExpedovano tinyint(1), datumExpedice date, isZmetek tinyint(1), datumZadaniZmetku date, cisloTavby varchar(10), cisloFaktury varchar(19), teplotaLiti varchar(20) )
BEGIN
declare idModelu int(10) unsigned;
declare isOdlitoFyzKus tinyint(1) unsigned;

select Id_modelu 
from seznam_zakazek join fyzkusy on (seznam_zakazek.Id_zakazky = fyzkusy.Id_zakazky)
where fyzkusy.Id_kusu = idKusu
into idModelu;

select Odlito from fyzkusy where fyzkusy.Id_kusu = idKusu into isOdlitoFyzKus;


if(idKusu is not null and idKusu > 0) then
	-- zadam model pouze kdyz prave ted oznacuji jako ze odlito = true nebo kdyz je odlito = false aktualizuji dany model (na default model zakazky)
	if (isOdlito = true and isOdlito != isOdlitoFyzKus or isOdlito = false) then
		UPDATE `pomdb`.`fyzkusy`
		SET
			Id_modelu_odlito = idModelu
		WHERE 
			`Id_kusu` = idKusu;
	end if;


	if(isOdlito is not null) then 
		UPDATE `pomdb`.`fyzkusy`
		SET
			`Odlito` = isOdlito
		WHERE
			`Id_kusu` = idKusu;
	end if;

	if(isVycisteno is not null) then
		UPDATE
			`pomdb`.`fyzkusy`
		SET
			`Vycisteno` = isVycisteno
		WHERE `Id_kusu` = idKusu;
	end if;

	if(isExpedovano is not null) then 
		UPDATE
			`pomdb`.`fyzkusy`
		SET
			fyzkusy.Expedovano = isExpedovano
		WHERE `Id_kusu` = idKusu;
	end if;

	if(isZmetek is not null) then
		UPDATE
			`pomdb`.`fyzkusy`
		SET
			fyzkusy.Zmetek = isZmetek
		WHERE `Id_kusu` = idKusu;
	end if;

	if (isZmetek = false)then
		set datumZadaniZmetku = null;
	end if;

	UPDATE `pomdb`.`fyzkusy`
	SET
		fyzkusy.Datum_odliti = datumOdliti,
		fyzkusy.Datum_vycisteni = datumVycisteni,
		fyzkusy.Datum_expedice = datumExpedice,
		fyzkusy.Datum_zadani_zmetku = datumZadaniZmetku,
		fyzkusy.Cislo_tavby = cisloTavby,
		fyzkusy.Cislo_faktury = cisloFaktury,
		fyzkusy.Teplota_liti = teplotaLiti
	WHERE 
		`Id_kusu` = idKusu;
end if;


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `zadejPlanovanyDatumLiti` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `zadejPlanovanyDatumLiti`(idKusu int(10) unsigned, datumLiti date)
BEGIN
UPDATE `pomdb`.`fyzkusy`
SET
	Datum_liti = datumLiti
WHERE 
	`Id_kusu` = idKusu;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `zadejUdajeOZmetku` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `zadejUdajeOZmetku`(idKusu int(10) unsigned, idVinika smallint(10) unsigned, idVady smallint(10) unsigned, OUT prubeh tinyInt(1))
BEGIN
declare pom int;
set prubeh = true;

if(isnull(idKusu) || idKusu = 0)
then set prubeh = false;
end if;

if((idVinika is null or idVady is null) and prubeh)
then delete from `pomdb`.`zmetky_vady` where zmetky_vady.Id_kusu = idKusu;
else
	select count(*) from vinici where vinici.Id_vinika = idVinika  into pom;
	if (pom != 1) then set prubeh = false;
	end if;
	select count(*) from vady where vady.idvady = idVady into pom ;
	if (pom != 1) then set prubeh = false;
	end if;

	if(prubeh)then
	select count(*) from `pomdb`.`zmetky_vady` where `Id_kusu`= idKusu into pom ;
		if(pom = 0) then
			INSERT INTO `pomdb`.`zmetky_vady`
			(`Id_kusu`,
			`Id_vinika`,
			`Id_vady`)
			VALUES
			(idKusu,
			idVinika,
			idVady);
		else 
			UPDATE `pomdb`.`zmetky_vady`
			SET
			`Id_vinika` = idVinika,
			`Id_vady` = idVady
			WHERE `Id_kusu` = idKusu;
		end if;
	end if;
end if;


END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `zadej_cislo_faktury_cislo_tavby_prohlizeci` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `zadej_cislo_faktury_cislo_tavby_prohlizeci`(idKusu int(10) unsigned, cisloTavby varchar(10), cisloFaktury varchar(19))
BEGIN

if(idKusu is not null and idKusu > 0) then
	UPDATE `pomdb`.`fyzkusy`
	SET
		fyzkusy.Cislo_tavby = cisloTavby,
		fyzkusy.Cislo_faktury = cisloFaktury
	WHERE 
		`Id_kusu` = idKusu;
end if;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 DROP PROCEDURE IF EXISTS `zalohaDatabaze` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8 */ ;
/*!50003 SET character_set_results = utf8 */ ;
/*!50003 SET collation_connection  = utf8_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,STRICT_ALL_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ALLOW_INVALID_DATES,ERROR_FOR_DIVISION_BY_ZERO,TRADITIONAL,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `zalohaDatabaze`()
BEGIN

SELECT * FROM pomdb.seznam_zakazek order by Id_zakazky;
SELECT * FROM pomdb.zakaznici order by Id_zakaznika;
SELECT * FROM pomdb.seznam_modelu order by Id_modelu;
SELECT * FROM pomdb.fyzkusy order by Id_kusu;
select * from pomdb.dilci_terminy order by Id_zakazky, dilci_termin;
SELECT * FROM pomdb.vady order by idvady;
SELECT * FROM pomdb.vinici order by Id_vinika;
SELECT * FROM pomdb.zmetky_vady order by Id_kusu;
SELECT * FROM technologicka_karta_db.technologicka_karta order by idtechnologicka_karta;
END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-07 20:12:30
