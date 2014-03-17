
library ieee;
use ieee.std_logic_1164.all;
use ieee.std_logic_unsigned.all;

entity RS_FF is
	generic (
		init 	: std_logic := '0';	--! Legt den initial gespeicherten Wert des FFs fest.
		delay	: time		:= 0 ns	--! Legt die Verzoegerung ab einer steigenden Taktflanke fest, ab dem der neue wert an den Ausgaengen sichtbar wird  
	);
	port  (
		clk 	: in  std_logic;	--! Taktsignal - an steigenden Flanken wird der neue Wert gespeichert
		r,s 	: in  std_logic;	--! Eingangssignale 
		q,qn	: out std_logic		--! Ausgangssignale (qn ist der invertierte Wert)
	);
end RS_FF;

architecture arch of RS_FF is

	signal d : std_logic := init;
	
begin
	sync : process(clk) is
	begin
		if rising_edge(clk) then
			if 		s='0' AND r='0' then 	d <= d;
			elsif 	s='0' AND r='1' then 	d <= '0';
			elsif 	s='1' AND r='0' then 	d <= '1';
			else							d <= 'X';
			end if;
		end if;
	end process;
	
	q <= d after delay;
	qn <= not d after delay;
end arch;

