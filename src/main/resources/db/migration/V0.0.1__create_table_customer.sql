CREATE TABLE IF NOT EXISTS bank.customer
(
    id character varying(50) NOT NULL,
    name character varying(100) COLLATE pg_catalog."default" NOT NULL,
    email character varying(150) COLLATE pg_catalog."default" NOT NULL,
    CONSTRAINT customer_pkey PRIMARY KEY (id),
    CONSTRAINT u_email UNIQUE (email)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS bank.customer
    OWNER to postgres;