
CREATE TABLE IF NOT EXISTS bank.transaction
(
    id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    amount numeric(15,2) NOT NULL,
    date_trans date NOT NULL,
    account_id character varying(50) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT transaction_pkey PRIMARY KEY (id),
    CONSTRAINT account_fk FOREIGN KEY (account_id)
        REFERENCES bank.account (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS bank.transaction
    OWNER to postgres;