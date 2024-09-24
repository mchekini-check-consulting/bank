
CREATE TABLE IF NOT EXISTS bank.account
(
    id character varying(50) NOT NULL,
    "accountNumber" character varying(50) COLLATE pg_catalog."default" NOT NULL,
    balance numeric(15,2) NOT NULL,
    customer_id character varying(50),
    CONSTRAINT account_pkey PRIMARY KEY (id),
    CONSTRAINT "FK_CUSTOMER_ID" FOREIGN KEY (customer_id)
        REFERENCES bank.customer (id) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION
        NOT VALID
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS bank.account
    OWNER to postgres;