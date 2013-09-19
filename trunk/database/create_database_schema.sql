--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.4
-- Dumped by pg_dump version 9.2.4
-- Started on 2013-09-19 13:08:08

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

DROP DATABASE "Messaggi";
--
-- TOC entry 1943 (class 1262 OID 16398)
-- Name: Messaggi; Type: DATABASE; Schema: -; Owner: jcw_dev
--

CREATE DATABASE "Messaggi" WITH TEMPLATE = template0 ENCODING = 'UTF8' LC_COLLATE = 'English_United States.1252' LC_CTYPE = 'English_United States.1252';


ALTER DATABASE "Messaggi" OWNER TO jcw_dev;

\connect "Messaggi"

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 5 (class 2615 OID 2200)
-- Name: public; Type: SCHEMA; Schema: -; Owner: postgres
--

CREATE SCHEMA public;


ALTER SCHEMA public OWNER TO postgres;

--
-- TOC entry 1944 (class 0 OID 0)
-- Dependencies: 5
-- Name: SCHEMA public; Type: COMMENT; Schema: -; Owner: postgres
--

COMMENT ON SCHEMA public IS 'standard public schema';


--
-- TOC entry 174 (class 3079 OID 11727)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 1946 (class 0 OID 0)
-- Dependencies: 174
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 173 (class 1259 OID 16449)
-- Name: active_session; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE active_session (
    id uuid NOT NULL,
    create_date time without time zone NOT NULL
);


ALTER TABLE public.active_session OWNER TO jcw_dev;

--
-- TOC entry 1947 (class 0 OID 0)
-- Dependencies: 173
-- Name: COLUMN active_session.create_date; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN active_session.create_date IS 'Note that all times in this column are in UTC.';


--
-- TOC entry 169 (class 1259 OID 16401)
-- Name: user; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE "user" (
    id bigint NOT NULL,
    name character varying(120) NOT NULL,
    email character varying(256) NOT NULL,
    phone character varying(50) NOT NULL,
    phone_parsed character varying(50) NOT NULL,
    password character varying(512) NOT NULL,
    locale character varying(10) NOT NULL
);


ALTER TABLE public."user" OWNER TO jcw_dev;

--
-- TOC entry 1948 (class 0 OID 0)
-- Dependencies: 169
-- Name: COLUMN "user".phone; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".phone IS 'Phone number as entered by the user.';


--
-- TOC entry 1949 (class 0 OID 0)
-- Dependencies: 169
-- Name: COLUMN "user".phone_parsed; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".phone_parsed IS 'User entered phone number with all non-numeric data stripped. 

* used for indexing purposes';


--
-- TOC entry 1950 (class 0 OID 0)
-- Dependencies: 169
-- Name: COLUMN "user".password; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".password IS 'SHA-512 salted password.';


--
-- TOC entry 172 (class 1259 OID 16437)
-- Name: user_msg_log; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE user_msg_log (
    id bigint NOT NULL,
    user_id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    msg_count integer NOT NULL
);


ALTER TABLE public.user_msg_log OWNER TO jcw_dev;

--
-- TOC entry 1951 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN user_msg_log.date; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN user_msg_log.date IS 'Note that all timezones are stored in UTC format.';


--
-- TOC entry 170 (class 1259 OID 16433)
-- Name: user_msg_log_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE user_msg_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_msg_log_id_seq OWNER TO jcw_dev;

--
-- TOC entry 1952 (class 0 OID 0)
-- Dependencies: 170
-- Name: user_msg_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE user_msg_log_id_seq OWNED BY user_msg_log.id;


--
-- TOC entry 171 (class 1259 OID 16435)
-- Name: user_msg_log_user_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE user_msg_log_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_msg_log_user_id_seq OWNER TO jcw_dev;

--
-- TOC entry 1953 (class 0 OID 0)
-- Dependencies: 171
-- Name: user_msg_log_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE user_msg_log_user_id_seq OWNED BY user_msg_log.user_id;


--
-- TOC entry 168 (class 1259 OID 16399)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE users_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.users_id_seq OWNER TO jcw_dev;

--
-- TOC entry 1954 (class 0 OID 0)
-- Dependencies: 168
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE users_id_seq OWNED BY "user".id;


--
-- TOC entry 1929 (class 2604 OID 16404)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- TOC entry 1930 (class 2604 OID 16440)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_msg_log ALTER COLUMN id SET DEFAULT nextval('user_msg_log_id_seq'::regclass);


--
-- TOC entry 1931 (class 2604 OID 16441)
-- Name: user_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_msg_log ALTER COLUMN user_id SET DEFAULT nextval('user_msg_log_user_id_seq'::regclass);


--
-- TOC entry 1937 (class 2606 OID 16453)
-- Name: pk-active-session; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY active_session
    ADD CONSTRAINT "pk-active-session" PRIMARY KEY (id);


--
-- TOC entry 1933 (class 2606 OID 16406)
-- Name: pk-user; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT "pk-user" PRIMARY KEY (id);


--
-- TOC entry 1935 (class 2606 OID 16443)
-- Name: pk-user_msg_log; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY user_msg_log
    ADD CONSTRAINT "pk-user_msg_log" PRIMARY KEY (id);


--
-- TOC entry 1938 (class 2606 OID 16444)
-- Name: fk-user-user_msg_log; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_msg_log
    ADD CONSTRAINT "fk-user-user_msg_log" FOREIGN KEY (user_id) REFERENCES "user"(id) ON UPDATE CASCADE;


--
-- TOC entry 1945 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-09-19 13:08:09

--
-- PostgreSQL database dump complete
--

