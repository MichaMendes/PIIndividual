DROP DATABASE IF EXISTS sisguard;
CREATE DATABASE IF NOT EXISTS sisguard;

USE sisguard;

select * from empresa;

CREATE TABLE empresa (
    idEmpresa INT PRIMARY KEY AUTO_INCREMENT,
    nomeEmpresa VARCHAR(40) NOT NULL,
    cnpj CHAR(14) UNIQUE NOT NULL,
    email VARCHAR(100) NOT NULL,
    senha VARCHAR(18) NOT NULL,
    plano INT CHECK (plano IN (1, 2)),
    dataCriacao TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE darkstore (
    idDarkstore INT PRIMARY KEY AUTO_INCREMENT,
    nome VARCHAR(40),
    canalSlack VARCHAR(100),
    fkEmpresa INT NOT NULL,
    FOREIGN KEY (fkEmpresa) REFERENCES empresa(idEmpresa) ON DELETE CASCADE
);

CREATE TABLE metrica_ideal (
    idMetricaIdeal INT PRIMARY KEY AUTO_INCREMENT,
    alertaPadrao DOUBLE,
    criticaPadrao DOUBLE,
    alertaCPU DOUBLE,
    criticoCPU DOUBLE,
    alertaRAM DOUBLE,
    criticoRAM DOUBLE,
    alertaDisco DOUBLE,
    criticoDisco DOUBLE,
    fkDarkStore INT NOT NULL,
    FOREIGN KEY (fkDarkStore) REFERENCES darkstore(idDarkstore) ON DELETE CASCADE
);
CREATE TABLE maquina (
    idMaquina INT AUTO_INCREMENT PRIMARY KEY,
    numSerie VARCHAR(30),
    nomeMaquina VARCHAR(50),
    fkDarkStore INT NOT NULL,
    UNIQUE (idMaquina, fkDarkStore),
    FOREIGN KEY (fkDarkStore) REFERENCES darkstore(idDarkstore) ON DELETE CASCADE
);

CREATE TABLE funcionario (
    idFuncionario INT PRIMARY KEY AUTO_INCREMENT,
    nomeFuncionario VARCHAR(40),
    sobrenome VARCHAR(40),
    emailFuncionario VARCHAR(100),
    senha VARCHAR(45),
    cargo VARCHAR(45) NOT NULL,
    fkEmpresa INT NOT NULL,
    FOREIGN KEY (fkEmpresa) REFERENCES empresa(idEmpresa) ON DELETE CASCADE
);

CREATE TABLE endereco (
    idEndereco INT PRIMARY KEY AUTO_INCREMENT,
    cep CHAR(8) NOT NULL,
    estado VARCHAR(50) NOT NULL,
    cidade VARCHAR(50) NOT NULL,
    bairro VARCHAR(50) NOT NULL,
    rua VARCHAR(50) NOT NULL,
    numero INT NOT NULL,
    fkDarkstore INT NOT NULL,
    FOREIGN KEY (fkDarkstore) REFERENCES darkstore(idDarkstore) ON DELETE CASCADE
);

CREATE TABLE componente (
    idComponente INT AUTO_INCREMENT PRIMARY KEY,
    Maquina_idMaquina INT,
    nome VARCHAR(100),
    INDEX idx_Maquina_idMaquina (Maquina_idMaquina),
    FOREIGN KEY (Maquina_idMaquina) REFERENCES maquina(idMaquina) ON DELETE CASCADE
);

CREATE TABLE processos (
    idProcessos INT PRIMARY KEY AUTO_INCREMENT,
    dado VARCHAR(650),
    pid VARCHAR(20),
    desativar CHAR(3) DEFAULT "NAO",
    fkMaquina INT,
    FOREIGN KEY (fkMaquina) REFERENCES maquina(idMaquina) ON DELETE CASCADE
);
CREATE TABLE registro (
    idRegistro INT PRIMARY KEY AUTO_INCREMENT,
    dado VARCHAR(200) NOT NULL,
    dataRegistro DATETIME,
    fkComponente INT NOT NULL,
    componente_fkMaquina INT NOT NULL,
    componente_maquina_fkDarkstore INT NOT NULL,
    componente_maquina_fkMetrica_ideal INT NOT NULL,
    FOREIGN KEY (fkComponente) REFERENCES componente(idComponente) ON DELETE CASCADE,
    FOREIGN KEY (componente_fkMaquina) REFERENCES maquina(idMaquina) ON DELETE CASCADE,
    FOREIGN KEY (componente_maquina_fkDarkstore) REFERENCES darkstore(idDarkstore) ON DELETE CASCADE,
    FOREIGN KEY (componente_maquina_fkMetrica_ideal) REFERENCES metrica_ideal(idMetricaIdeal) ON DELETE CASCADE
);

CREATE TABLE alerta (
    idAlerta INT PRIMARY KEY AUTO_INCREMENT,
    descricao VARCHAR(250),
    fkMaquina INT,
    maquinaMetricaIdeal INT,
    maquinafkDarkstore INT,
    fkRegistro INT,
    registrofkComponente INT,
    registroComponentefkMaquina INT,
    registroComponenteMaquinafkDarkstore INT,
    registroComponenteMaquinafkMetricaIdeal INT,
    FOREIGN KEY (fkMaquina) REFERENCES maquina(idMaquina) ON DELETE CASCADE,
    FOREIGN KEY (maquinafkDarkstore) REFERENCES darkstore(idDarkstore) ON DELETE CASCADE,
    FOREIGN KEY (fkRegistro) REFERENCES registro(idRegistro) ON DELETE CASCADE,
    FOREIGN KEY (registrofkComponente) REFERENCES componente(idComponente) ON DELETE CASCADE,
    FOREIGN KEY (registroComponentefkMaquina) REFERENCES maquina(idMaquina) ON DELETE CASCADE,
    FOREIGN KEY (registroComponenteMaquinafkDarkstore) REFERENCES darkstore(idDarkstore) ON DELETE CASCADE,
    FOREIGN KEY (registroComponenteMaquinafkMetricaIdeal) REFERENCES metrica_ideal(idMetricaIdeal) ON DELETE CASCADE
);

select * from darkstore;

SET SQL_SAFE_UPDATES = 0;
