ALTER TABLE instrucoes ADD COLUMN cancelada TINYINT(1) DEFAULT 0;
ALTER TABLE instrucoes ADD COLUMN motivo_cancelamento VARCHAR(255);