
library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity JK_FF is
	generic (
		init 	: std_logic := '0';	--! Legt den initial gespeicherten Wert des FFs fest.
		delay	: time		:= 0 ns	--! Legt die Verzoegerung ab einer steigenden Taktflanke fest, ab dem der neue wert an den Ausgaengen sichtbar wird  
	);
	port  (
		clk 	: in  std_logic;	--! Taktsignal - an steigenden Flanken wird der neue Wert gespeichert
		j,k		: in  std_logic;	--! Eingangssignale
		q,qn	: out std_logic		--! Ausgangssignale (qn ist der invertierte Wert)
	);
end JK_FF;

architecture arch of JK_FF is

	signal d : std_logic := init;
	
begin
	sync : process(clk) is
	begin
		if rising_edge(clk) then
			if 		j='0' and k='0' then 	d <= d;
			elsif 	j='0' and k='1' then 	d <= '0';
			elsif 	j='1' and k='0' then 	d <= '1';
			elsif 	j='1' and k='1' then 	d <= not d;
			else							d <= 'X';
			end if;
		end if;
	end process;
	
	q <= d after delay;
	qn <= not d after delay;
end arch;