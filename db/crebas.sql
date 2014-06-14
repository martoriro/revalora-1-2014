
INSERT INTO botis_revalora.ACCOUNT (RUT, ACCESS, ADDRESS, BIRTHDATE, EMAIL, EMAILPASSWORD, FIRSTNAME, GENDER, LASTNAME, PASSWORD, PHONE, `POSITION`, ACCOUNTTYPE_ID) 
	VALUES ('171885701', true, 'usach', '1989-02-02', 'mario@requies.cl', '1234', 'Mario', 'Masculino', 'López', '18496197305510df22af763507c99219ea08e08414383ae1abf1cb156d961a03', '12345678', 'Le programo', 2);

drop view if exists accounts;
create view accounts as 
select ACCOUNT.rut, ACCOUNT.password, ACCOUNTTYPE.name as rolename from
ACCOUNT inner join ACCOUNTTYPE on
ACCOUNT.accounttype_id = ACCOUNTTYPE.id;