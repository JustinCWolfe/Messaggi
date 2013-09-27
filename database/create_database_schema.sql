--
-- PostgreSQL database dump
--

-- Dumped from database version 9.2.4
-- Dumped by pg_dump version 9.2.4
-- Started on 2013-09-27 09:53:49

SET statement_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 195 (class 3079 OID 11727)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2085 (class 0 OID 0)
-- Dependencies: 195
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 174 (class 1259 OID 16456)
-- Name: application; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application (
    id bigint NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.application OWNER TO jcw_dev;

--
-- TOC entry 2086 (class 0 OID 0)
-- Dependencies: 174
-- Name: TABLE application; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE application IS 'Applications will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 187 (class 1259 OID 16569)
-- Name: application_platform_device; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_device (
    application_platform_id bigint NOT NULL,
    device_id bigint NOT NULL
);


ALTER TABLE public.application_platform_device OWNER TO jcw_dev;

--
-- TOC entry 185 (class 1259 OID 16565)
-- Name: application_device_application_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_device_application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_device_application_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2087 (class 0 OID 0)
-- Dependencies: 185
-- Name: application_device_application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_device_application_id_seq OWNED BY application_platform_device.application_platform_id;


--
-- TOC entry 186 (class 1259 OID 16567)
-- Name: application_device_device_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_device_device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_device_device_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2088 (class 0 OID 0)
-- Dependencies: 186
-- Name: application_device_device_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_device_device_id_seq OWNED BY application_platform_device.device_id;


--
-- TOC entry 173 (class 1259 OID 16454)
-- Name: application_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2089 (class 0 OID 0)
-- Dependencies: 173
-- Name: application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_id_seq OWNED BY application.id;


--
-- TOC entry 182 (class 1259 OID 16495)
-- Name: application_platform; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform (
    id bigint NOT NULL,
    application_id bigint NOT NULL,
    platform_id bigint NOT NULL,
    token uuid NOT NULL
);


ALTER TABLE public.application_platform OWNER TO jcw_dev;

--
-- TOC entry 2090 (class 0 OID 0)
-- Dependencies: 182
-- Name: TABLE application_platform; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE application_platform IS 'Link table between application and platform.

An application can be linked to multiple platforms.
A platform can be used across multiple applications.';


--
-- TOC entry 2091 (class 0 OID 0)
-- Dependencies: 182
-- Name: COLUMN application_platform.token; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN application_platform.token IS 'Token used by Messaggi service generated off the application platform attributes.  This token will be generated when the service consumer registers their application for the mobile platforms it supports and provides the relevant application/platform identifiers (bundle id for example).

This token will be passed for subsequent service calls.';


--
-- TOC entry 180 (class 1259 OID 16491)
-- Name: application_platform_application_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_platform_application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_platform_application_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2092 (class 0 OID 0)
-- Dependencies: 180
-- Name: application_platform_application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_platform_application_id_seq OWNED BY application_platform.application_id;


--
-- TOC entry 192 (class 1259 OID 16646)
-- Name: application_platform_attributes; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_attributes (
    application_platform_id bigint NOT NULL,
    key character varying(20) NOT NULL,
    value character varying(1024) NOT NULL
);


ALTER TABLE public.application_platform_attributes OWNER TO jcw_dev;

--
-- TOC entry 191 (class 1259 OID 16644)
-- Name: application_platform_attributes_application_platform_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_platform_attributes_application_platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_platform_attributes_application_platform_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2093 (class 0 OID 0)
-- Dependencies: 191
-- Name: application_platform_attributes_application_platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_platform_attributes_application_platform_id_seq OWNED BY application_platform_attributes.application_platform_id;


--
-- TOC entry 190 (class 1259 OID 16630)
-- Name: application_platform_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_platform_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2094 (class 0 OID 0)
-- Dependencies: 190
-- Name: application_platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_platform_id_seq OWNED BY application_platform.id;


--
-- TOC entry 189 (class 1259 OID 16605)
-- Name: application_platform_key; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_key (
    key character varying(20) NOT NULL,
    description character varying(1024) NOT NULL
);


ALTER TABLE public.application_platform_key OWNER TO jcw_dev;

--
-- TOC entry 172 (class 1259 OID 16437)
-- Name: application_platform_msg_log; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_msg_log (
    id bigint NOT NULL,
    application_platform_id bigint NOT NULL,
    date timestamp without time zone NOT NULL,
    msg_count integer NOT NULL
);


ALTER TABLE public.application_platform_msg_log OWNER TO jcw_dev;

--
-- TOC entry 2095 (class 0 OID 0)
-- Dependencies: 172
-- Name: COLUMN application_platform_msg_log.date; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN application_platform_msg_log.date IS 'Note that all timezones are stored in UTC format.';


--
-- TOC entry 181 (class 1259 OID 16493)
-- Name: application_platform_platform_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_platform_platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_platform_platform_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2096 (class 0 OID 0)
-- Dependencies: 181
-- Name: application_platform_platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_platform_platform_id_seq OWNED BY application_platform.platform_id;


--
-- TOC entry 184 (class 1259 OID 16546)
-- Name: device; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device (
    id bigint NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.device OWNER TO jcw_dev;

--
-- TOC entry 194 (class 1259 OID 16680)
-- Name: device_attributes; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device_attributes (
    device_id bigint NOT NULL,
    key character varying(20) NOT NULL,
    value character varying(1024) NOT NULL
);


ALTER TABLE public.device_attributes OWNER TO jcw_dev;

--
-- TOC entry 2097 (class 0 OID 0)
-- Dependencies: 194
-- Name: COLUMN device_attributes.value; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN device_attributes.value IS 'Stores the value for the associated platform specific device identifier key.

For example:

An iOS phone will have a device token so the key will be token, the value will be XYZ.
An Android phone will have an device id so the key will be device_id, the value will be 123.';


--
-- TOC entry 193 (class 1259 OID 16678)
-- Name: device_attributes_device_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE device_attributes_device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.device_attributes_device_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2098 (class 0 OID 0)
-- Dependencies: 193
-- Name: device_attributes_device_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE device_attributes_device_id_seq OWNED BY device_attributes.device_id;


--
-- TOC entry 183 (class 1259 OID 16544)
-- Name: device_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE device_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.device_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2099 (class 0 OID 0)
-- Dependencies: 183
-- Name: device_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE device_id_seq OWNED BY device.id;


--
-- TOC entry 188 (class 1259 OID 16597)
-- Name: device_key; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device_key (
    key character varying(20) NOT NULL,
    description character varying(1024) NOT NULL
);


ALTER TABLE public.device_key OWNER TO jcw_dev;

--
-- TOC entry 2100 (class 0 OID 0)
-- Dependencies: 188
-- Name: COLUMN device_key.key; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN device_key.key IS 'Stores the key for the platform specific device identifier.

For example:

An iOS phone will have a device token so the key will be token.
An Android phone will have an device id so the key will be device_id.
';


--
-- TOC entry 176 (class 1259 OID 16464)
-- Name: platform; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE platform (
    id bigint NOT NULL,
    name character varying(100) NOT NULL,
    service_name character varying(100) NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.platform OWNER TO jcw_dev;

--
-- TOC entry 2101 (class 0 OID 0)
-- Dependencies: 176
-- Name: TABLE platform; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE platform IS 'Platforms will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 175 (class 1259 OID 16462)
-- Name: platform_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE platform_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.platform_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2102 (class 0 OID 0)
-- Dependencies: 175
-- Name: platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE platform_id_seq OWNED BY platform.id;


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
    locale character varying(10) NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public."user" OWNER TO jcw_dev;

--
-- TOC entry 2103 (class 0 OID 0)
-- Dependencies: 169
-- Name: TABLE "user"; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE "user" IS 'Users will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 2104 (class 0 OID 0)
-- Dependencies: 169
-- Name: COLUMN "user".phone; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".phone IS 'Phone number as entered by the user.';


--
-- TOC entry 2105 (class 0 OID 0)
-- Dependencies: 169
-- Name: COLUMN "user".phone_parsed; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".phone_parsed IS 'User entered phone number with all non-numeric data stripped. 

* used for indexing purposes';


--
-- TOC entry 2106 (class 0 OID 0)
-- Dependencies: 169
-- Name: COLUMN "user".password; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".password IS 'SHA-512 salted password.';


--
-- TOC entry 179 (class 1259 OID 16478)
-- Name: user_application; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE user_application (
    user_id bigint NOT NULL,
    application_id bigint NOT NULL
);


ALTER TABLE public.user_application OWNER TO jcw_dev;

--
-- TOC entry 2107 (class 0 OID 0)
-- Dependencies: 179
-- Name: TABLE user_application; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE user_application IS 'Link table between user and application.

A user can manage multiple applications but an application can only be managed by a single user.';


--
-- TOC entry 178 (class 1259 OID 16476)
-- Name: user_application_application_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE user_application_application_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_application_application_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2108 (class 0 OID 0)
-- Dependencies: 178
-- Name: user_application_application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE user_application_application_id_seq OWNED BY user_application.application_id;


--
-- TOC entry 177 (class 1259 OID 16474)
-- Name: user_application_user_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE user_application_user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_application_user_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2109 (class 0 OID 0)
-- Dependencies: 177
-- Name: user_application_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE user_application_user_id_seq OWNED BY user_application.user_id;


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
-- TOC entry 2110 (class 0 OID 0)
-- Dependencies: 170
-- Name: user_msg_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE user_msg_log_id_seq OWNED BY application_platform_msg_log.id;


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
-- TOC entry 2111 (class 0 OID 0)
-- Dependencies: 171
-- Name: user_msg_log_user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE user_msg_log_user_id_seq OWNED BY application_platform_msg_log.application_platform_id;


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
-- TOC entry 2112 (class 0 OID 0)
-- Dependencies: 168
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE users_id_seq OWNED BY "user".id;


--
-- TOC entry 1999 (class 2604 OID 16459)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application ALTER COLUMN id SET DEFAULT nextval('application_id_seq'::regclass);


--
-- TOC entry 2005 (class 2604 OID 16498)
-- Name: application_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform ALTER COLUMN application_id SET DEFAULT nextval('application_platform_application_id_seq'::regclass);


--
-- TOC entry 2006 (class 2604 OID 16499)
-- Name: platform_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform ALTER COLUMN platform_id SET DEFAULT nextval('application_platform_platform_id_seq'::regclass);


--
-- TOC entry 2007 (class 2604 OID 16632)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform ALTER COLUMN id SET DEFAULT nextval('application_platform_id_seq'::regclass);


--
-- TOC entry 2012 (class 2604 OID 16649)
-- Name: application_platform_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_attributes ALTER COLUMN application_platform_id SET DEFAULT nextval('application_platform_attributes_application_platform_id_seq'::regclass);


--
-- TOC entry 2010 (class 2604 OID 16572)
-- Name: application_platform_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_device ALTER COLUMN application_platform_id SET DEFAULT nextval('application_device_application_id_seq'::regclass);


--
-- TOC entry 2011 (class 2604 OID 16573)
-- Name: device_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_device ALTER COLUMN device_id SET DEFAULT nextval('application_device_device_id_seq'::regclass);


--
-- TOC entry 1997 (class 2604 OID 16440)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_msg_log ALTER COLUMN id SET DEFAULT nextval('user_msg_log_id_seq'::regclass);


--
-- TOC entry 1998 (class 2604 OID 16441)
-- Name: application_platform_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_msg_log ALTER COLUMN application_platform_id SET DEFAULT nextval('user_msg_log_user_id_seq'::regclass);


--
-- TOC entry 2008 (class 2604 OID 16549)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device ALTER COLUMN id SET DEFAULT nextval('device_id_seq'::regclass);


--
-- TOC entry 2013 (class 2604 OID 16683)
-- Name: device_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device_attributes ALTER COLUMN device_id SET DEFAULT nextval('device_attributes_device_id_seq'::regclass);


--
-- TOC entry 2001 (class 2604 OID 16467)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY platform ALTER COLUMN id SET DEFAULT nextval('platform_id_seq'::regclass);


--
-- TOC entry 1995 (class 2604 OID 16404)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('users_id_seq'::regclass);


--
-- TOC entry 2003 (class 2604 OID 16481)
-- Name: user_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_application ALTER COLUMN user_id SET DEFAULT nextval('user_application_user_id_seq'::regclass);


--
-- TOC entry 2004 (class 2604 OID 16482)
-- Name: application_id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_application ALTER COLUMN application_id SET DEFAULT nextval('user_application_application_id_seq'::regclass);


--
-- TOC entry 2057 (class 0 OID 16456)
-- Dependencies: 174
-- Data for Name: application; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application (id, active) FROM stdin;
\.


--
-- TOC entry 2113 (class 0 OID 0)
-- Dependencies: 185
-- Name: application_device_application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_device_application_id_seq', 1, false);


--
-- TOC entry 2114 (class 0 OID 0)
-- Dependencies: 186
-- Name: application_device_device_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_device_device_id_seq', 1, false);


--
-- TOC entry 2115 (class 0 OID 0)
-- Dependencies: 173
-- Name: application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_id_seq', 1, false);


--
-- TOC entry 2065 (class 0 OID 16495)
-- Dependencies: 182
-- Data for Name: application_platform; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform (application_id, platform_id, token, id) FROM stdin;
\.


--
-- TOC entry 2116 (class 0 OID 0)
-- Dependencies: 180
-- Name: application_platform_application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_platform_application_id_seq', 1, false);


--
-- TOC entry 2075 (class 0 OID 16646)
-- Dependencies: 192
-- Data for Name: application_platform_attributes; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_attributes (application_platform_id, key, value) FROM stdin;
\.


--
-- TOC entry 2117 (class 0 OID 0)
-- Dependencies: 191
-- Name: application_platform_attributes_application_platform_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_platform_attributes_application_platform_id_seq', 1, false);


--
-- TOC entry 2070 (class 0 OID 16569)
-- Dependencies: 187
-- Data for Name: application_platform_device; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_device (application_platform_id, device_id) FROM stdin;
\.


--
-- TOC entry 2118 (class 0 OID 0)
-- Dependencies: 190
-- Name: application_platform_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_platform_id_seq', 1, false);


--
-- TOC entry 2072 (class 0 OID 16605)
-- Dependencies: 189
-- Data for Name: application_platform_key; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_key (key, description) FROM stdin;
\.


--
-- TOC entry 2055 (class 0 OID 16437)
-- Dependencies: 172
-- Data for Name: application_platform_msg_log; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_msg_log (id, application_platform_id, date, msg_count) FROM stdin;
\.


--
-- TOC entry 2119 (class 0 OID 0)
-- Dependencies: 181
-- Name: application_platform_platform_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_platform_platform_id_seq', 1, false);


--
-- TOC entry 2067 (class 0 OID 16546)
-- Dependencies: 184
-- Data for Name: device; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device (id, active) FROM stdin;
\.


--
-- TOC entry 2077 (class 0 OID 16680)
-- Dependencies: 194
-- Data for Name: device_attributes; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device_attributes (device_id, key, value) FROM stdin;
\.


--
-- TOC entry 2120 (class 0 OID 0)
-- Dependencies: 193
-- Name: device_attributes_device_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('device_attributes_device_id_seq', 1, false);


--
-- TOC entry 2121 (class 0 OID 0)
-- Dependencies: 183
-- Name: device_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('device_id_seq', 1, false);


--
-- TOC entry 2071 (class 0 OID 16597)
-- Dependencies: 188
-- Data for Name: device_key; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device_key (key, description) FROM stdin;
\.


--
-- TOC entry 2059 (class 0 OID 16464)
-- Dependencies: 176
-- Data for Name: platform; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY platform (id, name, service_name, active) FROM stdin;
\.


--
-- TOC entry 2122 (class 0 OID 0)
-- Dependencies: 175
-- Name: platform_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('platform_id_seq', 1, false);


--
-- TOC entry 2052 (class 0 OID 16401)
-- Dependencies: 169
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY "user" (id, name, email, phone, phone_parsed, password, locale, active) FROM stdin;
2	Justin C. Wolfe	jcw_222@yahoo.com	617-549-2403	6175492403	password1	US-en	t
3	Armen Solakhyan	asolakhyan@hotmail.com	(617) 335-9866	6173359866	password2	US-en	t
\.


--
-- TOC entry 2062 (class 0 OID 16478)
-- Dependencies: 179
-- Data for Name: user_application; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY user_application (user_id, application_id) FROM stdin;
\.


--
-- TOC entry 2123 (class 0 OID 0)
-- Dependencies: 178
-- Name: user_application_application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('user_application_application_id_seq', 1, false);


--
-- TOC entry 2124 (class 0 OID 0)
-- Dependencies: 177
-- Name: user_application_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('user_application_user_id_seq', 1, false);


--
-- TOC entry 2125 (class 0 OID 0)
-- Dependencies: 170
-- Name: user_msg_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('user_msg_log_id_seq', 1, false);


--
-- TOC entry 2126 (class 0 OID 0)
-- Dependencies: 171
-- Name: user_msg_log_user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('user_msg_log_user_id_seq', 1, false);


--
-- TOC entry 2127 (class 0 OID 0)
-- Dependencies: 168
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('users_id_seq', 3, true);


--
-- TOC entry 2020 (class 2606 OID 16461)
-- Name: pk-application; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application
    ADD CONSTRAINT "pk-application" PRIMARY KEY (id);


--
-- TOC entry 2028 (class 2606 OID 16643)
-- Name: pk-application_platform; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "pk-application_platform" PRIMARY KEY (id);


--
-- TOC entry 2039 (class 2606 OID 16700)
-- Name: pk-application_platform_attributes; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_attributes
    ADD CONSTRAINT "pk-application_platform_attributes" PRIMARY KEY (application_platform_id, key);


--
-- TOC entry 2032 (class 2606 OID 16712)
-- Name: pk-application_platform_device; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_device
    ADD CONSTRAINT "pk-application_platform_device" PRIMARY KEY (application_platform_id, device_id);


--
-- TOC entry 2036 (class 2606 OID 16612)
-- Name: pk-application_platform_key; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_key
    ADD CONSTRAINT "pk-application_platform_key" PRIMARY KEY (key);


--
-- TOC entry 2030 (class 2606 OID 16551)
-- Name: pk-device; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device
    ADD CONSTRAINT "pk-device" PRIMARY KEY (id);


--
-- TOC entry 2041 (class 2606 OID 16688)
-- Name: pk-device_attributes; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device_attributes
    ADD CONSTRAINT "pk-device_attributes" PRIMARY KEY (device_id, key);


--
-- TOC entry 2034 (class 2606 OID 16604)
-- Name: pk-device_key; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device_key
    ADD CONSTRAINT "pk-device_key" PRIMARY KEY (key);


--
-- TOC entry 2022 (class 2606 OID 16469)
-- Name: pk-platform; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY platform
    ADD CONSTRAINT "pk-platform" PRIMARY KEY (id);


--
-- TOC entry 2015 (class 2606 OID 16406)
-- Name: pk-user; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT "pk-user" PRIMARY KEY (id);


--
-- TOC entry 2025 (class 2606 OID 16484)
-- Name: pk-user_application; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT "pk-user_application" PRIMARY KEY (application_id);


--
-- TOC entry 2018 (class 2606 OID 16443)
-- Name: pk-user_msg_log; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_msg_log
    ADD CONSTRAINT "pk-user_msg_log" PRIMARY KEY (id);


--
-- TOC entry 2016 (class 1259 OID 16677)
-- Name: fki_fk-application_platform-application_platform_msg_log; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "fki_fk-application_platform-application_platform_msg_log" ON application_platform_msg_log USING btree (application_platform_id);


--
-- TOC entry 2037 (class 1259 OID 16666)
-- Name: fki_fk-application_platform_key-application_platform_attributes; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "fki_fk-application_platform_key-application_platform_attributes" ON application_platform_attributes USING btree (key);


--
-- TOC entry 2026 (class 1259 OID 16533)
-- Name: fki_fk-platform-application_platform; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "fki_fk-platform-application_platform" ON application_platform USING btree (platform_id);


--
-- TOC entry 2023 (class 1259 OID 16517)
-- Name: fki_fk-user-user_application; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "fki_fk-user-user_application" ON user_application USING btree (user_id);


--
-- TOC entry 2045 (class 2606 OID 16713)
-- Name: fk-application-application_platform; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "fk-application-application_platform" FOREIGN KEY (application_id) REFERENCES application(id) ON UPDATE CASCADE;


--
-- TOC entry 2044 (class 2606 OID 16518)
-- Name: fk-application-user_application; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT "fk-application-user_application" FOREIGN KEY (application_id) REFERENCES application(id) ON UPDATE CASCADE;


--
-- TOC entry 2047 (class 2606 OID 16656)
-- Name: fk-application_platform-application_platform_attributes; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_attributes
    ADD CONSTRAINT "fk-application_platform-application_platform_attributes" FOREIGN KEY (application_platform_id) REFERENCES application_platform(id) ON UPDATE CASCADE;


--
-- TOC entry 2042 (class 2606 OID 16672)
-- Name: fk-application_platform-application_platform_msg_log; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_msg_log
    ADD CONSTRAINT "fk-application_platform-application_platform_msg_log" FOREIGN KEY (application_platform_id) REFERENCES application_platform(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2048 (class 2606 OID 16661)
-- Name: fk-application_platform_key-application_platform_attributes; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_attributes
    ADD CONSTRAINT "fk-application_platform_key-application_platform_attributes" FOREIGN KEY (key) REFERENCES application_platform_key(key) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2049 (class 2606 OID 16689)
-- Name: fk-device-device_attributes; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device_attributes
    ADD CONSTRAINT "fk-device-device_attributes" FOREIGN KEY (device_id) REFERENCES device(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2050 (class 2606 OID 16694)
-- Name: fk-device_key-device_attributes; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device_attributes
    ADD CONSTRAINT "fk-device_key-device_attributes" FOREIGN KEY (key) REFERENCES device_key(key) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 2046 (class 2606 OID 16718)
-- Name: fk-platform-application_platform; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "fk-platform-application_platform" FOREIGN KEY (platform_id) REFERENCES platform(id) ON UPDATE CASCADE;


--
-- TOC entry 2043 (class 2606 OID 16512)
-- Name: fk-user-user_application; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT "fk-user-user_application" FOREIGN KEY (user_id) REFERENCES "user"(id) ON UPDATE CASCADE;


--
-- TOC entry 2084 (class 0 OID 0)
-- Dependencies: 5
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-09-27 09:53:50

--
-- PostgreSQL database dump complete
--

