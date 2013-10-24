-- Function: m_create_user(character varying, character varying, character varying, character varying, character varying)

-- DROP FUNCTION m_create_user(character varying, character varying, character varying, character varying, character varying);

CREATE OR REPLACE FUNCTION m_create_user(i_name character varying, i_email character varying, i_phone character varying, i_password character varying, i_locale character varying)
  RETURNS public.user AS
$BODY$
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
END; $BODY$
  LANGUAGE plpgsql VOLATILE SECURITY DEFINER
  COST 10;
ALTER FUNCTION m_create_user(character varying, character varying, character varying, character varying, character varying)
  OWNER TO jcw_dev;