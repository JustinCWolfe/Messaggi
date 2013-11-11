--
-- PostgreSQL database dump
--

-- Dumped from database version 9.3.1
-- Dumped by pg_dump version 9.3.1
-- Started on 2013-11-11 21:13:58

SET statement_timeout = 0;
SET lock_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SET check_function_bodies = false;
SET client_min_messages = warning;

--
-- TOC entry 188 (class 3079 OID 11750)
-- Name: plpgsql; Type: EXTENSION; Schema: -; Owner: 
--

CREATE EXTENSION IF NOT EXISTS plpgsql WITH SCHEMA pg_catalog;


--
-- TOC entry 2089 (class 0 OID 0)
-- Dependencies: 188
-- Name: EXTENSION plpgsql; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION plpgsql IS 'PL/pgSQL procedural language';


SET search_path = public, pg_catalog;

SET default_tablespace = '';

SET default_with_oids = false;

--
-- TOC entry 171 (class 1259 OID 24589)
-- Name: application; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application (
    id bigint NOT NULL,
    name character varying(120) NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.application OWNER TO jcw_dev;

--
-- TOC entry 2090 (class 0 OID 0)
-- Dependencies: 171
-- Name: TABLE application; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE application IS 'Applications will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 207 (class 1255 OID 24740)
-- Name: m_create_application(character varying); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_create_application(i_name character varying) RETURNS application
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE
	i_active boolean;
	return_value public.application%ROWTYPE;
BEGIN
	i_active := true;
	
	insert into public.application (id, name, active)
	values (nextval('public.application_id_seq'), i_name, i_active);
	
	select currval('public.application_id_seq'), i_name, i_active into return_value; 
	return return_value;
END; $$;


ALTER FUNCTION public.m_create_application(i_name character varying) OWNER TO jcw_dev;

--
-- TOC entry 173 (class 1259 OID 24595)
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
-- TOC entry 2091 (class 0 OID 0)
-- Dependencies: 173
-- Name: TABLE application_platform; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE application_platform IS 'Link table between application and platform.

An application can be linked to multiple platforms.
A platform can be used across multiple applications.';


--
-- TOC entry 2092 (class 0 OID 0)
-- Dependencies: 173
-- Name: COLUMN application_platform.token; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN application_platform.token IS 'Token used by Messaggi service generated off the application platform attributes.  This token will be generated when the service consumer registers their application for the mobile platforms it supports and provides the relevant application/platform identifiers (bundle id for example).

This token will be passed for subsequent service calls.';


--
-- TOC entry 217 (class 1255 OID 24751)
-- Name: m_create_application_platform(bigint, bigint, uuid); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_create_application_platform(i_application_id bigint, i_platform_id bigint, i_token uuid) RETURNS application_platform
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform%ROWTYPE;
BEGIN
	insert into public.application_platform (id, application_id, platform_id, token)
	values (nextval('public.application_platform_id_seq'), i_application_id, i_platform_id, i_token);
	
	select currval('public.application_platform_id_seq'), i_application_id, i_platform_id, i_token into return_value; 
	return return_value;
END; $$;


ALTER FUNCTION public.m_create_application_platform(i_application_id bigint, i_platform_id bigint, i_token uuid) OWNER TO jcw_dev;

--
-- TOC entry 180 (class 1259 OID 24620)
-- Name: device; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device (
    id bigint NOT NULL,
    active boolean DEFAULT true NOT NULL
);


ALTER TABLE public.device OWNER TO jcw_dev;

--
-- TOC entry 208 (class 1255 OID 24741)
-- Name: m_create_device(); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_create_device() RETURNS device
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE
	i_active boolean;
	return_value public.device%ROWTYPE;
BEGIN
	i_active := true;
	
	insert into public.device (id, active)
	values (nextval('public.device_id_seq'), i_active);
	
	select currval('public.device_id_seq'), i_active into return_value; 
	return return_value;
END; $$;


ALTER FUNCTION public.m_create_device() OWNER TO jcw_dev;

--
-- TOC entry 184 (class 1259 OID 24638)
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
-- TOC entry 2093 (class 0 OID 0)
-- Dependencies: 184
-- Name: TABLE platform; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE platform IS 'Platforms will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 206 (class 1255 OID 24739)
-- Name: m_create_platform(character varying, character varying); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_create_platform(i_name character varying, i_service_name character varying) RETURNS platform
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE
	i_active boolean;
	return_value public.platform%ROWTYPE;
BEGIN
	i_active := true;
	
	insert into public.platform (id, name, service_name, active)
	values (nextval('public.platform_id_seq'), i_name, i_service_name, i_active);
	
	select currval('public.platform_id_seq'), i_name, i_service_name, i_active into return_value; 
	return return_value;
END; $$;


ALTER FUNCTION public.m_create_platform(i_name character varying, i_service_name character varying) OWNER TO jcw_dev;

--
-- TOC entry 170 (class 1259 OID 24577)
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
-- TOC entry 2094 (class 0 OID 0)
-- Dependencies: 170
-- Name: TABLE "user"; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE "user" IS 'Users will not be deleted from the system but instead their active field will be set to false.';


--
-- TOC entry 2095 (class 0 OID 0)
-- Dependencies: 170
-- Name: COLUMN "user".phone; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".phone IS 'Phone number as entered by the user.';


--
-- TOC entry 2096 (class 0 OID 0)
-- Dependencies: 170
-- Name: COLUMN "user".phone_parsed; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".phone_parsed IS 'User entered phone number with all non-numeric data stripped. 

* used for indexing purposes';


--
-- TOC entry 2097 (class 0 OID 0)
-- Dependencies: 170
-- Name: COLUMN "user".password; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN "user".password IS 'SHA-512 salted password.';


--
-- TOC entry 201 (class 1255 OID 24584)
-- Name: m_create_user(character varying, character varying, character varying, character varying, character varying); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_create_user(i_name character varying, i_email character varying, i_phone character varying, i_password character varying, i_locale character varying) RETURNS "user"
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE
	i_active boolean;
	i_phone_parsed user.phone_parsed%TYPE;
	return_value public.user%ROWTYPE;
BEGIN
	i_active := true;
	i_phone_parsed := regexp_replace(i_phone, '[^0-9]', '', 'g');
	
	insert into public.user (id, name, email, phone, phone_parsed, password, locale, active)
	values (nextval('public.user_id_seq'), i_name, i_email, i_phone, i_phone_parsed, i_password, i_locale, i_active);
	
	select currval('public.user_id_seq'), i_name, i_email, i_phone, i_phone_parsed, i_password, i_locale, i_active into return_value; 
	return return_value;
END; $$;


ALTER FUNCTION public.m_create_user(i_name character varying, i_email character varying, i_phone character varying, i_password character varying, i_locale character varying) OWNER TO jcw_dev;

--
-- TOC entry 186 (class 1259 OID 24650)
-- Name: user_application; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE user_application (
    user_id bigint NOT NULL,
    application_id bigint NOT NULL
);


ALTER TABLE public.user_application OWNER TO jcw_dev;

--
-- TOC entry 2098 (class 0 OID 0)
-- Dependencies: 186
-- Name: TABLE user_application; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON TABLE user_application IS 'Link table between user and application.

A user can manage multiple applications but an application can only be managed by a single user.';


--
-- TOC entry 218 (class 1255 OID 24752)
-- Name: m_create_user_application(bigint, bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_create_user_application(i_user_id bigint, i_application_id bigint) RETURNS user_application
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.user_application%ROWTYPE;
BEGIN
	insert into public.user_application (user_id, application_id)
	values (i_user_id, i_application_id);
	
	select i_user_id, i_application_id into return_value; 
	return return_value;
END; $$;


ALTER FUNCTION public.m_create_user_application(i_user_id bigint, i_application_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 216 (class 1255 OID 24748)
-- Name: m_get_application_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_by_id(i_id bigint) RETURNS SETOF application
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application.id, public.application.name, public.application.active	
	from public.application
	where public.application.id = i_id and public.application.active = true
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 222 (class 1255 OID 24758)
-- Name: m_get_application_platform_by_application_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_by_application_id(i_application_id bigint) RETURNS SETOF application_platform
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform.id, public.application_platform.application_id, public.application_platform.platform_id, 
		public.application_platform.token
	from public.application_platform
	where public.application_platform.application_id = i_application_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_by_application_id(i_application_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 224 (class 1255 OID 24760)
-- Name: m_get_application_platform_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_by_id(i_id bigint) RETURNS SETOF application_platform
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform.id, public.application_platform.application_id, public.application_platform.platform_id, 
		public.application_platform.token
	from public.application_platform
	where public.application_platform.application_id = i_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 223 (class 1255 OID 24759)
-- Name: m_get_application_platform_by_platform_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_application_platform_by_platform_id(i_platform_id bigint) RETURNS SETOF application_platform
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.application_platform%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.application_platform.id, public.application_platform.application_id, public.application_platform.platform_id, 
		public.application_platform.token
	from public.application_platform
	where public.application_platform.application_id = i_platform_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_application_platform_by_platform_id(i_platform_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 215 (class 1255 OID 24749)
-- Name: m_get_device_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_device_by_id(i_id bigint) RETURNS SETOF device
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.device%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.device.id, public.device.active	
	from public.device
	where public.device.id = i_id and public.device.active = true
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_device_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 219 (class 1255 OID 24750)
-- Name: m_get_platform_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_platform_by_id(i_id bigint) RETURNS SETOF platform
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.platform%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.platform.id, public.platform.name, public.platform.service_name, public.platform.active	
	from public.platform
	where public.platform.id = i_id and public.platform.active = true
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_platform_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 220 (class 1255 OID 24756)
-- Name: m_get_user_application_by_application_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_user_application_by_application_id(i_application_id bigint) RETURNS SETOF user_application
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.user_application%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.user_application.user_id, public.user_application.application_id	
	from public.user_application
	where public.user_application.application_id = i_application_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_user_application_by_application_id(i_application_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 221 (class 1255 OID 24757)
-- Name: m_get_user_application_by_user_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_user_application_by_user_id(i_user_id bigint) RETURNS SETOF user_application
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.user_application%ROWTYPE;
BEGIN
	FOR return_value IN
	select public.user_application.user_id, public.user_application.application_id	
	from public.user_application
	where public.user_application.user_id = i_user_id
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_user_application_by_user_id(i_user_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 202 (class 1255 OID 24585)
-- Name: m_get_user_by_email(character varying); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_user_by_email(i_email character varying) RETURNS SETOF "user"
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.user%ROWTYPE;
BEGIN
	-- Note that we explicitly don't include the password
	FOR return_value IN
	select public.user.id, public.user.name, public.user.email, public.user.phone, public.user.phone_parsed, 
		null, public.user.locale, public.user.active	
	from public.user
	where public.user.email = i_email and public.user.active = true
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_user_by_email(i_email character varying) OWNER TO jcw_dev;

--
-- TOC entry 203 (class 1255 OID 24586)
-- Name: m_get_user_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_get_user_by_id(i_id bigint) RETURNS SETOF "user"
    LANGUAGE plpgsql IMMUTABLE SECURITY DEFINER COST 10
    AS $$
DECLARE
	return_value public.user%ROWTYPE;
BEGIN
	-- Note that we explicitly don't include the password
	FOR return_value IN
	select public.user.id, public.user.name, public.user.email, public.user.phone, public.user.phone_parsed, 
		null, public.user.locale, public.user.active	
	from public.user
	where public.user.id = i_id and public.user.active = true
	LOOP
		-- return the current row of select
		RETURN NEXT return_value;
	END LOOP;
	RETURN;
END; $$;


ALTER FUNCTION public.m_get_user_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 209 (class 1255 OID 24742)
-- Name: m_inactivate_application_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_inactivate_application_by_id(i_id bigint) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE		
BEGIN	
	update public.application set active = false
	where id = i_id;
END; $$;


ALTER FUNCTION public.m_inactivate_application_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 210 (class 1255 OID 24743)
-- Name: m_inactivate_device_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_inactivate_device_by_id(i_id bigint) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE		
BEGIN	
	update public.device set active = false
	where id = i_id;
END; $$;


ALTER FUNCTION public.m_inactivate_device_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 211 (class 1255 OID 24744)
-- Name: m_inactivate_platform_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_inactivate_platform_by_id(i_id bigint) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE		
BEGIN	
	update public.platform set active = false
	where id = i_id;
END; $$;


ALTER FUNCTION public.m_inactivate_platform_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 204 (class 1255 OID 24587)
-- Name: m_inactivate_user_by_id(bigint); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_inactivate_user_by_id(i_id bigint) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE		
BEGIN	
	update public.user set active = false
	where id = i_id;
END; $$;


ALTER FUNCTION public.m_inactivate_user_by_id(i_id bigint) OWNER TO jcw_dev;

--
-- TOC entry 212 (class 1255 OID 24745)
-- Name: m_update_application(bigint, character varying, boolean); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_update_application(i_id bigint, i_name character varying, i_active boolean) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE	
BEGIN	
	update public.application set name = i_name, active = i_active
	where id = i_id;
END; $$;


ALTER FUNCTION public.m_update_application(i_id bigint, i_name character varying, i_active boolean) OWNER TO jcw_dev;

--
-- TOC entry 213 (class 1255 OID 24746)
-- Name: m_update_device(bigint, boolean); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_update_device(i_id bigint, i_active boolean) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE	
BEGIN	
	update public.device set active = i_active
	where id = i_id;
END; $$;


ALTER FUNCTION public.m_update_device(i_id bigint, i_active boolean) OWNER TO jcw_dev;

--
-- TOC entry 214 (class 1255 OID 24747)
-- Name: m_update_platform(bigint, character varying, character varying, boolean); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_update_platform(i_id bigint, i_name character varying, i_service_name character varying, i_active boolean) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE	
BEGIN	
	update public.platform set name = i_name, service_name = i_service_name, active = i_active
	where id = i_id;
END; $$;


ALTER FUNCTION public.m_update_platform(i_id bigint, i_name character varying, i_service_name character varying, i_active boolean) OWNER TO jcw_dev;

--
-- TOC entry 205 (class 1255 OID 24588)
-- Name: m_update_user(bigint, character varying, character varying, character varying, character varying, character varying, boolean); Type: FUNCTION; Schema: public; Owner: jcw_dev
--

CREATE FUNCTION m_update_user(i_id bigint, i_name character varying, i_email character varying, i_phone character varying, i_password character varying, i_locale character varying, i_active boolean) RETURNS void
    LANGUAGE plpgsql SECURITY DEFINER COST 10
    AS $$
DECLARE	
	i_phone_parsed user.phone_parsed%TYPE;
BEGIN	
	i_phone_parsed := regexp_replace(i_phone, '[^0-9]', '', 'g');

	update public.user set name = i_name, email = i_email, phone = i_phone, phone_parsed = i_phone_parsed, 
		password = i_password, locale = i_locale, active = i_active
	where id = i_id;
END; $$;


ALTER FUNCTION public.m_update_user(i_id bigint, i_name character varying, i_email character varying, i_phone character varying, i_password character varying, i_locale character varying, i_active boolean) OWNER TO jcw_dev;

--
-- TOC entry 172 (class 1259 OID 24593)
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
-- TOC entry 2099 (class 0 OID 0)
-- Dependencies: 172
-- Name: application_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_id_seq OWNED BY application.id;


--
-- TOC entry 174 (class 1259 OID 24598)
-- Name: application_platform_attribute; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_attribute (
    application_platform_id bigint NOT NULL,
    application_platform_key_key character varying(20) NOT NULL,
    value character varying(1024) NOT NULL
);


ALTER TABLE public.application_platform_attribute OWNER TO jcw_dev;

--
-- TOC entry 175 (class 1259 OID 24604)
-- Name: application_platform_device; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_device (
    application_platform_id bigint NOT NULL,
    device_id bigint NOT NULL
);


ALTER TABLE public.application_platform_device OWNER TO jcw_dev;

--
-- TOC entry 176 (class 1259 OID 24607)
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
-- TOC entry 2100 (class 0 OID 0)
-- Dependencies: 176
-- Name: application_platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_platform_id_seq OWNED BY application_platform.id;


--
-- TOC entry 177 (class 1259 OID 24609)
-- Name: application_platform_key; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE application_platform_key (
    key character varying(20) NOT NULL,
    description character varying(1024) NOT NULL
);


ALTER TABLE public.application_platform_key OWNER TO jcw_dev;

--
-- TOC entry 178 (class 1259 OID 24615)
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
-- TOC entry 2101 (class 0 OID 0)
-- Dependencies: 178
-- Name: COLUMN application_platform_msg_log.date; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN application_platform_msg_log.date IS 'Note that all timezones are stored in UTC format.';


--
-- TOC entry 179 (class 1259 OID 24618)
-- Name: application_platform_msg_log_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE application_platform_msg_log_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.application_platform_msg_log_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2102 (class 0 OID 0)
-- Dependencies: 179
-- Name: application_platform_msg_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE application_platform_msg_log_id_seq OWNED BY application_platform_msg_log.id;


--
-- TOC entry 181 (class 1259 OID 24624)
-- Name: device_attribute; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device_attribute (
    device_id bigint NOT NULL,
    device_key_key character varying(20) NOT NULL,
    value character varying(1024) NOT NULL
);


ALTER TABLE public.device_attribute OWNER TO jcw_dev;

--
-- TOC entry 2103 (class 0 OID 0)
-- Dependencies: 181
-- Name: COLUMN device_attribute.value; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN device_attribute.value IS 'Stores the value for the associated platform specific device identifier key.

For example:

An iOS phone will have a device token so the key will be token, the value will be XYZ.
An Android phone will have an device id so the key will be device_id, the value will be 123.';


--
-- TOC entry 182 (class 1259 OID 24630)
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
-- TOC entry 2104 (class 0 OID 0)
-- Dependencies: 182
-- Name: device_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE device_id_seq OWNED BY device.id;


--
-- TOC entry 183 (class 1259 OID 24632)
-- Name: device_key; Type: TABLE; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE TABLE device_key (
    key character varying(20) NOT NULL,
    description character varying(1024) NOT NULL
);


ALTER TABLE public.device_key OWNER TO jcw_dev;

--
-- TOC entry 2105 (class 0 OID 0)
-- Dependencies: 183
-- Name: COLUMN device_key.key; Type: COMMENT; Schema: public; Owner: jcw_dev
--

COMMENT ON COLUMN device_key.key IS 'Stores the key for the platform specific device identifier.

For example:

An iOS phone will have a device token so the key will be token.
An Android phone will have an device id so the key will be device_id.
';


--
-- TOC entry 185 (class 1259 OID 24642)
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
-- TOC entry 2106 (class 0 OID 0)
-- Dependencies: 185
-- Name: platform_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE platform_id_seq OWNED BY platform.id;


--
-- TOC entry 187 (class 1259 OID 24653)
-- Name: user_id_seq; Type: SEQUENCE; Schema: public; Owner: jcw_dev
--

CREATE SEQUENCE user_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER TABLE public.user_id_seq OWNER TO jcw_dev;

--
-- TOC entry 2107 (class 0 OID 0)
-- Dependencies: 187
-- Name: user_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: jcw_dev
--

ALTER SEQUENCE user_id_seq OWNED BY "user".id;


--
-- TOC entry 1909 (class 2604 OID 24655)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application ALTER COLUMN id SET DEFAULT nextval('application_id_seq'::regclass);


--
-- TOC entry 1910 (class 2604 OID 24656)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform ALTER COLUMN id SET DEFAULT nextval('application_platform_id_seq'::regclass);


--
-- TOC entry 1911 (class 2604 OID 24657)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_msg_log ALTER COLUMN id SET DEFAULT nextval('application_platform_msg_log_id_seq'::regclass);


--
-- TOC entry 1913 (class 2604 OID 24658)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device ALTER COLUMN id SET DEFAULT nextval('device_id_seq'::regclass);


--
-- TOC entry 1915 (class 2604 OID 24659)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY platform ALTER COLUMN id SET DEFAULT nextval('platform_id_seq'::regclass);


--
-- TOC entry 1907 (class 2604 OID 24660)
-- Name: id; Type: DEFAULT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY "user" ALTER COLUMN id SET DEFAULT nextval('user_id_seq'::regclass);


--
-- TOC entry 2065 (class 0 OID 24589)
-- Dependencies: 171
-- Data for Name: application; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application (id, name, active) FROM stdin;
\.


--
-- TOC entry 2108 (class 0 OID 0)
-- Dependencies: 172
-- Name: application_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_id_seq', 1, false);


--
-- TOC entry 2067 (class 0 OID 24595)
-- Dependencies: 173
-- Data for Name: application_platform; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform (id, application_id, platform_id, token) FROM stdin;
\.


--
-- TOC entry 2068 (class 0 OID 24598)
-- Dependencies: 174
-- Data for Name: application_platform_attribute; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_attribute (application_platform_id, application_platform_key_key, value) FROM stdin;
\.


--
-- TOC entry 2069 (class 0 OID 24604)
-- Dependencies: 175
-- Data for Name: application_platform_device; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_device (application_platform_id, device_id) FROM stdin;
\.


--
-- TOC entry 2109 (class 0 OID 0)
-- Dependencies: 176
-- Name: application_platform_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_platform_id_seq', 1, false);


--
-- TOC entry 2071 (class 0 OID 24609)
-- Dependencies: 177
-- Data for Name: application_platform_key; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_key (key, description) FROM stdin;
\.


--
-- TOC entry 2072 (class 0 OID 24615)
-- Dependencies: 178
-- Data for Name: application_platform_msg_log; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY application_platform_msg_log (id, application_platform_id, date, msg_count) FROM stdin;
\.


--
-- TOC entry 2110 (class 0 OID 0)
-- Dependencies: 179
-- Name: application_platform_msg_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('application_platform_msg_log_id_seq', 1, false);


--
-- TOC entry 2074 (class 0 OID 24620)
-- Dependencies: 180
-- Data for Name: device; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device (id, active) FROM stdin;
\.


--
-- TOC entry 2075 (class 0 OID 24624)
-- Dependencies: 181
-- Data for Name: device_attribute; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device_attribute (device_id, device_key_key, value) FROM stdin;
\.


--
-- TOC entry 2111 (class 0 OID 0)
-- Dependencies: 182
-- Name: device_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('device_id_seq', 1, false);


--
-- TOC entry 2077 (class 0 OID 24632)
-- Dependencies: 183
-- Data for Name: device_key; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY device_key (key, description) FROM stdin;
\.


--
-- TOC entry 2078 (class 0 OID 24638)
-- Dependencies: 184
-- Data for Name: platform; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY platform (id, name, service_name, active) FROM stdin;
\.


--
-- TOC entry 2112 (class 0 OID 0)
-- Dependencies: 185
-- Name: platform_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('platform_id_seq', 1, false);


--
-- TOC entry 2064 (class 0 OID 24577)
-- Dependencies: 170
-- Data for Name: user; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY "user" (id, name, email, phone, phone_parsed, password, locale, active) FROM stdin;
2	Justin C. Wolfe	jcw_222@yahoo.com	617-549-2403	6175492403	password1	US-en	t
3	Armen Solakhyan	asolakhyan@hotmail.com	(617) 335-9866	6173359866	password2	US-en	t
155	User2376969	User2376969@yahoo.com	961-489-3471	9614893471	a6561220-faf9-4378-9305-b4dc1e3e33ff	en_US	f
740	User6319851	User6319851@yahoo.com	417-732-2488	4177322488	8c0e7a0d-43c6-49fd-8947-96d128c62bc7	en_US	f
17	User 6016440	User 6016440@yahoo.com	{0}-{1}-{2}	012	5c260daf-ab28-4476-9a1a-777cd3e4b893	en-US	t
18	User 5576972	User 5576972@yahoo.com	{0}-{1}-{2}	012	df191502-2c1d-4595-bbd5-17b5033f487d	en-US	t
19	User 5716297	User 5716297@yahoo.com	{0}-{1}-{2}	012	d57dc257-2d54-4ce1-982e-e02f6b4b0e13	it-IT	t
20	User 5552328	User 5552328@yahoo.com	424-428-8862	4244288862	18e3e942-231d-43c5-8dae-7a1c3b161d30	en-US	t
21	User 1518687	User 1518687@yahoo.com	854-736-2878	8547362878	555b5ace-0fdd-4ba5-b7f1-1d3be09dfce2	en-US	t
22	User 6150436	User 6150436@yahoo.com	454-267-1718	4542671718	cbaffe4c-6bad-40c6-97f3-1f8f241d1224	it-IT	t
24	User 7897040	User 7897040@yahoo.com	564-912-2767	5649122767	d22e6fb8-1a78-469d-8f98-73ff34e29b4c	en-US	t
25	User 1765148	User 1765148@yahoo.com	788-341-8144	7883418144	c038ee3b-51b4-4a6d-a5a8-dc0fc9165bb4	en-US	t
26	User 5180834	User 5180834@yahoo.com	231-244-5769	2312445769	c1e791a7-341e-4e8a-ab0f-393e0b74fe61	it-IT	t
163	User8531362	User8531362@yahoo.com	858-963-8012	8589638012	9f026f84-0148-4975-b4bf-6dd9221b098c	en_US	f
369	User603086	User603086@yahoo.com	684-488-722	684488722	74f45575-323a-49a0-9e60-b0e104ff80ba	en_US	t
30	User 2926297	User 2926297@yahoo.com	751-968-2709	7519682709	0a3fad32-7b36-46aa-b84f-e11bfbde2d5f	en-US	t
31	User9717055	User9717055@yahoo.com	583-209-9697	5832099697	789ec1ad-6a91-42ab-869c-6e9f54a169e2	en-US	t
32	User2856117	User2856117@yahoo.com	421-360-3224	4213603224	8cf6927f-30a1-4a89-b64c-afa930c83d18	it-IT	t
95	User7214948	User7214948@yahoo.com	807-915-9104	8079159104	e69d2ccc-d375-4890-a9d5-8748a64cc1ba	en_US	f
36	User7864434	User7864434@yahoo.com	165-536-3448	1655363448	f89901e2-aa5a-4357-864a-4906464bc78a	en-US	t
96	User7955275	User7955275@yahoo.com	435-552-9801	4355529801	def8c6d8-65a9-48d7-9c6d-4339199a7869	it_IT	f
555	User2855188	User2855188@yahoo.com	829-750-8942	8297508942	37975e60-c5bf-4dc6-8174-c15d96c19395	en_US	f
103	User6897469	User6897469@yahoo.com	746-712-8006	7467128006	70eac9f5-e3a9-4ede-9cb5-f729daa2c483	en_US	f
104	User8473376	User8473376@yahoo.com	989-136-1068	9891361068	4587f836-045d-45bc-bb0b-133f955f3eb0	it_IT	f
632	User8435458	User8435458@yahoo.com	216-607-6014	2166076014	1b20f75b-d028-4629-8cd5-29d898abf02e	en_US	t
244	User3900595	User3900595@yahoo.com	550-584-6086	5505846086	3c16d262-9c9c-40d1-8bb0-47c458590af3	en_US	f
385	User1214177	User1214177@yahoo.com	206-484-3276	2064843276	f9e56792-06c9-45a4-90e0-a9fe924a9ff5	en_US	t
246	User3267501	User3267501@yahoo.com	164-929-7013	1649297013	02df2d3d-0275-478e-85f0-49cfefaf8548	en_US	f
248	User8914541	User8914541@yahoo.com	174-635-67	17463567	4cb0a2aa-de3a-4873-8e17-644dd0b07e35	en_US	f
568	User2383456	User2383456@yahoo.com	575-100-9725	5751009725	a9f3e8c7-353e-431f-9d56-622252d06332	en_US	f
396	User2602958	User2602958@yahoo.com	814-100-3253	8141003253	ba63acc3-9e51-43fe-a514-05e9b6000579	en_US	t
726	User7753927	User7753927@yahoo.com	832-297-5919	8322975919	7ab339e3-412e-4453-be9b-172621251a82	en_US	f
600	User2157175	User2157175@yahoo.com	113-351-1974	1133511974	d47f601a-27c0-47cc-9f4b-379275f84271	en_US	f
299	User1137189	User1137189@yahoo.com	611-632-3987	6116323987	818e4509-54e4-4863-aea3-b9835a3c5d3a	en_US	t
300	User6555014	User6555014@yahoo.com	860-892-4403	8608924403	215c5958-87af-4cfa-bc47-2841b9a08ed2	it_IT	t
\.


--
-- TOC entry 2080 (class 0 OID 24650)
-- Dependencies: 186
-- Data for Name: user_application; Type: TABLE DATA; Schema: public; Owner: jcw_dev
--

COPY user_application (user_id, application_id) FROM stdin;
\.


--
-- TOC entry 2113 (class 0 OID 0)
-- Dependencies: 187
-- Name: user_id_seq; Type: SEQUENCE SET; Schema: public; Owner: jcw_dev
--

SELECT pg_catalog.setval('user_id_seq', 760, true);


--
-- TOC entry 1922 (class 2606 OID 24662)
-- Name: application-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application
    ADD CONSTRAINT "application-pkey" PRIMARY KEY (id);


--
-- TOC entry 1924 (class 2606 OID 24664)
-- Name: application_platform-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "application_platform-pkey" PRIMARY KEY (id);


--
-- TOC entry 1928 (class 2606 OID 24666)
-- Name: application_platform_attribute-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_attribute
    ADD CONSTRAINT "application_platform_attribute-pkey" PRIMARY KEY (application_platform_id, application_platform_key_key);


--
-- TOC entry 1930 (class 2606 OID 24668)
-- Name: application_platform_device-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_device
    ADD CONSTRAINT "application_platform_device-pkey" PRIMARY KEY (application_platform_id, device_id);


--
-- TOC entry 1932 (class 2606 OID 24670)
-- Name: application_platform_key-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_key
    ADD CONSTRAINT "application_platform_key-pkey" PRIMARY KEY (key);


--
-- TOC entry 1937 (class 2606 OID 24672)
-- Name: device-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device
    ADD CONSTRAINT "device-pkey" PRIMARY KEY (id);


--
-- TOC entry 1940 (class 2606 OID 24674)
-- Name: device_attribute-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device_attribute
    ADD CONSTRAINT "device_attribute-pkey" PRIMARY KEY (device_id, device_key_key);


--
-- TOC entry 1942 (class 2606 OID 24676)
-- Name: device_key-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY device_key
    ADD CONSTRAINT "device_key-pkey" PRIMARY KEY (key);


--
-- TOC entry 1944 (class 2606 OID 24678)
-- Name: platform-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY platform
    ADD CONSTRAINT "platform-pkey" PRIMARY KEY (id);


--
-- TOC entry 1917 (class 2606 OID 24680)
-- Name: user-email-key; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT "user-email-key" UNIQUE (email);


--
-- TOC entry 1920 (class 2606 OID 24682)
-- Name: user-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY "user"
    ADD CONSTRAINT "user-pkey" PRIMARY KEY (id);


--
-- TOC entry 1946 (class 2606 OID 24684)
-- Name: user_application-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT "user_application-pkey" PRIMARY KEY (application_id);


--
-- TOC entry 1935 (class 2606 OID 24686)
-- Name: user_msg_log-pkey; Type: CONSTRAINT; Schema: public; Owner: jcw_dev; Tablespace: 
--

ALTER TABLE ONLY application_platform_msg_log
    ADD CONSTRAINT "user_msg_log-pkey" PRIMARY KEY (id);


--
-- TOC entry 1925 (class 1259 OID 24687)
-- Name: application_platform-platform_id-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "application_platform-platform_id-idx" ON application_platform USING btree (platform_id);


--
-- TOC entry 1926 (class 1259 OID 24688)
-- Name: application_platform_attribute-application_platform_key_key-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "application_platform_attribute-application_platform_key_key-idx" ON application_platform_attribute USING btree (application_platform_key_key);


--
-- TOC entry 1933 (class 1259 OID 24689)
-- Name: application_platform_msg_log-application_platform_id-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "application_platform_msg_log-application_platform_id-idx" ON application_platform_msg_log USING btree (application_platform_id);


--
-- TOC entry 1938 (class 1259 OID 24690)
-- Name: device_attribute-device_key_key-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "device_attribute-device_key_key-idx" ON device_attribute USING btree (device_key_key);


--
-- TOC entry 1918 (class 1259 OID 24691)
-- Name: user-phone_parsed-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "user-phone_parsed-idx" ON "user" USING btree (phone_parsed);


--
-- TOC entry 1947 (class 1259 OID 24692)
-- Name: user_application-user_id-idx; Type: INDEX; Schema: public; Owner: jcw_dev; Tablespace: 
--

CREATE INDEX "user_application-user_id-idx" ON user_application USING btree (user_id);


--
-- TOC entry 1948 (class 2606 OID 24693)
-- Name: application_platform-application_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "application_platform-application_id-fkey" FOREIGN KEY (application_id) REFERENCES application(id) ON UPDATE CASCADE;


--
-- TOC entry 1949 (class 2606 OID 24698)
-- Name: application_platform-platform_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform
    ADD CONSTRAINT "application_platform-platform_id-fkey" FOREIGN KEY (platform_id) REFERENCES platform(id) ON UPDATE CASCADE;


--
-- TOC entry 1950 (class 2606 OID 24703)
-- Name: application_platform_attribute-ap_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_attribute
    ADD CONSTRAINT "application_platform_attribute-ap_id-fkey" FOREIGN KEY (application_platform_id) REFERENCES application_platform(id) ON UPDATE CASCADE;


--
-- TOC entry 1951 (class 2606 OID 24708)
-- Name: application_platform_attribute-apk_key-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_attribute
    ADD CONSTRAINT "application_platform_attribute-apk_key-fkey" FOREIGN KEY (application_platform_key_key) REFERENCES application_platform_key(key) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1952 (class 2606 OID 24713)
-- Name: application_platform_msg_log-application_platform_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY application_platform_msg_log
    ADD CONSTRAINT "application_platform_msg_log-application_platform_id-fkey" FOREIGN KEY (application_platform_id) REFERENCES application_platform(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1953 (class 2606 OID 24718)
-- Name: device_attribute-device_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device_attribute
    ADD CONSTRAINT "device_attribute-device_id-fkey" FOREIGN KEY (device_id) REFERENCES device(id) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1954 (class 2606 OID 24723)
-- Name: device_attribute-device_key_key-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY device_attribute
    ADD CONSTRAINT "device_attribute-device_key_key-fkey" FOREIGN KEY (device_key_key) REFERENCES device_key(key) ON UPDATE CASCADE ON DELETE CASCADE;


--
-- TOC entry 1955 (class 2606 OID 24728)
-- Name: user_application-application_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT "user_application-application_id-fkey" FOREIGN KEY (application_id) REFERENCES application(id) ON UPDATE CASCADE;


--
-- TOC entry 1956 (class 2606 OID 24733)
-- Name: user_application-user_id-fkey; Type: FK CONSTRAINT; Schema: public; Owner: jcw_dev
--

ALTER TABLE ONLY user_application
    ADD CONSTRAINT "user_application-user_id-fkey" FOREIGN KEY (user_id) REFERENCES "user"(id) ON UPDATE CASCADE;


--
-- TOC entry 2088 (class 0 OID 0)
-- Dependencies: 6
-- Name: public; Type: ACL; Schema: -; Owner: postgres
--

REVOKE ALL ON SCHEMA public FROM PUBLIC;
REVOKE ALL ON SCHEMA public FROM postgres;
GRANT ALL ON SCHEMA public TO postgres;
GRANT ALL ON SCHEMA public TO PUBLIC;


-- Completed on 2013-11-11 21:14:00

--
-- PostgreSQL database dump complete
--

