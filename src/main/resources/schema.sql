DROP TABLE IF EXISTS wallet;

CREATE TABLE wallet (
                        id INT AUTO_INCREMENT  PRIMARY KEY,
                        amount_currency VARCHAR(3) NOT NULL,
                        amount_value VARCHAR(10) NOT NULL
);

INSERT INTO wallet (amount_currency, amount_value) VALUES ('EUR', '0.00'), ('USD', '0.00'), ('EUR', '0.00');