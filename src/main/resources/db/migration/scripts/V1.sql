-- liquibase formatted sql
-- changeset chamados:V1 splitStatements:true endDelimiter:;

CREATE TABLE IF NOT EXISTS plano (
    plano_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    plano_nome VARCHAR(100) NOT NULL,
    plano_valor DECIMAL(10, 2) NOT NULL
);

CREATE TABLE IF NOT EXISTS tipo_usuario (
    tipo_usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_usuario_descricao VARCHAR(100) NOT NULL
);

INSERT INTO tipo_usuario (tipo_usuario_descricao)
VALUES ('cliente'), ('Funcionario'), ('Administrador');

CREATE TABLE IF NOT EXISTS usuario (
    usuario_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    usuario_nome VARCHAR(50) NOT NULL,
    usuario_email VARCHAR(100) NOT NULL,
    usuario_senha VARCHAR(100) NOT NULL,
    usuario_tipo_usuario BIGINT NOT NULL DEFAULT 1,
    CONSTRAINT usuario_previlegio FOREIGN KEY (usuario_tipo_usuario) REFERENCES tipo_usuario(tipo_usuario_id)
);

CREATE TABLE IF NOT EXISTS token_alteracao (
    token_alteracao_id bigint auto_increment,
	token_alteracao_usuario bigint not null,
    token_alteracao_confirmacao varchar(75) not null,
    token_alteracao_data_confirmacao timestamp not null,
    token_alteracao_final varchar(75) not null,
    token_alteracao_data_final timestamp,
	CONSTRAINT senha_primaria primary key(token_alteracao_id),
	CONSTRAINT senha_usuario_estrangeira foreign key(token_alteracao_usuario) references usuario(usuario_id),
	CONSTRAINT unico_por_usuario UNIQUE(token_alteracao_usuario)
);

CREATE TABLE IF NOT EXISTS cliente_plano (
    cliente_plano_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    cliente_plano_cliente BIGINT NOT NULL,
    plano_id BIGINT NOT NULL,
    CONSTRAINT plano_do_cliente FOREIGN KEY (cliente_plano_cliente) REFERENCES usuario(usuario_id),
    CONSTRAINT id_do_plano FOREIGN KEY (plano_id) REFERENCES plano(plano_id)
);

CREATE TABLE IF NOT EXISTS endereco_usuario (
    endereco_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    endereco_usuario BIGINT NOT NULL,
    endereco_numero BIGINT NOT NULL,
    endereco_rua VARCHAR(100) NOT NULL,
    endereco_bairro VARCHAR(100) NOT NULL,
    endereco_cidade VARCHAR(100) NOT NULL,
    endereco_cep VARCHAR(25) NOT NULL,
    endereco_uf CHAR(2) NOT NULL,
    CONSTRAINT endereco_do_usuario FOREIGN KEY (endereco_usuario) REFERENCES usuario(usuario_id)
);

CREATE TABLE IF NOT EXISTS tipo_atividade (
    tipo_atividade_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    tipo_atividade_descricao VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS atividade (
    atividade_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    atividade_tipo BIGINT NOT NULL,
    atividade_data TIMESTAMP NOT NULL,
    atividade_instrutor BIGINT NOT NULL,
    CONSTRAINT tipo_da_atividade FOREIGN KEY (atividade_tipo) REFERENCES tipo_atividade(tipo_atividade_id)
);

CREATE TABLE IF NOT EXISTS agendamento (
    usuario_atividade_id BIGINT AUTO_INCREMENT PRIMARY KEY,
    atividade_id BIGINT NOT NULL,
    usuario_atividade_cliente BIGINT NOT NULL,
    CONSTRAINT atividade_cliente FOREIGN KEY (usuario_atividade_cliente) REFERENCES usuario(usuario_id),
    CONSTRAINT atividade_estrangeira_id FOREIGN KEY (atividade_id) REFERENCES atividade(atividade_id),
    CONSTRAINT usuario_uma_vez_na_atividade UNIQUE (atividade_id, usuario_atividade_cliente)
);